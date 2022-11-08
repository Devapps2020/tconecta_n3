package com.blm.qiubopay.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.orhanobut.logger.Logger;


import mx.devapps.utils.components.HActivity;

public class SplashActivity extends HActivity {

    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //handleLink(getIntent());

        if(Tools.isN3Terminal()){
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.setCustomKey("environment", Globals.environment.toString());
            crashlytics.setCustomKey("device", "n3");
            crashlytics.setCustomKey("serial_number", N3Helper.getSn(getContext()));
        }

        MenuActivity.appLink = null;
        String appLinkAction = getIntent().getAction();
        Uri appLinkData = getIntent().getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            Logger.d(appLinkData.getLastPathSegment());
            MenuActivity.appLink = appLinkData.getLastPathSegment();
        }
        //Si ya esta abierto menu activity refresca un inicio de actividad
        if(MenuActivity.active){
            getContext().startActivity(MenuActivity.class, true);
            return;
        }

        if(AppPreferences.isBackground()) {
            AppPreferences.isBackground(false);
            getContext().startActivity(MenuActivity.class, true);
            return;
        }

        Thread timer=new Thread() {
            @Override
            public void run() {
                try {
                    music=MediaPlayer.create(SplashActivity.this, R.raw.bimbo_casi);
                    music.start();
                    sleep(4000);
                    getContext().startActivity(LoginActivity.class, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();

    }

    @Override
    protected void onPause() {

        try {
            super.onPause();
            music.release();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleLink(Intent intent){
        MenuActivity.appLink = null;
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            Logger.d(appLinkData.getLastPathSegment());
            MenuActivity.appLink = appLinkData.getLastPathSegment();
        }
        //Si ya esta abierto menu activity refresca un inicio de actividad
        if(MenuActivity.active){
            getContext().startActivity(MenuActivity.class, true);
            return;
        }
    }

}

