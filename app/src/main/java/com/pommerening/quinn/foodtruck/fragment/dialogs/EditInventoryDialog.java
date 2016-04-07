package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.RefreshScreenInterface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditInventoryDialog extends DialogFragment {
    private Dialog mDialog;
    private String mProductName;
    private String mProductPrice;
    private String mProductID;
    private Button mEditButton;
    private Button mCancelButton;
    private Button mDeleteButton;
    private EditText mProductNameEditText;
    private EditText mProductPiceEditText;
    private ProgressDialog pDialog;
    private RefreshScreenInterface callback;

    private static final String DELETE_URL = "http://192.168.1.72:80/webservice/empdeleteinv.php";
    private static final String EDIT_URL = "http://192.168.1.72:80/webservice/editinv.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private TextInputLayout nameLayout, priceLayout;


    public static EditInventoryDialog newInstance(String productName,
                                                  String productPrice, String productID ) {
        EditInventoryDialog f = new EditInventoryDialog();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        args.putString("productPrice", productPrice);
        args.putString("productID", productID);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductName = getArguments().getString("productName");
        mProductPrice = getArguments().getString("productPrice");
        callback = (RefreshScreenInterface) getTargetFragment();
        mProductID = getArguments().getString("productID");
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_forgot_id_dialog);
        mDialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_inventory_dialog, container, false);
        mProductNameEditText = (EditText) view.findViewById(R.id.edit_inv_name_et);
        mProductPiceEditText = (EditText) view.findViewById(R.id.edit_inv_price_et);

        priceLayout = (TextInputLayout) view.findViewById(R.id.edit_inv_price_layout);
        priceLayout.setHint("$ " + mProductPrice);
        nameLayout = (TextInputLayout) view.findViewById(R.id.edit_inv_name_layout);
        nameLayout.setHint(mProductName);


        mCancelButton = (Button) view.findViewById(R.id.edit_inv_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.edit_inv_cancel_button) {
                    mDialog.dismiss();
                }
            }
        });

        mEditButton = (Button) view.findViewById(R.id.edit_inv_edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameChange = mProductNameEditText.getText().toString();
                final String priceChange = mProductPiceEditText.getText().toString();

                if(nameChange.equals(mProductName) && priceChange.equals(mProductPrice)) {
                    Log.d("Nothing changed", "so we just close");
                    mDialog.dismiss();
                } else {
                    Log.d("We entered here: ", "This is the item changed part.");
                    new EditInventory().execute(mProductID, nameChange, priceChange);
                    mDialog.dismiss();
                    callback.refreshScreen();
                }

            }
        });

        mDeleteButton = (Button) view.findViewById(R.id.edit_inv_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteItem().execute(mProductID);
                mDialog.dismiss();
                callback.refreshScreen();
            }
        });
        return view;
    }

    public class DeleteItem extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Removing Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String productID = args[0];

            return runDelete(productID);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            pDialog.dismiss();
        }

        public String runDelete(String productID) {
            int success;
            Log.d("Product ID is : ", productID);
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("prod_id", productID));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(DELETE_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                Log.d("Success: ", json.getString(TAG_SUCCESS));

                if(success == 1) {
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class EditInventory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Removing Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String productID = args[0];
            String productName = args[1];
            String productPrice = args[2];

            return runEdit(productID, productName, productPrice);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            pDialog.dismiss();
        }

        public String runEdit(String productID, String productName, String productPrice) {
            int success;
            Log.d("Product ID is : ", productID);
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("prod_id", productID));
                params.add(new BasicNameValuePair("prod_name", productName));
                params.add(new BasicNameValuePair("prod_price", productPrice));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(EDIT_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                Log.d("Success: ", json.getString(TAG_SUCCESS));
                if(success == 1) {
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
