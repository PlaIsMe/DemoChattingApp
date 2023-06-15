package com.chattingapplication.controller;

import java.io.IOException;
import java.net.ServerSocket;

import com.chattingapplication.service.ServerService;

public class ServerController {
    public static void handleConnect() throws IOException, InterruptedException {
        ServerService.handleConnect(new ServerSocket(8081));
    }
}
