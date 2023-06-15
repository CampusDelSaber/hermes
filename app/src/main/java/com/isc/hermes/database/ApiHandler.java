package com.isc.hermes.database;

import com.google.gson.Gson;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class responsible for handling API requests and managing the execution of asynchronous tasks.
 */
public class ApiHandler {
    private final String API_URL = "https://api-rest-hermes.onrender.com/";

    private ExecutorService executorService;
    private final ApiRequestHandler requestHandler;
    private static ApiHandler instance;

    /**
     * Constructs a new instance of ApiHandler.
     */
    public ApiHandler() {
        executorService = Executors.newSingleThreadExecutor();
        requestHandler = ApiRequestHandler.getInstance();
    }

    /**
     * Retrieves the future result of a collection string from the API.
     *
     * @param params The parameters to be appended to the API URL.
     * @return A Future object representing the asynchronous result of the API request.
     */
    public Future<String> getFutureCollectionString(String params) {
        return executorService.submit(
                () -> requestHandler.getDataFromApi(API_URL + params)
        );
    }

    public Future<?> postFutureCollections(String params, Object object) {
        String url = API_URL + params;
        Gson gson = new Gson();
        String result = gson.toJson(object);
        System.out.println("RESULT: " + result);
        return executorService.submit(() -> requestHandler.postDataFromApi(url, result));
    }

    /**
     * Retrieves the singleton instance of ApiHandler.
     *
     * @return The singleton instance of ApiHandler.
     */
    public static ApiHandler getInstance() {
        if (instance == null) instance = new ApiHandler();
        return instance;
    }
}
