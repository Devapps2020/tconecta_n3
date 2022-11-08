package com.blm.qiubopay.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.pidelo.LineItem;
import com.blm.qiubopay.models.recarga.CompaniaDTO;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HActivity;

public class CompaniasAdapter extends RecyclerView.Adapter<CompaniasAdapter.ViewHolder> {

    private HActivity context;
    private List<CompaniaDTO> listCompanias;
    private List<CompaniaDTO> listCompaniasCopy;
    private CompaniasAdapter.ListItemClickListener mClick;

    public interface ListItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    public CompaniasAdapter(HActivity context, List<CompaniaDTO> listCompanias, ListItemClickListener mClick) {
        this.context = context;
        this.listCompanias = listCompanias;
        this.listCompaniasCopy = new ArrayList<>();
        this.listCompaniasCopy.addAll(listCompanias);
        this.mClick = mClick;
    }

    @NonNull
    @Override
    public CompaniasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logo_compania, parent, false);
        return new CompaniasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompaniasAdapter.ViewHolder holder, int position) {
        CompaniaDTO companiaDTO = listCompanias.get(position);
        Drawable imageFromDrawable = context.getDrawable(companiaDTO.getImage());
        holder.ivCompania.setImageDrawable(imageFromDrawable);
        holder.tvCompania.setText(companiaDTO.getName());
    }

    @Override
    public int getItemCount() {
        return listCompanias.size();
    }

    public void filtrar(String text) {
        listCompanias.clear();
        if(text.isEmpty()){
            listCompanias.addAll(listCompaniasCopy);
        } else{
            text = text.toUpperCase();
            for (CompaniaDTO item : listCompaniasCopy){
                if (item.getDescription().toUpperCase().contains(text)){
                    listCompanias.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivCompania;
        private TextView tvCompania;

        private ViewHolder(View itemView) {
            super(itemView);
            ivCompania = itemView.findViewById(R.id.iv_compania);
            tvCompania = itemView.findViewById(R.id.tv_compania);
            ivCompania.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(getAdapterPosition());
                }
            });
        }

    }

}