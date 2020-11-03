package com.example.apptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JSONArray jsonArray = new JSONArray();

    public static final String EXTRA_MESSAGE = "com.example.myapplication.listeNumero";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntent();
        getAllCards();

    }

    public void formCarte(View view){

        Intent intent = new Intent (this, Card.class);
        startActivity(intent);
    }

    public void sendMessage(View view) throws JSONException {


        Intent intent = new Intent(this, RefreshActivity.class);
        intent.putExtra("jsonArray",jsonArray.toString());


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
            }
        }, 500);



    }


    public JSONArray getAllCards() {
        String Url = "http://10.0.2.2:8080/carte";


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArray = response;
                        System.out.println(response+"///////////MAIN ACTIVITY");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        requestQueue.add(request);

        return jsonArray;
    }


}