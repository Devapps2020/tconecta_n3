package com.blm.qiubopay.modules.recarga;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.N3PrinterHelper;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_pago_recarga_ticket extends HFragment implements IMenuContext {

    private String data;
    private QPAY_TaeSale recarga;
    private QPAY_TaeSaleResponseFirst taeResponse;
    private String carrier;

    public static Fragment_pago_recarga_ticket newInstance(Object... data) {
        Fragment_pago_recarga_ticket fragment = new Fragment_pago_recarga_ticket();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_pago_recarga_ticket", (String) data[0]);

        if(data.length > 1)
            args.putString("Fragment_pago_recarga_ticket_1_1", (String) data[1]);

        if(data.length > 2)
            args.putString("Fragment_pago_recarga_ticket_1_2", new Gson().toJson(data[2]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            data = getArguments().getString("Fragment_pago_recarga_ticket");
            carrier = getArguments().getString("Fragment_pago_recarga_ticket_1_1");
            recarga = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_ticket_1_2"), QPAY_TaeSale.class);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_recarga_ticket, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        LinearLayout layout_folio = getView().findViewById(R.id.layout_folio);
        layout_folio.setVisibility(View.GONE);

        Button btn_compartir = getView().findViewById(R.id.btn_compartir);
        btn_compartir.setVisibility(View.GONE);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareVoucherTae(getContext(), taeResponse.getQpay_object()[0].getTransactionId(), taeResponse.getQpay_object()[0].getRequestId());
            }
        });

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        TextView monto_recarga = getView().findViewById(R.id.monto_recarga);
        TextView numero_recarga = getView().findViewById(R.id.numero_recarga);
        TextView autorizacion_recarga = getView().findViewById(R.id.autorizacion_recarga);
        TextView folio_recarga = getView().findViewById(R.id.folio_recarga);
        TextView transaccion_recarga = getView().findViewById(R.id.transaccion_recarga);
        TextView tienda_recarga = getView().findViewById(R.id.tienda_recarga);
        TextView fecha_recarga = getView().findViewById(R.id.fecha_recarga);
        TextView dudas_recargas = getView().findViewById(R.id.dudas_recargas);

        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponseFirst.QPAY_TaeSaleResponseFirstExcluder()).create();
        taeResponse = gson.fromJson(data, QPAY_TaeSaleResponseFirst.class);

        monto_recarga.setText(Utils.paserCurrency(recarga.getQpay_amount()));
        numero_recarga.setText(recarga.getQpay_mobile_number());
        tienda_recarga.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());


        if(taeResponse.getCreatedAt() == null)
            fecha_recarga.setText(taeResponse.getQpay_object()[0].getDateTime());
        else
            fecha_recarga.setText(taeResponse.getCreatedAt());



        if(taeResponse.getQpay_object()[0].getTrxId() != null &&
                !taeResponse.getQpay_object()[0].getTrxId().isEmpty())
            folio_recarga.setText(taeResponse.getQpay_object()[0].getTrxId());
        else
            folio_recarga.setText(taeResponse.getQpay_object()[0].getVendorReference());

        transaccion_recarga.setText(taeResponse.getQpay_object()[0].getRequestId());

        switch (carrier){

            case Globals.ID_TELCEL:
                layout_folio.setVisibility(View.VISIBLE);
                autorizacion_recarga.setText(getString(R.string.text_telcel).toUpperCase());
                dudas_recargas.setText(R.string.text_telcel_help);
                break;

            case Globals.ID_TELCEL_DATOS:
                layout_folio.setVisibility(View.VISIBLE);
                autorizacion_recarga.setText(getString(R.string.text_telcel_datos).toUpperCase());
                dudas_recargas.setText(R.string.text_telcel_datos_help);
                break;

            case Globals.ID_TELCEL_INTERNET:
                layout_folio.setVisibility(View.VISIBLE);
                autorizacion_recarga.setText(getString(R.string.text_telcel_internet).toUpperCase());
                dudas_recargas.setText(R.string.text_telcel_internet_help);
                break;

            case Globals.ID_MOVISTAR:
                autorizacion_recarga.setText(getString(R.string.text_movistar).toUpperCase());
                dudas_recargas.setText(R.string.text_movistar_help);
                break;

            case Globals.ID_IUSACELL: //IUSACELL Y AT&T
                autorizacion_recarga.setText(getString(R.string.text_att).toUpperCase());
                dudas_recargas.setText(R.string.text_att_help);
                break;

            case Globals.ID_UNEFON:
                autorizacion_recarga.setText(getString(R.string.text_unefon).toUpperCase());
                dudas_recargas.setText(R.string.text_unefon_help);
                break;

            case Globals.ID_VIRGIN_MOBILE:
                layout_folio.setVisibility(View.VISIBLE);
                autorizacion_recarga.setText(getString(R.string.text_virgin).toUpperCase());
                dudas_recargas.setText(R.string.text_virgin_help);
                break;

            case Globals.ID_MAS_RECARGA:
                autorizacion_recarga.setText(getString(R.string.text_mas_recarga).toUpperCase());
                dudas_recargas.setText(R.string.text_mas_recarga_help);
                break;

            case Globals.ID_TUENTI:
                autorizacion_recarga.setText(getString(R.string.text_tuenti).toUpperCase());
                dudas_recargas.setText(R.string.text_tuenti_help);
                break;

            case Globals.ID_FREEDOM_POP:
                autorizacion_recarga.setText(getString(R.string.text_freedom).toUpperCase());
                dudas_recargas.setText(R.string.text_freedom_help);
                break;

            case Globals.ID_ALO:
                layout_folio.setVisibility(View.VISIBLE);
                autorizacion_recarga.setText(getString(R.string.text_alo).toUpperCase());
                dudas_recargas.setText(R.string.text_alo_help);
                break;

            case Globals.ID_BUENOCELL:
                autorizacion_recarga.setText(getString(R.string.text_bueno_cell).toUpperCase());
                dudas_recargas.setText(R.string.text_bueno_cell_help);
                break;

            case Globals.ID_MI_MOVIL:
                autorizacion_recarga.setText(getString(R.string.text_mi_movil).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_TURBORED_TAE:
                autorizacion_recarga.setText(getString(R.string.text_turbored).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_TURBORED_DATOS:
                autorizacion_recarga.setText(getString(R.string.text_turbored_datos).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_SPACE:
                autorizacion_recarga.setText(getString(R.string.text_space).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_DIRI:
                autorizacion_recarga.setText(getString(R.string.text_diri).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_PILLOFON:
                autorizacion_recarga.setText(getString(R.string.text_pillofon).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_FOBO:
                autorizacion_recarga.setText(getString(R.string.text_fobo).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel());
                break;

            case Globals.ID_BAIT:
                autorizacion_recarga.setText(getString(R.string.text_bait).toUpperCase());
                dudas_recargas.setText(taeResponse.getQpay_object()[0].getTransactionLabel().toUpperCase());
                break;
        }

        if(taeResponse.getQpay_object()[0].getProviderAuth() != null &&
                !taeResponse.getQpay_object()[0].getProviderAuth().isEmpty()) {
            autorizacion_recarga.append(" " + taeResponse.getQpay_object()[0].getProviderAuth());
        } else if(taeResponse.getQpay_object()[0].getAuthorizationNumber() != null &&
                !taeResponse.getQpay_object()[0].getAuthorizationNumber().isEmpty()) {
            autorizacion_recarga.append(" " + taeResponse.getQpay_object()[0].getAuthorizationNumber());
        } else {
            autorizacion_recarga.append(" " + taeResponse.getQpay_object()[0].getVendorReference());
        }

        if(taeResponse.getQpay_object()[0].getBimboAward() != null && taeResponse.getQpay_object()[0].getBimboAward()) {
            TextView text_premio_bimbo = getView().findViewById(R.id.text_premio_bimbo);
            text_premio_bimbo.setText(taeResponse.getQpay_object()[0].getBimboAwardMessage());
            text_premio_bimbo.setVisibility(View.VISIBLE);
            getContext().alert("¡Número Ganador!", "Tu cliente ha ganado un Nito 62g, entrégaselo y te remplazaremos el producto sin costo en la próxima visita de tu vendedor.");
        }

        //Impresion de ticket
        if(Tools.isN3Terminal()) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<FormattedLine> lines = new ArrayList<FormattedLine>();
                    //LOGO
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"RECARGA EXITOSA"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Monto: " + Utils.paserCurrency(recarga.getQpay_amount())));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Teléfono: " + recarga.getQpay_mobile_number()));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + autorizacion_recarga.getText().toString()));
                    if(layout_folio.getVisibility() != View.GONE) {
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "Folio: " + folio_recarga.getText().toString()));
                    }
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"No. Transacción: " + transaccion_recarga.getText()));
                    if(taeResponse.getQpay_object()[0].getBimboAward() != null && taeResponse.getQpay_object()[0].getBimboAward()) {
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,taeResponse.getQpay_object()[0].getBimboAwardMessage()));
                    }
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,dudas_recargas.getText().toString()));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,fecha_recarga.getText().toString()));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE FISCAL"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

                    PrinterManager printerManager = new PrinterManager(getContext());
                    printerManager.printFormattedTicket(lines, getContext());
                }
            },1000);

        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public boolean onBackPressed() {
        getContextMenu().initHome();
        return true;
    }
}

