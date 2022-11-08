package com.blm.qiubopay.modules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.ICashCollection;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_CashCollection;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.models.ocr.OcrMatch;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.HashMap;

import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_depositos_2 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;

    private ArrayList<CViewEditText> campos;
    private Button btn_confirmar;

    //OCR id's de retorno
    private final String COL_1 = "COL 1.";
    private final String COL_2 = "COL 2.";
    private final String COL_3 = "COL 3.";

    //ID REGEX
    private final String ID_REGEX_1 = "COL 1. [0-9]{5} [0-9]{5}";
    private final String ID_REGEX_2 = "COL 2. [0-9]{5} [0-9]{5}";
    private final String ID_REGEX_3 = "COL 3. [0-9]{5} [0-9]{5}";

    public static Fragment_depositos_2 newInstance(Object... data) {
        Fragment_depositos_2 fragment = new Fragment_depositos_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_depositos_2", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_depositos_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        LinearLayout layout_scaner = view.findViewById(R.id.layout_scaner);

        layout_scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOCRAction();
            }
        });

        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QPAY_CashCollection petition = new QPAY_CashCollection();
                petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                petition.setQpay_pin1(campos.get(0).getText());
                petition.setQpay_pin2(campos.get(1).getText());
                petition.setQpay_pin3(campos.get(2).getText());
                petition.setQpay_merchantId(null);

                realizarTransaccion(petition);
            }
        });

        campos = new ArrayList();

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_pin_1))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_depositos_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_pin_2))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_depositos_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_pin_3))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_depositos_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_confirmar.setEnabled(false);
                return;
            }

        btn_confirmar.setEnabled(true);
    }

    public void realizarTransaccion(final QPAY_CashCollection petition){

        context.loading(true);

        CApplication.setAnalytics(CApplication.ACTION.CB_RECOLECCION_BIMBO_confirmar_RBF02);

        try {

            final ICashCollection sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    context.loading(false);
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        CApplication.setAnalytics(CApplication.ACTION.CB_RECOLECCION_BIMBO_noexitoso_RBF03_c);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CashCollectionResponse.QPAY_CashCollectionResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_CashCollectionResponse saleResponse = gson.fromJson(json, QPAY_CashCollectionResponse.class);

                        Logger.i(new Gson().toJson(saleResponse));

                        if(saleResponse.getQpay_response().equals("true")){

                            CApplication.setAnalytics(CApplication.ACTION.CB_recoleccion_bimbo_exitoso);

                            context.cargarSaldo(true,false,false,new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    context.alert("Abono a cuenta exitoso.", new IAlertButton() {
                                        @Override
                                        public String onText() {
                                            return "Aceptar";
                                        }

                                        @Override
                                        public void onClick() {
                                            context.initHome();
                                        }
                                    });

                                }
                            });

                        } else{
                            context.loading(false);
                            context.validaSesion(saleResponse.getQpay_code(), saleResponse.getQpay_description());
                            CApplication.setAnalytics(CApplication.ACTION.CB_RECOLECCION_BIMBO_noexitoso_RBF03_c);
                        }


                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                    CApplication.setAnalytics(CApplication.ACTION.CB_RECOLECCION_BIMBO_noexitoso_RBF03_c);
                }
            }, context);

            sale.doCashCollection(petition);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
            CApplication.setAnalytics(CApplication.ACTION.CB_RECOLECCION_BIMBO_noexitoso_RBF03_c);
        }

    }

    /**
     * Arma la accion para ejecutar OCR
     */
    private void setOCRAction(){
        setResultOCR();
        Intent intent = new Intent(context, OcrActivity.class);
        intent.putParcelableArrayListExtra(OcrActivity.OBJECT_TO_READ, buildOCRObjectToMatch());
        context.startActivityForResult(intent, OcrActivity.RC_OCR_CAPTURE);
    }

    /**
     * Método para armar el objeto a hacer match con el OCR
     * @return
     */
    private ArrayList<OcrMatch> buildOCRObjectToMatch(){

        ArrayList<OcrMatch> ocrMatchList = new ArrayList<OcrMatch>();
        OcrMatch ocrMatch;


        ocrMatch = new OcrMatch(COL_1, ID_REGEX_1, true, null);
        ocrMatchList.add(ocrMatch);

        ocrMatch = new OcrMatch(COL_2, ID_REGEX_2, true, null);
        ocrMatchList.add(ocrMatch);

        ocrMatch = new OcrMatch(COL_3, ID_REGEX_3, true, null);
        ocrMatchList.add(ocrMatch);


        return ocrMatchList;
    }


    /**
     * Metodo para colocar el resultado de lo interpretado por el lector OCR
     */
    private void setResultOCR() {

        context.setOnActivityResult(new IActivityResult() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent dataResult) {

                if(requestCode == OcrActivity.RC_OCR_CAPTURE) {
                    if (resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                        if (dataResult != null) {

                            HashMap<String,String> ocrReaded = (HashMap<String, String>) dataResult.getSerializableExtra(OcrActivity.OBJECT_READED);
                            byte[] originalPhoto = dataResult.getByteArrayExtra(OcrActivity.PHOTO_TAKEN);

                            if(ocrReaded!=null) {

                                if(ocrReaded.containsKey(COL_1))
                                    campos.get(0).setText(ocrReaded.get(COL_1).replace(COL_1,"").replace(" ",""));

                                if(ocrReaded.containsKey(COL_2))
                                    campos.get(1).setText(ocrReaded.get(COL_2).replace(COL_2,"").replace(" ",""));

                                if(ocrReaded.containsKey(COL_3))
                                    campos.get(2).setText(ocrReaded.get(COL_3).replace(COL_3,"").replace(" ",""));

                                if(ocrReaded.size() > 0)
                                    context.alert("Favor de validar los datos capturados");

                            }

                            if(originalPhoto!=null){
                                Bitmap image = Utils.decodeSampledBitmapFromByteArray(originalPhoto);
                                String photoEncoded = Base64.encodeToString(originalPhoto, Base64.DEFAULT);
                                //setPhoto(image,photoEncoded,resultCode);
                            }

                        } else {

                            context.alert("No se encontró ningún texto");
                            Log.d("OCR", "No Text captured, intent data is null");

                        }
                    } else {

                        Log.d("OCR", "ERROR");

                    }
                }
            }

        });

    }

}

