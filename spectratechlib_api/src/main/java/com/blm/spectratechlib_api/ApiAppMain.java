package com.blm.spectratechlib_api;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.spectratech.lib.Callback;
import com.spectratech.lib.Logger;
import com.spectratech.lib.TLVHelper;
import com.spectratech.lib.bluetooth.BluetoothSocketClass;
import com.spectratech.lib.bluetooth.BluetoothStatusClass;
import com.spectratech.lib.sp530.ApplicationProtocolHelper;
import com.spectratech.lib.sp530.comm_protocol_c.SP530_AppMcpCHelper;
import com.spectratech.lib.sp530.constant.ApplicationProtocolConstant;
import com.spectratech.lib.sp530app.SP530Communicator;
import com.spectratech.lib.sp530app.SP530Manager;
import com.spectratech.lib.sp530app.SP530_bt_S3INS_simple;
import com.spectratech.lib.sp530app.constant.FullEmvEnum;
import com.spectratech.lib.sp530app.data.Data_S3INS_response;
import com.spectratech.lib.tcpip.SSLServerHelper;
import com.spectratech.lib.tcpip.data.Data_SSLServerLocal;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class for api of sp530demo
 * Wrap non ui sp530demo code and embed libpack using transitive technique in JCenter
 */
public class ApiAppMain {
    private static Data_executeCommand m_currentExecuteCommand;
    public static Callback<Object> m_cb_general;
    private static int m_currentExecuteSeqno;

    private static final String TAG = ApiAppMain.class.getSimpleName();

    public static final String SP530API_ACTION_ACL_CONNECTED = "sp530api.bluetooth.device.action.ACL_CONNECTED";
    public static final String SP530API_ACTION_ACL_DISCONNECTED = "sp530api.bluetooth.device.action.ACL_DISCONNECTED";

    public static int TLV_AMOUNT = 0x9F02;

    public static final int NUMBER_TRANSACTION_AMOUNT_BYTES = 6;

    private static ApiAppMain sInst = null;

    static {
        Logger.setEnableLogging(false);
    }

    public static ApiAppMain getInstance() {
        if (sInst == null) {
            sInst = new ApiAppMain();
        }
        return sInst;
    }


    // api call

    /**
     * Set enable/disable logging in console
     * @param aFlag true - enable; false - disable
     */
    public static void setEnableLogging(boolean aFlag) {
        Logger.setEnableLogging(aFlag);
    }

    /**
     * Set wait response time in ms of SP530
     * @param waitReponseTimeInMs wait response time in ms unit
     */
    public static void setWaitResponseTimeInMs(long waitReponseTimeInMs) {
        ApiAppMain inst = ApiAppMain.getInstance();
        inst.mWaitReponseTimeInMs = waitReponseTimeInMs;
        Logger.i(TAG, "setWaitResponseTimeInMs " + inst.mWaitReponseTimeInMs);
    }

    /**
     * Get wait response time in ms of SP530
     * @return wait reponse time in ms
     */
    public static long getWaitResponseTimeInMs() {
        ApiAppMain inst = ApiAppMain.getInstance();
        return inst.mWaitReponseTimeInMs;
    }

    /**
     * Check onCreate function called
     * @return true - have been called; false - have not been called
     */
    public static boolean isOnCreateCalled() {
        ApiAppMain inst = ApiAppMain.getInstance();
        return inst.mFlagOnCreateCalled;
    }

    /**
     * onCreate function; this should be called once at onCreate function of an activity
     * This function is used for initialization
     * @param context context of an activity
     */
    public static void onCreate(Context context) {
        Logger.i(TAG, "onCreate");
        ApiAppMain inst = ApiAppMain.getInstance();

        inst.unregisterBroadcastReceiver();

        inst.m_context = context;

        inst.registerBroadcastReceiver();

        inst.m_SP530BTEventsForActivity.setContext(context);
        inst.m_SP530BTEventsForActivity.loadSavedActiveBluetoothDevice(TAG);

        m_cb_general=new Callback<Object>() {
            @Override
            public Void call() throws Exception {
                //cb_finish_common();
                return null;
            }
        };
    }

    /**
     * onDestroy function; this sould be called once at onDestroy function of an activity
     * This function is used for free resources
     */
    public static void onDestroy() {
        Logger.i(TAG, "onDestroy");

        SP530Manager.freeInstance();
        SP530Communicator.freeInstance();

        // stop ssl server local please if exist
        SSLServerHelper instSSLServerHelper=SSLServerHelper.getInstance();
        instSSLServerHelper.ssl_disconnectAll();

        if (sInst != null) {
            sInst.unregisterBroadcastReceiver();
            if (sInst.m_SP530BTEventsForActivity != null) {
                sInst.m_SP530BTEventsForActivity.onDestroy();
            }
            sInst = null;
        }
    }

    /**
     * Refresh Bluetooth connection
     * Making connection if necessary
     */
    public static void refreshBTConnection() {
        Logger.i(TAG, "refreshBTConnection");
        ApiAppMain inst = ApiAppMain.getInstance();
        inst.validateBTConnections();
    }


    // version

    /**
     * Get version name
     * @return version name
     */
    public static String getVersionName() {
        return com.spectratech.lib.BuildConfig.VERSION_NAME;
    }

    /**
     * Get version code string
     * @return version code string
     */
    public static int getVersionCode() {
        return com.spectratech.lib.BuildConfig.VERSION_CODE;
    }


    // bluetooth

    /**
     * Get connection status of BT between android and SP530
     * @return CONNECTED - connected; DISCONNECTED - disconnected
     */
    public static CONNECTIONSTATUS terminalAndMobileConnectionStatus() {
        // check for calling setcontext function?
        checkForSetContext();

        CONNECTIONSTATUS connectionstatus;

        ApiAppMain inst = ApiAppMain.getInstance();
        synchronized (inst.mLock) {
            if (inst.m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection == BluetoothStatusClass.STATUS_DEVICE_CONNECTION.CONNECTED) {
                connectionstatus = CONNECTIONSTATUS.CONNECTED;
            }
            else {
                connectionstatus = CONNECTIONSTATUS.DISCONNECTED;
            }
        }
        return connectionstatus;
    }

    /**
     * Launch Bluetooth connection activity
     */
    public static void launchBluetoothScreen() {
        // check for calling setcontext function?
        checkForSetContext();

        ApiAppMain inst = ApiAppMain.getInstance();
        Intent intent = new Intent(inst.m_context, ApiAppBluetoothConnectActivity.class);
        inst.m_context.startActivity(intent);
    }

    public static void launchBluetoothScreenWithDeviceName(String name, String address) {
        // check for calling setcontext function?
        checkForSetContext();

        ApiAppMain inst = ApiAppMain.getInstance();
        Intent intent = new Intent(inst.m_context, ApiAppBluetoothConnectActivity.class);

        intent.putExtra("blm_device_name", name);
        intent.putExtra("blm_device_address", address);

        inst.m_context.startActivity(intent);
    }


    // ssl

    /**
     * Refresh local channel SSL parameters/connections
     */
    public static void refreshLocalChannelSSL() {
        Logger.i(TAG, "refreshLocalChannelSSL call");
        // check for calling setcontext function?
        checkForSetContext();

        ApiAppMain inst = ApiAppMain.getInstance();
        Data_SSLServerLocal dataSSLServerLocal = inst.mSSLData.getSSLServerDataObject(inst.m_context);
        inst.sslDisconnectTriggered(inst.mSSLData.mFlagLocalChannelUseSSL, dataSSLServerLocal);
    }

    /**
     * Set local channel uses SSL
     * @param aFlag true - enable; false - disable
     */
    public static void setLocalChannelUseSSL(boolean aFlag) {
        // check for calling setcontext function?
        checkForSetContext();

        ApiAppMain inst = ApiAppMain.getInstance();
        inst.mSSLData.mFlagLocalChannelUseSSL = aFlag;
    }

    /**
     * Set local channel SSL cert using P12 formatted file
     * @param aServerCertFileInP12 file of server cert
     * @param aPassword password of server cert
     * @param aCaCertFile file of ca cert; null for disable; enable otherwise
     * @return true - success; false - fail
     */
    public static boolean setLocalChannelSSLCerts_p12(File aServerCertFileInP12, String aPassword, File aCaCertFile) {
        String aServerCertFormat = "PKCS12";
        return setLocalChannelSSLCerts(aServerCertFileInP12, aPassword, aServerCertFormat, aCaCertFile);
    }

    /**
     * Set local channel SSL cert using BKS formatted file
     * @param aServerCertFileInBks file of server cert
     * @param aPassword password of server cert
     * @param aCaCertFile file of ca cert; null for disable; enable otherwise
     * @return true - success; false - fail
     */
    public static boolean setLocalChannelSSLCerts_bks(File aServerCertFileInBks, String aPassword, File aCaCertFile) {
        String aServerCertFormat = "BKS";
        return setLocalChannelSSLCerts(aServerCertFileInBks, aPassword, aServerCertFormat, aCaCertFile);
    }

    /**
     * Set local channel SSL cert using P12 format in resource raw folder
     * @param aServerCertIdInP12 resource id of server cert
     * @param aPassword password of server cert
     * @param aCaCertId resource id of ca cert; disable - 0; enable otherwise
     * @return true - success; false - fail
     */
    public static boolean setLocalChannelSSLCerts_p12(int aServerCertIdInP12, String aPassword, int aCaCertId) {
        String aServerCertFormat = "PKCS12";
        return setLocalChannelSSLCerts(aServerCertIdInP12, aPassword, aServerCertFormat, aCaCertId);
    }

    /**
     * Set local channel SSL cert using BKS format in resource raw folder
     * @param aServerCertIdInBks resource id of server cert
     * @param aPassword password of server cert
     * @param aCaCertId resource id of ca cert; disable - 0; enable otherwise
     * @return true -success; false - fail
     */
    public static boolean setLocalChannelSSLCerts_bks(int aServerCertIdInBks, String aPassword, int aCaCertId) {
        String aServerCertFormat = "BKS";
        return setLocalChannelSSLCerts(aServerCertIdInBks, aPassword, aServerCertFormat, aCaCertId);
    }


    private static boolean setLocalChannelSSLCerts(File aServerCertFile, String aPassword, String aServerCertFormat, File aCaCertFile) {
        // check for calling setcontext function?
        checkForSetContext();
        ApiAppMain inst = ApiAppMain.getInstance();
        return inst.mSSLData.setLocalChannelSSLCerts(aServerCertFile, aPassword, aServerCertFormat, aCaCertFile);
    }

    private static boolean setLocalChannelSSLCerts(int aServerCertId, String aPassword, String aServerCertFormat, int aCaCertId) {
        // check for calling setcontext function?
        checkForSetContext();
        ApiAppMain inst = ApiAppMain.getInstance();
        Context context = inst.m_context;
        return inst.mSSLData.setLocalChannelSSLCerts(context, aServerCertId, aPassword, aServerCertFormat, aCaCertId);
    }

    private void sslDisconnectTriggered(boolean bLocalChannelSSL, Data_SSLServerLocal dataSSLServerLocal) {
        Logger.i(TAG, "sslDisconnectTriggered");
        SP530_AppMcpCHelper instSP530_AppMcpCHelper=SP530_AppMcpCHelper.getInstance();
        instSP530_AppMcpCHelper.localChannelDisconnectTriggered(bLocalChannelSSL, dataSSLServerLocal);
    }

    /**
     * Transaction for SP530
     * @param aTlvData tlv byte array data
     * @return result byte array data
     */
    public static byte[] getTransactionBuffer(byte[] aTlvData, boolean isFinish) {
        // check for calling setcontext function?
        Log.i("EMV FINISH", "Llegó al final del proceso 1");
        checkForSetContext();
        Log.i("EMV FINISH", "Llegó al final del proceso 2");

        Callback<Object> cb_finish=new Callback<Object>() {
            @Override
            public Object call() {

                Object param = this.getParameter();

                if (param instanceof Data_S3INS_response) {
                    Data_S3INS_response dataS3INSResponse = (Data_S3INS_response) param;
                    FullEmvEnum.TRANSACTIONSTATUS status_trans = dataS3INSResponse.m_statusTrans;
                    switch (status_trans) {
                        case STATUS_FINISH: {
                            byte[] responseBytes=dataS3INSResponse.m_data;
                            ApiAppMain inst = ApiAppMain.getInstance();
                            synchronized (inst.mLock) {
                                inst.mResultBytes = responseBytes;
                            }
                        }
                        break;
                    }
                    Log.i(TAG, "call back - status_trans: "+FullEmvEnum.Status2String(status_trans));
                }
                else {
                    Log.i(TAG, "call back - NOT instanceof Data_S3INS_response");
                }

                // notify thread
                ApiAppMain inst = ApiAppMain.getInstance();
                synchronized (inst.mLock) {
                    inst.mLock.notifyAll();
                }

                return null;
            }
        };

        Log.i("EMV FINISH", "Llegó al final del proceso 3");

        ApiAppMain inst = ApiAppMain.getInstance();

        final SP530Communicator instSP530Communicator=SP530Communicator.getInstance(inst.m_context);

        SP530Manager instSP530Manager=SP530Manager.getInstance(inst.m_context);
        instSP530Manager.cancel();
        instSP530Manager.setLocalChannelUseSSL(inst.mSSLData.mFlagLocalChannelUseSSL);

        // set wait response time in ms
        Log.i(TAG, "Set wait response time " + inst.mWaitReponseTimeInMs);
        instSP530Manager.setWaitResponseTimeInMs(inst.mWaitReponseTimeInMs);

        byte[] transData = parse2TransactionData(aTlvData);

        if(!isFinish) {
            if (transData == null) {
                Log.i(TAG, "transData == null, missing 0x9F02 tag?");
                return null;
            }
        }

        Log.i("EMV FINISH", "Llegó al final del proceso 4");

        synchronized (inst.mLock) {
            inst.mResultBytes = null;
            if(isFinish) {
                Log.i("EMV FINISH", "Llegó al final del proceso");
                //instSP530Manager.cancel();
                //instSP530Manager.start(inst.m_context, instSP530Communicator, (byte)0xFF, null, cb_finish);
                instSP530Manager.start(inst.m_context, instSP530Communicator, ApplicationProtocolConstant.S3INS_SHOW_STAT, null, cb_finish);
                //instSP530Manager.cancel();//reset_mutuAuthInfo(inst.m_context);
            }
            else
                instSP530Manager.start(inst.m_context, instSP530Communicator, ApplicationProtocolConstant.S3INS_FULL_EMV, transData, cb_finish);
            try {
                inst.mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return inst.mResultBytes;
    }

    private static byte[] parse2TransactionData(byte[] aTlvData) {
        TLVHelper instTLVHelper = TLVHelper.getInstance();
        byte[] aAmountbytearray = instTLVHelper.tlvValueData(TLV_AMOUNT, aTlvData);

        // check amount byte array null
        if (aAmountbytearray == null ) {
            Logger.w(TAG, "aAmountbytearray == null");
            return null;
        }

        // check amount byte array length
        if (aAmountbytearray.length != NUMBER_TRANSACTION_AMOUNT_BYTES) {
            Logger.w(TAG, "aAmountbytearray.length "+aAmountbytearray.length+" != NUMBER_TRANSACTION_AMOUNT_BYTES "+NUMBER_TRANSACTION_AMOUNT_BYTES);
            return null;
        }

        // remove TLV_AMOUNT data: 0x9F02|0x06|0xXX 0xXX 0xXX 0xXX 0xXX 0xXX
        byte[] tlvAmountRemovedData = instTLVHelper.removeFirstTlv(TLV_AMOUNT, aTlvData);

        List<Byte> dataList=new ArrayList<Byte>();
        dataList.addAll(Arrays.asList(ArrayUtils.toObject(aAmountbytearray)));
        if (tlvAmountRemovedData != null) {
            dataList.addAll(Arrays.asList(ArrayUtils.toObject(tlvAmountRemovedData)));
        }

        byte[] dataBuf = null;
        if (dataList.size()>0) {
            dataBuf = Bytes.toArray(dataList);
        }
        return dataBuf;
    }

    private static void checkForSetContext() {
        ApiAppMain inst = ApiAppMain.getInstance();
        if (inst.m_context == null) {
            throw new RuntimeException("Context is null, did you remember to call onCreate?");
        }
    }

    /**
     * Variable to store Bluetooth event parameters
     */
    public BTEventsForActivity m_SP530BTEventsForActivity;

    private Object mLock;
    private Context m_context;
    private byte[] mResultBytes;

    // ssl
    private SSLData mSSLData;

    private BroadcastReceiver m_broadcastReceiver;

    private boolean mFlagOnCreateCalled;

    private long mWaitReponseTimeInMs;

    private ApiAppMain() {
        mFlagOnCreateCalled = false;
        mLock = new Object();
        m_context = null;
        mResultBytes = null;
        mWaitReponseTimeInMs = SP530_bt_S3INS_simple.DEFAULT_WAITTIMENORMALINMS;
        mSSLData = new SSLData();

        initBTEventsForActivity();
    }

    private void initBTEventsForActivity() {
        m_SP530BTEventsForActivity = new BTEventsForActivity();
        m_SP530BTEventsForActivity.setKeyForDeviceAddressStoreInSharedPreferences("SP530");
    }

    synchronized void safe_start_comm_SP530Communicator() {
        SP530Manager.freeInstance();
        SP530Communicator.freeInstance();


        // check for calling setcontext function?
        checkForSetContext();

        BluetoothSocketClass btSocketClassInst = m_SP530BTEventsForActivity.getBluetoothSocketClassInstance();
        final SP530Communicator instSP530Communicator=SP530Communicator.getInstance(m_context);
        Callback<Object> cb_success_listening = new Callback<Object>() {
            @Override
            public Object call() throws Exception {
                Logger.i(TAG, "Started SP530Communicator");
                return null;
            }
        };
        Callback<Object> cb_fail_listening = new Callback<Object>() {
            @Override
            public Object call() throws Exception {
                ((Activity)m_context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(m_context)
                                .setTitle("SP530Communicator")
                                .setMessage("Fail to initial SP530Communicator")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // empty
                                    }
                                })
                                .show();
                    }
                });
                return null;
            }
        };
        Data_SSLServerLocal dataSSLServerLocal=null;
        if (mSSLData.mFlagLocalChannelUseSSL) {
            dataSSLServerLocal=mSSLData.getSSLServerDataObject(m_context);
        }
        instSP530Communicator.start(m_context, btSocketClassInst, mSSLData.mFlagLocalChannelUseSSL, dataSSLServerLocal, cb_success_listening, cb_fail_listening);
    }

    private synchronized void validateBTConnections() {
        Logger.i(TAG, "validateBTConnections called");

        // sp530
        if (m_SP530BTEventsForActivity!=null) {
            Callback<Object> cb_connected=new Callback<Object>() {
                @Override
                public Object call() throws Exception {
                    synchronized (mLock) {
                        if (m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection != BluetoothStatusClass.STATUS_DEVICE_CONNECTION.CONNECTED) {
                            m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection = BluetoothStatusClass.STATUS_DEVICE_CONNECTION.CONNECTED;
                            // send broadcast SP530 connected
                            apiAppMainSendBroadcast(SP530API_ACTION_ACL_CONNECTED);
                        }
                    }

                    Object obj=getParameter();
                    if (obj instanceof BluetoothDevice) {
                        final BluetoothDevice btdevice=(BluetoothDevice)obj;
                        String strConnect = "Connect";
                        String strMsg = strConnect + " " + btdevice;
                        Logger.i(TAG, strMsg);
                    }

                    safe_start_comm_SP530Communicator();

                    return null;
                }
            };
            m_SP530BTEventsForActivity.validateBTConnection(cb_connected);
        }
    }

    private void registerBroadcastReceiver() {
        Logger.i(TAG, "registerBroadcastReceiver");
        if (m_context == null) {
            return;
        }
        if (m_broadcastReceiver == null) {
            m_broadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    String action = intent.getAction();

                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        String strTmp = TAG;
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                        switch (state) {
                            case BluetoothAdapter.STATE_OFF: {
                                if (m_SP530BTEventsForActivity != null) {
                                    synchronized (mLock) {
                                        m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection = BluetoothStatusClass.STATUS_DEVICE_CONNECTION.DISCONNECTED;
                                    }
                                }

                                // send broadcast SP530 disconnected
                                apiAppMainSendBroadcast(SP530API_ACTION_ACL_DISCONNECTED);
                            }
                            break;
                            case BluetoothAdapter.STATE_TURNING_OFF: {
                                // empty
                            }
                            break;
                            case BluetoothAdapter.STATE_ON: {
                                // empty
                            }
                            break;
                            case BluetoothAdapter.STATE_TURNING_ON: {
                                // empty
                            }
                            break;
                            default: {
                                // empty
                            }
                            break;
                        }
                    }
                    else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // empty
                    }
                    else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        //Device is now connected
                        if (m_SP530BTEventsForActivity != null) {
                            BluetoothDevice activeDevice = m_SP530BTEventsForActivity.getActiveBluetoothDevice();
                            if ((activeDevice != null) && (activeDevice.equals(device))) {
                                synchronized (mLock) {
                                    m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection = BluetoothStatusClass.STATUS_DEVICE_CONNECTION.CONNECTED;
                                }
                                // send broadcast SP530 connected
                                apiAppMainSendBroadcast(SP530API_ACTION_ACL_CONNECTED);
                            }
                        }
                    }
                    else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        // empty
                    }
                    else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                        // empty
                    }
                    else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        //Device has disconnected
                        if (m_SP530BTEventsForActivity!=null) {
                            BluetoothDevice activeDevice = m_SP530BTEventsForActivity.getActiveBluetoothDevice();
                            if ((activeDevice != null) && (activeDevice.equals(device))) {
                                synchronized (mLock) {
                                    m_SP530BTEventsForActivity.m_btStatus.m_stausDeviceConnection = BluetoothStatusClass.STATUS_DEVICE_CONNECTION.DISCONNECTED;
                                }

                                // send broadcast SP530 connected
                                apiAppMainSendBroadcast(SP530API_ACTION_ACL_DISCONNECTED);

                                Logger.i(TAG, "registerBroadcastReceiver, ACTION_ACL_DISCONNECTED, validateBTConnections");

                                SP530Manager.freeInstance();
                                SP530Communicator.freeInstance();

                                validateBTConnections();
                            }
                        }
                    }
                }
            };

            HandlerThread handlerThread = new HandlerThread("demomain");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            Handler handler = new Handler(looper);

            // Register the BroadcastReceiver
            // Don't forget to unregister during onDestroy
            Activity act = (Activity) m_context;
            IntentFilter filter = new IntentFilter();
            // ACTION_FOUND
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            // ACTION_STATE_CHANGED
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            // ACTION_ACL_CONNECTED
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            // ACTION_ACL_DISCONNECT_REQUESTED
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            // ACTION_ACL_DISCONNECTED
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

            act.registerReceiver(m_broadcastReceiver, filter, null, handler);
        }
    }
    private void unregisterBroadcastReceiver() {
        Logger.i(TAG, "unregisterBroadcastReceiver");
        if (m_context == null) {
            return;
        }
        if (m_broadcastReceiver!=null) {
            Activity act = (Activity) m_context;
            act.unregisterReceiver(m_broadcastReceiver);
            m_broadcastReceiver=null;
        }
    }

    private synchronized void apiAppMainSendBroadcast(String aStrAction) {
        if (m_context == null) {
            Logger.w(TAG, "apiAppMainSendBroadcast, m_context == null");
            return;
        }
        if (aStrAction == null) {
            Logger.w(TAG, "apiAppMainSendBroadcast, aStrAction == null");
            return;
        }
        if (aStrAction.isEmpty()) {
            Logger.w(TAG, "apiAppMainSendBroadcast, aStrAction is empty");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(aStrAction);
        m_context.sendBroadcast(intent);
    }

    /**********************************************************************************************/
    /*                                  FIN DE LA TRANSACCIÓN                                     */
    /**********************************************************************************************/

    public static void sendResetTransactionCommand() {
        Logger.i("ApiAppMain", "sendResetTransactionCommand called");
        setCurrentExecuteCommand(ApplicationProtocolConstant.S3INS_INIT_MODE, null, m_cb_general, false);
        //(byte)0x01
        //setCurrentExecuteCommand((byte)0x40, null, m_cb_general, false);
        startCommand();
    }

    /**
     * Set current executing command
     * @param commandCode command code byte
     * @param dataBuf input data array
     * @param cb_finish callback function for finish
     */
    public static void setCurrentExecuteCommand(byte commandCode, byte[] dataBuf, Callback<Object> cb_finish, boolean bIncludeUIExtraTlv) {
        byte[] commandCodeArray=new byte [1];
        commandCodeArray[0]=commandCode;
        setCurrentExecuteCommand(commandCodeArray, dataBuf, cb_finish, bIncludeUIExtraTlv);
    }
    /**
     * Set current executing command
     * @param commandCodeArray command code byte array
     * @param dataBuf input data array
     * @param cb_finish callback function for finish
     */
    public static void setCurrentExecuteCommand(byte[] commandCodeArray, byte[] dataBuf, Callback<Object> cb_finish, boolean bIncludeUIExtraTlv) {
        m_currentExecuteCommand=new Data_executeCommand();
        m_currentExecuteCommand.m_commandCode=new byte[commandCodeArray.length];
        System.arraycopy(commandCodeArray, 0, m_currentExecuteCommand.m_commandCode, 0, commandCodeArray.length);
        m_currentExecuteCommand.m_dataBuf=dataBuf;
        m_currentExecuteCommand.m_cb_finish=cb_finish;
        m_currentExecuteCommand.m_bIncludeUIExtraTlv=bIncludeUIExtraTlv;
    }

    private static void startCommand() {
        byte commandCode=m_currentExecuteCommand.m_commandCode[0];

        List<Byte> dataList=new ArrayList<Byte>();
        if ( (m_currentExecuteCommand.m_dataBuf!=null)&&(m_currentExecuteCommand.m_dataBuf.length>0) ) {
            dataList.addAll(Arrays.asList(ArrayUtils.toObject(m_currentExecuteCommand.m_dataBuf)));
        }

        // add transaction extra tlv
        /*if (m_currentExecuteCommand.m_bIncludeUIExtraTlv) {
            String strExtraTlv = m_dataSettingS3Trans.m_strTransactionExtraTlv;
            if ((strExtraTlv != null) && (!strExtraTlv.equals(""))) {
                byte[] byteExtraTlv = null;
                try {
                    ByteHexHelper instByteHexHelper = ByteHexHelper.getInstance();
                    byteExtraTlv = instByteHexHelper.hexStringToByteArray(strExtraTlv);
                } catch (Exception ex) {
                    Logger.w(m_className, "ex: " + ex.toString());
                    byteExtraTlv = null;
                }
                if ((byteExtraTlv != null) && (byteExtraTlv.length > 0)) {
                    dataList.addAll(Arrays.asList(ArrayUtils.toObject(byteExtraTlv)));
                }
            }
        }*/

        byte[] dataBuf= Bytes.toArray(dataList);

        Callback<Object> cb_finish=m_currentExecuteCommand.m_cb_finish;

        ApplicationProtocolHelper instApplicationProtocolHelper=ApplicationProtocolHelper.getInstance();

        m_currentExecuteSeqno=instApplicationProtocolHelper.getCurrentSequnceNumber();

        ApiAppMain inst = ApiAppMain.getInstance();

        final SP530Communicator instSP530Communicator=SP530Communicator.getInstance(inst.m_context);

        SP530Manager instSP530Manager=SP530Manager.getInstance(inst.m_context);
        instSP530Manager.cancel();
        instSP530Manager.setLocalChannelUseSSL(inst.mSSLData.mFlagLocalChannelUseSSL);

        // set wait response time in ms
        Log.i(TAG, "Set wait response time " + inst.mWaitReponseTimeInMs);
        instSP530Manager.setWaitResponseTimeInMs(inst.mWaitReponseTimeInMs);

        //m_responseMessage="";

        //reloadSP530Class(commandCode);

        /*switch (m_idx_deviceselected) {
            case 0: {*/
                //m_sp530Class.set_callback_progressText(m_cb_transprogressText);
                //m_sp530Class.start(commandCode, dataBuf, cb_finish);
                instSP530Manager.start(inst.m_context, instSP530Communicator, commandCode, null, m_currentExecuteCommand.m_cb_finish);
            /*}
            break;
            case 1: {
                m_t1000Class.set_callback_progressText(m_cb_transprogressText);
                m_t1000Class.start(commandCode, dataBuf, cb_finish);
            }
            break;
        }*/
    }
}
