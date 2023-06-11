package com.chattingapplication;

import java.io.IOException;

import com.chattingapplication.controller.ServerController;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerController.handleConnect();
    }
}