package com.uet.fries.tmq.edoo.helper.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tmq on 16/04/2017.
 */

@Entity
public class User {

    @Id(autoincrement = true)
    private long id;

    @NotNull
    private String email;

    private String name;
    private String avatar;
    private String regular_class;
    private String code;
    private String capability;
    private String username;
    private String birthday;

    @Generated(hash = 427806365)
    public User(long id, @NotNull String email, String name, String avatar,
            String regular_class, String code, String capability, String username,
            String birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.regular_class = regular_class;
        this.code = code;
        this.capability = capability;
        this.username = username;
        this.birthday = birthday;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getRegular_class() {
        return this.regular_class;
    }
    public void setRegular_class(String regular_class) {
        this.regular_class = regular_class;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCapability() {
        return this.capability;
    }
    public void setCapability(String capability) {
        this.capability = capability;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}
