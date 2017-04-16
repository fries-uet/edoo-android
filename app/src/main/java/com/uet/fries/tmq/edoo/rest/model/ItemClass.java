package com.uet.fries.tmq.edoo.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmq on 12/04/2017.
 */

public class ItemClass
//        extends ItemAbstract
{
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("student_count")
    private String student_count;
    @SerializedName("teacher_name")
    private String teacher_name;

    @Override
    public String toString() {
        return name;
    }
}
