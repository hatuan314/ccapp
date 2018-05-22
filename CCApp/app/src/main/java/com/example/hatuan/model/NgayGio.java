package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 29/03/2018.
 */

public class NgayGio {
    private String weekofyear;
    private String dayofyear;
    private String dayofweek;


    public NgayGio(String weekofyear, String dayofweek, String dayofyear) {
        this.dayofyear = dayofyear;
        this.dayofweek = dayofweek;
        this.weekofyear = weekofyear;
    }

    public String getDayofyear() {
        return dayofyear;
    }

    public void setDayofyear(String dayofyear) {
        this.dayofyear = dayofyear;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getWeekofyear() {
        return weekofyear;
    }

    public void setWeekofyear(String weekofyear) {
        this.weekofyear = weekofyear;
    }
}
