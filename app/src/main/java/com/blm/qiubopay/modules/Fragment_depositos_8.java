package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.TicketInfo;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasTransactionResponse;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_depositos_8 extends HFragment implements IMenuContext {

    public static QPAY_FinancialVasTransactionResponse data;
    private TicketInfo ticketInfo;

    public static Fragment_depositos_8 newInstance(Object... data) {
        Fragment_depositos_8 fragment = new Fragment_depositos_8();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_depositos_8, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().initHome();
            }
        });


        ticketInfo = new TicketInfo();
        ticketInfo.setAmount(data.getQpay_object()[0].getAmount());
        ticketInfo.setTotal(data.getQpay_object()[0].getAmount());
        ticketInfo.setTransaction(data.getQpay_object()[0].getTransactionId());
        ticketInfo.setAuthorization(data.getQpay_object()[0].getRequestId());  //REQUESTID es AUTORIZACION
        ticketInfo.setTimestamp(data.getCreatedAt());
        String tienda = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name();
        ticketInfo.setNegocio(tienda==null ? "" : tienda);

        TextView txt_monto = getView().findViewById(R.id.monto_deposito);
        TextView txt_autorizacion = getView().findViewById(R.id.autorizacion_deposito);
        TextView txt_transaction = getView().findViewById(R.id.transaccion_deposito);
        TextView txt_tienda = getView().findViewById(R.id.tienda_deposito);
        TextView txt_fecha = getView().findViewById(R.id.fecha_deposito);

        txt_monto.setText(Utils.paserCurrency(ticketInfo.getAmount()));
        txt_autorizacion.setText(ticketInfo.getAuthorization());
        txt_transaction.setText(ticketInfo.getTransaction());
        txt_tienda.setText(ticketInfo.getNegocio());
        txt_fecha.setText(ticketInfo.getTimestamp());

        Button btn_compartir = getView().findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.share(getContext(),Globals.HOST + "/api/v1/g/ticketVAS?ti=" + ticketInfo.getTransaction()
                        + "&ri=" + ticketInfo.getAuthorization());            }
        });

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });


        btn_compartir.setVisibility(View.GONE);

        // Impresion de ticket para N3

        btn_compartir.setVisibility(View.GONE);

        // Impresion de ticket para N3

        List<FormattedLine> lines = new ArrayList<FormattedLine>();

        //LOGO
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"Transferencia exitosa"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Traspaso a saldo " + getContextMenu().getResources().getString(R.string.app_name)));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Monto: MXN " + Utils.paserCurrency(ticketInfo.getAmount())));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Fecha y hora: " + ticketInfo.getTimestamp()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Transacción: " + ticketInfo.getTransaction()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + ticketInfo.getAuthorization()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"IMPORTANTE"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Conservar este comprobante para futuras aclaraciones"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Cualquier problema, favor de ponerse en contacto con Soporte al Cliente. ¡¡Gracias!!"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + ticketInfo.getNegocio()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"GRACIAS POR SU VISITA!!"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE \nFISCAL"));

        PrinterManager printerManager = new PrinterManager(getContext());
        printerManager.printFormattedTicket(lines, getContext());


    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}

