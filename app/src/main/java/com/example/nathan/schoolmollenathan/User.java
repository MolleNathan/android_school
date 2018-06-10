package com.example.nathan.schoolmollenathan;

/**
 * Created by Nathan on 10/06/2018.
 */

public class User {

    private String email;
    private String password;
    private static String auth_token;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public static String getAuth_token() {
        return auth_token;
    }

    public static void setAuth_token(String auth_token) {
        User.auth_token = auth_token;
    }
}
