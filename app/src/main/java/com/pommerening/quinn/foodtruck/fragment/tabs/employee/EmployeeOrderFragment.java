package com.pommerening.quinn.foodtruck.fragment.tabs.employee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pommerening.quinn.foodtruck.R;

public class EmployeeOrderFragment extends Fragment {
    private String mUsername;

    public static EmployeeOrderFragment newInstance(String username) {
        EmployeeOrderFragment f = new EmployeeOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_employee_order, container, false);

        return view;
    }

}
