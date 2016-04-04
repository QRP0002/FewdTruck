package com.pommerening.quinn.foodtruck.pojo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.pommerening.quinn.foodtruck.fragment.MiniMapFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.customer.CartFragment;
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
    private Context context;

    public CustomerPagerAdapter(FragmentManager fm, int NumberOfTabs, String username,
                                String distance, Context context) {
        super(fm);
        this.mTabNumbers = NumberOfTabs;
        this.username = username;
        this.distance = distance;
        this.context = context;
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
            case 4:
                CartFragment cartTab = CartFragment.newInstance(username);
                return cartTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumbers;
    }
}
