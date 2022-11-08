package com.blm.qiubopay.utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

public class DateUtils {

    private Context context;
    private Activity activity;
    private static final long THREE_DAYS_MS = 259200000;

    public DateUtils(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public Boolean areDatesEquals(int yearSaved, int monthSaved, int daySaved) {
        ArrayList<Integer> todayDate = getTodayDate();
        if (yearSaved != todayDate.get(0) || monthSaved != todayDate.get(1) || daySaved != todayDate.get(2)) return false;
        return true;
    }

    public ArrayList<Integer> getTodayDate() {
        ArrayList<Integer> todayDate = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        todayDate.add(year);
        todayDate.add(month);
        todayDate.add(day);
        return todayDate;
    }

    public boolean itIsAboutToExpire(int year, int month, int day) {
        if (getDifference(year, month, day) < THREE_DAYS_MS) return true;
        else return false;
    }

    public boolean itIsExpired(int year, int month, int day) {
        if (getDifference(year, month, day) < 0) return true;
        else return false;
    }

    public long getDifference(int year, int month, int day) {
        Calendar todayDate = Calendar.getInstance();
        Calendar couponValidity = Calendar.getInstance();
        couponValidity.set(Calendar.YEAR, year);
        couponValidity.set(Calendar.MONTH, month - 1);
        couponValidity.set(Calendar.DATE, day);
        long todayDate_ms = todayDate.getTimeInMillis();
        long couponValidity_ms = couponValidity.getTimeInMillis();
        return couponValidity_ms - todayDate_ms;
    }

}
