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
import com.blm.qiubopay.adapters.CreditoPesitoAdapter;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.utils.SessionApp;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_credito_bimbo_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;
    public static SellerUserDTO seller;

    public static Fragment_credito_bimbo_1 newInstance(Object... data) {
        Fragment_credito_bimbo_1 fragment = new Fragment_credito_bimbo_1();
        Bundle args = new Bundle();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_credito_bimbo_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_credito_bimbo_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_CREDITO_PESITO_inician);

        RecyclerView recyclerView = view.findViewById(R.id.list_vendedores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        CreditoPesitoAdapter adapter = new CreditoPesitoAdapter(Arrays.asList(SessionApp.getInstance().getSellerUserResponse().getQpay_object()),context);
        recyclerView.setAdapter(adapter);

    }
}
