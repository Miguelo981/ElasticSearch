package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Empleado;
import modelo.Evento;
import modelo.Incidencia;
import modelo.RankingTO;
import modelo.enums.Tipo;
import modelo.enums.TipoEvento;
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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

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

    public boolean index(IndexRequest indexRequest) {
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

    public List<Empleado> findEmpleados(SearchRequest findRequest) throws IOException {
        List<Empleado> empleados = new ArrayList<>();
        SearchResponse response = client.search(findRequest, RequestOptions.DEFAULT);
        SearchHit[] results = response.getHits().getHits();
        for (SearchHit h : results) {
            Map<String, Object> sourceAsMap = h.getSourceAsMap();
            Empleado e = new Empleado((String) sourceAsMap.get("user"),
                    (String) sourceAsMap.get("name"), (String) sourceAsMap.get("surname"),
                    (String) sourceAsMap.get("phone"), (String) sourceAsMap.get("dni"),
                    (String) sourceAsMap.get("pass"));
            empleados.add(e);
        }
        return empleados;
    }

    public int getID(SearchRequest searchRequest) {
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] results = response.getHits().getHits();
            int maxId = 0;
            for (SearchHit h : results) {
                int actualId = Integer.parseInt(h.getId());
                if (actualId > maxId) {
                    maxId = actualId;
                }
            }
            maxId++;
            return maxId;
        } catch (IOException ex) {
            return 0;
        }
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
                Map<String, Object> incident = hit.getSourceAsMap();
                if (hit.getId().equals(String.valueOf(id))) {
                    LocalDate date = LocalDate.parse(incident.get("date").toString(), formatter);
                    i = new Incidencia(date, incident.get("origin").toString(),
                            incident.get("destination").toString(), incident.get("detail").toString(), getIncidentType(incident.get("type").toString()));
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

    public List<RankingTO> getRanking(SearchRequest searchEmpleadoRequest) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<RankingTO> rankingEmpleados = new ArrayList<>();
        List<Incidencia> incidencias = new ArrayList<>();
        SearchRequest searchIncidentRequest = new SearchRequest();
        searchIncidentRequest.indices("incidents");
        SearchResponse incidentResponse = client.search(searchIncidentRequest, RequestOptions.DEFAULT);
        
        SearchHit[] incidentsResults = incidentResponse.getHits().getHits();
        for (SearchHit h : incidentsResults) {
            Map<String, Object> sourceAsMap = h.getSourceAsMap();
            LocalDate date = LocalDate.parse(sourceAsMap.get("date").toString(), formatter);
            Incidencia i = new Incidencia(date, sourceAsMap.get("origin").toString(),
                    sourceAsMap.get("destination").toString(), sourceAsMap.get("detail").toString(), getIncidentType(sourceAsMap.get("type").toString()));
            incidencias.add(i);
        }
        
        SearchResponse empleadoResponse = client.search(searchEmpleadoRequest, RequestOptions.DEFAULT);
        SearchHit[] employeeResults = empleadoResponse.getHits().getHits();
        for (SearchHit h : employeeResults) {
            Map<String, Object> sourceAsMap = h.getSourceAsMap();
            Empleado e = new Empleado((String) sourceAsMap.get("user"),
                    (String) sourceAsMap.get("name"), (String) sourceAsMap.get("surname"),
                    (String) sourceAsMap.get("phone"), (String) sourceAsMap.get("dni"),
                    (String) sourceAsMap.get("pass"));
            int numInc = 0;
            for (Incidencia i : incidencias){
                if (i.getOrigen().equals(e.getUsuario())){
                    if(i.getTipo().equals(Tipo.URGENTE)){
                        numInc++;
                    }
                }
            }
            rankingEmpleados.add(new RankingTO(e, numInc));
        }
        return rankingEmpleados;
    }

    public Evento getLastSession(Empleado e) {
        Evento evento = null;
        try {
            
            SearchRequest getRequest = new SearchRequest();
            getRequest.indices("events");
            SearchResponse response = client.search(getRequest, RequestOptions.DEFAULT);
            SearchHit[] results = response.getHits().getHits();
            List<SearchHit> sortedResults = new ArrayList<>();
            for (SearchHit h : results){
                Map<String, Object> sourceAsMap = h.getSourceAsMap();
                if(sourceAsMap.get("user").equals(e.getUsuario())){
                    if (sourceAsMap.get("type").equals("I"))
                        sortedResults.add(h);
                }
            }
            if(!sortedResults.isEmpty()){
                SearchHit hit = sortedResults.get(sortedResults.size()-1);
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                evento = new Evento(TipoEvento.I, LocalDate.parse(sourceAsMap.get("date").toString(), formatter), sourceAsMap.get("user").toString());
            }            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return evento;
    }
}
