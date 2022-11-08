package com.blm.qiubopay.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<String> categoriesList;

    public void setData(List<String> categoriesList) {this.categoriesList = categoriesList; }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.categories_item, viewGroup, false);
        return new CategoriesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int i) {
        if (i == 0){
            Context context = holder.categoryBtn.getContext();
            holder.categoryBtn.setText(categoriesList.get(i));
            holder.categoryBtn.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPidelo));
            holder.categoryBtn.setTextColor(Color.WHITE);
        }else{
            holder.categoryBtn.setText(categoriesList.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button categoryBtn;

        private ViewHolder(View view){
            super(view);
            categoryBtn = view.findViewById(R.id.categoryItem);
        }

    }
}