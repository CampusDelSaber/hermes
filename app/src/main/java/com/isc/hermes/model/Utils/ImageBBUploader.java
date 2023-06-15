package com.isc.hermes.model.Utils;


import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageBBUploader {

    private static final String IMGUR_CLIENT_ID = "8c888c11506deaa";
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    public static void uploadToImgur(File imageFile) {
        // Ejecutar el AsyncTask para subir la imagen a Imgur
        new ImgurUploadTask().execute(imageFile);
    }

    private static class ImgurUploadTask extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... files) {
            File imageFile = files[0];
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(), RequestBody.create(MEDIA_TYPE_JPEG, imageFile))
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgur.com/3/upload")
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al subir la imagen a Imgur: " + response);
                }

                // Obtener la URL de la imagen subida desde la respuesta JSON
                String responseBody = response.body().string();
                // Analizar el JSON de la respuesta
                JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
                // Obtener la URL de la imagen subida
                String imageUrl = json.getAsJsonObject("data").get("link").getAsString();

                return imageUrl;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            if (imageUrl != null) {
                System.out.println("Imagen subida a Imgur: " + imageUrl);
            } else {
                System.out.println("Error al subir la imagen a Imgur.");
            }
        }
    }
}