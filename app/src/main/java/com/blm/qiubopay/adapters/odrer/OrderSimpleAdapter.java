package com.blm.qiubopay.adapters.odrer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.order.ProductsModel;

import java.util.ArrayList;

public class OrderSimpleAdapter extends RecyclerView.Adapter<OrderSimpleAdapter.ViewHolder> {

    private Context context;
    private AppCompatActivity activity = (AppCompatActivity) context;
    private ArrayList<ProductsModel> mDataList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvQuantity;
        public TextView tvProductName;
        int position;

        public ViewHolder(TextView v) {
            super(v);
            tvQuantity = v;
        }

        public ViewHolder(LinearLayout v) {
            super(v);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvProductName = v.findViewById(R.id.tvProductName);
        }

        public void setData(ProductsModel currentModel, int position) {
            String productName = currentModel.getName();
            int productQuantity = currentModel.getQty();

            this.tvProductName.setText(productName);
            this.tvQuantity.setText("x" + productQuantity);
            this.position = position;
        }

        public void setListeners() {

        }

        @Override
        public void onClick(View v) {

        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     * @param mDataList
     */
    public OrderSimpleAdapter(ArrayList<ProductsModel> mDataList) {
        this.mDataList = mDataList;
    }

    /**
     * Returns complete data list
     * @return
     */
    public ArrayList<ProductsModel> getDataList() {
        return mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_list, parent, false);
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
        ProductsModel current = mDataList.get(position);
        holder.setData(current, position);
        holder.setListeners();
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
    public void addItem(int position, ProductsModel data) {
        mDataList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    /**
     * Add a set of items into RecyclerView
     * @param arrayList
     */
    public void addSetOfItems(ArrayList<ProductsModel> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            addItem(i, arrayList.get(i));
        }
    }

}
