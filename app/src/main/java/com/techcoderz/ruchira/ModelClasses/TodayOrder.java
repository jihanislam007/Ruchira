package com.techcoderz.ruchira.ModelClasses;

/**
 * Created by Shahriar on 10/4/2016.
 */

public class TodayOrder {
    String yesterdeay;
    String monthAccumulation;
    String monthDailyAvg;
    String monthWeeklyAvg;
    String todayOrder;

    public String getTodayOrder() {
        return todayOrder;
    }

    public void setTodayOrder(String todayOrder) {
        this.todayOrder = todayOrder;
    }

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
}
