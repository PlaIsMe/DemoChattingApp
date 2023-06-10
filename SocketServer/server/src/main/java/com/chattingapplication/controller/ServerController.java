package com.chattingapplication.controller;

import java.io.IOException;
import java.net.ServerSocket;

import com.chattingapplication.service.ServerService;


public class ServerController {
    public static void HandleConnect() throws IOException, InterruptedException {
        ServerService.HandleConnect(new ServerSocket(8081));
    }
}
