package com.pommerening.quinn.foodtruck.pojo;

import android.content.Context;

import com.pommerening.quinn.foodtruck.database.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Quinn Pommerening on 2/1/2016.
 */
public class RetrieveEmail {
    private Realm realm;

    public RetrieveEmail(Context context) {
        realm = Realm.getInstance(context);
    }

    public boolean emailSearch(String email) {
        RealmResults<User> emailList = realm.where(User.class)
                .equalTo("email", email)
                .findAll();
        if(emailList.size() == 1) {
            return true;
        }
        return false;
    }

    public String getEmailInformation(String email) {
        User user = realm.where(User.class)
                .equalTo("email", email).findFirst();
        String username = user.getUsername();
        String password = user.getPassword();
        String send = "Username: " + username
                + "\nPassword: " + password;
        return send;
    }

}
