package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.QrCodi;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_fincomun_cobro_detalle extends HFragment implements IMenuContext {


    public Fragment_fincomun_cobro_detalle() {
    }

    public static Fragment_fincomun_cobro_detalle newInstance(String data, String qr){
        Fragment_fincomun_cobro_detalle fragment = new Fragment_fincomun_cobro_detalle();
        Bundle args = new Bundle();
        args.putString("param",data);
        args.putString("qr",qr);
        fragment.setArguments(args);
        return fragment;
    }

    private QrCodi qr;
    private String qrEncoded;
    private TextView tv_monto, tv_referencia, tv_concepto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_cobro_qr, container, false),R.drawable.background_splash_header_2);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            // TODO : Deserializar información
            qr = new Gson().fromJson(getArguments().getString("param"), QrCodi.class);
            qrEncoded = getArguments().getString("qr");
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_white)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });

        Button btn_shared = getView().findViewById(R.id.btn_share);
        ImageView img_qr_code = getView().findViewById(R.id.imv_cobro_qr);



        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);


        tv_monto = getView().findViewById(R.id.tv_monto);
        tv_monto.setText(fmt.format(Double.parseDouble(qr.getMonto())));

        tv_referencia = getView().findViewById(R.id.tv_referencia);
        tv_referencia.setText(qr.getReferencia());

        tv_concepto = getView().findViewById(R.id.tv_concepto);
        tv_concepto.setText(qr.getConcepto());

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrEncoded, BarcodeFormat.QR_CODE, 800, 800);
            img_qr_code.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        btn_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        shareIt(getImageUri(getContextMenu(),getBitmapFromView(getView().findViewById(R.id.imv_cobro_qr))));
                    }
                }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});

            }
        });


    }



    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void shareIt(Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "CoDi T-Conecta";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}
