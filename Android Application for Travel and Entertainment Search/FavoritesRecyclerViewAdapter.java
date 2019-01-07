package com.example.azamats.homework9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mPlaceID;
    private List<String> mIcon;
    private List<String> mVicinity;
    private LayoutInflater mInflater;
    private List<String> favKeys;
    private ItemClickListener mClickListener;
    public List<String> temporaryFavs = new ArrayList<>();
    public int globalPosition;
    public Context context;

    // data is passed into the constructor
    FavoritesRecyclerViewAdapter(Context context, List<String> data, List<String> icons, List<String> placeIds, List<String> addresses, List<String> favKeys) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mPlaceID = placeIds;
        this.mIcon = icons;
        this.mVicinity = addresses;
        this.favKeys = favKeys;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_favorites_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        String icon = mIcon.get(position);
        String vicinity = mVicinity.get(position);
        String placeId = mPlaceID.get(position);
        globalPosition =  position;


        holder.myTextView.setText(animal);
        holder.subText.setText(vicinity);

        holder.myFavoriteView.setTag(R.drawable.heart_fill_red);
        holder.myFavoriteView.setTag(R.drawable.heart_fill_red);

        Picasso.get().load(Uri.parse(mIcon.get(position))).into(holder.myImageView);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView subText;
        TextView placeID;
        ImageView myImageView;
        ImageView myFavoriteView;


        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.img_android);
            myFavoriteView = itemView.findViewById(R.id.img_favorites);
            subText = itemView.findViewById(R.id.address);
            myTextView = itemView.findViewById(R.id.tvAnimalName);

            Picasso.get().load(Uri.parse(mIcon.get(globalPosition))).into(myImageView);
            myFavoriteView.setImageResource(R.drawable.heart_fill_red);
            myFavoriteView.setTag(R.drawable.heart_fill_red);


            itemView.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    intent.putExtra("placeId", mPlaceID.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);

                }
            });

            myFavoriteView.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {


                    if (v instanceof ImageView){


                        //SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
                        //SharedPreferences.Editor editor = favorites.edit();

                        Log.i("logo", v.getTag().toString());

                        if((v.getTag().toString()).equals("2131230841")) {
                            ImageView myFavoriteView1 = (ImageView) v.findViewById(R.id.img_favorites);
                            myFavoriteView1.setImageResource(R.drawable.heart_fill_red);
                            myFavoriteView1.setTag(R.drawable.heart_fill_red);
                            temporaryFavs.add(mPlaceID.get(getAdapterPosition()));

                            if (mClickListener != null) {
                                //mClickListener.onItemClick(view, getAdapterPosition(), 'a');
                            }
                        }
                        else {
                            ImageView myFavoriteView1 = (ImageView) v.findViewById(R.id.img_favorites);
                            myFavoriteView1.setImageResource(R.drawable.heart_outline_black);
                            myFavoriteView1.setTag(R.drawable.heart_outline_black);
                            temporaryFavs.remove(mPlaceID.get(getAdapterPosition()));

                            //favKeys.set(getAdapterPosition(), "false");

                            if (mClickListener != null) {
                                //mClickListener.onItemClick(view, getAdapterPosition(), 'd');
                            }
                        }
                    }
                    else {
                        Log.i("54321", "12345");

                    }



                }
            });

            //myFavoriteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view instanceof ImageView){

                Log.i("logo", view.getTag().toString());

                if((view.getTag().toString()).equals("2131230841")) {
                    ImageView myFavoriteView1 = (ImageView) view.findViewById(R.id.img_favorites);
                    myFavoriteView1.setImageResource(R.drawable.heart_fill_red);
                    myFavoriteView1.setTag(R.drawable.heart_fill_red);
                    temporaryFavs.add(mPlaceID.get(getAdapterPosition()));

                    if (mClickListener != null) {
                        //mClickListener.onItemClick(view, getAdapterPosition(), 'a');
                    }
                }
                else {
                    ImageView myFavoriteView1 = (ImageView) view.findViewById(R.id.img_favorites);
                    myFavoriteView1.setImageResource(R.drawable.heart_outline_black);
                    myFavoriteView1.setTag(R.drawable.heart_outline_black);
                    temporaryFavs.remove(mPlaceID.get(getAdapterPosition()));

                    favKeys.set(getAdapterPosition(), "false");

                    if (mClickListener != null) {
                        //mClickListener.onItemClick(view, getAdapterPosition(), 'd');
                    }
                }
            }
            else {
                Log.i("54321", "12345");

            }
        }
    }


    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    String getIcon(int id) {
        return mIcon.get(id);
    }

    String getPlaceId(int id) {
        return mPlaceID.get(id);
    }

    String getVicinity(int id) {
        return mVicinity.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


