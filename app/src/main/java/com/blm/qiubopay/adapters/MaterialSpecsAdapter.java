package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;

import java.util.List;

public class MaterialSpecsAdapter extends RecyclerView.Adapter<MaterialSpecsAdapter.ViewHolder> {

    private List<String> mData;

    public MaterialSpecsAdapter(List<String> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paquete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String desc = mData.get(position);
        holder.tvDescription.setText(desc);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDescription;

        private ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.text_paquete);
        }

    }

}
