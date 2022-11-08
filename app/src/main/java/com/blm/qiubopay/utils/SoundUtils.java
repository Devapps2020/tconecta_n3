package com.blm.qiubopay.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.blm.qiubopay.R;

public class SoundUtils {

    public void executeSound(Context context) {
        Thread timer=new Thread() {
            @Override
            public void run() {
                try {
                    MediaPlayer music;
                    music= MediaPlayer.create(context, R.raw.bimbo_casi);
                    music.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

}
