package controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ManagerDao {
    private static ManagerDao managerDao;
    RestHighLevelClient client;

    private ManagerDao() {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }
    
    public static ManagerDao getManagerDao() {
        if (managerDao == null) {
            managerDao = new ManagerDao();
        }
        return managerDao;
    }

    public void index() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "miguelangel");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("3").source(jsonMap).opType(DocWriteRequest.OpType.CREATE);;
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get() throws Exception {
        GetRequest getRequest = new GetRequest("posts", "3");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return getResponse.getSourceAsString();
    }

    public void updatePosts() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "daily update");
        UpdateRequest updateRequest = new UpdateRequest("posts", "3")
                .doc(jsonMap);
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
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
