package com.pommerening.quinn.foodtruck.pojo;

import android.content.Context;

import com.pommerening.quinn.foodtruck.database.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Quinn Pommerening on 1/28/2016.
 */
public class Registration {
    private Realm realm;

    public Registration(Context context) {
        realm = Realm.getInstance(context);
    }

    /**Method is used to test and see if a username is already in use
     * It takes the user name is searches the database for any matches.
     * If a match is found it will be added to a list and return false*/
    public boolean validationName(String username) {
        RealmResults<User> nameList = realm.where(User.class)
                .equalTo("username", username)
                .findAll();
        if(nameList.size() == 0) {
            return true;
        }
        return false;
    }

    /**Method is used to test and see if a email address is already in use
     * It takes the email adress and search the database for any matches.
     * If a match is found it will be added to a list and return false*/
    public boolean validationEmail(String email) {
        RealmResults<User> emailList = realm.where(User.class)
                .equalTo("email", email)
                .findAll();
        if(emailList.size() == 0) {
            return true;
        }
        return false;
    }


}
