package com.blm.qiubopay.modules.registro;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.modules.Fragment_ubicacion;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetDynamicQuestions;
import com.blm.qiubopay.listeners.IRegisterUser;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_NewUserResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnline_Object;
import com.blm.qiubopay.models.questions.QPAY_CampaignRequest;
import com.blm.qiubopay.models.questions.QPAY_Channels_object;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestionsResponse;
import com.blm.qiubopay.modules.campania.Fragment_preguntas;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.home.Fragment_notification;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_registro_1 extends HFragment {

    public static QPAY_NewUser newUser = new QPAY_NewUser();
    public static List<QPAY_Channels_object> channels = new ArrayList();

    private ArrayList<CViewEditText> campos = null;
    private Button btn_confirmar;

    private RadioButton rad_aceptar_tyc;
    private RadioButton rad_aceptar_avi;
    private RadioButton rd_gb_bimboId;

    private boolean accepted;
    private boolean accepted_1;

    private String tycTimestamp;
    private String aviTimestamp;

    private boolean bimboID;

    private AlertDialog alertConfirmation = null;

    public static Fragment_registro_1 newInstance(Object... data) {
        Fragment_registro_1 fragment = new Fragment_registro_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_register_1, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment(){

        newUser.setOperative_type(Globals.OPERATIVE_TYPE_RETAIL);

        CApplication.setAnalytics(CApplication.ACTION.CB_capturar_informacion_personal);

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);
        rad_aceptar_tyc = getView().findViewById(R.id.rad_aceptar_tyc);
        rad_aceptar_avi = getView().findViewById(R.id.rad_aceptar_avi);
        rd_gb_bimboId = getView().findViewById(R.id.rd_gb_bimboId);

        campos = new ArrayList();

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
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

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_correo_electronico))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(70)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_access_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_correo_electronico_confirma))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(70)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_access_22)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_23)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_access_26)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombres))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_proceso_pedidos_09)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_paterno))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_proceso_pedidos_010)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_materno))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_proceso_pedidos_011)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        List<ModelItem> items = new ArrayList();

        for (QPAY_Channels_object chanel : channels)
            items.add(new ModelItem(chanel.getName(), chanel.getId()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_canal))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_register_22)
                .setSpinner(items)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colaborador))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(14)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_register_colaborador)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_bimbo))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(rd_gb_bimboId.isChecked() ? 28 : 30)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.placeholderParameterMerchantBimboId)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        rd_gb_bimboId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bimboID){
                    bimboID = false;
                }else{
                    bimboID = true;
                }
                campos.get(8).setPrefix(bimboID ? "GB" : null);
                campos.get(8).setMaximum(bimboID ? 28 : 30);
                rd_gb_bimboId.setChecked(bimboID);

                validate();

            }
        });

        rad_aceptar_tyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accepted)
                    accepted = false;
                else {
                    accepted = true;
                    newUser.setQpay_accepted_conditions_date(Utils.getTimestamp());
                    newUser.setQpay_accepted_privacy_date(Utils.getTimestamp());
                }

                rad_aceptar_tyc.setChecked(accepted);
                rad_aceptar_avi.setChecked(accepted);

                validate();
            }
        });

        rad_aceptar_avi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accepted_1)
                    accepted_1 = false;
                else {
                    accepted_1 = true;
                    newUser.setQpay_accepted_privacy_date(Utils.getTimestamp());
                }

                rad_aceptar_avi.setChecked(accepted_1);

                validate();
            }
        });

        ImageView img_help = getView().findViewById(R.id.img_help);
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().alert(R.string.text_register_help);
            }
        });

        ImageView img_help_code = getView().findViewById(R.id.img_help_code);
        img_help_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQrAlert();
            }
        });

        TextView btn_terminos = getView().findViewById(R.id.btn_terminos);
        btn_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_browser.newInstance(Globals.URL_TERMS_AND_CONDITIONS));
            }
        });

        TextView btn_aviso = getView().findViewById(R.id.btn_aviso);
        btn_aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_browser.newInstance(Globals.URL_PRIVACY_ADVICE));
            }
        });

        ImageView btn_scaner = getView().findViewById(R.id.btn_scaner);
        btn_scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {

                        ScanActivity.action = new ZBarScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result result) {

                                if(result == null || result.getContents().isEmpty())
                                    return;

                                String resultado = result.getContents().trim().toUpperCase();
                                // 220328 Validaciones bimbo ID

                                bimboID = resultado.contains("GB") ? true : false;
                                rd_gb_bimboId.setChecked(resultado.contains("GB") ? true : false);


                                campos.get(8).setPrefix(rd_gb_bimboId.isChecked() ? "GB" : null);
                                campos.get(8).setMaximum(rd_gb_bimboId.isChecked() ? 28 : 30);

                                resultado = resultado.replaceAll("[^0-9]","").trim();
                                campos.get(8).setText(resultado);

                            }
                        };

                        getContext().startActivity(ScanActivity.class);
                    }
                }, new String[]{Manifest.permission.CAMERA});
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newUser.getQpay_bimbo_id() == null) {

                    //220413 RSB. Solicitud de cambio alerta No cuenta con BimboId
                    getContext().setItemAlert(R.layout.view_alert_modal_inverse);
                    getContext().alert(R.drawable.warning, R.color.mango, R.string.alert_message_register_without_bimbo_id_question, new IAlertButton() {
                        @Override
                        public String onText() {
                            return getContext().getResources().getString(R.string.info_button);
                        }

                        @Override
                        public void onClick() {
                            Fragment_notification.notificationFunction = null;
                            getContext().setFragment(Fragment_notification.newInstance(new Fragment_notification.NotificationData(
                                    R.string.text_alert_access_cb_1,
                                    R.string.text_alert_access_cb_2,
                                    R.drawable.illustrations_faqs_115_x_95,
                                    R.layout.fragment_notification_2
                            )), false);
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return getContext().getResources().getString(R.string.accept_button);
                        }

                        @Override
                        public void onClick() {
                            validateRegister();
                        }
                    });
                    getContext().setItemAlert(R.layout.view_alert_modal);

                } else {
                    validateRegister();
                }

            }
        });

        setDataBimboId();
    }

    public void setDataBimboId() {

        if(SessionApp.getInstance().getDataRecordOnlineResponse() != null) {

            QPAY_DataRecordOnline_Object data = SessionApp.getInstance().getDataRecordOnlineResponse().getQpay_object().get(0);
            campos.get(7).setText(data.getRetailer_id());
            campos.get(3).setText(data.getRetailer_name());

        }

    }

    public void validateRegister() {

        if(newUser.getSocial_token() == null || newUser.getSocial_token() .isEmpty() ) {
            //RSB 211025. OnboardingVAS N3
            if(Tools.isN3Terminal()){
                getContext().setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.REGISTER_VAS_ADDRESS,newUser));
            } else {
                getContext().setFragment(Fragment_registro_5.newInstance(newUser));
            }

            return;
        }

        if(CApplication.getLastLocation() != null) {
            newUser.getQpay_device_info()[0].setQpay_geo_x(String.valueOf(CApplication.getLastLocation().getLongitude()));
            newUser.getQpay_device_info()[0].setQpay_geo_y(String.valueOf(CApplication.getLastLocation().getLatitude()));
        }

        newUser.setQpay_password("");

        register(newUser, new IFunction() {
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



    private void validate(){
        String regexpGB = "[0-9]{1,28}"; // GB + 28 números
        String regexNum = "[0-9]{1,30}"; // 30 números

        if (bimboID){
            if (!campos.get(8).getText().matches(rd_gb_bimboId.isChecked() ? regexpGB : regexNum) ){
                campos.get(8).activeError();
                campos.get(8).setError("Código bimbo invalido",false);

                btn_confirmar.setEnabled(false);
                return;
            }
        }


        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_confirmar.setEnabled(false);
                return;
            }

        if(!rad_aceptar_tyc.isChecked()){
            btn_confirmar.setEnabled(false);
            return;
        }

        if(!rad_aceptar_avi.isChecked()){
            btn_confirmar.setEnabled(false);
            return;
        }

        if(!campos.get(0).getText().equals(campos.get(1).getText())){
            campos.get(1).activeError();
            btn_confirmar.setEnabled(false);
            return;
        }




        setData();

        btn_confirmar.setEnabled(true);
    }

    public void showQrAlert(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_remesas_qr, null);

        Button btn_solicitar = view.findViewById(R.id.btn_aceptar);

        ImageView image_qr = view.findViewById(R.id.image_qr);

        TextView text_message = view.findViewById(R.id.text_message);

        TextView text_title = view.findViewById(R.id.text_title);

        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertConfirmation.dismiss();

            }
        });

        text_message.setText(R.string.text_register_29);

        text_title.setText(R.string.text_register_30);
        image_qr.setImageResource(R.drawable.code_bimbo_id);

        builder.setView(view);
        builder.setCancelable(false);

        alertConfirmation = builder.create();
        alertConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertConfirmation.show();

        builder = null;

    }

    public void setData(){
        newUser = new QPAY_NewUser();

        newUser.setQpay_cellphone(campos.get(2).getText());
        newUser.setQpay_name(campos.get(3).getText().toUpperCase());
        newUser.setQpay_father_surname(campos.get(4).getText().toUpperCase());
        newUser.setQpay_mother_surname(campos.get(5).getText().toUpperCase());

        newUser.setQpay_mail(campos.get(1).getText());
        newUser.setQpay_sepomex("1");
        newUser.setClient_channel_id(campos.get(6).getTag());

        newUser.setQpay_promoter_id(campos.get(7).getText());
        String bimbo_id = "";
        bimbo_id = bimboID ? "GB" + campos.get(8).getText() : campos.get(8).getText();
        newUser.setQpay_bimbo_id(bimbo_id.isEmpty() || bimbo_id.equals("GB") ? null : bimbo_id);

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
                    } else {
                        QPAY_NewUserResponse registerUserResponse = (QPAY_NewUserResponse) result;

                        if (registerUserResponse.getQpay_response().equals("true")) {

                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_exitoso);

                            dynamicQuestions(Globals.REGISTER_QUESTIONS, function);

                        } else {
                            getContext().loading(false);
                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_no_exitoso);
                            getContext().alert(registerUserResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().alert(R.string.general_error_catch);
        }

        registerUserService.registerUser(data);
    }

    public void dynamicQuestions(String tag, IFunction function){

        try {

            QPAY_CampaignRequest qpay_seed = new QPAY_CampaignRequest();
            qpay_seed.setType(tag);
            qpay_seed.setClient_channel_id(newUser.getClient_channel_id());
            qpay_seed.setQpay_mail(newUser.getQpay_mail());

            IGetDynamicQuestions questions = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

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

}

