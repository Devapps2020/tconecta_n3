package com.blm.qiubopay.helpers;

import android.app.Activity;
import android.os.Build;

import com.orhanobut.logger.Logger;
import com.blm.qiubopay.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

//import com.example.android.multidex.myCApplication.BuildConfig;

public class HException implements Thread.UncaughtExceptionHandler {

    private Activity activity;
    private final String LINE_SEPARATOR = "\n";

    public HException(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {

        exception.printStackTrace();

        Logger.e(exception.getMessage(), exception);

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        StringBuilder messageReport = new StringBuilder();
        StringBuilder errorReport = new StringBuilder();
        StringBuilder deviceReport = new StringBuilder();
        StringBuilder firmwareReport = new StringBuilder();

        messageReport.append(exception.getCause().getMessage());

        errorReport.append(stackTrace.toString());

        deviceReport.append("Brand: ");
        deviceReport.append(Build.BRAND);
        deviceReport.append(LINE_SEPARATOR);
        deviceReport.append("Device: ");
        deviceReport.append(Build.DEVICE);
        deviceReport.append(LINE_SEPARATOR);
        deviceReport.append("Model: ");
        deviceReport.append(Build.MODEL);
        deviceReport.append(LINE_SEPARATOR);
        deviceReport.append("Id: ");
        deviceReport.append(Build.ID);
        deviceReport.append(LINE_SEPARATOR);
        deviceReport.append("Product: ");
        deviceReport.append(Build.PRODUCT);
        deviceReport.append(LINE_SEPARATOR);

        firmwareReport.append("APP: ");
        firmwareReport.append(BuildConfig.VERSION_NAME);
        firmwareReport.append(LINE_SEPARATOR);
        firmwareReport.append("SDK: ");
        firmwareReport.append(Build.VERSION.SDK);
        firmwareReport.append(LINE_SEPARATOR);
        firmwareReport.append("Release: ");
        firmwareReport.append(Build.VERSION.RELEASE);
        firmwareReport.append(LINE_SEPARATOR);
        firmwareReport.append("Incremental: ");
        firmwareReport.append(Build.VERSION.INCREMENTAL);
        firmwareReport.append(LINE_SEPARATOR);


        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}