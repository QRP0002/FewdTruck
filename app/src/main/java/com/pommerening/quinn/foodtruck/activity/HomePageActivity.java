package com.pommerening.quinn.foodtruck.activity;


import android.support.v4.app.Fragment;

import com.pommerening.quinn.foodtruck.fragment.CustomerHomeFragment;

public class HomePageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CustomerHomeFragment();
    }
}
