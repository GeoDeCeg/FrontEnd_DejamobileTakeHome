package com.example.apptest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Card extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Button save = findViewById(R.id.btnSave);
        ;
        CardForm cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .setup(Card.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {

                    String nom = cardForm.getCardholderNameEditText().getText().toString();
                    String numeroCarte = cardForm.getCardNumber();
                    String date = cardForm.getExpirationDateEditText().getText().toString();
                    String cvc = cardForm.getCvv();
                    int cVv = Integer.parseInt(cvc);

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Card.this);
                    alertBuilder.setTitle("Confirmation");
                    alertBuilder.setMessage("Confirmez-vous les informations renseignées ?");

                    alertBuilder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            try {
                                enregistrerCarte(numeroCarte, nom, cVv, date);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    alertBuilder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(Card.this, "Veuillez compléter les champs", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void enregistrerCarte(String num, String nom, int cvc, String date) throws JSONException {
        String Url = "http://10.0.2.2:8080/carte";

        JSONObject carte = new JSONObject();
        carte.put("numeroCarte", num);
        carte.put("nomSurCarte", nom);
        carte.put("date", date);
        carte.put("cvc", cvc);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                Url, carte,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"///////////////CARD AJOUTEE OK");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        requestQueue.add(request);

    }


}
