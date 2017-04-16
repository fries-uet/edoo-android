package com.uet.fries.tmq.edoo.rest.models;

import com.uet.fries.tmq.edoo.model.ItemUser;

/**
 * Created by tmq on 16/04/2017.
 */

public class ItemLogin {

    private String token;
    private ItemUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ItemUser getUser() {
        return user;
    }

    public void setUser(ItemUser user) {
        this.user = user;
    }
}
