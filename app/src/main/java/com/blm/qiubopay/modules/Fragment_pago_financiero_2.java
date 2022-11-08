package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.modules.restaurante.Fragment_restaurante_2;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

public class Fragment_pago_financiero_2 extends HFragment {

    private View view;
    private MenuActivity context;
    private QPAY_VisaResponse data;
    private CustomMitecTransaction mitecData;
    private static Boolean IsMitecTransaction;

    private boolean isCashbackTransaction;

    LinearLayout layout_total_retiro;// = view.findViewById(R.id.layout_total_retiro);
    TextView txt_comision;// = view.findViewById(R.id.txt_comision);
    TextView txt_toltal;// = view.findViewById(R.id.txt_toltal);
    TextView text_retiro_venta;// = view.findViewById(R.id.text_retiro_venta);

    TextView txt_concepto;// = view.findViewById(R.id.txt_concepto);
    TextView txt_tarjeta;// = view.findViewById(R.id.txt_tarjeta);
    TextView txt_monto;// = view.findViewById(R.id.txt_monto);
    TextView txt_autorizacion;// = view.findViewById(R.id.txt_autorizacion);
    TextView txt_factura;// = view.findViewById(R.id.txt_factura);
    TextView txt_tienda;// = view.findViewById(R.id.txt_tienda);
    TextView txt_fecha;// = view.findViewById(R.id.txt_fecha);

    public static Fragment_pago_financiero_2 newInstance(boolean isCashbackTransaction, QPAY_VisaResponse data) {
        //Transacción Visa
        IsMitecTransaction = false;
        Fragment_pago_financiero_2 fragment = new Fragment_pago_financiero_2();
        Bundle args = new Bundle();

        args.putBoolean("Fragment_pago_financiero_2_1", isCashbackTransaction);


        if(data != null)
            args.putString("Fragment_pago_financiero_2", new Gson().toJson(data));

        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment_pago_financiero_2 newInstance(boolean isCashbackTransaction, CustomMitecTransaction data) {
        //Transacción MIT
        IsMitecTransaction = true;
        Fragment_pago_financiero_2 fragment = new Fragment_pago_financiero_2();
        Bundle args = new Bundle();

        args.putBoolean("Fragment_pago_financiero_2_1", isCashbackTransaction);

        if(data != null)
            args.putString("Fragment_pago_financiero_2", new Gson().toJson(data));

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


        try{

            isCashbackTransaction = getArguments().getBoolean("Fragment_pago_financiero_2_1");

            //if(IsMitecTransaction)
            if(true)
            {
                if (getArguments() != null && getArguments().getString("Fragment_pago_financiero_2") != null)
                    mitecData = new Gson().fromJson(getArguments().getString("Fragment_pago_financiero_2"), CustomMitecTransaction.class);
            }
            else {
                if (getArguments() != null && getArguments().getString("Fragment_pago_financiero_2") != null)
                    data = new Gson().fromJson(getArguments().getString("Fragment_pago_financiero_2"), QPAY_VisaResponse.class);
            }

        } catch (Exception ex) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if(view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_pago_financiero_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });
        context.loading(false);

        if(isCashbackTransaction)
            CApplication.setAnalytics(CApplication.ACTION.CB_retiro_efectivo);
        else
            CApplication.setAnalytics(CApplication.ACTION.CB_pago_tarjeta);

        //if(IsMitecTransaction){
        if(true){
            if (isCashbackTransaction) {

                layout_total_retiro = view.findViewById(R.id.layout_total_retiro);
                txt_comision = view.findViewById(R.id.txt_comision);
                txt_toltal = view.findViewById(R.id.txt_toltal);
                text_retiro_venta = view.findViewById(R.id.text_retiro_venta);

                layout_total_retiro.setVisibility(View.VISIBLE);

                text_retiro_venta.setText("Retiro: ");
                txt_comision.setText(mitecData.getComision());
                txt_toltal.setText(mitecData.getTotal());

            }
        }
        else {
            if (isCashbackTransaction) {

                layout_total_retiro = view.findViewById(R.id.layout_total_retiro);
                txt_comision = view.findViewById(R.id.txt_comision);
                txt_toltal = view.findViewById(R.id.txt_toltal);
                text_retiro_venta = view.findViewById(R.id.text_retiro_venta);

                layout_total_retiro.setVisibility(View.VISIBLE);

                text_retiro_venta.setText("Retiro: ");
                txt_comision.setText(data.getComision());
                txt_toltal.setText(data.getTotal());

            }
        }

        txt_concepto = view.findViewById(R.id.txt_concepto);
        txt_tarjeta = view.findViewById(R.id.txt_tarjeta);
        txt_monto = view.findViewById(R.id.txt_monto);
        txt_autorizacion = view.findViewById(R.id.txt_autorizacion);
        txt_factura = view.findViewById(R.id.txt_factura);
        txt_tienda = view.findViewById(R.id.txt_tienda);
        txt_fecha = view.findViewById(R.id.txt_fecha);

        if(IsMitecTransaction){
            txt_concepto.setText(mitecData.getQpay_description());
            txt_tarjeta.setText("**** **** **** " + mitecData.getTransactionResponse().getCcNumber());
            txt_monto.setText(mitecData.getQpay_response());
            txt_autorizacion.setText(mitecData.getTransactionResponse().getAuth());
            txt_factura.setText(mitecData.getTransactionResponse().getReference());
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(mitecData.getTransactionResponse().getDate() + " " + mitecData.getTransactionResponse().getTime()));

            if(null != mitecData.getPaymentType()
                    && !mitecData.getPaymentType().isEmpty()
                    && !mitecData.getPaymentType().trim().equals("C"))
            {
                txt_fecha.setText(Utils.formatDate(mitecData.getTransactionResponse().getDate() + " " + mitecData.getTransactionResponse().getTime())
                        + "\n\n"
                        + mitecData.getPaymentType().replace("M"," meses sin intereses"));
            }
        }
        else
        {
            txt_concepto.setText(data.getQpay_description());
            txt_tarjeta.setText("**** **** **** " + data.getQpay_object()[0].getCspBody().getMaskedPan().substring(12));
            txt_monto.setText(data.getQpay_response());
            txt_autorizacion.setText(data.getQpay_object()[0].getCspBody().getAuthNum());
            txt_factura.setText(data.getQpay_object()[0].getCspBody().getInvoice());
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(data.getQpay_object()[0].getCspBody().getTxDate()));
        }

        Button btn_terminar = view.findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mitecData.getComesFromATippedSale()) {

                    /*if(Fragment_home.despuesPago != null) {
                        Fragment_home.despuesPago.execute();7856

                        return;
                    }*/

                    context.initHome();

                }  else {
                    //context.clearFragment();
                    context.setFragment(Fragment_restaurante_2.newInstance());
                }
            }
        });

        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setVisibility(View.GONE);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsMitecTransaction)
                    Utils.shareVoucherFinanciero(context, mitecData.getTransactionResponse().getFolio());
                else
                    Utils.shareVoucherFinanciero(context, data.getQpay_object()[0].getCspHeader().getRspId());
            }
        });

        //Impresion de ticket
        if(Tools.isN3Terminal()){

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    printTicket(new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            context.alert("¿Desea imprimir la copia del comercio?", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "SI";
                                }
                                @Override
                                public void onClick() {
                                    printTicket(null, false);
                                }
                            }, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "NO";
                                }

                                @Override
                                public void onClick() {

                                }
                            });

                        }
                    }, true);

                }
            }, 1000);

        }

        if(context.pagoTarjeta != null)
            context.pagoTarjeta.execute();
    }

    private void printTicket(final IFunction function, Boolean isCardHolderTicket){

        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        if(isCashbackTransaction)
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "CASHBACK"));
        else
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "VENTA"));

        String comercio = String.format("%s. %s %s, %s %s", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, comercio));

        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));


        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"1014327" + "/" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()));

        /*if(mitecData.getPosEntryMode().equals("022"))
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "Tarjeta " + txt_tarjeta.getText().toString()));
        else*/
        String cardTitle = mitecData.getTransactionResponse().getAppidLabel();
        cardTitle = cardTitle!=null && !cardTitle.isEmpty() ? cardTitle : "Tarjeta: ";
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "" + cardTitle + " " + mitecData.getTransactionResponse().getCcNumber()));

        if(isCardHolderTicket)
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "C-L-I-E-N-T-E"));
        else
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "C-O-M-E-R-C-I-O"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "APROBADA"));

        if(isCashbackTransaction){
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"", "Retiro:", Utils.paserCurrency(mitecData.getInitial_amount()) + "MXN"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "","Costo por Servicio:", Utils.paserCurrency(mitecData.getComision()) + "MXN"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "","Total:", Utils.paserCurrency("" + mitecData.getTransactionResponse().getAmount()) + "MXN"));
        }
        else {
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"","IMPORTE", Utils.paserCurrency(mitecData.getTransactionResponse().getAmount().toString()) + " MXN"));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Oper: " + mitecData.getTransactionResponse().getFolio(),"Aut: " + mitecData.getTransactionResponse().getAuth()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "", "Ref: ", mitecData.getTransactionResponse().getReference()));
        if(!mitecData.getPosEntryMode().equals("022")){
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "", "ARQC:", mitecData.getTransactionResponse().getArqc()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "", "AID:", mitecData.getTransactionResponse().getAppId()));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,mitecData.getTransactionResponse().getDate() + " " +mitecData.getTransactionResponse().getTime()));
        if(null != mitecData.getPaymentType()
                && !mitecData.getPaymentType().isEmpty()
                && !mitecData.getPaymentType().trim().equals("C"))
        {
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, mitecData.getPaymentType().replace("M"," meses sin intereses")));
        }


        if(!isCardHolderTicket){
            if(mitecData.isUsePIN()){
                //Transacción aprobada con firma electrónica
                lines.add(new FormattedLine(Globals.FONT_SIZE_SMALL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Autorizada mediante firma electrónica"));
            }else{
                //Es necesario que el usuario la firme
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,""));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,""));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,""));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
                if(("" + mitecData.getTransactionResponse().getCcName().trim()).equals(""))
                    lines.add(new FormattedLine(Globals.FONT_SIZE_SMALL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "FIRMA DEL CLIENTE"));
                else
                    lines.add(new FormattedLine(Globals.FONT_SIZE_SMALL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, mitecData.getTransactionResponse().getCcName()));
            }
        }else{
            if(mitecData.isUsePIN()){
                //Transacción aprobada con firma electrónica
                lines.add(new FormattedLine(Globals.FONT_SIZE_SMALL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Autorizada mediante firma electrónica"));
            }
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,getResources().getString(R.string.app_name) + " v" + Tools.getVersion()));

        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, context);//N3_BACKUP

    }

    private void printCommerceTicket(){

    }
}

