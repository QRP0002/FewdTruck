package com.pommerening.quinn.foodtruck.pojo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.pommerening.quinn.foodtruck.fragment.MiniMapFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.FavoritesTabFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.ItemsTabFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.SettingsTabFragment;

/**
 *
 * Created by Quinn Pommerening on 2/19/2016.
 */
public class CustomerPagerAdapter extends FragmentStatePagerAdapter {
    int mTabNumbers;
    private String mUsername;

    public CustomerPagerAdapter(FragmentManager fm, int NumberOfTabs, String username) {
        super(fm);
        this.mTabNumbers = NumberOfTabs;
        this.mUsername = username;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SupportMapFragment tabMap = MiniMapFragment.newInstance();
                return tabMap;
            case 1:
                ItemsTabFragment itemsTab = new ItemsTabFragment();
                return itemsTab;
            case 2:
                FavoritesTabFragment favTab = new FavoritesTabFragment();
                return favTab;
            case 3:
                SettingsTabFragment setTab = SettingsTabFragment.newInstance(mUsername);
                return setTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumbers;
    }
}
