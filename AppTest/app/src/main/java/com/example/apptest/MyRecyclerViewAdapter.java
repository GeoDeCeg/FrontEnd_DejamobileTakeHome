package com.example.apptest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    //private ArrayList<String> mData;
    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private JSONArray newData;

        // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, JSONArray data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject carte = null;
        try {
            carte = (JSONObject) mData.get(position);
            holder.myTextView.setText(carte.getString("numeroCarte"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.length();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.numCarte);
            itemView.setOnClickListener(this);

            //bouton effacer
            itemView.findViewById(R.id.buttonRow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    try {
                        Integer idInteger = mData.getJSONObject(pos).getInt("id");
                        String id =idInteger.toString();

                        String Url = "http://10.0.2.2:8080/carte/"+id;

                        RequestQueue requestQueue = Volley.newRequestQueue(itemView.findViewById(R.id.buttonRow).getContext());

                        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, Url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);

                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error.
                                        Log.d("Error.Response", error.toString());
                                    }
                                }
                        );

                        requestQueue.add(deleteRequest);


                        Intent intent = new Intent(itemView.findViewById(R.id.buttonRow).getContext(), RefreshActivity.class);
                        itemView.findViewById(R.id.buttonRow).getContext().startActivity(intent);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            });

        }



        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                try {
                    mClickListener.onItemClick(view, getAdapterPosition());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) throws JSONException {
        return (String) mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }



    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position) throws JSONException;

    }



}