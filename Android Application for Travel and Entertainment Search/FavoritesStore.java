package com.example.azamats.homework9;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FavoritesStore extends MainActivity {
    public String placeId;
    public String name;
    public String icon;
    public String vicinity;
    SharedPreferences favorites = getSharedPreferences("favs", Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = favorites.edit();




    FavoritesStore() {
        /*this.placeId =placeId;
        this.name = name;
        this.icon = icon;
        this.vicinity = vicinity;*/
    }



    JSONObject getFavs(String placeId) {

        String json1 = favorites.getString(placeId, "");

        JSONObject jObj = null;
        try {
            //String jsonStr = "[{\"pmid\":\"2\",\"name\":\" MANAGEMENT\",\"result\":\"1\",\"properties\":[{\"prop_id\":\"32\",\"prop_name\":\"Bonneville\",\"address\":\"122 Lakeshore\",\"city\":\"Ripley\",\"state\":\"OH\",\"zip\":\"11454\",\"lat\":\"41.123\",\"long\":\"-85.5034\"}]}]";
            //

            jObj = new JSONObject(json1);


            //Log.i("54362", jObj.get("icon").toString());


            //JSONArray jArr = jObj.getJSONArray("properties");

            //JSONObject c = jArr.getJSONObject(0);


        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jObj;
    }



    public void setFav(String placeId, String name, String icon, String vicinity) {

        editor.clear();

        String jsonStr = "[{\"name\":\"" + name + "\",\"icon\":\"" + icon + "\",\"address\":\"" + vicinity + "\"}]";

        jsonStr = jsonStr.substring(1, jsonStr.length()-1);

        editor.putString(placeId, jsonStr);
        editor.commit();
    }









}
