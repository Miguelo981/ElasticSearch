package view;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 *
 * @author alu2018240
 */
public class Main {

    final private static String[] FETCH_FIELDS = {"@timestamp", "@message"};

    final private static String MATCH_FIELD = "@message";
    final private static String[] MUST_MATCH = {"brewster-shaw", "AppleWebKit"};
    final private static String[] MUST_NOT_MATCH = {"21.211.33.63"};
    final private static String TIME_FIELD = "@timestamp";
    final private static String START_TIME = "2015-05-20T13:06:50";
    final private static String END_TIME = "2025-05-06T00:00:00";

    final private static String INDEX = "logstash-2015.05.20"; // accepts * as wildcard, .e.g log*

    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder qb = QueryBuilders.boolQuery();

        if (MUST_MATCH.length > 0) {
            for (String match : MUST_MATCH) {
                qb.must(QueryBuilders.matchQuery(MATCH_FIELD, match));
            }
        }

        if (MUST_NOT_MATCH.length > 0) {
            for (String notMatch : MUST_NOT_MATCH) {
                qb.mustNot(QueryBuilders.matchQuery(MATCH_FIELD, notMatch));
            }
        }

        qb.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME));
        qb.must(QueryBuilders.rangeQuery(TIME_FIELD).lte(END_TIME));

        searchSourceBuilder.query(qb).fetchSource(FETCH_FIELDS, null);

        searchRequest.indices(INDEX);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        if (searchResponse.getHits().getTotalHits().value > 0) {

            System.out.println(searchResponse.getHits().getTotalHits());

            for (SearchHit hit : searchResponse.getHits()) {
                System.out.println("Match: ");
                for (String fetchField : FETCH_FIELDS) {
                    System.out.println(" - " + fetchField + " " + hit.getSourceAsMap().get(fetchField));
                }
            }
        } else {
            System.out.println("No results matching the criteria.");
        }

        client.close();

    }
}
