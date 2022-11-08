package com.blm.qiubopay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableMap;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.modules.Fragment_proveedor_bimbo_2;
import com.blm.qiubopay.utils.Utils;

import java.util.List;

import mx.devapps.utils.components.HActivity;

public class VendedoresAdapter extends RecyclerView.Adapter <VendedoresAdapter.ViewHolder> {

    private List<SellerUserDTO> data;
    private HActivity context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView main_layout;
        ImageView profile_image;
        TextView text_nombre;

        ImageView image_star_1;
        ImageView image_star_2;
        ImageView image_star_3;
        ImageView image_star_4;
        ImageView image_star_5;

        public ViewHolder(View v) {
            super(v);

            main_layout = v.findViewById(R.id.main_layout);
            profile_image = v.findViewById(R.id.profile_image);
            text_nombre = v.findViewById(R.id.text_nombre);

            image_star_1 = v.findViewById(R.id.image_star_1);
            image_star_2 = v.findViewById(R.id.image_star_2);
            image_star_3 = v.findViewById(R.id.image_star_3);
            image_star_4 = v.findViewById(R.id.image_star_4);
            image_star_5 = v.findViewById(R.id.image_star_5);

        }

        public void activateStar(Context context, int valor){

            image_star_1.setColorFilter(context.getResources().getColor(R.color.colorBimboGrayLight2));
            image_star_2.setColorFilter(context.getResources().getColor(R.color.colorBimboGrayLight2));
            image_star_3.setColorFilter(context.getResources().getColor(R.color.colorBimboGrayLight2));
            image_star_4.setColorFilter(context.getResources().getColor(R.color.colorBimboGrayLight2));
            image_star_5.setColorFilter(context.getResources().getColor(R.color.colorBimboGrayLight2));

            switch (valor) {
                case 5:
                    image_star_5.setColorFilter(context.getResources().getColor(R.color.colorBimboStar));
                case 4:
                    image_star_4.setColorFilter(context.getResources().getColor(R.color.colorBimboStar));
                case 3:
                    image_star_3.setColorFilter(context.getResources().getColor(R.color.colorBimboStar));
                case 2:
                    image_star_2.setColorFilter(context.getResources().getColor(R.color.colorBimboStar));
                case 1:
                    image_star_1.setColorFilter(context.getResources().getColor(R.color.colorBimboStar));
            }
        }
    }

    public VendedoresAdapter(List<SellerUserDTO> data, HActivity context) {
        this.data = data;
        this.context = context;
    }

    public VendedoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repartidor, parent, false);
        VendedoresAdapter.ViewHolder vh = new VendedoresAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder (VendedoresAdapter.ViewHolder holder, int position) {

        SellerUserDTO seller = data.get(position);

        holder.text_nombre.setText(seller.getSeller_name());
        Utils.setImageSeller(seller.getSeller_id() + "", holder.profile_image);
        holder.activateStar(context, Integer.parseInt(("" + seller.getSeller_qualify()).split("\\.")[0] ));

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CApplication.setAnalytics(CApplication.ACTION.CB_EVALUANOS_seleccionan, ImmutableMap.of(CApplication.ACTION.REPARTIDOR.name(), seller.getSeller_id()));

                Fragment_proveedor_bimbo_2.seller = seller;
                context.setFragment(Fragment_proveedor_bimbo_2.newInstance());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
