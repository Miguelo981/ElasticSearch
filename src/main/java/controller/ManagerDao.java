package controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import modelo.Empleado;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
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

    public Empleado getEmpleado(GetRequest getRequest) throws Exception {
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.print(response.getSourceAsString());
        String user = (String) response.getField("user").getValue();
        String name = (String) response.getField("name").getValue();
        String surname = (String) response.getField("surname").getValue();
        String phone = (String) response.getField("phone").getValue();
        String dni = (String) response.getField("dni").getValue();
        return new Empleado(user, name, surname, phone, dni);

    }

    public Empleado getEmpleado() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("userss");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "Omar"); 
        searchSourceBuilder.query(matchQueryBuilder);
        System.out.println(searchRequest.source(searchSourceBuilder));
        return null;
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
