package com.blm.qiubopay.modules.chambitas.cupones;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.modules.chambitas.adapter.CouponsAdapter;
import com.blm.qiubopay.modules.chambitas.cupones.viewmodel.CouponsVM;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;

public class FragmentCoupons extends HFragment implements IMenuContext {

    private CouponsVM viewModel;
    private View view;
    private HActivity context;
    private CouponsAdapter adapter;
    private RecyclerView rvChambitas;

    private ArrayList<CouponDetailsListResponse> couponDetailsListResponses;

    public static FragmentCoupons newInstance() {
        return new FragmentCoupons();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new CouponsVM(getContext());
        init();
        getData();
        setListeners();
        setObservers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        if( view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_coupons, container, false);
        setView(view);
        initFragment();
        return view;
    }

    private void init() {
        rvChambitas = view.findViewById(R.id.rvChambitas);
    }

    private void getData() {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        if (bimboId == null || bimboId.isEmpty()) {
            Runnable runOnUI = new Runnable() {
                @Override
                public void run() {
                    ((MenuActivity) context).setFragment(FragmentCouponDetails.newInstance(null));
                }
            };
            Runnable runOnThread = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    context.runOnUiThread(runOnUI);
                }
            };
            Thread thread = new Thread(runOnThread);
            thread.start();
        } else {
            context.loading(true);
            viewModel.getCouponAllDetailsByUser();
        }
    }

    private void setListeners() {
        getContext().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (((MenuActivity) context).wasCouponDetailsOpen) {
                    ((MenuActivity) context).wasCouponDetailsOpen = false;
                    getData();
                }
            }
        });
    }

    private void setObservers() {
        viewModel._couponsAllDetailsResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<CouponDetailsListResponse>>() {
            @Override
            public void onChanged(BaseResponse<CouponDetailsListResponse> couponDetailsListResponse) {
                context.closeLoading();
                if (couponDetailsListResponse != null) {
                    if (couponDetailsListResponse.getQpayObject() != null && !couponDetailsListResponse.getQpayObject().isEmpty()) {
                        couponDetailsListResponses = couponDetailsListResponse.getQpayObject();
                        setupRecyclerView();
                    }
                }
            }
        });
    }

    public void initFragment(){
        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });
    }

    private void setupRecyclerView() {
        rvChambitas.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChambitas.setLayoutManager(linearLayoutManager);
        adapter = new CouponsAdapter(couponDetailsListResponses, this.context);
        rvChambitas.setItemViewCacheSize(50);
        rvChambitas.setAdapter(adapter);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}
