package com.blm.qiubopay.adapters.odrer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.order.OrderModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.devapps.utils.components.HActivity;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private String TAG = "OrderAdapter";
    private Context context;
    private AppCompatActivity activity = (AppCompatActivity) context;
    private ArrayList<OrderModel> mDataList;
    private TextView tvError;
    private TextView tvTotalAmount;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvProductName;
        public TextView tvQuantity;
        public EditText etQuantity;
        public EditText etProductPrice;
        public CheckBox cbSimilarProduct;
        public LinearLayout llSimilarProduct;
        public EditText etSimilarProductName;
        int position;
        String current = "";

        public ViewHolder(TextView v) {
            super(v);
            tvProductName = v;
        }

        public ViewHolder(LinearLayout v) {
            super(v);
            tvProductName = v.findViewById(R.id.tvProductName);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            etQuantity = v.findViewById(R.id.etQuantity);
            etProductPrice = v.findViewById(R.id.etProductPrice);
            cbSimilarProduct = v.findViewById(R.id.cbSimilarProduct);
            llSimilarProduct = v.findViewById(R.id.llSimilarProduct);
            etSimilarProductName = v.findViewById(R.id.etSimilarProductName);

            cbSimilarProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbSimilarProduct.isChecked()) {
                        mDataList.get(position).setBoxChecked(true);
                        llSimilarProduct.setVisibility(View.VISIBLE);
                    } else {
                        mDataList.get(position).setBoxChecked(false);
                        llSimilarProduct.setVisibility(View.GONE);
                    }
                }
            });

            etSimilarProductName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvError.setVisibility(View.GONE);
                    if (s.toString().isEmpty()) {
                        mDataList.get(position).setProductQuantity(0);
                    } else {
                        etSimilarProductName.removeTextChangedListener(this);
                        mDataList.get(position).setSimilarProductName(etSimilarProductName.getText().toString());
                        etSimilarProductName.addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etProductPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvError.setVisibility(View.GONE);
                    if (!s.toString().equals("null") && !s.toString().isEmpty()) {
                        String cleanString = s.toString().replaceAll("[$€,. ]", "");
                        mDataList.get(position).setProductPrice(cleanString);
                    } else {
                        mDataList.get(position).setProductPrice("");
                    }
                    tvTotalAmount.setText(getTotalAmount());
                    if(!s.toString().equals(current)) {
                        etProductPrice.removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[$€,. ]", "");
                        if (!cleanString.isEmpty()) {
                            double parsed = Double.parseDouble(cleanString);
                            String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed/100));
                            String noSymbol = formatted.replaceAll("[$€ ]", "");
                            current = noSymbol;
                            etProductPrice.setText(noSymbol);
                            etProductPrice.setSelection(noSymbol.length());
                        }
                        etProductPrice.addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            Runnable runOnUI = new Runnable() {
                @Override
                public void run() {
                    etQuantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            tvError.setVisibility(View.GONE);
                            etQuantity.removeTextChangedListener(this);
                            if (!s.toString().equals("null") && !s.toString().isEmpty()) {
                                int quantity = Integer.parseInt(s.toString());
                                mDataList.get(position).setProductQuantity(quantity);
                            } else {
                                mDataList.get(position).setProductQuantity(0);
                            }
                            tvTotalAmount.setText(getTotalAmount());
                            etQuantity.addTextChangedListener(this);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            };

            Runnable runOnThread = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        ((HActivity) context).runOnUiThread(runOnUI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runOnThread);
            thread.start();

        }

        private String getTotalAmount() {
            String res = "";
            long totalAmount = 0;
            for (OrderModel item : mDataList) {
                if (item.getProductPrice() != null && !item.getProductPrice().isEmpty()) {
                    String strItemPrice = item.getProductPrice();
                    String strNewItemPrice = strItemPrice.replaceAll("", "");
                    Log.d(TAG, "strItemPrice: " + strItemPrice);
                    Log.d(TAG, "strNewItemPrice: " + strNewItemPrice);
                    int itemPrice = Integer.parseInt(strNewItemPrice);
                    int itemAmount = item.getProductQuantity() * itemPrice;
                    totalAmount += itemAmount;
                }
                double parsed = Double.parseDouble(totalAmount + "");
                String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed/100));
                res = formatted.replaceAll("[$€ ]", "");
            }
            return res;
        }

        public void setData(OrderModel currentModel, int position) {
            String productName = currentModel.getProductName();
            int productQuantity = currentModel.getProductQuantity();
            String productPrice = currentModel.getProductPrice();

            this.tvProductName.setText(productName);
            if (productPrice != null && !productPrice.isEmpty()) {
                this.etProductPrice.setText(productPrice + "");
            }
            if (productQuantity <= 0) {
                this.etQuantity.setText("");
            } else {
                this.tvQuantity.setText(productQuantity + "");
                this.etQuantity.setText(productQuantity + "");
            }
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
    public OrderAdapter(ArrayList<OrderModel> mDataList, TextView tvError, TextView tvTotalAmount) {
        this.mDataList = mDataList;
        this.tvError = tvError;
        this.tvTotalAmount = tvTotalAmount;
    }

    /**
     * Returns complete data list
     * @return
     */
    public ArrayList<OrderModel> getDataList() {
        return mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
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
        OrderModel current = mDataList.get(position);
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
    public void addItem(int position, OrderModel data) {
        mDataList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    /**
     * Add a set of items into RecyclerView
     * @param arrayList
     */
    public void addSetOfItems(ArrayList<OrderModel> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            addItem(i, arrayList.get(i));
        }
    }

}