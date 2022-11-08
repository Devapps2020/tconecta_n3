package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import com.blm.qiubopay.printers.PrinterManager;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import integration.praga.mit.com.apiintegration.model.TransactionResponse;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_cancelacion_2 extends HFragment {

    private static boolean isMitecTxr = false;
    private View view;
    private MenuActivity context;
    private QPAY_VisaResponse data;
    private TransactionResponse mitecData;

    private TextView txt_concepto;
    private TextView txt_tarjeta;
    private TextView txt_monto;
    private TextView txt_factura;
    private TextView txt_tienda;
    private TextView txt_fecha;

    private void setIsMitecTxr(){
        isMitecTxr = true;
    }

    public static Fragment_cancelacion_2 newInstance(QPAY_VisaResponse data) {
        Fragment_cancelacion_2 fragment = new Fragment_cancelacion_2();
        Bundle args = new Bundle();

        args.putString("Fragment_cancelacion_2", new Gson().toJson(data));

        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment_cancelacion_2 newInstance(TransactionResponse data) {
        Fragment_cancelacion_2 fragment = new Fragment_cancelacion_2();
        Bundle args = new Bundle();
        isMitecTxr = true;
        args.putString("Fragment_cancelacion_2", new Gson().toJson(data));

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
            if(isMitecTxr)
                mitecData = new Gson().fromJson(getArguments().getString("Fragment_cancelacion_2"), TransactionResponse.class);
            else
                data = new Gson().fromJson(getArguments().getString("Fragment_cancelacion_2"), QPAY_VisaResponse.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if(view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_cancelacion_2, container, false);

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
                getContext().backFragment();
            }
        });

         txt_concepto = view.findViewById(R.id.txt_concepto);
         txt_tarjeta = view.findViewById(R.id.txt_tarjeta);
         txt_monto = view.findViewById(R.id.txt_monto);
         txt_factura = view.findViewById(R.id.txt_factura);
         txt_tienda = view.findViewById(R.id.txt_tienda);
         txt_fecha = view.findViewById(R.id.txt_fecha);

        txt_concepto.setText("Cancelación");

        if(isMitecTxr){
            //TRANSACCIÓN MIT
            txt_tarjeta.setText("**** **** **** " + mitecData.getCcNumber());
            txt_monto.setText(Utils.paserCurrency(mitecData.getAmount().toString()));
            txt_factura.setText(mitecData.getFolio());
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(mitecData.getDate()) + " " + mitecData.getTime());
        }
        else {
            //TRANSACCIÓN VISA
            txt_tarjeta.setText("**** **** **** " + data.getQpay_object()[0].getCspBody().getMaskedPan().substring(12));
            txt_monto.setText(data.getQpay_object()[0].getCspBody().getAmt());
            txt_factura.setText(data.getQpay_object()[0].getCspBody().getInvoice());
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(data.getQpay_object()[0].getCspBody().getTxDate()) + " " + data.getQpay_object()[0].getCspBody().getTxDate());
        }

        Button btn_terminar = view.findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.initHome();
            }
        });

        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setVisibility(View.GONE);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMitecTxr)
                    Utils.shareVoucherFinanciero(context, mitecData.getFolio());
                else
                    Utils.shareVoucherFinanciero(context, data.getQpay_object()[0].getCspHeader().getRspId());
            }
        });

        //Impresion de ticket
        if(Tools.isN3Terminal()){
            //printTicket(true);

            printTicket(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContext().alert("¿Desea imprimir la copia del comercio?", new IAlertButton() {
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

            /*try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }
    }

    private void printTicket(final IFunction function, Boolean isCardHolderTicket){
        //N3_FLAG_COMMENT
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "CANCELACION"));

        String comercio = String.format("%s. %s %s, %s %s", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, comercio));

        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"1014327" + "/" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "Tarjeta: " + txt_tarjeta.getText().toString()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, mitecData.getAppidLabel()));

        if(isCardHolderTicket)
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "C-L-I-E-N-T-E"));
        else
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "C-O-M-E-R-C-I-O"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "APROBADA"));


        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "","IMPORTE",Utils.paserCurrency(""+mitecData.getAmount()) + " MXN"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"", "Oper: " + mitecData.getFolio(), "Aut: " + mitecData.getAuth()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","Ref:", mitecData.getReference()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,mitecData.getDate() + " " +mitecData.getTime()));

        if(!isCardHolderTicket){
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,mitecData.getCcName()));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,getResources().getString(R.string.app_name) + " v" + Tools.getVersion()));

        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());
        //N3_FLAG_COMMENT
    }
}

