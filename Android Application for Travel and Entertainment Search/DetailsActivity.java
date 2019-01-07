package com.example.azamats.homework9;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity implements InfoFragment.OnFragmentInteractionListener, PhotosFragment.OnFragmentInteractionListener, MapsFragment.OnFragmentInteractionListener, ReviewsFragment.OnFragmentInteractionListener {

    private String address = "null";
    private String phone = "null";
    private String price = "null";
    private String rating = "null";
    private String googlePage = "null";
    private String website = "null";
    private String yelpAddress = "";
    private String placeID = "null";
    private String placeIDFromPrevious = "null";
    private String yelpCity = "null";
    private String yelpState = "null";
    private String yelpCountry = "null";
    private String yelpPostal = "null";
    private LatLng coordinatesOfPlace;
    private FragmentActivity myContext;
    private String nameOfThePlace = "null";
    private JSONArray reviews = new JSONArray();
    private JSONArray address_components = new JSONArray();
    private JSONArray yelpReviews = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_details);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutDetails);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.info_tab_layout));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.photos_tab_layout));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.map_tab_layout));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.reviews_tab_layout));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pagerDetails);
        final DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    RequestQueue queue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        placeIDFromPrevious = bundle.getString("placeId");

        String url ="http://hw9-env.us-east-2.elasticbeanstalk.com/placeid/" + placeIDFromPrevious;

        Log.i("simpleURL", url);

        final ProgressDialog nDialog;
        nDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        nDialog.setMessage("Fetching details");
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(true);
        nDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle = new Bundle();


                        ImageView backButton = (ImageView) findViewById(R.id.backToResult);
                        backButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                finish();
                            }
                        });


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("result");

                            ////////////////////////puts name of the place
                            nameOfThePlace = result.getString("name");
                            TextView nameOfPlace = (TextView)findViewById(R.id.detailName);
                            nameOfPlace.setText(nameOfThePlace);
                            /////////////////////////////////////////////
                            if (result.has("formatted_address")) {
                               address = result.getString("formatted_address");
                            }

                            if (result.has("formatted_phone_number")) {
                                phone = result.getString("formatted_phone_number");
                            }
                            if (result.has("price_level")) {
                                price = result.getString("price_level");
                            }
                            if (result.has("rating")) {
                                rating = result.getString("rating");
                            }
                            if (result.has("url")) {
                                googlePage = result.getString("url");
                            }
                            if (result.has("website")) {
                                website = result.getString("website");
                            } else {
                                website = googlePage;
                            }
                            if (result.has("reviews")) {
                                reviews = result.getJSONArray("reviews");
                            }

                            if (result.has("address_components")) {
                                address_components = result.getJSONArray("address_components");
                            }

                            for (int i = 0; i < address_components.length(); i++) {
                                JSONObject locality = address_components.getJSONObject(i);

                                JSONArray types = locality.getJSONArray("types");
                                String typeOfAddress = types.getString(0);

                                if (typeOfAddress.equals("locality")) {
                                    yelpCity = locality.getString("long_name");
                                }
                                if (typeOfAddress.equals("administrative_area_level_1")) {
                                    yelpState = locality.getString("short_name");
                                }
                                if (typeOfAddress.equals("country")) {
                                    yelpCountry = locality.getString("short_name");
                                }
                                if (typeOfAddress.equals("street_number")) {
                                    yelpAddress = yelpAddress + locality.getString("short_name");
                                }
                                if (typeOfAddress.equals("route")) {
                                    yelpAddress = yelpAddress + locality.getString("short_name");
                                }
                            }

                            if (yelpAddress.equals("")) {
                                yelpAddress = result.getString("vicinity");
                            }

                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject locationCoordinates = geometry.getJSONObject("location");

                            double geometryLat = locationCoordinates.getDouble("lat");
                            double geometryLng = locationCoordinates.getDouble("lng");

                            coordinatesOfPlace = new LatLng(geometryLat, geometryLng);


                            //////////////////////////////////link to twitter
                            final String twitterUrl = "https://twitter.com/intent/tweet?text=Check out " + nameOfThePlace + " located at " + address + ". Website:&url=" + website+ "&hashtags=TravelAndEntertainmentSearch";

                            ImageView twitterButton = (ImageView) findViewById(R.id.shareTwitter);

                            twitterButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(twitterUrl));
                                    startActivity(i);
                                }
                            });
                            //////////////////////////////////////////////////
                            SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = favorites.edit();
                            editor.apply();

                            final String placeIDOfThePlace = result.getString("place_id");

                            placeID = placeIDOfThePlace;

                            ArrayList<String> favKeys = new ArrayList<String>();

                            Map<String, ?> allEntries = favorites.getAll();

                                Boolean itIsFavorite = false;

                                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                                    if ((entry.getKey()).equals(placeIDOfThePlace)) {
                                        itIsFavorite = true;
                                    }
                                }

                                final ImageView favoriteImage = (ImageView) findViewById(R.id.isFavorite);

                                if (itIsFavorite) {
                                    favoriteImage.setImageResource(R.drawable.heart_fill_white);
                                    favoriteImage.setTag(R.drawable.heart_fill_white);
                                } else {
                                    favoriteImage.setImageResource(R.drawable.heart_outline_white);
                                    favoriteImage.setTag(R.drawable.heart_outline_white);
                                }
                            //////////////////////////////////////////////////////////
                            // Instantiate the RequestQueue.
                            RequestQueue queueYelp = Volley.newRequestQueue(getBaseContext());
                            String urlYelp ="http://hw9-env.us-east-2.elasticbeanstalk.com/yelp/name/" + nameOfThePlace + "/address1/" + yelpAddress + "/city/" + yelpCity + "/state/" + yelpState + "/country/" + yelpCountry + "/postal_code/" + yelpPostal + "/latitude/" + Double.toString(geometryLat) + "/longitude/" + Double.toString(geometryLng);

                            Log.i("yelpURL", urlYelp);
                            StringRequest stringRequestYelp = new StringRequest(Request.Method.GET, urlYelp,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("No result")) {
                                            } else {

                                                try {

                                                    JSONObject jsonObjectYelp = new JSONObject(response);
                                                    yelpReviews = jsonObjectYelp.getJSONArray("reviews");

                                                    Log.i("yelpREVIEWS", yelpReviews.toString());

                                                } catch (JSONException e) { }
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            queueYelp.add(stringRequestYelp);

                            //////////////////////////////////////////////////////////////

                            final String iconOfThePlace = result.getString("icon");

                            favoriteImage.setOnClickListener(new View.OnClickListener() {

                                SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = favorites.edit();

                                @Override
                                public void onClick(View view) {
                                    int drawableId = (int)favoriteImage.getTag();
                                    Log.i("54321", Integer.toString(drawableId));
                                    if (drawableId == 2131230842) {
                                        favoriteImage.setImageResource(R.drawable.heart_fill_white);
                                        favoriteImage.setTag(R.drawable.heart_fill_white);

                                        String jsonStr = "[{\"name\":\"" + nameOfThePlace + "\",\"icon\":\"" + iconOfThePlace + "\",\"address\":\"" + address + "\"}]";
                                        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                                        editor.putString(placeIDOfThePlace, jsonStr);
                                        editor.commit();


                                        //Toast.makeText(this, nameOfThePlace + " was added to favorites ", Toast.LENGTH_SHORT).show();

                                    } else  {
                                        favoriteImage.setImageResource(R.drawable.heart_outline_white);
                                        favoriteImage.setTag(R.drawable.heart_outline_white);

                                        editor.remove(placeIDOfThePlace);
                                        editor.commit();
                                    }
                                }
                            });


                            nDialog.dismiss();

                            int InfoTab = 0;
                            int PhotoTab = 1;
                            int MapTab = 2;

                            InfoFragment fragment = (InfoFragment) getSupportFragmentManager().getFragments().get(InfoTab);
                            getSupportFragmentManager().beginTransaction()
                                    .detach(fragment)
                                    .attach(fragment)
                                    .commit();

                            PhotosFragment photoFragment = (PhotosFragment) getSupportFragmentManager().getFragments().get(PhotoTab);
                            getSupportFragmentManager().beginTransaction()
                                    .detach(photoFragment)
                                    .attach(photoFragment)
                                    .commit();


                        }
                        catch (JSONException e) { }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("11111", "Doesnt work");
            }
        });

        queue.add(stringRequest);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }



    public String getAddress(){
        return address;
    }

    public String getPhone(){
        return phone;
    }

    public String getPrice(){
        return price;
    }

    public String getRating(){
        return rating;
    }

    public String getGooglePage(){
        return googlePage;
    }

    public String getWebsite(){
        return website;
    }

    public String getPlaceId() {
        return placeID;
    }

    public LatLng getCoordinatesOfPlace() {
        return coordinatesOfPlace;
    }

    public String getName() {return nameOfThePlace;}

    public JSONArray getReviews() { return reviews; }

    public JSONArray getYelpReviews() {
        Log.i("reviewDataYElp", yelpReviews.toString());
        return yelpReviews; }

    @Override
    public void onFragmentInteraction(Uri uri) {}

}
