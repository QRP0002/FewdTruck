package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.database.User;
import com.pommerening.quinn.foodtruck.pojo.Registration;
import com.pommerening.quinn.foodtruck.R;

import io.realm.Realm;

public class NewRegisterDialog extends DialogFragment {

    private Dialog dialog;
    private Button mSendButton;
    private Button mCancelButton;
    public final String TAG = "New Register Dialog";
    private static int idNum = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_new_register_dialog);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_register_dialog, container, false);

        mSendButton = (Button) view.findViewById(R.id.register_dialog_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText usernameInput = (EditText) dialog.findViewById(
                        R.id.register_dialog_username);
                final EditText passwordInput = (EditText) dialog.findViewById(
                        R.id.register_dialog_password);
                final EditText emailInput = (EditText) dialog.findViewById(
                        R.id.register_dialog_email);

                Registration reg = new Registration(getActivity());

                if(v.getId() == R.id.register_dialog_send_button) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();
                    final String email = emailInput.getText().toString();

                    if(!reg.validationName(username)) {
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_inv_username,
                                Toast.LENGTH_SHORT).show();
                    } else if(username.equals("")) {
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_null_username,
                                Toast.LENGTH_SHORT).show();
                    } else if(!reg.validationEmail(email)) {
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_inv_email,
                                Toast.LENGTH_SHORT).show();
                    } else if(email.equals("")) {
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_null_email,
                                Toast.LENGTH_SHORT).show();
                    } else if(password.equals("")) {
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_null_password,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Realm realm = Realm.getInstance(getActivity());
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                User user = realm.createObject(User.class);
                                user.setUsername(username);
                                user.setPassword(password);
                                user.setEmail(email);
                                user.setId(idNum++);
                                user.setEmployee("no");
                            }
                        });
                        Toast.makeText(getActivity(),
                                R.string.register_dialog_success,
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.register_dialog_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.register_dialog_cancel_button) {
                    dialog.dismiss();
                }
            }
        });
        return view;
    }
}
