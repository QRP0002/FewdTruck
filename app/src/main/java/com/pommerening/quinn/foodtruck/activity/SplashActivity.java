package com.pommerening.quinn.foodtruck.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.fragment.MiniMapFragment;
import com.pommerening.quinn.foodtruck.pojo.GPSLocation;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.LocationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    private static final String URL = "http://192.168.1.72/webservice/loadlocation.php";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_TRUCK = "truck";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private static final int REQUEST_CODE = 200;

    private JSONArray mTruck = null;
    private ArrayList<HashMap<String, String>> mTruckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);
        new SplashBackground().execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
        GPSLocation gps = GPSLocation.locationSingleton(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private class SplashBackground extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            final int SPLASH_SHOW_TIME = 5000;
            runJSON();
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            LocationData.setLocationData(mTruckList);
            Intent i = new Intent(SplashActivity.this,
                    LoginActivity.class);
            startActivity(i);
            finish();
        }


        private void runJSON() {
            mTruckList = new ArrayList<HashMap<String, String>>();
            JSONParser jParser = new JSONParser();

            try {
                Log.d("Request", "Starting");
                JSONObject json = jParser.getJSONFromUrl(URL);
                mTruck = json.getJSONArray(TAG_POSTS);

                for (int i = 0; i < mTruck.length(); i++) {
                    JSONObject c = mTruck.getJSONObject(i);

                    String truck = c.getString(TAG_TRUCK);
                    String latitude = c.getString(TAG_LATITUDE);
                    String longitude = c.getString(TAG_LONGITUDE);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_TRUCK, truck);
                    map.put(TAG_LATITUDE, latitude);
                    map.put(TAG_LONGITUDE, longitude);
                    mTruckList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
