package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pommerening.quinn.foodtruck.R;

import java.security.PrivateKey;

public class EditFavoritesDialogFragment extends DialogFragment {
    private Dialog mDialog;
    private String mUsername;
    private String mProductName;
    private String mProductPrice;
    private String mProductID;
    private String mTruckName;

    private TextView mProductNameTV;
    private TextView mProductPriceTV;
    private TextView mTruckNameTV;

    private Button mCancelButton;
    private Button mRemoveButton;
    private Button mOrderButton;
    public static EditFavoritesDialogFragment newInstance(String username, String productName,
                                                          String productPrice, String productID,
                                                          String truckName) {
        EditFavoritesDialogFragment f = new EditFavoritesDialogFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("productName", productName);
        args.putString("productPrice", productPrice);
        args.putString("productID", productID);
        args.putString("truckName", truckName);
        f.setArguments(args);
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString("username");
            mProductName = getArguments().getString("productName");
            mProductPrice = getArguments().getString("productPrice");
            mProductID = getArguments().getString("productID");
            mTruckName = getArguments().getString("truckName");
        }
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_add_favorite_dialog);
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_favorites_dialog, container, false);

        mProductNameTV = (TextView) view.findViewById(R.id.edit_fav_prod_name);
        mProductNameTV.setText(mProductName);
        mProductPriceTV = (TextView) view.findViewById(R.id.edit_fav_prod_price);
        mProductPriceTV.setText(mProductPrice);
        mTruckNameTV = (TextView) view.findViewById(R.id.edit_fav_truck_name);
        mTruckNameTV.setText(mTruckName);

        mCancelButton = (Button) view.findViewById(R.id.edit_fav_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.edit_fav_cancel_button) {
                    mDialog.dismiss();
                }
            }
        });

        mRemoveButton = (Button) view.findViewById(R.id.edit_fav_remove_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mOrderButton = (Button) view.findViewById(R.id.edit_fav_order_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}