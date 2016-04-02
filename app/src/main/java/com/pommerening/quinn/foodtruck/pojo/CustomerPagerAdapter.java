package com.pommerening.quinn.foodtruck.pojo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.pommerening.quinn.foodtruck.fragment.MiniMapFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.customer.FavoritesTabFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.customer.ItemsTabFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.customer.SettingsTabFragment;

/**
 *
 * Created by Quinn Pommerening on 2/19/2016.
 */
public class CustomerPagerAdapter extends FragmentStatePagerAdapter {
    int mTabNumbers;
    private String username;
    private String distance;

    public CustomerPagerAdapter(FragmentManager fm, int NumberOfTabs, String username,
                                String distance) {
        super(fm);
        this.mTabNumbers = NumberOfTabs;
        this.username = username;
        this.distance = distance;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SupportMapFragment tabMap = MiniMapFragment.newInstance();
                return tabMap;
            case 1:
                ItemsTabFragment itemsTab = ItemsTabFragment.newInstance(username);
                return itemsTab;
            case 2:
                FavoritesTabFragment favTab = FavoritesTabFragment.newInstance(username);
                return favTab;
            case 3:
                SettingsTabFragment setTab = SettingsTabFragment.newInstance(username, distance);
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
