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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IBuyProduct;
import com.blm.qiubopay.listeners.IRollPuchase;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.product.EquipmentCost;
import com.blm.qiubopay.models.product.QPAY_BuyEquipmentRequest;
import com.blm.qiubopay.models.product.QPAY_BuyEquipmentResponse;
import com.blm.qiubopay.models.rolls.QPAY_BuyRollPetition;
import com.blm.qiubopay.models.rolls.QPAY_BuyRollResponse;
import com.blm.qiubopay.models.rolls.QPAY_DeliveryAddress;
import com.blm.qiubopay.models.rolls.QPAY_Roll;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.registro.Fragment_registro_5;
import com.blm.qiubopay.modules.registro.Fragment_registro_pin;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_ubicacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ubicacion extends HFragment {

    private final String TAG = "fragment_ubicacion";

    /**
     * Variables para invocar la pantalla de dirección
     */
	public static final int REGISTER_VAS_ADDRESS    = 1;
    public static final int BUY_ROLLS_ADDRESS       = 4;
    public static final int T1000SWAP_ADDRESS       = 6;
    public static final int BUY_EQUIPMENT_ADDRESS   = 7;
    public static final int APP2TERMINAL_MIGRATION_ADDRESS   = 8;

    private QPAY_FinancialInformation data = SessionApp.getInstance().getFinancialInformation();

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private Collection<SepomexInformation> sepomexSearchResult;
    private DataBaseAccessHelper dataBaseAccessHelper;
    private ArrayList<StateInfo> stateList;

    private boolean setAddress1st = false;

    final private int CALLE_MAX_SIZE = 30;
    final private int NO_EXT_MAZ_SIZE = 6;
    final private int CP_MAX_SIZE = 5;

    private RadioButton rad_aceptar_tyc;
    private boolean accepted;
    private String tycTimestamp;
    private String aviTimestamp;

    //Definen el comportamiento de la dirección
    private int addressType;
    private String jsonObject;

    CViewEditText.ITextChanged validate;

    boolean isSepomexCharging = false;

    public static Fragment_ubicacion newInstance(Object... params) {
        Fragment_ubicacion fragment = new Fragment_ubicacion();
        Bundle args = new Bundle();
        if(params.length > 0) {
            args.putInt("Fragment_ubicacion", (Integer) params[0]);
        }
        if(params.length > 1) {
            args.putString("Fragment_ubicacion_1",new Gson().toJson(params[1]));
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            addressType = getArguments().getInt("Fragment_ubicacion",0);
            jsonObject = getArguments().getString("Fragment_ubicacion_1","");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        return super.onCreated(inflater.inflate(R.layout.fragment_ubicacion, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        campos = new ArrayList();

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        //0 calle
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_calle_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_6)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //1 NoExt
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_exterior))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //2 NoInt
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_interior))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_8)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //3 CP
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_codigo_postal))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(5)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(campos.get(3).getText().length() == 5 && !isSepomexCharging)
                            sepomex(campos.get(3).getText());

                        validate.onChange(text);
                    }
                }));
        //4 EDO
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_estado))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //5 MPO
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_municipio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //6 COL
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colonia))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Se debe terminar de configurar la pantalla acorde al módulo del que proviene
        configScreen();

        // Listeners para elementos de pantalla

        //Se debe de configurar la acción del botón acorde al módulo del que proviene
        btn_continuar.setOnClickListener(configAcceptListener());

        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(getContext());

        getStates();

        boolean getGeo = false;
        if(AppPreferences.getUserProfile()!=null && addressType!=REGISTER_VAS_ADDRESS
                && addressType!=APP2TERMINAL_MIGRATION_ADDRESS){
            setAddress();
            // Verificar si ya tiene algún domicilio y pintarlo
            if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration()){
                campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
            } else {
                getGeo = true;
            }
        } else {
            getGeo = true;
        }

        if(getGeo){
            // Geolocalizar y obtener CP
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGeoInfo();
                }
            }, 1000);
        }


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

        if(isSepomexCharging)
            return;


        isSepomexCharging = true;

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
                    campos.get(5).setTag(info.getC_mnpio());

                    campos.get(6).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    campos.get(6).setTag(info.getC_tipo_asenta());


                    return;
                }

            }
        } else {
            campos.get(6).removeSpinner();
        }

        isSepomexCharging = false;

    }

    /**
     * Si el usuario tiene un domicilio registrado en su perfil se pinta
     */
    private void setAddress(){
        setAddress1st = false;
        campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street());
        campos.get(1).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number());
        campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() : "");
        //campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        //Para estado se hace el set por el StateInfo
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        campos.get(4).setText(userState.getState_name());
        campos.get(5).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality());
        campos.get(6).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb());

    }

    /**
     * Obtener Geolocalizacion y llenar codigo postal
     */
    private void getGeoInfo() {

        ((HLocActivity)getContext()).obtainLocation(new IFunction() {
            @Override
            public void execute(Object[] data) {
                LastLocation coordenadas = CApplication.getLastLocation();
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    Log.d(TAG,"location: " + coordenadas.getLatitude() + "," + coordenadas.getLongitude());
                    List<Address> addresses = geocoder.getFromLocation(coordenadas.getLatitude(), coordenadas.getLongitude(), 1);
                    Log.d(TAG,"location: " + addresses.size() + " ubicaciones localizadas");
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
                    Log.e(TAG,"location: " + e.getMessage());
                    //e.printStackTrace();
                }
            }
        });

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


    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        if(addressType==T1000SWAP_ADDRESS){
            if(!rad_aceptar_tyc.isChecked()){
                btn_continuar.setEnabled(false);
                return;
            }
        }

        btn_continuar.setEnabled(true);
    }


    /**
     * Configuracion pantalla
     */
    private void configScreen(){

        final TextView tvTitle = getView().findViewById(R.id.tv_id_title);
        final TextView tvAddressDescription = getView().findViewById(R.id.tv_address_description);
        final TextView tvStep = getView().findViewById(R.id.tv_step);

        switch (addressType) {

            case REGISTER_VAS_ADDRESS: //REGISTRO VAS N3 UNICAMENTE
                tvTitle.setText(R.string.text_register_8);
                tvAddressDescription.setText("");
                tvStep.setText("");

                final LinearLayout layoutNegocio = getView().findViewById(R.id.layout_negocio);
                layoutNegocio.setVisibility(View.VISIBLE);

                //7 NOMBRE COMERCIO
                campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombre_comercio))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(30)
                        .setType(CViewEditText.TYPE.TEXT)
                        .setHint(R.string.text_registro_5)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                break;

            case T1000SWAP_ADDRESS:  //SWAP

                final LinearLayout layoutSwap = getView().findViewById(R.id.layout_swap);
                final LinearLayout layoutTerminos = getView().findViewById(R.id.layout_terminos_aviso);
                rad_aceptar_tyc = getView().findViewById(R.id.rad_aceptar_tyc);
                final TextView btn_terminos = getView().findViewById(R.id.btn_terminos);

                layoutSwap.setVisibility(View.VISIBLE);
                layoutTerminos.setVisibility(View.VISIBLE);

                tvTitle.setText(AppPreferences.isSwapT1000() ? R.string.text_swapt1000_title : R.string.text_multi_t1000_title);

                //7 CORREO ELECTRONICO
                campos.add(CViewEditText.create(getView().findViewById(R.id.edit_correo_electronico))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(70)
                        .setType(CViewEditText.TYPE.EMAIL)
                        .setHint(R.string.text_access_2)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));
                //8 NUMERO CELULAR
                campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                        .setRequired(true)
                        .setMinimum(10)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_access_26)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                rad_aceptar_tyc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (accepted)
                            accepted = false;
                        else {
                            accepted = true;
                            tycTimestamp = Utils.getTimestamp();
                            aviTimestamp = Utils.getTimestamp();
                        }
                        rad_aceptar_tyc.setChecked(accepted);
                        validate();
                    }
                });

                btn_terminos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().setFragment(Fragment_browser.newInstance(Globals.URL_TERMS_AND_CONDITIONS));
                    }
                });

                break;

            case BUY_EQUIPMENT_ADDRESS:

                tvTitle.setText(R.string.compra_material_title);
                tvAddressDescription.setText("¿En qué domicilio quieres recibirlo?");
                addReferences();

                break;

            case BUY_ROLLS_ADDRESS:

                tvTitle.setText(R.string.compra_rolls_title);
                tvAddressDescription.setText("¿En qué domicilio quieres recibirlos?");
                addReferences();

                break;

            case APP2TERMINAL_MIGRATION_ADDRESS: //SPARTAN MIGRATION
                tvTitle.setText(R.string.text_register_8);
                tvAddressDescription.setText("");
                tvStep.setText("");

                final LinearLayout layoutNegocio1 = getView().findViewById(R.id.layout_negocio);
                final LinearLayout layoutColaborador = getView().findViewById(R.id.layout_colaborador);
                layoutNegocio1.setVisibility(View.VISIBLE);
                layoutColaborador.setVisibility(View.VISIBLE);

                //7 NOMBRE COMERCIO
                campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombre_comercio))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(30)
                        .setType(CViewEditText.TYPE.TEXT)
                        .setHint(R.string.text_registro_5)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                //8 NUMERO COLABORADOR
                campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colaborador))
                        .setRequired(true)
                        .setMinimum(5)
                        .setMaximum(14)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_register_colaborador)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                break;
        }
    }


    private void addReferences(){

        final LinearLayout layoutReferencias = getView().findViewById(R.id.layout_referencias);
        layoutReferencias.setVisibility(View.VISIBLE);

        //7 ENTRE CALLES
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_crossing_streets))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.negocio_info_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //8 REFERENCIAS
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_references))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.negocio_info_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

    }


    /**
     * Funcionamiento de boton aceptar acorde al módulo que lo llama
     * @return
     */
    private View.OnClickListener configAcceptListener() {
        View.OnClickListener onClick = null;

        switch (addressType) {
            case REGISTER_VAS_ADDRESS:  //REGISTRO VAS N3 UNICAMENTE
                if(jsonObject!=null && !jsonObject.isEmpty()) {
                    Gson gson = new GsonBuilder().create();
                    QPAY_NewUser newUser = gson.fromJson(jsonObject, QPAY_NewUser.class);
                    onClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Datos que serán obtenidos de pantalla de forma general para todos los servicios
                            newUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
                            newUser.setQpay_merchant_external_number(campos.get(1).getText());
                            newUser.setQpay_merchant_internal_number(campos.get(2).getText());
                            newUser.setQpay_merchant_postal_code(campos.get(3).getText());
                            StateInfo state = getStateInfo(campos.get(4).getText());
                            newUser.setQpay_merchant_state(Integer.parseInt(state.getState_code()) + "");
                            newUser.setQpay_merchant_municipality(campos.get(5).getText().toUpperCase());
                            newUser.setQpay_merchant_suburb(campos.get(6).getText().toUpperCase());
                            //Datos adicionales del swap
                            newUser.setQpay_merchant_name(campos.get(7).getText());
                            //Datos default sin opcion en pantalla
                            newUser.setQpay_sepomex("1");

                            getContext().setFragment(Fragment_registro_5.newInstance(newUser));
                        }
                    };
                }
                break;

            case T1000SWAP_ADDRESS:  //SWAP
                onClick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QPAY_NewUser newUser = new QPAY_NewUser();
                        //Datos que serán obtenidos de pantalla de forma general para todos los servicios
                        newUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
                        newUser.setQpay_merchant_external_number(campos.get(1).getText());
                        newUser.setQpay_merchant_internal_number(campos.get(2).getText());
                        newUser.setQpay_merchant_postal_code(campos.get(3).getText());
                        StateInfo state = getStateInfo(campos.get(4).getText());
                        newUser.setQpay_merchant_state(Integer.parseInt(state.getState_code())+"");
                        newUser.setQpay_merchant_municipality(campos.get(5).getText().toUpperCase());
                        newUser.setQpay_merchant_suburb(campos.get(6).getText().toUpperCase());
                        //Datos adicionales del swap
                        newUser.setQpay_mail(campos.get(7).getText());
                        newUser.setQpay_cellphone(campos.get(8).getText());
                        newUser.setQpay_accepted_conditions_date(tycTimestamp);
                        newUser.setQpay_accepted_privacy_date(aviTimestamp);
                        //Datos default sin opcion en pantalla
                        newUser.setQpay_sepomex("1");

                        getContext().setFragment(Fragment_registro_5.newInstance(newUser));
                    }
                };
                break;

            case BUY_EQUIPMENT_ADDRESS:  //En compra de equipo
                if(jsonObject!=null && !jsonObject.isEmpty()) {
                    Gson gson = new GsonBuilder().create();
                    EquipmentCost equipmentCost = gson.fromJson(jsonObject, EquipmentCost.class);
                    onClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_continuar.setEnabled(false);
                            buyEquipment(equipmentCost, new IFunction() {
                                @Override
                                public void execute(Object[] result) {
                                    String json = gson.toJson(result[0]);
                                    Log.d(TAG,"Response buyEquipment: " + json);
                                    QPAY_BuyEquipmentResponse response = gson.fromJson(json, QPAY_BuyEquipmentResponse.class);

                                    if (response.getQpay_response().equals("true")) {
                                        getContext().alert(R.string.compra_dongle_2_confirmacion, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                                ((MenuActivity) getContext()).cargarSaldo(true,false,
                                                        false,new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {
                                                        ((MenuActivity) getContext()).initHome();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        btn_continuar.setEnabled(true);
                                        ((MenuActivity) getContext()).validaSesion(response.getQpay_code(),response.getQpay_description());
                                    }
                                }
                            });
                        }
                    };
                }
                break;

            case BUY_ROLLS_ADDRESS:  //En compra de rollos
                if(jsonObject!=null && !jsonObject.isEmpty()) {
                    Gson gson = new GsonBuilder().create();
                    QPAY_Roll rollCost = gson.fromJson(jsonObject, QPAY_Roll.class);
                    onClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_continuar.setEnabled(false);
                            buyRolls(rollCost, new IFunction() {
                                @Override
                                public void execute(Object[] result) {
                                    String json = gson.toJson(result[0]);
                                    Log.d(TAG,"Response buyRolls " + json);
                                    QPAY_BuyRollResponse response = gson.fromJson(json, QPAY_BuyRollResponse.class);

                                    if (response.getQpay_response().equals("true")) {
                                        getContext().alert(R.string.compra_dongle_2_confirmacion, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                                ((MenuActivity) getContext()).cargarSaldo(true,false,
                                                        false,new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {
                                                        ((MenuActivity) getContext()).initHome();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        btn_continuar.setEnabled(true);
                                        ((MenuActivity) getContext()).validaSesion(response.getQpay_code(),response.getQpay_description());
                                    }
                                }
                            });
                        }
                    };
                }
                break;

            case APP2TERMINAL_MIGRATION_ADDRESS: //SPARTAN MIGRATION
                onClick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();
                        QPAY_NewUser newUser = new QPAY_NewUser();
                        newUser.setQpay_seed(seed);
                        //Datos que serán obtenidos de pantalla de forma general para todos los servicios
                        newUser.setQpay_merchant_street(campos.get(0).getText().toUpperCase());
                        newUser.setQpay_merchant_external_number(campos.get(1).getText());
                        newUser.setQpay_merchant_internal_number(campos.get(2).getText());
                        newUser.setQpay_merchant_postal_code(campos.get(3).getText());
                        StateInfo state = getStateInfo(campos.get(4).getText());
                        newUser.setQpay_merchant_state(Integer.parseInt(state.getState_code())+"");
                        newUser.setQpay_merchant_municipality(campos.get(5).getText().toUpperCase());
                        newUser.setQpay_merchant_suburb(campos.get(6).getText().toUpperCase());
                        //Datos adicionales del swap
                        newUser.setQpay_merchant_name(campos.get(7).getText());
                        newUser.setQpay_promoter_id(campos.get(8).getText());
                        //Datos default sin opcion en pantalla
                        newUser.setQpay_sepomex("1");

                        updateUserInformation(newUser, new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getContext().setFragment(Fragment_registro_pin.newInstance(), true);
                            }
                        });
                    }
                };
                break;

        }

        return onClick;
    }


    /**
     * Compra de Material Publicitario
     * @param equipmentCost
     * @param function
     */
    private void buyEquipment(EquipmentCost equipmentCost, IFunction function) {

        getContext().loading(true);

        QPAY_BuyEquipmentRequest petition = new QPAY_BuyEquipmentRequest();
        petition.setId(""+equipmentCost.getId());
        petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        petition.setProductType(equipmentCost.getProductType());
        petition.setQuantity(1);//Este campo no se usa en QTC pero quedo en el objeto

        petition.setDeliveryAddress(new QPAY_DeliveryAddress());
        petition.getDeliveryAddress().setStreet(campos.get(0).getText().toUpperCase());
        petition.getDeliveryAddress().setExternalNumber(campos.get(1).getText());
        petition.getDeliveryAddress().setInternalNumber(campos.get(2).getText());
        petition.getDeliveryAddress().setPostalCode(campos.get(3).getText());
        StateInfo state = getStateInfo(campos.get(4).getText());
        petition.getDeliveryAddress().setState(Integer.parseInt(state.getState_code())+""); //Se manda sin 0 los primeros 9
        petition.getDeliveryAddress().setMunicipality(campos.get(5).getText().toUpperCase());
        petition.getDeliveryAddress().setSuburb(campos.get(6).getText().toUpperCase());
        petition.getDeliveryAddress().setCrossingStreets(campos.get(7).getText().toUpperCase());
        petition.getDeliveryAddress().setReferences(campos.get(8).getText().toUpperCase());

        Log.d(TAG,"Request buyEquipment: " + new Gson().toJson(petition));

        IBuyProduct equipmentCostListener = null;
        try {
            equipmentCostListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                        btn_continuar.setEnabled(true);
                    } else {
                        function.execute(result);
                    }
                }
                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_continuar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_continuar.setEnabled(true);
        }

        equipmentCostListener.buyEquipment(petition);
    }


    /**
     * Método para comprar dongle
     */
    public void buyRolls(QPAY_Roll rollCost, IFunction function) {

        getContext().loading(true);

        QPAY_BuyRollPetition petition = new QPAY_BuyRollPetition();
        petition.setId(""+rollCost.getId());
        petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        petition.setProductType(rollCost.getProduct_type());

        petition.getDeliveryAddress().setStreet(campos.get(0).getText().toUpperCase());
        petition.getDeliveryAddress().setExternalNumber(campos.get(1).getText());
        petition.getDeliveryAddress().setInternalNumber(campos.get(2).getText());
        petition.getDeliveryAddress().setPostalCode(campos.get(3).getText());
        StateInfo state = getStateInfo(campos.get(4).getText());
        petition.getDeliveryAddress().setState(Integer.parseInt(state.getState_code())+"");
        petition.getDeliveryAddress().setMunicipality(campos.get(5).getText().toUpperCase());
        petition.getDeliveryAddress().setSuburb(campos.get(6).getText().toUpperCase());
        petition.getDeliveryAddress().setCrossingStreets(campos.get(7).getText().toUpperCase());
        petition.getDeliveryAddress().setReferences(campos.get(8).getText().toUpperCase());

        Log.d(TAG,"Request buyRolls: " + new Gson().toJson(petition));

        IRollPuchase rollPuchaseListener = null;
        try {
            rollPuchaseListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                        btn_continuar.setEnabled(true);
                    } else {
                        function.execute(result);
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_continuar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_continuar.setEnabled(true);
        }

        rollPuchaseListener.doPurchaseRoll(petition);
    }


    /**
     * Llama servicio para actualizar el registro y localizar la terminal
     * @param data
     * @param function
     */
    private void updateUserInformation(QPAY_NewUser data, final IFunction function){

        getContext().loading(true);

        IUpdateUserInformation registerUserService = null;
        try {
            registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        } else {

                            getContext().alert(userProfile.getQpay_description());

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
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

        registerUserService.updateUserInformation(data);
    }

}