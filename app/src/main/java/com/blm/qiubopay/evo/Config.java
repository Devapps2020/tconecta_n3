package com.blm.qiubopay.evo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blm.qiubopay.utils.Globals;
import com.mastercard.gateway.android.sdk.Gateway;

public enum Config {

    KEY(Globals.debug ? "43bdec51a23ecff41462723b7d615132" : "9ba5eceed06364d37aa4a7ea99a0da90"),
    CURRENCY("MXN"),
    MERCHANT_ID(Globals.debug ? "TEST1142854" : "1142854"),
    REGION(Gateway.Region.NORTH_AMERICA.name()),
    MERCHANT_URL(Globals.debug ? "https://bimbonet-qa.herokuapp.com/" : "https://bimbonet.herokuapp.com");

    String defValue;

    Config(String defValue) {
        this.defValue = defValue;
    }

    public String getValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(this.name(), defValue);
    }

    public void setValue(Context context, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(this.name(), value);
        editor.apply();
    }
}