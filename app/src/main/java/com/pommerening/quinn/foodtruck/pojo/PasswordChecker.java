package com.pommerening.quinn.foodtruck.pojo;

/**
 * Created by Quinn Pommerening on 2/22/2016.
 */
public class PasswordChecker {
    public boolean passwordConfirmed (String strOne, String strTwo) {
        if(strOne.equals(strTwo)) {
            return true;
        }
        return false;
    }
}
