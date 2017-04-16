package com.uet.fries.tmq.edoo.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmq on 16/04/2017.
 */

public class ItemLesson {

    private String address;
    private String code;
    private int id;
    private String name;
    private String period;
    private String semester;
    private String type;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("credit_count")
    private int creditCount;

    @SerializedName("day_of_week")
    private int dayOfWeek;

    @SerializedName("student_count")
    private int studentCount;

    @SerializedName("teacher_name")
    private String teacherName;

    // Util ------------------------------

    /**
     * @return the number of lesson
     * Eg: 4-5 -> 2,        6-9 -> 4
     */
    public int getLengthOfPeriod(){
        int posDivider = period.indexOf("-");
        String start = period.substring(0, posDivider);
        String end = period.substring(posDivider+1, period.length());
        return Integer.parseInt(end) - Integer.parseInt(start) + 1;
    }

    /**
     * @return start position of period
     * Eg: 4-5 -> 4,        6-9 -> 6
     */
    public int getPosOfPeriod(){
        return Integer.parseInt(period.substring(0, period.indexOf("-")));
    }

    /**
     * @return First Character of Name
     * Eg: "Dai so" -> "DS"
     */
    public String getAcronymOfName() {
        if (name == null) {
            return "";
        }
        String acronym = "";
        String temp[] = name.split(" ");
        for (String aTemp : temp) {
            if (aTemp != null && aTemp.length() > 0) {
                acronym += aTemp.charAt(0);
            }
        }
        acronym = acronym.toUpperCase();
        return acronym;
    }


    // Getter ----------------------------

    public String getAddress() {
        return address;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public String getSemester() {
        return semester;
    }

    public String getType() {
        return type;
    }

    public String getClassId() {
        return classId;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public String getTeacherName() {
        return teacherName;
    }

    @Override
    public String toString() {
        return name + ": " + period + ", " + dayOfWeek;
    }
}
