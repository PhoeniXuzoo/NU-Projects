package edu.northwestern.ssa;

import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.http.HttpExecuteResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static void printInputStream(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String readLine;
            while (((readLine = br.readLine()) != null)) System.out.println(readLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String FILE_NAME = System.getenv("COMMON_CRAWL_FILENAME");
        final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");
        final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
        final String ELASTIC_SEARCH_HOST = System.getenv("ELASTIC_SEARCH_HOST");
        final String ELASTIC_SEARCH_INDEX = System.getenv("ELASTIC_SEARCH_INDEX");
        //File f = new File("./data");
        InputStream is;

//        try (S3Client s3 = S3Client.builder()
//                            .region(Region.US_EAST_1)
//                            .overrideConfiguration(ClientOverrideConfiguration.builder()
//                                                    .apiCallTimeout(Duration.ofMinutes(30)).build())
//                            .build();) {
//            if (FILE_NAME == null || FILE_NAME.equals("")) {
//                ListObjectsV2Response list = s3.listObjectsV2(ListObjectsV2Request.builder().bucket("commoncrawl").prefix("crawl-data/CC-NEWS/2020/04").build());
//                List<S3Object> l = list.contents();
//                FILE_NAME= l.get(l.size()-1).key();
//            }
//
//            GetObjectRequest request = GetObjectRequest.builder().bucket("commoncrawl").key(FILE_NAME).build();
//            // s3.getObject(request, ResponseTransformer.toFile(f));
//            is = s3.getObject(request, ResponseTransformer.toInputStream());
//        }
//        catch (Exception e) {
//            System.out.println(e.toString());
//        }

        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofMinutes(30)).build())
                .build();
            if (FILE_NAME == null || FILE_NAME.equals("")) {
                ListObjectsV2Response list = s3.listObjectsV2(ListObjectsV2Request.builder().bucket("commoncrawl").prefix("crawl-data/CC-NEWS/2020/04").build());
                List<S3Object> l = list.contents();
                FILE_NAME= l.get(l.size()-1).key();
            }

            GetObjectRequest request = GetObjectRequest.builder().bucket("commoncrawl").key(FILE_NAME).build();
            // s3.getObject(request, ResponseTransformer.toFile(f));
            is = s3.getObject(request, ResponseTransformer.toInputStream());



        try {
            ElasticSearch es = new ElasticSearch("es");
            HttpExecuteResponse output = es.inital();
            System.out.println("inital elasticeSearch and its index");
           printInputStream(output.responseBody().get());
//            HttpExecuteResponse response3 = es.delete("zxp3141");
//            printInputStream(response3.responseBody().get());
//            HttpExecuteResponse response1 = es.delete("test-index-789210");
//            printInputStream(response1.responseBody().get());
//            HttpExecuteResponse response2 = es.delete("test-index-920775313");
//            printInputStream(response2.responseBody().get());
//            es.close();


            try {
                //ArchiveReader archiveReader = WARCReaderFactory.get(f);
                 ArchiveReader archiveReader = WARCReaderFactory.get(FILE_NAME, is, true);
                int count = 0;
                ArrayList<JSONObject> bundle = new ArrayList<>();
                for (ArchiveRecord archiveRecord : archiveReader) {
                    try {
                        if (archiveRecord.getHeader().getHeaderValue("WARC-Type").equals("response")) {
                            String url = archiveRecord.getHeader().getUrl();
                            String Date;
                            try {
                                Date = archiveRecord.getHeader().getDate();
                                if (Date.length() >= 10) {
                                    Date = Date.substring(0, 10);
                                }
                                else continue;
                            }
                            catch (Exception e) {
                                continue;
                            }


                            byte[] charArray = new byte[archiveRecord.available()];
                            int offset = 0;
                            int num = 0;
                            while (num >= 0) {
                                num = archiveRecord.read(charArray, offset, charArray.length);
                                offset += num;
                            }
                            String content = new String(charArray);

                            int endOfLangFeild = content.indexOf("html lang=\"en\"");
                            if (endOfLangFeild == -1) continue;

                            int endOfHTTPHeader = content.indexOf("\r\n\r\n");
                            if (endOfHTTPHeader == -1) continue;
                            else endOfHTTPHeader += 4;
                            String html = content.substring(endOfHTTPHeader);

                            Document doc = Jsoup.parse(html);
                            String title = doc.title();
                            String text = doc.text();
                            JSONObject tuple = new JSONObject();
                            tuple.put("title", title);
                            tuple.put("url", url);
                            tuple.put("txt", text);
                            tuple.put("date", Date);
                            tuple.put("lang", "en");

                            bundle.add(tuple);

                            if (count == 100) {
                                HttpExecuteResponse response = es.postBundle(bundle);
                                //printInputStream(response.responseBody().get());
                                response.responseBody().get().close();
                                bundle.clear();
                                count = 0;
                            }
                            count++;


                        }
                    } catch (Exception e) {
                        ;
                    }
                }
                bundle.clear();
                es.close();
                output.responseBody().get().close();
                s3.close();
            }
            catch (Exception e) {
                es.close();
                output.responseBody().get().close();
                s3.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
