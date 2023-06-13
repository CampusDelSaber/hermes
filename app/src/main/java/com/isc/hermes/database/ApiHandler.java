package com.isc.hermes.database;

import org.json.JSONArray;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiHandler {
    private final String API_URL = "https://api-rest-hermes.onrender.com/";

    private ExecutorService executorService;
    private ApiRequestHandler requestHandler;
    private ApiResponseParser responseParser;

    public ApiHandler() {
        executorService = Executors.newSingleThreadExecutor();
        requestHandler = ApiRequestHandler.getInstance();
        responseParser = ApiResponseParser.getInstance();
    }

    public  Future<String> getFutureCollectionGetString(String params){
        Future<String> future = executorService.submit(
            () -> requestHandler.getDataFromApi(API_URL + params)
        );
        return future;
    }

}