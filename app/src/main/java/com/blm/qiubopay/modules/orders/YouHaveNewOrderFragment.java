package com.blm.qiubopay.modules.orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.modules.orders.communicator.IYouHaveNewOrder;

import mx.devapps.utils.components.HFragment;

// TODO: Remove this fragment from flow
public class YouHaveNewOrderFragment extends HFragment {

    private ImageView ivClose;
    private Button btnOrderDetails;
    private Button btnRejectOrder;
    private static Context myContext;
    private IYouHaveNewOrder communicator;

    public static YouHaveNewOrderFragment newInstance(Context context) {
        myContext = context;
        return new YouHaveNewOrderFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IYouHaveNewOrder) getActivity();
        init();
        setListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_you_have_new_order, container, false), R.drawable.background_splash_header_1 );
    }

    private void init() {
        ivClose = getContext().findViewById(R.id.ivClose);
        btnOrderDetails = getContext().findViewById(R.id.btnOrderDetails);
        btnRejectOrder = getContext().findViewById(R.id.btnRejectOrder);
    }

    private void setListeners() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onYouHaveNewOrderCloseClickListener();
            }
        });
        btnOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onYouHaveNewOrderDetailsClickListener();
            }
        });
        btnRejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onYouHaveNewOrderRejectClickListener();
            }
        });
    }

    @Override
    public void initFragment() {

    }

}