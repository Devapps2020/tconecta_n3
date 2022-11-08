package com.blm.qiubopay.modules.fincomun.credito;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCCreditoOXXOResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

import static com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_quiero_cobrar_2.getBitmapFromView;

public class Fragment_fincomun_ref_oxxo extends HFragment implements IMenuContext {

    private TextView tv_monto_oxxo,tv_minimo,tv_maximo,tv_code_oxxo;
    private Slider sld_monto;
    private LinearLayout ll_codigo,ll_selector_monto;
    private Button btn_next;

    public static String numCredito;
    public static Double maximo = 0.0;
    public static Double minimo = 0.0;
    private Double montoSeleccionado = 0.0;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public static Fragment_fincomun_ref_oxxo newInstance() {
        Fragment_fincomun_ref_oxxo fragment = new Fragment_fincomun_ref_oxxo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_ref_oxxo, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        tv_monto_oxxo = getView().findViewById(R.id.tv_monto_oxxo);
        tv_minimo = getView().findViewById(R.id.tv_minimo);
        tv_maximo = getView().findViewById(R.id.tv_maximo);
        tv_code_oxxo = getView().findViewById(R.id.tv_code_oxxo);
        sld_monto = getView().findViewById(R.id.sld_monto);
        ll_selector_monto = getView().findViewById(R.id.ll_selector_monto);
        ll_codigo = getView().findViewById(R.id.ll_codigo);
        btn_next = getView().findViewById(R.id.btn_next);


        configView();
    }

    private void configView() {
        tv_monto_oxxo.setText("$"+decimalFormat.format(maximo));
        tv_minimo.append("\n$"+decimalFormat.format(minimo));
        tv_maximo.append("\n$"+decimalFormat.format(maximo));

        sld_monto.setValueTo((float) maximo.intValue());
        sld_monto.setValueFrom((float) minimo.intValue());
        sld_monto.setValue((float) maximo.intValue());
        sld_monto.setStepSize(1f);

        montoSeleccionado = new Double(maximo);

        sld_monto.addOnChangeListener((slider, value, fromUser) -> {
            montoSeleccionado = new Double(value);
            tv_monto_oxxo.setText("$"+decimalFormat.format(montoSeleccionado));
        });

        btn_next.setTag("1");
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (montoSeleccionado >=minimo && montoSeleccionado <= maximo) {
                    switch (String.valueOf(btn_next.getTag())) {
                        case "1":
                            getRefOXXO();
                            break;
                        case "2":
                            compartir();
                            break;
                    }
                }else {
                    getContextMenu().alert("Selecciona un monto válido para generar la referencia");
                }
            }
        });
    }

    private void getRefOXXO() {
        getContextMenu().getTokenFC((String... data) -> {
            if (data!= null) {
                getContextMenu().getRefOxxo(numCredito,false,montoSeleccionado, data[0], new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        if (data != null && data[0] != null) {
                            btn_next.setTag("2");
                            btn_next.setText("Compartir");
                            FCCreditoOXXOResponse response = new Gson().fromJson(String.valueOf(data[0]), FCCreditoOXXOResponse.class);
                            String ref = new String(Base64.decode(response.getReferenciaOxxo(), Base64.DEFAULT));
                            SessionApp.getInstance().getDhListaCreditos().setRefOxxo(ref);
                            // montoOXXO = response.getMontoPago();
                            // tv_next_fee.setText(Utils.paserCurrency(String.valueOf(montoOXXO)));

                            tv_code_oxxo.setText(ref);
                            ll_codigo.setVisibility(View.VISIBLE);
                            ll_selector_monto.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void compartir() {
        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {
                shareIt(getImageUri(getContextMenu(),getBitmapFromView(getView().findViewById(R.id.ll_content))));
            }
        }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void shareIt(Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Pago de tus cuotas Fincomún";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}