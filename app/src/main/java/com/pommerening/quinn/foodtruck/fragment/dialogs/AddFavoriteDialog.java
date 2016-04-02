package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFavoriteDialog extends DialogFragment {

    private String mUsername;
    private String mProductID;
    private String mProductPrice;
    private String mProductName;
    private String mTruckName;
    private Dialog mDialog;
    private Button mCancelButton;
    private Button mAddButton;
    private Button mOrderButton;

    private TextView mProductNameTV;
    private TextView mProductPriceTV;
    private TextView mTruckNameTV;

    private static final String URL = "http://192.168.1.72:80/webservice/addfavorites.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static AddFavoriteDialog newInstance(String username, String productID,
                                                String productName, String productPrice ,
                                                String truckName) {
        AddFavoriteDialog fragment = new AddFavoriteDialog();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("productID", productID);
        args.putString("productPrice", productPrice);
        args.putString("productName", productName);
        args.putString("truckName", truckName);
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString("username");
            mProductID = getArguments().getString("productID");
            mProductPrice = getArguments().getString("productPrice");
            mProductName = getArguments().getString("productName");
            mTruckName = getArguments().getString("truckName");
        }
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_add_favorite_dialog);
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_favorite_dialog, container, false);

        mProductNameTV = (TextView) view.findViewById(R.id.favorite_prod_name);
        mProductNameTV.setText(mProductName);
        mProductPriceTV = (TextView) view.findViewById(R.id.favorite_prod_price);
        mProductPriceTV.setText(mProductPrice);
        mTruckNameTV = (TextView) view.findViewById(R.id.favorite_truck_name);
        mTruckNameTV.setText(mTruckName);

        mCancelButton = (Button) view.findViewById(R.id.favorite_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.favorite_cancel_button) {
                    mDialog.dismiss();
                }
            }
        });

        mAddButton = (Button) view.findViewById(R.id.favorite_add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFavorites().execute(mUsername, mProductID);
            }
        });

        mOrderButton = (Button) view.findViewById(R.id.favorite_order_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private class AddFavorites extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... args) {
            String username = args[0];
            String productID = args[1];

            return runJSON(username, productID);
        }

        private String runJSON(String username, String productID) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("prod_id", productID));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

                Log.d("Recovery Attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Email Information!", json.toString());
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }
}
