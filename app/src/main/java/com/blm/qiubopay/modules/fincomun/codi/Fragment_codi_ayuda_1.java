package com.blm.qiubopay.modules.fincomun.codi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import mx.devapps.utils.components.HFragment;

public class Fragment_codi_ayuda_1 extends HFragment implements IMenuContext {


    public static Fragment_codi_ayuda_1 newInstance(Object... data) {
         Fragment_codi_ayuda_1 fragment = new Fragment_codi_ayuda_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_ayuda_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return  (MenuActivity) getContext();
    }
}

