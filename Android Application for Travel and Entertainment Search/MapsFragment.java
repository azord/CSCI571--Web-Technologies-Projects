package com.example.azamats.homework9;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View globalRootView;
    private FragmentActivity myContext;
    private GoogleMap mMap;
    private int REQ_PERMISSION;
    private String description;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LatLng detailLocation;
    private String nameOfThePlace = "null";
    private String API_KEY = "AIzaSyC_3v8MdqVkzZPpBwVDQklULLTwbWIHDcQ";
    private String textSpinner;
    private ArrayList<LatLng> polylineCoords = new ArrayList<LatLng>();
    private Marker source;
    private Marker destination;
    private LatLng startingPosition;
    private LatLng endingPosition;
    private Polyline polylineFinal;
    //private Polyline polylineOld = mMap.addPolyline(new PolylineOptions());


    private OnFragmentInteractionListener mListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        globalRootView = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        DetailsActivity activity = (DetailsActivity) getActivity();
        detailLocation = activity.getCoordinatesOfPlace();
        nameOfThePlace = activity.getName();


        Spinner mySpinner=(Spinner) globalRootView.findViewById(R.id.modeSpinner);
        textSpinner = mySpinner.getSelectedItem().toString();


        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) globalRootView.findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String travel_mode = "null";

                Spinner mySpinner=(Spinner) globalRootView.findViewById(R.id.modeSpinner);
                textSpinner = mySpinner.getSelectedItem().toString();

                description = (String) parent.getItemAtPosition(position);

                if (textSpinner.equals("Driving")) {
                    travel_mode = "driving";
                } else if (textSpinner.equals("Bicycling")) {
                    travel_mode = "bicycling";
                } else if (textSpinner.equals("Transit")) {
                    travel_mode = "transit";
                } else if (textSpinner.equals("Walking")) {
                    travel_mode = "walking";
                }

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        description + "&destination=" +
                        nameOfThePlace +
                        "&mode=" +
                        travel_mode +
                        "&key=" + API_KEY;

                Log.i("URIL", url);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                //mTextView.setText("Response is: "+ response.substring(0,500));

                                //Log.i("Documentation", response.toString());

                                SetPolylineCoords(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mTextView.setText("That didn't work!");
                    }
                });

                queue.add(stringRequest);
            }
        });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Spinner mySpinner=(Spinner) globalRootView.findViewById(R.id.modeSpinner);
                textSpinner = mySpinner.getSelectedItem().toString();

                String fromText = autocompleteView.getText().toString();

                String travel_mode = "null";

                if (textSpinner.equals("Driving")) {
                    travel_mode = "driving";
                } else if (textSpinner.equals("Bicycling")) {
                    travel_mode = "bicycling";
                } else if (textSpinner.equals("Transit")) {
                    travel_mode = "transit";
                } else if (textSpinner.equals("Walking")) {
                    travel_mode = "walking";
                }

                if (fromText.equals("")) {

                } else {



                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url ="https://maps.googleapis.com/maps/api/directions/json?origin=" +
                            fromText + "&destination=" +
                            nameOfThePlace +
                            "&mode=" +
                            travel_mode +
                            "&key=" + API_KEY;


                    Log.i("URIL", url);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    SetPolylineCoords(response);;

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

                    queue.add(stringRequest);
                }

                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        return globalRootView;
    }


    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        } else {

            if (nameOfThePlace.equals("null")) {
                LatLng point = new LatLng(-33.867, 151.206);
                //map.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

                mMap.addMarker(new MarkerOptions()
                        .title("Sydney")
                        .position(point))
                        .showInfoWindow();
            }
            else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(detailLocation, 16));

                mMap.addMarker(new MarkerOptions()
                        .title(nameOfThePlace)
                        .position(detailLocation))
                        .showInfoWindow();
            }

        }
    }

    public void SetPolylineCoords (String json) {

        try {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray routes = jsonObject.getJSONArray("routes");
        JSONObject nil = routes.getJSONObject(0);
        JSONArray legs = nil.getJSONArray("legs");
        JSONObject legs_nil = legs.getJSONObject(0);
        JSONObject start_location = legs_nil.getJSONObject("start_location");

        String start_lat = start_location.getString("lat");
        String start_lng = start_location.getString("lng");

        double latitude = Double.parseDouble(start_lat);
        double longitude = Double.parseDouble(start_lng);
        LatLng location = new LatLng(latitude, longitude);
        JSONArray steps = legs_nil.getJSONArray("steps");

            polylineCoords.clear();


            for (int i =0; i < steps.length(); i++) {

            JSONObject end_location = steps.getJSONObject(i);
            JSONObject end_location_coords = end_location.getJSONObject("end_location");

                    JSONObject start_location_steps = steps.getJSONObject(i);
                    JSONObject start_location_steps_coords = start_location_steps.getJSONObject("start_location");

                    String start_lat_steps = start_location_steps_coords.getString("lat");
                    String start_lng_steps = start_location_steps_coords.getString("lng");

                    double latitude_start = Double.parseDouble(start_lat_steps);
                    double longitude_start = Double.parseDouble(start_lng_steps);

                    polylineCoords.add(new LatLng(latitude_start, longitude_start));

                    if (i == 0) {
                        startingPosition = new LatLng(latitude_start, longitude_start);
                    }

            String end_lat = end_location_coords.getString("lat");
            String end_lng = end_location_coords.getString("lng");

            double latitude_end = Double.parseDouble(end_lat);
            double longitude_end = Double.parseDouble(end_lng);

            polylineCoords.add(new LatLng(latitude_end, longitude_end));
            if (i == steps.length() - 1) {
                endingPosition = new LatLng(latitude_end, longitude_end);
            }
        }
            mMap.clear();
            PolylineOptions rectOptions = new PolylineOptions();

            for (int s=0; s<polylineCoords.size(); s++) {
                rectOptions.add((polylineCoords.get(s)));
            }
            rectOptions.color(Color.BLUE).geodesic(true);

            polylineFinal = mMap.addPolyline(rectOptions);

            source = mMap.addMarker(new MarkerOptions().position(startingPosition).title(description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            destination = mMap.addMarker(new MarkerOptions().position(endingPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            source.showInfoWindow();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(source.getPosition());
            builder.include(destination.getPosition());

            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.45); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.animateCamera(cu);

        }
        catch (JSONException e) { }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
