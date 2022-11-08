package com.blm.qiubopay.modules.orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.adapters.odrer.OrderSimpleAdapter;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.order.OrderModelResponse;
import com.blm.qiubopay.modules.orders.communicator.IOrderDetail;

import java.text.DecimalFormat;

public class OrderDetailFragment extends HFragment {

    private TextView tvPhoneName;
    private TextView tvOrderDate;
    private TextView tvAddress;
    private TextView tvMapLocation;
    private RelativeLayout rlBackFromDetail;
    private RecyclerView rvProducts;
    private TextView tvTotalSale;
    private static OrderModelResponse mOrderStatusModel;
    private static Context myContext;
    private IOrderDetail communicator;
    private LastLocation storeLocation;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static OrderDetailFragment newInstance(OrderModelResponse orderStatusModel, Context context) {
        mOrderStatusModel = orderStatusModel;
        myContext = context;
        return new OrderDetailFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IOrderDetail) myContext;
        init();
        getData();
        setContent();
        setListeners();
        setupRecyclerView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_order_detail, container, false), R.drawable.background_splash_header_1 );
    }

    private void init() {
        tvPhoneName = getContext().findViewById(R.id.tvPhoneName);
        tvOrderDate = getContext().findViewById(R.id.tvOrderDate);
        tvAddress = getContext().findViewById(R.id.tvAddress);
        tvMapLocation = getContext().findViewById(R.id.tvMapLocation);
        rlBackFromDetail = getContext().findViewById(R.id.rlBackFromDetail);
        rvProducts = getContext().findViewById(R.id.rvProducts);
        tvTotalSale = getContext().findViewById(R.id.tvTotalSale);
    }

    private void getData() {

    }

    private void setContent() {
        if (mOrderStatusModel != null) {
            tvPhoneName.setText("+" + mOrderStatusModel.getClientNumber() + " / " + mOrderStatusModel.getClientName());
            tvOrderDate.setText(mOrderStatusModel.getCreationDate());
            tvAddress.setText(mOrderStatusModel.getAddress());

            String totalSale = df.format(mOrderStatusModel.getTotalAmount());
            tvTotalSale.setText("$" + totalSale);
            setupRecyclerView();
        }
    }

    private void setListeners() {
        rlBackFromDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onOrderDetailBackClickListener();
            }
        });
        tvMapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLocation = AppPreferences.getStoreLocation();
                showDestination(mOrderStatusModel.getLatitude(), mOrderStatusModel.getLongitude(), storeLocation.getLatitude() + "", storeLocation.getLongitude() + "");
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        communicator.onOrderDetailBackClickListener();
        return true;
    }

    private void setupRecyclerView() {
        rvProducts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvProducts.setLayoutManager(linearLayoutManager);
        OrderSimpleAdapter adapter = new OrderSimpleAdapter(mOrderStatusModel.getProducts());
        rvProducts.setAdapter(adapter);
    }

    private void showDestination(String lat, String lon, String dLat, String dLon) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + lat + "," + lon + "&daddr=" + dLat + "," + dLon));
        startActivity(intent);
    }

}
