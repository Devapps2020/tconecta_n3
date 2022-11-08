package com.blm.qiubopay.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.models.bimbo.BrandDTO;
import com.blm.qiubopay.models.bimbo.CatProducts;
import com.blm.qiubopay.models.bimbo.ProductoDTO;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.devapps.utils.components.HActivity;

public class MarcasAdapter extends RecyclerView.Adapter <MarcasAdapter.ViewHolder> {

    private List<BrandDTO> data;
    private HActivity context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView brand_img;

        public ViewHolder(View v) {
            super(v);
            brand_img = v.findViewById(R.id.img_marca);
        }
    }

    public MarcasAdapter(List<BrandDTO> data, HActivity context) {
        this.data = data;
        this.context = context;
    }

    public MarcasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marca_bimbo, parent, false);
        MarcasAdapter.ViewHolder vh = new MarcasAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder (MarcasAdapter.ViewHolder holder, int position) {

        BrandDTO brand = data.get(position);

        holder.brand_img.setImageDrawable(Utils.getDrawable( "img_" + brand.getBrand_id(), context));

        holder.brand_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProductos(brand.getMarca());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NewApi")
    public void setProductos(Globals.MARCAS brand) {
        SessionApp.getInstance().setMarca(brand);
        SessionApp.getInstance().setCatProducts(new CatProducts(getProductsBrand(context, brand.ordinal())));
    }

    public static List<ProductoDTO> getProductsBrand(HActivity context, Integer id_brand) {
        try {
            Map<String, Object> where = new HashMap<>();
            where.put("id_brand", id_brand);
            HDatabase db = new HDatabase(context);
            return db.query(ProductoDTO.class, where);
        } catch (SQLException e) {
            return new ArrayList();
        }
    }

}
