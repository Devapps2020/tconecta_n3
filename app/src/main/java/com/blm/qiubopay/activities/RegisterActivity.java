package com.blm.qiubopay.activities;

import android.os.Bundle;

import com.blm.qiubopay.helpers.HLocActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.ICreateRegister;
import com.blm.qiubopay.listeners.ICreateUserFcIdentification;
import com.blm.qiubopay.listeners.ICreateUserPersonalData;
import com.blm.qiubopay.listeners.IGetAllDataRecord;
import com.blm.qiubopay.listeners.IGetDataRecordOnline;
import com.blm.qiubopay.listeners.IGetUserDataRecord;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecord;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecordResponse;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnline;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnlineResponse;
import com.blm.qiubopay.models.proceedings.QPAY_UserDataRecord;
import com.blm.qiubopay.models.proceedings.QPAY_UserDataRecordResponse;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcIdentification;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcPersonalData;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class RegisterActivity extends HLocActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        getContext().setHostFragmentId(R.id.nav_host_fragment);

        CApplication.setAnalytics(CApplication.ACTION.CB_crear_cuenta);

    }

    public static void getUserDataRecord(HActivity context, IFunction<QPAY_UserDataRecordResponse> function) {

        QPAY_UserDataRecord data = new QPAY_UserDataRecord();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        context.loading(true);

        try {

            IGetUserDataRecord service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_UserDataRecordResponse response = gson.fromJson(json,QPAY_UserDataRecordResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        SessionApp.getInstance().setUserDataRecord(response);

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.getUserDataRecord(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    public static void getAllDataRecord(HActivity context, IFunction<QPAY_AllDataRecordResponse> function) {

        SessionApp.getInstance().setAllDataRecordResponse(null);

        if (true) {
            function.execute(null);
            return;
        }

        QPAY_AllDataRecord data = new QPAY_AllDataRecord();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());



        context.loading(true);

        try {

            IGetAllDataRecord service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_AllDataRecordResponse response = gson.fromJson(json,QPAY_AllDataRecordResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        SessionApp.getInstance().setAllDataRecordResponse(response);

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.getAllDataRecord(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    public static void getDataRecordOnline(HActivity context, QPAY_DataRecordOnline data, IFunction<QPAY_DataRecordOnlineResponse> function) {

        SessionApp.getInstance().setDataRecordOnlineResponse(null);

        context.loading(true);

        try {

            IGetDataRecordOnline service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_DataRecordOnlineResponse response = gson.fromJson(json, QPAY_DataRecordOnlineResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        if(response.getQpay_response().equals("true")) {

                            SessionApp.getInstance().setDataRecordOnlineResponse(response);

                            if(function != null)
                                function.execute(response);

                        } else {
                            context.alert("No se encontraron resultados", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Continuar";
                                }

                                @Override
                                public void onClick() {
                                    if (function != null)
                                        function.execute();
                                }
                            }, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Cancelar";
                                }

                                @Override
                                public void onClick() {
                                    context.backFragment();
                                }
                            });
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.getDataRecordOnline(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    public static void createUserFcIdentification(HActivity context, QPAY_UserFcIdentification data, IFunction<QPAY_BaseResponse> function) {

        context.loading(true);

        try {

            ICreateUserFcIdentification service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_BaseResponse response = gson.fromJson(json,QPAY_BaseResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.createUserFcIdentification(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    public static void createUserPersonalData(HActivity context, QPAY_UserFcPersonalData data, IFunction<QPAY_BaseResponse> function) {

        context.loading(true);

        try {

            ICreateUserPersonalData service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_BaseResponse response = gson.fromJson(json,QPAY_BaseResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.createUserPersonalData(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    public static void createRegister(HActivity context, QPAY_Register data, IFunction<QPAY_BaseResponse> function) {

        context.loading(true);

        try {

            ICreateRegister service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_BaseResponse response = gson.fromJson(json,QPAY_BaseResponse.class);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        if(function != null)
                            function.execute(response);


                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            service.createRegister(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

}
