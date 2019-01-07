package com.example.azamats.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment implements ReviewRecyclerViewAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View globalRootView;
    private JSONArray reviews = new JSONArray();
    private JSONArray yelpReviews = new JSONArray();
    private Context context;

    ReviewRecyclerViewAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
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
        globalRootView = inflater.inflate(R.layout.fragment_reviews, container, false);


        DetailsActivity activity = (DetailsActivity) getActivity();
        reviews = activity.getReviews();
        yelpReviews = activity.getYelpReviews();

        Log.i("GOOGLEREVIEWS", reviews.toString());


        final Spinner typeOfReview = (Spinner)globalRootView.findViewById(R.id.typeReview) ;
        final Spinner reviewSort = (Spinner)globalRootView.findViewById(R.id.reviewSort);

        typeOfReview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String valueOfSpinnerType = typeOfReview.getSelectedItem().toString();
                String valueOfSpinnerSort = reviewSort.getSelectedItem().toString();
                if (valueOfSpinnerSort.equals("Default order") && valueOfSpinnerType.equals("Google reviews")) {
                    // set up the RecyclerView
                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), reviews, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Highest rating") && valueOfSpinnerType.equals("Google reviews")) {
                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Lowest rating") && valueOfSpinnerType.equals("Google reviews")) {
                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Most recent") && valueOfSpinnerType.equals("Google reviews")) {

                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);


                } else if (valueOfSpinnerSort.equals("Least recent") && valueOfSpinnerType.equals("Google reviews")) {

                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Default order") && valueOfSpinnerType.equals("Yelp reviews")) {
                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), yelpReviews, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Highest rating") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Lowest rating") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);

                } else if (valueOfSpinnerSort.equals("Most recent") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time_created";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Least recent") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time_created";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        reviewSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                String valueOfSpinnerType = typeOfReview.getSelectedItem().toString();
                String valueOfSpinnerSort = reviewSort.getSelectedItem().toString();
                if (valueOfSpinnerSort.equals("Default order") && valueOfSpinnerType.equals("Google reviews")) {
                    // set up the RecyclerView
                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), reviews, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Highest rating") && valueOfSpinnerType.equals("Google reviews")) {
                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Lowest rating") && valueOfSpinnerType.equals("Google reviews")) {
                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Most recent") && valueOfSpinnerType.equals("Google reviews")) {

                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);


                } else if (valueOfSpinnerSort.equals("Least recent") && valueOfSpinnerType.equals("Google reviews")) {

                    JSONArray jsonArr = reviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < reviews.length(); i++) {
                            jsonValues.add(reviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "google");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Default order") && valueOfSpinnerType.equals("Yelp reviews")) {
                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), yelpReviews, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Highest rating") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Lowest rating") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "rating";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);

                } else if (valueOfSpinnerSort.equals("Most recent") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time_created";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return -valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                } else if (valueOfSpinnerSort.equals("Least recent") && valueOfSpinnerType.equals("Yelp reviews")) {
                    JSONArray jsonArr = yelpReviews;
                    JSONArray sortedJsonArray = new JSONArray();

                    try {
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviews.length(); i++) {
                            jsonValues.add(yelpReviews.getJSONObject(i));
                        }
                        Collections.sort( jsonValues, new Comparator<JSONObject>() {
                            private static final String KEY_NAME = "time_created";

                            @Override
                            public int compare(JSONObject a, JSONObject b) {
                                String valA = new String();
                                String valB = new String();

                                try {
                                    valA = (String) a.getString(KEY_NAME);
                                    valB = (String) b.getString(KEY_NAME);
                                }
                                catch (JSONException e) {
                                    //do something
                                }
                                //return valA.compareTo(valB);
                                //if you want to change the sort order, simply use the following:
                                return valA.compareTo(valB);
                            }
                        });

                        for (int i = 0; i < jsonArr.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                    } catch (JSONException e) {}


                    RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new ReviewRecyclerViewAdapter(getContext(), sortedJsonArray, "yelp");
                    //adapter.setClickListener();
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        // set up the RecyclerView
        /*RecyclerView recyclerView = globalRootView.findViewById(R.id.reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReviewRecyclerViewAdapter(getContext(), reviews, "google");
        //adapter.setClickListener();
        recyclerView.setAdapter(adapter);*/
        // Inflate the layout for this fragment
        return globalRootView;
    }

    public void onItemClick(View view, int position) {
        Log.i("HIAZA", Integer.toString(position));
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
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
