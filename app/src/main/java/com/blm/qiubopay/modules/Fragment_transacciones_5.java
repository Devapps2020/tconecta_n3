package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;

import mx.devapps.utils.interfaces.IAlertButton;

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

public class Fragment_transacciones_5 extends HFragment {

    private View view;
    private MenuActivity context;
    public static QPAY_VisaEmvResponse data;
    public static FINANCIAL_BD_ROW financial_bd_row;

    private List<FormattedLine> lines;
    private String comision;
    public static Fragment_transacciones_5 newInstance(FINANCIAL_BD_ROW data) {
        Fragment_transacciones_5 fragment = new Fragment_transacciones_5();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reportes_4", new Gson().toJson(data));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_transacciones_5 newInstance(QPAY_VisaEmvResponse data) {
        Fragment_transacciones_5 fragment = new Fragment_transacciones_5();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reportes_4", new Gson().toJson(data));

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
            if(Globals.LOCAL_TRANSACTION && !AppPreferences.isAdmin())
                financial_bd_row = new Gson().fromJson(getArguments().getString("Fragment_reportes_4"), FINANCIAL_BD_ROW.class);
            else
                data = new Gson().fromJson(getArguments().getString("Fragment_reportes_4"), QPAY_VisaEmvResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_view_detalle_transaccion, container, false);

        setView(view);

        try {
            initFragment();
        } catch (Exception e) {
            e.printStackTrace();
            getContext().alert("Información no disponible", new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    context.initHome();
                }
            });
        }

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        Button btn_compartir = view.findViewById(R.id.btn_compartir);

        String paymentType = "";

        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printTicket();
            }
        });

        TextView txt_toltal = new TextView(context);
        ImageView img_service_transaction = view.findViewById(R.id.img_service_transaction);

        TextView title = view.findViewById(R.id.text_title);
        TextView txt_monto = view.findViewById(R.id.monto_recarga);
        TextView txt_referencia = view.findViewById(R.id.numero_recarga);
        TextView txt_fecha = view.findViewById(R.id.fecha_recarga);

        TextView txt_tarjeta = view.findViewById(R.id.autorizacion_recarga);
        TextView text_label_tarjeta = view.findViewById(R.id.text_label_tarjeta);
        ImageView img_tarjeta = view.findViewById(R.id.img_tarjeta);

        TextView txt_autorizacion = new TextView(context);
        TextView txt_comision = new TextView(context);

        text_label_tarjeta.setText("Tarjeta");
        img_tarjeta.setImageDrawable(context.getDrawable(R.drawable.icons_cobro_con_tarjeta));

        img_service_transaction.setImageDrawable(context.getDrawable(R.drawable.icons_cobro_con_tarjeta));

        if(null != financial_bd_row){
            if(null != financial_bd_row.getResponse2()) {
                data = financial_bd_row.getResponse2();
            }
        }

        if(conditions()){
            //CONSULTA LOCAL DE TRANSACCIONES

            if (financial_bd_row.getType().equals("SELL"))
                title.setText("VENTA");
            else if (financial_bd_row.getType().equals("CASHBACK"))
                title.setText("RETIRO DE EFECTIVO" );
            else if (financial_bd_row.getType().equals("CANCELATION"))
                title.setText("CANCELACIÓN");

            txt_monto.setText(Utils.paserCurrency(financial_bd_row.getResponse1().getTransactionResponse().getAmount().toString()));

            if ("CASHBACK".equals(financial_bd_row.getType())) {

                LinearLayout layout_total_retiro = view.findViewById(R.id.layout_total_retiro);

                layout_total_retiro.setVisibility(View.VISIBLE);

                //text_retiro_venta.setText("Retiro: ");
                txt_comision.setText(financial_bd_row.getResponse1().getComision());
                txt_monto.setText(financial_bd_row.getResponse1().getInitial_amount());
                txt_toltal.setText(Utils.paserCurrency(financial_bd_row.getResponse1().getTotal()));

            }

            if (financial_bd_row.getResponse1().getTransactionResponse().getCcNumber().trim().length() == 4)
                txt_tarjeta.setText("**** **** **** " + financial_bd_row.getResponse1().getTransactionResponse().getCcNumber());
            else
                txt_tarjeta.setText("**** **** **** " + financial_bd_row.getResponse1().getTransactionResponse().getCcNumber().substring(12).trim());

            txt_referencia.setText(financial_bd_row.getResponse1().getTransactionResponse().getFolio());
            txt_fecha.setText(Utils.formatDate(financial_bd_row.getResponse1().getTransactionResponse().getDate() + " " + financial_bd_row.getResponse1().getTransactionResponse().getTime()));

            if(null != financial_bd_row.getResponse1().getPaymentType()
                    && !financial_bd_row.getResponse1().getPaymentType().isEmpty()
                    && !financial_bd_row.getResponse1().getPaymentType().trim().equals("C"))
            {
                    txt_fecha.setText(Utils.formatDate(financial_bd_row.getResponse1().getTransactionResponse().getDate() + " " + financial_bd_row.getResponse1().getTransactionResponse().getTime())
                        + "\n\n"
                        + financial_bd_row.getResponse1().getPaymentType().replace("M"," meses sin intereses"));

                    paymentType = financial_bd_row.getResponse1().getPaymentType().replace("M"," meses sin intereses");
            }

            txt_referencia.setText(financial_bd_row.getResponse1().getTransactionResponse().getFolio());

            //LinearLayout layout_autorizacion = view.findViewById(R.id.layout_autorizacion);

            //if (data.getCspBody().getAuthNum() == null || data.getCspBody().getAuthNum().trim().isEmpty())
                //layout_autorizacion.setVisibility(View.GONE);

            txt_autorizacion.setText(financial_bd_row.getResponse1().getTransactionResponse().getAuth());
        }
        else
            {

            //CONSULTA DE TRANSACCIONES AL SERVIDOR
            title.setText(data.getName());

            txt_monto.setText(Utils.paserCurrency(data.getCspBody().getAmt()));

            if ("13005".equals(data.getCspHeader().getProductId())) {

                LinearLayout layout_total_retiro = view.findViewById(R.id.layout_total_retiro);

                layout_total_retiro.setVisibility(View.VISIBLE);

                /*TextView txt_comision = view.findViewById(R.id.txt_comision);
                TextView txt_toltal = view.findViewById(R.id.txt_toltal);
                TextView text_retiro_venta = view.findViewById(R.id.text_retiro_venta);*/


                //text_retiro_venta.setText("Retiro: ");
                txt_comision.setText(data.getCspBody().getCashbackFee());
                txt_toltal.setText(data.getCspBody().getAmt());
                txt_monto.setText(Utils.paserCurrency(data.getCspBody().getCashbackAmt()));

            }

            if (data.getCspBody().getMaskedPan().trim().length() == 4)
                txt_tarjeta.setText("**** **** **** " + data.getCspBody().getMaskedPan().trim());
            else
                txt_tarjeta.setText("**** **** **** " + data.getCspBody().getMaskedPan().substring(12).trim());

            txt_referencia.setText(data.getCspBody().getRrn());
            txt_referencia.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(data.getCspBody().getTxDate()));

                if(null != data.getCspBody().getPaymentType()
                        && !data.getCspBody().getPaymentType().isEmpty()
                        && !data.getCspBody().getPaymentType().trim().equals("C"))
                {
                            txt_fecha.setText(Utils.formatDate(data.getCspBody().getTxDate())
                            + "\n\n"
                            + data.getCspBody().getPaymentType().replace("M"," meses sin intereses"));

                            paymentType = data.getCspBody().getPaymentType().replace("M"," meses sin intereses");
                }

            txt_referencia.setText(data.getCspHeader().getRspId());

            //LinearLayout layout_autorizacion = view.findViewById(R.id.layout_autorizacion);

            //if (data.getCspBody().getAuthNum() == null || data.getCspBody().getAuthNum().trim().isEmpty())
            //    layout_autorizacion.setVisibility(View.GONE);

            txt_autorizacion.setText(data.getCspBody().getAuthNum());
        }

        lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        if(conditions())
        {
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, title.getText().toString()));
        }
        else
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, data.getName()));

        String comercio = String.format("%s. %s %s, %s %s", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, comercio));

        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"1014327" + "/" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "Tarjeta: " + txt_tarjeta.getText().toString()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "C-O-P-I-A"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "APROBADA"));

        if(title.getText().toString().contains("RETIRO")){
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","Retiro:", Utils.paserCurrency(txt_monto.getText().toString()) + "MXN"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "","Costo por Servicio:",txt_comision.getText()  + "MXN"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "","Total:",txt_toltal.getText() + "MXN"));
        }
        else {
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"","IMPORTE",  txt_monto.getText() + " MXN"));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"", "Oper: " + txt_referencia.getText(),"Aut: " + txt_autorizacion.getText()));

        if(!title.getText().toString().contains("CANCELACIÓN")){
            if(conditions()){
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","ARQC:", financial_bd_row.getResponse1().getTransactionResponse().getArqc()));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","AID:", financial_bd_row.getResponse1().getTransactionResponse().getAppId()));
            }
            else
            {
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","ARQC:", data.getCspBody().getEmv().getApplicationCryptogram()));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"","AID:", data.getCspBody().getEmv().getAid()));
            }
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, txt_fecha.getText().toString()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,getResources().getString(R.string.app_name) + " v" + Tools.getVersion()));

    }

    private boolean conditions(){
        return Globals.LOCAL_TRANSACTION
                && null != financial_bd_row
                && null != financial_bd_row.getResponse1()
                && !AppPreferences.isAdmin();
    }

    private void printTicket(){
        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(lines, context);///N3_BACKUP
    }

}

