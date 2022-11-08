package com.blm.qiubopay.modules;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;

import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_soporte_bimbo_3 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;

    public static Fragment_soporte_bimbo_3 newInstance(Object... data) {
        Fragment_soporte_bimbo_3 fragment = new Fragment_soporte_bimbo_3();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_1", new Gson().toJson(data[0]));

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

        view = inflater.inflate(R.layout.fragment_soporte_bimbo_3, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                context.initHome();
            }
        });

        TextView text_soporte_telefono = view.findViewById(R.id.text_soporte_telefono);
        text_soporte_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + "5546319740"));
                        startActivity(intent);
                    }
                }, new String[]{Manifest.permission.CALL_PHONE});

            }
        });
    }


}

