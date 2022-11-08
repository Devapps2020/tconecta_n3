package com.blm.qiubopay.modules.orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.odrer.OrderSimpleAdapter;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.order.OrderModelResponse;
import com.blm.qiubopay.models.order.ProductsModel;
import com.blm.qiubopay.modules.orders.communicator.IShippngConfirmation;

import java.util.ArrayList;

public class ShippingConfirmationFragment extends HFragment {

    private static Context myContext;
    private static OrderModelResponse mOrderStatusModel;
    private static boolean mOrderAccepted = false;
    private RelativeLayout rlShippingHeader;
    private TextView tvOrder;
    private RecyclerView rvProducts;
    private TextView tvClientName;
    private TextView tvDeliver;
    private TextView tvAddress;
    private TextView tvMapLocation;
    private LinearLayout llOrderReport;
    private LinearLayout llSuccess;
    private LinearLayout llFail;
    private TextView tvSuccessTime;
    private Button btnAccept;
    private IShippngConfirmation communicator;
    private LastLocation storeLocation;

    public static ShippingConfirmationFragment newInstance(Context context, OrderModelResponse orderStatusModel, boolean orderAccepted) {
        myContext = context;
        mOrderStatusModel = orderStatusModel;
        mOrderAccepted = orderAccepted;
        return new ShippingConfirmationFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IShippngConfirmation) myContext;
        init();
        setContent();
        setListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_shipping_confirmation, container, false), R.drawable.background_splash_header_1 );
    }

    private void init() {
        rlShippingHeader = getContext().findViewById(R.id.rlShippingHeader);
        tvOrder = getContext().findViewById(R.id.tvOrder);
        rvProducts = getContext().findViewById(R.id.rvProducts);
        tvClientName = getContext().findViewById(R.id.tvClientName);
        tvDeliver = getContext().findViewById(R.id.tvDeliver);
        tvAddress = getContext().findViewById(R.id.tvAddress);
        tvMapLocation = getContext().findViewById(R.id.tvMapLocation);
        llOrderReport = getContext().findViewById(R.id.llOrderReport);
        llSuccess = getContext().findViewById(R.id.llSuccess);
        llFail = getContext().findViewById(R.id.llFail);
        tvSuccessTime = getContext().findViewById(R.id.tvSuccessTime);
        btnAccept = getContext().findViewById(R.id.btnAccept);

        rvProducts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvProducts.setLayoutManager(linearLayoutManager);
        if (mOrderStatusModel != null && !mOrderStatusModel.getProducts().isEmpty()) {
            setAdapter(mOrderStatusModel.getProducts());
        }
    }

    private void setAdapter(ArrayList<ProductsModel> list) {
        OrderSimpleAdapter adapter = new OrderSimpleAdapter(list);
        rvProducts.setAdapter(adapter);
    }

    private void setContent() {
        if (mOrderAccepted) {
            tvClientName.setText(mOrderStatusModel.getClientName() + "");
            tvDeliver.setText(mOrderStatusModel.getEstimatedTime() + "");
            tvAddress.setText(mOrderStatusModel.getAddress() + "");
            llFail.setVisibility(View.GONE);
            llSuccess.setVisibility(View.GONE);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuActivity.stopTimerForOrders();
                    if (llSuccess.getVisibility() == View.GONE) {
                        llOrderReport.setVisibility(View.GONE);
                        rlShippingHeader.setVisibility(View.GONE);
                        llSuccess.setVisibility(View.VISIBLE);
                        tvSuccessTime.setText(mOrderStatusModel.getEstimatedTime() + "");
                    } else {
                        communicator.onAcceptClickListener();
                    }
                }
            });
        } else {
            llOrderReport.setVisibility(View.GONE);
            rlShippingHeader.setVisibility(View.GONE);
            llSuccess.setVisibility(View.GONE);
            llFail.setVisibility(View.VISIBLE);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communicator.onAcceptClickListener();
                }
            });
        }
    }

    private void setListeners() {
        tvMapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLocation = AppPreferences.getStoreLocation();
                showDestination(mOrderStatusModel.getLatitude(), mOrderStatusModel.getLongitude(), storeLocation.getLatitude() + "", storeLocation.getLongitude() + "");
            }
        });
    }

    private void showDestination(String lat, String lon, String dLat, String dLon) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + lat + "," + lon + "&daddr=" + dLat + "," + dLon));
        startActivity(intent);
//        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + lat + "," + lon);
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
