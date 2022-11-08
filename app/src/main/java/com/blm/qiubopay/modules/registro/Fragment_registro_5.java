package com.blm.qiubopay.modules.registro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.listeners.IGetDynamicQuestions;
import com.blm.qiubopay.listeners.IMultiDevice;
import com.blm.qiubopay.listeners.IRegisterUser;
import com.blm.qiubopay.listeners.ISwapT1000;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_NewUserResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.questions.QPAY_CampaignRequest;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestionsResponse;
import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.models.swap.SwapData;
import com.blm.qiubopay.modules.campania.Fragment_preguntas;
import com.blm.qiubopay.modules.home.Fragment_notification;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_registro_5 extends HFragment {

    private QPAY_NewUser data;

    private ArrayList<CViewEditText> campos = null;
    private Button btn_enviar;
    private boolean pass;

    public static Fragment_registro_5 newInstance(Object... data) {
        Fragment_registro_5 fragment = new Fragment_registro_5();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_5", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_registro_5"), QPAY_NewUser.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_register_2, container, false),R.drawable.background_splash_header_3);
    }

    public void initFragment(){

        //20200803 RSB. Imp. Geolocalizar al crear el registro
        ((HLocActivity)getContext()).setForceLocation(true);
        ((HLocActivity)getContext()).obtainLocation(null);

        campos = new ArrayList();

        CApplication.setAnalytics(CApplication.ACTION.CB_capturar_contrasenia);

        btn_enviar = getView().findViewById(R.id.btn_enviar);

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_pass))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT_PASS)
                .setHint(R.string.text_access_4)
                .setAlert(R.string.text_input_required)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(!campos.get(1).getText().isEmpty() && !text.equals(campos.get(1).getText())){
                            campos.get(1).setText("");
                            campos.get(1).setShowPass(false);
                        }
                        validate.onChange(text);

                    }
                }));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_pass_confirm))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT_PASS)
                .setHint(R.string.text_access_error_6)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_19)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(validate));

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_enviar.setEnabled(false);

                data.setQpay_password(Tools.encodeSHA256(campos.get(0).getText()));

                //20200803 RSB. Imp. Geolocalizar al crear el registro
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ((HLocActivity)getContext()).obtainLocation(null);
                    return;
                }

                ((HLocActivity)getContext()).setForceLocation(false);

                if(CApplication.getLastLocation() != null) {
                    data.getQpay_device_info()[0].setQpay_geo_x(String.valueOf(CApplication.getLastLocation().getLongitude()));
                    data.getQpay_device_info()[0].setQpay_geo_y(String.valueOf(CApplication.getLastLocation().getLatitude()));
                }

                if(Tools.isN3Terminal()){
                    //Se sobre escribe el número de serie.
                    data.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));
                    //IccId
                    data.getQpay_device_info()[0].setQpay_icc_id(Tools.getIccId(getContext()));
                }

                //VALIDA SI ES SWAP T1000
                if(AppPreferences.isSwapT1000()){

                    SwapData swapData = AppPreferences.getSwapT1000();
                    data.setQpay_folio(swapData.getFolio());
                    data.setQpay_promotor(swapData.getPromotor());

                    String jsonData = new Gson().toJson(data);
                    Log.d("SWAP",jsonData);

                    finishMigration(data, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            AppPreferences.setSwapT1000(null);

                            getContext().setDefaultBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            getContext().setFinishBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            Fragment_notification.notificationFunction = null;
                            getContext().setFragment(Fragment_notification.newInstance(new Fragment_notification.NotificationData(
                                    getContext().getResources().getString(R.string.text_swapt1000_finish_title),
                                    getContext().getResources().getString(R.string.text_swapt1000_finish_subtitle),
                                    R.drawable.check,
                                    R.layout.fragment_notification
                            )), true);

                        }
                    });

                    //20210111 RSB. MultiDevice T1000. Se valida si es asociación de terminal
                } else if (AppPreferences.isLinkT1000()) {

                    QPAY_LinkT1000Request linkRequest = AppPreferences.getLinkT1000();
                    data.setQpay_blm_id(linkRequest.getQpay_blm_id());
                    data.setQpay_folio(linkRequest.getQpay_folio());

                    String jsonData = new Gson().toJson(data);
                    Log.d("LINK",jsonData);

                    finishLinkT1000(data, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            AppPreferences.setLinkT1000(null);

                            getContext().setDefaultBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            getContext().setFinishBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            Fragment_notification.notificationFunction = null;
                            getContext().setFragment(Fragment_notification.newInstance(new Fragment_notification.NotificationData(
                                    getContext().getResources().getString(R.string.text_swapt1000_finish_title),
                                    getContext().getResources().getString(R.string.text_multi_t1000_finish_subtitle),
                                    R.drawable.check,
                                    R.layout.fragment_notification
                            )), true);
                        }
                    });

                } else {

                    register(data, new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            getContext().setDefaultBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            getContext().setFinishBack(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

							Fragment_notification.notificationFunction = null;
                            getContext().setFragment(Fragment_notification.newInstance(new Fragment_notification.NotificationData(
                                    getContext().getResources().getString(R.string.text_alert_access_8),
                                    getContext().getResources().getString(R.string.text_alert_access_9),
                                    R.drawable.check,
                                    R.layout.fragment_notification
                            )), true);

                        }
                    });
                }
            }
        });

    }

    public void validate(){

        btn_enviar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()) {
                return;
            }

        if(campos.size() > 0 && !campos.get(0).getText().equals(campos.get(1).getText())){
            campos.get(1).activeError();
            return;
        }

        btn_enviar.setEnabled(true);
    }

    public void register(QPAY_NewUser data, final IFunction function){

        getContext().loading(true);

        data.setQpay_fcmId(AppPreferences.getFCM());

        IRegisterUser registerUserService = null;
        try {
            registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);
                        btn_enviar.setEnabled(true);
                    } else {
                        QPAY_NewUserResponse registerUserResponse = (QPAY_NewUserResponse) result;

                        if (registerUserResponse.getQpay_response().equals("true")) {

                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_exitoso);

                            dynamicQuestions(Globals.REGISTER_QUESTIONS, function);

                        } else {
                            getContext().loading(false);
                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_no_exitoso);
                            getContext().alert(registerUserResponse.getQpay_description());
                            btn_enviar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_enviar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_enviar.setEnabled(true);
        }

        registerUserService.registerUser(data);
    }

    public void dynamicQuestions(String tag, IFunction function){

        try {

            QPAY_CampaignRequest qpay_seed = new QPAY_CampaignRequest();
            qpay_seed.setClient_channel_id(Fragment_registro_1.newUser.getClient_channel_id());
            qpay_seed.setQpay_mail(Fragment_registro_1.newUser.getQpay_mail());
            qpay_seed.setType(tag);

            IGetDynamicQuestions questions = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);
                    btn_enviar.setEnabled(true);

                    String gson = new Gson().toJson(result);

                    Log.e("response", "" + gson);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        getContext().loading(false);

                        QPAY_DynamicQuestionsResponse response_txn = new Gson().fromJson(gson, QPAY_DynamicQuestionsResponse.class);

                        if("000".equals(response_txn.getQpay_code())) {
                            Fragment_preguntas.setContinue(function);
                            Fragment_preguntas.setData(response_txn.getQpay_object()[0]);

                            if(response_txn.getQpay_object()[0] == null || response_txn.getQpay_object()[0].getQuestion() == null || response_txn.getQpay_object()[0].getQuestion().length == 0) {
                                  function.execute();
                            } else
                                getContext().setFragment(Fragment_preguntas.newInstance(), true);

                        } else {
                            function.execute();
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(qpay_seed));

            questions.getDynamicQuestions(qpay_seed);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void finishMigration(QPAY_NewUser data, IFunction function) {

        getContext().loading(true);

        ISwapT1000 finishSwapListener = null;
        try {
            finishSwapListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                        btn_enviar.setEnabled(true);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            getContext().alert(response.getQpay_description());
                            btn_enviar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_enviar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_enviar.setEnabled(true);
        }

        finishSwapListener.finishSwap(data);

    }

    public void finishLinkT1000(QPAY_NewUser data, IFunction function) {

        getContext().loading(true);

        IMultiDevice finishLinkListener = null;
        try {
            finishLinkListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                        btn_enviar.setEnabled(true);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            getContext().alert(response.getQpay_description());
                            btn_enviar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_enviar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_enviar.setEnabled(true);
        }

        finishLinkListener.finishLinkT1000(data);

    }
}

