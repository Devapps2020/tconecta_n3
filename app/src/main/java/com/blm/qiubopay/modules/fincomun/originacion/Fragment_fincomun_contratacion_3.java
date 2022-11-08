package com.blm.qiubopay.modules.fincomun.originacion;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.modules.Fragment_depositos_1;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Originacion.FCBeneficiariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBeneficiariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCReferenciasResponse;
import mx.com.fincomun.origilib.Model.Originacion.Beneficiarios;
import mx.com.fincomun.origilib.Model.Originacion.Referencias;
import mx.com.fincomun.origilib.Objects.Beneficiarios.DHBeneficiarioDireccion;
import mx.com.fincomun.origilib.Objects.Catalogos.Genero;
import mx.com.fincomun.origilib.Objects.Catalogos.LugarNacimiento;
import mx.com.fincomun.origilib.Objects.Catalogos.Municipio;
import mx.com.fincomun.origilib.Objects.Catalogos.Parentesco;
import mx.com.fincomun.origilib.Objects.DHBeneficiario;
import mx.com.fincomun.origilib.Objects.Referencia.DHReferencia;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_contratacion_3 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;

    private TextView et_name,et_last_name,et_last_name_2,tv_gender, tv_birthday,tv_street,tv_avenue,
            et_relationship, tv_interior_num,tv_outdoor_num, tv_municipality, tv_population,
            tv_state, tv_postal_code,tv_suburb,tv_country;
    private LinearLayout ll_address;
    private CheckBox check_enable_address;
    private ImageView iv_help;

    private ArrayList<ModelItem> relationship = new ArrayList();
    private DHReferencia referencia1 = new DHReferencia();
    private DHBeneficiario beneficiario;
    private Boolean isLoading  =false;

    public static Fragment_fincomun_contratacion_3 newInstance() {
        Fragment_fincomun_contratacion_3 fragment = new Fragment_fincomun_contratacion_3();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_contratacion_3"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_contratacion_3, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .showTitle("Contratación de préstamo")
                .setColorTitle(R.color.FC_blue_6)
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });
        campos = new ArrayList();



        iv_help = getView().findViewById(R.id.iv_help);
        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().alert("El beneficiario es la persona que recibirá el dinero de tu cuenta en caso de fallecimiento",new IAlertButton() {
                    @Override
                    public String onText() {
                        return "CERRAR";
                    }

                    @Override
                    public void onClick() {

                    }
                });
            }
        });
        ll_address = getView().findViewById(R.id.ll_address);
        check_enable_address = getView().findViewById(R.id.check_enable_address);
        check_enable_address.setOnCheckedChangeListener(listenerCheck);

        //0/name
        campos.add(FCEditText.create(getView().findViewById(R.id.et_name))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Nombre")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //1/lastname
        campos.add(FCEditText.create(getView().findViewById(R.id.et_last_name))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Apellido Paterno")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //2/lastname
        campos.add(FCEditText.create(getView().findViewById(R.id.et_last_name_2))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Apellido Materno")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //3/gender
        campos.add(FCEditText.create(getView().findViewById(R.id.et_gender))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Género")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //4/birthday
        campos.add(FCEditText.create(getView().findViewById(R.id.et_birthday))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(FCEditText.TYPE.NONE)
                .setDatePicker(true)
                .setIcon(R.drawable.date_range)
                .setHint("Fecha de nacimiento")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //5/relationship
        campos.add(FCEditText.create(getView().findViewById(R.id.et_relationship))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Parentesco con el titular")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //6/street
        campos.add(FCEditText.create(getView().findViewById(R.id.et_street))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Calle")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //7/interior
        campos.add(FCEditText.create(getView().findViewById(R.id.et_interior_num))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(15)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Número interior")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //8/outdoor
        campos.add(FCEditText.create(getView().findViewById(R.id.et_outdoor_num))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(15)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Número exterior")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //9/state
        campos.add(FCEditText.create(getView().findViewById(R.id.et_state))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Entidad federativa/Estado")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        setMunicipios(campos.get(9).getTag());
                       // validate();
                    }
                }));
        //10/municipaliy
        campos.add(FCEditText.create(getView().findViewById(R.id.et_municipality))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Alcaldía/Municipio")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //11/population
        campos.add(FCEditText.create(getView().findViewById(R.id.et_population))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Ciudad/Población")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //12/postal code
        campos.add(FCEditText.create(getView().findViewById(R.id.et_postal_code))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(5)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("Código Postal")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //13/suburb
        campos.add(FCEditText.create(getView().findViewById(R.id.et_suburb))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Colonia")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        //14/country
        campos.add(FCEditText.create(getView().findViewById(R.id.et_country))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(20)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("País")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    validate();
                }
            }
        });

        getContextMenu().catalogs(new IFunction() {
            @Override
            public void execute(Object[] data) {
                setList();
            }
        });
        fillData();
        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_BENEFICIARIOS);

    }

    FCEditText.ITextChanged validate = new FCEditText.ITextChanged() {
        @Override
        public void onChange(String text) {
            //validate();
        }
    };

    CompoundButton.OnCheckedChangeListener listenerCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                ll_address.setVisibility(View.GONE);
                updateFields(false);
            }else{
                ll_address.setVisibility(View.VISIBLE);
            }
        }
    };
    private void fillData() {
        FCReferenciasRequest fcReferenciasRequest = SessionApp.getInstance().getFcRequestDTO().getReferencias();
        if (fcReferenciasRequest != null ) {
            if (fcReferenciasRequest.getReferencias() != null && !fcReferenciasRequest.getReferencias().isEmpty()) {
                referencia1 = fcReferenciasRequest.getReferencias().get(0);
            }
        }

        FCBeneficiariosRequest fcBeneficiariosRequest = SessionApp.getInstance().getFcRequestDTO().getBeneficiarios();
        if (fcBeneficiariosRequest != null ){
            if ( fcBeneficiariosRequest.getBeneficiarios() != null && !fcBeneficiariosRequest.getBeneficiarios().isEmpty()) {
                 beneficiario = fcBeneficiariosRequest.beneficiarios.get(0);

                campos.get(0).setText(beneficiario.getNombre());
                campos.get(1).setText(beneficiario.getApellidoPaterno());
                campos.get(2).setText(beneficiario.getApellidoMaterno());
                campos.get(4).setText(beneficiario.getFechaNacimiento());
                for (int i = 0; i < relationship.size(); i++) {
                    if (relationship.get(i).getValue().equalsIgnoreCase(beneficiario.getIdParentesco())) {
                        campos.get(5).setText(relationship.get(i).getName());
                        campos.get(5).setTag(relationship.get(i).getValue());
                    }
                }
                if (beneficiario.getDireccion() != null){
                    check_enable_address.setOnCheckedChangeListener(null);
                    check_enable_address.setChecked(false);
                    check_enable_address.setOnCheckedChangeListener(listenerCheck);
                    DHBeneficiarioDireccion direccion = beneficiario.getDireccion();
                    campos.get(6).setText(direccion.getCalle());
                    campos.get(7).setText(direccion.getNum_interior());
                    campos.get(8).setText(direccion.getNum_exterior());
                   // campos.get(9).setText(direccion.getEstado());
                   // campos.get(9).setTag(direccion.getIdEntidad());
                   // campos.get(10).setText(direccion.getIdMunicipio());
                   // campos.get(11).setText("");
                    campos.get(12).setText(direccion.getCp());
                    campos.get(13).setText(direccion.getColonia());
                }
            }

        }
    }


    private void validate() {

        String mVal = campos.get(10).getText();
        String mTag = campos.get(10).getTag();
        String pTag = campos.get(9).getTag();
        campos.get(9).setTextChanged(null);
        campos.get(10).setTextChanged(null);

        campos.get(0).setRequired(true);
        campos.get(1).setRequired(true);
        campos.get(2).setRequired(true);
        campos.get(3).setRequired(false);
        campos.get(4).setRequired(true);
        campos.get(5).setRequired(true);

        if (!check_enable_address.isChecked()){
            updateFields(true);
        }

        campos.get(7).setRequired(false);
        campos.get(11).setRequired(false);

        campos.get(9).setTag(pTag);
        campos.get(9).setTextChanged(new FCEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                setMunicipios(campos.get(9).getTag());
                // validate();
            }
        });

        campos.get(10).setTag(mTag);
        campos.get(10).setText(mVal);
        campos.get(10).setTextChanged(validate);

        int index = 0;
        for (FCEditText item: campos){
            if(!item.isValid()){
                isLoading = false;
                getContext().alert("Completa los campos obligatorios");
                return;
            }
            index++;
        }

        getContextMenu().getTokenFC((String... data) -> {
            if (data != null) {
                postBeneficiarios(data[0]);
            }else{
                isLoading = false;
            }
        });
    }
    private void updateFields(boolean required) {
        for (int i = 6; i<campos.size();i++){
            campos.get(i).setRequired(required);
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void setList() {

        ArrayList<ModelItem> genero = new ArrayList();
        ArrayList<ModelItem> estado = new ArrayList();
        ArrayList<ModelItem> pais = new ArrayList();

        for(Genero gen : SessionApp.getInstance().getCatalog().getGenero())
            genero.add(new ModelItem(gen.getName(), gen.getValue()));

        for(Parentesco relation : SessionApp.getInstance().getCatalog().getParentesco())
            relationship.add(new ModelItem(relation.getDescripcion(), relation.getIdParentesco()));

        for(LugarNacimiento est : SessionApp.getInstance().getCatalog().getLugarNacimiento())
            estado.add(new ModelItem(est.getName(), est.getValue()));

        pais.add(new ModelItem("Mexico", "484"));

        campos.get(3).setSpinner(genero);
        campos.get(5).setSpinner(relationship);
        campos.get(9).setSpinner(estado);
        setMunicipios("");
        campos.get(14).setSpinner(pais);
        campos.get(14).setText("Mexico");

    }

    public void setMunicipios(String data) {

        ArrayList<ModelItem> municipio = new ArrayList();

        if(!TextUtils.isEmpty(data)) {
            for (Municipio mun : SessionApp.getInstance().getCatalog().getMunicipio())
                if (mun.getValue().substring(0, data.length()).equals(data))
                    municipio.add(new ModelItem(mun.getName(), mun.getValue()));
        }
        campos.get(10).setText("");
        campos.get(10).setSpinner(municipio);

    }

    private void postBeneficiarios(String token){
        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCBeneficiariosRequest fcBeneficiariosRequest = new FCBeneficiariosRequest();
        fcBeneficiariosRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcBeneficiariosRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcBeneficiariosRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcBeneficiariosRequest.setBeneficiarios(getBeneficiarios());
        Logger.d("REQUEST : "  + new Gson().toJson(fcBeneficiariosRequest));


        Beneficiarios beneficiariosWS = new Beneficiarios(context);
        beneficiariosWS.postBeneficiarios(fcBeneficiariosRequest, new Beneficiarios.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCBeneficiariosResponse response = (FCBeneficiariosResponse) Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    context.alert(response.getRespuesta().getDescripcion().get(0));
                    isLoading = false;
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.CATORCE_15);

                    postReferencias(token, fcBeneficiariosRequest.getBeneficiarios().get(0));

                }

                context.loading(false);
            }
            @Override
            public void onFailure(String mensaje) {
                context.loading(false);

                Logger.d("RESPONSE : "  + mensaje);

                context.alert(mensaje);
                isLoading = false;

            }
        });
    }

    public ArrayList<DHBeneficiario> getBeneficiarios() {

        ArrayList<DHBeneficiario> beneficiarios = new ArrayList();

        DHBeneficiario beneficiario = new DHBeneficiario();

        beneficiario.setNombre(campos.get(0).getText());
        beneficiario.setApellidoPaterno(campos.get(1).getText());
        beneficiario.setApellidoMaterno(campos.get(2).getText());
        beneficiario.setTelefono("5500000000");
        beneficiario.setFechaNacimiento(campos.get(4).getText());
        beneficiario.setIdParentesco(campos.get(5).getTag());
        beneficiario.setPorcentaje(100);
        beneficiario.setDireccion_activa(1);
        if(this.beneficiario != null){
            beneficiario.setIdBeneficiario(this.beneficiario.getIdBeneficiario());
        }

        if(check_enable_address.isChecked()){
            FCIdentificacionRequest data =  SessionApp.getInstance().getFcRequestDTO().getIdentificacion();
            DHBeneficiarioDireccion direccion = new DHBeneficiarioDireccion();
            direccion.setCalle(data.getCalle());
            direccion.setNum_interior(data.getNumInterior());
            direccion.setNum_exterior(data.getNumExterior());
            direccion.setEstado(data.getEstado());
            direccion.setMunicipio(data.getMunicipio());
            direccion.setId_poblacion("03709");
            direccion.setCp(data.getCp());
            direccion.setColonia(data.getColonia());
            direccion.setEntidad(data.getEntidad());
            direccion.setLocalidad(data.getLocalidad());
            direccion.setManzana(data.getManzana());
            direccion.setLote(data.getLote());
            direccion.setAntiguedad(data.getAntiguedad());
            direccion.setCalle_1(data.getCalle_1());
            direccion.setCalle_2(data.getCalle_2());
            beneficiario.setDireccion(direccion);
        }else{
            DHBeneficiarioDireccion direccion = new DHBeneficiarioDireccion();
            direccion.setCalle(campos.get(6).getText());
            direccion.setNum_interior(campos.get(7).getText());
            direccion.setNum_exterior(campos.get(8).getText());
            direccion.setEstado(campos.get(9).getText());
            direccion.setMunicipio(campos.get(10).getTag());
            direccion.setId_poblacion("03709");
            direccion.setCp(campos.get(12).getText());
            direccion.setColonia(campos.get(13).getText());
            direccion.setEntidad(campos.get(9).getTag());
            direccion.setLocalidad(SessionApp.getInstance().getCatalog().getLocalidad().get(0).getValue());
            direccion.setManzana("NA");
            direccion.setLote("NA");
            direccion.setAntiguedad("01/01/2020");
            direccion.setCalle_1("NA");
            direccion.setCalle_2("NA");
            beneficiario.setDireccion(direccion);
        }
        beneficiarios.add(beneficiario);


        return beneficiarios;
    }

    public void postReferencias(String token, DHBeneficiario beneficiario){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        ArrayList<DHReferencia> referencias = new ArrayList();

        referencia1.setApellidoPaterno(beneficiario.getApellidoPaterno());
        referencia1.setApellidoMaterno(beneficiario.getApellidoMaterno());
        referencia1.setNombre(beneficiario.getNombre());
        referencia1.setTelefono(beneficiario.getTelefono());
        referencia1.setIdParentesco("CO");
        referencias.add(referencia1);

        FCReferenciasRequest fcReferenciasRequest = new FCReferenciasRequest();

        fcReferenciasRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcReferenciasRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcReferenciasRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcReferenciasRequest.setReferencias(referencias);
        Logger.d("REQUEST : "  + new Gson().toJson(fcReferenciasRequest));

        SessionApp.getInstance().getFcRequestDTO().setReferencias(fcReferenciasRequest);

        Referencias referencia = new Referencias(getContext());
        referencia.postReferencias(fcReferenciasRequest, new Referencias.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCReferenciasResponse response = (FCReferenciasResponse) e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {

                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                     getContext().setFragment(Fragment_fincomun_codigo.newInstance());


                }
                isLoading = false;

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });

    }
}