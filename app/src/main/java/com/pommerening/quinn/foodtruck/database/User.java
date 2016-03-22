package com.pommerening.quinn.foodtruck.database;

import io.realm.RealmObject;

/**
 * Created by Quinn Pommerening on 1/28/2016.
 */
public class User extends RealmObject {

    private int id;
    private String username;
    private String password;
    private String email;
    private String employee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
}
