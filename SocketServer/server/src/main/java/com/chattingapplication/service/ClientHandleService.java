package com.chattingapplication.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.chattingapplication.model.Account;

public class ClientHandleService implements Runnable {
    private Socket clientSocket;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private Account clientAccount;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public DataInputStream getdIn() {
        return dIn;
    }

    public void setdIn(DataInputStream dIn) {
        this.dIn = dIn;
    }

    public DataOutputStream getdOut() {
        return dOut;
    }

    public void setdOut(DataOutputStream dOut) {
        this.dOut = dOut;
    }

    public Account getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(Account clientAccount) {
        this.clientAccount = clientAccount;
    }

    public ClientHandleService(Socket clientSocket) {        
        try {
            this.clientSocket = clientSocket;
            this.dIn = new DataInputStream(clientSocket.getInputStream());
            this.dOut = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            CloseClientSocket();
        }
    }

    public void RemoveClient() {
        ServerService.clientHandlers.remove(this);
    }

    public void CloseClientSocket() {
        this.RemoveClient();        
        try {
            dIn.close();
            dOut.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (clientSocket.isConnected()) {
            try {
                String buffer = ServerService.SocketReceive(this);
                if (buffer == null) {
                    CloseClientSocket();
                    break;
                } else {
                    ServerService.HandlePattern(this, buffer);
                }
            } catch (IOException | InterruptedException e) {
                CloseClientSocket();
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
    }
}
