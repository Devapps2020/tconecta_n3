package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IRegisterFinancialInformation;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecord_Object;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_registro_financiero_1 extends HFragment {
    private final String TAG = "reg_financiero_1";

    private View view;
    private MenuActivity context;
    private String nombre_negocio;
    private QPAY_FinancialInformation data = SessionApp.getInstance().getFinancialInformation();
    private ArrayList<CViewEditText> campos;
    private ArrayList<RadioButton> options;
    private Button btn_continuar;
    private String gender;

    final private String GENERO_MASCULINO = "Masculino";
    final private String GENERO_FEMENINO = "Femenino";

    public static Fragment_registro_financiero_1 newInstance(Object... data) {
        Fragment_registro_financiero_1 fragment = new Fragment_registro_financiero_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_1", data[0].toString());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            nombre_negocio = getArguments().getString("Fragment_registro_financiero_1");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_registro_financiero_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        CApplication.setAnalytics(CApplication.ACTION.CB_REGISTRO_FINANCIERO_inician);

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

        campos = new ArrayList();

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_identificacion))
                .setRequired(true)
                .setMinimum(13)
                .setMaximum(13)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_financiero_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_fecha_nacimiento))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setDatePicker(true)
                .setHint(R.string.text_registro_financiero_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_rfc))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(13)
                .setType(CViewEditText.TYPE.RFC)
                .setHint(R.string.text_registro_financiero_16)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_curp))
                .setRequired(true)
                .setMinimum(18)
                .setMaximum(18)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_financiero_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombres))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_registro_financiero_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_paterno))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_registro_financiero_13)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_materno))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint(R.string.text_registro_financiero_14)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        options = new ArrayList();

        options.add((RadioButton) view.findViewById(R.id.rad_genero_h));
        options.add((RadioButton) view.findViewById(R.id.rad_genero_m));

        btn_continuar = view.findViewById(R.id.btn_continuar);

        // Listeners para elementos en pantalla

        options.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = GENERO_MASCULINO;
                options.get(1).setChecked(false);
                validate();
            }
        });

        options.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.get(0).setChecked(false);
                gender = GENERO_FEMENINO;
                validate();
            }
        });

        ImageView img_help = getView().findViewById(R.id.img_help);
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.alert("Información personal");
            }
        });

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Elementos del perfil
                data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                data.setQpay_mail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
                //Elementos de pantalla
                data.setQpay_name(campos.get(4).getText().toUpperCase());
                data.setQpay_father_surname(campos.get(5).getText().toUpperCase());
                data.setQpay_mother_surname(campos.get(6).getText().toUpperCase());
                data.setQpay_rfc(campos.get(2).getText().toUpperCase());
                data.setQpay_curp(campos.get(3).getText().toUpperCase());
                data.setQpay_gender(gender);
                String date[] = campos.get(1).getText().split("/");
                data.setQpay_birth_date(date[2] + "-" + date[1] + "-" + date[0]);
                data.setQpay_identification(campos.get(0).getText());

                updateUserInformation(buildUpdateUser(), new IFunction() {
                    @Override
                    public void execute(Object[] data1) {
                        registerFinancialInformation(data);
                    }
                });

            }
        });

        setDataFC();

        setViewData();

    }

    /**
     *
     */
    private void setViewData() {

        String id = data.getQpay_identification();
        id = (id!=null && !id.isEmpty() ? id : "");
        campos.get(0).setText(id);

        String birthdate = data.getQpay_birth_date();
        birthdate = (birthdate!=null && !birthdate.isEmpty() ? birthdate : "");
        campos.get(1).setText(birthdate);

        String rfc = data.getQpay_rfc();
        rfc = (rfc!=null && !rfc.isEmpty() ? rfc : "");
        campos.get(2).setText(rfc);

        String curp = data.getQpay_curp();
        curp = (curp!=null && !curp.isEmpty() ? curp : "");
        campos.get(3).setText(curp);

        String name = data.getQpay_name();
        name = (name!=null && !name.isEmpty() ? name : "");

        String father_surname = data.getQpay_father_surname();
        father_surname = (father_surname!=null && !father_surname.isEmpty() ? father_surname : "");

        String mother_surname = data.getQpay_mother_surname();
        mother_surname = (mother_surname!=null && !mother_surname.isEmpty() ? mother_surname : "");

        campos.get(4).setText(name);
        campos.get(5).setText(father_surname);
        campos.get(6).setText(mother_surname);

        String sGender = data.getQpay_gender();
        sGender = sGender!=null && !sGender.isEmpty() ? sGender : "";

        if(sGender.compareTo(GENERO_MASCULINO)==0){
            options.get(0).setChecked(true);
            gender = GENERO_MASCULINO;
        } else if (sGender.compareTo(GENERO_FEMENINO)==0) {
            options.get(1).setChecked(true);
            gender = GENERO_FEMENINO;
        }

        //Validar Nombre
        boolean isSameName = true;
        name = name.trim().toUpperCase();
        if(name.compareTo(AppPreferences.getUserProfile().getQpay_object()[0]
                .getQpay_name().trim().toUpperCase())!=0){
            isSameName = false;
        }
        father_surname = father_surname.trim().toUpperCase();
        if(father_surname.compareTo(AppPreferences.getUserProfile().getQpay_object()[0]
                .getQpay_father_surname().trim().toUpperCase())!=0){
            isSameName = false;
        }
        mother_surname = mother_surname.trim().toUpperCase();
        if(mother_surname.compareTo(AppPreferences.getUserProfile().getQpay_object()[0]
                .getQpay_mother_surname().trim().toUpperCase())!=0){
            isSameName = false;
        }
        if(!isSameName) {
            String merchant = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name()
                    + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname()
                    + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mother_surname();
            context.alert("Verifica tu información, el nombre registrado en tu cuenta es: " + merchant);
        }

        validate();

    }

    private void validate() {

        btn_continuar.setEnabled(false);

        for(int i=0; i<campos.size(); i++) {
            if (!campos.get(i).isValid()) {
                return;
            }
        }

        if(!options.get(0).isChecked() && !options.get(1).isChecked())
            return;

        btn_continuar.setEnabled(true);
    }


    private QPAY_NewUser buildUpdateUser() {

        QPAY_NewUser updateUser = new QPAY_NewUser();

        updateUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        //Datos extraidos de pantalla de captura de domicilio
        updateUser.setQpay_merchant_name(nombre_negocio.toUpperCase().replaceAll("\\s{2,}"," ").trim());
        updateUser.setQpay_merchant_street(data.getQpay_street());
        updateUser.setQpay_merchant_internal_number(data.getQpay_internal_number());
        updateUser.setQpay_merchant_external_number(data.getQpay_external_number());
        StateInfo state = getStateInfo(data.getQpay_state());
        updateUser.setQpay_merchant_state(state.getState_code());
        updateUser.setQpay_merchant_postal_code(data.getQpay_postal_code());
        updateUser.setQpay_merchant_suburb(data.getQpay_suburb());
        updateUser.setQpay_merchant_municipality(data.getQpay_municipality());
        updateUser.setQpay_birth_date(data.getQpay_birth_date());
        updateUser.setQpay_gender(data.getQpay_gender());
        updateUser.setQpay_primary_agency(null);

        return updateUser;

    }

    /**
     * Llama servicio para completar o actualizar el registro 1-B
     * @param data
     * @param function
     */
    private void updateUserInformation(QPAY_NewUser data, final IFunction function){

        //String json = new GsonBuilder().create().toJson(data);
        //Log.d(TAG,"Enviando a QTC: " + json );

        context.loading(true);

        IUpdateUserInformation registerUserService = null;
        try {
            registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        } else {

                            context.validaSesion(userProfile.getQpay_code(), userProfile.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        registerUserService.updateUserInformation(data);
    }


    /**
     * Llama servicio de registro financiero
     * @param financialInformation
     */
    private void registerFinancialInformation(QPAY_FinancialInformation financialInformation){

        //String json = new GsonBuilder().create().toJson(financialInformation);
        //Log.d(TAG,"Enviando a QTC: " + json );

        context.loading(true);

        IRegisterFinancialInformation registerFinancialInformation = null;
        try {

            String a = new Gson().toJson(financialInformation);
            AppPreferences.setImagePerfil(a);

            registerFinancialInformation = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Log.i(TAG,new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if (userProfile.getQpay_response().equals("true")) {
                            AppPreferences.setUserProfile(userProfile);
                            SessionApp.getInstance().setFinancialInformation(null);
                            if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                                context.alert(R.string.alert_message_success_register_all_information_mit, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        context.initHome();

                                    }
                                });

                            else
                                context.alert(R.string.alert_message_success_register_all_information, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        context.initHome();

                                    }
                                });
                        } else {
                            context.validaSesion(userProfile.getQpay_code(), userProfile.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        registerFinancialInformation.registerFinancialInformation(financialInformation);


    }

    /**
     * Retorna la descripcion en base al id del Estado
     * @param codeOrName
     * @return
     */
    private StateInfo getStateInfo(String codeOrName) {

        try {
            Integer code = Integer.parseInt(codeOrName.trim());
            codeOrName = String.format("%02d",code);
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }

        codeOrName = (codeOrName==null?"":codeOrName.trim().toUpperCase());

        ArrayList<StateInfo> stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        for (StateInfo state:stateList){
            if(state.getState_code().compareTo(codeOrName)==0){
                return state;
            }
            if(state.getState_name().trim().toUpperCase().compareTo(codeOrName)==0){
                return state;
            }
        }

        return new StateInfo("","");
    }

    public void setDataFC(){

        if(SessionApp.getInstance().getAllDataRecordResponse() != null) {

            QPAY_AllDataRecord_Object data = SessionApp.getInstance().getAllDataRecordResponse().getQpay_object().get(0);

            if(data.getUser() != null) {

                campos.get(0).setText(data.getFcIdentification().getId_identificacion());
                campos.get(1).setText(data.getUser().getQpay_birth_date());
                campos.get(2).setText(data.getAccount().getQpay_rfc());
                campos.get(3).setText(data.getAccount().getQpay_curp());
                campos.get(4).setText(data.getUser().getQpay_name());
                campos.get(5).setText(data.getUser().getQpay_father_surname());
                campos.get(6).setText(data.getUser().getQpay_mother_surname());

            }

        }
    }

}

