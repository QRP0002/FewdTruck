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

import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewRegisterDialog extends DialogFragment {

    private Dialog dialog;
    private Button mSendButton;
    private Button mCancelButton;
    private ProgressDialog pDialog;

    JSONParser jp = new JSONParser();
    private static final String URL = "http://192.168.1.72/webservice/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_new_register_dialog);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_register_dialog, container, false);

        mSendButton = (Button) view.findViewById(R.id.register_dialog_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText mUsername = (EditText) dialog.findViewById(
                        R.id.register_dialog_username);
                final EditText mPassword = (EditText) dialog.findViewById(
                        R.id.register_dialog_password);
                final EditText mEmail = (EditText) dialog.findViewById(
                        R.id.register_dialog_email);

                if(v.getId() == R.id.register_dialog_send_button) {
                    String username = mUsername.getText().toString();
                    String password = mPassword.getText().toString();
                    String email = mEmail.getText().toString();
                    new CreationService().execute(username, password, email);
                }
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.register_dialog_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.register_dialog_cancel_button) {
                    dialog.dismiss();
                }
            }
        });
        return view;
    }

    class CreationService extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating Account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String username = args[0];
            String password = args[1];
            String email = args[2];

            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("email", email));

                String temp = params.get(0).toString();
                Log.d("Username this is: ", temp);
                Log.d("Password: ", params.get(1).toString());
                Log.d("Email: ", params.get(2).toString());

                Log.d("Request", "Starting");
                JSONObject json = jp.makeHttpRequest(URL, "POST", params);

                Log.d("Creation attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Creation Successful!", json.toString());
                    dialog.dismiss();
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

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }

        }
    }
}
