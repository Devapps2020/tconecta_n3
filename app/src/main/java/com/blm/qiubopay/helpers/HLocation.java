package com.blm.qiubopay.helpers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.tools.Tools;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class HLocation {

    private HActivity context;
    private boolean required = false;

    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

    private IFunction<Location> function = new IFunction<Location>() {
        @Override
        public void execute(Location[] data) {

        }
    };

    public static HLocation start(boolean required, HActivity context, IFunction<Location>... function) {
        return new HLocation(required, context, function);
    }

    private HLocation(boolean required, HActivity context, IFunction<Location>... function) {

        Log.d("HLocation","initLocation");

        this.context = context;
        this.required = required;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if(function != null && function.length > 0)
            this.function = function[0];

        LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        context.requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                if (!enabled) {

                    context.alert(Html.fromHtml(context.getResources().getString(R.string.text_localization_disabled)).toString(), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Configurar";
                        }
                        @Override
                        public void onClick() {

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);

                            if (required)
                                context.finish();

                        }
                    });

                } else {
                    start();
                }

            }
        }, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});

    }

    public void start() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnCompleteListener(context, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();
                    Log.d("HLocation",mLastLocation.getLatitude() + " : " + mLastLocation.getLongitude());
                    //Toast.makeText(context, "" + mLastLocation.getLatitude() + " : " + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    CApplication.setLastLocation(new LastLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(), Tools.getTodayDate()));
                }
            }
        });

    }

}
