package com.fenrir.scissors.model.uploader;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ImgurUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImgurUploader.class);

    private static final String UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "f56ba7718f727fe";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(40))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    static public String upload(Image image) throws IOException, WebException, InterruptedException {
        byte[] bytes = toBase64(image);
        HttpResponse<String> response = makeRequest(bytes);
        return getUrlFrom(response);
    }

    static private byte[] toBase64(Image image) throws IOException {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Image to Base64 String conversion error: {}", e.getMessage());
            throw e;
        }
    }

    static private HttpResponse<String> makeRequest(byte[] bytes) throws IOException, InterruptedException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UPLOAD_URL))
                    .header("Authorization", "Client-ID " + CLIENT_ID)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .timeout(Duration.ofSeconds(40))
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOGGER.error("Sending request to the server error: {}", e.getMessage());
            throw e;
        } catch (InterruptedException e) {
            LOGGER.error("HttpClient thread has been interrupted: {}", e.getMessage());
            throw e;
        }
    }

    static private String getUrlFrom(HttpResponse<String> response) throws WebException {
        int status = response.statusCode();
        if (status == StatusCode.OK.getCode()) {
            String body = response.body();
            return extractUrlFrom(body);
        } else {
            throw new WebException(StatusCode.getResponseCode(status));
        }
    }

    static private String extractUrlFrom(String body) {
        return new JSONObject(body).getJSONObject("data")
                .getString("link");
    }
}
