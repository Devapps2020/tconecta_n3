package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.VendedoresAdapter;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.utils.SessionApp;

import java.util.Arrays;

public class Fragment_proveedor_bimbo_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;

    public static Fragment_proveedor_bimbo_1 newInstance(Object... data) {
        Fragment_proveedor_bimbo_1 fragment = new Fragment_proveedor_bimbo_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_bimbo_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_soporte_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_proveedor_bimbo_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CApplication.setAnalytics(CApplication.ACTION.CB_EVALUANOS_inician);

        if(SessionApp.getInstance().getSellerUserResponse() == null || SessionApp.getInstance().getSellerUserResponse().getQpay_object().length == 0)
            return;

        RecyclerView recyclerView = view.findViewById(R.id.list_vendedores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        VendedoresAdapter adapter = new VendedoresAdapter(Arrays.asList(SessionApp.getInstance().getSellerUserResponse().getQpay_object()),context);
        recyclerView.setAdapter(adapter);
    }

}

