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

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecord_Object;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.SessionApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;


public class Fragment_registro_financiero_2 extends HFragment implements IMenuContext {

    private final String TAG = "reg_financioro_2";

    private View view;
    private MenuActivity context;
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



    public static Fragment_registro_financiero_2 newInstance(QPAY_FinancialInformation... data) {
        Fragment_registro_financiero_2 fragment = new Fragment_registro_financiero_2();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_2", new Gson().toJson(data[0]));

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_registro_financiero_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        campos = new ArrayList();

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        //Campo 0
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_nombre_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_5)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 1
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_calle_comercio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_6)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 2
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_numero_exterior))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 3
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_numero_interior))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_8)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 4
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

                        if(campos.get(4).getText().length() == 5)
                            sepomex(campos.get(4).getText());

                        validate.onChange(text);
                    }
                }));

        //Campo 5
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_estado))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 6
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_municipio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //Campo 7
        campos.add(CViewEditText.create(view.findViewById(R.id.edit_colonia))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        btn_continuar = view.findViewById(R.id.btn_continuar);

        // Listeners para elementos de pantalla

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Datos de pantalla
                String nombre_comercio = campos.get(0).getText();
                Fragment_registro_financiero_ocr_1.nombre_negocio = nombre_comercio;

                data.setQpay_street(campos.get(1).getText().toUpperCase());
                data.setQpay_external_number(campos.get(2).getText());
                data.setQpay_internal_number(campos.get(3).getText());
                data.setQpay_postal_code(campos.get(4).getText());
                data.setQpay_state(campos.get(5).getText().toUpperCase());
                data.setQpay_suburb(campos.get(7).getText().toUpperCase());
                data.setQpay_municipality(campos.get(6).getText().toUpperCase());
                //Datos default sin opcion en pantalla
                data.setQpay_sepomex("1");
                data.setQpay_birth_country("MEXICO");
                data.setQpay_nationality("MEXICANA");

                //Se pasa solo el nombre como string para no instanciar otro objeto mas pesado en memoria

                context.setFragment(Fragment_registro_financiero_ocr_1.newInstance(nombre_comercio));
            }
        });

        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(context);

        getStates();

        setAddress();

        setDataFC();

        // Verificar si ya tiene algún domicilio y pintarlo
        if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration()){
            campos.get(4).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        } else {
            // Geolocalizar y obtener CP
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGeoInfo();
                }
            }, 1000);
        }

    }

    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    public void setEstados(){

        List<ModelItem> estados = new ArrayList();

        for(int i=0; i<stateList.size(); i++)
            estados.add(new ModelItem(stateList.get(i).getState_name(), stateList.get(i).getState_code()));

        campos.get(5).setSpinner(estados);
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

            campos.get(7).setSpinner(poblados);

            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name())){

                    campos.get(5).setText(stateList.get(i).getState_name());
                    campos.get(5).setTag(stateList.get(i).getState_code());

                    campos.get(6).setText(Tools.getOnlyNumbersAndLetters(info.getD_mnpio()));
                    campos.get(6).setTag(info.getC_mnpio());

                    campos.get(7).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    campos.get(7).setTag(info.getC_tipo_asenta());

                    return;
                }

            }
        } else {
            campos.get(7).removeSpinner();
        }

    }

    /**
     * Si el usuario tiene un domicilio registrado en su perfil se pinta
     */
    private void setAddress(){
        setAddress1st = false;
        campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
        campos.get(1).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street());
        campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number());
        campos.get(3).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() : "");
        //campos.get(4).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        //Para estado se hace el set por el StateInfo
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        campos.get(5).setText(userState.getState_name());
        campos.get(7).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb());
        campos.get(6).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality());

    }

    /**
     * Obtener Geolocalizacion y llenar codigo postal
     */
    private void getGeoInfo() {

        ((HLocActivity)getContext()).obtainLocation(new IFunction() {
            @Override
            public void execute(Object[] data) {
                LastLocation coordenadas = CApplication.getLastLocation();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {

                    List<Address> addresses = geocoder.getFromLocation(coordenadas.getLatitude(), coordenadas.getLongitude(), 1);

                    if(addresses.size()>0){

                        String editCP = campos.get(4).getText();
                        if(editCP!=null && editCP.isEmpty()) {

                            String calle = addresses.get(0).getThoroughfare();
                            calle = (calle != null ? calle.toUpperCase() : "");
                            calle = calle.replace("Ñ","N").replace("Á","A")
                                    .replace("É","E").replace("Í","I")
                                    .replace("Ó","O").replace("Ú","U");
                            calle = calle.replaceAll("[^A-Za-z1-9 ]+", "");
                            calle = (calle.length() > CALLE_MAX_SIZE ? calle.substring(0, CALLE_MAX_SIZE) : calle);
                            campos.get(1).setText(calle);

                            String noExt = addresses.get(0).getFeatureName();
                            noExt = (noExt != null ? noExt : "");
                            noExt = noExt.replaceAll("[^1-9]+", "");
                            noExt = (noExt.length() > NO_EXT_MAZ_SIZE ? noExt.substring(0, NO_EXT_MAZ_SIZE) : noExt);
                            campos.get(2).setText(noExt);

                            String cp = addresses.get(0).getPostalCode();
                            cp = (cp != null ? cp : "");
                            cp = (cp.length() > CP_MAX_SIZE ? cp.substring(0, CP_MAX_SIZE) : cp);
                            campos.get(4).setText(cp);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) context;
    }

    public void setDataFC(){

        if(SessionApp.getInstance().getAllDataRecordResponse() != null) {

            QPAY_AllDataRecord_Object data = SessionApp.getInstance().getAllDataRecordResponse().getQpay_object().get(0);

            if(data.getUser() != null) {

                campos.get(0).setText(data.getUser().getQpay_merchant_name());
                campos.get(1).setText(data.getUser().getQpay_merchant_street());
                campos.get(2).setText(data.getUser().getQpay_merchant_external_number());
                campos.get(3).setText(data.getUser().getQpay_merchant_internal_number());
                campos.get(4).setText(data.getUser().getQpay_merchant_postal_code());
                campos.get(5).setText(data.getUser().getQpay_merchant_state());
                campos.get(6).setText(data.getUser().getQpay_merchant_municipality());
                campos.get(7).setText(data.getUser().getQpay_merchant_suburb());

            }

        }
    }
}

