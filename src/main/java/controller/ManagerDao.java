package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ManagerDao {

    public ManagerDao() {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")
            )
        );
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ManagerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
