package com.blm.qiubopay.modules.fincomun.prestamo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.fincomun.Perentesco;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcIdentification;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcPersonalData;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBeneficiariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosPersonalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCValidaSMSRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCOfertaSeleccionadaCreditoResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBeneficiariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCReferenciasResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSMSResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCValidaSMSResponse;
import mx.com.fincomun.origilib.Model.Originacion.Beneficiarios;
import mx.com.fincomun.origilib.Model.Originacion.Credito.OfertaSeleccionadaCredito;
import mx.com.fincomun.origilib.Model.Originacion.Referencias;
import mx.com.fincomun.origilib.Model.Originacion.SMS;
import mx.com.fincomun.origilib.Objects.Catalogos.Parentesco;
import mx.com.fincomun.origilib.Objects.Credito.DHOferta;
import mx.com.fincomun.origilib.Objects.DHBeneficiario;
import mx.com.fincomun.origilib.Objects.Referencia.DHReferencia;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_prestamos_solicitud_fincomun_7 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;
    private Button btn_save;
    private Button btn_next;
    private Button add_new;
    private ITextChanged iTextChanged;

    private LinearLayout layout_beneficiario_2, layout_beneficiario_3, layout_beneficiario_4;

    public static FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();
    public static FCDatosPersonalesRequest fcDatosPersonalesRequest = new FCDatosPersonalesRequest();

    public static Fragment_prestamos_solicitud_fincomun_7 newInstance(Object... data) {
        Fragment_prestamos_solicitud_fincomun_7 fragment = new Fragment_prestamos_solicitud_fincomun_7();
        Bundle args = new Bundle();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_solicitud_fincomun_14"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_prestamos_solicitud_fincomun_7, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        campos = new ArrayList();

        iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {
                context.hideKeyboard();
            }
        };

        layout_beneficiario_2 = view.findViewById(R.id.layout_beneficiario_2);
        layout_beneficiario_3 = view.findViewById(R.id.layout_beneficiario_3);
        layout_beneficiario_4 = view.findViewById(R.id.layout_beneficiario_4);

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_name),
                true, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_name)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_last_name),
                true, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_last_name)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_mother_last_name),
                true, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_mother_last_name)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_relationship),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_relationship)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_phone),
                true, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_phone)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_porcentaje),
                false, 3, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_birthday),
                true, 15, 0, HEditText.Tipo.NONE,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje)));


        LinearLayout layout_fecha = view.findViewById(R.id.layout_fecha);
        layout_fecha.setOnClickListener(view -> {
            context.showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(6).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_fecha_2 = view.findViewById(R.id.layout_fecha_2);
        layout_fecha_2.setOnClickListener(view -> {
            context.showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(13).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_fecha_3 = view.findViewById(R.id.layout_fecha_3);
        layout_fecha_3.setOnClickListener(view -> {
            context.showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(20).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_fecha_4 = view.findViewById(R.id.layout_fecha_4);
        layout_fecha_4.setOnClickListener(view -> {
            context.showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(27).getEditText().setText(data[0].toString());
                }
            });
        });

        add_new = view.findViewById(R.id.add_new);
        btn_next = view.findViewById(R.id.btn_next);
        btn_save = view.findViewById(R.id.btn_save);

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(layout_beneficiario_2.getVisibility() == View.GONE){
                    layout_beneficiario_2.setVisibility(View.VISIBLE);
                    config();
                    validate();
                    return;
                }

                if(layout_beneficiario_3.getVisibility() == View.GONE){
                    layout_beneficiario_3.setVisibility(View.VISIBLE);
                    config();
                    validate();
                    return;
                }

                if(layout_beneficiario_4.getVisibility() == View.GONE){
                    layout_beneficiario_4.setVisibility(View.VISIBLE);
                    config();
                    validate();
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int porcentaje = 0;

                for (DHBeneficiario ben : getBeneficiarios())
                    porcentaje += ben.getPorcentaje();

                Logger.d("% " + porcentaje);

                if(porcentaje != 100) {
                    context.alert("El porcentaje especificado debe ser 100%");
                    context.loading(false);
                    return;
                }

                context.getTokenFC((String... data) -> {
                    setInfo(data[0]);
                });
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.CATORCE);
                AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                context.alert("Datos Guardados");

            }
        });

        ArrayList<ModelSpinner> parentesco = new ArrayList();

        for(Parentesco pae : SessionApp.getInstance().getCatalog().getParentesco())
            parentesco.add(new ModelSpinner(pae.getDescripcion(), pae.getIdParentesco()));

        EditSpinner spparentesco = view.findViewById(R.id.edt_relationship);
        spparentesco.setLines(3);
        spparentesco.setSingleLine(false);

        EditSpinner spparentesco2 = view.findViewById(R.id.edt_relationship_2);
        spparentesco2.setLines(3);
        spparentesco2.setSingleLine(false);

        EditSpinner spparentesco3 = view.findViewById(R.id.edt_relationship_3);
        spparentesco3.setLines(3);
        spparentesco3.setSingleLine(false);

        EditSpinner spparentesco4 = view.findViewById(R.id.edt_relationship_4);
        spparentesco4.setLines(3);
        spparentesco4.setSingleLine(false);

        context.setDataSpinner(spparentesco, parentesco, data -> campos.get(3).setIdentifier(data[0]));
        context.setDataSpinner(spparentesco2, parentesco, data -> campos.get(10).setIdentifier(data[0]));
        context.setDataSpinner(spparentesco3, parentesco, data -> campos.get(19).setIdentifier(data[0]));
        context.setDataSpinner(spparentesco4, parentesco, data -> campos.get(26).setIdentifier(data[0]));

        validate();
    }

    public void config() {



        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_name_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_name_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_last_name_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_last_name_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_mother_last_name_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_mother_last_name_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_relationship_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_relationship_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_phone_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_phone_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_porcentaje_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 3, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_2)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_birthday_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 15, 0, HEditText.Tipo.NONE,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_2)));


        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_name_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_name_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_last_name_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_last_name_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_mother_last_name_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_mother_last_name_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_relationship_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_relationship_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_phone_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_phone_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_porcentaje_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 3, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_3)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_birthday_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 15, 0, HEditText.Tipo.NONE,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_3)));


        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_name_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_name_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_last_name_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_last_name_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_mother_last_name_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_mother_last_name_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_relationship_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_relationship_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_phone_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_phone_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_porcentaje_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 3, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_4)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edt_birthday_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 15, 0, HEditText.Tipo.NONE,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_porcentaje_4)));


    }

    public ArrayList<DHBeneficiario> getBeneficiarios() {

        ArrayList<DHBeneficiario> beneficiarios = new ArrayList();

        DHBeneficiario beneficiario = new DHBeneficiario();

        beneficiario.setApellidoPaterno(campos.get(1).getText());
        beneficiario.setApellidoMaterno(campos.get(2).getText());
        beneficiario.setNombre(campos.get(0).getText());
        beneficiario.setTelefono(campos.get(4).getText());
        beneficiario.setIdParentesco(campos.get(3).getIdentifier());
        beneficiario.setPorcentaje(100);
        beneficiario.setFechaNacimiento(campos.get(6).getText());

        beneficiarios.add(beneficiario);

        if(layout_beneficiario_2.getVisibility() == View.VISIBLE){

            DHBeneficiario beneficiario2 = new DHBeneficiario();

            beneficiario2.setApellidoPaterno(campos.get(8).getText());
            beneficiario2.setApellidoMaterno(campos.get(9).getText());
            beneficiario2.setNombre(campos.get(7).getText());
            beneficiario2.setTelefono(campos.get(11).getText());
            beneficiario2.setIdParentesco(campos.get(10).getIdentifier());
            beneficiario2.setPorcentaje(campos.get(12).getTextInt());
            //beneficiario.setIdBeneficiario();
            beneficiario2.setFechaNacimiento(campos.get(13).getText());

            beneficiarios.add(beneficiario2);

        }

        if(layout_beneficiario_3.getVisibility() == View.VISIBLE){

            DHBeneficiario beneficiario3 = new DHBeneficiario();

            beneficiario3.setApellidoPaterno(campos.get(15).getText());
            beneficiario3.setApellidoMaterno(campos.get(16).getText());
            beneficiario3.setNombre(campos.get(14).getText());
            beneficiario3.setTelefono(campos.get(18).getText());
            beneficiario3.setIdParentesco(campos.get(17).getIdentifier());
            beneficiario3.setPorcentaje(campos.get(19).getTextInt());
            //beneficiario.setIdBeneficiario();
            beneficiario3.setFechaNacimiento(campos.get(20).getText());

            beneficiarios.add(beneficiario3);

        }

        if(layout_beneficiario_4.getVisibility() == View.VISIBLE){

            DHBeneficiario beneficiario4 = new DHBeneficiario();

            beneficiario4.setApellidoPaterno(campos.get(22).getText());
            beneficiario4.setApellidoMaterno(campos.get(23).getText());
            beneficiario4.setNombre(campos.get(21).getText());
            beneficiario4.setTelefono(campos.get(25).getText());
            beneficiario4.setIdParentesco(campos.get(24).getIdentifier());
            beneficiario4.setPorcentaje(campos.get(26).getTextInt());
            //beneficiario.setIdBeneficiario();
            beneficiario4.setFechaNacimiento(campos.get(27).getText());

            beneficiarios.add(beneficiario4);

        }

        return beneficiarios;
    }

    public void setInfo(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCBeneficiariosRequest fcBeneficiariosRequest = new FCBeneficiariosRequest();
        fcBeneficiariosRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcBeneficiariosRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcBeneficiariosRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcBeneficiariosRequest.setBeneficiarios(getBeneficiarios());
        Logger.d("REQUEST : "  + new Gson().toJson(fcBeneficiariosRequest));

        //SessionApp.getInstance().getFcRequestDTO().setReferencias(fcReferenciasRequest);

        Beneficiarios beneficiariosWS = new Beneficiarios(context);
        beneficiariosWS.postBeneficiarios(fcBeneficiariosRequest, new Beneficiarios.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCBeneficiariosResponse response = (FCBeneficiariosResponse) Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    context.alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.CATORCE_15);
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    postReferencias(token, fcBeneficiariosRequest.getBeneficiarios().get(0));

                }

                context.loading(false);
            }
            @Override
            public void onFailure(String mensaje) {
                context.loading(false);

                Logger.d("RESPONSE : "  + mensaje);

                context.alert(mensaje);
            }
        });

    }

    public void validate(){

        btn_save.setEnabled(false);
        btn_next.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_save.setEnabled(true);
        btn_next.setEnabled(true);


    }


    public void postReferencias(String token, DHBeneficiario beneficiario){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        ArrayList<DHReferencia> referencias = new ArrayList();
        DHReferencia referencia1 = new DHReferencia();

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
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    sendSMS(token);
                }


            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });

    }

    public void sendSMS(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCSMSRequest fcsmsRequest = new FCSMSRequest();
        fcsmsRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcsmsRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcsmsRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcsmsRequest.setTipo_aplicacion(2);

        SessionApp.getInstance().getFcRequestDTO().setSms(fcsmsRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcsmsRequest));

        SMS sms = new SMS(getContext());
        sms.envioSMS(fcsmsRequest, new SMS.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                try {

                    FCSMSResponse response = (FCSMSResponse) e;

                    Logger.d("RESPONSE : "  + new Gson().toJson(response));

                    if(response.getRespuesta().getCodigo() < 0) {
                        getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    } else {
                        SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.QUINCE);
                        SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                        SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                        AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                        initAlert();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    getContext().alert("", new IAlertButton() {
                        @Override
                        public String onText() {
                            return null;
                        }

                        @Override
                        public void onClick() {
                            context.initHome();
                        }
                    });
                }

                getContext().loading(false);

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });
    }

    public void initAlert() {
        //VERIFICAR
        context.showAlertSMS(new IClickView() {
            @Override
            public void onClick(Object... data) {

                String code = (String) data[0];

                if(!code.isEmpty()) {
                    context.getTokenFC((String... text) -> {
                        validateSMS(text[0], (String) data[0]);
                    });
                } else {
                    getContext().alert("Token no válido", "Favor de ingresar un token válido", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Continuar";
                        }

                        @Override
                        public void onClick() {
                            initAlert();

                        }
                    });

                }
            }
        }, new IClickView() {
            @Override
            public void onClick(Object... data) {
                context.getTokenFC((String... text) -> {
                    sendSMS(text[0]);
                });
            }
        });

    }

    public void validateSMS(String token, String code) {

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCValidaSMSRequest fcValidaSMSRequest = new FCValidaSMSRequest();
        fcValidaSMSRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcValidaSMSRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcValidaSMSRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcValidaSMSRequest.setToken(code);
        fcValidaSMSRequest.setTipo_aplicacion(2);

        Logger.d("REQUEST : "  + new Gson().toJson(fcValidaSMSRequest));

        getContext().loading(true);

        SMS sms = new SMS(getContext());
        sms.validaSMS(fcValidaSMSRequest, new SMS.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCValidaSMSResponse response = (FCValidaSMSResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    getContext().loading(false);
                } else {

                    SessionApp.getInstance().getFcRequestDTO().setValidSMS(1);
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());


                    DHOferta oferta = new DHOferta();
                    oferta.setPlazo(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getNumPagos()));
                    oferta.setOferta(Double.valueOf(SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo() + ""));
                    oferta.setFrecuencia(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getFrecuenciaPago()));
                    oferta.setCuota(SessionApp.getInstance().getFcRequestDTO().getSimulador().getCuota());
                    oferta.setTasa(SessionApp.getInstance().getFcRequestDTO().getTasa());

                    selctOferta(token, oferta);

                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });
    }

    public void selctOferta(String token, DHOferta oferta) {

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCOfertaSeleccionadaCreditoRequest fcOfertaSeleccionadaCreditoRequest = new FCOfertaSeleccionadaCreditoRequest();

        fcOfertaSeleccionadaCreditoRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcOfertaSeleccionadaCreditoRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcOfertaSeleccionadaCreditoRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcOfertaSeleccionadaCreditoRequest.tieneSeguro = (SessionApp.getInstance().getFcRequestDTO().isMedico() ? 1 : 0);
        fcOfertaSeleccionadaCreditoRequest.setOferta(oferta);

        Logger.d("REQUEST : "  + new Gson().toJson(fcOfertaSeleccionadaCreditoRequest));

        OfertaSeleccionadaCredito ofertaSeleccionadaCredito = new OfertaSeleccionadaCredito(getContext());
        ofertaSeleccionadaCredito.postOfertaSeleccionadaCredito(fcOfertaSeleccionadaCreditoRequest, new OfertaSeleccionadaCredito.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                Logger.d("RESPONSE : "  + e);

                FCOfertaSeleccionadaCreditoResponse response = (FCOfertaSeleccionadaCreditoResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {

                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.DIECISEIS);
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    saveRecordData();
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });

    }

    public void saveRecordData() {

        Gson gson = new Gson();

        QPAY_UserFcIdentification identification = new QPAY_UserFcIdentification();
        identification = gson.fromJson(gson.toJson(fcIdentificacionRequest), QPAY_UserFcIdentification.class);

        RegisterActivity.createUserFcIdentification(getContext(), identification, new IFunction<QPAY_BaseResponse>() {
            @Override
            public void execute(QPAY_BaseResponse... data) {

                QPAY_UserFcPersonalData personal = new QPAY_UserFcPersonalData();
                personal = gson.fromJson(gson.toJson(fcDatosPersonalesRequest), QPAY_UserFcPersonalData.class);

                RegisterActivity.createUserPersonalData(getContext(), personal, new IFunction<QPAY_BaseResponse>() {
                    @Override
                    public void execute(QPAY_BaseResponse... data) {
                        fcIdentificacionRequest = null;
                        fcDatosPersonalesRequest = null;
                        context.loading(false);
                        getContext().setFragment(Fragment_prestamos_solicitud_fincomun_6.newInstance());
                    }
                });
            }
        });

    }



}
