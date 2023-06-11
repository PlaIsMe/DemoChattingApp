package com.example.demoapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Model.Account;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    String SERVER_IP = "192.168.1.245";
    int SERVER_PORT = 8081;
    Socket clientFd;
    static DataOutputStream dOut;
    DataInputStream dIn;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();
//        HandlePattern("REGISTER|{\"username\":\"test19\",\"email\":\"test19@gmail.com\",\"password\":\"IamPhong\"}");
    }

    class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                clientFd = new Socket(SERVER_IP, SERVER_PORT);
                dOut = new DataOutputStream(clientFd.getOutputStream());
                dIn = new DataInputStream(clientFd.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (clientFd.isConnected()) {
                        try {
                            response = dIn.readUTF();
                            HandleResponse(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();
        }
    }

    public class SendTask extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String request;
            try {
                request = new JSONObject()
                        .put("RequestFunction", params[0])
                        .put("RequestParam", params[1])
                        .toString();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                dOut.writeUTF(request);
                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void RegisterResponse(String jsonString) {
        Gson gson = new Gson();
        try {
            Account currentAccount = gson.fromJson(jsonString, Account.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.fragment_chatting);
                }
            });
        } catch (JsonSyntaxException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView txtRegisterView = findViewById(R.id.txtRegisterResult);
                    txtRegisterView.setText(jsonString);
                }
            });
        }
    }

    public void ChattingResponse(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppendOtherMsg(message);
            }
        });
    }

    class Response {
        String ResponseFunction;
        String ResponseParam;
    }

    public void HandleResponse(String jsonResponse) {
        Gson gson = new Gson();
        Response response = gson.fromJson(jsonResponse, Response.class);
        try {
            Class<?> c = Class.forName(getPackageName() + ".MainActivity");
            Method method = c.getDeclaredMethod(response.ResponseFunction, String.class);
            method.invoke(this, response.ResponseParam);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void AppendOtherMsg(String message) {
        LinearLayout linearLayout = findViewById(R.id.layoutReceive);
        TextView otherMsg = new TextView(this);
        otherMsg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        otherMsg.setText(message);
        otherMsg.setBackgroundColor(Color.parseColor("#808080"));
        otherMsg.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        linearLayout.addView(otherMsg);
    }

    public void Register(View v) throws IOException {
        EditText editTextUserName = findViewById(R.id.editTxtName);
        EditText editTextEmail = findViewById(R.id.editTxtEmail);
        EditText editTextPassword = findViewById(R.id.editTxtPassword);
        String jsonString;
        try {
            jsonString = new JSONObject()
                    .put("username", editTextUserName.getText())
                    .put("email", editTextEmail.getText())
                    .put("password", editTextPassword.getText())
                    .toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        SendTask sendTask = new SendTask();
        sendTask.execute("RegisterRequest", jsonString);
    }

    public void SendMessage(View view) {
        EditText editTxtMessage = findViewById(R.id.editTxtMessage);
        String message = editTxtMessage.getText().toString();
        editTxtMessage.setText("");
        SendTask sendTask = new SendTask();
        sendTask.execute("ChattingRequest", message);
        LinearLayout linearLayout = findViewById(R.id.layoutReceive);
        TextView myMsg = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
        myMsg.setLayoutParams(layoutParams);
        myMsg.setText(message);
        myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        linearLayout.addView(myMsg);
    }

    public void CloseClient() {
        try {
            if (clientFd != null) {
                clientFd.close();
            }
            if (dIn != null) {
                dIn.close();
            }
            if (dOut != null) {
                dOut.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}