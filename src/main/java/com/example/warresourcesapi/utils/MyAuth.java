package com.example.warresourcesapi.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MyAuth extends Authenticator {
    final PasswordAuthentication authentication;

    public MyAuth(String userName, String password) {
        authentication = new PasswordAuthentication(userName, password.toCharArray());
    }
}
