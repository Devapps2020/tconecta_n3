package com.blm.qiubopay.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.views.HSignatureView;

public class Fragment_registro_financiero_5 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;

    public static Fragment_registro_financiero_5 newInstance(Object... data) {
        Fragment_registro_financiero_5 fragment = new Fragment_registro_financiero_5();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_5", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_registro_financiero_5"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_registro_financiero_5, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        final HSignatureView signature = view.findViewById(R.id.signature);

        Button btn_continuar = view.findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setImage.execute(signature.getBitmap());
            }
        });

        ImageView img_exit = view.findViewById(R.id.img_delete);
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.ClearCanvas();
            }
        });

    }

    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

}

