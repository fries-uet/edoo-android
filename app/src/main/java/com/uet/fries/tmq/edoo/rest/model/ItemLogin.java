package com.uet.fries.tmq.edoo.rest.model;

import com.uet.fries.tmq.edoo.helper.dao.User;

/**
 * Created by tmq on 16/04/2017.
 */

public class ItemLogin {

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
