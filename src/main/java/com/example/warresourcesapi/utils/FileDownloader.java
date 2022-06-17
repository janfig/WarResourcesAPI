package com.example.warresourcesapi.utils;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.model.War;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.example.warresourcesapi.utils.CSVOpener.arrayToWars;
import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;

public class FileDownloader extends Authenticator {

    //    static String resPath = System.getProperty("user.dir") + "/src/main/resources/static/";
    static String resPath;

    static {
        try {
            resPath = pwd() + "/src/main/resources/static/";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private Logger logger = LoggerFactory.getLogger(FileDownloader.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        String json = downloadJSON("https://api.eia.gov/v2/natural-gas/pri/fut/data?api_key=D4umPxd4ER1AkOrwTo38jsztp54OgzRba7YiFKay&frequency=daily&facets%5Bseries%5D%5B%5D=RNGWHHD&sort%5B0%5D%5Bcolumn%5D=period&sort%5B0%5D%5Bdirection%5D=desc&data%5B1%5D=value&length=12830");
        Resource resource = new Resource("tets");
        JsonConverter(json, resource);
        System.out.println(resource);
    }

    public static String pwd() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("pwd");
        Process process = pb.start();
        String pwd = new String(process.getInputStream().readAllBytes());
        return pwd.substring(0, pwd.length() - 1);
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
//                .header("Content-Type", "application/json")
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void JsonConverter(String json, Resource resource) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        JsonNode pricesCore = tree.get("response").get("data");
        Set<Price> prices = new TreeSet<>();
        for (var el : pricesCore) {
            prices.add(new Price(
                    el.get("value").asDouble(),
                    LocalDate.parse(el.get("period").asText())
            ));
        }
        resource.setPrices(prices);
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
