package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Empleado;
import modelo.Incidencia;
import modelo.enums.Tipo;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import static org.elasticsearch.rest.RestRequest.request;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ManagerDao {

    private static ManagerDao managerDao;
    RestHighLevelClient client;

    private ManagerDao() {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    public static ManagerDao getManagerDao() {
        if (managerDao == null) {
            managerDao = new ManagerDao();
        }
        return managerDao;
    }

    public boolean index(IndexRequest indexRequest) throws ElasticsearchStatusException {
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            /*if (indexResponse.equals(false)) {
                return true;
            }*/
            return true;
        } catch (IOException | ElasticsearchStatusException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public Map<String, DocumentField> get(GetRequest getRequest) throws Exception {
        String s;
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            s = getResponse.getSourceAsString();
            //client = ESclient.getInstant();
            //client.get
            /*GetResponse response = client.prepareGet("user", "tenth", "1")
                    .setOperationThreaded(false)
                    .get();*/
            if (getResponse != null) {
                //Map<String,DocumentField> FieldsMap
                return getResponse.getFields();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    //public Empleado getEmpleado(GetFieldMappingsRequest  getRequest) throws Exception {
    public Empleado getEmpleado(SearchRequest getRequest) throws Exception {
        SearchResponse response = client.search(getRequest, RequestOptions.DEFAULT);

        //GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> e = response.getHits().getAt(0).getSourceAsMap();
        return new Empleado(e.get(""));
    }

    public int getTryEmpleado(SearchRequest searchRequest) throws Exception {
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] results = response.getHits().getHits();
        int maxId = 0;
        for (SearchHit h : results) {
            int actualId = Integer.parseInt(h.getId());
            if (actualId > maxId) {
                maxId = actualId;
            }
        }
        return maxId;
    }

    public List<Incidencia> getAllIncidents() {
        List<Incidencia> incidencias = new ArrayList<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("incidents");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> incidents = hit.getSourceAsMap();
                LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);

                Incidencia i = new Incidencia(date, incidents.get("origin").toString(),
                        incidents.get("destination").toString(), incidents.get("detail").toString(), getPlatoType(incidents.get("type").toString()));
                incidencias.add(i);
                System.out.println("source: " + sourceAsString);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return incidencias;
    }

    public boolean checkUserExists(String userName) {
        GetRequest getRequest = new GetRequest("users", "2");
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public void update(Empleado e) {
        try {
            HashMap<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("user", e.getUsuario());
            jsonMap.put("name", e.getNombre());
            jsonMap.put("pass", e.getPassword());
            jsonMap.put("surname", e.getApellidos());
            jsonMap.put("phone", e.getTelefono());
            jsonMap.put("dni", e.getDni());

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> usuarios = hit.getSourceAsMap();
                if (usuarios.get("user").toString().equals("testUsuario")) {
                    UpdateRequest updateRequest = new UpdateRequest("users", hit.getId())
                            .doc(jsonMap);
                    UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
                } else {
                    System.out.println(usuarios.get("user").toString());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Tipo getPlatoType(String tipo) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return Tipo.URGENTE;
            case "NORMAL":
                return Tipo.NORMAL;
        }
        return null;
    }

    public void delete(Empleado e) {
        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> usuarios = hit.getSourceAsMap();
                if (usuarios.get("user").toString().equals("pepe")) {
                    DeleteRequest deleterequest = new DeleteRequest("users", hit.getId());
                    DeleteResponse deleteResponse = client.delete(deleterequest, RequestOptions.DEFAULT);
                }
            }
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.CONFLICT) {
                System.out.println("hola");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Incidencia getIncidentByID(int id) {
        Incidencia i = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("incidents");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> incidents = hit.getSourceAsMap();
                if (hit.getId().equals(String.valueOf(id))) {
                    LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);
                    i = new Incidencia(date, incidents.get("origin").toString(),
                            incidents.get("destination").toString(), incidents.get("detail").toString(), getPlatoType(incidents.get("type").toString()));
                    return i;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return i;
    }

    public void close() {
        try {
            client.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
