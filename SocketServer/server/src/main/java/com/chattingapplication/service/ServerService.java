package com.chattingapplication.service;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerService {
    public static ArrayList<ClientHandleService> clientHandlers = new ArrayList<>();

    public static String SocketReceive(ClientHandleService clientHandleService) {
        try {
            return clientHandleService.getdIn().readUTF();           
        } catch (EOFException e) {
            clientHandleService.CloseClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void SocketSend(ClientHandleService clientHandleService, String message) {
        try {
            clientHandleService.getdOut().writeUTF(message);
            clientHandleService.getdOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SendMessage(ClientHandleService clientHandleService, String message) {
        for (ClientHandleService client: clientHandlers) {
            if (client.getClientSocket() != clientHandleService.getClientSocket()) {
                // SocketSend(client, "CHATTING|" + clientHandleService.getClientAccount().getUsername() + ": " + message);
                SocketSend(client, "CHATTING|" + message);
            }
            // SocketSend(client, "CHATTING|" + message);
        }
    }

    public static void HandlePattern(ClientHandleService clientHandleService, String buffer) throws IOException, InterruptedException {
        System.out.println(buffer);
        int pos = buffer.indexOf("|");
        String pattern = buffer.substring(0, pos);
        String value = buffer.substring(pos + 1);
        switch (pattern) {
            case "REGISTER":
                SocketSend(clientHandleService, "REGISTER|" + RequestService.PostRequest("account/signin", value));
                break;
            case "CHATTING":
                System.out.println(clientHandleService.getClientAccount());
                SendMessage(clientHandleService, value);
                break;
            default:
                break;
        }
    }

    public static void HandleConnect(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            ClientHandleService clientHandleService = new ClientHandleService(clientSocket);
            clientHandlers.add(clientHandleService);
            Thread thread = new Thread(clientHandleService);
            thread.start();
        }
    }

    public static void ShutDownServer(ServerSocket serverSocket) throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
