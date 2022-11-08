package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IPesitoCatalog;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PesitoCatalogRequest;
import com.blm.qiubopay.models.bimbo.PesitoCatalogResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.modules.Fragment_credito_bimbo_2;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.List;

import mx.devapps.utils.components.HActivity;

public class CreditoPesitoAdapter extends RecyclerView.Adapter <CreditoPesitoAdapter.ViewHolder> {

    private List<SellerUserDTO> data;
    private HActivity context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout main_layout;
        ImageView profile_image;
        TextView text_nombre;
        TextView text_dias;
        ImageView img_marca;
        LinearLayout layout_disponible;

        public ViewHolder(View v) {
            super(v);

            profile_image = v.findViewById(R.id.profile_image);
            text_nombre = v.findViewById(R.id.text_nombre);
            text_dias = v.findViewById(R.id.text_dias);
            img_marca = v.findViewById(R.id.img_marca);
            main_layout = v.findViewById(R.id.main_layout);
            layout_disponible = v.findViewById(R.id.layout_disponible);

        }
    }

    public CreditoPesitoAdapter(List<SellerUserDTO> data, HActivity context) {
        this.data = data;
        this.context = context;
    }

    public CreditoPesitoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proveedor_bimbo, parent, false);
        CreditoPesitoAdapter.ViewHolder vh = new CreditoPesitoAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder (CreditoPesitoAdapter.ViewHolder holder, int position) {

        SellerUserDTO seller = data.get(position);

        holder.img_marca.setImageDrawable(Utils.getDrawable( "img_" + seller.getBrand().getBrand_id(), context));

        String[] dias = seller.getSeller_visit_days().split(",");

        String result = "";
        for (String dato : dias)
            result =    dato + " ,";

        result = result.substring(0, result.length() - 1);

        holder.text_nombre.setText(seller.getSeller_name());
        holder.text_dias.setText("Visita: " + result);
        holder.layout_disponible.setVisibility(View.GONE);
        Utils.setImageSeller(seller.getSeller_id() + "", holder.profile_image);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Fragment_credito_bimbo_2.seller = seller;
                    sendRequestStatusCredito();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void sendRequestStatusCredito(){

        context.loading(true);

        try {

            PesitoCatalogRequest pesitoCatalogRequest = new PesitoCatalogRequest();
            pesitoCatalogRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            pesitoCatalogRequest.setSeller_id(Fragment_credito_bimbo_2.seller.getSeller_id());
            pesitoCatalogRequest.setRoute_id(Fragment_credito_bimbo_2.seller.getRoute().getRoute_id());

            CApplication.setAnalytics(CApplication.ACTION.CB_CREDITO_PESITO_seleccionan, ImmutableMap.of(CApplication.ACTION.REPARTIDOR.name(), pesitoCatalogRequest.getSeller_id()));

            IPesitoCatalog petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        context.loading(false);

                    } else {

                        context.loading(false);

                        String json = new Gson().toJson(result);
                        PesitoCatalogResponse response = new Gson().fromJson(json, PesitoCatalogResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            Fragment_credito_bimbo_2.pesitoCatalog = response;

                            if(Fragment_credito_bimbo_2.pesitoCatalog.getQpay_object() == null || Fragment_credito_bimbo_2.pesitoCatalog.getQpay_object().length == 0 || Fragment_credito_bimbo_2.pesitoCatalog.getQpay_object()[0].getSales_center_code() == null){
                                context.alert("No cuentas con crédito pesito para esta ruta, solicítalo a tu vendedor");
                                return;
                            }

                            context.setFragment(Fragment_credito_bimbo_2.newInstance());
                        } else {
                            MenuActivity.validaSesion(context, response.getQpay_code(), response.getQpay_description());

                            context.loading(false);

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.pesitoCatalog(pesitoCatalogRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

}
