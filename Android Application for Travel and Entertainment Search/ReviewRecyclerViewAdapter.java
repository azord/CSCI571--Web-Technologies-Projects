package com.example.azamats.homework9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String typeOfReview;
    private Context context;

    // data is passed into the constructor
    ReviewRecyclerViewAdapter(Context context, JSONArray data, String typeOfReview) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.typeOfReview = typeOfReview;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.i("reviewDataRECYCLERVIEW", mData.toString());

        if (typeOfReview.equals("google")) {
            try {
                JSONObject temporary = mData.getJSONObject(position);
                String author_name = temporary.getString("author_name");
                int rating = temporary.getInt("rating");
                String description = temporary.getString("text");
                int epochTime = temporary.getInt("time");
                String authorImageUrl = temporary.getString("profile_photo_url");
                Date dateFormat = new Date(epochTime * 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String convertedTime = sdf.format(dateFormat);
                holder.myTextView.setText(author_name);
                holder.myRatingBar.setRating(rating);
                holder.textReview.setText(description);
                holder.time.setText(convertedTime);
                Picasso.get().load(Uri.parse(authorImageUrl)).resize(160,150).into(holder.authorImage);
            } catch (JSONException e) { }
        } else {
            try {

                JSONObject temporary = mData.getJSONObject(position);

                JSONObject user = temporary.getJSONObject("user");

                String author_name = user.getString("name");

                int rating = temporary.getInt("rating");
                String description = temporary.getString("text");
                String epochTime = temporary.getString("time_created");
                String authorImageUrl = user.getString("image_url");

                holder.myTextView.setText(author_name);
                holder.myRatingBar.setRating(rating);
                holder.textReview.setText(description);
                holder.time.setText(epochTime);
                Picasso.get().load(Uri.parse(authorImageUrl)).resize(160,150).into(holder.authorImage);

            } catch (JSONException e) {}
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.length();
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return "AZA";
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        SimpleRatingBar myRatingBar;
        TextView textReview;
        TextView time;
        ImageView authorImage;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.authorName);
            myRatingBar = itemView.findViewById(R.id.reviewRatingBar);
            textReview = itemView.findViewById(R.id.reviewDescription);
            time = itemView.findViewById(R.id.dateReview);
            authorImage = itemView.findViewById(R.id.authorImage);

            //itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {

                    String url = "https://www.google.com";

                    try {
                        if (typeOfReview.equals("google")) {
                            JSONObject temporary = mData.getJSONObject(getAdapterPosition());
                            url = temporary.getString("author_url");
                        } else {
                            JSONObject temporary = mData.getJSONObject(getAdapterPosition());
                            url = temporary.getString("url");
                        }

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        v.getContext().startActivity(i);

                    } catch (JSONException e) {}



                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    /*String getItem(int id) {
        return mData.get(id);
    }*/

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
