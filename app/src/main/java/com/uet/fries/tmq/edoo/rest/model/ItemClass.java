package com.uet.fries.tmq.edoo.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmq on 12/04/2017.
 */

public class ItemClass {
    @SerializedName("id")
    private String id;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("semester")
    private String semester;

    @SerializedName("credit_count")
    private String credit_count;

    @SerializedName("student_count")
    private String student_count;

    @SerializedName("teacher_name")
    private String teacher_name;

    @SerializedName("lessions")
    private List<ItemLesson> lessons;

    @Override
    public String toString() {
        return name;
    }

    public List<ItemLesson> getLessons() {
        if (lessons == null) return new ArrayList<>();
        return lessons;
    }
}
