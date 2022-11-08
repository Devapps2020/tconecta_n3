package com.blm.qiubopay.modules.chambitas.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.modules.chambitas.cupones.FragmentCouponDetails;
import com.blm.qiubopay.utils.DateUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    private String TAG = "CouponsAdapter";
    private Context context;
    private ArrayList<CouponDetailsListResponse> mDataList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout llItemCoupon;
        public ImageView ivItemCoupon;
        public TextView tvItemCouponTitle;
        public TextView tvItemCouponValidity;
        public TextView tvItemCouponDescription;
        int position;

        public ViewHolder(TextView v) {
            super(v);
            tvItemCouponTitle = v;
        }

        public ViewHolder(LinearLayout v) {
            super(v);
            llItemCoupon = v.findViewById(R.id.llItemCoupon);
            ivItemCoupon = v.findViewById(R.id.ivItemCoupon);
            tvItemCouponTitle = v.findViewById(R.id.tvItemCouponTitle);
            tvItemCouponValidity = v.findViewById(R.id.tvItemCouponValidity);
            tvItemCouponDescription = v.findViewById(R.id.tvItemCouponDescription);
        }

        public void setData(CouponDetailsListResponse currentModel, int position) {
            String title = currentModel.getChambitaName();
            String validity = currentModel.getValidity();
            String[] justDate = validity.split(" ");
            String description = currentModel.getDescription();
            String redeemed = currentModel.getPrintedAt();
            String itemImage = currentModel.getChambitaURL();
            Glide.with(context)
                    .load(itemImage)
                    .error(R.drawable.illustrations_chambita_248_x_200)
                    .centerCrop()
                    .into(ivItemCoupon);
            this.tvItemCouponTitle.setText(title);
            this.tvItemCouponValidity.setText(context.getResources().getString(R.string.coupons_redeem_before) + " " + justDate[0]);
            this.tvItemCouponDescription.setText(description);
            this.position = position;
            this.llItemCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MenuActivity) context).setFragment(FragmentCouponDetails.newInstance(mDataList.get(position)));
                }
            });
            if (!redeemed.equals("")) setBackground();
            else {
                String[] separateDate = validity.split("-");
                reviewValidity(separateDate);
            }
        }

        private void setBackground() {
            llItemCoupon.setBackground(context.getDrawable(R.drawable.item_coupon_gray_shape));
        }

        public void setListeners() {

        }

        private void reviewValidity(String[] date) {
            if (date != null && date.length == 3) {
                try {
                    DateUtils dateUtils = new DateUtils(context);
                    if (dateUtils.itIsAboutToExpire(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]))) {
                        this.tvItemCouponValidity.setTextColor(context.getResources().getColor(R.color.colorRed));
                        this.tvItemCouponDescription.setTextColor(context.getResources().getColor(R.color.colorRed));
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     *
     * @param mDataList
     */
    public CouponsAdapter(ArrayList<CouponDetailsListResponse> mDataList, Context context) {
        this.mDataList = mDataList;
        this.context = context;
    }

    /**
     * Returns complete data list
     *
     * @return
     */
    public ArrayList<CouponDetailsListResponse> getDataList() {
        return mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupons, parent, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CouponDetailsListResponse current = mDataList.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
