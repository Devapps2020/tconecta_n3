package com.blm.qiubopay.modules.fincomun.originacion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.credito.Fragment_fincomun_detalle_credito;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Objects.Catalogos.Destino;
import mx.com.fincomun.origilib.Objects.Catalogos.Matriz;
import mx.com.fincomun.origilib.Objects.Catalogos.ModCredito;
import mx.com.fincomun.origilib.Objects.Catalogos.ProductoCredito;
import mx.com.fincomun.origilib.Objects.Catalogos.TipoCredito;
import mx.com.fincomun.origilib.Objects.DHOfertaBimbo;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_originacion_simulador extends HFragment implements IMenuContext {

    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private TextView tv_frequency,tv_term,tv_frequency_name,tv_term_name,tv_fee_payment,tv_total_amount,tv_payment_name;
    private Slider sld_frequency, sld_term;

    public static int term, frequency;
    public static Double total_amount = 0.0;
    public static Double fee_payment = 0.0;
    public static String name = "";
    private Boolean isLoading  =false;

    public static Fragment_fincomun_originacion_simulador newInstance() {
        Fragment_fincomun_originacion_simulador fragment = new Fragment_fincomun_originacion_simulador();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_originacion_simulador"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_originacion_simulador, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .showTitle("Simula tu préstamo")
                .setColorTitle(R.color.FC_blue_6)
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        campos = new ArrayList();

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

        campos.add(FCEditText.create(getView().findViewById(R.id.edit_amount))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(9)
                .setType(FCEditText.TYPE.CURRENCY_SD)
                .setCurrencyWithSymbol(false)
                .setHint("")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                   total_amount = Double.parseDouble(campos.get(0).getText().replace(",","").replace("$",""));

                                   if (total_amount >= 4000.0){
                                       getCuota(false);
                                   }else {
                                       tv_fee_payment.setText("$0");
                                       tv_total_amount.setText("$0");
                                   }



                                }catch (Exception ex){
                                }

                            }
                        });



                    }
                })
        .setStartIcon(R.drawable.ic_money,R.color.colorBimboBlueDark));

        campos.get(0).setTextDecimal(paserCurrencySD(ofertaBimbo.getMonto()));

        tv_fee_payment = getView().findViewById(R.id.tv_fee_payment);
        tv_total_amount = getView().findViewById(R.id.tv_total_amount);
        tv_payment_name = getView().findViewById(R.id.tv_payment_name);

        sld_frequency = getView().findViewById(R.id.sld_frequency);
        sld_frequency.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                setFrequencyName(String.valueOf(Math.round(value)));
                getCuota(false);
                validate();
            }
        });
        sld_term = getView().findViewById(R.id.sld_term);
        sld_term.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                tv_term.setText(String.valueOf(Math.round(value)));
                term = Math.round(value);
                getCuota(false);
                validate();
            }
        });

        tv_frequency = getView().findViewById(R.id.tv_frequency);

        tv_frequency_name = getView().findViewById(R.id.tv_frequency_name);
        tv_term = getView().findViewById(R.id.tv_term);
        tv_term_name = getView().findViewById(R.id.tv_term_name);
        tv_fee_payment = getView().findViewById(R.id.tv_fee_payment);
        tv_total_amount = getView().findViewById(R.id.tv_total_amount);


        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    Double monto = Double.parseDouble(TextUtils.isEmpty(campos.get(0).getTextDecimal()) ? "0.0" : campos.get(0).getTextDecimal());
                    if (monto < 4000.0 || monto > Double.valueOf(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto())) {

                        Double ofertaMxima = Double.valueOf(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
                        getContext().alert("El monto ingresado no puede ser menor\na $4,000.00 ni mayor a " + NumberFormat
                                .getCurrencyInstance(Locale.US)
                                .format(ofertaMxima), new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                setMontoMinimo();


                            }
                        });
                        isLoading = false;
                    } else {
                        configRequest(total_amount);
                        SessionApp.getInstance().getFcRequestDTO().setMedico(false);
                        Fragment_fincomun_originacion_simulador_detalle.type = type;
                        getContext().setFragment(Fragment_fincomun_originacion_simulador_detalle.newInstance());
                        isLoading = false;
                    }
                }
            }
        });

        getContextMenu().catalogs(new IFunction() {
            @Override
            public void execute(Object[] data) {
                setList();
            }
        });

        sld_frequency.setValue(1F);
        sld_term.setValue(18F);
        validate();

        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_SIMULADOR);
    }

    public void setMontoMinimo() {

        Double monto = 4000.0;
        campos.get(0).setTextDecimal(String.valueOf(monto));

    }

    private void setList() {
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

        setListPagos(getMatriz(Integer.parseInt(tipocreditos.get(0).getValue()),
                Integer.parseInt(modocreditos.get(0).getValue())));

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



    }

    private String setFrequencyName(String freq) {

        String paymentName = "Tu pago es de:";
        Integer plazo = 1;

        switch (freq){
            case "7":
            case "0":
                plazo = 36;
                name = "semanas";
                paymentName = "Tu pago semanal es de:";
                this.frequency = 7;
                break;
            case "14":
            case "1":
                plazo = 18;
                //name = "catorcenas";
                name = "quincenas";
                //paymentName = "Tu pago catorcenal es de:";
                paymentName = "Tu pago quincenal es de:";
                this.frequency = 14;
                break;
           /* case "15":
            case "2":
                plazo = 18;
                name = "quincenas";
                paymentName = "Tu pago quincenal es de:";
                this.frequency = 15;
                break;*/
            case "28":
            case "2":
                plazo = 9;
                name = "meses";
                paymentName = "Tu pago mensual es de:";
                this.frequency = 28;
                break;
            default:
                break;
        }
        tv_frequency.setText(String.valueOf(this.frequency));
        tv_term_name.setText(name);
        tv_payment_name.setText(paymentName);
        sld_term.setValueTo(plazo.floatValue());
        sld_term.setValue(1f);
        tv_term.setText("1");
        this.term = 1;


        return name;
    }

    public void validate(){

        Double monto = Double.parseDouble(TextUtils.isEmpty(campos.get(0).getTextDecimal())?"0.0":campos.get(0).getTextDecimal());
        if(monto < 4000.0 || monto > Double.valueOf(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto())) {
            Double ofertaMxima =  Double.valueOf(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
            getContext().alert("El monto ingresado no puede ser menor\na $4,000.00 ni mayor a "+ NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(ofertaMxima) , new IAlertButton() {
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

        if(term == 0 || frequency == 0){
            return;
        }

        btn_next.setEnabled(true);
    }

    public Double getCuota(boolean seguro) {

        try {

            Integer plazo = this.term;
            Integer monto = total_amount.intValue();

            Double tasa_periodo = getPlazoPeriodo(seguro)/100;
            Double potencia1 = (tasa_periodo * (Math.pow((1.0 + tasa_periodo), plazo)));
            Double potencia2 = (Math.pow((1.0 + tasa_periodo), plazo) - 1.0);

           // AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

            Double total = round(monto * (potencia1 / potencia2));
            tv_fee_payment.setText(paserCurrencySD(String.valueOf(Math.round(total))));
            tv_total_amount.setText((paserCurrencySD(String.valueOf((Math.round(total)*plazo)))));
            fee_payment = total;
            return total ;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public Double getPlazoPeriodo(boolean seguro) {

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

        Integer frecuencia = this.frequency;
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

    public void configRequest(Double monto) {

        FCSimuladorRequest simuladorRequest = new FCSimuladorRequest();

        simuladorRequest.setIdTipoCredito(2);
        simuladorRequest.setIdModCredito(6);
        simuladorRequest.setIdTipoProducto(2);
        simuladorRequest.setMontoPrestamo(BigDecimal.valueOf(monto).toBigInteger());
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos(String.valueOf(term));
        simuladorRequest.setDestino("311");
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago(String.valueOf(frequency));
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        simuladorRequest.setCuota(getCuota(false));

        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);

        getContext().setFragment(Fragment_fincomun_originacion_simulador_detalle.newInstance());

    }

    public static String paserCurrencySD(String dato) {


        try {
            Double value = Double.parseDouble(dato.replace(",", "").replace("$", "").replace("E-", ""));

            String result =  java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

            return  result.substring(0, result.length() - 2).replace(".","");

        } catch (Exception ex) {
            return "$0";
        }

    }
}