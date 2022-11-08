package com.blm.qiubopay.listeners;

import android.widget.TextView;

public interface IFingerprint {

    TextView onAuthenticationStart();

    void onAuthenticationSucceeded();

    void onAuthenticationFailed();

}
