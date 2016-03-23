package com.pommerening.quinn.foodtruck.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.EmployeePagerAdapter;

public class EmployeeHomeFragment extends Fragment {
    private String mUsername;

    public static EmployeeHomeFragment newInstance(String username) {
        EmployeeHomeFragment f = new EmployeeHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_employee_home, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.emp_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Inventory"));
        tabLayout.addTab(tabLayout.newTab().setText("Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.emp_pager);
        final EmployeePagerAdapter epa = new EmployeePagerAdapter(getFragmentManager(),
                tabLayout.getTabCount(), mUsername);
        viewPager.setAdapter(epa);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
