package com.blm.qiubopay.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blm.qiubopay.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    boolean back = false;

    public static ZBarScannerView.ResultHandler action = new ZBarScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {

        }
    };

    //camera permission is needed.

    @Override
    public void onCreate(Bundle state) {

        super.setTheme(R.style.AppTheme_camera);


        super.onCreate(state);

        //HTimerApp.getTimer().cancel();

        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with the result here

        back = true;

        onBackPressed();

        if(action != null)
            action.handleResult(result);

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onBackPressed() {

        if(!back && action != null)
            action.handleResult(null);

        back = false;

        finish();
    }

    @Override
    public void onStart(){
        super.onStart();

        //HTimerApp.getTimer().cancel();

    }
}

