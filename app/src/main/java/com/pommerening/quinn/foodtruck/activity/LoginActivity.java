package com.pommerening.quinn.foodtruck.activity;

import android.support.v4.app.Fragment;

import com.pommerening.quinn.foodtruck.fragment.LoginHomeFragment;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginHomeFragment();
    }

}
