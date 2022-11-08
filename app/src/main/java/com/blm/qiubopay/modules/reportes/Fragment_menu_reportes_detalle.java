package com.blm.qiubopay.modules.reportes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.database.model.QP_BD_ROW;
import com.blm.qiubopay.database.model.SERVICE_BD_ROW;
import com.blm.qiubopay.database.model.TAE_BD_ROW;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxr;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.Fragment_transacciones_3;
import com.blm.qiubopay.modules.Fragment_transacciones_4;
import com.blm.qiubopay.modules.Fragment_transacciones_5;
import com.blm.qiubopay.modules.Fragment_transacciones_6;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.regional.Fragment_pago_regional_menu;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_menu_reportes_detalle extends HFragment implements IMenuContext {

    public static Integer tipo;

    private ArrayAdapter adapter;
    private ListView list_operations;
    public List<Object> list;

    private String url = "";
    private String time = "";
    private String date = "";

    public static Fragment_menu_reportes_detalle newInstance() {
        return new Fragment_menu_reportes_detalle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_reportes_detalle, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .showLogo()
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        //RSB 20191127/20211021. Cambio de etiqueta
        TextView tvTransactionsTitle = getView().findViewById(R.id.tv_transactions_title);
        switch (tipo) {
            case 1: //Transacciones Recargas
                tvTransactionsTitle.setText(R.string.text_operativas_2);
                break;
            case 2: //Transacciones Pago de Servicios
                tvTransactionsTitle.setText(R.string.text_operativas_3);
                break;
            case 3: //Transacciones Financieras
                tvTransactionsTitle.setText(R.string.text_operativas_4);
                break;
            case 4: //Transacciones Pagos Qiubo
                tvTransactionsTitle.setText(R.string.text_operativas_6);
                break;
            case 5://Transacciones de cargos y abonos
                tvTransactionsTitle.setText(R.string.text_operativas_5);
                break;
        }

        list = Fragment_menu_reportes.list;

        list_operations = getView().findViewById(R.id.list_transacciones);

        adapter  = new TransaccionesAdapter(getContext(), list, tipo);
        list_operations.setAdapter(adapter);

        list_operations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                nullObjects();

                switch (tipo) {
                    case 1: //Transacciones Recargas

                        if(Globals.LOCAL_TRANSACTION){
                            TAE_BD_ROW tae_bd_row = (TAE_BD_ROW) list.get(position);
                            Fragment_transacciones_3.tae_bd_row = tae_bd_row;
                            getContext().setFragment(Fragment_transacciones_3.newInstance(tae_bd_row));
                        }
                        else {
                            TaeSale tae = new Gson().fromJson(new Gson().toJson(list.get(position)), TaeSale.class); //#3
                            String json = tae.getQpay_rspBody().replace("\\", "");
                            tae.setDynamicData(new Gson().fromJson(json, TaeSale.class).getDynamicData());
                            Fragment_transacciones_3.data = tae;
                            getContext().setFragment(Fragment_transacciones_3.newInstance(tae));
                        }
                        break;
                    case 2: //Transacciones Pago de Servicios
                        if(Globals.LOCAL_TRANSACTION){
                            SERVICE_BD_ROW service_bd_row = (SERVICE_BD_ROW) list.get(position);
                            Fragment_transacciones_4.service_bd_row = service_bd_row;
                            getContext().setFragment(Fragment_transacciones_4.newInstance(service_bd_row));
                        }
                        else {
                            ServicePayment service = new Gson().fromJson(new Gson().toJson(list.get(position)), ServicePayment.class);
                            String jsonn = service.getQpay_rspBody().replace("\\", "");
                            service.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());

                            service.getDynamicData().setAmount(service.getQpay_amount());

                            String service_product = getServiceName(service.getQpay_product());
                            service.setProduct(service_product);

                            Fragment_transacciones_4.data = service;
                            getContext().setFragment(Fragment_transacciones_4.newInstance(service));
                        }

                        break;
                    case 3:

                        if (Globals.LOCAL_TRANSACTION) {
                            FINANCIAL_BD_ROW financial_bd_row = (FINANCIAL_BD_ROW) list.get(position);
                            Fragment_transacciones_5.financial_bd_row = financial_bd_row;
                            getContext().setFragment(Fragment_transacciones_5.newInstance(financial_bd_row));
                        } else {
                            QPAY_VisaEmvResponse visaEmvResponse = new Gson().fromJson(new Gson().toJson(list.get(position)), QPAY_VisaEmvResponse.class);
                            Fragment_transacciones_5.data = visaEmvResponse;
                            getContext().setFragment(Fragment_transacciones_5.newInstance(visaEmvResponse));
                        }

                        break;
                    case 4: //Transacciones PagosConecta
                        if (Globals.LOCAL_TRANSACTION) {
                            QP_BD_ROW qp_bd_row = (QP_BD_ROW) list.get(position);
                            getContext().setFragment(Fragment_transacciones_6.newInstance(qp_bd_row));
                        } else {
                            QPAY_Pago_Qiubo_object pago_qiubo_object = new Gson().fromJson(new Gson().toJson(list.get(position)), QPAY_Pago_Qiubo_object.class);

                            Fragment_transacciones_6.response = pago_qiubo_object;

                            getContext().setFragment(Fragment_transacciones_6.newInstance());
                        }
                        break;

                    case 5://Transacciones de cargos y abonos

                        break;
                }

            }
        });


    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class TransaccionesAdapter extends ArrayAdapter<Object> {

        int tipo = 0;
        List<Object> operaciones;

        public TransaccionesAdapter(Context context, List<Object> operaciones, Integer tipo) {
            super(context, 0, operaciones);
            this.tipo = tipo;
            this.operaciones = operaciones;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaccion, parent, false);

            TextView text_titulo = (TextView) convertView.findViewById(R.id.text_titulo);
            TextView text_fecha = (TextView) convertView.findViewById(R.id.text_fecha);
            TextView text_amount = (TextView) convertView.findViewById(R.id.text_amount);
            ImageView img_operation = convertView.findViewById(R.id.img_operation);

            switch (tipo){
                case 1:
                    String carrier = "";

                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_tiempo_aire_60_x_60));

                    if (Globals.LOCAL_TRANSACTION) {
                        TAE_BD_ROW tae_bd_row = (TAE_BD_ROW) getItem(position);
                        if(null != tae_bd_row.getResponse1()) {
                            carrier = getCarrierName(tae_bd_row.getCarrier());

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(tae_bd_row.getResponse1().getQpay_object()[0].getAmount()));
                            text_fecha.setText(Utils.formatDate(tae_bd_row.getResponse1().getCreatedAt()));

                        }else if(null != tae_bd_row.getResponse2()) {
                            TaeSale tae = tae_bd_row.getResponse2();

                            if (tae.getDynamicData() == null) {
                                String jsonn = tae.getQpay_rspBody().replace("\\", "");
                                tae.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());

                                tae_bd_row.setResponse2(tae);
                            }

                            carrier = getCarrierName(tae.getQpay_carrier());

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(tae.getQpay_amount()));
                            text_fecha.setText(Utils.formatDate(tae.getResponseAt()));
                        }
                    }
                    else {
                        TaeSale tae = new Gson().fromJson(new Gson().toJson(getItem(position)), TaeSale.class);

                        if (tae.getDynamicData() == null) {
                            String jsonn = tae.getQpay_rspBody().replace("\\", "");
                            tae.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());
                        }

                        carrier = getCarrierName(tae.getQpay_carrier());

                        text_titulo.setText(carrier);
                        text_amount.setText(Utils.paserCurrency(tae.getQpay_amount()));
                        text_fecha.setText(Utils.formatDate(tae.getResponseAt()));
                    }
                    break;
                case 2: {


                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_pago_servicios));

                    if (Globals.LOCAL_TRANSACTION) {
                        SERVICE_BD_ROW service_bd_row = (SERVICE_BD_ROW) getItem(position);
                        if(null != service_bd_row.getResponse1()) {
                            carrier = getServiceName(service_bd_row.getType());

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(service_bd_row.getResponse1().getQpay_object()[0].getAmount()));
                            text_fecha.setText(Utils.formatDate(service_bd_row.getResponse1().getCreatedAt()));
                        }else if(null != service_bd_row.getResponse2()) {
                            ServicePayment service = service_bd_row.getResponse2();

                            if (service.getDynamicData() == null) {
                                String jsonn1 = service.getQpay_rspBody().replace("\\", "");
                                service.setDynamicData(new Gson().fromJson(jsonn1, TaeSale.class).getDynamicData());

                                service_bd_row.setResponse2(service);
                            }

                            String service_product = getServiceName(service.getQpay_product());

                            text_titulo.setText(service_product);
                            text_amount.setText(Utils.paserCurrency(service.getQpay_amount()));

                            if (service.getDynamicData().getTimestamp() == null)
                                service.getDynamicData().setTimestamp(Utils.formatDate(service.getDynamicData().getTimestamp()));

                            text_fecha.setText(Utils.formatDate(service.getResponseAt()));
                        }
                    } else {
                        ServicePayment service = new Gson().fromJson(new Gson().toJson(getItem(position)), ServicePayment.class);

                        Logger.e(new Gson().toJson(service));

                        if (service.getDynamicData() == null) {
                            String jsonn1 = service.getQpay_rspBody().replace("\\", "");
                            service.setDynamicData(new Gson().fromJson(jsonn1, TaeSale.class).getDynamicData());
                        }

                        String service_product = getServiceName(service.getQpay_product());

                        String monto = service.getQpay_amount() != null ? service.getQpay_amount() : "000";
                        //QFix. 210510 RSB. Dado que el monto para netwey regresa en entero y no en base 100 o double como todos los demas se tiene que parsear
                        if(service.getQpay_product().compareTo("14")==0){
                            monto = monto.concat("00");
                        }
                        monto = getMonto(monto);

                        String comision = service.getDynamicData().getFlatFee() != null ? service.getDynamicData().getFlatFee() : "000";
                        comision = getMonto(comision);
                        String total = (Double.parseDouble(monto) + ""); //+ Double.parseDouble(comision)) + "";

                        text_titulo.setText(service_product);
                        text_amount.setText(Utils.paserCurrency(total));

                        if (service.getDynamicData().getTimestamp() == null)
                            service.getDynamicData().setTimestamp(Utils.formatDate(service.getDynamicData().getTimestamp()));

                        text_fecha.setText(Utils.formatDate(service.getResponseAt()));
                    }
                }
                break;
                case 3:
                {

                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_cobro_con_tarjeta));

                    String name = "";

                    if (Globals.LOCAL_TRANSACTION) {
                        FINANCIAL_BD_ROW financial_bd_row = (FINANCIAL_BD_ROW) getItem(position);
                        if(null != financial_bd_row.getResponse1()) {
                            if (financial_bd_row.getType().equals("SELL"))
                                name = "VENTA";
                            else if (financial_bd_row.getType().equals("CASHBACK"))
                                name = "RETIRO DE EFECTIVO";
                            else if (financial_bd_row.getType().equals("CANCELATION"))
                                name = "CANCELACIÓN";

                            text_titulo.setText(name);
                            text_amount.setText(Utils.paserCurrency(financial_bd_row.getResponse1().getTransactionResponse().getAmount().toString()));
                            text_fecha.setText(Utils.formatDate(financial_bd_row.getResponse1().getTransactionResponse().getDate() + " " + financial_bd_row.getResponse1().getTransactionResponse().getTime()));
                        }else if(null != financial_bd_row.getResponse2()) {
                            QPAY_VisaEmvResponse visaEmvResponse = financial_bd_row.getResponse2();

                            text_titulo.setText(visaEmvResponse.getName());
                            text_amount.setText(Utils.paserCurrency(visaEmvResponse.getCspBody().getAmt()));
                            text_fecha.setText(Utils.formatDate(visaEmvResponse.getCspBody().getTxDate()));
                        }
                    } else {
                        QPAY_VisaEmvResponse visaEmvResponse = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_VisaEmvResponse.class);

                        if (visaEmvResponse.getCspHeader().getProductId().trim().equals("13001")) {
                            if (visaEmvResponse.getCspHeader().getTxTypeId().equals("140"))
                                name = "CANCELACIÓN";
                            else
                                name = "VENTA";
                        } else if (visaEmvResponse.getCspHeader().getProductId().trim().equals("13005"))
                            name = "RETIRO DE EFECTIVO";

                        visaEmvResponse.setName(name);

                        operaciones.set(position, visaEmvResponse);

                        text_titulo.setText(name);
                        text_amount.setText(Utils.paserCurrency(visaEmvResponse.getCspBody().getAmt()));
                        text_fecha.setText(Utils.formatDate(visaEmvResponse.getCspBody().getTxDate()));
                    }
                }
                break;
                case 4:

                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_pagos_regionales));

                    if (Globals.LOCAL_TRANSACTION) {
                        QP_BD_ROW qp_bd_row = (QP_BD_ROW) getItem(position);

                        if (null != qp_bd_row.getResponse1()) {

                            //QPAY_Pago_Qiubo_object pago_qiubo_object = qp_bd_row.getResponse2();//new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                            String producto = Fragment_pago_regional_menu.getNameProduct(qp_bd_row.getResponse1().getClaveQiubo().substring(0, 4));

                            producto = producto.isEmpty() ? Fragment_pago_regional_menu.getNameProduct(qp_bd_row.getResponse1().getClaveQiubo().substring(0, 3)) : producto;

                            text_titulo.setText(producto);
                            text_amount.setText(Utils.paserCurrency(qp_bd_row.getResponse1().getAmount()));
                            text_fecha.setText(Utils.formatDate(qp_bd_row.getResponse1().getTimestamp()));
                        }else if(null != qp_bd_row.getResponse2()) {

                            QPAY_Pago_Qiubo_object pago_qiubo_object = qp_bd_row.getResponse2();//new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                            String producto = Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 4));

                            producto = producto.isEmpty() ? Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3)) : producto;
                            text_titulo.setText(producto);
                            text_amount.setText(Utils.paserCurrency(pago_qiubo_object.getAmount()));

                            text_fecha.setText(Utils.formatDate(pago_qiubo_object.getTimestamp()));
                        }
                    }else {

                        QPAY_Pago_Qiubo_object pago_qiubo_object = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                        String producto = Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3));

                        //producto = producto.isEmpty() ? Fragment_pago_qiubo_1.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3)) : producto;

                        text_titulo.setText(producto);
                        text_amount.setText(Utils.paserCurrency(pago_qiubo_object.getAmount()));
                        text_fecha.setText(Utils.formatDate(pago_qiubo_object.getTimestamp()));
                    }

                    break;
                case 5://Transacciones de cargos y abonos

                    QPAY_CreditAndDebitTxr object = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_CreditAndDebitTxr.class);

                    String product = object.getOperationType();//Fragment_pago_qiubo_1.getNameProduct(object.getClaveQiubo().substring(0,4));

                    //producto = product.isEmpty() ? Fragment_pago_qiubo_1.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0,3)) : producto;

                    text_titulo.setText(product);
                    text_amount.setText(Utils.paserCurrency(object.getAmount()));
                    text_fecha.setText(Utils.formatDate(object.getResponseAt()));

                    //20200811 RSB. Desaparecer flecha


                    break;
            }


            return convertView;
        }
    }

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

    private String getServiceName(String service){
        String serviceName = "";
        switch (service){
            case Globals.TELMEX_ID:
                serviceName = getString(R.string.text_telmex).toUpperCase();
                break;
            case Globals.SKY_ID:
                serviceName = getString(R.string.text_sky).toUpperCase();
                break;
            case Globals.CFE_ID:
                serviceName = getString(R.string.text_cfe).toUpperCase();
                break;
            case Globals.DISH_ID:
                serviceName = getString(R.string.text_dish).toUpperCase();
                break;
            case Globals.TELEVIA_ID:
                serviceName = getString(R.string.text_televia).toUpperCase();
                break;
            case Globals.NATURGY_ID:
                serviceName = getString(R.string.text_naturgy).toUpperCase();
                break;
            case Globals.IZZI_ID:
                serviceName = getString(R.string.text_izzi).toUpperCase();
                break;
            case Globals.MEGACABLE_ID:
                serviceName = getString(R.string.text_megacable).toUpperCase();
                break;
            case Globals.PASE_URBANO_ID:
                serviceName = getString(R.string.text_pase_urbano).toUpperCase();
                break;
            case Globals.TOTALPLAY_ID:
                serviceName = getString(R.string.text_totalplay).toUpperCase();
                break;
            case Globals.STARTV_ID:
                serviceName = getString(R.string.text_star_tv).toUpperCase();
                break;
            case Globals.CEA_QRO_ID:
                serviceName = getString(R.string.text_cea_qro).toUpperCase();
                break;
            case Globals.NETWAY_ID:
                serviceName = getString(R.string.text_netwey).toUpperCase();
                break;
            case Globals.VEOLIA_ID:
                serviceName = getString(R.string.text_veolia).toUpperCase();
                break;
            case Globals.OPDM_ID:
                serviceName = getString(R.string.text_opdm).toUpperCase();
                break;
            case Globals.POST_ATT_ID:
                serviceName = getString(R.string.text_post_att).toUpperCase();
                break;
            case Globals.POST_MOVISTAR_ID:
                serviceName = getString(R.string.text_post_movistar).toUpperCase();
                break;
            case Globals.SACMEX_ID:
                serviceName = getString(R.string.text_sacmex).toUpperCase();
                break;
            case Globals.GOB_MX_ID:
                serviceName = getString(R.string.text_gob_mx).toUpperCase();
                break;
            case Globals.SIAPA_GDL_ID:
                serviceName = getString(R.string.text_siapa_gdl).toUpperCase();
                break;
            case Globals.AYDM_MTY_ID:
                serviceName = getString(R.string.text_aydm_mty).toUpperCase();
                break;
            case Globals.GOB_EDOMEX_ID:
                serviceName = getString(R.string.text_gob_edomex).toUpperCase();
                break;
            case Globals.CESPT_TIJ_ID:
                serviceName = getString(R.string.text_cespt_tij).toUpperCase();
                break;
            case Globals.BLUE_TEL_ID:
                serviceName = getString(R.string.text_blue_tel).toUpperCase();
                break;

        }

        return serviceName;
    }

    private String getCarrierName(String carrier){
        String back = "";

        switch (carrier){
            case Globals.ID_TELCEL:
                back = getString(R.string.text_telcel).toUpperCase();
                break;
            case Globals.ID_TELCEL_DATOS:
                back = getString(R.string.text_telcel_datos).toUpperCase();
                break;
            case Globals.ID_TELCEL_INTERNET:
                back = getString(R.string.text_telcel_internet).toUpperCase();
                break;
            case Globals.ID_MOVISTAR:
                back = getString(R.string.text_movistar).toUpperCase();
                break;
            case Globals.ID_IUSACELL:
                back = getString(R.string.text_att).toUpperCase();
                break;
            case Globals.ID_UNEFON:
                back = getString(R.string.text_unefon).toUpperCase();
                break;
            case Globals.ID_VIRGIN_MOBILE:
                back = getString(R.string.text_virgin).toUpperCase();;
                break;
            case Globals.ID_MAS_RECARGA:
                back = getString(R.string.text_mas_recarga).toUpperCase();;
                break;
            case Globals.ID_TUENTI:
                back = getString(R.string.text_tuenti).toUpperCase();
                break;
            case Globals.ID_FREEDOM_POP:
                back = getString(R.string.text_freedom).toUpperCase();
                break;
            case Globals.ID_ALO:
                back = getString(R.string.text_alo).toUpperCase();
                break;
            case Globals.ID_BUENOCELL:
                back = getString(R.string.text_bueno_cell).toUpperCase();
                break;
            //210909 RSB. MVNO
            case Globals.ID_MI_MOVIL:
                back = getString(R.string.text_mi_movil).toUpperCase();
                break;
            case Globals.ID_TURBORED_TAE:
                back = getString(R.string.text_turbored).toUpperCase();
                break;
            case Globals.ID_TURBORED_DATOS:
                back = getString(R.string.text_turbored_datos).toUpperCase();
                break;
            case Globals.ID_SPACE:
                back = getString(R.string.text_space).toUpperCase();
                break;
            case Globals.ID_DIRI:
                back = getString(R.string.text_diri).toUpperCase();
                break;
            case Globals.ID_PILLOFON:
                back = getString(R.string.text_pillofon).toUpperCase();
                break;
            case Globals.ID_FOBO:
                back = getString(R.string.text_fobo).toUpperCase();
                break;
            case Globals.ID_BAIT:
                back = getString(R.string.text_bait).toUpperCase();
                break;
        }

        return back;
    }

    public static void nullObjects() {

        Fragment_transacciones_3.data = null;
        Fragment_transacciones_3.tae_bd_row = null;

        Fragment_transacciones_4.data = null;
        Fragment_transacciones_4.service_bd_row = null;

        Fragment_transacciones_5.data = null;
        Fragment_transacciones_5.financial_bd_row = null;

        Fragment_browser.execute = null;

    }

}