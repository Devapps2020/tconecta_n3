package com.blm.qiubopay.adapters.odrer;

import static com.blm.qiubopay.activities.orders.OrderActivity.ACCEPTED;
import static com.blm.qiubopay.activities.orders.OrderActivity.ACCEPTED_BY_MERCHANT;
import static com.blm.qiubopay.activities.orders.OrderActivity.CREATED;
import static com.blm.qiubopay.activities.orders.OrderActivity.DELIVERED;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.adapters.odrer.communicator.IStatusOrderForAdapter;
import com.blm.qiubopay.models.order.OrderIds;

import java.util.ArrayList;

public class StatusOrderAdapter extends RecyclerView.Adapter<StatusOrderAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<OrderIds> mDataList;
    private IStatusOrderForAdapter communicator;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvClientNumber;
        public TextView tvDate;
        public TextView tvOrderStatus;
        public Button btnDelivered;
        public TextView tvDetail;
        int position;

        public ViewHolder(TextView v) {
            super(v);
            tvClientNumber = v;
        }

        public ViewHolder(LinearLayout v) {
            super(v);
            tvClientNumber = v.findViewById(R.id.tvClientNumber);
            tvDate = v.findViewById(R.id.tvDate);
            tvOrderStatus = v.findViewById(R.id.tvOrderStatus);
            btnDelivered = v.findViewById(R.id.btnDelivered);
            tvDetail = v.findViewById(R.id.tvDetail);
        }

        public void setData(OrderIds currentModel, int position) {
            String cellPhone = currentModel.getClient_cellphone();
            String clientName = currentModel.getClient_name();
            int orderId = currentModel.getId();
            String orderDate = currentModel.getDate();
            String orderStatus = currentModel.getStatus();
            Boolean isReadyForDeliver = reviewReadyForDeliver(currentModel.getStatus());

            this.tvClientNumber.setText("+" + cellPhone + " / " + clientName + " / " + orderId);
            this.tvDate.setText(orderDate);
            this.tvOrderStatus.setText(translateStatus(orderStatus));
            if (!isReadyForDeliver) btnDelivered.setVisibility(View.INVISIBLE);
            else btnDelivered.setVisibility(View.VISIBLE);
            this.position = position;
            btnDelivered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communicator.onDeliverOrderClickListener(currentModel.getId().toString());
                }
            });
        }

        private String translateStatus(String status) {
            if (status.equals(CREATED)) {
                return "Creado";
            } else if (status.equals(ACCEPTED_BY_MERCHANT)) {
                return "Aceptado por el tendero";
            } else if (status.equals(ACCEPTED)) {
                return "Aceptado por el cliente";
            } else if (status.equals(DELIVERED)) {
                return "Entregado";
            } else {
                return "No definido";
            }
        }

        private Boolean reviewReadyForDeliver(String status) {
            if (status.equals(ACCEPTED)) return true;
            return false;
        }

        public void setListeners(OrderIds current, int position) {
            tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communicator.onStatusOrderClickListener(current);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     * @param mDataList
     */
    public StatusOrderAdapter(ArrayList<OrderIds> mDataList, Context context) {
        this.mDataList = mDataList;
        this.mContext = context;
        communicator = (IStatusOrderForAdapter) mContext;
    }

    /**
     * Returns complete data list
     * @return
     */
    public ArrayList<OrderIds> getDataList() {
        return mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_deliver_order, parent, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderIds current = mDataList.get(position);
        holder.setData(current, position);
        holder.setListeners(current, position);
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * Remove an item and notify
     * @param position
     */
    public void removeItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    /**
     * Remove all items from RecyclerView
     */
    public void removeAllItems() {
        while (mDataList.size() > 0) {
            removeItem(0);
        }
    }

    /**
     * Add an item into RecyclerView
     * @param position
     * @param data
     */
    public void addItem(int position, OrderIds data) {
        mDataList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    /**
     * Add a set of items into RecyclerView
     * @param arrayList
     */
    public void addSetOfItems(ArrayList<OrderIds> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            addItem(i, arrayList.get(i));
        }
    }

}
