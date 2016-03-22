package com.pommerening.quinn.foodtruck.pojo;

import android.content.Context;

import com.pommerening.quinn.foodtruck.database.Employee;

import io.realm.Realm;

/**
 * Created by Quinn Pommerening on 2/25/2016.
 */
public class EmployeeLoad {
    private Realm realm;
    public EmployeeLoad(Context context) {
        realm = Realm.getInstance(context);
    }
    public void loadEmployee() {
        realm.beginTransaction();
        Employee emp = realm.createObject(Employee.class);
        emp.setUsername("emp");
        emp.setPassword("pass");
        emp.setEmployee("yes");
        emp.setEmail("emp");
        emp.setId(100001);
        realm.commitTransaction();
    }
}
