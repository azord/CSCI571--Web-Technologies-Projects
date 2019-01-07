package com.example.azamats.homework9;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Bitmap> {

    CustomAdapter(Context context, List<Bitmap> images) {
        super(context, R.layout.custom_row, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater azaInflater = LayoutInflater.from(getContext());
        View customView = azaInflater.inflate(R.layout.custom_row, parent, false);

        Bitmap singleImage = getItem(position);

        ImageView imageView = (ImageView) customView.findViewById(R.id.placePhotoList);


        imageView.setImageBitmap(singleImage);
        return customView;
    }
}
