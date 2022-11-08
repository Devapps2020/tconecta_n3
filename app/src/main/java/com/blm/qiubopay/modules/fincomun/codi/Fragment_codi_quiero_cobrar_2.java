package com.blm.qiubopay.modules.fincomun.codi;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import java.io.ByteArrayOutputStream;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_codi_quiero_cobrar_2 extends HFragment implements IMenuContext {


    public static String qr = "";

    public static Fragment_codi_quiero_cobrar_2 newInstance(Object... data) {
       Fragment_codi_quiero_cobrar_2 fragment = new Fragment_codi_quiero_cobrar_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_codi_quiero_cobrar_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_quiero_cobrar_2, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        Button btn_shared = getView().findViewById(R.id.btn_shared);
        ImageView img_qr_code = getView().findViewById(R.id.image_qr_codi);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap((String) qr, BarcodeFormat.QR_CODE, 800, 800);
            img_qr_code.setImageBitmap(bitmap);
            qr = null;
        } catch(Exception e) {

        }

        btn_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        shareIt(getImageUri(getContextMenu(),getBitmapFromView(getView().findViewById(R.id.layout_shared))));
                    }
                }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});

            }
        });


    }

    public static Bitmap getBitmapFromView(View view) {
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
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

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

