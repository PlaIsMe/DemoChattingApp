package com.chattingapplication.service;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.chattingapplication.model.Account;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ServerService {
    public static ArrayList<ClientHandleService> clientHandlers = new ArrayList<>();

    public static String socketReceive(ClientHandleService clientHandleService) {
        try {
            return clientHandleService.getdIn().readUTF();           
        } catch (EOFException e) {
            clientHandleService.closeClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void socketSend(ClientHandleService clientHandleService, String responseName, String responseParam) {
        String message;
        try {
            message = new JSONObject()
                    .put("ResponseFunction", responseName)
                    .put("ResponseParam", responseParam)
                    .toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            clientHandleService.getdOut().writeUTF(message);
            clientHandleService.getdOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chattingRequest(ClientHandleService clientHandleService, String message) {
        for (ClientHandleService client: clientHandlers) {
            if (client.getClientSocket() != clientHandleService.getClientSocket()) {
                socketSend(client, "chattingResponse", clientHandleService.getClientAccount().getUsername() + ": " + message);
            }
        }
    }

    public static void registerRequest(ClientHandleService clientHandleService, String jsonString) throws IOException, InterruptedException {
        String response = RequestService.postRequest("account/signin", jsonString);
        Gson gson = new Gson();
        try {
            Account currentAccount = gson.fromJson(response, Account.class);
            clientHandleService.setClientAccount(currentAccount);
        } catch (JsonSyntaxException e) {

        }
        ServerService.socketSend(clientHandleService, "registerResponse", response);
    }

    class Request {
        String RequestFunction;
        String RequestParam;
    }

    public static void handleRequest(ClientHandleService clientHandleService, String jsonRequest) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        System.out.println(jsonRequest);
        Gson gson = new Gson();
        Request request = gson.fromJson(jsonRequest, Request.class);
        Method method = ServerService.class.getMethod(request.RequestFunction, ClientHandleService.class ,String.class);
        method.invoke(null, clientHandleService, request.RequestParam);
    }

    public static void handleConnect(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            ClientHandleService clientHandleService = new ClientHandleService(clientSocket);
            clientHandlers.add(clientHandleService);
            Thread thread = new Thread(clientHandleService);
            thread.start();
        }
    }

    public static void shutDownServer(ServerSocket serverSocket) throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
