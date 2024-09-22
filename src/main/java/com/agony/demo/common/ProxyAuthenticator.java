package com.agony.demo.common;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 代理类
 */
public class ProxyAuthenticator extends Authenticator {
    private final String username;
    private final String password;

    public ProxyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
