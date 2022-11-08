package com.blm.spectratechlib_api;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spectratech.lib.BluetoothHelper;
import com.spectratech.lib.CustomBaseActivity;
import com.spectratech.lib.Logger;
import com.spectratech.lib.ResourcesHelper;
import com.spectratech.lib.bluetooth.BluetoothConnectBaseThread;
import com.spectratech.lib.bluetooth.BluetoothConnectConstant;
import com.spectratech.lib.bluetooth.BluetoothListListView;
import com.spectratech.lib.bluetooth.BluetoothManagementInterface;
import com.spectratech.lib.bluetooth.Data_bluetoothdevice;

import java.util.Set;

public abstract class BluetoothConnectActivityBLM extends CustomBaseActivity implements BluetoothManagementInterface {

    private final String m_className = "BluetoothConnectActivity";

    private static final long MAX_WAIT_TIME_PAIRING_INMS = (60 * 1000);

    public static final String KEY_CONNECTIONMODE = "KEY_CONNECTIONMODE";

    public static final int VAL_CONNECTIONMODE_CONNECT = 0;
    public static final int VAL_CONNECTIONMODE_PAIR = 1;

    /**
     * Key use by external call RESPONSE_ENDACTITYWITHRESULT_WHENCONNECTED for putting data to intent
     */
    public static final String RESPONSE_ENDACTITYWITHRESULT_WHENCONNECTED = "RESPONSE_ENDACTITYWITHRESULT_WHENCONNECTED";

    /**
     * Request code for enable Bluetooth in a device
     */
    public static final int REQUEST_ENABLE_BT = 1001;

    private final int MSG_HANDLER_ENDACTIVITY = 1000;
    private final int MSG_HANDLER_ENDACTIVITY_WITH_RESULT_OK = 1001;
    private final int MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW = 1100;
    private final int MSG_HANDLER_REFRESHUI_CONNECTIONCONTROL = 1101;
    private final int MSG_HANDLER_REFRESHUI_STATUS = 1102;
    /**
     * Message id to handle hiding softkeyboard
     */
    public static final int MSG_HANDLER_HIDE_SOFTKEYBOARD = 1103;
    public static final int MSG_HANDLER_TOAST_MSG = 1104;
    public static final int MSG_HANDLER_DISMISSPROGRESSDIALOG = 1105;

    private int m_timeSearchCountInMS = 0;

    public LinearLayout m_mainll;

    private LinearLayout m_connectionModell;
    private TextView m_tv_connectionMode;

    private LinearLayout m_bluetoothconnectPanelConnectionll;
    private LinearLayout m_devicelistPairedContainerll;
    private LinearLayout m_devicelistSearchedContainerll;
    private LinearLayout m_btStatusContainerll;
    private TextView m_statustv;
    private TextView m_messageSinglelinetv;

    private BluetoothListListView m_btPariedLV;
    private BluetoothListListView m_btSearchedLV;

    private Toast m_toast;

    private BroadcastReceiver m_broadcastReceiver;

    BluetoothConnectActivityBLM.BluetoothDevicePairThread m_devicePairThreadInst;

    BluetoothConnectActivityBLM.ConnectThread m_connectThreadInst;

    private Handler m_handler;

    private boolean m_bResponseEndActivtyWithResult;

    protected int m_connectionMode;

    private int m_n_tryHidenSoftKeyboardAtStartup;

    private TextView m_titleBluetoothConnectTV;

    /**
     * Variable to store UUID string
     */
    protected String m_uuid_string = BluetoothConnectConstant.DEFAULT_UUID_STRING;

    /**
     * Flag to set use of secure RfcommSocket connection
     */
    protected boolean m_bUseSecureRfcommSocket;

    protected boolean m_bInMultiWindowMode;

    private String blm_device_name;
    private String blm_device_address;

    private boolean showToast = true;
    /**
     * OnCreate function
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        m_bInMultiWindowMode=android7_isInMultiWindowMode();

        initHandler();

        m_mainll = (LinearLayout) LinearLayout.inflate(m_context, R.layout.activity_bluetoothconnect, null);
        setContentView(m_mainll);

        m_mainll.setVisibility(View.GONE);

        //this.setVisible(View.GONE);
        m_connectionModell = (LinearLayout) m_mainll.findViewById(R.id.connectionModeContainer);
        m_tv_connectionMode = (TextView) m_mainll.findViewById(R.id.tv_connectionMode);

        m_bluetoothconnectPanelConnectionll = (LinearLayout) m_mainll.findViewById(R.id.bluetoothconnect_panel_connection);
        m_btStatusContainerll = (LinearLayout) m_mainll.findViewById(R.id.btStatusContainer);
        m_statustv = (TextView) m_mainll.findViewById(R.id.tv_btStatus);
        m_messageSinglelinetv = (TextView) m_mainll.findViewById(R.id.message_singleline);
        m_devicelistPairedContainerll = (LinearLayout) m_mainll.findViewById(R.id.device_paired_container);
        m_devicelistSearchedContainerll = (LinearLayout) m_mainll.findViewById(R.id.device_searched_container);
        m_btPariedLV = (BluetoothListListView) m_mainll.findViewById(R.id.bt_pairedlistview);
        if (m_btPariedLV != null) {
            m_btPariedLV.setTypeList_paired();
        }
        m_btSearchedLV = (BluetoothListListView) m_mainll.findViewById(R.id.bt_searchedlistview);
        if (m_btSearchedLV != null) {
            m_btSearchedLV.setTypeList_searched();
        }

        m_titleBluetoothConnectTV = (TextView) m_mainll.findViewById(R.id.title_bluetooth_connect);

        m_bResponseEndActivtyWithResult = false;
        // 1. get passed intent
        Intent intent = getIntent();
        String strTmp = intent.getStringExtra(RESPONSE_ENDACTITYWITHRESULT_WHENCONNECTED);

        blm_device_name     = intent.getStringExtra("blm_device_name");
        blm_device_address  = intent.getStringExtra("blm_device_address");

        if ((strTmp != null) && (strTmp.equals("1"))) {
            m_bResponseEndActivtyWithResult = true;
        }
        m_connectionMode = intent.getIntExtra(KEY_CONNECTIONMODE, VAL_CONNECTIONMODE_CONNECT);

        initUIs();

        refreshBluetoothPairedDeviceList();

        registerBroadcastReceiver();

        m_connectThreadInst = null;
        m_devicePairThreadInst = null;

        m_n_tryHidenSoftKeyboardAtStartup = 1;

        m_bUseSecureRfcommSocket = true;

        BLMConnectDevice();
    }

    private boolean android7_isInMultiWindowMode() {
        boolean bInMultiWindowMode=false;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInMultiWindowMode()) {
                bInMultiWindowMode = true;
            }
        }
        return bInMultiWindowMode;
    }

    /**
     * OnResume function
     */
    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * This interface is called when user has Bluetooth connection action.
     * It should be implemented in child class
     */
    @Override
    public abstract void userActionBTConnectTrigger();

    /**
     * This interface is called when user has Bluetooth disconnection action.
     * It should be implemented in child class
     */
    @Override
    public abstract void userActionBTDisconnectTrigger();

    /**
     * This interface queries for the existence of active Bluetooth device.
     * It should be implemented in child class
     *
     * @return true if there is a bluetooth device connected; false if there is no bluetooth device connected
     */
    @Override
    public abstract boolean isActiveBluetoothDeviceConnected();

    /**
     * This interface set active Bluetooth device.
     * It should be implemented in child class
     *
     * @param device BluetoothDevice object
     */
    @Override
    public abstract void setActiveBluetoothDevice(BluetoothDevice device);

    /**
     * This interface gets the active Bluetooth device
     * It should be implemented in child class
     *
     * @return BluetoothDevice object; null for no BluttoothDevice object
     */
    @Override
    public abstract BluetoothDevice getActiveBluetoothDevice();

    /**
     * This interface unset active Bluetooth device
     * It should be implemented in child class
     *
     * @param device BluetoothDevice
     */
    @Override
    public abstract void unsetActiveBluetoothDevice(BluetoothDevice device);

    /**
     * This interface manages Bluetooth socket.
     * It should be implemented in child class
     *
     * @param socket Bluetooth socket provided by BLuetooth connect activity and manage it if necessary
     */
    @Override
    public abstract void manageConnectedSocket(BluetoothSocket socket);

    /**
     * This interface frees the resource occupied by the BluetoothSocket which is set by manageConnectedSocket
     * It should be implemented in child class
     */
    @Override
    public abstract void safeFreeBTSocket();

    /**
     * Set the title of this activity
     *
     * @param val title string
     */
    public void setTitle(String val) {
        if (m_titleBluetoothConnectTV != null) {
            if (val != null) {
                m_titleBluetoothConnectTV.setText(val);
            }
        }
    }

    private void initHandler() {
        m_handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what != MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW) {
                    Logger.i(m_className, "Handler message: " + msg.what);
                }
                switch (msg.what) {
                    case MSG_HANDLER_HIDE_SOFTKEYBOARD:
                        View v = getCurrentFocus();
                        if (v == null) {
                            if (m_n_tryHidenSoftKeyboardAtStartup > 10) {
                                return;
                            }
                            m_n_tryHidenSoftKeyboardAtStartup++;
                            Logger.i(m_className, "handler, try to hide soft keyboard, try: " + m_n_tryHidenSoftKeyboardAtStartup);
                            m_handler.sendEmptyMessageDelayed(MSG_HANDLER_HIDE_SOFTKEYBOARD, 500);
                            return;
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        break;
                    case MSG_HANDLER_ENDACTIVITY:
                        endActivity();
                        break;
                    case MSG_HANDLER_ENDACTIVITY_WITH_RESULT_OK:
                        endActivityWithResultOK();
                        break;
                    case MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW:
                        if (m_timeSearchCountInMS <= 0) {
                            stopBluetooth_clientSearch();
                            setTVMessageSingleLine();
                            return;
                        }
                        int timeInMS = 1000;
                        m_handler.sendEmptyMessageDelayed(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW, timeInMS);
                        int tINS = (int) Math.floor((float) m_timeSearchCountInMS / 1000);
                        String strSearching = ResourcesHelper.getBaseContextString(m_context, R.string.searching);
                        setTVMessageSingleLine(strSearching + " (" + tINS + ") ...");
                        m_timeSearchCountInMS -= timeInMS;
                        break;
                    case MSG_HANDLER_REFRESHUI_CONNECTIONCONTROL:
                        refreshUI_connectionControl();
                        break;
                    case MSG_HANDLER_REFRESHUI_STATUS:
                        String strPrefix = (String) msg.obj;
                        Logger.i(m_className, "m_handler, MSG_HANDLER_REFRESHUI_STATUS, " + strPrefix);
                        updateUI_BlutetoothDeviceStatus(strPrefix);
                        break;
                    case MSG_HANDLER_TOAST_MSG: {
                        String strMsg = (String) msg.obj;
                        ToastMessage(strMsg);
                    }
                    break;
                    case MSG_HANDLER_DISMISSPROGRESSDIALOG: {
                        BluetoothConnectActivityBLM.this.dismissProgressDialog();
                    }
                    break;
                }
            }
        };
    }

    public void DismissProgressDialogByHandler() {
        m_handler.sendEmptyMessage(MSG_HANDLER_DISMISSPROGRESSDIALOG);
    }

    public void ToastMessageByHandler(String strMsg) {
        if(showToast) {
            Message msg = m_handler.obtainMessage(MSG_HANDLER_TOAST_MSG);
            msg.obj = strMsg;
            m_handler.sendMessage(msg);
        }
    }

    public void ToastMessage(String msg) {
        if(showToast) {
            if (m_toast != null) {
                m_toast.cancel();
                m_toast = null;
            }
            m_toast = Toast.makeText(m_context, msg, Toast.LENGTH_SHORT);
            m_toast.show();
        }
    }

    private void initUIs() {
        if (m_connectionMode == VAL_CONNECTIONMODE_PAIR) {
            String strPairingMode = ResourcesHelper.getBaseContextString(m_context, R.string.mode_pairing);
            m_connectionModell.setVisibility(View.VISIBLE);
            m_tv_connectionMode.setText(strPairingMode);
            m_btStatusContainerll.setVisibility(View.GONE);
            m_bluetoothconnectPanelConnectionll.setVisibility(View.GONE);
        }
        refreshUI_BluetoothDeviceStatus();
        refreshUI_connectionControl();
    }

    private void refreshUI_connectionControl() {
        if (m_bluetoothconnectPanelConnectionll == null) {
            return;
        }

        // connect button only shows in VAL_CONNECTIONMODE_CONNECT
        if (m_connectionMode != VAL_CONNECTIONMODE_CONNECT) {
            return;
        }

        BluetoothDevice btDevice = getActiveBluetoothDevice();

        int visibility = View.GONE;
        if (btDevice != null) {
            visibility = View.VISIBLE;
        }
        m_bluetoothconnectPanelConnectionll.setVisibility(visibility);
    }

    private synchronized void updateUI_deviceLists() {
        if (m_btPariedLV.getDataCount() > 0) {
            m_devicelistPairedContainerll.setVisibility(View.VISIBLE);
        } else {
            m_devicelistPairedContainerll.setVisibility(View.GONE);
        }
        if (m_btSearchedLV.getDataCount() > 0) {
            m_devicelistSearchedContainerll.setVisibility(View.VISIBLE);
        } else {
            m_devicelistSearchedContainerll.setVisibility(View.GONE);
        }
    }

    /**
     * Refreshs TextView text which shown the connection status of Bluetooth
     */
    public void refreshUI_BluetoothDeviceStatus() {
        boolean bConnected = isActiveBluetoothDeviceConnected();
        String strPrefix = "";
        if (bConnected) {
            String strConnected = ResourcesHelper.getBaseContextString(m_context, R.string.connected);
            strPrefix = strConnected;
        } else {
            String strDisconnected = ResourcesHelper.getBaseContextString(m_context, R.string.disconnected);
            strPrefix = strDisconnected;
        }
        updateUI_BlutetoothDeviceStatus(strPrefix);
    }

    private void updateUI_BlutetoothDeviceStatus(String strPrefix) {
        if (strPrefix == null) {
            strPrefix = "";
        }

        BluetoothDevice btDevice = getActiveBluetoothDevice();

        if (btDevice != null) {
            Data_bluetoothdevice dataBluetoothDevice = new Data_bluetoothdevice(btDevice);
            String strMiddle = "";
            if (!strPrefix.equals("")) {
                strMiddle = " - ";
            }
            setTVStatus(strPrefix + strMiddle + dataBluetoothDevice.getConcateNamePlain());
        } else {
            setTVStatus("-");
        }
    }

    private void updateUI_BlutetoothDeviceStatusByHandler(String strPrefix) {
        Message msg = new Message();
        msg.what = MSG_HANDLER_REFRESHUI_STATUS;
        msg.obj = strPrefix;
        m_handler.sendMessage(msg);
    }

    private boolean addressExistinLists(String address) {
        boolean bExist = false;
        if ((address == null) || (address.equals(""))) {
            Logger.e(m_className, "addressExistinLists, address is NULL");
            return bExist;
        }
        Data_bluetoothdevice device = m_btPariedLV.getDataBluetoothDevice(address);
        bExist = (device != null) ? true : false;
        if (!bExist) {
            device = m_btSearchedLV.getDataBluetoothDevice(address);
            bExist = (device != null) ? true : false;
        }
        return bExist;
    }

    private void refreshBluetoothPairedDeviceList() {
        BluetoothHelper btHelperInst = BluetoothHelper.getInstance();
        if (!btHelperInst.isSupportedBluetooth()) {
            return;
        }

        BluetoothAdapter btAdapter = btHelperInst.getBluetoothAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Data_bluetoothdevice data = new Data_bluetoothdevice(device);
                String address = data.getAddress();
                if (!addressExistinLists(address)) {
                    m_btPariedLV.add(data);
                    //BLM CONNECT
                    if(data.getAddress().equals(blm_device_address))
                        onClick_btconnect(device);
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listview_notifyDataSetChanged(m_btPariedLV);
                updateUI_deviceLists();
            }
        });
    }

    private void listview_updateHeight(BluetoothListListView lv_inst) {
        if (lv_inst==null) {
            return;
        }
        Logger.i(m_className, "listview_updateHeight, bInMultiWindowMode: "+m_bInMultiWindowMode);
        if (m_bInMultiWindowMode) {
            lv_inst.setView2TotalHeightofListView();
        }
        else {
            lv_inst.setView2DefaultHeightofListView();
        }
    }

    private void listview_notifyDataSetChanged(BluetoothListListView lv_inst) {
        if (lv_inst!=null) {
            lv_inst.notifyDataSetChanged();
            listview_updateHeight(lv_inst);
        }
    }

    private void stopBluetooth_clientSearch() {
        BluetoothHelper btHelperInst = BluetoothHelper.getInstance();
        btHelperInst.discoverBluetoothDevices_cancel();

        m_handler.removeMessages(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW);
    }

    private void startBluetooth_clientSearch() {
        BluetoothHelper btHelperInst = BluetoothHelper.getInstance();
        if (!btHelperInst.isSupportedBluetooth()) {
            return;
        }
        if (!btHelperInst.isEnableBluetooth()) {
            Activity act = this;
            btHelperInst.requestEnableBluetooth(act, REQUEST_ENABLE_BT);
        } else {
            startSearchBluetoothDevices();
        }

    }

    private void startSearchBluetoothDevices() {
        BluetoothHelper btHelperInst = BluetoothHelper.getInstance();
        btHelperInst.discoverBluetoothDevices_cancel();
        if (btHelperInst.isSupportedBluetooth()) {
            m_btSearchedLV.clear();
            btHelperInst.discoverBluetoothDevices();
            restartSearchBluetoothCountDown();
        }
    }

    private void restartSearchBluetoothCountDown() {
        m_handler.removeMessages(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW);
        m_timeSearchCountInMS = BluetoothConnectConstant.BT_SEARCH_DEVICE_TIME_INMS;
        m_handler.sendEmptyMessage(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW);
    }

    /**
     * Upair a Bluetooth device
     *
     * @param data_btdevice Data_bluetoothdevice object (com.spectratech.lib.bluetooth.Data_bluetoothdevice)
     */
    public void unpairBluetoothDevice(Data_bluetoothdevice data_btdevice) {
        final String strUnpair = ResourcesHelper.getBaseContextString(m_context, R.string.unpair);
        BluetoothHelper btHelperInst = BluetoothHelper.getInstance();
        BluetoothDevice device = data_btdevice.getBTDevice();
        btHelperInst.unpairDevice(device);
        String address = device.getAddress();
        m_btPariedLV.remove(data_btdevice);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listview_notifyDataSetChanged(m_btPariedLV);
                updateUI_deviceLists();
            }
        });

        if (isSameActiveBTDevice(device)) {
            unsetActiveBluetoothDevice(device);
        }

        setTVMessageSingleLine(strUnpair + " " + data_btdevice.getConcateNamePlain());

        ToastMessageByHandler(strUnpair + "\n" + data_btdevice.getConcateName());
    }

    private void registerBroadcastReceiver() {
        if (m_broadcastReceiver == null) {
            m_broadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    String action = intent.getAction();

                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        String strTmp = m_className;
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        Data_bluetoothdevice data = new Data_bluetoothdevice(device);
                        String address = device.getAddress();
                        if (!addressExistinLists(address)) {
                            m_btSearchedLV.add(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listview_notifyDataSetChanged(m_btSearchedLV);
                                    updateUI_deviceLists();
                                }
                            });
                        }
                    } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        if (m_connectionMode == VAL_CONNECTIONMODE_PAIR) {
                        } else {
                            BluetoothDevice activeBtDevice = getActiveBluetoothDevice();
                            if ((activeBtDevice != null) && (activeBtDevice.equals(device))) {
                                String strConnected = ResourcesHelper.getBaseContextString(m_context, R.string.connected);
                                updateUI_BlutetoothDeviceStatusByHandler(strConnected);
                                BluetoothConnectActivityBLM.this.dismissProgressDialog();
                            }
                        }
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        if (m_connectionMode == VAL_CONNECTIONMODE_PAIR) {
                            if ((m_devicePairThreadInst != null) && (!m_devicePairThreadInst.isFinished())) {
                                BluetoothDevice btDeviceParing = m_devicePairThreadInst.getBluetoothDevice();
                                if (btDeviceParing.equals(device)) {
                                    m_devicePairThreadInst.cancel();
                                }
                            }
                        } else {
                            BluetoothDevice activeBtDevice = getActiveBluetoothDevice();
                            if ((activeBtDevice != null) && (activeBtDevice.equals(device))) {
                                String strDisconnected = ResourcesHelper.getBaseContextString(m_context, R.string.disconnected);
                                updateUI_BlutetoothDeviceStatusByHandler(strDisconnected);
                                BluetoothConnectActivityBLM.this.dismissProgressDialog();
                                if (device != null) {
                                    final Data_bluetoothdevice dataBluetoothDevice = new Data_bluetoothdevice(device);
                                    ToastMessageByHandler(strDisconnected + " - " + dataBluetoothDevice.getConcateNamePlain());
                                }
                            }
                        }
                    }
                }
            };

            HandlerThread handlerThread = new HandlerThread("btconnectReceiverHandler");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            Handler handler = new Handler(looper);

            // Register the BroadcastReceiver
            // Don't forget to unregister during onDestroy
            IntentFilter filter = new IntentFilter();
            // ACTION_STATE_CHANGED
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            // ACTION_FOUND
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            // ACTION_ACL_CONNECTED
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            // ACTION_ACL_DISCONNECT_REQUESTED
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            // ACTION_ACL_DISCONNECTED
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

            registerReceiver(m_broadcastReceiver, filter, null, handler);
        }
    }

    private void unregisterBroadcastReceivers() {
        if (m_broadcastReceiver != null) {
            unregisterReceiver(m_broadcastReceiver);
            m_broadcastReceiver = null;
        }
    }

    /**
     * onActivityResult function
     *
     * @param requestCode Request code from the activity which sends result intent
     * @param resultCode  Result code from the activity which sends result intent
     * @param data        Intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                String strEnableBluetT = ResourcesHelper.getBaseContextString(m_context, R.string.bluetooth_enabled);
                Toast.makeText(m_context, strEnableBluetT, Toast.LENGTH_SHORT).show();
                refreshBluetoothPairedDeviceList();
                startSearchBluetoothDevices();
            }
        }
    }

    private void bluetoothParingDevice_onDestroy() {
        m_handler.removeMessages(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW);
        stopBluetooth_clientSearch();
        unregisterBroadcastReceivers();
    }

    /**
     * onDestroy function
     */
    @Override
    protected void onDestroy() {
        bluetoothParingDevice_onDestroy();
        super.onDestroy();
    }

    private void setTVStatus() {
        setTVStatus("");
    }

    private void setTVStatus(String strText) {
        setTV(m_statustv, strText);
    }

    private void setTVMessageSingleLine() {
        setTVMessageSingleLine("");
    }

    private void setTVMessageSingleLine(String strText) {
        setTV(m_messageSinglelinetv, strText);
    }

    private void setTV(TextView tv, String strText) {
        if (tv != null) {
            tv.setText(strText);
        }
    }

    // pair bluetooth device
    private class BluetoothDevicePairThread extends Thread {
        private static final String m_fnName = "BluetoothConnectBaseThread";

        /**
         * Variable stores reference id for this thread
         */
        protected int m_idRef;

        protected BluetoothDevice m_btDevice2Pair;

        /**
         * flag to indicate cancel of operation
         */
        protected boolean m_bCancel;

        /**
         * flag to indiciate finish of operation
         */
        protected boolean m_bFinish;


        public BluetoothDevicePairThread(BluetoothDevice device) {
            int idRef = -1;
            init(device, idRef);
        }

        public BluetoothDevicePairThread(BluetoothDevice device, int idRef) {
            init(device, idRef);
        }

        private void init(BluetoothDevice device, int idRef) {
            m_btDevice2Pair = device;
            m_idRef = idRef;
            m_bCancel = false;
            m_bFinish = false;
        }

        public BluetoothDevice getBluetoothDevice() {
            return m_btDevice2Pair;
        }

        public void run() {
            Logger.i(m_className, "BluetoothDevicePairThread, run");

            if (m_btDevice2Pair.getBondState() == BluetoothDevice.BOND_BONDED) {
                DismissProgressDialogByHandler();
                String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.device_hasbeenalready_paired);
                ToastMessageByHandler(strMsg);
                m_bFinish = true;
                return;
            }

            BluetoothHelper btHelperInst = BluetoothHelper.getInstance();

            btHelperInst.discoverBluetoothDevices_cancel();

            boolean bUpdateListViews = false;
            if (m_btDevice2Pair.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    btHelperInst.pairDevice(m_btDevice2Pair);
                    bUpdateListViews = true;
                } catch (Exception ex) {
                    Logger.w(m_className, "initBluetoothSocket, pairDevice ex: " + ex.toString());
                }
            }
            long tSlotInMs = 500;
            long accWaitTime = 0;
            while ((m_btDevice2Pair.getBondState() != BluetoothDevice.BOND_BONDED) && (accWaitTime < MAX_WAIT_TIME_PAIRING_INMS)) {
                if (m_bCancel) {
                    break;
                }
                accWaitTime += tSlotInMs;
                try {
                    Thread.sleep(tSlotInMs);
                } catch (InterruptedException ie) {
                }
            }
            if (m_btDevice2Pair.getBondState() != BluetoothDevice.BOND_BONDED) {
                DismissProgressDialogByHandler();
                String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.no_device_hasbeen_paired);
                Logger.w(m_className, strMsg);
                ToastMessageByHandler(strMsg);
                m_bFinish = true;
                return;
            }

            if (bUpdateListViews) {
                updateListViews();
            }

            // disconnect current
            safeFreeBTSocket();
            userActionBTDisconnectTrigger();

            setActiveBluetoothDevice(m_btDevice2Pair);

            DismissProgressDialogByHandler();
            String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.device_hasbeen_paired) + ": " + m_btDevice2Pair.getAddress();
            ToastMessageByHandler(strMsg);
            m_bFinish = true;

            if (m_bResponseEndActivtyWithResult) {
                m_handler.sendEmptyMessage(MSG_HANDLER_ENDACTIVITY_WITH_RESULT_OK);
            }
        }

        /**
         * Get address of BluetoothDevice
         *
         * @return address of BluetoothDevice
         */
        public String getDeviceAddress() {
            return m_btDevice2Pair.getAddress();
        }

        /**
         * Set finish of operation
         */
        public void setFinishFlag() {
            m_bFinish = true;
        }

        /**
         * Check for finish of operation
         *
         * @return true if finish; false otherwise
         */
        public boolean isFinished() {
            return m_bFinish;
        }

        /**
         * Check for cancel of operation
         *
         * @return true if cancel; false otherwise
         */
        public boolean isCancelled() {
            return m_bCancel;
        }

        /**
         */
        public void cancel() {
            m_bCancel = true;
        }

        private void updateListViews() {
            String address = m_btDevice2Pair.getAddress();
            Data_bluetoothdevice dataDevice = m_btPariedLV.getDataBluetoothDevice(address);
            boolean bContain = (dataDevice != null) ? true : false;
            if (!bContain) {
                Data_bluetoothdevice dataBluetoothdevice = new Data_bluetoothdevice(m_btDevice2Pair);
                synchronized (m_btPariedLV) {
                    m_btPariedLV.add(dataBluetoothdevice);
                }
                synchronized (m_btSearchedLV) {
                    m_btSearchedLV.remove(dataBluetoothdevice);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listview_notifyDataSetChanged(m_btPariedLV);
                        updateUI_deviceLists();
                    }
                });
            }
        }
    }


    // Connecting as a client
    private class ConnectThread extends BluetoothConnectBaseThread {
        private static final String m_fnName = "ConnectThread";

        public ConnectThread(BluetoothDevice device) {
            super(device);
        }

        public ConnectThread(BluetoothDevice device, int idRef) {
            super(device, idRef);
        }

        @Override
        protected void initBluetoothSocket() {
            BluetoothHelper btHelperInst = BluetoothHelper.getInstance();

            mmSocket = btHelperInst.getBluetoothSocketFromBluetoothDevice(m_btDevice_ConnectBaseThread, m_uuid_string, m_bUseSecureRfcommSocket);

            if (mmSocket!=null) {
                try {
                    long tSleepInMs=1000;
                    Thread.sleep(tSleepInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateListViews() {
            String address = m_btDevice_ConnectBaseThread.getAddress();
            Data_bluetoothdevice dataDevice = m_btPariedLV.getDataBluetoothDevice(address);
            boolean bContain = (dataDevice != null) ? true : false;
            if (!bContain) {
                Data_bluetoothdevice dataBluetoothdevice = new Data_bluetoothdevice(m_btDevice_ConnectBaseThread);
                synchronized (m_btPariedLV) {
                    m_btPariedLV.add(dataBluetoothdevice);
                }
                synchronized (m_btSearchedLV) {
                    m_btSearchedLV.remove(dataBluetoothdevice);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listview_notifyDataSetChanged(m_btPariedLV);
                        updateUI_deviceLists();
                    }
                });
            }
        }

        public void run() {
            run_socketConnect();

            if (m_bCancel) {
                Logger.i(m_fnName, "thread cancelled and return, idRef: " + m_idRef);
                return;
            }
            if (!m_bConnected) {
                Logger.i(m_fnName, "socket NOT connected and return, idRef: " + m_idRef);
                setFinishFlag();
                return;
            }

            Logger.i(m_fnName, "socket connected, idRef: " + m_idRef);

            setActiveBluetoothDevice(m_btDevice_ConnectBaseThread);
            m_handler.sendEmptyMessage(MSG_HANDLER_REFRESHUI_CONNECTIONCONTROL);

            updateListViews();

            manageConnectedSocket(mmSocket);

            Logger.i(m_fnName, m_fnName + ", finish ConnectThread run, idRef: " + m_idRef);
            final Data_bluetoothdevice fdataDevice = new Data_bluetoothdevice(m_btDevice_ConnectBaseThread);
            final String fstrConnected = ResourcesHelper.getBaseContextString(m_context, R.string.connected);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI_BlutetoothDeviceStatus(fstrConnected);
                }
            });
            DismissProgressDialogByHandler();
            ToastMessageByHandler(fstrConnected + " - " + fdataDevice.getConcateNamePlain());
            setFinishFlag();

            if (m_bResponseEndActivtyWithResult) {
                m_handler.sendEmptyMessage(MSG_HANDLER_ENDACTIVITY_WITH_RESULT_OK);
            }
        }
    }

    private int countBTEvent = 0;

    private void loadDevicePairThreadIntance(BluetoothDevice btDevice) {
        if (btDevice == null) {
            Logger.e(m_className, "reloadDevicePairThreadIntance, btDevice is NULL");
            return;
        }
        if (m_devicePairThreadInst != null) {
            if (!m_devicePairThreadInst.isFinished()) {
                String strPleaseWait = ResourcesHelper.getBaseContextString(m_context, R.string.prompt_please_wait_message);
                ToastMessageByHandler(strPleaseWait);
                return;
            }
            m_devicePairThreadInst = null;
        }

        this.showProgressDialogAndDismissInTimeSlot(30 * 1000);

        m_devicePairThreadInst = new BluetoothConnectActivityBLM.BluetoothDevicePairThread(btDevice, countBTEvent);
        m_devicePairThreadInst.start();
        countBTEvent++;
    }

    private void reloadConnectThreadInstance(BluetoothDevice btDevice) {
        if (btDevice == null) {
            Logger.e(m_className, "reloadConnectThreadInstance, btDevice is NULL");
            return;
        }
        boolean bValidAndSameActivBTConnection = isValidAndSameActiveBTConnection(btDevice);
        if (bValidAndSameActivBTConnection) {
            return;
        }

        if (m_connectThreadInst != null) {
            m_connectThreadInst.cancel();
        }

        this.showProgressDialogAndDismissInTimeSlot(10 * 1000);

        m_connectThreadInst = new BluetoothConnectActivityBLM.ConnectThread(btDevice, countBTEvent);
        m_connectThreadInst.start();
        countBTEvent++;
    }

    /**
     * Check BluetoothDevice the same as active BluetoothDevice
     *
     * @param btDevice BluetoothDevice object
     * @return true if the same; false otherwise
     */
    public boolean isSameActiveBTDevice(BluetoothDevice btDevice) {
        boolean bSame = false;
        if (btDevice == null) {
            return bSame;
        }
        BluetoothDevice btActiveDevice = getActiveBluetoothDevice();
        if (btActiveDevice == null) {
            return bSame;
        }
        String add1 = btActiveDevice.getAddress();
        String add2 = btDevice.getAddress();
        if (add1.equals(add2)) {
            bSame = true;
        }
        return bSame;
    }

    /**
     * Verify active BluetoothDevice and check the same as active BluetoothDevice
     *
     * @param btDevice BluetoothDevice object
     * @return true if the same; false otherwise
     */
    public boolean isValidAndSameActiveBTConnection(BluetoothDevice btDevice) {
        boolean bValid = false;
        BluetoothDevice btActiveDevice = getActiveBluetoothDevice();
        if (btActiveDevice == null) {
            return false;
        }
        String add1 = btActiveDevice.getAddress();
        String add2 = btDevice.getAddress();
        if (add1.equals(add2)) {
            boolean bConnected = isActiveBluetoothDeviceConnected();
            if (bConnected) {
                Data_bluetoothdevice dataBTDevice = new Data_bluetoothdevice(btDevice);
                String strAlreadyConnected = ResourcesHelper.getBaseContextString(m_context, R.string.already_connected);
                ToastMessage(strAlreadyConnected + " - " + dataBTDevice.getConcateNamePlain());
                bValid = true;
            }
        }
        return bValid;
    }

    /**
     * Function used for onclick of Translucent view
     *
     * @param v Corresponding onclick view
     */
    public void onClick_translucent(View v) {
        endActivity();
    }

    /**
     * Bluetooth pair
     */
    public void btpair(BluetoothDevice btDevice) {
        loadDevicePairThreadIntance(btDevice);
    }

    /**
     * Function used for onclick of Bluetooth connect button
     *
     * @param v Corresponding onclick view
     */
    public void onClick_btconnect(View v) {
        userActionBTConnectTrigger();
        BluetoothDevice activeDevice = getActiveBluetoothDevice();
        reloadConnectThreadInstance(activeDevice);
    }

    public void BLMConnectDevice(){
        userActionBTConnectTrigger();
        BluetoothDevice activeDevice = getActiveBluetoothDevice();
        reloadConnectThreadInstance(activeDevice);
    }

    /**
     * Function used for connect BluetoothDevice object
     *
     * @param btDevice BluetoothDevice
     */
    public void onClick_btconnect(BluetoothDevice btDevice) {
        if (m_connectionMode == VAL_CONNECTIONMODE_PAIR) {
            loadDevicePairThreadIntance(btDevice);
        } else {
            userActionBTConnectTrigger();
            reloadConnectThreadInstance(btDevice);
        }
    }

    //public void BLMConnectDevice()

    /**
     * Function used for onclick of Bluetooth disconnect button
     *
     * @param v Corresponding onclick view
     */
    public void onClick_btdisconnect(View v) {
        userActionBTDisconnectTrigger();
        boolean bConnected = isActiveBluetoothDeviceConnected();
        if (!bConnected) {
            return;
        }

        this.showProgressDialogAndDismissInTimeSlot(10 * 1000);

        //safeFreeBTSocketByThread();
        safeFreeBTSocket();
    }

    /**
     * Function used for onclick of Bluetooth search button
     *
     * @param v Corresponding onclick view
     */
    public void onClick_btsearch(View v) {
        startBluetooth_clientSearch();
    }

    /**
     * Function used for onclick of cancel Bluetooth search button
     *
     * @param v Corresponding onclick view
     */
    public void onClick_btsearchcancel(View v) {
        m_handler.removeMessages(MSG_HANDLER_SEARCHBTDEVICE_COUNTDONW);
        BluetoothHelper instBluetoothHelper = BluetoothHelper.getInstance();
        if (instBluetoothHelper.isDiscovering()) {
            String strSearchCancel = ResourcesHelper.getBaseContextString(m_context, R.string.cancel_search);
            setTVMessageSingleLine(strSearchCancel);
        }
        stopBluetooth_clientSearch();
    }


    /**
     * onBackPressed
     */
    @Override
    public void onBackPressed() {
        endActivity();
    }

    private void endActivityWithResultOK() {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK, returnIntent);
        endActivity();
    }

    private void endActivity() {
        this.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        m_bInMultiWindowMode=isInMultiWindowMode;
        listview_updateHeight(m_btPariedLV);
        listview_updateHeight(m_btSearchedLV);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

