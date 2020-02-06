package controller;

import org.elasticsearch.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.core.XPackClient;
import org.elasticsearch.xpack.core.XPackPlugin;
import org.elasticsearch.core.watcher.client.WatcherClient;

public class ManagerDao {

    public ManagerDao() {
        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("host1"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("host2"), 9300));

// on shutdown
        client.close();

        TransportClient client = new PreBuiltXPackTransportClient(Settings.builder().put("cluster.name", "myClusterName")
        ...
    .build()
        )
    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

        XPackClient xpackClient = new XPackClient(client);
        WatcherClient watcherClient = xpackClient.watcher();

        Client client = new PreBuiltTransportClient(
                Settings.builder().put("client.transport.sniff", true)
                        .put("cluster.name", "elasticsearch").build())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    private TransportClient getElasticClient() {
        try {

            Settings setting = Settings.builder()
                    .put("cluster.name", elasticPro.getProperty("cluster"))
                    .put("client.transport.sniff", Boolean.valueOf(elasticPro.getProperty("transport.sniff"))).build();

            client = new PreBuiltTransportClient(setting)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticPro.getProperty("host")), Integer.valueOf(elasticPro.getProperty("port"))));
        } catch (UnknownHostException ex) {
            log.error("Exception occurred while getting Client : " + ex, ex);
        }
        return client;
    }
}
