package controller;


import org.elasticsearch.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.core.XPackClient;
import org.elasticsearch.xpack.core.XPackPlugin;
import org.elasticsearch.core.watcher.client.WatcherClient;

public class ManagerDao {
    private Client client;

    public ManagerDao() {
        this.client = new PreBuiltTransportClient(
                Settings.builder().put("client.transport.sniff", true)
                        .put("cluster.name", "elasticsearch").build())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }
}
