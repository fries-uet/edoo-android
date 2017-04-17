package com.uet.fries.tmq.edoo.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmq on 16/04/2017.
 */

public class ItemResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private String statusCode;

    public ItemResponse() {
        this.error = "";
        this.message = "";
        this.statusCode = "";
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
