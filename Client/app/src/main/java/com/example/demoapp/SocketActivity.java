package com.example.demoapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketActivity extends AsyncTask<String, Void, Void> {
    private static Socket clientFd;
    private static String ip = "192.168.1.245";
    private static BufferedReader bufferedReader;
    private static PrintWriter printWriter;

    @Override
    protected Void doInBackground(String... params) {
        try {
            clientFd = new Socket(ip, 8081);
            printWriter = new PrintWriter(clientFd.getOutputStream());
            printWriter.write(params[0]);
            printWriter.flush();
            printWriter.close();
            clientFd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
