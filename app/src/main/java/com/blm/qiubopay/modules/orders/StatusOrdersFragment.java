package com.blm.qiubopay.modules.orders;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.odrer.StatusOrderAdapter;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.order.OrderIds;
import com.blm.qiubopay.models.order.OrderResponse;
import com.blm.qiubopay.modules.orders.communicator.IStatusOrder;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class StatusOrdersFragment extends HFragment implements IMenuContext {

    private static ArrayList<OrderResponse> mOrderPendingModelList;
    private static ArrayList<OrderResponse> mOrderDeliveredModelList;
    private static Boolean mIsPendingList = true;
    private static Context myContext;
    private static String mOrdersFlag;
    private Switch swOrdersActivation;
    private EditText etSearch;
    private RelativeLayout rlSearch;
    private LinearLayout llPending;
    private LinearLayout llDelivered;
    private View vPending;
    private View vDelivered;
    private RecyclerView rvOrders;
    private IStatusOrder communicator;
    private Dialog dialog;
    private Boolean isServiceBussy = false;

    public static StatusOrdersFragment newInstance(ArrayList<OrderResponse> orderPendingModelList, ArrayList<OrderResponse> orderDeliveredModelList, Boolean isPendingList, String ordersFlag, Context context) {
        mOrderPendingModelList = orderPendingModelList;
        mOrderDeliveredModelList = orderDeliveredModelList;
        mIsPendingList = isPendingList;
        mOrdersFlag = ordersFlag;
        myContext = context;
        return new StatusOrdersFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IStatusOrder) getActivity();
        init();
        getData();
        setContent();
        setListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_status_orders, container, false), R.drawable.background_splash_header_1 );
    }

    void init() {
        swOrdersActivation = getContext().findViewById(R.id.swOrdersActivation);
        etSearch = getContext().findViewById(R.id.etSearch);
        rlSearch = getContext().findViewById(R.id.rlSearch);
        llPending = getContext().findViewById(R.id.llPending);
        llDelivered = getContext().findViewById(R.id.llDelivered);
        vPending = getContext().findViewById(R.id.vPending);
        vDelivered = getContext().findViewById(R.id.vDelivered);
        rvOrders = getContext().findViewById(R.id.rvOrders);

        rvOrders.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvOrders.setLayoutManager(linearLayoutManager);

        if (mIsPendingList) setPendingList();
        else setDeliveredList();
    }

    void getData() {

    }

    void setContent() {
        if (mOrdersFlag != null && !mOrdersFlag.isEmpty()) {
            if (mOrdersFlag.equals("1")) swOrdersActivation.setChecked(true);
            else swOrdersActivation.setChecked(false);
        }
    }

    void setListeners() {
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onSearchClickListener(etSearch.getText().toString());
            }
        });
        llPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsPendingList = true;
                setPendingList();
            }
        });
        llDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsPendingList = false;
                setDeliveredList();
            }
        });
        swOrdersActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!swOrdersActivation.isChecked()) showDialog();
                else communicator.onStatusOrderClickListener("1");
            }
        });
        rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isServiceBussy) {
                    isServiceBussy = true;
                    communicator.onScrollEnd(mIsPendingList);
                }
            }
        });
    }

    public void setServiceFree() {
        isServiceBussy = false;
    }

    public void reloadList() {
        if (mIsPendingList) {
            setPendingList();
        } else {
            setDeliveredList();
        }
    }

    private void setPendingList() {
        llPending.setBackgroundColor(getResources().getColor(R.color.FC_blue_1));
        vPending.setBackgroundColor(getResources().getColor(R.color.clear_blue));
        llDelivered.setBackgroundColor(getResources().getColor(R.color.white));
        vDelivered.setBackgroundColor(getResources().getColor(R.color.FC_blue_1));
        if (mOrderPendingModelList != null && !mOrderPendingModelList.isEmpty()) {
            setAdapter(mOrderPendingModelList.get(0).getOrderIds());
        } else setAdapter(new ArrayList<>());
    }

    private void setDeliveredList() {
        llDelivered.setBackgroundColor(getResources().getColor(R.color.FC_blue_1));
        vDelivered.setBackgroundColor(getResources().getColor(R.color.clear_blue));
        llPending.setBackgroundColor(getResources().getColor(R.color.white));
        vPending.setBackgroundColor(getResources().getColor(R.color.FC_blue_1));
        if (mOrderDeliveredModelList != null && !mOrderDeliveredModelList.isEmpty()) {
            setAdapter(mOrderDeliveredModelList.get(0).getOrderIds());
        } else setAdapter(new ArrayList<>());
    }

    private void setAdapter(ArrayList<OrderIds> list) {
        StatusOrderAdapter adapter = new StatusOrderAdapter(list, myContext);
        rvOrders.setAdapter(adapter);
        communicator.onListSelected(mIsPendingList);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void initFragment() {

    }

    private void showDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_notifications_warning);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onStatusOrderClickListener("0");
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swOrdersActivation.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
