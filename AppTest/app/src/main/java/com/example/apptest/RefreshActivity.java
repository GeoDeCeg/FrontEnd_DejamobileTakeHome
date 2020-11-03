package com.example.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class RefreshActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getIntent();

        String Url = "http://10.0.2.2:8080/carte";

        RequestQueue requestQ = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray newCardsList = response;
                        System.out.println(newCardsList + "////////////REFRESH ACTIVITY");
                        Intent intent = new Intent(getApplicationContext(), Display.class);
                        intent.putExtra("jsonArray", newCardsList.toString());
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                requestQ.add(request);
            }
        }, 5000);

    }


}