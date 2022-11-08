package com.blm.qiubopay.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.blm.qiubopay.R;

public class LoadingDialogPidelo {

    private Context context;
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialogPidelo(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void startLoading(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.alert_dialog_pidelo, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }

    public void dismissDialog (){
        alertDialog.dismiss();
    }
}
