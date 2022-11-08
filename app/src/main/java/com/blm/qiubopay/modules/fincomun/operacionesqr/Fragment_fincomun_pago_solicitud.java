package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_pago_solicitud extends HFragment implements IMenuContext {


    private LinearLayout ll_camara, ll_galeria;

    private ImageView imv_camara , imv_galeria;

    private final Integer GALLERY_REQUEST = 11;

    private View.OnClickListener onClickListenerCamara = v -> {
        //TODO : Lanzar cámara para escanear QR
        capturePictureFromCamera();
    };

    private View.OnClickListener onClickListenerGaleria = v -> {
        //TODO : Lanzar galeria para seleccionar QR
        setResultCamera();
        getImageFromGallery();
    };



    public Fragment_fincomun_pago_solicitud() {
    }

    public static Fragment_fincomun_pago_solicitud newInstance(){
        return new Fragment_fincomun_pago_solicitud();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_pago, container, false),R.drawable.background_splash_header_3);
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

        ll_camara = getView().findViewById(R.id.ll_camara);
        ll_camara.setOnClickListener(onClickListenerCamara);
        imv_camara = getView().findViewById(R.id.imv_camara);
        imv_camara.setOnClickListener(onClickListenerCamara);

        ll_galeria = getView().findViewById(R.id.ll_galeria);
        ll_galeria.setOnClickListener(onClickListenerGaleria);
        imv_galeria = getView().findViewById(R.id.imv_galeria);
        imv_galeria.setOnClickListener(onClickListenerGaleria);
    }


    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getContext().startActivityForResult(intent, GALLERY_REQUEST);
    }

    public void setResultCamera(){

        getContextMenu().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                if(intent != null) {

                    try {

                        Uri imageUri = intent.getData();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContextMenu().getContentResolver() , imageUri);
                        decifrarQR(scanQRImage(bitmap).trim());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void capturePictureFromCamera() {

        ScanActivity.action = new ZBarScannerView.ResultHandler() {
            @Override
            public void handleResult(me.dm7.barcodescanner.zbar.Result result) {

                if(result == null || result.getContents() == null || result.getContents().trim().isEmpty())
                    return;

                decifrarQR(result.getContents().trim());

            }
        };

        getContext().startActivity(ScanActivity.class, false);

    }

    private String scanQRImage(Bitmap bMap) {

        try {

            String contents = null;

            int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            contents = result.getText();

            return contents;

        } catch (Exception ex) {
            return "";
        }

    }

    private void decifrarQR(String qr) {

        getContext().loading(true);

        getContextMenu().gethCoDi().decifrarQR(qr, new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().loading(false);
                ModelObjetoCobro dataObtained  = (ModelObjetoCobro) data[0];
                getContext().setFragment(Fragment_fincomun_pago_confimacion.newInstance(new Gson().toJson(dataObtained), dataObtained.getFCIdentificadorCobro()));
            }
        });
    }


}
