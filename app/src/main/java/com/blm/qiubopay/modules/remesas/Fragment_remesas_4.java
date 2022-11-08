package com.blm.qiubopay.modules.remesas;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.views.HSignatureView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IRemesas;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.remesas.TC_PayRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_PayRemittanceResponse;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.logger.Logger;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_remesas_4 extends HFragment implements IMenuContext {

    private AlertDialog alertConfirmation = null;

    private TC_PayRemittancePetition payRemittancePetition;
    private TC_PayRemittanceResponse response;

    private Button btn_continuar;
    private CheckBox check_acept;

    private String sign = "";

    private HSignatureView signature;

    private Object data;

    public static Fragment_remesas_4 newInstance(Object... data) {
        Fragment_remesas_4 fragment = new Fragment_remesas_4();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_remesas_4", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_remesas_4 newInstance() {
        return new Fragment_remesas_4();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_4, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_remesas_4"), Object.class);
    }

    @Override
    public void initFragment() {

        Gson gson = new Gson();
        String queryData = gson.toJson(data);
        payRemittancePetition = gson.fromJson(queryData, TC_PayRemittancePetition.class);

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        signature = getView().findViewById(R.id.signature);

        btn_continuar = getView().findViewById(R.id.btn_continuar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payRemittance();
            }
        });

        ImageView img_exit = getView().findViewById(R.id.img_delete);
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.ClearCanvas();
            }
        });

        TextView text_terms = getView().findViewById(R.id.text_terms);
        text_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQrAlert();
            }
        });

        check_acept = getView().findViewById(R.id.check_acept);
        check_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btn_next.setEnabled(check_acept.isChecked());
                validate();
            }
        });
    }

    private void validate(){

        btn_continuar.setEnabled(false);

        if(!check_acept.isChecked())// || !signature.drawSomething())
            return;

        btn_continuar.setEnabled(true);

    }

    //Alerte de términos y condiciones
    public void showQrAlert(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_remesas_qr, null);

        Button btn_solicitar = view.findViewById(R.id.btn_aceptar);

        ImageView image_qr = view.findViewById(R.id.image_qr);

        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertConfirmation.dismiss();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap((String) Globals.URL_REMESAS_TERMINOS_Y_CONDICIONES, BarcodeFormat.QR_CODE, 150, 150);
            image_qr.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        alertConfirmation = builder.create();
        alertConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertConfirmation.show();

        builder = null;

    }

    private void payRemittance(){

        getContext().loading(true);

        payRemittancePetition.getBeneficiario().setImagenFirmaIdentificacion(Utils.convert(signature.getBitmap()));

        try {

            IRemesas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {

                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new TC_PayRemittanceResponse.TC_PayRemittanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        response = gson.fromJson(json, TC_PayRemittanceResponse.class);
                        Log.d("CONSULTA_INFO_REMESA", json);

                        if (response.getQpay_response().equals("true")) {

                            getContextMenu().cargarSaldo(true, false, false, new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    getContext().setFragment(Fragment_remesas_5.newInstance(response.getQpay_object()[0], payRemittancePetition));
                                }
                            });

                        } else {
                            getContext().loading(false);
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                    }

                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            service.payRemittance(payRemittancePetition);

        } catch (Exception e) {

            getContext().loading(false);

            e.printStackTrace();
            getContext().alert(R.string.general_error_catch);
        }

    }

}
