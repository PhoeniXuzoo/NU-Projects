package edu.northwestern.ssa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northwestern.ssa.AwsSignedRestRequest;
import edu.northwestern.ssa.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.http.HttpExecuteResponse;
import software.amazon.awssdk.http.SdkHttpMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Path("/search")
public class Search {

    /** when testing, this is reachable at http://localhost:8080/api/search?query=hello */
    @GET
    public Response getMsg(@QueryParam("query") String q, @QueryParam("language") String lang, @QueryParam("date") String date, @QueryParam("count") String count, @QueryParam("offset") String offset) throws IOException {
        //JSONArray test = new JSONArray();
        Map<String, String> URLParam = new HashMap<>();
        String queryString;
        final String ELASTIC_SEARCH_HOST = Config.getParam("ELASTIC_SEARCH_HOST");
        final String ELASTIC_SEARCH_INDEX = Config.getParam("ELASTIC_SEARCH_INDEX");

        if (q == null || q.isEmpty()) {
            //test.put("'query' is missing from url.");
            JSONObject test = new JSONObject("{}");
            test.put("returned_results", 0);
            test.put("articles", new JSONArray());
            test.put("total_results", 0);
            return Response.status(200).type("application/json").entity(test.toString(4))
                    // below header is for CORS
                    .header("Access-Control-Allow-Origin", "*").build();
        }

        if (q != null && q.contains(" ")) {
            queryString = "txt:" + q.replace(" ", " AND ");
        }
        else queryString = "txt:" + q;
        System.out.println("queryString is " + queryString);
        if (lang != null && !lang.isEmpty()) {
            queryString += " AND lang:" + lang;
        }

        if (date != null && !date.isEmpty()) {
            queryString += " AND date:" + date;
        }

        if(count==null||count.isEmpty())
            count = "10";

        URLParam.put("size",count);

        if(offset!=null&&!offset.isEmpty())
            URLParam.put("from",offset);

        URLParam.put("q", queryString);

        System.out.println("queryString is " + URLParam.get("q"));
        System.out.println("size is " + URLParam.get("size"));

        Optional<Map<String,String>> query = Optional.of(URLParam);
        AwsSignedRestRequest request = new AwsSignedRestRequest("es");
        HttpExecuteResponse response = request.restRequest(SdkHttpMethod.GET, ELASTIC_SEARCH_HOST, ELASTIC_SEARCH_INDEX + "/_search", query, Optional.empty());

        System.out.println("get Response");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map> JSON = mapper.readValue(response.responseBody().get(), Map.class);

        JSONObject results = new JSONObject("{}");
        JSONObject hits = new JSONObject(JSON.get("hits"));
        JSONObject receivedMsgSizeJSON = hits.getJSONObject("total");
        JSONArray hitsArray = hits.getJSONArray("hits");
        int receivedMsgSize = receivedMsgSizeJSON.getInt("value");
        int countInt = Integer.parseInt(count);
        int resultsSize = Math.min(receivedMsgSize, countInt);
        //System.out.println(JSON);

        JSONArray array = new JSONArray();
        for (int i = 0; i < resultsSize; ++i) {
            JSONObject txt = hitsArray.getJSONObject(i);
            JSONObject source = txt.getJSONObject("_source");
            array.put(source);
        }

        results.put("returned_results", resultsSize);
        results.put("articles", array);
        results.put("total_results", receivedMsgSize);
        return Response.status(200).type("application/json").entity(results.toString(4))
                // below header is for CORS
                .header("Access-Control-Allow-Origin", "*").build();


    }
}
