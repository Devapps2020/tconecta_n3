package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.database.model.SERVICE_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_transacciones_4 extends HFragment {

    private View view;
    private MenuActivity context;

    private List<FormattedLine> lines;
    private String serviceName;

    public static ServicePayment data;
    public static SERVICE_BD_ROW service_bd_row;

    private String[] infoRows;
    private String total;

    public static Fragment_transacciones_4 newInstance(SERVICE_BD_ROW data) {
        Fragment_transacciones_4 fragment = new Fragment_transacciones_4();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reportes_4", new Gson().toJson(data));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_transacciones_4 newInstance(ServicePayment data) {
        Fragment_transacciones_4 fragment = new Fragment_transacciones_4();
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
                service_bd_row = new Gson().fromJson(getArguments().getString("Fragment_reportes_4"), SERVICE_BD_ROW.class);
            else
                data = new Gson().fromJson(getArguments().getString("Fragment_reportes_4"), ServicePayment.class);
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

        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printTicket();
            }
        });


       /* TextView txt_comision = view.findViewById(R.id.txt_comision);
        TextView txt_utilidad = view.findViewById(R.id.txt_utilidad);

        TextView txt_transaccion = view.findViewById(R.id.txt_transaccion);
        TextView txt_tienda = view.findViewById(R.id.txt_tienda);
        */
        TextView dudas_recargas = new TextView(context);
        ImageView img_service_transaction = view.findViewById(R.id.img_service_transaction);
        TextView title = view.findViewById(R.id.text_title);
        TextView txt_monto = view.findViewById(R.id.monto_recarga);
        TextView txt_referencia = view.findViewById(R.id.numero_recarga);
        TextView txt_fecha = view.findViewById(R.id.fecha_recarga);
        TextView txt_autorizacion = view.findViewById(R.id.autorizacion_recarga);

       /* LinearLayout layout_utilidad = view.findViewById(R.id.layout_utilidad);
        LinearLayout layout_comision = view.findViewById(R.id.layout_comision);*/

        img_service_transaction.setImageDrawable(context.getDrawable(R.drawable.icon_pago_servicios));
        title.setText("Pago de servicios");

        if(null != service_bd_row){
            if(null != service_bd_row.getResponse2()) {

                data = service_bd_row.getResponse2();

                if (data.getDynamicData() == null) {
                    String jsonn = data.getQpay_rspBody().replace("\\", "");
                    data.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());
                }
            }
        }

        /*//NUEVO FORMATEO DE MONTOS
        String monto = data.getDynamicData().getAmount();

        String comision = data.getDynamicData().getFlatFee() != null ?
                data.getDynamicData().getFlatFee() : "000";

        comision = comision.substring(0,comision.length() - 2);

        String utilidad = data.getDynamicData().getCommission() != null ?
                data.getDynamicData().getCommission() : "000";

        utilidad = utilidad.substring(0,utilidad.length() - 2);
        */

        String productId = "";
        String monto = "";
        String comision = "";
        String utilidad = "";
        String ticketText = "";

        if(conditions()){
            productId = service_bd_row.getType();

            monto = service_bd_row.getResponse1().getQpay_object()[0].getAmount();


            //String comision = data.getDynamicData().getFlatFee() != null ?  data.getDynamicData().getFlatFee() : "000";
            comision = service_bd_row.getResponse1().getQpay_object()[0].getFlatFee() != null ?  service_bd_row.getResponse1().getQpay_object()[0].getFlatFee() : "000";

            comision = Tools.getMonto(comision);

            //String utilidad = data.getDynamicData().getCommission() != null ? data.getDynamicData().getCommission() : "000";
            utilidad = service_bd_row.getResponse1().getQpay_object()[0].getCommission() != null ? service_bd_row.getResponse1().getQpay_object()[0].getCommission() : "000";

            utilidad = Tools.getMonto(utilidad);

            //if (Double.parseDouble(comision) == 0)
               //layout_comision.setVisibility(View.GONE);

            //if (Double.parseDouble(utilidad) == 0)
                //layout_utilidad.setVisibility(View.GONE);

            total = (Double.parseDouble(monto) + Double.parseDouble(comision)) + "";

            //txt_comision.setText(Utils.paserCurrency(comision));
            //txt_utilidad.setText(Utils.paserCurrency(utilidad));

            txt_monto.setText(Utils.paserCurrency(monto));
            //txt_toltal.setText(Utils.paserCurrency(total));

            txt_referencia.setText(service_bd_row.getResponse1().getQpay_object()[0].getAccountNumber());
            //txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(service_bd_row.getResponse1().getCreatedAt()));

            //txt_transaccion.setText(service_bd_row.getResponse1().getQpay_object()[0].getRequestId());
        }
        else {

            monto = data.getDynamicData().getAmount();


            comision = data.getDynamicData().getFlatFee() != null ?
                    data.getDynamicData().getFlatFee() : "000";

            comision = Tools.getMonto(comision);

            utilidad = data.getDynamicData().getCommission() != null ?
                    data.getDynamicData().getCommission() : "000";

            utilidad = Tools.getMonto(utilidad);

            //if (Double.parseDouble(comision) == 0)
                //layout_comision.setVisibility(View.GONE);

            //if (Double.parseDouble(utilidad) == 0)
                //layout_utilidad.setVisibility(View.GONE);

            total = (Double.parseDouble(monto) + Double.parseDouble(comision)) + "";

            //txt_comision.setText(Utils.paserCurrency(comision));
            //txt_utilidad.setText(Utils.paserCurrency(utilidad));

            txt_monto.setText(Utils.paserCurrency(monto));
            //txt_toltal.setText(Utils.paserCurrency(total));

            //20200902 RSB. Se valida si no viene el accountNumber se toma billReference, en transacciones al parecer tampoco hay billreference, se toman los 3 campos de accountNumber
            String referencia = data.getQpay_account_number();
            referencia = (referencia!=null && !referencia.isEmpty() ? referencia : data.getBillReference());

            if(referencia==null || referencia.isEmpty()){
                String accountNumber1 = (data.getQpay_account_number1()!=null && !data.getQpay_account_number1().isEmpty() ? data.getQpay_account_number1() : "");
                String accountNumber2 = (data.getQpay_account_number2()!=null && !data.getQpay_account_number2().isEmpty() ? data.getQpay_account_number2() : "");
                String accountNumber3 = (data.getQpay_account_number3()!=null && !data.getQpay_account_number3().isEmpty() ? data.getQpay_account_number3() : "");
                referencia = accountNumber1 + accountNumber2 + accountNumber3;
            }

            txt_referencia.setText(referencia);
            //txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(data.getResponseAt()));

            //txt_transaccion.setText(data.getRequestId());

            ticketText = data.getDynamicData().getTicket_text() != null ?
                    data.getDynamicData().getTicket_text() : "";


            productId = data.getQpay_product();



        }

        getContext().logger(data);

        switch (productId){

            case Globals.TELMEX_ID:
                serviceName = getString(R.string.text_telmex).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_telmex_help).toUpperCase());
                break;

            case Globals.SKY_ID:
                serviceName = getString(R.string.text_sky).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_sky_help).toUpperCase());
                break;

            case Globals.CFE_ID:
                serviceName = getString(R.string.text_cfe).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_cfe_help).toUpperCase());
                break;

            case Globals.DISH_ID:
                serviceName = getString(R.string.text_dish).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_dish_help).toUpperCase());
                break;

            case Globals.TELEVIA_ID:
                serviceName = getString(R.string.text_televia).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_televia_help).toUpperCase());
                break;

            case Globals.NATURGY_ID:
                serviceName = getString(R.string.text_naturgy).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_naturgy_help).toUpperCase());
                break;

            case Globals.IZZI_ID:
                serviceName = getString(R.string.text_izzi).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_izzi_help).toUpperCase());
                break;

            case Globals.MEGACABLE_ID:
                serviceName = getString(R.string.text_megacable).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_megacable_help).toUpperCase());
                break;

            case Globals.PASE_URBANO_ID:
                serviceName = getString(R.string.text_pase_urbano).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_pase_urbano_help).toUpperCase());
                break;

            case Globals.TOTALPLAY_ID:
                serviceName = getString(R.string.text_totalplay).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_totalplay_help).toUpperCase());
                break;

            case Globals.STARTV_ID:
                serviceName = getString(R.string.text_star_tv).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_star_tv_help).toUpperCase());
                break;

            case Globals.CEA_QRO_ID:
                serviceName = getString(R.string.text_cea_qro).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_cea_qro_help).toUpperCase());
                break;

            case Globals.NETWAY_ID:
                serviceName = getString(R.string.text_netwey).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_netwey_help).toUpperCase());
                break;

            case Globals.VEOLIA_ID:
                serviceName = getString(R.string.text_veolia).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_veolia_help).toUpperCase());
                break;

            case Globals.OPDM_ID:
                serviceName = getString(R.string.text_opdm).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_opdm_help).toUpperCase());
                break;

            case Globals.POST_ATT_ID:
                serviceName = getString(R.string.text_post_att).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_post_att_help).toUpperCase());
                break;

            case Globals.POST_MOVISTAR_ID:
                serviceName = getString(R.string.text_post_movistar).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_post_movistar_help).toUpperCase());
                break;

            case Globals.SACMEX_ID:
                serviceName = getString(R.string.text_sacmex).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_sacmex_help).toUpperCase());
                break;

            case Globals.GOB_MX_ID:
                serviceName = getString(R.string.text_gob_mx).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_gob_mx_help).toUpperCase());
                break;

            case Globals.SIAPA_GDL_ID:
                serviceName = getString(R.string.text_siapa_gdl).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_siapa_help).toUpperCase());
                break;

            case Globals.AYDM_MTY_ID:
                serviceName = getString(R.string.text_aydm_mty).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_aydm_help).toUpperCase());
                break;

            case Globals.GOB_EDOMEX_ID:
                serviceName = getString(R.string.text_gob_edomex).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_gob_edomex_help).toUpperCase());
                break;

            case Globals.CESPT_TIJ_ID:
                serviceName = getString(R.string.text_cespt_tij).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_cespt_tij_help).toUpperCase());
                break;

            case Globals.BLUE_TEL_ID:
                serviceName = getString(R.string.text_blue_tel).toUpperCase();
                dudas_recargas.setText(getString(R.string.text_blue_tel_help).toUpperCase());
                break;
        }

        if(conditions())//service_bd_row.getResponse1().getQpay_object()[0]
            txt_autorizacion.setText(serviceName + "  " + service_bd_row.getResponse1().getQpay_object()[0].getVendorReference());
        else
            txt_autorizacion.setText(serviceName + "  " + data.getDynamicData().getVendorReference());

        //Impresion de ticket
        if(Tools.isN3Terminal()){
            //N3_FLAG_COMMENT

            lines = new ArrayList<FormattedLine>();
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"CARGO EXITOSO"));
            //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            //lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"123456789012345678901234567"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,serviceName));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Referencia: " + txt_referencia.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Monto: " + txt_monto.getText()));
            if(!comision.isEmpty())
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Comisión: " + comision));
            if(!utilidad.isEmpty())
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"C.T.: " + utilidad));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"_____: "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Total: " + Utils.paserCurrency(total)));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + txt_autorizacion.getText()));

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,txt_fecha.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

            if(!ticketText.isEmpty() && ticketText != null){
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,ticketText));
                lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            }

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,dudas_recargas.getText().toString()));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"IMPORTANTE"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Conservar este comprobante para futuras aclaraciones"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));

            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"GRACIAS POR SU VISITA!!"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE \nFISCAL"));


        }
    }

    /**
     * Metodo para editar monto sea en decimal o base 100
     * @param monto
     * @return
     */
    private String getMonto(String monto) {

        monto = monto.replace(",",".");
        String[] montoArray = monto.split("\\.");
        if(montoArray.length>1){
            //Significa que es decimal, se deja pasar tal cual
        } else {
            //Significa que es base 100, pero en lugar de quitar los ultimos dos dígitos ahora
            //le agregaremos un punto a la cadena y así respetamos CFE
            if(monto.length()<3){
                monto = String.format("%03d", Integer.valueOf(monto));
            }
            StringBuffer str = new StringBuffer(monto);
            str.insert(monto.length()-2,".");
            monto = str.toString();

        }

        return monto;

    }

    private boolean conditions(){
        return Globals.LOCAL_TRANSACTION
                && null != service_bd_row
                && null != service_bd_row.getResponse1()
                && !AppPreferences.isAdmin();
    }

    private void printTicket(){
        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(lines, context);///N3_BACKUP
    }

}

