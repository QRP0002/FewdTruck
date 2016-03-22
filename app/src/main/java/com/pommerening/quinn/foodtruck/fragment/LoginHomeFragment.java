package com.pommerening.quinn.foodtruck.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.pommerening.quinn.foodtruck.fragment.dialogs.ForgotIdDialog;
import com.pommerening.quinn.foodtruck.fragment.dialogs.NewRegisterDialog;
import com.pommerening.quinn.foodtruck.pojo.EmployeeLoad;
import com.pommerening.quinn.foodtruck.pojo.LoginServices;
import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.TimeOfDay;

public class LoginHomeFragment extends Fragment implements MiniMapFragment.OnMapReadyListner {

    private Button mLoginButton;
    private TextView mForgotInfo;
    private TextView mNewUser;
    private TextView mTimeOfDay;
    private EditText mUsername;
    private EditText mPassword;
    private FragmentManager fm;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private final String TAG = "LoginHomeFragment";


    public static LoginHomeFragment newInstance() {
        LoginHomeFragment f = new LoginHomeFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login_home, container, false);

        mMapFragment = MiniMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.child_fragment, mMapFragment)
                .commit();
        onMapReady();

        int holder = 1;
        if(holder == 1) {
            EmployeeLoad el = new EmployeeLoad(getActivity());
            el.loadEmployee();
            holder++;
        }

        mLoginButton = (Button) view.findViewById(R.id.login_fragment_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginServices ls = new LoginServices(getActivity(), getActivity());
                mUsername = (EditText) view.findViewById(R.id.login_name_id);
                mPassword = (EditText) view.findViewById(R.id.login_password_id);

                try {
                    final String username = mUsername.getText().toString();
                    final String password = mPassword.getText().toString();
                    final String employee = ls.isEmployee(username);

                    if (employee.equals("yes") && ls.employeeVerification(username, password)) {
                        Fragment display = new EmployeeHomeFragment();
                        getFragmentManager().beginTransaction()
                                .addToBackStack("fragment")
                                .replace(R.id.fragmentContainer, display)
                                .commit();
                    } else if (employee.equals("no") && ls.customerVerification(username,
                            password)) {
                        Fragment display = CustomerHomeFragment.newInstance(username);
                        getFragmentManager().beginTransaction()
                                .addToBackStack("fragment")
                                .replace(R.id.fragmentContainer, display)
                                .commit();
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.login_incorrect_string,
                                Toast.LENGTH_SHORT).show();
                    }
                }  catch (NullPointerException e) {
                        Log.e(TAG, "employee String null");
                        Log.d(TAG, Log.getStackTraceString(e));
                        Toast.makeText(getActivity(),
                                R.string.login_no_account,
                                Toast.LENGTH_SHORT).show();
            }
            }
        });

        mForgotInfo = (TextView) view.findViewById(R.id.login_forgot_password);
        mForgotInfo.setTextColor(Color.parseColor("#0000FF"));
        mForgotInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new ForgotIdDialog();
                newFragment.show(getActivity().getSupportFragmentManager(), "forgot dialog");
            }
        });

        mNewUser = (TextView) view.findViewById(R.id.login_new_user);
        mNewUser.setTextColor(Color.parseColor("#0000FF"));
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

}
