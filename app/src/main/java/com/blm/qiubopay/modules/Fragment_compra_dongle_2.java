package com.blm.qiubopay.modules;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IDonglePurchase;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.dongle.QPAY_DongleBuyResponse;
import com.blm.qiubopay.models.profile.QPAY_Profile;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class  Fragment_compra_dongle_2 extends HFragment {

    final private String TAG = "compra_dongle_2";

    private View view;
    private MenuActivity context;

    private QPAY_CashCollectionResponse data;

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private HEditText edit_calle;
    private HEditText edit_no_ext;
    private HEditText edit_cp;

    private EditSpinner estados;
    private EditSpinner poblacion;

    private Collection<SepomexInformation> sepomexSearchResult;
    private DataBaseAccessHelper dataBaseAccessHelper;
    private ArrayList<StateInfo> stateList;

    private boolean setAddress1st = false;

    final private int CALLE_MAX_SIZE = 30;
    final private int NO_EXT_MAZ_SIZE = 6;
    final private int CP_MAX_SIZE = 5;


    public static Fragment_compra_dongle_2 newInstance(Object... data) {
        Fragment_compra_dongle_2 fragment = new Fragment_compra_dongle_2();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_dongle_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);*/
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
            data = new Gson().fromJson(getArguments().getString("Fragment_compra_dongle_2"), QPAY_CashCollectionResponse.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_dongle_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        setView(view);

        campos = new ArrayList();

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

        btn_continuar = view.findViewById(R.id.btn_continuar);

        //Inicializar los spinner y la BD
        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(context);
        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        // Inicializar elementos de pantalla

        //RSB 20191230. Cambio solicitado, se cambian las longitudes acorde a como estan en Fragment_Registro_2
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_calle_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(CALLE_MAX_SIZE)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_6)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_exterior))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(NO_EXT_MAZ_SIZE)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_interior))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_8)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 2
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_codigo_postal))
                .setRequired(true)
                .setMinimum(CP_MAX_SIZE)
                .setMaximum(CP_MAX_SIZE)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(campos.get(3).getText().length() == 5)
                            sepomex(campos.get(3).getText());

                        validate.onChange(text);
                    }
                }));

        //Campo 4
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_estado))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(25)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        //Campo 5

        //Campo 6
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_municipio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colonia))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


               // Listeners para elementos de pantalla

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continuar.setClickable(false);
                if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration() && isSameAdress()){
                    //La dirección es igual a la del perfil
                    comprarDongle(true);
                } else {
                    //La dirección del perfil cambia

                    QPAY_NewUser updateUser = new QPAY_NewUser();

                    updateUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                    //Datos de pantalla
                    updateUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
                    updateUser.setQpay_merchant_internal_number(campos.get(2).getText());
                    updateUser.setQpay_merchant_external_number(campos.get(1).getText());
                    updateUser.setQpay_merchant_postal_code(campos.get(3).getText());
                    StateInfo state = getStateInfo(campos.get(4).getText());
                    updateUser.setQpay_merchant_state(Integer.parseInt(state.getState_code())+"");
                    updateUser.setQpay_merchant_suburb(campos.get(6).getText().toUpperCase());
                    updateUser.setQpay_merchant_municipality(campos.get(5).getText().toUpperCase());
                    //Datos default sin opcion en pantalla
                    updateUser.setQpay_sepomex("1");

                    context.loading(true);

                    updateUserInformation(updateUser, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            comprarDongle(false);
                        }
                    });
                }


            }
        });

        getStates();

        setAddress();

        setEstados();

        // Verificar si ya tiene algún domicilio y pintarlo
        if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration()){
            campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        } else {
            // Geolocalizar y obtener CP
            getGeoInfo();
        }

    }

    /**
     * Valida si es la misma dirección que tenía
     * @return
     */
    private boolean isSameAdress(){
        if(campos.get(0).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street().trim())!=0){
            return false;
        }
        if(campos.get(2).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number().trim())!=0){
            return false;
        }
        if(campos.get(3).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code().trim())!=0){
            return false;
        }
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        if(campos.get(4).getText().trim().compareTo(userState.getState_name())!=0){
            return false;
        }
        if(campos.get(5).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality().trim())!=0){
            return false;
        }
        if(campos.get(6).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb().trim())!=0){
            return false;
        }
        return true;
    }

    /**
     * Validar campos requeridos
     */
    private void validate(){

        btn_continuar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_continuar.setEnabled(true);
    }

    public void setEstados(){

        List<ModelItem> estados = new ArrayList();

        for(int i=0; i<stateList.size(); i++)
            estados.add(new ModelItem(stateList.get(i).getState_name(), stateList.get(i).getState_code()));

        campos.get(4).setSpinner(estados);
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

            campos.get(6).setSpinner(poblados);

            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name())){

                    campos.get(4).setText(stateList.get(i).getState_name());
                    campos.get(4).setTag(stateList.get(i).getState_code());

                    campos.get(5).setText(Tools.getOnlyNumbersAndLetters(info.getD_mnpio()));
                    campos.get(5).setTag(info.getC_tipo_asenta());

                    campos.get(6).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    campos.get(6).setTag(info.getC_mnpio());

                    return;
                }

            }
        } else {
            campos.get(6).removeSpinner();
        }

    }

    /**
     * Si el usuario tiene un domicilio registrado en su perfil se pinta
     */
    private void setAddress(){
        setAddress1st = false;
        campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street());
        campos.get(1).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number());
        campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() : "");
        //campos.get(4).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        //Para estado se hace el set por el StateInfo
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        campos.get(4).setText(userState.getState_name());
        campos.get(5).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb());
        campos.get(6).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality());

    }

    /**
     * Obtener Ubicación y llenar codigo postal
     */
    private void getGeoInfo() {

        LastLocation coordenadas = CApplication.getLastLocation();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(coordenadas.getLatitude(), coordenadas.getLongitude(), 1);

            if(addresses.size()>0){

                String editCP = campos.get(3).getText();
                if(editCP!=null && editCP.isEmpty()) {

                    String calle = addresses.get(0).getThoroughfare();
                    calle = (calle != null ? calle.toUpperCase() : "");
                    calle = calle.replace("Ñ","N").replace("Á","A")
                            .replace("É","E").replace("Í","I")
                            .replace("Ó","O").replace("Ú","U");
                    calle = calle.replaceAll("[^A-Za-z1-9 ]+", "");
                    calle = (calle.length() > CALLE_MAX_SIZE ? calle.substring(0, CALLE_MAX_SIZE) : calle);
                    campos.get(0).setText(calle);

                    String noExt = addresses.get(0).getFeatureName();
                    noExt = (noExt != null ? noExt : "");
                    noExt = noExt.replaceAll("[^1-9]+", "");
                    noExt = (noExt.length() > NO_EXT_MAZ_SIZE ? noExt.substring(0, NO_EXT_MAZ_SIZE) : noExt);
                    campos.get(1).setText(noExt);

                    String cp = addresses.get(0).getPostalCode();
                    cp = (cp != null ? cp : "");
                    cp = (cp.length() > CP_MAX_SIZE ? cp.substring(0, CP_MAX_SIZE) : cp);
                    campos.get(3).setText(cp);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private StateInfo getStateInfo(String codeOrName) {

        try {
            Integer code = Integer.parseInt(codeOrName.trim());
            codeOrName = String.format("%02d",code);
        } catch (Exception e) {
            Log.e("info_negocio_1",e.getMessage());
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

    /**
     * Método para actualizar los datos del usuario donde le será entregado el dongle
     */
    public void updateUserInformation(QPAY_NewUser userToUpdate, final IFunction function){
        btn_continuar.setClickable(false);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userToUpdate);
        Log.d(TAG,"Inicia actualizaciòn de domicilio");
        //Log.d(TAG,json);


        IUpdateUserInformation registerUserService = null;
        try {
            registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_Profile.QPAY_ProfileExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {
                            Log.d(TAG,"Actualizacion de domicilio exitosa");
                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        } else {
                            context.loading(false);

                            context.validaSesion(userProfile.getQpay_code(), userProfile.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    btn_continuar.setClickable(true);
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.alert(R.string.general_error_catch);
        }

        registerUserService.updateUserInformation(userToUpdate);
    }


    /**
     * Método para comprar dongle
     */
    public void comprarDongle(Boolean showDialog) {
        btn_continuar.setClickable(false);
        Log.d(TAG,"Inicia compra de dongle");
        QPAY_Seed seed = new QPAY_Seed();
        seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        if(showDialog)
            context.loading(true);
        try {

            IDonglePurchase sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    btn_continuar.setClickable(true);
                    Logger.i(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_DongleBuyResponse.QPAY_DongleBuyResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_DongleBuyResponse dongleBuyResponse = gson.fromJson(json, QPAY_DongleBuyResponse.class);

                        if (dongleBuyResponse.getQpay_response().equals("true")) {

                            String balance = "Saldo " + Utils.paserCurrency(dongleBuyResponse.getQpay_object()[0].getBalance().replace("MXN", ""));
                            AppPreferences.setKinetoBalance(balance);

                            context.alert(R.string.compra_dongle_2_confirmacion, new IAlertButton() {
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
                            context.validaSesion(dongleBuyResponse.getQpay_code(), dongleBuyResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    btn_continuar.setClickable(true);
                    context.loading(false);
                    context.alert(R.string.general_error);

                }
            }, context);

            sale.doPurchase(seed);

        } catch (Exception e) {

            context.loading(false);
            e.printStackTrace();
            context.alert(R.string.general_error_catch);
        }

    }



}
