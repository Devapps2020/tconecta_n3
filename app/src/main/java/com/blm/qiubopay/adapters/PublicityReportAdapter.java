package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicityReportAdapter extends RecyclerView.Adapter<PublicityReportAdapter.ViewHolder> {

    private List<QPAY_TipsAdvertising_publicities> publicityItems;
    private PublicityReportAdapter.ListPublicityClickListener mClick;

    public interface ListPublicityClickListener {
        void onItemClick(int clickedItemIndex);
    }

    public PublicityReportAdapter(ListPublicityClickListener mClick) {
        this.mClick = mClick;
    }

    public void setData(List<QPAY_TipsAdvertising_publicities> reportItems) {
        this.publicityItems = reportItems;
    }

    @NonNull
    @Override
    public PublicityReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.publicity_item_layout, viewGroup, false);
        return new PublicityReportAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PublicityReportAdapter.ViewHolder holder, int i) {

        final QPAY_TipsAdvertising_publicities item = publicityItems.get(i);

        Picasso.get().load(item.getImage()).into(holder.ivPublicity);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return publicityItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cvPublicity;
        private ImageView ivPublicity;
        private TextView tvTitle;
        private TextView tvDescription;

        private ViewHolder(View view){
            super(view);
            cvPublicity = view.findViewById(R.id.card_publicity);
            ivPublicity = view.findViewById(R.id.iv_publicity);
            tvTitle = view.findViewById(R.id.tv_title_publicity);
            tvDescription = view.findViewById(R.id.tv_description_publicity);
            cvPublicity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(getAdapterPosition());
                }
            });

        }


    }
}

