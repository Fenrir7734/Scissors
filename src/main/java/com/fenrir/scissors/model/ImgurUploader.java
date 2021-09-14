package com.fenrir.scissors.model;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.stream.Collectors;

public class ImgurUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImgurUploader.class);

    private static final String UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "f56ba7718f727fe";

    static public String upload(BufferedImage image) throws IOException {
        HttpURLConnection connection = getConnection();
        writeToConnection(connection, toBase64(image));
        return getUploadUrl(connection);
    }

    static private String toBase64(BufferedImage image) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            LOGGER.error("Image to Base64 String conversion error: {}", e.getMessage());
            throw e;
        }
    }

    static private void writeToConnection(HttpURLConnection connection, String message) throws IOException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Write to connection error: {}", e.getMessage());
            throw e;
        }
    }

    static private String getUploadUrl(HttpURLConnection connection) throws IOException {
        try {
            int status = connection.getResponseCode();
            if(status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.lines().collect(Collectors.joining());
                return new JSONObject(response).getJSONObject("data").getString("link");
            } else {
                throw new IOException(connection.getResponseMessage());
            }
        } catch (IOException e) {
            LOGGER.error("Reading connection response error: {}", e.getMessage());
            throw e;
        }
    }

    static private HttpURLConnection getConnection() throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            connection.connect();
            return connection;
        } catch (MalformedURLException e) {
            LOGGER.error("api.imgur.com could not be found");
            throw e;
        } catch (IOException e) {
            LOGGER.error("Getting connection to api.imgur.com error: {}", e.getMessage());
            throw e;
        }
    }
}
