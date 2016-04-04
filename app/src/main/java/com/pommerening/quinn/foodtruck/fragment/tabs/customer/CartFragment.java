package com.pommerening.quinn.foodtruck.fragment.tabs.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.LocationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends ListFragment {
    private String mUsername;
    private ArrayList<HashMap<String, String>> mOrderList;
    private static final String TAG_PRODUCTID = "_ProductID";
    private static final String TAG_TRUCKNAME = "_TruckName";
    private static final String TAG_USERNAME = "_Username";
    private static final String TAG_PRODUCTPRICE = "_ProductPrice";
    private static final String TAG_PRDUCTNAME = "_PRODUCTNAME";
    OrderAdapter orderAdapter;
    public static CartFragment newInstance(String username) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString("username");
        }

        mOrderList = LocationData.getTicketOrder();
        orderAdapter = new OrderAdapter(mOrderList);
        setListAdapter(orderAdapter);
    }


    private class OrderAdapter extends ArrayAdapter<HashMap<String, String>> {

        public OrderAdapter(ArrayList<HashMap<String, String>> orderList) {
            super(getActivity(), 0, orderList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.cart_list_view,
                        null);
            }

            HashMap<String, String> entry = getItem(position);

            TextView productName = (TextView) convertView.findViewById(R.id.
                    cart_prod_name);
            productName.setText(entry.get(TAG_PRDUCTNAME));

            TextView productPrice = (TextView) convertView.findViewById(R.id.
                    cart_prod_price);
            productPrice.setText(entry.get(TAG_PRODUCTPRICE));

            TextView truckName = (TextView) convertView.findViewById(R.id.
                    cart_truck_name);

            truckName.setText(entry.get(TAG_TRUCKNAME));

            Button removeButton = (Button) convertView.findViewById(R.id.cart_remove_button);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return convertView;
        }
    }
}
