package com.pommerening.quinn.foodtruck.pojo;

import android.content.Context;
import android.util.Log;

import com.pommerening.quinn.foodtruck.database.Employee;
import com.pommerening.quinn.foodtruck.database.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Quinn Pommerening on 2/3/2016.
 */
public class LoginServices {
    private Realm realmUser;
    private Realm realmEmp;

    public LoginServices(Context contextUser, Context contextEmp) {
        realmUser = Realm.getInstance(contextUser);
        realmEmp = Realm.getInstance(contextEmp);
    }

    public boolean customerVerification(String username, String password) {
        RealmResults<User> user = realmUser.where(User.class)
                .equalTo("username", username)
                .beginGroup()
                    .equalTo("password", password)
                .endGroup().findAll();
        if(user.size() == 1) {
            return true;
        }
        return false;
    }

    public boolean employeeVerification(String username, String password) {
        RealmResults<Employee> employee= realmEmp.where(Employee.class)
                .equalTo("username", username)
                .beginGroup()
                .equalTo("password", password)
                .endGroup().findAll();
        Log.d("This is a tag:", "Employee Size: " + employee.size());
        if(employee.size() == 1) {
            return true;
        }
        return false;
    }

    public String isEmployee(String username) {
        String userResult = userEmployeeLoad(username);
        String empResult = empEmployeeLoad(username);
        if(empResult.equals("yes")) {
            return empResult;
        }
        return userResult;
    }

    private String userEmployeeLoad(String username) {
        String nullCatch = "one";
        try {
            User user = realmEmp.where(User.class)
                    .equalTo("username", username)
                    .findFirst();
            return user.getEmployee();
        } catch (NullPointerException e) {
            return nullCatch;
        }
    }

    private String empEmployeeLoad(String username) {
        String nullCatch = "one";
        try {
            Employee emp = realmEmp.where(Employee.class)
                    .equalTo("username", username)
                    .findFirst();
            return emp.getEmployee();
        } catch (NullPointerException e) {
            return nullCatch;
        }
    }
}
