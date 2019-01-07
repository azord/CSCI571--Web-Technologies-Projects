package com.example.azamats.homework9;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecyclerActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    Button previousButton;
    Button nextButton;
    String nextPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        nextButton = (Button)findViewById(R.id.next_button);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.resultBack);
        mToolbar.setTitle("Search Results");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        Bundle bundle = getIntent().getExtras();
        nextPage = bundle.getString("next_page");
        Boolean previous = bundle.getBoolean("previous");
        String[] names = bundle.getStringArray("names");
        String[] icons = bundle.getStringArray("icons");
        String[] vicinities = bundle.getStringArray("vicinities");
        String[] placeIds = bundle.getStringArray("placeIds");

        previousButton = (Button) findViewById(R.id.previous_button);

        if (previous == false) {
            previousButton.setEnabled(false);
        } else {
            previousButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        for (int i =0; i < names.length; i++) {
            animalNames.add(names[i]);
        }

        ArrayList<String> animalIcons = new ArrayList<>();
        for (int i =0; i < names.length; i++) {
            animalIcons.add(icons[i]);
        }

        ArrayList<String> animalVicinities = new ArrayList<>();
        for (int i =0; i < names.length; i++) {
            animalVicinities.add(vicinities[i]);
        }

        ArrayList<String> animalPlaceIds = new ArrayList<>();
        for (int i =0; i < names.length; i++) {
            animalPlaceIds.add(placeIds[i]);
        }

        if (nextPage.equals("false")) {
            nextButton.setEnabled(false);
        }
        else {



            nextButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    final ProgressDialog nDialog = new ProgressDialog(RecyclerActivity.this ,R.style.AppCompatAlertDialogStyle);
                    nDialog.setMessage("Fetching next page");
                    nDialog.setIndeterminate(true);
                    nDialog.setCancelable(true);
                    nDialog.show();

                    String url = "http://571hw8.us-east-2.elasticbeanstalk.com/pagetoken/" + nextPage;

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {



                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        String next_page_token = "false";

                                        if (jsonObject.has("next_page_token")) {
                                            next_page_token = jsonObject.getString("next_page_token");
                                        }
                                        JSONArray results = jsonObject.getJSONArray("results");

                                        String[] names = new String[results.length()];
                                        for (int i = 0; i < results.length(); i++) {
                                            JSONObject object = results.getJSONObject(i);
                                            names[i] = object.getString("name");
                                        }

                                        String[] icons = new String[results.length()];
                                        for (int i = 0; i < results.length(); i++) {
                                            JSONObject object = results.getJSONObject(i);
                                            icons[i] = object.getString("icon");
                                        }

                                        String[] vicinities = new String[results.length()];
                                        for (int i = 0; i < results.length(); i++) {
                                            JSONObject object = results.getJSONObject(i);
                                            vicinities[i] = object.getString("vicinity");
                                        }

                                        String[] placeIds = new String[results.length()];
                                        for (int i = 0; i < results.length(); i++) {
                                            JSONObject object = results.getJSONObject(i);
                                            placeIds[i] = object.getString("place_id");
                                        }


                                        Intent intent = new Intent(RecyclerActivity.this, RecyclerActivity.class);
                                        intent.putExtra("next_page", next_page_token);
                                        intent.putExtra("names", names);
                                        intent.putExtra("icons", icons);
                                        intent.putExtra("vicinities", vicinities);
                                        intent.putExtra("placeIds", placeIds);
                                        intent.putExtra("previous", true);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                    }

                                    nDialog.dismiss();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("2345", "Doesnt work");
                        }
                    });


                    queue.add(stringRequest);

                }
            });
        }

        SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = favorites.edit();
        editor.apply();

        ArrayList<String> favKeys = new ArrayList<String>();

        //isFavorite = false;
        //String placeId = mPlaceID.get(globalPosition);

        /*for(int i = 0; i < favKeys.size(); i++) {
        }*/

        Map<String, ?> allEntries = favorites.getAll();

        for (int i =0; i < names.length; i++) {

            Boolean itIsFavorite = false;

            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                if ((entry.getKey()).equals(placeIds[i])) {
                   itIsFavorite = true;
                }
            }

            if (itIsFavorite) {
                favKeys.add("true");
            } else {
                favKeys.add("false");
            }

        }


        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, animalNames, animalIcons, animalPlaceIds, animalVicinities, favKeys);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position, char action) {

        SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = favorites.edit();

    if (action == 'a') {
        String jsonStr = "[{\"name\":\"" + adapter.getItem(position) + "\",\"icon\":\"" + adapter.getIcon(position) + "\",\"address\":\"" + adapter.getVicinity(position) + "\"}]";
        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
        editor.putString(adapter.getPlaceId(position), jsonStr);
        editor.commit();

        Toast.makeText(this, adapter.getItem(position) + " was added to favorites ", Toast.LENGTH_SHORT).show();

    } else {
        editor.remove(adapter.getPlaceId(position));
        editor.commit();

        Toast.makeText(this, adapter.getItem(position) + " was removed from favorites ", Toast.LENGTH_SHORT).show();
    }

   // editor.clear();
   // editor.commit();

    }


}
