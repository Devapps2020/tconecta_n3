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
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.ISendRestorePassword;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.restore_password.QPAY_RestorePassword;
import com.blm.qiubopay.tools.CheckInternetConnectionHelper;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_recuperar_pass extends HFragment {

    private QPAY_Login data;

    private ArrayList<CViewEditText> campos = null;
    private Button btn_enviar;
    private boolean pass;

    public static Fragment_recuperar_pass newInstance(Object... data) {
        Fragment_recuperar_pass fragment = new Fragment_recuperar_pass();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_recuperar_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_recuperar_2"), QPAY_Login.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_recuperar_pass, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        campos = new ArrayList();

        btn_enviar = getView().findViewById(R.id.btn_enviar);

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

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_pass))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT_PASS)
                .setHint(R.string.text_access_4)
                .setAlert(R.string.text_input_required)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_pass_confirm))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT_PASS)
                .setHint(R.string.text_access_error_6)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_19)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(validate));


        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QPAY_RestorePassword request = new QPAY_RestorePassword();
                request.setQpay_mail(data.getQpay_mail());
                request.setQpay_password(data.getQpay_password());
                request.setQpay_new_password(campos.get(0).getText());
                request.setQpay_merchant_postal_code(null);
                request.setQpay_cellphone(null);

                final QPAY_UserCredentials credentials = new QPAY_UserCredentials();
                credentials.setUser(data.getQpay_mail());
                credentials.setPwd(data.getQpay_password());
                credentials.setFcmId(AppPreferences.getFCM());

                restorePassword(request, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        AppPreferences.setUserCredentials(credentials);
                        getContext().startActivity(LoginActivity.class, true);
                    }
                });

            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_enviar.setEnabled(false);
                return;
            }

        if(campos.size() > 0 && !campos.get(0).getText().equals(campos.get(1).getText())){
            campos.get(1).activeError();
            return;
        }

        btn_enviar.setEnabled(true);
    }

    private void restorePassword(QPAY_RestorePassword request, final IFunction function) {

        request.setQpay_new_password(Tools.encodeSHA256(request.getQpay_new_password()));

         getContext().loading(true);

        if(CheckInternetConnectionHelper.isAvailable(getContext())) {
            //Proceso de Login
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

                                getContext().alert("Su contraseña se ha cambiado con éxito.", "Es necesario que inicie sesión nuevamente.", new IAlertButton() {
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

                restore_password.restorePassword(request, true);

            }catch (Exception e) {
                e.printStackTrace();
                getContext().loading(false);
                getContext().alert(R.string.general_error_catch);
            }
        }
    }

}

