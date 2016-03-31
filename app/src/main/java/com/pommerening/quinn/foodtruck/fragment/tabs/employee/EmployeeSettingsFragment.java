package com.pommerening.quinn.foodtruck.fragment.tabs.employee;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.GPSLocation;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSettingsFragment extends Fragment {
    private Button mSaveButton;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmEditText;
    private EditText mEmailEditText;
    private EditText mTruckNameEditText;
    private String mUsername;
    private ProgressDialog pDialog;
    private ToggleButton mToggleButton;

    private static int broadcastToggle;

    private static final String SETTING_URL = "http://192.168.1.72/webservice/settingemployee.php";
    private static final String BROADCAST_URL = "http://192.168.1.72/webservice/locationupdate.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static EmployeeSettingsFragment newInstance(String username) {
        EmployeeSettingsFragment f = new EmployeeSettingsFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_settings, container, false);
        final GPSLocation gps = GPSLocation.locationSingleton(getActivity());

        mNameEditText = (EditText) view.findViewById(R.id.emp_set_name_et);
        mPasswordEditText = (EditText) view.findViewById(R.id.emp_set_password_et);
        mConfirmEditText = (EditText) view.findViewById(R.id.emp_set_confirm_et);
        mEmailEditText = (EditText) view.findViewById(R.id.emp_set_email_et);
        mTruckNameEditText = (EditText)  view.findViewById(R.id.emp_set_truck_et);

        mToggleButton = (ToggleButton) view.findViewById(R.id.emp_set_toggle_button);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(gps.canGetLocation()) {
                        broadcastToggle = 1;
                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();
                        new Broadcast().execute(mUsername, String.valueOf(broadcastToggle),
                                String.valueOf(latitude), String.valueOf(longitude));
                    }
                } else {
                    broadcastToggle = 0;
                    String latitude = "0";
                    String longitude = "0";
                    new Broadcast().execute(mUsername, String.valueOf(broadcastToggle),
                            latitude, longitude);
                }
            }
        });

        mSaveButton = (Button) view.findViewById(R.id.emp_set_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameSend = mNameEditText.getText().toString();
                final String passwordSend = mPasswordEditText.getText().toString();
                final String confirmSend = mConfirmEditText.getText().toString();
                final String emailSend = mEmailEditText.getText().toString();
                final String truckSend = mTruckNameEditText.getText().toString();

                new SaveInformation().execute(mUsername, nameSend, passwordSend,
                        confirmSend, emailSend, truckSend);

            }
        });

        return view;
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
            String truckName = args[5];

            return runJSON(username, name, password, confirm, email, truckName);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if (url != null){
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
            }
        }

        private String runJSON(String username, String name, String password,
                               String confirm, String email, String truckName) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("password_confirm", confirm));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("truck_name", truckName));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(SETTING_URL, "POST", params);

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

    private class Broadcast extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String username = args[0];
            String broadcast = args[1];
            String latitude = args[2];
            String longitude = args[3];

            return changeBroadcast(username, broadcast, latitude, longitude);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if (url != null){
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
            }
        }

        private String changeBroadcast(String username, String broadcast, String latitude,
                                       String longitude) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("broadcast", broadcast));
                params.add(new BasicNameValuePair("latitude", latitude));
                params.add(new BasicNameValuePair("longitude", longitude));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(BROADCAST_URL, "POST", params);

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
