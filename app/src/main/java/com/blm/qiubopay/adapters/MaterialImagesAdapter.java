package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MaterialImagesAdapter extends RecyclerView.Adapter<MaterialImagesAdapter.ViewHolder> {

    private List<String> mData;

    public MaterialImagesAdapter(List<String> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imagen, parent, false);
        return new MaterialImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialImagesAdapter.ViewHolder holder, int position) {
        String urlImage = mData.get(position);
        if(urlImage!=null && !urlImage.isEmpty()){
            Picasso.get().load(urlImage).into(holder.ivImagen);
        }else{
            holder.ivImagen.setBackgroundColor(holder.ivImagen.getContext().getResources().getColor(R.color.coloTextPidelo));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImagen;

        private ViewHolder(View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.iv_imagen);
        }

    }

}