package com.blm.qiubopay.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.tools.Tools;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IFunction;

public class HLocActivity extends HActivity {

    private HActivity context = this;

    //RSB 20200226. Improvements. Geolocalizacion
    private final String TAGLoc = "Location";
    private final int LOCATION_REQUEST_CODE = 2;
    public static final int REQUEST_CHECK_SETTINGS = 3;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //public LastLocation lastLocation;
    private IFunction functionLocation;
    private boolean forceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //RSB 20200226. Improvements. Geolocalizacion
        forceLocation = false;
        initializeLocation();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {

            case LOCATION_REQUEST_CODE:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(functionLocation!=null) {
                        functionLocation.execute();
                    }
                } else {
                    if(forceLocation){

                        boolean deniedloc = false;
                        boolean neverloc = false;

                        if (permissions.length>0 && ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]))
                            deniedloc = true;
                        else if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                            neverloc = true;

                        if (deniedloc && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            requestPermissions(permissions, requestCode);
                        else if (neverloc)
                            showAlertPermission(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert("Lo sentimos, no se puede acceder sin ubicación");
                                }
                            },new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });

                    } else {
                        //TODO No acepto los permisos
                        Log.d(TAGLoc,"No ha aceptado los permisos");
                    }
                }

                break;

        }
    }



    //RSB 20200226. Improvements. Geolocalizacion
    /**
     * Inicializa las variables para ubicacion.
     */
    public void initializeLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e(TAGLoc,"onLocationResult: NULL");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        Log.d(TAGLoc,"OnLocationResult: Lat. " + location.getLatitude() + " Long. " + location.getLongitude());
                        CApplication.setLastLocation(new LastLocation(location.getLatitude(),location.getLongitude(), Tools.getTodayDate()));
                        fusedLocationClient.removeLocationUpdates(locationCallback);

                        if(functionLocation!=null)
                            functionLocation.execute();

                    }
                }
            }
        };

    }

    //RSB 20200226. Improvements. Geolocalizacion
    /**
     * Método que obtiene la ultima localización conocida, si no inicia la actualizacion de ubicacion.
     */
    public void obtainLocation(final IFunction locfunction) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAGLoc,"Permissions required");

            functionLocation = new IFunction() {
                @Override
                public void execute(Object[] data) {
                    obtainLocation(locfunction);
                }
            };

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);

        } else {

            // already permission granted
            Log.d(TAGLoc,"Permissions already granted");

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {

                @Override
                public void onSuccess(final LocationSettingsResponse locationSettingsResponse) {

                    Log.d(TAGLoc,"All location settings are satisfied. The client can initialize location requests.");

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {

                                        // Logic to handle location object
                                        CApplication.setLastLocation(new LastLocation(location.getLatitude(),location.getLongitude(), Tools.getTodayDate()));

                                        if(locfunction!=null)
                                            locfunction.execute();

                                        String ubicacion = "GetLastLocation: Lat. " + location.getLatitude() + " Long. " + location.getLongitude();
                                        Log.d(TAGLoc,ubicacion);

                                    } else {
                                        Log.d(TAGLoc, "SUCCESS LISTENER ELSE");
                                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                                locationCallback, null /* Looper */);

                                    }
                                }
                            })
                            .addOnFailureListener(context, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    CApplication.setLastLocation(null);
                                    Log.e(TAGLoc,"Error onFailure getLastLocation: " + e.getMessage());
                                }
                            });

                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.

                        Log.e(TAGLoc,"Location settings are not satisfied, but this can be fixed by showing the user a dialog.");
                        try {

                            context.setOnActivityResult(new IActivityResult() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    Log.d(TAGLoc, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
                                    switch (requestCode) {
                                        case REQUEST_CHECK_SETTINGS:
                                            if (resultCode == Activity.RESULT_OK) {
                                                obtainLocation(locfunction);
                                                context.setOnActivityResult(null);
                                            } else {
                                                //TODO Que pasa si no accede a encender el sensor
                                                CApplication.setLastLocation(null);
                                                Log.d(TAGLoc,"No ha aceptado encender el sensor");
                                                if(Tools.isN3Terminal() && forceLocation){
                                                    obtainLocation(locfunction);
                                                    context.setOnActivityResult(null);
                                                }
                                            }
                                            break;
                                        default:
                                            this.onActivityResult(requestCode, resultCode, data);
                                            break;
                                    }
                                }
                            });

                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(context,REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                            Log.e(TAGLoc,"Task.onFailure: " + e.getMessage());
                        }
                    }
                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("WrongConstant")
    private void showAlertPermission(DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener configureListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Dialog);
        //builder.setMessage(getResources().getString(R.string.text_neverAskAgain));

        TextView view = new TextView(builder.getContext());
        view.setText(Html.fromHtml(getResources().getString(R.string.text_never_ask_again)));
        view.setPadding(10, 10, 10, 10);
        view.setTextColor(getResources().getColor(R.color.background));
        view.setTextSize(getResources().getDimension(R.dimen._5sdp));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            view.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        builder.setView(view);

        builder.setCancelable(false);
        builder.setTitle(R.string.app_name);

        builder.setPositiveButton(R.string.text_cancel, cancelListener);

        builder.setNegativeButton(R.string.text_configuration, configureListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    public boolean isForceLocation() {
        return forceLocation;
    }

    public void setForceLocation(boolean forceLocation) {
        this.forceLocation = forceLocation;
    }

}
