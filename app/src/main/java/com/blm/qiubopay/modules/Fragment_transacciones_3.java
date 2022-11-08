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

import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.model.TAE_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import mx.devapps.utils.interfaces.IAlertButton;

import java.util.ArrayList;
import java.util.List;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_transacciones_3 extends HFragment {

    private View view;
    private MenuActivity context;

    public static TaeSale data;
    public static TAE_BD_ROW tae_bd_row;

    List<FormattedLine> lines;
    private String[] infoRows;

    private String transactionLabel;

    public static Fragment_transacciones_3 newInstance(TAE_BD_ROW data) {
        Fragment_transacciones_3 fragment = new Fragment_transacciones_3();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reportes_3", new Gson().toJson(data));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_transacciones_3 newInstance(TaeSale data) {
        Fragment_transacciones_3 fragment = new Fragment_transacciones_3();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reportes_3", new Gson().toJson(data));

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
                tae_bd_row = new Gson().fromJson(getArguments().getString("Fragment_reportes_3"), TAE_BD_ROW.class);
            else
                data = new Gson().fromJson(getArguments().getString("Fragment_reportes_3"), TaeSale.class);
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
                context.initHome();
            }
        });

        LinearLayout layout_folio = new LinearLayout(context);
        layout_folio.setVisibility(View.GONE);

        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printTicket();
            }
        });

        //tittulo de operacion
        //monto de operacion
        //numero de referencia - numero de recarga o servicio | numero de referencia
        //fecha de operacion
        //compañia en caso de que aplique

        ImageView img_service_transaction = view.findViewById(R.id.img_service_transaction);
        TextView title = view.findViewById(R.id.text_title);
        TextView monto_recarga = view.findViewById(R.id.monto_recarga);
        TextView numero_recarga = view.findViewById(R.id.numero_recarga);
        TextView fecha_recarga = view.findViewById(R.id.fecha_recarga);
        TextView autorizacion_recarga = view.findViewById(R.id.autorizacion_recarga);
        TextView dudas_recargas = new TextView(context);

        /* TextView tienda_recarga = view.findViewById(R.id.tienda_recarga);
        TextView folio_recarga = view.findViewById(R.id.folio_recarga);
        TextView transaccion_recarga = view.findViewById(R.id.transaccion_recarga);
        */


        String amount="", mobile_number="", date="", carrier="", folio="", txr="", auth="";
        img_service_transaction.setImageDrawable(context.getDrawable(R.drawable.icons_tiempo_aire_60_x_60));
        title.setText("Tiempo aire");

        if(null != tae_bd_row){
            if(null != tae_bd_row.getResponse2()) {
                data = tae_bd_row.getResponse2();

                if (data.getDynamicData() == null) {
                    String jsonn = data.getQpay_rspBody().replace("\\", "");
                    data.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());
                }
            }
        }

        if(Globals.LOCAL_TRANSACTION
                && null != tae_bd_row
                && null != tae_bd_row.getResponse1()
                && !AppPreferences.isAdmin()){
            amount = tae_bd_row.getResponse1().getQpay_object()[0].getAmount();
            mobile_number = tae_bd_row.getResponse1().getQpay_object()[0].getMobileNumber();
            date = tae_bd_row.getResponse1().getCreatedAt();
            carrier = tae_bd_row.getCarrier();

            if(tae_bd_row.getResponse1().getQpay_object()[0].getTrxId() != null &&
                    !tae_bd_row.getResponse1().getQpay_object()[0].getTrxId().isEmpty())
                folio = tae_bd_row.getResponse1().getQpay_object()[0].getTrxId();
            else
                folio = tae_bd_row.getResponse1().getQpay_object()[0].getVendorReference();

            txr = tae_bd_row.getResponse1().getQpay_object()[0].getRequestId();

            auth = tae_bd_row.getResponse1().getQpay_object()[0].getVendorReference();
        }
        else
        {
            amount = data.getQpay_amount();
            mobile_number = data.getQpay_mobile_number();
            date = data.getResponseAt();
            carrier = data.getQpay_carrier();

            if(data.getDynamicData().getTrxId() != null &&
                    !data.getDynamicData().getTrxId().isEmpty())
                folio = data.getDynamicData().getTrxId();
            else
                folio = data.getDynamicData().getVendorReference();

            txr = data.getDynamicData().getRequestId();

            if(data.getDynamicData().getVendorReference() != null &&
                    !data.getDynamicData().getVendorReference().isEmpty())
                auth = data.getDynamicData().getVendorReference();

            if(data.getDynamicData().getTransactionLabel() != null && !data.getDynamicData().getTransactionLabel().isEmpty()){
                transactionLabel = data.getDynamicData().getTransactionLabel();
            } else {
                transactionLabel = "";
            }
        }

        monto_recarga.setText(Utils.paserCurrency(amount));
        numero_recarga.setText(mobile_number);
        fecha_recarga.setText(Utils.formatDate(date));


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
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_TURBORED_TAE:
                autorizacion_recarga.setText(getString(R.string.text_turbored).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_TURBORED_DATOS:
                autorizacion_recarga.setText(getString(R.string.text_turbored_datos).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_SPACE:
                autorizacion_recarga.setText(getString(R.string.text_space).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_DIRI:
                autorizacion_recarga.setText(getString(R.string.text_diri).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_PILLOFON:
                autorizacion_recarga.setText(getString(R.string.text_pillofon).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_FOBO:
                autorizacion_recarga.setText(getString(R.string.text_fobo).toUpperCase());
                dudas_recargas.setText(transactionLabel);
                break;

            case Globals.ID_BAIT:
                autorizacion_recarga.setText(getString(R.string.text_bait).toUpperCase());
                break;
        }

        autorizacion_recarga.append(" " + auth);

        /*if(data.getDynamicData().getVendorReference() != null &&
                !data.getDynamicData().getVendorReference().isEmpty())
            autorizacion_recarga.append(data.getDynamicData().getVendorReference());*/

        //autorizacion_recarga.append(auth);

        //Impresion de ticket
        if(Tools.isN3Terminal()) {
            //N3_FLAG_COMMENT
            lines = new ArrayList<FormattedLine>();
            //LOGO
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG_V2   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"RECARGA EXITOSA"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Monto: " + monto_recarga.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Teléfono: " + numero_recarga.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + autorizacion_recarga.getText().toString()));
            if(layout_folio.getVisibility() != View.GONE)
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Folio: " + folio));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"No. Transacción: " + txr));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,dudas_recargas.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,fecha_recarga.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE FISCAL"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL_V2, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            //N3_FLAG_COMMENT
        }
    }

    private void printTicket(){
        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(lines, context);///N3_BACKUP
    }
}

