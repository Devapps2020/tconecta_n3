package com.blm.qiubopay.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xgd.smartpos.manager.ICloudService;
import com.xgd.smartpos.manager.system.ISystemUIOperate;

public class N3ConfigHelper extends AppCompatActivity {

    private static final String TAG = "N3ConfigHelper";

    private final int SYSTEMUI_OPERATE = 5;

    private ICloudService mICloudService = null;

    private Context context;

    private boolean disable;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "Service connected");
                try {
                    mICloudService = ICloudService.Stub.asInterface(service);
                } catch (Exception e){
                    e.printStackTrace();
                }
                setButtonControlbarConfiguration();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service disconnected");
            mICloudService = null;
        }
    };

    public N3ConfigHelper(Context context, boolean disable) {
        this.context = context;
        this.disable = disable;
        bindNexgoSystemService();
    }

    private void bindNexgoSystemService() {
        try {
            Intent mIntent = new Intent();
            mIntent.setComponent(new ComponentName("com.xgd.possystemservice", "com.xgd.smartpos.systemservice.SystemInterfaceService"));
            context.bindService(mIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setButtonControlbarConfiguration(){
        if(mICloudService == null){
            Toast.makeText(getParent(), "Service not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        IBinder binder;

        try {
            binder = mICloudService.getManager(SYSTEMUI_OPERATE);
            ISystemUIOperate systemManager  = ISystemUIOperate.Stub.asInterface(binder);

            Log.d(TAG,"Deshabilitando botón home");
            systemManager.enableHome(disable);     //true - disable home , false - enable home

            Log.d(TAG,"Deshabilitando botón TaskManager");
            systemManager.enableRecv(disable);     //true - disable task  , false - enable task

            context.unbindService(serviceConnection);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}



