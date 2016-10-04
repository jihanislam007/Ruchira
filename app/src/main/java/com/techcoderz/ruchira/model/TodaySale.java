package com.techcoderz.ruchira.model;

/**
 * Created by Shahriar on 10/4/2016.
 */

public class TodaySale {
    String yesterdeay;
    String monthAccumulation;
    String monthDailyAvg;
    String monthWeeklyAvg;
    String todaySale;

    public String getYesterdeay() {
        return yesterdeay;
    }

    public void setYesterdeay(String yesterdeay) {
        this.yesterdeay = yesterdeay;
    }

    public String getMonthDailyAvg() {
        return monthDailyAvg;
    }

    public void setMonthDailyAvg(String monthDailyAvg) {
        this.monthDailyAvg = monthDailyAvg;
    }

    public String getMonthAccumulation() {
        return monthAccumulation;
    }

    public void setMonthAccumulation(String monthAccumulation) {
        this.monthAccumulation = monthAccumulation;
    }

    public String getMonthWeeklyAvg() {
        return monthWeeklyAvg;
    }

    public void setMonthWeeklyAvg(String monthWeeklyAvg) {
        this.monthWeeklyAvg = monthWeeklyAvg;
    }

    public String getTodaySale() {
        return todaySale;
    }

    public void setTodaySale(String todaySale) {
        this.todaySale = todaySale;
    }
}
