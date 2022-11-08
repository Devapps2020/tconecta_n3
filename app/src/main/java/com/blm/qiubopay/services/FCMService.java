package com.blm.qiubopay.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.blm.qiubopay.helpers.AppPreferences;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    }

    @Override
    public void onNewToken(String token) {

        Log.d(TAG, "Refreshed token: " + token);

        Log.e("token 2", token);
        AppPreferences.setFCM(token);

    }


}