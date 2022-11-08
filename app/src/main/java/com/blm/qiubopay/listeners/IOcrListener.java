package com.blm.qiubopay.listeners;

import android.graphics.Bitmap;

import com.blm.qiubopay.models.HOcrMatch;

import java.util.List;

public interface IOcrListener {

    void execute(List<HOcrMatch> data, Bitmap bitmap);

}
