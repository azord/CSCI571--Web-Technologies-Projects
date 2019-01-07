package com.example.azamats.homework9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DetailsPagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public DetailsPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.numOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                InfoFragment infoTab = new InfoFragment();
                return infoTab;
            case 1:
                PhotosFragment photosTab = new PhotosFragment();
                return photosTab;
            case 2:
                MapsFragment mapsTab = new MapsFragment();
                return mapsTab;
            case 3:
                ReviewsFragment reviewsTab = new ReviewsFragment();
                return reviewsTab;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }


}
