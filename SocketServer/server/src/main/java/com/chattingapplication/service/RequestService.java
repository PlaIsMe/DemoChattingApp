package com.chattingapplication.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import com.chattingapplication.model.ExceptionError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RequestService {
    static String apiUrl = "http://localhost:8080/api/";
    
    public static String getRequest(String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + path))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
                return response.body();
        } else {
                return "Oops something went wrong!";
        }
    }

    public static String postRequest(String path, String jsonString) throws IOException, InterruptedException {
        HashMap<String, Object> values = new Gson().fromJson(jsonString, HashMap.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + path))
                .setHeader("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
                return response.body();
        } else {
                Gson gson = new Gson();
                ExceptionError exceptionError = gson.fromJson(response.body(), ExceptionError.class);
                return exceptionError.getMessage();
        }
    }

    public static String putRequest(String path, String jsonString) throws IOException, InterruptedException {
        HashMap<String, Object> values = new Gson().fromJson(jsonString, HashMap.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + path))
                .setHeader("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
                return response.body();
        } else {
                Gson gson = new Gson();
                ExceptionError exceptionError = gson.fromJson(response.body(), ExceptionError.class);
                return exceptionError.getMessage();
        }
    }

    public static String deleteRequest(String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + path))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
                return "Delete Success!";
        } else {
                Gson gson = new Gson();
                ExceptionError exceptionError = gson.fromJson(response.body(), ExceptionError.class);
                return exceptionError.getMessage();
        }
    }
}
