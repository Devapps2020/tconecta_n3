package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IGetOrders;
import com.blm.qiubopay.listeners.IGetPromotios;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.GetOrdersRequest;
import com.blm.qiubopay.models.bimbo.GetOrdersResponse;
import com.blm.qiubopay.models.bimbo.GetPromotionsRequest;
import com.blm.qiubopay.models.bimbo.GetPromotionsResponse;
import com.blm.qiubopay.models.bimbo.PromotDTO;
import com.blm.qiubopay.models.bimbo.PromotionDTO;
import com.blm.qiubopay.models.bimbo.PromotionResponse;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.devapps.utils.interfaces.IFunction;

public class Fragment_ofertas_bimbo_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;
    ListView list_ofertas;
    ArrayAdapter adapter;
    String opcion = "";
    private List<PromotDTO> list;

    public static PromotionResponse promotionResponse;

    public static Fragment_ofertas_bimbo_1 newInstance(Object... data) {
        Fragment_ofertas_bimbo_1 fragment = new Fragment_ofertas_bimbo_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_bimbo_1", new Gson().toJson(data[0]));

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

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_soporte_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_ofertas_bimbo_1, container, false);

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

        CApplication.setAnalytics(CApplication.ACTION.CB_GANA_MAS_inician);

        list_ofertas = view.findViewById(R.id.list_ofertas);
        adapter = new OfertasAdapter(context, filterPromos(2) );
        list_ofertas.setAdapter(adapter);

        configTabs();
    }

    public void configTabs() {

        LinearLayout layout_ofertas = view.findViewById(R.id.layout_ofertas);
        LinearLayout layout_historial_ofertas = view.findViewById(R.id.layout_historial_ofertas);

        LinearLayout tab_1 = view.findViewById(R.id.tab_1);
        LinearLayout tab_2 = view.findViewById(R.id.tab_2);

        View line_1 = view.findViewById(R.id.line_1);
        View line_2 = view.findViewById(R.id.line_2);

        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_ofertas.setVisibility(View.VISIBLE);
                layout_historial_ofertas.setVisibility(View.GONE);

                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                tab_1.setBackgroundColor(getContext().getResources().getColor(R.color.white_three));
                tab_2.setBackgroundColor(getContext().getResources().getColor(R.color.white_two));

                adapter = new OfertasAdapter(context, filterPromos(2) );
                list_ofertas.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPromotions(new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        layout_ofertas.setVisibility(View.GONE);
                        layout_historial_ofertas.setVisibility(View.VISIBLE);

                        line_2.setVisibility(View.VISIBLE);
                        line_1.setVisibility(View.INVISIBLE);
                        tab_2.setBackgroundColor(getContext().getResources().getColor(R.color.white_three));
                        tab_1.setBackgroundColor(getContext().getResources().getColor(R.color.white_two));

                        historial();
                    }
                });

            }
        });

    }

    public void historial() {

        ListView list_reportes = view.findViewById(R.id.list_promociones);

        ArrayAdapter adapter  = new PromosAplicadasAdapter(context, list);

        list_reportes.setAdapter(adapter);

        list_reportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Fragment_reportes_bimbo_6.promo = list.get(position);
                //context.setFragment(Fragment_reportes_bimbo_6.newInstance());
            }
        });
    }

    public List<PromotionDTO> filterPromos(int filter) {

        List<PromotionDTO> result = new ArrayList();

        for (PromotionDTO pro : promotionResponse.getQpay_object())
            if(date(pro.getIni_date(), pro.getExp_date()))
                result.add(pro);

        return result;
    }

    public boolean date(String inicio, String fin) {

        try {

            inicio = inicio.substring(0, 10).replaceAll("-", "/");
            fin = fin.substring(0, 10).replaceAll("-", "/");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date dateActual = new Date();
            Date dateInicio = formatter.parse(inicio);
            Date dateFin = formatter.parse(fin);

            return dateActual.after(dateInicio) && dateActual.before(dateFin);

        } catch (ParseException e) { }

        return true;
    }

    public class OfertasAdapter extends ArrayAdapter<PromotionDTO> {

        List<PromotionDTO> promos;

        public OfertasAdapter(Context context, List<PromotionDTO> promos) {
            super(context, 0, promos);
            this.promos = promos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PromotionDTO promotion = promos.get(position);

            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto_oferta_bimbo , parent, false);

            ImageView img_producto = convertView.findViewById(R.id.img_producto);
            TextView text_name = convertView.findViewById(R.id.text_name);
            TextView text_fecha = convertView.findViewById(R.id.text_fecha);
            TextView text_monto = convertView.findViewById(R.id.text_monto);

            Utils.setImagePromotion(promotion.getPromotion_code() + "", img_producto);

            text_name.setText(promotion.getPromotion_name());
            text_fecha.setText("Vence el " + promotion.getExp_date().split(" ")[0]);
            text_monto.setText(Utils.paserCurrency(promotion.getPiece_priece() + ""));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_ofertas_bimbo_2.promotion = promotion;
                    context.setFragment(Fragment_ofertas_bimbo_2.newInstance());
                }
            });

            return convertView;
        }
    }

    public void getPromotions(final IFunction function){

        context.loading(true);

        try {

            GetPromotionsRequest seed = new GetPromotionsRequest();
            seed.setRetailerId(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            seed.setEndDate(null);
            seed.setInitDate(null);

            IGetPromotios petition  = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new Gson();
                        GetPromotionsResponse response = gson.fromJson(gson.toJson(result), GetPromotionsResponse.class);
                        Logger.d(gson.toJson(response));

                        if("true".equals(response.getQpay_response())){

                            if(response.getQpay_object() == null || response.getQpay_object().isEmpty()) {
                                context.alert("Sin promociones aplicadas");
                                return;
                            }

                            list = response.getQpay_object();

                            if(function != null)
                                function.execute();

                        } else {
                            MenuActivity.validaSesion(context, response.getQpay_code(), response.getQpay_description());
                        }


                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);

                    context.alert(R.string.general_error);
                }

            }, context);

            petition.getPromotionReportByRetailerId(seed);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    public void getOrders(final IFunction function){

        context.loading(true);

        try {

            GetOrdersRequest seed = new GetOrdersRequest();
            seed.setRetailerId(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            seed.setEndDate(null);
            seed.setInitDate(null);

            IGetOrders petition  = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new Gson();
                        GetOrdersResponse response = gson.fromJson(gson.toJson(result), GetOrdersResponse.class);
                        Logger.d(gson.toJson(response));

                        if("true".equals(response.getQpay_response())){

                            if(response.getQpay_object() == null || response.getQpay_object().isEmpty()) {
                                context.alert("Sin pedidos aplicados");
                                return;
                            }


                            if(function != null)
                                function.execute();

                        } else {
                            MenuActivity.validaSesion(context, response.getQpay_code(), response.getQpay_description());
                        }


                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);

                    context.alert(R.string.general_error);
                }

            }, context);

            petition.getOrderByRetailerId(seed);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    public class PromosAplicadasAdapter extends ArrayAdapter<PromotDTO> {

        public PromosAplicadasAdapter(Context context, List<PromotDTO> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PromotDTO promotion = list.get(position);

            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto_oferta_bimbo , parent, false);

            ImageView img_producto = convertView.findViewById(R.id.img_producto);
            TextView text_name = convertView.findViewById(R.id.text_name);
            TextView text_fecha = convertView.findViewById(R.id.text_fecha);
            TextView text_monto = convertView.findViewById(R.id.text_monto);

            Utils.setImagePromotion(promotion.getPromotionCode() + "", img_producto);

            text_name.setText(promotion.getPromotionName());
            text_fecha.setText("Cantidad: " + promotion.getQuantity() + "\nFecha: " + promotion.getRequestDate().split(" ")[0] + "\n" + "Estatus: " + promotion.getStatusDescription());
            text_monto.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            return convertView;
        }
    }

}

