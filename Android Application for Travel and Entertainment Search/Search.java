package com.example.azamats.homework9;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //LocationManager mLocationManager;
    //private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;


    public Context mContext;
    private Gson gson;
    private Handler handler;
    private ProgressDialog dialog;
    private String specificLocation = "null";


    EditText keywordInput;
    Button searchButton;
    Button clearButton;
    AutoCompleteTextView specify_location_input;
    EditText distance_input;
    TextView mandatoryKeyword;
    TextView mandatoryLocation;
    RadioGroup locationGroup;
    RadioButton currentRadioButton;
    RadioButton specifyRadioButton;
    Spinner category_input;
    AutoCompleteTextView searchPlace;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;



    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        final View rootView = inflater.inflate(R.layout.fragment_search,container,false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        keywordInput = (EditText)rootView.findViewById(R.id.keyword_input);
        searchButton = (Button)rootView.findViewById(R.id.search_button);
        clearButton = (Button)rootView.findViewById(R.id.clear_button);
        locationGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        //specify_location_input = (EditText) rootView.findViewById(R.id.specify_location_input);
        specify_location_input =  (AutoCompleteTextView) rootView.findViewById(R.id.specify_location_input);
        currentRadioButton = (RadioButton) rootView.findViewById(R.id.current);
        specifyRadioButton = (RadioButton) rootView.findViewById(R.id.specify);
        mandatoryKeyword = (TextView) rootView.findViewById(R.id.keywordMandatory);
        mandatoryLocation = (TextView) rootView.findViewById(R.id.locationMandatory);
        distance_input = (EditText) rootView.findViewById(R.id.distance_input);
        category_input = (Spinner)rootView.findViewById(R.id.spinner);
       //searchPlace = (AutoCompleteTextView) rootView.findViewById(R.id.search_place);




        specify_location_input.setEnabled(false);

        mandatoryKeyword.setVisibility(View.GONE);
        mandatoryLocation.setVisibility(View.GONE);


        locationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.current) {
                    specify_location_input.setEnabled(false);
                    specificLocation = "current";

                } else  if (checkedId == R.id.specify) {
                    specify_location_input.setEnabled(true);
                    specificLocation = "specify";
                }
            }
        });


        specify_location_input.setAdapter(new PlacesAutoCompleteAdapterMain(getActivity(), R.layout.autocomplete_list_item));


        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
                {
                    keywordInput.setText("");
                    distance_input.setText("");
                    specify_location_input.setText("");
                    currentRadioButton.setChecked(true);
                    category_input.setSelection(0);
                    mandatoryKeyword.setVisibility(View.GONE);
                    mandatoryLocation.setVisibility(View.GONE);

                }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String inputK = keywordInput.getText().toString();
                String inputL = specify_location_input.getText().toString();
                String message = "Please fix all fields with errors";

                 Snackbar mySnackbar = Snackbar.make(rootView.findViewById(R.id.snackbar_text), message, Snackbar.LENGTH_SHORT);

                if (inputK.matches("") && currentRadioButton.isChecked()) {
                    mandatoryKeyword.setVisibility(View.VISIBLE);

                    View sbView = mySnackbar.getView();

                    TextView tv = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.BLACK);

                    sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightGrey));

                    sbView.setBackgroundResource(R.drawable.rounder_corner);

                    GradientDrawable drawable = (GradientDrawable) sbView.getBackground();

                    mySnackbar.show();

                } else if (inputK.matches("") && specifyRadioButton.isChecked() && inputL.matches("")) {
                    mandatoryKeyword.setVisibility(View.VISIBLE);
                    mandatoryLocation.setVisibility(View.VISIBLE);

                } else if (!inputK.matches("") && specifyRadioButton.isChecked() && inputL.matches("")) {
                    mandatoryLocation.setVisibility(View.VISIBLE);
                } else if (inputK.matches("") && specifyRadioButton.isChecked() && !inputL.matches("")) {
                    mandatoryKeyword.setVisibility(View.VISIBLE);
                } else {

                    /*getActivity().finish();
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    startActivity(intent);*/

                    final ProgressDialog nDialog;
                    nDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
                    nDialog.setMessage("Fetching results");
                    nDialog.setIndeterminate(true);
                    nDialog.setCancelable(true);
                    nDialog.show();

                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getContext());

                    String fromDistance = distance_input.getText().toString();
                    String category = category_input.getSelectedItem().toString();
                    String keyword = keywordInput.getText().toString();

                    if (fromDistance.matches("")) {
                        fromDistance = "10.0";
                    }

                    Double distance = Double.parseDouble(fromDistance) * 16093.0;

                    String url ="http://571hw8.us-east-2.elasticbeanstalk.com/coordinates/34.024957,-118.285674/radius/" + distance.toString() + "/type/" + category + "/keyword/" + keyword;

                    if (specificLocation.equals("null") || specificLocation.equals("current")) {

                        url = "http://571hw8.us-east-2.elasticbeanstalk.com/coordinates/34.024957,-118.285674/radius/" + distance.toString() + "/type/" + category + "/keyword/" + keyword;

                    } else if (specificLocation.equals("specify")) {

                        url = "http://571hw8.us-east-2.elasticbeanstalk.com/specificLocation/" + specify_location_input.getText().toString() + "/radius/" + distance.toString() + "/type/" + category + "/keyword/" + keyword;
                    }

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
                                        for (int i =0; i < results.length(); i++) {
                                            JSONObject object = results.getJSONObject(i);
                                            names[i] = object.getString("name");
                                        }

                                        String[] icons = new String[results.length()];
                                        for (int i =0; i < results.length(); i++) {
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


                                        Intent intent = new Intent(getActivity(), RecyclerActivity.class);
                                        intent.putExtra("next_page", next_page_token);
                                        intent.putExtra("names", names);
                                        intent.putExtra("icons", icons);
                                        intent.putExtra("vicinities", vicinities);
                                        intent.putExtra("placeIds", placeIds);
                                        intent.putExtra("previous", false);
                                        startActivity(intent);

                                    }
                                    catch (JSONException e) { }


                                    nDialog.dismiss();



                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("11111", "Doesnt work");
                        }
                    });


                    queue.add(stringRequest);

                }
            }
        });


        return rootView;
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
