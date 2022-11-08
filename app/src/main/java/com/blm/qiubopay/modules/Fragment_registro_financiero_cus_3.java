package com.blm.qiubopay.modules;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_registro_financiero_cus_3 extends HFragment implements IMenuContext {


    private ImageView img_photo_contrato;
    private ImageView img_photo_comprobante;
    private Button btn_confirmar;
    private ArrayList<CViewEditText> campos;
    private Uri uri;
    private CViewEditText edit_numero_celular;
    private QPAY_FinancialInformation data = SessionApp.getInstance().getFinancialInformation();
    private ImageView img_contrato;

    public static Fragment_registro_financiero_cus_3 newInstance(Object... data) {
        Fragment_registro_financiero_cus_3 fragment = new Fragment_registro_financiero_cus_3();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_3", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_registro_financiero_cus_3, container, false),R.drawable.background_splash_header_1);
    }
    @Override

    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        data.setQpay_image_3(null);
        data.setQpay_image_4(null);

        campos = new ArrayList();

        // Inicializar elementos de pantalla

        edit_numero_celular = CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_access_26)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validate();
                    }
                });

        campos.add(edit_numero_celular);

        img_contrato = getView().findViewById(R.id.img_contrato);
        img_photo_contrato = getView().findViewById(R.id.img_photo_contrato);

        ImageView img_add_photo_comprobante = getView().findViewById(R.id.img_add_photo_comprobante);
        img_photo_comprobante = getView().findViewById(R.id.img_photo_comprobante);

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        //Se obtiene el celular y se coloca en pantalla
        String cel = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone();
        campos.get(0).setText(cel);

        //Listeners para elementos de pantalla

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1-Registrar cuenta   2-Abrir cuenta nueva
                data.setQpay_account_action("2");
                // Limpiar parametros de Registrar Cuenta en caso de que los ingresara previamente
                data.setQpay_account_number(null);
                data.setQpay_bank_name(null);
                // Colocar datos propios de la pantalla
                data.setQpay_cellphone(campos.get(0).getText());
                data.setQpay_image_name_3("proof_of_address.png");
                data.setQpay_image_name_4("contract_sign.png");
                // Continuamos a la captura de domicilio en el nuevo flujo

                getContext().setFragment(Fragment_registro_financiero_2.newInstance());

            }
        });

        img_contrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSigningAction();
            }
        });

        img_photo_contrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSigningAction();
            }
        });

        img_add_photo_comprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setCameraAction();
                setOCRAction();
            }
        });

        img_photo_comprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOCRAction();
            }
        });


    }

    /**
     * Validar campos requeridos
     */
    private void validate(){

        btn_confirmar.setEnabled(false);

        for(int i=0; i<campos.size(); i++) {
            if (!campos.get(i).isValid()) {
                return;
            }
        }

        //Validar firma
        if(data.getQpay_image_4() == null){
            img_photo_contrato.setBackground(getContext().getDrawable(R.drawable.background_view_border_red));
            return;
        }

        img_photo_contrato.setBackground(getContext().getDrawable(R.drawable.background_view_border_green));

        //Validar comprobante de domicilio
        if(data.getQpay_image_3() == null){
            img_photo_comprobante.setBackground(getContext().getDrawable(R.drawable.background_view_border_red));
            return;
        }

        btn_confirmar.setEnabled(true);

    }

    /**
     * Ejecuta la camara
     */
    private void setCameraAction() {
        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {
                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        setResultCamera();
                        uri = Utils.getFilePhoto(getContext());
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        getContext().startActivityForResult(i, 1);
                    }
                }, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE });
            }
        }, new String[]{ Manifest.permission.CAMERA });

    }

    /**
     * Ejecuta la acción para que el usuario realice su firma
     */
    private void setSigningAction() {
        // Se coloca la funcion a ejecutar en la pantalla de firma
        getContextMenu().setImage = new IFunction<Bitmap>() {
            @Override
            public void execute(Bitmap... dataSign) {
                img_contrato.setVisibility(View.GONE);
                img_photo_contrato.setImageBitmap(dataSign[0]);
                data.setQpay_image_4(Utils.convert(dataSign[0]));
                validate();
                getContext().backFragment();
                getContext().backFragment();
            }
        };

        Fragment_browser.execute = new IAlertButton() {
            @Override
            public String onText() {
                return "Continuar";
            }

            @Override
            public void onClick() {
                getContext().setFragment(Fragment_registro_financiero_5.newInstance());
            }
        };

        getContext().setFragment(Fragment_browser.newInstance(Globals.URL_CONTRACT));


    }

    /**
     * Indica lo que ocurrira tras la captura de fotografía
     */
    private void setResultCamera(){

        getContext().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                try {

                    final Bitmap image = Utils.decodeSampledBitmapFromFile();
                    String photoEnconded = Utils.convert(image);
                    setPhoto(image,photoEnconded,resultCode);

                    Utils.deleteFilePhoto();
                    uri = null;

                } catch (Exception e) {

                }
            }
        });

    }

    /**
     * Arma la accion para ejecutar OCR
     */
    private void setOCRAction(){
        setResultOCR();
        Intent intent = new Intent(getContext(), OcrActivity.class);
        getContext().startActivityForResult(intent, OcrActivity.RC_OCR_CAPTURE);
    }

    /**
     * Metodo para colocar el resultado de lo interpretado por el lector OCR
     */
    private void setResultOCR() {

        getContext().setOnActivityResult(new IActivityResult() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent dataResult) {

                if(requestCode == OcrActivity.RC_OCR_CAPTURE) {
                    if (resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                        if (dataResult != null) {

                            byte[] originalPhoto = dataResult.getByteArrayExtra(OcrActivity.PHOTO_TAKEN);

                            if(originalPhoto!=null){
                                Bitmap image = Utils.decodeSampledBitmapFromByteArray(originalPhoto);
                                String photoEncoded = Base64.encodeToString(originalPhoto, Base64.DEFAULT);
                                setPhoto(image,photoEncoded,resultCode);
                            }

                        } else {
                            Log.d("OCR", "No Text captured, intent data is null");
                        }
                    } else {
                        Log.d("OCR", "ERROR");
                    }
                }
            }

        });

    }

    /**
     * Metodo para presentar la foto en pantalla
     * @param image
     * @param photoEncoded
     * @param resultCode
     */
    private void setPhoto(Bitmap image, String photoEncoded, int resultCode) {

        Log.d("OcrActivity","Codigo Resultado: " + resultCode);

        img_photo_comprobante.setImageBitmap(image);
        img_photo_comprobante.setBackground(getContext().getDrawable(R.drawable.background_view_border_green));

        if (resultCode != CommonStatusCodes.SUCCESS_CACHE)
            data.setQpay_image_3(null);
        else
            data.setQpay_image_3(photoEncoded);

        validate();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

