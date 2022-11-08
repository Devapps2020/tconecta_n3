package com.blm.spectratechlib_api;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;

import com.spectratech.lib.Logger;
import com.spectratech.lib.bluetooth.BluetoothManagementInterface;
import com.spectratech.lib.bluetooth.BluetoothStatusClass;
import com.spectratech.lib.bluetooth.BluetoothUserActionClass;
import com.spectratech.lib.sp530.comm_protocol_c.BtauxCHelper;
import com.spectratech.lib.sp530.comm_protocol_c.SP530_AppMcpCHelper;

/**
 * ApiAppBluetoothConnectActivity - SP530 BLuetooth connection activity
 */

public class ApiAppBluetoothConnectActivity extends BluetoothConnectActivityBLM implements BluetoothManagementInterface {

    private final String TAG = ApiAppBluetoothConnectActivity.class.getSimpleName();
    public BluetoothDevice theDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SP530");
        m_bUseSecureRfcommSocket = false;

        //this.setVisible(View.GONE);
    }

    @Override
    public void userActionBTConnectTrigger() {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity.m_btUserAction.m_stausUserAction != BluetoothUserActionClass.STATUS_USERACTION.USERACTION_BT_CONNECT) {
            inst.m_SP530BTEventsForActivity.m_btUserAction.m_stausUserAction = BluetoothUserActionClass.STATUS_USERACTION.USERACTION_BT_CONNECT;
        }
    }

    @Override
    public void userActionBTDisconnectTrigger() {
        SP530_AppMcpCHelper.freeInstance();
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity.m_btUserAction.m_stausUserAction != BluetoothUserActionClass.STATUS_USERACTION.USERACTION_BT_DISCONNECT) {
            inst.m_SP530BTEventsForActivity.m_btUserAction.m_stausUserAction = BluetoothUserActionClass.STATUS_USERACTION.USERACTION_BT_DISCONNECT;
            inst.m_SP530BTEventsForActivity.setBTAddress2SharedPreferences("");
        }
    }

    @Override
    public boolean isActiveBluetoothDeviceConnected() {
        boolean flag = false;
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity != null) {
            flag = inst.m_SP530BTEventsForActivity.isActiveBluetoothDeviceConnected();
        }
        return flag;
    }

    @Override
    public void setActiveBluetoothDevice(BluetoothDevice device) {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity!=null) {
            inst.m_SP530BTEventsForActivity.setActiveBluetoothDevice(device);
        }
        finish();
    }

    @Override
    public BluetoothDevice getActiveBluetoothDevice() {
        BluetoothDevice btDevice=null;
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity!=null) {
            btDevice=inst.m_SP530BTEventsForActivity.getActiveBluetoothDevice();
        }
        return btDevice;
    }

    @Override
    public void unsetActiveBluetoothDevice(BluetoothDevice device) {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity!=null) {
            inst.m_SP530BTEventsForActivity.unsetActiveBluetoothDevice(device);
        }
    }

    @Override
    public void manageConnectedSocket(BluetoothSocket socket) {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity!=null) {
            inst.m_SP530BTEventsForActivity.manageConnectedSocket(socket);
            inst.m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection = BluetoothStatusClass.STATUS_DEVICE_CONNECTION.CONNECTED;

            inst.safe_start_comm_SP530Communicator();
        }
    }

    @Override
    public void safeFreeBTSocket() {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_SP530BTEventsForActivity!=null) {
            // fix Android OS 4.4 crashes on bt disconnect
            BtauxCHelper instBtauxCHelper=BtauxCHelper.getInstance();
            instBtauxCHelper.setIO(null, null);

            inst.m_SP530BTEventsForActivity.safeFreeBTSocket();
        }
    }

    private void onOrientationPortrait() {
        // not full screen mode
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void onOrientationLandscape() {
        if (!m_bInMultiWindowMode) {
            // full screen mode
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.i(TAG, "onConfigurationChanged, m_bInMultiWindowMode: " + m_bInMultiWindowMode);
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                onOrientationPortrait();
            }
            break;
            case Configuration.ORIENTATION_LANDSCAPE: {
                onOrientationLandscape();
            }
            break;
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

}