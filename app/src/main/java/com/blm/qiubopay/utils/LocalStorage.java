package com.blm.qiubopay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }

    public String getLocalString(String storeName, String itemName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(storeName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(itemName, "");
    }

    public Integer getLocalInt(String storeName, String itemName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(storeName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(itemName, -1);
    }

    public Boolean getLocalBoolean(String storeName, String itemName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(storeName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(itemName, false);
    }

}
