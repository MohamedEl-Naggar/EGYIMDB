package edu.aucegypt.egyimdb;

import android.app.Application;

public class EmailClass extends Application {
    public String email;

    private static final EmailClass ourInstance = new EmailClass();

    public static EmailClass getInstance()
    {
        return ourInstance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
