package edu.northwestern.ssa;

import org.json.JSONObject;
import software.amazon.awssdk.http.HttpExecuteResponse;
import software.amazon.awssdk.http.SdkHttpMethod;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class ElasticSearch extends AwsSignedRestRequest {

    ElasticSearch(String serviceName) {
        super(serviceName);
    }


    protected HttpExecuteResponse inital() throws IOException {

        String index = System.getenv("ELASTIC_SEARCH_INDEX");
        JSONObject body = new JSONObject("{}");
        SdkHttpMethod method = SdkHttpMethod.PUT;
        Optional<Map<String, String>> q = Optional.empty();
        Optional<String> b = Optional.of(body.toString());;

        return super.restRequest(method,System.getenv("ELASTIC_SEARCH_HOST"),index,q,b);
    }

    protected HttpExecuteResponse delete(String s) throws IOException {
        return super.restRequest(SdkHttpMethod.DELETE, System.getenv("ELASTIC_SEARCH_HOST"), s, Optional.empty());
    }

    protected HttpExecuteResponse postBundle(ArrayList<JSONObject> Bundle) throws IOException{

        SdkHttpMethod method = SdkHttpMethod.POST;
        String index = System.getenv("ELASTIC_SEARCH_INDEX");
        String path = "/"+"_bulk";
        Optional<Map<String, String>> q = Optional.empty();
        String createIndex = "{"+"\"index\""+":{\"_index\":\""+index+"\"}}\n";
        StringBuilder sb = new StringBuilder();

        for( JSONObject j : Bundle){
            sb.append(createIndex);
            sb.append(j.toString());
            sb.append("\n");
        }

        Optional<String> b= Optional.of(sb.toString());

        return  super.restRequest(method,System.getenv("ELASTIC_SEARCH_HOST"),path,q,b);
    }

    protected  HttpExecuteResponse Search(Map<String, String> map) throws IOException{

        SdkHttpMethod method = SdkHttpMethod.PUT;
        String index = System.getenv("ELASTIC_SEARCH_INDEX");
        String path = "/"+index+"/_search";
        Optional<Map<String, String>> mapping = Optional.of(map);
        Optional<String> body = Optional.empty();

        return super.restRequest(SdkHttpMethod.GET,System.getenv("ELASTIC_SEARCH_HOST"),path,mapping,body);
    }
}
