package com.blm.qiubopay.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseOpenHelper   extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "sepomex.sqlite";
    private static final int DATABASE_VERSION = 1;

    public DataBaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
