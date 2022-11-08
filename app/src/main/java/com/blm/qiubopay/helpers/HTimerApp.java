package com.blm.qiubopay.helpers;

import android.os.CountDownTimer;

import com.blm.qiubopay.helpers.interfaces.ITimerApp;

import mx.devapps.utils.components.HActivity;

public class HTimerApp {

    private static volatile HTimerApp timer = new HTimerApp();

    private static CountDownTimer countTimer;

    private static ITimerApp iTimer;

    private static HActivity context;

    private static int minutes;

    public static HTimerApp getTimer(){
        return HTimerApp.timer;
    }

    public static void setTimer(int minutes, ITimerApp iTimer) {

        HTimerApp.getTimer().iTimer = iTimer;

        HTimerApp.getTimer().minutes = minutes;

    }

    public void start(HActivity context){

        HTimerApp.getTimer().context = context;

        if(HTimerApp.getTimer().iTimer != null)
        {
            if(HTimerApp.getTimer().countTimer != null)
                HTimerApp.getTimer().countTimer.cancel();

            HTimerApp.getTimer().countTimer = new CountDownTimer((HTimerApp.getTimer().minutes * 1000) * 60, 1000) {
                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                    HTimerApp.getTimer().iTimer.finish(HTimerApp.getTimer().context);
                    HTimerApp.getTimer().cancel();
                }
            };

            HTimerApp.getTimer().countTimer.start();
        }

    }

    public void cancel(){

        if(HTimerApp.getTimer().countTimer != null)
            HTimerApp.getTimer().countTimer.cancel();

        HTimerApp.getTimer().countTimer = null;

    }

    public boolean isCancel(){
        return HTimerApp.getTimer().countTimer == null;
    }

}
