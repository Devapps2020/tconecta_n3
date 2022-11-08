package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.services.ServicePackageItemModel;

import java.util.List;

import mx.devapps.utils.components.HActivity;

public class ServicePackagesAdapter extends RecyclerView.Adapter<ServicePackagesAdapter.ViewHolder> {

    private HActivity context;
    private List<ServicePackageItemModel> mData;
    private ListPackageClickListener mClick;
    private boolean hideAmounts;

    public interface ListPackageClickListener {
        void onListPackageClick(int clickedItemIndex);
    }

    public ServicePackagesAdapter(HActivity context, List<ServicePackageItemModel> mData, ListPackageClickListener mClick,
                                  boolean hideAmounts) {
        this.context = context;
        this.mData = mData;
        this.mClick = mClick;
        this.hideAmounts = hideAmounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_package, parent, false);
        return new ServicePackagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServicePackageItemModel service = mData.get(position);
        holder.tvAmount.setText(service.getAmount());
        if(hideAmounts){
            holder.tvAmount.setVisibility(View.GONE);
        }
        if(service.getDescription() != null && !service.getDescription().isEmpty()){
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(service.getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvAmount;
        CardView packageCardview;

        private ViewHolder(View view) {
            super(view);
            tvDescription = view.findViewById(R.id.tv_description);
            tvAmount = view.findViewById(R.id.tv_amount);
            packageCardview = view.findViewById(R.id.package_cardview);
            packageCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onListPackageClick(getAdapterPosition());
                }
            });
        }
    }

}
