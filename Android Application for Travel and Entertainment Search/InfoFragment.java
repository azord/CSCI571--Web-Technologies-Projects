package com.example.azamats.homework9;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String address;
    public String phone;
    public String price;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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

    public void dataFromActivity(String address){
        this.address = address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_info,container,false);


        //Log.i("fragmentId", Integer.toString(i));


        DetailsActivity activity = (DetailsActivity) getActivity();
        String address = activity.getAddress();
        String phone = activity.getPhone();
        String price = activity.getPrice();
        String rating = activity.getRating();
        String url = activity.getGooglePage();

        String website = null;
        website = activity.getWebsite();

        //TableLayout infoTable = (TableLayout) rootView.findViewById(R.id.infoTable);
        TextView detailAddress = (TextView)rootView.findViewById(R.id.detailDescriptionAddress);
        TextView detailPhone = (TextView)rootView.findViewById(R.id.detailDescriptionPhone);
        TextView detailPrice = (TextView)rootView.findViewById(R.id.detailDescriptionPrice);
        TextView detailUrl = (TextView)rootView.findViewById(R.id.detailDescriptionUrl);
        TextView detailWebsite = (TextView)rootView.findViewById(R.id.detailDescriptionWebsite);

        if (address.equals("null")) {
            TableRow addressRow = (TableRow)rootView.findViewById(R.id.detailAddress);
            addressRow.setVisibility(View.GONE);
        } else {
            detailAddress.setText(address);
        }

        if (phone.equals("null")) {
            TableRow phoneRow = (TableRow)rootView.findViewById(R.id.detailPhone);
            phoneRow.setVisibility(View.GONE);
        } else {

            detailPhone.setText(phone);
        }

        if (price.equals("null")) {
           TableRow priceRow = (TableRow)rootView.findViewById(R.id.detailPrice);
           priceRow.setVisibility(View.GONE);
        } else {
            if (price.equals("0")) {
                detailPrice.setText("$");
            } else if (price.equals("1")) {
                detailPrice.setText("$");
            } else if (price.equals("2")) {
                detailPrice.setText("$$");
            } else if (price.equals("3")) {
                detailPrice.setText("$$$");
            } else if (price.equals("4")) {
                detailPrice.setText("$$$$");
            }
        }

        if (rating.equals("null")) {
            TableRow ratingRow = (TableRow) rootView.findViewById(R.id.detailRatingBar);
            ratingRow.setVisibility(View.GONE);
        } else {


            TableRow ratingRow = (TableRow) rootView.findViewById(R.id.detailRatingBar);
            ratingRow.setVisibility(View.GONE);
            Float number = Float.parseFloat(rating);

            SimpleRatingBar detailRatingBar = new SimpleRatingBar(getActivity());

            detailRatingBar = (SimpleRatingBar) rootView.findViewById(R.id.pop_ratingbar);

            Log.i("ratingBar", Float.toString(detailRatingBar.getRating()));
            detailRatingBar.setRating(4.0f);
            Log.i("ratingBar", Float.toString(detailRatingBar.getRating()));
            ratingRow.setVisibility(View.VISIBLE);
        }

        detailUrl.setText(url);

        if (website.equals("null")) {
            detailWebsite.setText(url);
        } else {
            detailWebsite.setText(website);
        }

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
