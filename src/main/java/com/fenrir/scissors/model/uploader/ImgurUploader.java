/**
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 * Modifications copyright 2021 Fenrir7734 (Karol Hetman)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fenrir.scissors.model.uploader;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
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

    static public String upload(Image image) throws IOException, WebException {
        HttpURLConnection connection = getConnection();
        write(connection, toBase64(image));
        return getUrlFrom(connection);
    }

    static private String toBase64(Image image) throws IOException {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            LOGGER.error("Image to Base64 String conversion error: {}", e.getMessage());
            throw e;
        }
    }

    static private void write(HttpURLConnection connection, String message) throws IOException {
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

    static private String getUrlFrom(HttpURLConnection connection) throws IOException, WebException {
        try {
            int status = connection.getResponseCode();
            if(status == StatusCode.OK.getCode()) {
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.lines().collect(Collectors.joining());
                return new JSONObject(response).getJSONObject("data").getString("link");
            } else {
                throw new WebException(StatusCode.getResponseCode(status));
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
