package com.chattingapplication.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

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
            ServerService.removeClient(this);
        }
    }

    public void closeClientSocket() {
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
            String buffer = ServerService.socketReceive(this);
            if (buffer == null) {
                ServerService.removeClient(this);
                break;
            } else {
                try {
                    ServerService.handleRequest(this, buffer);
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
