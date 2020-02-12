package controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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
    RestHighLevelClient client;

    public ManagerDao() {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }
    
    public void index(){
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "eric");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("2").source(jsonMap).opType(DocWriteRequest.OpType.CREATE);;
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String get() throws Exception{
        String s;
        GetRequest getRequest = new GetRequest(
        "posts", 
        "2"); 
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            s = getResponse.getSourceAsString();
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
            s="";
        }
        return s;
    }
    
    public void update(){
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("message", "trying on Elasticsearch");
        UpdateRequest updateRequest = new UpdateRequest("posts", "2")
                .doc(jsonMap);
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close(){
        try {
            client.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
