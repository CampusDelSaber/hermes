package com.isc.hermes.model.Utils;

import android.os.AsyncTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utility class for uploading images to Imgur server.
 */
public class ImageUploaderToServerImgur {

    //TODO: Implement this class with DataBase
    private static final String IMGUR_CLIENT_ID = "8c888c11506deaa";
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    /**
     * Uploads the specified image file to Imgur.
     *
     * @param imageFile The image file to upload.
     */
    public static void uploadToImgur(File imageFile) {
        new ImgurUploadTask().execute(imageFile);
    }

    /**
     * AsyncTask that performs the image upload operation in the background.
     */
    private static class ImgurUploadTask extends AsyncTask<File, Void, String> {

        /**
         * Performs the image upload operation in the background.
         *
         * @param files The image file to upload.
         * @return The URL of the uploaded image, or null if an error occurred.
         */
        @Override
        protected String doInBackground(File... files) {
            File imageFile = files[0];
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = buildMultipartRequestBody(imageFile);
            Request request = buildRequest(requestBody);

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Error al subir la imagen a Imgur: " + response);

                String responseBody = response.body().string();
                JsonObject json = parseResponseBody(responseBody);
                return getImageUrlFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Builds the multipart request body for uploading the image.
         *
         * @param imageFile The image file to upload.
         * @return The request body.
         */
        private RequestBody buildMultipartRequestBody(File imageFile) {
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(), RequestBody.create(MEDIA_TYPE_JPEG, imageFile))
                    .build();
        }

        /**
         * Builds the request for uploading the image.
         *
         * @param requestBody The request body.
         * @return The request.
         */
        private Request buildRequest(RequestBody requestBody) {
            return new Request.Builder()
                    .url("https://api.imgur.com/3/upload")
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .post(requestBody)
                    .build();
        }

        /**
         * Parses the response body and extracts the image URL.
         *
         * @param responseBody The response body.
         * @return The image URL.
         */
        private JsonObject parseResponseBody(String responseBody) {
            return new JsonParser().parse(responseBody).getAsJsonObject();
        }

        /**
         * Retrieves the image URL from the JSON response.
         *
         * @param json The JSON response.
         * @return The image URL.
         */
        private String getImageUrlFromJson(JsonObject json) {
            return json.getAsJsonObject("data").get("link").getAsString();
        }

        /**
         * Called when the image upload operation is completed.
         *
         * @param imageUrl The URL of the uploaded image, or null if an error occurred.
         */
        @Override
        protected void onPostExecute(String imageUrl) {
            if (imageUrl != null) System.out.println("Imagen subida a Imgur: " + imageUrl);
            else System.out.println("Error al subir la imagen a Imgur.");
        }
    }
}