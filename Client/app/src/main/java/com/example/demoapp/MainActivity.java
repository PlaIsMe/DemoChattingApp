package com.example.demoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Register(View v) {
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

        SocketActivity socketActivity = new SocketActivity();
        socketActivity.execute(jsonString);
    }
}