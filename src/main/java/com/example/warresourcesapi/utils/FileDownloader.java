package com.example.warresourcesapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class FileDownloader extends Authenticator {

    static String resPath = "/home/janek/java-crash/projects/WarResourcesAPI/src/main/resources/static/";
    static private Logger logger = LoggerFactory.getLogger(FileDownloader.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        download(
                "https://correlatesofwar.org/data-sets/COW-war/intra-state-wars-v5-1.zip/@@download/file/Intra-State%20Wars%20v5.1.zip",
                "file123.zip"
                );
        unzip(resPath, "file123.zip");
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
}
