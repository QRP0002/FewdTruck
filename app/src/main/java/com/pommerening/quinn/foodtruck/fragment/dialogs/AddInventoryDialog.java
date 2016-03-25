package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddInventoryDialog extends DialogFragment {
    private String mUsername;
    private Button mAddButton;
    private Button mCancelButton;
    private EditText mProdNameEditText;
    private EditText mProdPriceEditText;
    private Dialog mDialog;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String URL = "http://192.168.1.72/webservice/addinv.php";
    private ProgressDialog pDialog;
    private RefreshInterface callback;
    private boolean itemAddedTracker = false;

    public static AddInventoryDialog newInstance(String username) {
        AddInventoryDialog f = new AddInventoryDialog();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mUsername = getArguments().getString("username");
        try {
            callback = (RefreshInterface) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement PasswordCreatedListener interface");
        }
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_forgot_id_dialog);
        return mDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inventory_dialog, container, false);

        mProdNameEditText = (EditText) view.findViewById(R.id.add_dialog_prod_name);
        mProdPriceEditText = (EditText) view.findViewById(R.id.add_dialog_prod_price);

        mAddButton= (Button) view.findViewById(R.id.add_dialog_add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add_dialog_add_button) {
                    final String productName = mProdNameEditText.getText().toString();
                    final String productPrice = mProdPriceEditText.getText().toString();
                    new AddInventory().execute(mUsername, productName, productPrice);
                    itemAddedTracker = true;
                }
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.add_dialog_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add_dialog_cancel_button) {
                    if(itemAddedTracker) {
                        callback.refreshJobsScreen();
                    }
                    mDialog.dismiss();
                }
            }
        });

        return view;
    }


    public class AddInventory extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            pDialog.dismiss();
            if (url != null){
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String productName = params[1];
            String productPrice = params[2];
            return itemJSONAdd(username, productName, productPrice);
        }

        public String itemJSONAdd(String username, String productName, String productPrice) {
            int success;

            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("prodname", productName));
                params.add(new BasicNameValuePair("prodprice", productPrice));
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
    }

    public interface RefreshInterface{
        void refreshJobsScreen();
    }
}
