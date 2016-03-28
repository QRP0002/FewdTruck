package com.pommerening.quinn.foodtruck.fragment.tabs.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.database.User;
import com.pommerening.quinn.foodtruck.pojo.PasswordChecker;

import io.realm.Realm;

public class SettingsTabFragment extends Fragment {
    private String mUsername;
    private EditText mPasswordET;
    private EditText mConfirmET;
    private EditText mNameET;
    private EditText mEmailET;
    private Spinner mRangeSpinner;

    public static SettingsTabFragment newInstance(String username) {
        SettingsTabFragment f = new SettingsTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings_tab, container, false);

        mRangeSpinner = (Spinner) view.findViewById(R.id.settings_range_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.range_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRangeSpinner.setAdapter(adapter);

        return view;
    }
}
