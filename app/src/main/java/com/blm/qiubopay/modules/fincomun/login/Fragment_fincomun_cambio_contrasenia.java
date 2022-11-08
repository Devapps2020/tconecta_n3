package com.blm.qiubopay.modules.fincomun.login;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.credito.Fragment_fincomun_detalle_credito;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.FCil.FCCambioContrasenaRequest;
import mx.com.fincomun.origilib.Http.Response.FCil.FCCambioContrasenaResponse;
import mx.com.fincomun.origilib.Http.Response.Login.FCLoginResponse;
import mx.com.fincomun.origilib.Model.FCil.CambioContrasena;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_cambio_contrasenia extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private TextView tv_user;

    public static Fragment_fincomun_login.TypeLogin type = Fragment_fincomun_login.TypeLogin.ORIGINACION;

    public static Fragment_fincomun_cambio_contrasenia newInstance() {
        Fragment_fincomun_cambio_contrasenia fragment = new Fragment_fincomun_cambio_contrasenia();

        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_cambio_contrasenia"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_cambio_contrasenia, container, false),R.drawable.background_splash_header_1);
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
                });

        tv_user = getView().findViewById(R.id.tv_user);
        tv_user.setText("Usuario: "+ AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());

        campos = new ArrayList();
        campos.add(FCEditText.create(getView().findViewById(R.id.et_password))
                .setMinimum(8)
                .setMaximum(8)
                .setType(FCEditText.TYPE.TEXT_PASS)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setHint("Introduce tu contraseña")
                .setAlert(R.string.text_input_required));

        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campos.get(0).setRequired(true);
                if (TextUtils.isEmpty(campos.get(0).getText()) || !campos.get(0).getText().matches("^(?=\\w*\\d)(?=\\w*[A-Z])\\S{8}$") ){
                    getContextMenu().alert("Ingresa una contraseña valida");
                }else {
                    postActualizaContrasenia();
                }
            }
        });
    }

    private void postActualizaContrasenia() {
        getContext().loading(true);
        CambioContrasena cambioContrasena = new CambioContrasena(getContext());

        FCCambioContrasenaRequest request = new FCCambioContrasenaRequest(
                HCoDi.numCliente,
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id(),
                campos.get(0).getText()
        );
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        cambioContrasena.postCambioContrasena(request, new CambioContrasena.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);
                FCCambioContrasenaResponse response = (FCCambioContrasenaResponse)e;
                Logger.d("REQUEST : "  + new Gson().toJson(response));

                if (response != null && response.getCodigo() == 0){
                    getContextMenu().alert("Contraseña actualizada", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "CERRAR";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().backFragment();
                        }
                    });
                }else{
                    getContextMenu().alert(response.getDescripcion(), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "ACEPTAR";
                        }

                        @Override
                        public void onClick() {
                        }
                    });
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContextMenu().alert(s, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "CERRAR";
                    }

                    @Override
                    public void onClick() {
                    }
                });
            }
        });
    }


}
