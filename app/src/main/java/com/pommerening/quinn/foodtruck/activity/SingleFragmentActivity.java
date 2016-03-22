package com.pommerening.quinn.foodtruck.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.pommerening.quinn.foodtruck.R;

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragmentContainer);

        if (f == null) {
            f = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, f, "display")
                    .commit();
        }
    }
}
