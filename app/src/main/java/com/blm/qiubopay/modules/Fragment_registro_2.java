package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.profile.QPAY_Profile;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_registro_2 extends HFragment {

    private View view;
    //private HActivity context;
    private MenuActivity context;
    private QPAY_NewUser data;

    private ArrayList<CViewEditText> campos;
    private ArrayList<RadioButton> options;
    private Button btn_confirmar;

    private String gender;
    private Collection<SepomexInformation> sepomexSearchResult;
    private DataBaseAccessHelper dataBaseAccessHelper;
    private ArrayList<StateInfo> stateList;

    private Boolean isDongleBuy;

    public static Fragment_registro_2 newInstance(Object... data) {
        Fragment_registro_2 fragment = new Fragment_registro_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_2", new Gson().toJson(data[0]));

        if(data.length > 1)
            args.putBoolean("Fragment_registro_2_1", (Boolean) data[1]);

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

        if (getArguments() != null) {
            data = new Gson().fromJson(getArguments().getString("Fragment_registro_2"), QPAY_NewUser.class);
            isDongleBuy = getArguments().getBoolean("Fragment_registro_2_1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_registro_2_v2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CApplication.setAnalytics(CApplication.ACTION.CB_capturar_informacion_negocio);

        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        options = new ArrayList();
        campos = new ArrayList();

        CViewMenuTop.create(view)
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

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_fecha_nacimiento))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setDatePicker(true)
                .setHint(R.string.text_registro_1)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_nombre_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_5)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_calle_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_6)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_numero_exterior))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        campos.add(CViewEditText.create(view.findViewById(R.id.edit_numero_interior))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_8)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_codigo_postal))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(5)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(campos.get(5).getText().length() == 5)
                            sepomex(campos.get(5).getText());

                        validate.onChange(text);
                    }
                }));


        campos.add(CViewEditText.create(view.findViewById(R.id.edit_estado))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_municipio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_colonia))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        options.add((RadioButton) view.findViewById(R.id.rad_genero_h));
        options.add((RadioButton) view.findViewById(R.id.rad_genero_m));

        options.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Masculino";
                validate();
            }
        });

        options.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Femenino";
                validate();
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = new QPAY_NewUser();

                data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

                //RSB 20200108. Cambio para dejar un solo espacio entre texto, ya no salio en la 1.4.8 saldrá en 1.4.9

                String date[] = campos.get(0).getText().split("/");
                data.setQpay_birth_date(date[2] + "-" + date[1] + "-" + date[0]);

                data.setQpay_merchant_name(campos.get(1).getText().toUpperCase().replaceAll("\\s{2,}"," ").trim());
                data.setQpay_merchant_street(campos.get(2).getText().toUpperCase());
                data.setQpay_merchant_internal_number(campos.get(3).getText());
                data.setQpay_merchant_external_number(campos.get(4).getText());
                data.setQpay_merchant_postal_code(campos.get(5).getText());
                data.setQpay_merchant_state(Integer.parseInt(campos.get(6).getTag()) + "");
                data.setQpay_merchant_suburb(campos.get(7).getText().toUpperCase());
                data.setQpay_merchant_municipality(campos.get(8).getText().toUpperCase());

                data.setQpay_primary_agency(null);
                data.setQpay_gender(gender);

                register(data, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Actualización de información exitosa", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {

                                if(isDongleBuy){
                                    context.comprarDongle();
                                } else {
                                    context.initHome();
                                }

                            }
                        });
                    }
                });
            }
        });

        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(context);

        getStates();

    }

    public void setEstados(){

        List<ModelItem> estados = new ArrayList();

        for(int i=0; i<stateList.size(); i++)
            estados.add(new ModelItem(stateList.get(i).getState_name(), stateList.get(i).getState_code()));

        campos.get(6).setSpinner(estados);
    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_confirmar.setEnabled(false);
                return;
            }

        int select = 0;
        for(int i=0; i<options.size(); i++)
            if(options.get(i).isChecked())
                select++;

        if(select < 1)
             return;

        btn_confirmar.setEnabled(true);
    }

    private ArrayList<StateInfo> getStates() {

        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        setEstados();

        return stateList;
    }

    public void sepomex(String codigo){

        dataBaseAccessHelper.open();
        sepomexSearchResult = dataBaseAccessHelper.findPostalCode(codigo);
        dataBaseAccessHelper.close();

        if(sepomexSearchResult != null && sepomexSearchResult.size() > 0){

            List<ModelItem> poblados = new ArrayList();

            for(int i=0; i<sepomexSearchResult.size(); i++) {
                poblados.add(new ModelItem(Tools.getOnlyNumbersAndLetters(((SepomexInformation)sepomexSearchResult.toArray()[i]).getD_asenta().replace(")","").replace("(",""))));
            }

            campos.get(8).setSpinner(poblados);

            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name())){

                    campos.get(6).setText(stateList.get(i).getState_name());
                    campos.get(6).setTag(stateList.get(i).getState_code());

                    campos.get(7).setText(Tools.getOnlyNumbersAndLetters(info.getD_mnpio()));
                    campos.get(7).setTag(info.getC_tipo_asenta());

                    campos.get(8).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    campos.get(8).setTag(info.getC_mnpio());

                    return;
                }

            }
        } else {
            campos.get(8).removeSpinner();
        }

    }

    public void register(QPAY_NewUser data, final IFunction function){

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

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_Profile.QPAY_ProfileExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {

                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_exitoso);

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        } else {

                            CApplication.setAnalytics(CApplication.ACTION.CB_registro_no_exitoso);

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
            context.alert(R.string.general_error_catch);
        }

        registerUserService.updateUserInformation(data);
    }

}

