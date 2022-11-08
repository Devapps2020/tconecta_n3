package com.blm.qiubopay.modules.fincomun.prestamo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.math.BigInteger;
import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSimuladorResponse;
import mx.com.fincomun.origilib.Model.Originacion.Simulador;
import mx.com.fincomun.origilib.Objects.Catalogos.Destino;
import mx.com.fincomun.origilib.Objects.Catalogos.Matriz;
import mx.com.fincomun.origilib.Objects.Catalogos.ModCredito;
import mx.com.fincomun.origilib.Objects.Catalogos.ProductoCredito;
import mx.com.fincomun.origilib.Objects.Catalogos.TipoCredito;
import mx.com.fincomun.origilib.Objects.DHOfertaBimbo;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_prestamos_solicitud_fincomun_1 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;
    private Object data;

    private EditSpinner spnumeropagos;
    private EditSpinner spfrecuenciapagos;

    private TextView label_cuota_con_medico, label_cuota_sin_medico;
    private Button btn_solicita_con_medico, btn_solicita_sin_medico;

    Double monto_maximo = 0.0;
    FCSimuladorRequest simuladorRequest= new FCSimuladorRequest();

    LinearLayout layout_images, layout_con_medico;

    public static Fragment_prestamos_solicitud_fincomun_1 newInstance(Object... data) {
        Fragment_prestamos_solicitud_fincomun_1 fragment = new Fragment_prestamos_solicitud_fincomun_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_prestamos_solicitud_fincomun_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_FINCOMUN_Simulador_credito);

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);
        Double tasa_seguro = Double.parseDouble(ofertaBimbo.getTasa_con_medico_en_casa()) * 100;
        Double tasa_sin_seguro = Double.parseDouble(ofertaBimbo.getTasa_sin_medico_en_casa()) * 100;

        layout_images = getView().findViewById(R.id.layout_images);
        layout_con_medico = getView().findViewById(R.id.layout_con_medico);

        label_cuota_con_medico = getView().findViewById(R.id.label_cuota_con_medico);
        label_cuota_sin_medico = getView().findViewById(R.id.label_cuota_sin_medico);

        btn_solicita_con_medico = getView().findViewById(R.id.btn_solicita_con_medico);
        btn_solicita_sin_medico = getView().findViewById(R.id.btn_solicita_sin_medico);

        campos = new ArrayList();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_credit_type),
                true, 25, 1, HEditText.Tipo.NONE, new ITextChanged() {
            @Override
            public void onChange() {

            }
            @Override
            public void onMaxLength() {
            }
        }, (TextView) getView().findViewById(R.id.text_error_credit_type)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_credit_mod),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_credit_mod)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_product),
                true, 100, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_product)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_how_much_money),
                true, 11, 1, HEditText.Tipo.MONEDA_SC,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_how_much_money)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_how_income),
                false, 11, 1, HEditText.Tipo.MONEDA_SC,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_how_income)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_frecuencia),
                true, 25, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_frecuencia)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_destination),
                true, 100, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_destination)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_number_payments),
                true, 25, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_number_payments)));

        btn_solicita_con_medico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionApp.getInstance().getFcRequestDTO().setTasa(tasa_seguro);

                simuladorRequest.setCuota(Math.ceil(getCuota(true)));

                Double monto = Double.parseDouble((campos.get(3).getTextDecimal()));
                if(monto < 1500.0) {
                    getContext().alert("El monto ingresado no puede ser menor\na $1,500.00", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            setMontoMinimo();


                        }
                    });
                    return;
                }

                SessionApp.getInstance().getFcRequestDTO().setMedico(true);

                sendInfo();

            }
        });

        btn_solicita_sin_medico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionApp.getInstance().getFcRequestDTO().setTasa(tasa_sin_seguro);

                simuladorRequest.setCuota(Math.ceil(getCuota(false)));

                Double monto = Double.parseDouble((campos.get(3).getTextDecimal()));
                if(monto < 1500.0) {

                    getContext().alert("El monto ingresado no puede ser menor\na $1,500.00", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            setMontoMinimo();


                        }
                    });
                    return;
                }

                SessionApp.getInstance().getFcRequestDTO().setMedico(false);
                sendInfo();

            }
        });

        if(tasa_seguro == 0.0) {
            layout_images.setVisibility(View.GONE);
            layout_con_medico.setVisibility(View.GONE);
        }

        setList();

        setMontoSeleccionado();

        validate();
    }

    public void setMontoSeleccionado() {

        if(SessionApp.getInstance().getFcConsultaOfertaBimboResponse() != null) {

            monto_maximo = Double.parseDouble(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());

            campos.get(3).setTipo(HEditText.Tipo.NONE);
            campos.get(3).setText(Utils.paserCurrency(monto_maximo + ""));
            campos.get(3).getEditText().setTag(campos.get(3).getTextDecimal());
            campos.get(3).setTipo(HEditText.Tipo.MONEDA_SC);
        }

    }

    public void setMontoMinimo() {

        Double monto = 1500.0;
        campos.get(3).setTipo(HEditText.Tipo.NONE);
        campos.get(3).setText(Utils.paserCurrency(monto + ""));
        campos.get(3).getEditText().setTag(campos.get(3).getTextDecimal());
        campos.get(3).setTipo(HEditText.Tipo.MONEDA_SC);

    }

    public void sendInfo(){

        simuladorRequest.setIdTipoCredito(campos.get(0).getIdentifierInt());
        simuladorRequest.setIdModCredito(campos.get(1).getIdentifierInt());
        simuladorRequest.setIdTipoProducto(campos.get(2).getIdentifierInt());
        simuladorRequest.setMontoPrestamo(BigInteger.valueOf(Integer.parseInt(campos.get(3).getTextDecimal().replace(".00", ""))));
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos(campos.get(7).getIdentifier());
        simuladorRequest.setDestino(campos.get(6).getIdentifier());
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago(campos.get(5).getIdentifier());
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);

        getContextMenu().getTokenFC((String... data) -> {

            setPLD();

            simuladorRequest.setTokenJwt(data[0]);
            simulador(simuladorRequest);
        });
    }

    public void setPLD() {

        double cuota = Math.ceil(getCuota(SessionApp.getInstance().getFcRequestDTO().isMedico()));
        double monto = Double.parseDouble(simuladorRequest.getMontoPrestamo() + "");

        if(cuota <= 5000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[0] = Utils.paserCurrency("5000");

        if(cuota > 5000 && cuota <= 10000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[0] = Utils.paserCurrency("10000");

        if(cuota > 10000 && cuota <= 20000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[0] = Utils.paserCurrency("20000");

        switch (Integer.parseInt(simuladorRequest.getFrecuenciaPago())) {
            case 7:
            case 14:
            case 15:
            case 30:
                Fragment_prestamos_solicitud_fincomun_6.respuestas[1] = "5";
                break;
        }

        if(monto <= 5000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[2] = Utils.paserCurrency("5000");

        if(monto > 5000 && cuota <= 10000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[2] = Utils.paserCurrency("10000");

        if(monto > 10000 && cuota <= 20000)
            Fragment_prestamos_solicitud_fincomun_6.respuestas[2] = Utils.paserCurrency("20000");



        Fragment_prestamos_solicitud_fincomun_6.respuestas[3] = "5";
        Fragment_prestamos_solicitud_fincomun_6.respuestas[4] = "TIENDA DE ABARROTES Y MISCELANEA";

    }

    private void simulador(FCSimuladorRequest request) {

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        Simulador simulador = new Simulador(getContext());
        simulador.postSimulador(request, new Simulador.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                FCSimuladorResponse response = (FCSimuladorResponse)Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.UNO);
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                    getContext().setFragment(Fragment_prestamos_solicitud_fincomun_4.newInstance());
                }

                getContext().loading(false);
            }

            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    public void validate(){

        btn_solicita_con_medico.setEnabled(false);
        btn_solicita_sin_medico.setEnabled(false);

        setMonto();

        if(monto_maximo > 0 ) {
            try {

                Double monto = Double.parseDouble((campos.get(3).getTextDecimal()));

                if(monto > monto_maximo) {
                    getContext().alert("El monto ingresado no puede ser mayor\nal monto autorizado", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            setMontoSeleccionado();


                        }
                    });
                    return;
                }

            } catch (Exception ex) { }

        }

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_solicita_con_medico.setEnabled(true);
        btn_solicita_sin_medico.setEnabled(true);

    }

    public void setMonto() {

        if(!campos.get(3).getText().isEmpty() && !campos.get(7).getText().isEmpty()) {
            label_cuota_con_medico.setText(Utils.paserCurrency( Math.ceil(getCuota(true)) + ""));
            label_cuota_sin_medico.setText(Utils.paserCurrency( Math.ceil(getCuota(false)) + ""));
        } else {
            label_cuota_con_medico.setText("$0.00");
            label_cuota_sin_medico.setText("$0.00");
        }

    }

    private void setList() {

        EditSpinner sptipocredito = getView().findViewById(R.id.edt_credit_type);
        EditSpinner speligeproducto = getView().findViewById(R.id.edt_product);
        EditSpinner sptmodcredito = getView().findViewById(R.id.edt_credit_mod);
        EditSpinner spdestino = getView().findViewById(R.id.edt_destination);

        spnumeropagos = getView().findViewById(R.id.edt_number_payments);
        spfrecuenciapagos = getView().findViewById(R.id.edt_frecuencia);

        sptipocredito.setLines(3);
        sptipocredito.setSingleLine(false);
        speligeproducto.setLines(3);
        speligeproducto.setSingleLine(false);
        spdestino.setLines(3);
        spdestino.setSingleLine(false);
        sptmodcredito.setLines(3);
        sptmodcredito.setSingleLine(false);

        spnumeropagos.setLines(3);
        spnumeropagos.setSingleLine(false);
        spfrecuenciapagos.setLines(3);
        spfrecuenciapagos.setSingleLine(false);

        ArrayList<ModelSpinner> tipocreditos = new ArrayList();
        ArrayList<ModelSpinner> eligeproductos = new ArrayList();
        ArrayList<ModelSpinner> destinos = new ArrayList();
        ArrayList<ModelSpinner> modocreditos = new ArrayList();

        for(TipoCredito tip : SessionApp.getInstance().getCatalog().getTipoCredito())
            if("2".equals("" + tip.getIdTipoProducto()))
                tipocreditos.add(new ModelSpinner(tip.getNombreTipoCredito(), "" + tip.getIdTipoProducto()));

        for(ProductoCredito pro : SessionApp.getInstance().getCatalog().getProductoCredito())
            if("2".equals("" + pro.getIdProductoCredito()))
                eligeproductos.add(new ModelSpinner(pro.getNombreProducto(), "" + pro.getIdProductoCredito()));

        for(ModCredito mod : SessionApp.getInstance().getCatalog().getModCredito())
            if("6".equals("" + mod.getIdModCredito()))
                modocreditos.add(new ModelSpinner(mod.getNombreModCredito(), "" + mod.getIdModCredito()));

        for(Destino des : SessionApp.getInstance().getCatalog().getDestino())
            if("311".equals("" + des.getValue()))
                destinos.add(new ModelSpinner(des.getName(), "" + des.getValue()));
            //VERIFICAR

        getContextMenu().setDataSpinner(sptipocredito, tipocreditos, data -> {
            campos.get(0).setIdentifier(data[0]);
            setListPagos(getMatriz(campos.get(0).getIdentifierInt(), campos.get(1).getIdentifierInt()));
        });

        getContextMenu().setDataSpinner(speligeproducto, eligeproductos, data -> campos.get(2).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(spnumeropagos, new ArrayList(), data -> campos.get(5).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(spfrecuenciapagos, new ArrayList(), data -> campos.get(7).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(spdestino, destinos, data -> campos.get(6).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(sptmodcredito, modocreditos, data -> {
            campos.get(1).setIdentifier(data[0]);
            setListPagos(getMatriz(campos.get(0).getIdentifierInt(), campos.get(1).getIdentifierInt()));
        });

        speligeproducto.selectItem(0);
        sptmodcredito.selectItem(0);
        sptipocredito.selectItem(0);
        spdestino.selectItem(0);

        campos.get(0).setIdentifier(tipocreditos.get(0).getValue());
        campos.get(1).setIdentifier(modocreditos.get(0).getValue());
        campos.get(2).setIdentifier(eligeproductos.get(0).getValue());
        campos.get(6).setIdentifier(destinos.get(0).getValue());

        setListPagos(getMatriz(campos.get(0).getIdentifierInt(), campos.get(1).getIdentifierInt()));

    }

    public static Matriz getMatriz(Integer tipoCreditoId, Integer tipoModId) {

        for(Matriz mat : SessionApp.getInstance().getCatalog().getMatriz())
            if(mat.getIdTipoProducto() == tipoCreditoId && mat.getIdModCredito() == tipoModId )
                return mat;

        return null;
    }

    public void setListPagos(Matriz matriz) {

        ArrayList<ModelSpinner> frecuenciapagos = new ArrayList();

        if(matriz != null) {
            for(String fre : matriz.getFrecuenciaPago()) {
                try {
                    if(Integer.parseInt(fre) != 28)
                        frecuenciapagos.add(new ModelSpinner(fre + " días", fre));
                } catch (Exception ex) { }
            }
        }

       getContextMenu().setDataSpinner(spfrecuenciapagos, frecuenciapagos, new IFunction<String>() {
            @Override
            public void execute(String... data2) {
                campos.get(7).setText("");
                campos.get(5).setIdentifier(data2[0]);
                setPlazo();
            }
        });

        spfrecuenciapagos.selectItem(2);
        campos.get(5).setIdentifier("15");

        int numero = setPlazo();

        spnumeropagos.selectItem(numero - 1);
        campos.get(7).setIdentifier("18");

    }

    public Integer setPlazo() {

        ArrayList<ModelSpinner> numeropagos = new ArrayList();

        String name = "";

        int plazo = 1;
        switch (campos.get(5).getIdentifierInt()) {
            case 7:
                plazo = 36;
                name = " semanas";
                break;
            case 14:
                plazo = 18;
                name = " catorcenas";
                break;
            case 15:
                plazo = 18;
                name = " quincenas";
                break;
            case 30:
                plazo = 9;
                name = " meses";
                break;
        }

        for(int i=1; i<=plazo; i++)
            numeropagos.add(new ModelSpinner(i +  name, i + ""));

        getContextMenu().setDataSpinner(spnumeropagos, numeropagos, data -> campos.get(7).setIdentifier(data[0]));

        return numeropagos.size();
    }

    public Double getCuota(boolean seguro) {

        try {

            Integer plazo = campos.get(7).getIdentifierInt();
            Integer monto = Integer.parseInt(campos.get(3).getTextDecimal().replace(".00", ""));

            Double tasa_periodo = getPlazoPeriodo(seguro)/100;
            Double potencia1 = (tasa_periodo * (Math.pow((1.0 + tasa_periodo), plazo)));
            Double potencia2 = (Math.pow((1.0 + tasa_periodo), plazo) - 1.0);

            AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

            return round(monto * (potencia1 / potencia2));

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public Double getPlazoPeriodo(boolean seguro) {

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

        Integer frecuencia = campos.get(5).getIdentifierInt();
        Double tasa_seguro = Double.parseDouble(ofertaBimbo.getTasa_con_medico_en_casa()) * 100;
        Double tasa_sin_seguro = Double.parseDouble(ofertaBimbo.getTasa_sin_medico_en_casa()) * 100;
        Double tasa = (seguro ? tasa_seguro : tasa_sin_seguro);

        SessionApp.getInstance().getFcRequestDTO().setTasa(tasa);

        ArrayList<Integer> frecuencias = new ArrayList();
        frecuencias.add(7);
        frecuencias.add(14);
        frecuencias.add(28);

        int periodo = 360;

        if(frecuencias.contains(frecuencia))
            periodo = 364;

        return round((tasa / periodo) * frecuencia * 1.16);
    }

    public Double round(double number) {
        number = Math.round(number * 100);
        return  number/100;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
