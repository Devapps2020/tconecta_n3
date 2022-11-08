package com.blm.qiubopay.modules.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.listeners.ISendRestorePassword;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.restore_password.QPAY_RestorePassword;
import com.blm.qiubopay.tools.CheckInternetConnectionHelper;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_recuperar_cuenta extends HFragment {

    private QPAY_RestorePassword data;

    private ArrayList<CViewEditText> campos = null;
    private Button btn_siguiente;

    public static Fragment_recuperar_cuenta newInstance(Object... data) {
        Fragment_recuperar_cuenta fragment = new Fragment_recuperar_cuenta();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_recuperar_cuenta_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        campos = new ArrayList();

        btn_siguiente = getView().findViewById(R.id.btn_siguiente);

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
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

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_email))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_access_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_access_26)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = new QPAY_RestorePassword();
                data.setQpay_mail(campos.get(0).getText());
                data.setQpay_cellphone(campos.get(1).getText());
                data.setQpay_merchant_postal_code("");
                data.setQpay_password(null);
                data.setQpay_new_password(null);

                restorePassword(data, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().startActivity(LoginActivity.class, true);
                    }
                });

            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_siguiente.setEnabled(false);
                return;
            }

        btn_siguiente.setEnabled(true);
    }

    private void restorePassword(QPAY_RestorePassword request, final IFunction function) {

        getContext().loading(true);

        if(CheckInternetConnectionHelper.isAvailable(getContext())) {

            try {

                ISendRestorePassword restore_password = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        getContext().loading(false);

                        if (result instanceof ErrorResponse) {
                            getContext().alert(R.string.general_error);
                        }else{
                            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BaseResponse.QPAY_BaseResponseExcluder()).create();
                            String json = gson.toJson(result);
                            QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                            if(response.getQpay_response().equals("true")) {

                                getContext().alert("Se ha enviado una contraseña temporal a su correo electrónico.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        if(function != null)
                                            function.execute();
                                    }
                                });

                            }else{
                                getContext().alert(response.getQpay_description());
                            }
                        }
                    }

                    @Override
                    public void onConnectionFailed(Object result) {
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);
                    }
                }, getContext());

                restore_password.restorePassword(request, false);

            }catch (Exception e) {
                e.printStackTrace();
                getContext().loading(false);
                getContext().alert(R.string.general_error_catch);
            }
        }
    }


}

