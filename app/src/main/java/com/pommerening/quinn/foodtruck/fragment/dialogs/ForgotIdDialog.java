package com.pommerening.quinn.foodtruck.fragment.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.pojo.Mail;
import com.pommerening.quinn.foodtruck.pojo.RetrieveEmail;
import com.pommerening.quinn.foodtruck.R;

public class ForgotIdDialog extends DialogFragment {
    private Dialog mDialog;
    private Button mSendButton;
    private Button mCancelButton;
    private EditText mEmail;
    private TextView mHeader;
    public final String TAG = "TAG";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_forgot_id_dialog);
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_forgot_id_dialog, container, false);

        mHeader= (TextView) view.findViewById(R.id.forgot_id_header);
        mCancelButton = (Button) view.findViewById(R.id.forgot_id_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.forgot_id_cancel_button) {
                    mDialog.dismiss();
                }
            }
        });

        mSendButton = (Button) view.findViewById(R.id.forgot_id_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mEmail = (EditText) view.findViewById(R.id.forgot_id_email_et);
                        if(v.getId() == R.id.forgot_id_send_button) {
                            final String email = mEmail.getText().toString();
                            RetrieveEmail re = new RetrieveEmail(getActivity());
                            Mail m = new Mail("qpommer0@gmail.com", "flashman0");
                            String[] toArray = new String[1];

                            if(re.emailSearch(email)) {
                                toArray[0] = email;
                            } else {
                                // TODO: Make sure the toast works here.
                                Toast.makeText(getActivity(),
                                        R.string.information_dialog_failure,
                                        Toast.LENGTH_SHORT).show();
                            }

                            m.set_to(toArray);
                            m.set_from("qpommer0@gmail.com");
                            m.set_subject("Account Information");
                            String temp = re.getEmailInformation(email);
                            m.set_body(re.getEmailInformation(email));

                            try{
                                if(m.send()) {
                                    // TODO: Add a toast here.
                                    mDialog.dismiss();
                                } else {
                                    // TODO: Make sure the toast works here.
                                    Toast.makeText(getActivity(),
                                            R.string.information_dialog_not_sent,
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch(Exception e) {
                                Log.e("Food Truck", "Could not send email", e);
                            }
                        }
                    }
                }).start();
            }
        });
        return view;
    }

}
