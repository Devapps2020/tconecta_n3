package com.blm.qiubopay.modules.servicio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.prestamos.QPAY_LoanResponse;
import com.blm.qiubopay.models.services.QPAY_ServicePaymentResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

public class Fragment_pago_servicio_ticket extends HFragment implements IMenuContext {

    private String data;

    private QPAY_ServicePaymentResponse serviceResponse;
    private String carrier;

    private QPAY_LoanResponse loanQueryResponse;

    private boolean isLoanService;

    private String loan1;
    private String serviceName = "";

    public static Fragment_pago_servicio_ticket newLoanInstance(boolean loanAfterQuery, Object... data) {
        Fragment_pago_servicio_ticket fragment = new Fragment_pago_servicio_ticket();

        Bundle args = new Bundle();

        args.putString("IS_LOAN_SERVICE", "1");

        if(data.length > 0)
            args.putString("Fragment_pago_servicio_ticket", new Gson().toJson(data[0]));

        if(data.length > 1)
            args.putString("Fragment_pago_servicio_ticket_1", (String) data[1]);

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_pago_servicio_ticket newInstance(Object... data) {
        Fragment_pago_servicio_ticket fragment = new Fragment_pago_servicio_ticket();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_pago_servicio_ticket", (String) data[0]);

        if(data.length > 1)
            args.putString("Fragment_pago_servicio_ticket_1", (String) data[1]);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loan1 = getArguments().getString("IS_LOAN_SERVICE");

        if(loan1 != null)
            isLoanService = true;

        if (getArguments() != null)
            data = getArguments().getString("Fragment_pago_servicio_ticket");

        if (getArguments() != null)
            carrier = getArguments().getString("Fragment_pago_servicio_ticket_1");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(isLoanService)
            return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_ticket_1, container, false),R.drawable.background_splash_header_1);
        else
            return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_ticket_2, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        TextView title = getView().findViewById(R.id.text_title);

        if(isLoanService) {
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_LoanResponse.QPAY_LoanResponseExcluder()).create();
            loanQueryResponse = gson.fromJson(data, QPAY_LoanResponse.class);
            Logger.d(new Gson().toJson(loanQueryResponse));
            title.setText(Html.fromHtml("<br>Disposición<b>Exitosa</b>"));
        }
        else{
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_ServicePaymentResponse.QPAY_ServicePaymentResponseExcluder()).create();
            serviceResponse = gson.fromJson(data, QPAY_ServicePaymentResponse.class);
            Logger.d(new Gson().toJson(serviceResponse));
        }

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        Button btn_compartir = getView().findViewById(R.id.btn_compartir);
        btn_compartir.setVisibility(View.GONE);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareVoucherServicios(getContext(), serviceResponse.getQpay_object()[0].getTransactionId(), serviceResponse.getQpay_object()[0].getRequestId());
            }
        });

        //TextView activity_title = getView().findViewById(R.id.activity_title);

        if(isLoanService){
            //PRESTAMO FIN EN COMÚN
            btn_compartir.setVisibility(View.GONE);

           // activity_title.setText("Abono exitoso");

            TextView monto_recarga          = getView().findViewById(R.id.monto_recarga);
            //TextView numero_recarga         = view.findViewById(R.id.numero_recarga);
            TextView autorizacion_recarga   = getView().findViewById(R.id.autorizacion_recarga);
            //TextView folio_recarga          = view.findViewById(R.id.folio_recarga);
            TextView transaccion_recarga    = getView().findViewById(R.id.transaccion_recarga);
            TextView tienda_recarga         = getView().findViewById(R.id.tienda_recarga);
            TextView fecha_recarga          = getView().findViewById(R.id.fecha_recarga);
            TextView dudas_recargas         = getView().findViewById(R.id.dudas_recargas);

            monto_recarga.setText("$"+loanQueryResponse.getAmount()+" "+loanQueryResponse.getCurrency().replace("484", "MXN"));
            autorizacion_recarga.setText(loanQueryResponse.getVendorReference());
            transaccion_recarga.setText(loanQueryResponse.getTransactionId());
            fecha_recarga.setText(loanQueryResponse.getDateTime());
            tienda_recarga.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());

            dudas_recargas.setText("");

            /*String monto = loanQueryResponse.getAmount();
            String comision = loanQueryResponse.getInterestDue();
            String utilidad = serviceResponse.getQpay_object()[0].getCommission() != null ?
                    serviceResponse.getQpay_object()[0].getCommission() : "000";*/

            /*txt_comision.setText(Utils.paserCurrency(comision));
            txt_utilidad.setText(Utils.paserCurrency(utilidad));

            txt_monto.setText(Utils.paserCurrency(monto));
            txt_toltal.setText(Utils.paserCurrency(total));

            txt_referencia.setText(serviceResponse.getQpay_object()[0].getAccountNumber());
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(serviceResponse.getCreatedAt()));*/
        }
        else
        {
            //PAGO DE SERVICIOS
            TextView txt_referencia = getView().findViewById(R.id.txt_referencia);
            TextView txt_monto = getView().findViewById(R.id.txt_monto);
            TextView txt_comision = getView().findViewById(R.id.txt_comision);
            TextView txt_utilidad = getView().findViewById(R.id.txt_utilidad);
            TextView txt_toltal = getView().findViewById(R.id.txt_toltal);
            TextView txt_autorizacion = getView().findViewById(R.id.txt_autorizacion);
            TextView txt_transaccion = getView().findViewById(R.id.txt_transaccion);
            TextView txt_tienda = getView().findViewById(R.id.txt_tienda);
            TextView txt_fecha = getView().findViewById(R.id.txt_fecha);
            TextView dudas_recargas = getView().findViewById(R.id.dudas_recargas);
            TextView txt_ticket = getView().findViewById(R.id.txt_ticket);

            LinearLayout layout_utilidad = getView().findViewById(R.id.layout_utilidad);
            LinearLayout layout_comision = getView().findViewById(R.id.layout_comision);


            String monto = serviceResponse.getQpay_object()[0].getAmount() != null ?
                    serviceResponse.getQpay_object()[0].getAmount() : "000";

            monto = getMonto(monto);

            String comision = serviceResponse.getQpay_object()[0].getFlatFee() != null ?
                    serviceResponse.getQpay_object()[0].getFlatFee() : "000";

            comision = getMonto(comision);

            String utilidad = serviceResponse.getQpay_object()[0].getCommission() != null ?
                    serviceResponse.getQpay_object()[0].getCommission() : "000";

            utilidad = getMonto(utilidad);

            String ticketText = serviceResponse.getQpay_object()[0].getTicket_text();
            txt_ticket.setVisibility(View.GONE);

            if (ticketText != null && !ticketText.isEmpty()) {
                txt_ticket.setVisibility(View.VISIBLE);
                txt_ticket.setText(ticketText);
        }



            if (Double.parseDouble(comision) == 0)
                layout_comision.setVisibility(View.GONE);

            if (Double.parseDouble(utilidad) == 0)
                layout_utilidad.setVisibility(View.GONE);

            String total = (Double.parseDouble(monto) + Double.parseDouble(comision)) + "";

            txt_comision.setText(Utils.paserCurrency(comision));
            txt_utilidad.setText(Utils.paserCurrency(utilidad));

            txt_monto.setText(Utils.paserCurrency(monto));
            txt_toltal.setText(Utils.paserCurrency(total));

            //20200902 RSB. Se valida si no viene el accountNumber se toma billReference
            String referencia = serviceResponse.getQpay_object()[0].getAccountNumber();
            referencia = (referencia!=null && !referencia.isEmpty() ? referencia : serviceResponse.getQpay_object()[0].getBillReference());
            txt_referencia.setText(referencia);
            txt_tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            txt_fecha.setText(Utils.formatDate(serviceResponse.getCreatedAt()));

            if (Globals.kineto) {
                if (serviceResponse.getQpay_object()[0].getProviderAuth() != null &&
                        !serviceResponse.getQpay_object()[0].getProviderAuth().isEmpty())
                    txt_autorizacion.setText(serviceResponse.getQpay_object()[0].getProduct().toUpperCase() + "  " +
                            serviceResponse.getQpay_object()[0].getProviderAuth());
                else
                    txt_autorizacion.setText(serviceResponse.getQpay_object()[0].getProduct().toUpperCase() + "  " +
                            serviceResponse.getQpay_object()[0].getVendorReference());
            } else {
                if (carrier.equals("06")) {
                    //210703 RSB. Pendings. Dish en ticket
                    txt_autorizacion.setText(getString(R.string.text_dish).toUpperCase() + " " + serviceResponse.getQpay_object()[0].getVendorReference());
                } else {
                    if (serviceResponse.getQpay_object()[0].getProviderAuth() != null &&
                            !serviceResponse.getQpay_object()[0].getProviderAuth().isEmpty())
                        txt_autorizacion.setText(serviceResponse.getQpay_object()[0].getProduct().toUpperCase() + "  " +
                                serviceResponse.getQpay_object()[0].getProviderAuth());
                    else
                        txt_autorizacion.setText(serviceResponse.getQpay_object()[0].getProduct().toUpperCase() + "  " +
                                serviceResponse.getQpay_object()[0].getVendorReference());
                }

            }

            txt_transaccion.setText(serviceResponse.getQpay_object()[0].getRequestId());

            switch (carrier) {

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

            //Impresion de ticket
            if(Tools.isN3Terminal()){
                //N3_FLAG_COMMENT

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        List<FormattedLine> lines = new ArrayList<FormattedLine>();
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"CARGO EXITOSO"));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,serviceName));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Referencia: " + txt_referencia.getText().toString()));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Monto: " + txt_monto.getText()));
                        if(txt_comision.getVisibility() != getView().GONE)
                            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Comisión: " + txt_comision.getText()));
                        if(txt_utilidad.getVisibility() != getView().GONE)
                            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"C.T.: " + txt_utilidad.getText()));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"_____: "));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Total: " + txt_toltal.getText()));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));


                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + txt_autorizacion.getText()));

                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,txt_fecha.getText().toString()));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

                        if(txt_ticket.getVisibility() != getView().GONE){
                            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,txt_ticket.getText().toString()));
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

                        PrinterManager printerManager = new PrinterManager(getActivity());
                        printerManager.printFormattedTicket(lines, getContext());//N3_BACKUP

                    }
                }, 1000);

            }

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

