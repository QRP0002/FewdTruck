package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.Mail;
import com.pommerening.quinn.foodtruck.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotIdDialog extends DialogFragment {
    private Dialog mDialog;
    private Button mSendButton;
    private Button mCancelButton;
    private EditText mEmail;
    private TextView mHeader;
    private String collectedUsername;
    private String collectedPassword;

    private ProgressDialog pDialog;
    private static final String URL = "http://192.168.1.72/webservice/recover.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private TextInputLayout inputLayoutEmail;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.fragment_forgot_id_dialog);
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_forgot_id_dialog, container, false);

        mHeader= (TextView) view.findViewById(R.id.forgot_id_header);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        mCancelButton = (Button) view.findViewById(R.id.forgot_id_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.forgot_id_cancel_button) {
                    mDialog.dismiss();
                }
            }
        });

        mSendButton = (Button) view.findViewById(R.id.forgot_id_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mEmail = (EditText) view.findViewById(R.id.forgot_id_email_et);
                if(v.getId() == R.id.forgot_id_send_button) {
                    final String email = mEmail.getText().toString();
                    new EmailRecoverService().execute(email);
                }
            }
        });
        return view;
    }
    public class EmailRecoverService extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String email = args[0];
            String output = setInformation(email);
            sendInformation(email);
            return output;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
                if(file_url.equals("Email Sent!")) {
                    mDialog.dismiss();
                }
            }
        }

        public String setInformation(String email) {
            int success;

            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("email", email));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

                Log.d("Recovery Attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Email Information!", json.toString());
                    collectedPassword = json.getString(TAG_PASSWORD);
                    collectedUsername = json.getString(TAG_USERNAME);
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

        public void sendInformation(String email) {
            Mail m = new Mail("place holder", "place holder");
            String[] toArray = new String[1];

            toArray[0] = email;
            m.set_to(toArray);
            m.set_from("username");
            m.set_subject("Account Information");
            m.set_body("Username: " + collectedUsername
                    + "\nPassword: " + collectedPassword);
            try{
                m.send();
            } catch (Exception e) {
                Log.e("Food Truck", "Could not send email", e);
            }
        }

    }
}
