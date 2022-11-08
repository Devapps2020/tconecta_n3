package com.blm.qiubopay.modules.orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.orders.communicator.IActivateMyOrders;

import mx.devapps.utils.components.HFragment;

public class ActivateMyOrdersFragment extends HFragment implements IMenuContext {

    private static Context myContext;
    private TextView tvContent;
    private Button btnActivateOrders;
    private IActivateMyOrders communicator;

    public static ActivateMyOrdersFragment newInstance(Context context) {
        myContext = context;
        return new ActivateMyOrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IActivateMyOrders) getActivity();
        init();
        setContent();
        setListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_activate_my_orders, container, false), R.drawable.background_splash_header_1 );
    }

    @Override
    public MenuActivity getContextMenu() {
        return null;
    }

    @Override
    public void initFragment() { }

    private void init() {
        btnActivateOrders = getContext().findViewById(R.id.btnActivateOrders);
        tvContent = getContext().findViewById(R.id.tvContent);
    }

    private void setContent() {

    }

    private void setListeners() {
        btnActivateOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onActivateClickListener();
            }
        });
    }

}