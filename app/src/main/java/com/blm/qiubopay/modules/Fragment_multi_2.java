package com.blm.qiubopay.modules;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.QPAY_LinkCode;
import com.blm.qiubopay.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_multi_2 extends HFragment {

    private View view;
    private MenuActivity context;

    QPAY_LinkCode linkCode;

    public Fragment_multi_2() {
        // Required empty public constructor
    }

    public static Fragment_multi_2 newInstance(Object... data) {
        Fragment_multi_2 fragment = new Fragment_multi_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_multi_2", new Gson().toJson(data[0]));

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
            linkCode = new Gson().fromJson(getArguments().getString("Fragment_multi_2"), QPAY_LinkCode.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_tc_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        TextView txtCode = view.findViewById(R.id.txt_codigo);
        ImageView img_qr_code = view.findViewById(R.id.img_multi_qr);


        txtCode.setText(linkCode.getQpay_link_code());

        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CApplication.setAnalytics(CApplication.ACTION.CB_AGREGAR_CAJERO_comparten);

                Utils.share(getContext(),"Conecta- Código para vinculación",linkCode.getQpay_link_code(),"Conecta- Código");

            }
        });


        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(linkCode.getQpay_link_code(), BarcodeFormat.QR_CODE, 800, 800);
            img_qr_code.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

    }


}
