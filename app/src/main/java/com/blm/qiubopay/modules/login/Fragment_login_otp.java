package com.blm.qiubopay.modules.login;


import android.content.Context;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.modules.Fragment_ubicacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.ILogin;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_PrivilegesResponse;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.modules.registro.Fragment_registro_pin;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;

import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_login_otp extends HFragment {

    private QPAY_Login dataUser;
    private String dataCode;
    private String dataPhone;
    private String dataSpartanMigration;

    private boolean isAppMigration;

    private CViewEditText editCode;

    private String clearpwd;

    public static Fragment_login_otp newInstance(Object... data) {
        Fragment_login_otp fragment = new Fragment_login_otp();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_login_otp_1_1", new Gson().toJson(data[0]));

        if(data.length > 1)
            args.putString("Fragment_login_otp_1_2", data[1].toString());

        if(data.length > 2)
            args.putString("Fragment_login_otp_1_3", data[2].toString());

        if(data.length > 3)
            args.putString("Fragment_login_otp_1_4", data[3].toString());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            dataUser = new Gson().fromJson(getArguments().getString("Fragment_login_otp_1_1"), QPAY_Login.class);
            dataCode = getArguments().getString("Fragment_login_otp_1_2");
            dataPhone = getArguments().getString("Fragment_login_otp_1_3");
            dataSpartanMigration = getArguments().getString("Fragment_login_otp_1_4",null);

            if(dataSpartanMigration !=null && dataSpartanMigration.compareTo("1")==0){
                isAppMigration = true;
            }
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_login_otp, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        Log.d("dataCode",dataCode);

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView textOTP = getView().findViewById(R.id.text_otp);

        final Button btnContinuar = (Button) getView().findViewById(R.id.btn_continuar);

        String otpMessage = "";
        if (dataPhone!=null && !dataPhone.isEmpty()) {
            if (dataPhone.length()>4){
                dataPhone = dataPhone.substring(dataPhone.length()-4);
                otpMessage = getString(R.string.text_login_otp_1_desc).replace("**last**",dataPhone);
            } else {
                otpMessage = getString(R.string.text_login_otp_1_desc_sn);
            }
        } else {
            otpMessage = getString(R.string.text_login_otp_1_desc_sn);
        }

        textOTP.setText(otpMessage);

        editCode = CViewEditText.create(getView().findViewById(R.id.edit_1))
                .setRequired(true)
                .setMinimum(8)
                .setMaximum(8)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_access_20)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_21)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(editCode!=null && editCode.isValid()){
                            btnContinuar.setEnabled(true);
                        } else {
                            btnContinuar.setEnabled(false);
                        }

                    }
                });

        //210719 RSB. Pendings. QA Fix login otp doble encode al presionar nuevamente el botón
        clearpwd = dataUser.getQpay_password();
        dataUser.setQpay_password(Tools.encodeSHA256(dataUser.getQpay_password()));

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()){
                    createDevice(dataUser);
                }
            }
        });

    }

    /**
     * Validacion
     * @return
     */
    private boolean validate() {

        String sCode = editCode.getText().trim();

        if(sCode.compareTo(dataCode) != 0) {
            editCode.activeError();
            return false;
        }

        return true;
    }

    /**
     * Create Device
     * @param data
     */
    public void createDevice(final QPAY_Login data){

        getContext().loading(true);

        try {

            IMultiUserListener registerDeviceListenter = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    }else{

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if(response.getQpay_response().equals("true")) {

                            login(data, new IFunction() {
                                @Override
                                public void execute(Object[] dataUser) {

                                    QPAY_UserProfile user = (QPAY_UserProfile) dataUser[0];

                                    //2021-12-14 RSB. Se comenta temporalmente para que al llegar a home se lancé el home de telemetría
                                    //AppPreferences.setDateLastLogin(new LastLogin(Tools.getTodayDate()));

                                    String rol = user.getQpay_object()[0].getQpay_user_type();
                                    rol = (rol != null ? rol.trim() : "");

                                    if(rol.compareTo(Globals.ROL_CAJERO)==0) {

                                        QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
                                        linkedUser.setQpay_seed(user.getQpay_object()[0].getQpay_seed());
                                        getPrivileges(linkedUser, new IFunction() {
                                            @Override
                                            public void execute(Object[] dataPrivileges) {
                                                QPAY_PrivilegesResponse privilegesResponse = (QPAY_PrivilegesResponse) dataPrivileges[0];
                                                AppPreferences.setUserPrivileges(privilegesResponse.getQpay_object()[0]);

                                                //220506 s8.1 RSB. App2Terminal Migration
                                                if(isAppMigration){
                                                    getContext().setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.APP2TERMINAL_MIGRATION_ADDRESS), true);
                                                } else {
                                                    if (AppPreferences.isPinRegistered()) {
                                                        getContext().startActivity(MenuActivity.class, true);
                                                    } else {
                                                        getContext().setFragment(Fragment_registro_pin.newInstance(), true);
                                                    }
                                                }


                                            }
                                        });

                                    } else {

                                        //220506 s8.1 RSB. App2Terminal Migration
                                        if(isAppMigration){
                                            getContext().setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.APP2TERMINAL_MIGRATION_ADDRESS), true);
                                        } else {
                                            if (AppPreferences.isPinRegistered()) {
                                                getContext().startActivity(MenuActivity.class, true);
                                            } else {
                                                getContext().setFragment(Fragment_registro_pin.newInstance(), true);
                                            }
                                        }

                                    }

                                }
                            });

                        } else {
                            getContext().alert(response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            registerDeviceListenter.createUserDevice(data);

        }catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    /**
     * Login
     * @param data
     * @param function
     */
    public void login(final QPAY_Login data, final IFunction function){

        getContext().loading(true);

        try {

            final QPAY_UserCredentials credentials = new QPAY_UserCredentials();
            credentials.setUser(data.getQpay_mail());
            credentials.setPwd(clearpwd);
            String fcmId = AppPreferences.getFCM();
            credentials.setFcmId(fcmId);
            data.setQpay_fcmId(fcmId);

            //Se codifica en sha al inicio del fragmento para no duplicar codificación en cada click
            data.setQpay_password(data.getQpay_password());

            if(Tools.isN3Terminal()){
                //Se sobre escribe el número de serie.
                data.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));

                //IccId
                try {
                    TelephonyManager telMngr = ((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE));
                    String iccid = telMngr.getSimSerialNumber();

                    if (iccid==null){
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                            SubscriptionManager sm = SubscriptionManager.from(getContext());
                            List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
                            SubscriptionInfo si = sis.get(0);
                            iccid = si.getIccId();
                        } else {
                            data.getQpay_device_info()[0].setQpay_icc_id("NA");
                        }
                    }

                    data.getQpay_device_info()[0].setQpay_icc_id(iccid);

                } catch (SecurityException e) {
                    data.getQpay_device_info()[0].setQpay_icc_id("ND");
                } catch (Exception e ) {
                    data.getQpay_device_info()[0].setQpay_icc_id("NA");
                }
            }

            ILogin registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);
                    }else{

                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        AppPreferences.setUserCredentials(credentials);

                        if(userProfile.getQpay_response().equals("true")) {

                            CApplication.setAnalytics(CApplication.ACTION.CB_login_exitoso);

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute(userProfile);

                        } else {

                            getContext().loading(false);

                            CApplication.setAnalytics(CApplication.ACTION.CB_login_no_exitoso);

                            if(userProfile.getQpay_code().trim().equals("004")) {
                                getContext().setFragment(Fragment_recuperar_pass.newInstance(data));
                            } else if (userProfile.getQpay_code().trim().equals("015")) {

                                String code = userProfile.getQpay_description();
                                String phone = userProfile.getQpay_object()[0].getQpay_cellphone();
                                data.setQpay_password(credentials.getPwd());

                                getContext().setFragment(Fragment_login_otp.newInstance(data, code, phone));

                            } else {
                                getContext().alert(userProfile.getQpay_description());
                            }

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            registerUserService.login(data);

        }catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    /**
     * Metodo para obtener los privilegios en caso de ser usuario de tipo operador/cajero
     * @param linkedUser
     * @param function
     */
    private void getPrivileges(final QPAY_LinkedUser linkedUser, final IFunction function) {

        try {

            IMultiUserListener userPrivilegesListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_PrivilegesResponse response = gson.fromJson(json,QPAY_PrivilegesResponse.class);

                        if(response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute(response);

                        } else {
                            getContext().loading(false);
                            getContext().alert(response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            userPrivilegesListener.getUserPrivileges(linkedUser);

        }catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

}
