package com.blm.qiubopay.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blm.qiubopay.R;
import com.blm.qiubopay.utils.Utils;

public class AppImages {

    private static String TAG = "com.blm.qiuboplus.images";
    private static String NOT_FOUND = "NOT_FOUND";

    private static SharedPreferences sharedPreferences;

    private AppImages(){}

    public static void init(Context context){
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        setImage(NOT_FOUND, BitmapFactory.decodeResource(context.getResources(), R.drawable.no_disponible));
    }

    public static void clear(Context context){
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setImage(String name, Bitmap bitmap){
        try {  sharedPreferences.edit().putString(name, Utils.convertPNG(bitmap)).apply(); } catch (Exception ex) { }
    }

    public static Bitmap getImage(String name){
        try {
            return Utils.stringToBitMap(sharedPreferences.getString(name, ""));
        } catch (Exception ex) {
            return Utils.stringToBitMap(sharedPreferences.getString(NOT_FOUND, ""));
        }
    }

    public static boolean getExists(String name){
        return  !sharedPreferences.getString(name, "").isEmpty();
    }



}
