package controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import modelo.Empleado;
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

        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    //public Empleado getEmpleado(GetFieldMappingsRequest  getRequest) throws Exception {
    public void getEmpleado(SearchRequest  getRequest) throws Exception {
        SearchResponse response = client.search(getRequest, RequestOptions.DEFAULT);
        
        //GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getHits().getAt(0).getSourceAsString());
        Map<String, Object> sourceAsMap = response.getHits().getAt(0).getSourceAsMap();
                
    }
    
    public void getTryEmpleado(SearchRequest  getRequest) throws Exception {
        SearchResponse response = client.search(getRequest, RequestOptions.DEFAULT);
        
        //GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getHits().getAt(0).getSourceAsString());
        SearchHit[] hits = response.getHits().getHits();
        int maxId = 0;
        for (SearchHit hit : hits){
            int id = Integer.parseInt(hit.getId());
            if(id > maxId){
                maxId = id;
            }
        }
        System.out.println(maxId);
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

    public void update() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("message", "trying on Elasticsearch");
        UpdateRequest updateRequest = new UpdateRequest("posts", "2")
                .doc(jsonMap);
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void delete() {
        DeleteRequest request = new DeleteRequest("users", "1");
        try {
            DeleteResponse deleteResponse = client.delete(
                    request,
                    RequestOptions.DEFAULT);
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.CONFLICT) {
                System.out.println("hola");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void close() {
        try {
            client.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
