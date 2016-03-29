package com.pommerening.quinn.foodtruck.fragment.tabs.customer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsTabFragment extends Fragment {
    private String mUsername;
    private EditText mPasswordET;
    private EditText mConfirmET;
    private EditText mNameET;
    private EditText mEmailET;
    private Spinner mRangeSpinner;
    private Button mSaveButton;
    private String mValue;

    private static final String URL = "http://192.168.1.72/webservice/settingscustomer.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static SettingsTabFragment newInstance(String username, String distance) {
        SettingsTabFragment f = new SettingsTabFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("distance", distance);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");
        mValue = getArguments().getString("distance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_tab, container, false);

        mRangeSpinner = (Spinner) view.findViewById(R.id.settings_range_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.range_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRangeSpinner.setAdapter(adapter);
        if (!mValue.equals("05")) {
            int spinnerIndex = adapter.getPosition(mValue);
            mRangeSpinner.setSelection(spinnerIndex);
        }

        mNameET = (EditText) view.findViewById(R.id.settings_name_et);
        mPasswordET = (EditText) view.findViewById(R.id.settings_password_et);
        mConfirmET = (EditText) view.findViewById(R.id.settings_confirm_et);
        mEmailET = (EditText) view.findViewById(R.id.settings_email_et);
        mSaveButton = (Button) view.findViewById(R.id.settings_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameSend = mNameET.getText().toString();
                final String passwordSend = mPasswordET.getText().toString();
                final String confirmSend = mConfirmET.getText().toString();
                final String emailSend = mEmailET.getText().toString();
                final String distanceSend = mRangeSpinner.getSelectedItem().toString();

                new SaveInformation().execute(mUsername, nameSend, passwordSend, confirmSend,
                        emailSend, distanceSend);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class SaveInformation extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String username = args[0];
            String name = args[1];
            String password = args[2];
            String confirm = args[3];
            String email = args[4];
            String distance = args[5];

            return runJSON(username, name, password, confirm, email, distance);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if (url != null) {
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
            }
        }

        private String runJSON(String username, String name, String password,
                               String confirm, String email, String distance) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("password_confirm", confirm));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("distance", distance));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                Log.d("Success: ", json.getString(TAG_SUCCESS));

                if (success == 1) {
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
