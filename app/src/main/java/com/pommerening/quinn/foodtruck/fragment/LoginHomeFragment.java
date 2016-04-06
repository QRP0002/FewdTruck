package com.pommerening.quinn.foodtruck.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pommerening.quinn.foodtruck.Manifest;
import com.pommerening.quinn.foodtruck.fragment.dialogs.ForgotIdDialog;
import com.pommerening.quinn.foodtruck.fragment.dialogs.NewRegisterDialog;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.LocationData;
import com.pommerening.quinn.foodtruck.pojo.TimeOfDay;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginHomeFragment extends Fragment implements MiniMapFragment.OnMapReadyListener{

    private Button mLoginButton;
    private TextView mForgotInfo;
    private TextView mNewUser;
    private TextView mTimeOfDay;
    private EditText mUsername;
    private EditText mPassword;
    private FragmentManager fm;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private ProgressDialog pDialog;
    private TextInputLayout inputLayoutName, getInputLayoutPassword;

    JSONParser jp = new JSONParser();
    private static final String URL = "http://192.168.1.72:80/webservice/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_DISTANCE = "distance";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login_home, container, false);

        Log.d("This is mMap", "I am null");
        mMapFragment = MiniMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.child_fragment,
                mMapFragment).commit();
        onMapReady();

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        getInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);

        mUsername = (EditText) view.findViewById(R.id.login_name_id);
        mPassword = (EditText) view.findViewById(R.id.login_password_id);
        mLoginButton = (Button) view.findViewById(R.id.login_fragment_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                new UserLoginServices().execute(username, password);
            }
        });

        mForgotInfo = (TextView) view.findViewById(R.id.login_forgot_password);
        mForgotInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new ForgotIdDialog();
                newFragment.show(getActivity().getSupportFragmentManager(), "forgot dialog");
            }
        });

        mNewUser = (TextView) view.findViewById(R.id.login_new_user);
        mNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new NewRegisterDialog();
                newFragment.show(getActivity().getSupportFragmentManager(), "register dialog");
            }
        });

        mTimeOfDay = (TextView) view.findViewById(R.id.time_of_day);
        TimeOfDay timeOfDay = new TimeOfDay();
        String TOD = timeOfDay.getTimeOfDay();
        mTimeOfDay.setText(TOD);
        return view;
    }

    @Override
    public void onMapReady() {
        mMap = mMapFragment.getMap();
    }

    public class UserLoginServices extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String username = args[0];
            String password = args[1];

            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("Request", "Starting");
                JSONObject json = jp.makeHttpRequest(URL, "POST", params);
                String usernameSend;
                String distanceSend;
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    usernameSend = json.getString(TAG_USERNAME);
                    Fragment display = EmployeeHomeFragment.newInstance(usernameSend);
                    getFragmentManager().beginTransaction()
                            .addToBackStack("fragment")
                            .replace(R.id.fragmentContainer, display)
                            .commit();
                    return json.getString(TAG_MESSAGE);
                } else if(success == 2) {
                    Log.d("Login Successful!", json.toString());
                    usernameSend = json.getString(TAG_USERNAME);
                    distanceSend = json.getString(TAG_DISTANCE);
                    LocationData.setDistance(Double.parseDouble(distanceSend));
                    Fragment display = CustomerHomeFragment.newInstance(usernameSend, distanceSend);
                    getFragmentManager().beginTransaction()
                            .addToBackStack("fragment")
                            .replace(R.id.fragmentContainer, display)
                            .commit();
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

