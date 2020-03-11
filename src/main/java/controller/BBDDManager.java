package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Empleado;
import modelo.Incidencia;
import modelo.enums.Tipo;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;

public class BBDDManager {

    private static BBDDManager managerDao;
    RestHighLevelClient client;

    private BBDDManager() {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    public static BBDDManager getManagerDao() {
        if (managerDao == null) {
            managerDao = new BBDDManager();
        }
        return managerDao;
    }

    public boolean index(IndexRequest indexRequest) throws ElasticsearchStatusException {
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException | ElasticsearchStatusException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    //public Empleado getEmpleado(GetFieldMappingsRequest  getRequest) throws Exception {
    public Empleado getEmpleado(SearchRequest getRequest) throws Exception {
        SearchResponse response = client.search(getRequest, RequestOptions.DEFAULT);

        //GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> sourceAsMap = response.getHits().getAt(0).getSourceAsMap();
        Empleado e = new Empleado((String) sourceAsMap.get("user"),
                (String) sourceAsMap.get("name"), (String) sourceAsMap.get("surname"),
                (String) sourceAsMap.get("phone"), (String) sourceAsMap.get("dni"),
                (String) sourceAsMap.get("pass"));
        return e;
    }

    public int getID(SearchRequest searchRequest) throws Exception {
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
                Map<String, Object> incidents = hit.getSourceAsMap();
                LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);

                Incidencia i = new Incidencia(date, incidents.get("origin").toString(),
                        incidents.get("destination").toString(), incidents.get("detail").toString(), getIncidentType(incidents.get("type").toString()));
                incidencias.add(i);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return incidencias;
    }

    public List<Incidencia> getIncidentsByOrigin(SearchRequest searchRequest, String e) {
        List<Incidencia> incidencias = new ArrayList<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                Map<String, Object> incidents = hit.getSourceAsMap();
                if (incidents.get("origin").toString().equals(e)) {
                    LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);
                    Incidencia i = new Incidencia(date, incidents.get("origin").toString(),
                            incidents.get("destination").toString(), incidents.get("detail").toString(), getIncidentType(incidents.get("type").toString()));
                    incidencias.add(i);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return incidencias;
    }

    public List<Incidencia> getIncidentsByDestination(SearchRequest searchRequest, String e) {
        List<Incidencia> incidencias = new ArrayList<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                Map<String, Object> incidents = hit.getSourceAsMap();
                if (incidents.get("destination").toString().equals(e)) {
                    LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);

                    Incidencia i = new Incidencia(date, incidents.get("origin").toString(),
                            incidents.get("destination").toString(), incidents.get("detail").toString(), getIncidentType(incidents.get("type").toString()));
                    incidencias.add(i);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return incidencias;
    }

    public boolean checkUserExists(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            if (hits.length == 0) {
                return false;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    public void updateEmpleado(SearchRequest searchRequest, HashMap<String, Object> jsonMap) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                Map<String, Object> usuarios = hit.getSourceAsMap();
                if (usuarios.get("user").toString().equals(jsonMap.get("user"))) {
                    UpdateRequest updateRequest = new UpdateRequest("users", hit.getId())
                            .doc(jsonMap);
                    UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Tipo getIncidentType(String tipo) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return Tipo.URGENTE;
            case "NORMAL":
                return Tipo.NORMAL;
        }
        return null;
    }

    public void delete(SearchRequest searchRequest, String user) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] results = searchResponse.getHits().getHits();
            for (SearchHit hit : results) {
                Map<String, Object> usuarios = hit.getSourceAsMap();
                if (usuarios.get("user").toString().equals(user)) {
                    DeleteRequest deleterequest = new DeleteRequest("users", hit.getId());
                    DeleteResponse deleteResponse = client.delete(deleterequest, RequestOptions.DEFAULT);
                }
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
                Map<String, Object> incidents = hit.getSourceAsMap();
                if (hit.getId().equals(String.valueOf(id))) {
                    LocalDate date = LocalDate.parse(incidents.get("date").toString(), formatter);
                    i = new Incidencia(date, incidents.get("origin").toString(),
                            incidents.get("destination").toString(), incidents.get("detail").toString(), getIncidentType(incidents.get("type").toString()));
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