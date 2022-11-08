package com.blm.qiubopay.modules;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IRollPuchase;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.profile.QPAY_Profile;
import com.blm.qiubopay.models.rolls.QPAY_BuyRollPetition;
import com.blm.qiubopay.models.rolls.QPAY_BuyRollResponse;
import com.blm.qiubopay.models.rolls.QPAY_Roll;
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

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_compra_rollos_2 extends HFragment {

    final private String TAG = "compra_rollos_2";

    private View view;
    private MenuActivity context;

    private QPAY_Roll data;

    private ArrayList<HEditText> campos;
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


    public static Fragment_compra_rollos_2 newInstance(Object... data) {
        Fragment_compra_rollos_2 fragment = new Fragment_compra_rollos_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_rollos_2", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_compra_rollos_2"), QPAY_Roll.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_rollos_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(view).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        campos = new ArrayList();

        final ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {
                //context.hideKeyboard();
            }
        };

        // Inicializar elementos de pantalla

        //RSB 20191230. Cambio solicitado, se cambian las longitudes acorde a como estan en Fragment_Registro_2
        edit_calle = new HEditText((EditText) view.findViewById(R.id.edit_calle_comercio),
                true, CALLE_MAX_SIZE, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_calle_comercio));

        //Campo 0
        campos.add(edit_calle);

        //RSB 20191230. Cambio solicitado, se cambian las longitudes acorde a como estan en Fragment_Registro_2
        //Campo 1
        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_numero_interior),
                false, 6, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_numero_interior)));

        edit_no_ext = new HEditText((EditText) view.findViewById(R.id.edit_numero_exterior),
                true, NO_EXT_MAZ_SIZE, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_numero_exterior));

        //Campo 2
        campos.add(edit_no_ext);

        edit_cp = new HEditText((EditText) view.findViewById(R.id.edit_codigo_postal),
                true, CP_MAX_SIZE, CP_MAX_SIZE, HEditText.Tipo.NUMERO, new ITextChanged() {
            @Override
            public void onChange() {

                if(edit_cp.getText().length() == CP_MAX_SIZE)
                    findByPostalCode(edit_cp.getText());

                if(setAddress1st)
                    setAddress();

                validate();
            }

            @Override
            public void onMaxLength() {
                //context.hideKeyboard();
            }
        }, (TextView) view.findViewById(R.id.text_codigo_postal));

        //Campo 3
        campos.add(edit_cp);

        //Campo 4
        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_estado),
                true, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_estado)));

        //Campo 5
        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_colonia),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_colonia)));

        //Campo 6
        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_municipio),
                true, 100, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_municipio)));

        btn_continuar = view.findViewById(R.id.btn_continuar);

        //Inicializar los spinner y la BD
        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(context);
        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();
        setEstados();

        // Verificar si ya tiene algún domicilio y pintarlo
        if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration()){
            setAddress1st = true;
            campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        } else {
            // Geolocalizar y obtener CP
            getGeoInfo();
        }

        // Listeners para elementos de pantalla

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continuar.setClickable(false);
                //if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration() && isSameAdress()){
                //La dirección es igual a la del perfil
                comprarRollos();
                /*} else {
                    //La dirección del perfil cambia

                    QPAY_NewUser updateUser = new QPAY_NewUser();

                    updateUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                    //Datos de pantalla
                    updateUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
                    updateUser.setQpay_merchant_internal_number(campos.get(1).getText());
                    updateUser.setQpay_merchant_external_number(campos.get(2).getText());
                    updateUser.setQpay_merchant_postal_code(campos.get(3).getText());
                    StateInfo state = getStateInfo(campos.get(4).getText());
                    updateUser.setQpay_merchant_state(Integer.parseInt(state.getState_code())+"");
                    updateUser.setQpay_merchant_suburb(campos.get(5).getText().toUpperCase());
                    updateUser.setQpay_merchant_municipality(campos.get(6).getText().toUpperCase());
                    //Datos default sin opcion en pantalla
                    updateUser.setQpay_sepomex("1");

                    updateUserInformation(updateUser, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            comprarRollos();
                        }
                    });
                }*/


            }
        });

    }

    /**
     * Validar campos requeridos
     */
    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    /**
     * Buscar dirección por Código Postal
     * @param codigo
     */
    private void findByPostalCode(String codigo){

        LinearLayout layout_estado = view.findViewById(R.id.layout_estado);

        dataBaseAccessHelper.open();
        sepomexSearchResult = dataBaseAccessHelper.findPostalCode(codigo);
        dataBaseAccessHelper.close();

        if(sepomexSearchResult != null && sepomexSearchResult.size() > 0){

            ArrayList<String> nombre_poblados = new ArrayList();

            for(int i=0; i<sepomexSearchResult.size(); i++) {
                nombre_poblados.add(Tools.getOnlyNumbersAndLetters(((SepomexInformation) sepomexSearchResult.toArray()[i])
                        .getD_asenta().replace("(", "").replace(")", "")));
            }

            setPoblacion(nombre_poblados);

            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name()))
                {
                    //Este layout invisible al hacerse true ya no permite que se pueda hacer click en la entidad
                    layout_estado.setClickable(true);
                    estados.setText(stateList.get(i).getState_name());
                    estados.setTag(stateList.get(i).getState_code());

                    campos.get(5).getEditText().setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    campos.get(5).getEditText().setTag(info.getC_tipo_asenta());
                    campos.get(5).getEditText().setEnabled(false);

                    campos.get(6).getEditText().setText(Tools.getOnlyNumbersAndLetters(info.getD_mnpio()));
                    campos.get(6).getEditText().setTag(info.getC_mnpio());
                    campos.get(6).getEditText().setEnabled(false);

                    return;
                }
            }
        }else{

            if(poblacion != null){
                poblacion.clearListSelection();
                setPoblacion(new ArrayList<String>());
                poblacion.setEditable(true);
            }

            campos.get(5).getEditText().setEnabled(true);
            campos.get(6).getEditText().setEnabled(true);

            layout_estado.setClickable(false);

        }


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

        for (StateInfo state:stateList){
            if(state.getState_code().compareTo(codeOrName)==0){
                return state;
            }
            if(state.getState_name().trim().toUpperCase().compareTo(codeOrName)==0){
                return state;
            }
        }

        return null;
    }


    /**
     * Colocar los estados obtenidos en el arreglo en duro
     */
    private void setEstados(){

        estados = view.findViewById(R.id.edit_estado);
        ArrayList<String> nombre_estados = new ArrayList();
        for(int i=0; i<stateList.size(); i++)
            nombre_estados.add(stateList.get(i).getState_name());
        setDataSpinner(estados, nombre_estados);
    }

    /**
     * Colocar las posibles colonias o poblaciones para el código postal
     * @param nombre_poblados
     */
    private void setPoblacion(ArrayList<String> nombre_poblados){

        poblacion = view.findViewById(R.id.edit_colonia);
        setDataSpinner(poblacion, nombre_poblados);
    }


    /**
     * Llena los combos de estados y colonias (poblacion)
     * @param spiner
     * @param data
     */
    private void setDataSpinner(EditSpinner spiner, ArrayList<String> data){

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,data);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                validate();
            }
        });
    }


    /**
     * Si el usuario tiene un domicilio registrado en su perfil se pinta
     */
    private void setAddress(){
        setAddress1st = false;
        campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street());
        campos.get(1).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number(): "");
        campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number());
        //campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        //Para estado se hace el set por el StateInfo
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        campos.get(4).setText(userState.getState_name().toUpperCase());
        campos.get(5).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb());
        campos.get(6).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality());

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
        if(campos.get(5).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb().trim())!=0){
            return false;
        }
        if(campos.get(6).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality().trim())!=0){
            return false;
        }
        return true;
    }


    /**
     * Obtener Geolocalizacion y llenar codigo postal
     */
    private void getGeoInfo() {

        LastLocation coordenadas = CApplication.getLastLocation();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(coordenadas.getLatitude(), coordenadas.getLongitude(), 1);

            if(addresses.size()>0){

                if(edit_cp!=null){

                    String editCP = edit_cp.getText();
                    if(editCP!=null && editCP.isEmpty()) {

                        String cp = addresses.get(0).getPostalCode();
                        cp = (cp != null ? cp : "");
                        cp = (cp.length() > CP_MAX_SIZE ? cp.substring(0, CP_MAX_SIZE) : cp);
                        edit_cp.setText(cp);

                        if (edit_calle != null) {
                            String calle = addresses.get(0).getThoroughfare();
                            calle = (calle != null ? calle.toUpperCase() : "");
                            calle = calle.replace("Ñ","N").replace("Á","A")
                                    .replace("É","E").replace("Í","I")
                                    .replace("Ó","O").replace("Ú","U");
                            calle = calle.replaceAll("[^A-Za-z1-9 ]+", "");
                            calle = (calle.length() > CALLE_MAX_SIZE ? calle.substring(0, CALLE_MAX_SIZE) : calle);
                            edit_calle.setText(calle);
                        }

                        if (edit_no_ext != null) {
                            String noExt = addresses.get(0).getFeatureName();
                            noExt = (noExt != null ? noExt : "");
                            noExt = noExt.replaceAll("[^1-9]+", "");
                            noExt = (noExt.length() > NO_EXT_MAZ_SIZE ? noExt.substring(0, NO_EXT_MAZ_SIZE) : noExt);
                            edit_no_ext.setText(noExt);
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void comprarRollos() {

        //context.onLoading(true);

        Log.d(TAG,"Inicia compra de dongle");
        //QPAY_Seed seed = new QPAY_Seed();
        //seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        QPAY_BuyRollPetition petition = new QPAY_BuyRollPetition();
        petition.setId(""+data.getId());
        petition.setProductType("ROLL");
        petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        /*updateUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
        updateUser.setQpay_merchant_internal_number(campos.get(1).getText());
        updateUser.setQpay_merchant_external_number(campos.get(2).getText());
        updateUser.setQpay_merchant_postal_code(campos.get(3).getText());
        StateInfo state = getStateInfo(campos.get(4).getText());
        updateUser.setQpay_merchant_state(Integer.parseInt(state.getState_code())+"");
        updateUser.setQpay_merchant_suburb(campos.get(5).getText().toUpperCase());
        updateUser.setQpay_merchant_municipality(campos.get(6).getText().toUpperCase());*/

        StateInfo state = getStateInfo(campos.get(4).getText());
        petition.getDeliveryAddress().setStreet(campos.get(0).getText().toUpperCase());
        petition.getDeliveryAddress().setExternalNumber(campos.get(2).getText());
        petition.getDeliveryAddress().setInternalNumber(campos.get(1).getText());
        petition.getDeliveryAddress().setSuburb(campos.get(5).getText().toUpperCase());
        petition.getDeliveryAddress().setMunicipality(campos.get(6).getText().toUpperCase());
        petition.getDeliveryAddress().setPostalCode(campos.get(3).getText());
        petition.getDeliveryAddress().setState(Integer.parseInt(state.getState_code())+"");


        petition.getDeliveryAddress().setLatitude(null);
        petition.getDeliveryAddress().setLongitude(null);

        //QPAY_DeliveryAddress deliveryAddress = new QPAY_DeliveryAddress();

        //deliveryAddress

        context.loading(true);

        try {

            IRollPuchase sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BuyRollResponse.QPAY_BuyRollResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BuyRollResponse response = gson.fromJson(json, QPAY_BuyRollResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            String balance = "Saldo " + Utils.paserCurrency(response.getQpay_object()[0].getBalance().replace("MXN", ""));
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

                        } else if (response.getQpay_response().equals("false")) {
                            btn_continuar.setClickable(true);
                            context.validaSesion(response.getQpay_code(), response.getQpay_description());
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

            sale.doPurchaseRoll(petition);

        } catch (Exception e) {

            context.loading(false);
            e.printStackTrace();
            context.alert(R.string.general_error_catch);
        }

    }



}

