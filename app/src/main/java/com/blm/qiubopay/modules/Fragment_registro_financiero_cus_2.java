package com.blm.qiubopay.modules;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.listeners.IGetBankFromClabe;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.bank.QPAY_BankPetition;
import com.blm.qiubopay.models.bank.QPAY_BankResponse;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_registro_financiero_cus_2 extends HFragment implements IMenuContext {



    private ImageView img_photo_estado_cuenta;
    private ImageView img_photo_comprobante;
    private Button btn_confirmar;
    private ArrayList<CViewEditText> campos = null;
    private Uri uri;
    private QPAY_FinancialInformation data = SessionApp.getInstance().getFinancialInformation();
    private int imagen = 0;
    private boolean queryBankSucces;

    public static Fragment_registro_financiero_cus_2 newInstance(Object... data) {
        Fragment_registro_financiero_cus_2 fragment = new Fragment_registro_financiero_cus_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_registro_financiero_cus_2, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        queryBankSucces = false;

        ImageView img_add_photo_estado_cuenta = getView().findViewById(R.id.img_add_photo_estado_cuenta);
        img_photo_estado_cuenta = getView().findViewById(R.id.img_photo_estado_cuenta);
        ImageView img_add_photo_comprobante = getView().findViewById(R.id.img_add_photo_comprobante);
        img_photo_comprobante = getView().findViewById(R.id.img_photo_comprobante);
        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        data.setQpay_image_3(null);
        data.setQpay_image_4(null);

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };


        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_clabe))
                .setRequired(true)
                .setMinimum(18)
                .setMaximum(18)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_registro_14)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_21)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(text.length() > 17) {
                            getBankName(text);
                            queryBankSucces = true;
                        } else {
                            queryBankSucces = false;
                            campos.get(1).setText("");
                        }

                        validate();

                    }
                }));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_banco))
                .setEnabled(false)
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(25)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_registro_15)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1-Registrar cuenta   2-Abrir cuenta nueva
                data.setQpay_account_action("1");
                // Colocar datos propios de la pantalla
                data.setQpay_account_number(campos.get(0).getText());
                data.setQpay_bank_name(campos.get(1).getText());
                data.setQpay_image_name_3("proof_of_address.png");
                data.setQpay_image_name_4("account_balance.png");
                // Colocar el celular registrado en el registro VAS
                data.setQpay_cellphone(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                // Continuamos a la captura de domicilio en el nuevo flujo

                getContextMenu().setFragment(Fragment_registro_financiero_2.newInstance());

            }
        });

        img_add_photo_estado_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = 1;
                //Es la cámara previa se cambia por la del OCR
                //setCameraAction();
                setOCRAction();
            }
        });

        img_photo_estado_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagen = 1;
                setOCRAction();
            }
        });

        img_add_photo_comprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = 2;
                //setCameraAction();
                setOCRAction();
            }
        });

        img_photo_comprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagen = 2;
                setOCRAction();
            }
        });

    }

    private void validate(){

        btn_confirmar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        //Validar estado de cuenta
        if(data.getQpay_image_4() == null){
            img_photo_estado_cuenta.setBackground(getContextMenu().getDrawable(R.drawable.background_view_border_red));
            return;
        }

        //Validar comprobante de domicilio
        if(data.getQpay_image_3() == null){
            img_photo_comprobante.setBackground(getContextMenu().getDrawable(R.drawable.background_view_border_red));
            return;
        }

        btn_confirmar.setEnabled(true);
    }

    /**
     * Arma la accion para ejecutar OCR
     */
    private void setOCRAction(){
        setResultOCR();
        Intent intent = new Intent(getContextMenu(), OcrActivity.class);
        getContextMenu().startActivityForResult(intent, OcrActivity.RC_OCR_CAPTURE);
    }

    /**
     * Metodo para colocar el resultado de lo interpretado por el lector OCR
     */
    private void setResultOCR() {

        getContextMenu().setOnActivityResult(new IActivityResult() {

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

        if(imagen == 1) {

            img_photo_estado_cuenta.setImageBitmap(image);
            img_photo_estado_cuenta.setBackground(getContextMenu().getDrawable(R.drawable.background_view_border_green));
            data.setQpay_image_4(photoEncoded);

        } else if (imagen == 2) {

            img_photo_comprobante.setImageBitmap(image);
            img_photo_comprobante.setBackground(getContextMenu().getDrawable(R.drawable.background_view_border_green));
            data.setQpay_image_3(photoEncoded);

        }

        validate();
    }

    private void getBankName(String clabe){

        if(queryBankSucces)
            return;

        QPAY_BankPetition objectPetition = new QPAY_BankPetition();
        objectPetition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        objectPetition.setQpay_clabe(clabe);

        getContextMenu().loading(true);

        try {

            IGetBankFromClabe petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        queryBankSucces = false;
                        campos.get(1).setText("");
                        getContextMenu().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BankResponse.QPAY_BankResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BankResponse response = gson.fromJson(json, QPAY_BankResponse.class);

                        if(response.getQpay_response().equals("true")){
                            if(null != response.getQpay_object()
                                    && response.getQpay_object().length != 0
                                    && null != response.getQpay_object()[0]) {
                                queryBankSucces = false;
                                campos.get(1).setText(response.getQpay_object()[0].getShortname());

                                validate();
                            }
                            else
                            {
                                queryBankSucces = false;
                                campos.get(1).setText("");
                                campos.get(1).activeError();
                                getContextMenu().alert("CLABE inválida.");
                            }
                        }
                        else
                        {
                            queryBankSucces = false;
                            campos.get(1).setText("");
                            getContextMenu().alert(response.getQpay_description());
                        }
                    }

                    getContextMenu().loading(false);

                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContextMenu().loading(false);
                    queryBankSucces = false;
                    campos.get(1).setText("");
                    getContextMenu().alert(R.string.general_error);
                }
            }, getContextMenu());

            petition.getBank(objectPetition);

        } catch (Exception e) {
            e.printStackTrace();
            getContextMenu().loading(false);
            getContextMenu().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

