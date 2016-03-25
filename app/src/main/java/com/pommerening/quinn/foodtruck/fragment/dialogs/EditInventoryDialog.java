package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.pommerening.quinn.foodtruck.R;

public class EditInventoryDialog extends DialogFragment {
    private Dialog mDialog;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_forgot_id_dialog);
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_inventory_dialog, container, false);
        return view;
    }
}
