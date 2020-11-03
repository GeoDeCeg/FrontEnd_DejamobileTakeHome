package com.example.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;


    JSONObject user = new JSONObject();
    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);


        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez les champs d'authentification", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        findUser(inputName, inputPassword);
                        

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private boolean validate(JSONObject userTest) {

        if (user != null) {
            return isValid = true;
        } else {
            return isValid = false;
        }
    }

    private JSONObject findUser(String login, String mdp) throws JSONException {

        String Url = "http://10.0.2.2:8080/client/login";


        JSONObject couple = new JSONObject();
        couple.put("login", login);
        couple.put("password", mdp);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                Url, couple,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        user = response;
                        validate(user);
                        if (!isValid) {
                            Toast.makeText(LoginActivity.this, "Mot de passe ou Login incorrect", Toast.LENGTH_SHORT).show();
                            eLogin.setEnabled(false);
                            ePassword.setError("Mot de passe ou Login incorrect");
                        } else {
                            Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Non connecté");
                        Toast.makeText(LoginActivity.this, "Mot de passe ou Login incorrect", Toast.LENGTH_SHORT).show();

                        ePassword.setError("Mot de passe ou Login incorrect");
                    }
                });

        requestQueue.add(request);

        System.out.println(user + "!!!!!!!!!!!!!!!!!!!!!!!!!");
        return user;


    }
}