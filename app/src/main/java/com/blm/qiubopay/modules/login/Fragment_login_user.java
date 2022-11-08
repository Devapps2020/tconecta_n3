package com.blm.qiubopay.modules.login;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.MigrateHelper;
import com.blm.qiubopay.models.questions.QPAY_Channels_object;
import com.blm.qiubopay.modules.Fragment_soporte_bimbo_1;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.modules.swap.Fragment_menu_swap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.components.CLoginOption;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppImages;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetChannels;
import com.blm.qiubopay.listeners.IGetTipsAdvertising;
import com.blm.qiubopay.listeners.ILogin;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LastLogin;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_PrivilegesResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertisingResponse;
import com.blm.qiubopay.models.questions.QPAY_Channels;
import com.blm.qiubopay.models.questions.QPAY_ChannelsResponse;
import com.blm.qiubopay.modules.campania.Fragment_tips;
import com.blm.qiubopay.modules.registro.Fragment_registro_1;
import com.blm.qiubopay.modules.registro.Fragment_registro_option;
import com.blm.qiubopay.modules.registro.Fragment_registro_pin;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_login_user extends HFragment {

    private static final String TAG = "login_1";

    private QPAY_Login data;
    private ArrayList<CViewEditText> campos = null;
    private Button btn_continuar;
    private Button btn_registrarse = null;
    private TextView btn_recuperar_pass = null;
    private Switch switch_remember_user = null;
    private LinearLayout view_menu_register;
    private ImageView ivLogo;

    private boolean isMailChange = false;

    //210805 RSB. Migracion. Autologin, remover al terminar migracion
    private boolean isAutologin = false;

    public static Fragment_login_user newInstance(Object... data) {
        Fragment_login_user fragment = new Fragment_login_user();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_login_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_login_1"), QPAY_Login.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(AppPreferences.getUserProfile() != null) {
            if(AppPreferences.isRememberUser()) {
                return super.onCreated(inflater.inflate(R.layout.fragment_login_record, container, false), R.drawable.background_splash_header_1);
            }
        }

        return super.onCreated(inflater.inflate(R.layout.fragment_login_user, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        //210805 RSB. Migracion. Autologin, remover al terminar migracion
        MigrateHelper migrateHelper = new MigrateHelper(getContext());
        if(migrateHelper.validateFileExist()){
            QPAY_UserCredentials credentials = migrateHelper.readBackup();
            if(credentials!=null){
                data = new QPAY_Login(getContext());
                data.setQpay_mail(credentials.getUser());
                data.setQpay_password(credentials.getPwd());
                data.setTconectaMigration("1");
                setDataInfoDeviceFirstTime();

                isAutologin = true;

                login(data);
            }
        }

        CViewMenuTop.create(getView()).showTitle(Globals.VERSION).onClickQuestionLeft(() -> {
            getContext().setFragment(Fragment_soporte_bimbo_1.newInstance());

        });

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {
                //20201125 RSB. Forzar localización para N3
                if(Tools.isN3Terminal()){
                    ((HLocActivity)getContext()).setForceLocation(true);
                    ((HLocActivity)getContext()).obtainLocation(null);
                }
            }
        }, new String[]{Manifest.permission.READ_PHONE_STATE});

        AppImages.clear(getContext());

        campos = new ArrayList();

        ivLogo = getView().findViewById(R.id.ivLogo);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        btn_recuperar_pass = getView().findViewById(R.id.btn_recuperar_pass);
        btn_recuperar_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_recuperar_cuenta.newInstance());
            }
        });

        if(AppPreferences.getUserProfile() != null) {
            if(AppPreferences.isRememberUser()) {
                initFragmentRecover();
                return;
            }
        }
        /*ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/

        configRegisterOption();

        CLoginOption.create(getContext()).onGmail(getView().findViewById(R.id.btn_gmail), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO login) {

                switch_remember_user.setChecked(false);

                data = new QPAY_Login(getContext());
                data.setQpay_mail(login.getEmail());
                data.setSocial_token(login.getId());
                data.setLogin_type(login.getType());
                data.setQpay_password("");

                setDataInfoDeviceFirstTime();

                login(data);

            }
        }).onFacebook(getView().findViewById(R.id.btn_facebook), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO login) {

                switch_remember_user.setChecked(false);

                data = new QPAY_Login(getContext());
                data.setQpay_mail(login.getEmail());
                data.setSocial_token(login.getId());
                data.setLogin_type(login.getType());
                data.setQpay_password("");

                setDataInfoDeviceFirstTime();

                login(data);

            }
        });

        switch_remember_user = getView().findViewById(R.id.switch_remember_user);


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
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_email))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(50)
                .setPredictive(false)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_access_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if (isMailChange) {
                            campos.get(0).deactiveError();
                            isMailChange = false;
                        }
                        validate();
                    }
                }));



        btn_registrarse = getView().findViewById(R.id.btn_registrarse);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChannels(false);
            }
        });

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = new QPAY_Login(getContext());
                data.setQpay_mail( campos.get(1).getText());
                data.setQpay_password( campos.get(0).getText());
                //RSB. Se solicita a pesar de no ser autologin de migracion beta pues puede ser para usarios que comparten terminal
                data.setTconectaMigration("1");

                setDataInfoDeviceFirstTime();

                if (CApplication.getLastLocation() != null) {
                    data.getQpay_device_info()[0].setQpay_geo_x(String.valueOf(CApplication.getLastLocation().getLongitude()));
                    data.getQpay_device_info()[0].setQpay_geo_y(String.valueOf(CApplication.getLastLocation().getLatitude()));
                }

                login(data);

            }
        });

        if(AppPreferences.getUserProfile() != null) {
            if(AppPreferences.isRememberUser()) {
                campos.get(0).setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
            }
        }

        //RSB. Al entrar a soporte deben empezar de 0 swap o ligue de t1000
        if(AppPreferences.isSwapT1000()){
            AppPreferences.setSwapT1000(null);
        }
        if(AppPreferences.isLinkT1000()){
            AppPreferences.setLinkT1000(null);
        }

        final TextView btnSoporte = getView().findViewById(R.id.btn_soporte);
        btnSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_menu_swap.newInstance());
            }
        });

    }

    private void setDataInfoDeviceFirstTime(){
        data.getQpay_device_info()[0].setQpay_dongle_sn(null);
        data.getQpay_device_info()[0].setQpay_dongle_mac(null);
        data.getQpay_device_info()[0].setQpay_dongle_firmware_version(null);
        data.getQpay_device_info()[0].setQpay_dongle_model(null);
    }

    public void initFragmentRecover() {

        TextView label_not_me = getView().findViewById(R.id.label_not_me);
        TextView label_name = getView().findViewById(R.id.label_name);

        String name = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name()!=null?AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name():"";
        String lastname = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname()!=null?AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname():"";

        label_name.setText(name + "\n" + lastname);

        label_not_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppPreferences.setRememberUser(false);

                getContext().startActivity(LoginActivity.class, true);
            }
        });

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
                        validate();
                    }
                }));

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = new QPAY_Login(getContext());
                data.setQpay_mail( AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
                data.setQpay_password(campos.get(0).getText());

                data.getQpay_device_info()[0].setQpay_dongle_sn(null);
                data.getQpay_device_info()[0].setQpay_dongle_mac(null);
                data.getQpay_device_info()[0].setQpay_dongle_firmware_version(null);
                data.getQpay_device_info()[0].setQpay_dongle_model(null);

                login(data);

            }
        });

        switch_remember_user = getView().findViewById(R.id.switch_remember_user);
        switch_remember_user.setChecked(true);

    }

    public void configRegisterOption() {

        view_menu_register = getView().findViewById(R.id.view_menu_register);
        ImageView img_close = getView().findViewById(R.id.img_close);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_menu_register.setVisibility(View.GONE);
            }
        });

        Button btn_bimbo = getView().findViewById(R.id.btn_register_bimbo);

        btn_bimbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view_menu_register.setVisibility(View.GONE);

                Fragment_registro_1.newUser = new QPAY_NewUser();
                getChannels(true);
            }
        });

        CLoginOption.create(getContext()).onGmail(getView().findViewById(R.id.btn_register_gmail), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO data) {

                view_menu_register.setVisibility(View.GONE);

                Fragment_registro_1.newUser = new QPAY_NewUser();
                Fragment_registro_1.newUser.setSocial_token(data.getId());
                Fragment_registro_1.newUser.setQpay_mail(data.getEmail());
                Fragment_registro_1.newUser.setQpay_name(data.getFirstName());
                Fragment_registro_1.newUser.setQpay_father_surname(data.getLastName());
                Fragment_registro_1.newUser.setQpay_mother_surname("");
                Fragment_registro_1.newUser.setLogin_type(data.getType());

                getChannels(false);
            }
        }).onFacebook(getView().findViewById(R.id.btn_register_facebook), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO data) {

                view_menu_register.setVisibility(View.GONE);

                Fragment_registro_1.newUser = new QPAY_NewUser();
                Fragment_registro_1.newUser.setSocial_token(data.getId());
                Fragment_registro_1.newUser.setQpay_mail(data.getEmail());
                Fragment_registro_1.newUser.setQpay_name(data.getFirstName());
                Fragment_registro_1.newUser.setQpay_father_surname(data.getLastName());
                Fragment_registro_1.newUser.setQpay_mother_surname("");
                Fragment_registro_1.newUser.setLogin_type(data.getType());

                getChannels(false);
            }
        });

    }

    public void login(QPAY_Login data) {

        AppPreferences.setCloseSessionFlag("0",null);

        login(data, new IFunction() {
            @Override
            public void execute(Object[] dataUser) {

                QPAY_UserProfile user = (QPAY_UserProfile) dataUser[0];

                //2021-12-14 RSB. Se comenta temporalmente para que al llegar a home se lancé el home de telemetría
                AppPreferences.setDateLastLogin(new LastLogin(Tools.getTodayDate()));
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

                            getContext().setFragment(Fragment_registro_pin.newInstance(), true);

                        }
                    });

                } else {

                    getContext().setFragment(Fragment_registro_pin.newInstance(), true);

                }

            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    public void login(final QPAY_Login data, final IFunction function){

        getContext().loading(true);

        try {

            final QPAY_UserCredentials credentials = new QPAY_UserCredentials();
            credentials.setUser(data.getQpay_mail());
            credentials.setPwd(data.getQpay_password());
            String fcmId = AppPreferences.getFCM();
            credentials.setFcmId(fcmId);
            data.setQpay_fcmId(fcmId);

            data.setQpay_password(Tools.encodeSHA256(data.getQpay_password()));

            if(Tools.isN3Terminal()){
                //Se sobre escribe el número de serie.
                data.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));
                //IccId
                data.getQpay_device_info()[0].setQpay_icc_id(Tools.getIccId(getContext()));
            }

            ILogin registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);
                    }else{

                        String json = new Gson().toJson(result);

                        QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);

                        AppPreferences.setUserCredentials(credentials);

                        if(userProfile.getQpay_response().equals("true")) {

                            CApplication.setAnalytics(CApplication.ACTION.CB_login_exitoso);

                            //220216 RSB. Fix Recordar usuario
                            //userProfile.getQpay_object()[0].setQpay_remember_user(switch_remember_user != null ? switch_remember_user.isChecked() : false);
                            AppPreferences.setRememberUser(switch_remember_user != null ? switch_remember_user.isChecked() : false);

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute(userProfile);

                        } else {

                            getContext().loading(false);

                            CApplication.setAnalytics(CApplication.ACTION.CB_login_no_exitoso);

                            Log.d(TAG,"LoginERROR " + userProfile.getQpay_code() + " - " + userProfile.getQpay_description());

                            if(userProfile.getQpay_code().trim().equals("004")) {

                                getContext().setFragment(Fragment_recuperar_pass.newInstance(data));

                            } else if (userProfile.getQpay_code().trim().equals("015")) {

                                String code = userProfile.getQpay_description();
                                String phone = userProfile.getQpay_object()[0].getQpay_cellphone();
                                String spartanMigration = userProfile.getQpay_object()[0].getQpay_isSpartanMigration();
                                data.setQpay_password(credentials.getPwd());

                                if(spartanMigration!=null && !spartanMigration.isEmpty()) {
                                    getContext().setFragment(Fragment_login_otp.newInstance(data, code, phone, spartanMigration));
                                }else{
                                    getContext().setFragment(Fragment_login_otp.newInstance(data, code, phone));
                                }

                            } else if (userProfile.getQpay_code().trim().equals("134")) {

                                campos.get(0).setError(userProfile.getQpay_description(),false);
                                campos.get(0).activeError();
                                isMailChange = true;

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

    public void getChannels(boolean cliente){

        getContext().loading(true);

        try {

            QPAY_Channels channels = new QPAY_Channels();

            IGetChannels channel = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result);

                    if (result instanceof ErrorResponse) {
                        //getContext().alert(R.string.general_error);
                        Fragment_registro_1.channels = getCatalogChannels().getQpay_object().get(0);
                        getContext().setFragment(Fragment_registro_1.newInstance());
                    } else {

                        QPAY_ChannelsResponse response = new Gson().fromJson(gson, QPAY_ChannelsResponse.class);

                        if("000".equals(response.getQpay_code())) {
                            Fragment_registro_1.channels = response.getQpay_object().get(0);

                            getContext().setFragment(Fragment_registro_1.newInstance());

                        } else {
                            getContext().alert(response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error);
                    Fragment_registro_1.channels = getCatalogChannels().getQpay_object().get(0);
                    getContext().setFragment(Fragment_registro_1.newInstance());
                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(channels));

            channel.getChannels(channels);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            //getContext().alert(R.string.general_error_catch);
            Fragment_registro_1.channels = getCatalogChannels().getQpay_object().get(0);
            getContext().setFragment(Fragment_registro_1.newInstance());
        }

    }

    //Se crea este método para que no haya dependencia directa de RS para onboarding
    private QPAY_ChannelsResponse getCatalogChannels(){

        QPAY_ChannelsResponse response = new QPAY_ChannelsResponse();

        List<List<QPAY_Channels_object>> catalogs = new ArrayList<List<QPAY_Channels_object>>();
        List<QPAY_Channels_object> catalog = new ArrayList<QPAY_Channels_object>();

        QPAY_Channels_object object = new QPAY_Channels_object();
        object.setId("1");
        object.setName("Autofiliacion");
        catalog.add(object);
        QPAY_Channels_object object1 = new QPAY_Channels_object();
        object1.setId("2");
        object1.setName("Repartidor Bimbo");
        catalog.add(object1);

        catalogs.add(catalog);
        response.setQpay_object(catalogs);

        return response;
    }

}

