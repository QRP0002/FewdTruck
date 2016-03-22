package com.pommerening.quinn.foodtruck.fragment.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.database.User;
import com.pommerening.quinn.foodtruck.pojo.PasswordChecker;

import io.realm.Realm;

public class SettingsTabFragment extends Fragment {
    private Button mSubmitButton;
    private EditText mPassword;
    private String mUsername;
    private EditText mConfirmedPassword;
    private EditText mEmail;
    private Realm realm;

    public static SettingsTabFragment newInstance(String username) {
        SettingsTabFragment f = new SettingsTabFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_tab, container, false);

        mPassword = (EditText)view.findViewById(R.id.settings_tab_password_str);
        mConfirmedPassword = (EditText) view.findViewById(R.id.settings_tab_password_confirm_str);
        mEmail = (EditText) view.findViewById(R.id.settings_email_str);

        mSubmitButton = (Button) view.findViewById(R.id.settings_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordChecker pc = new PasswordChecker();
                final String newPassword = mPassword.getText().toString();
                final String newConfirmedPassword = mConfirmedPassword.getText().toString();
                final String newEmail = mEmail.getText().toString();

                if(pc.passwordConfirmed(newPassword, newConfirmedPassword)) {
                    realm = Realm.getInstance(getActivity());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            User user = realm.where(User.class)
                                    .equalTo("username", mUsername)
                                    .findFirst();
                            user.setPassword(newPassword);
                            user.setEmail(newEmail);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(),
                            R.string.settings_invalid,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
