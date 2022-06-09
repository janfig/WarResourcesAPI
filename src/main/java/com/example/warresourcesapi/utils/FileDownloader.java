package com.example.warresourcesapi.utils;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;

import java.io.*;
import java.net.Authenticator;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FileDownloader extends Authenticator {


    static String resPath = System.getProperty("user.dir") + "/src/main/resources/static/";
    static private Logger logger = LoggerFactory.getLogger(FileDownloader.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        String json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/GOLD.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1");
        Resource resource = new Resource("dupa");
        fillResource(json, resource);
        System.out.println(resource.toString());
    }

    public static void download(String url, String fileName) throws IOException {
        Authenticator.setDefault(new MyAuth("janke27", "33de27cc0bc6955c48289d187e5dd499"));
        ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
        FileOutputStream fileOutputStream =
                new FileOutputStream(resPath + fileName);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }

    public static String redirectLink(String url) {
        String username = "janke27";
        String pass = "33de27cc0bc6955c48289d187e5dd499";
        String credentials = username + ":" + pass;
        String basicToken = Base64.getEncoder().encodeToString(credentials.getBytes());
        var uri = URI.create(url);
        String redirectLink = "";
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        try {
            var request = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("accept", "file")
                    .header("authorization", "Basic " + basicToken)
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            logger.info("Status code: " + response.statusCode());
            logger.info("Headers: " + response.headers().allValues("location"));
            logger.info("Body: " + response.body());
            redirectLink = response.headers().allValues("location").get(0);
            logger.info("Redirect link: " + redirectLink);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return redirectLink;

    }


    public static void unzip(String path, String fileName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("unzip", path + fileName);
        pb.directory(new File(path));
        Process p = pb.start();
        p.waitFor(5, TimeUnit.SECONDS);
    }

    public static String getResPath() {
        return resPath;
    }

    public static void setResPath(String resPath) {
        FileDownloader.resPath = resPath;
    }

    public static String downloadJSON(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        var uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void JsonConverter(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        JsonNode pricesCore = tree.get("dataset").get("data");
        Set<Price> prices = new HashSet<>();
        for (var el : pricesCore) {
            prices.add(new Price(
                    el.get(1).asDouble(),
                    LocalDate.parse(el.get(0).asText())
                    ));
        }
        Resource resource = new Resource("zlotko");
        resource.setPrices(prices);
        System.out.println(resource);
//        Set<Price> prices =
//                mapper.readValue(pricesCore, new TypeReference<Set<Price>>(){});
//        Set<Price> prices = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(Set.class, Price.class));

    }

    public static void fillResource(String json, Resource resource) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        JsonNode pricesCore = tree.get("dataset").get("data");
        TreeSet<Price> prices = new TreeSet<>();
        for (var el : pricesCore) {
            prices.add(new Price(
                    el.get(1).asDouble(),
                    LocalDate.parse(el.get(0).asText())
            ));
        }
        resource.setPrices(prices);
        System.out.println("Resource " + resource.getName() + " filled.");
    }

}
