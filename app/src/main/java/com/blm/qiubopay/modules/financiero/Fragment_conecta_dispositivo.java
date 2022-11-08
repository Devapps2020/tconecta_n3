package com.blm.qiubopay.modules.financiero;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.SdkProxy;
import com.nexgo.oaf.api.communication.Communication;
import com.nexgo.oaf.api.communication.OnDeviceConnectListener;
import com.nexgo.oaf.api.communication.OnDeviceScannerListener;
import com.nexgo.oaf.api.communication.ScanFailEnum;
import com.spectratech.lib.BluetoothHelper;
import com.spectratech.lib.Logger;
import com.spectratech.lib.bluetooth.BluetoothManagementInterface;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IUpdateDeviceData;
import com.blm.qiubopay.models.BL_Device;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.K206.DeviceAdapter;
import com.blm.qiubopay.models.QPAY_DeviceUpdate;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.utils.WSHelper;

import org.scf4a.Event;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import pl.droidsonroids.gif.GifImageView;

public class Fragment_conecta_dispositivo extends HFragment implements IMenuContext, BluetoothManagementInterface {

    private final String m_className = "Fragment_conectar_dispositivo_1";

    private Object data;

    public static Communication communication = null;
    private Event.ConnectType mConnectType = null;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceAdapter deviceAdapter;

    private BL_Device BLDevice;
    private ListView list_montos;
    private GifImageView img_cargando;

    private BluetoothDevice BTDevice;

    //Objetos SP530
    private BluetoothHelper btHelperInst;
    private BluetoothDevicePairThread m_devicePairThreadInst;
    private static final long MAX_WAIT_TIME_PAIRING_INMS = (60 * 1000);

    private OnDeviceScannerListener onDeviceScannerListener = new OnDeviceScannerListener() {
        @Override
        public void onScannerResult(BluetoothDevice devInfo) {

            if(devInfo != null)
                if(devInfo.getName() != null && !devInfo.getName().contains("Unknown") && (devInfo.getName().contains("XGD") || devInfo.getName().contains("SP530"))){
                    deviceAdapter.addDevice(devInfo);
                    deviceAdapter.notifyDataSetChanged();
                }
        }

        @Override
        public void onScanFailed(ScanFailEnum scanFailEnum) {

            String errMsg = "";
            switch (scanFailEnum) {
                case ALREADY_STARTED:
                    errMsg = getString(R.string.already_started);
                    break;
                case APPLICATION_REGISTRATION_FAILED:
                    errMsg = getString(R.string.application_registeration_failed);
                    break;
                case INTERNAL_ERROR:
                    errMsg = getString(R.string.internal_error);
                    break;
                case FEATURE_UNSUPPORTED:
                    errMsg = getString(R.string.feature_unsupport);
                    break;
                case OUT_OF_HARDWARE_RESOURCES:
                    errMsg = getString(R.string.out_of_hardware_resources);
                    break;
                default:
                    break;
            }

            getContext().alert(errMsg);
        }
    };

    private OnDeviceConnectListener onDeviceConnectListener = new OnDeviceConnectListener() {
        @Override
        public void onDeviceConnected() {

            getContextMenu().communication = communication;

            boolean isConnected = communication.getConnectionStatus();

            getContextMenu().loading(false);

            if(isConnected){

                if(!AppPreferences.isDeviceRegistered()) {
                    AppPreferences.setDevice(BLDevice);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            updateData(BLDevice);
                        }
                    };
                    runnable.run();

                    communication.stopScanner();

                    getContext().alert("Su lector se ha vinculado\ncon éxito.", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().device_action.execute();
                        }
                    });

                }

            }else{

                getContextMenu().alert("No se ha conectado con el dispositivo");

            }

        }

        @Override
        public void onDeviceDisConnected() {

            if(!getContextMenu().isConnected) {

                getContextMenu().loading(false);

                getContext().alert("Se ha desconectado el dispositivo", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        getContextMenu().disConnect();
                        getContextMenu().device_action = null;
                        getContextMenu().initHome();
                    }
                });
            }

        }
    };

    public static Fragment_conecta_dispositivo newInstance(Object... data) {
        Fragment_conecta_dispositivo fragment = new Fragment_conecta_dispositivo();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_conectar_dispositivo_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btHelperInst = BluetoothHelper.getInstance();

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_conectar_dispositivo_1"), QPAY_NewUser.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_conectar_dispositivo_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        if(communication == null) {
            try {
                communication = SdkProxy.getCommunication();
                mConnectType = Event.ConnectType.SPP;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            communication.open(mConnectType, getActivity());//Se abre la comunicación con el SDK de Nexgo.
        }

        list_montos = getView().findViewById(R.id.list_dispositivos);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        deviceAdapter = new DeviceAdapter(getContext());

        list_montos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                getContextMenu().loadingConectando(true, "Conectando con lector...", new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        BLDevice = new BL_Device();
                        BLDevice.setMacAddress(deviceAdapter.getDevice(position).getAddress());
                        BLDevice.setName(deviceAdapter.getDevice(position).getName());
                        //ParcelUuid[] uuids = deviceAdapter.getDevice(position).getUuids();
                        //device.setSerie(deviceAdapter.getDevice(position).getUuids()[0].toString());

                        if(BLDevice.getName().contains("SP530")) {
                            try {
                                loadDevicePairThreadIntance(deviceAdapter.getDevice(position));
                                //btHelperInst.pairDevice(deviceAdapter.getDevice(position));
                            }catch (Exception e){

                            }
                        }
                        else
                            communication.startConnect(BLDevice.getMacAddress(), onDeviceConnectListener);

                    }
                });

            }
        });

        img_cargando = getView().findViewById(R.id.img_cargando);

        Button buscar_dispositivos = getView().findViewById(R.id.buscar_dispositivos);
        buscar_dispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mBluetoothAdapter.isEnabled()) {

                    getContext().alert("El Bluetooth de su dispositivo se encuentra apagado.", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Activar";
                        }

                        @Override
                        public void onClick() {

                            getContext().setOnActivityResult(new IActivityResult() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    checkLocationForSDK();
                                }
                            });
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, 1);

                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }

                        @Override
                        public void onClick() {

                        }
                    });

                    return;
                }

                checkLocationForSDK();

            }
        });

        buscar_dispositivos.performClick();

        //getContextMenu().isConnected = true;

        /*if(null != AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_12() && AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_12().equals("4")) {
            context.showAlert(R.string.text_migration_6);
        }*/

    }

    //RSB 20200228. Improvements. Localizar dispositivos bluetooth, versiones android 10 requieren encender el sensor de Ubicación
    /**
     * Verifica si se debe encender el sensor de localizacion
     */
    public void checkLocationForSDK(){
        dispositivos();
    }

    public void dispositivos(){

        if (null != communication) {

            img_cargando.setVisibility(View.VISIBLE);

            list_montos.setAdapter(deviceAdapter);

            deviceAdapter.clear();
            deviceAdapter.notifyDataSetChanged();
            communication.startScanner(onDeviceScannerListener);
        }

    }

    private void loadDevicePairThreadIntance(BluetoothDevice btDevice) {
        if (btDevice == null) {
            Logger.e(m_className, "reloadDevicePairThreadIntance, btDevice is NULL");
            return;
        }

        /*if (m_devicePairThreadInst != null) {
            if (!m_devicePairThreadInst.isFinished()) {
                String strPleaseWait = ResourcesHelper.getBaseContextString(m_context, R.string.prompt_please_wait_message);
                ToastMessageByHandler(strPleaseWait);
                return;
            }
            m_devicePairThreadInst = null;
        }*/

        //this.showProgressDialogAndDismissInTimeSlot(30 * 1000);

        m_devicePairThreadInst = new BluetoothDevicePairThread(btDevice);//, countBTEvent);
        m_devicePairThreadInst.start();
        //countBTEvent++;
    }

    private void updateData(final BL_Device data) {

        try {

            QPAY_DeviceUpdate deviceUpdate = new QPAY_DeviceUpdate();
            String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();
            deviceUpdate.setQpay_seed(seed);
            deviceUpdate.getQpay_device_info()[0].setQpay_dongle_sn(data.getName());
            deviceUpdate.getQpay_device_info()[0].setQpay_dongle_mac(data.getMacAddress());

            IUpdateDeviceData iUpdateDeviceData = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_BaseResponse baseResponse = gson.fromJson(json, QPAY_BaseResponse.class);

                    if (result instanceof ErrorResponse) {
                        Log.d("ConectarDispositivo",((ErrorResponse) result).getMessage());
                    }else{

                        if(baseResponse.getQpay_response().equals("true")) {

                            Log.d("ConectarDispositivo","Success. " + baseResponse.getQpay_description());
                            BL_Device myDevice = AppPreferences.getDevice();
                            myDevice.setQtcRegistered(true);
                            AppPreferences.setDevice(myDevice);

                        }else{
                            Log.d("ConectarDispositivo","Failed." + baseResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    //context.onLoading(false);
                    Log.d("ConectarDispositivo",getString(R.string.general_error));
                    //context.showAlert(R.string.general_error);
                }
            }, getContext());

            iUpdateDeviceData.doUpdateDeviceData(deviceUpdate);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void userActionBTConnectTrigger() {
        Log.d("","userActionBTConnectTrigger");
    }

    @Override
    public void userActionBTDisconnectTrigger() {
        Log.d("","userActionBTDisconnectTrigger");
    }

    @Override
    public boolean isActiveBluetoothDeviceConnected() {
        Log.d("","isActiveBluetoothDeviceConnected");
        return false;
    }

    @Override
    public BluetoothDevice getActiveBluetoothDevice() {
        Log.d("","getActiveBluetoothDevice");
        return null;
    }

    @Override
    public void setActiveBluetoothDevice(BluetoothDevice device) {

        Log.d("","setActiveBluetoothDevice");

        if(!AppPreferences.isDeviceRegistered()) {

            AppPreferences.setDevice(BLDevice);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateData(BLDevice);
                }
            };

            runnable.run();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    communication.stopScanner();

                    getContext().alert("Su lector se ha vinculado con éxito.", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }
                        @Override
                        public void onClick() {
                            m_devicePairThreadInst.setFinishFlag();
                            getContext().backFragment();
                        }
                    });

                }
            });
        }

    }

    @Override
    public void unsetActiveBluetoothDevice(BluetoothDevice device) {
        Log.d("","unsetActiveBluetoothDevice");
    }

    @Override
    public void manageConnectedSocket(BluetoothSocket socket) {
        Log.d("","manageConnectedSocket");
    }

    @Override
    public void safeFreeBTSocket() {
        Log.d("","safeFreeBTSocket");
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

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

            //if (m_btDevice2Pair.getBondState() == BluetoothDevice.BOND_BONDED) {
            //DismissProgressDialogByHandler();
            //String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.device_hasbeenalready_paired);
            //ToastMessageByHandler(strMsg);
            //m_bFinish = true;
            //return;
            //}

            BluetoothHelper btHelperInst = BluetoothHelper.getInstance();

            //btHelperInst.discoverBluetoothDevices_cancel();

            //boolean bUpdateListViews = false;
            if (m_btDevice2Pair.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    btHelperInst.pairDevice(m_btDevice2Pair);
                    //bUpdateListViews = true;
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

            //if (m_btDevice2Pair.getBondState() != BluetoothDevice.BOND_BONDED) {
            //DismissProgressDialogByHandler();
            //String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.no_device_hasbeen_paired);
            //Logger.w(m_className, strMsg);
            //ToastMessageByHandler(strMsg);
            //m_bFinish = true;
            //return;
            //}

            /*if (bUpdateListViews) {
                updateListViews();
            }*/

            // disconnect current
            safeFreeBTSocket();
            userActionBTDisconnectTrigger();

            setActiveBluetoothDevice(m_btDevice2Pair);

            //DismissProgressDialogByHandler();
            //String strMsg = ResourcesHelper.getBaseContextString(m_context, R.string.device_hasbeen_paired) + ": " + m_btDevice2Pair.getAddress();
            //ToastMessageByHandler(strMsg);
            m_bFinish = true;

            /*if (m_bResponseEndActivtyWithResult) {
                m_handler.sendEmptyMessage(MSG_HANDLER_ENDACTIVITY_WITH_RESULT_OK);
            }*/
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

    }

}

