package com.blm.qiubopay.modules.fincomun.enrolamiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_menu;
import com.blm.qiubopay.modules.fincomun.operacionesqr.Fragment_fincomun_operaciones_qr;
import com.blm.qiubopay.utils.SessionApp;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Response.Login.FCLoginResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_enrolamiento_fincomun_3 extends HFragment implements IMenuContext {


    private ArrayList<HEditText> campos;
    private Object data;

    private Button btn_continuar;

    public static Fragment_enrolamiento_fincomun_3 newInstance(Object... data) {
        Fragment_enrolamiento_fincomun_3 fragment = new Fragment_enrolamiento_fincomun_3();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_enrolamiento_fincomun_3, container, false),R.drawable.background_splash_header_1);
    }

    @Override

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        campos = new ArrayList();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {
            }
        };

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edit_usuario),
                true, 10, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.edit_usuario_line)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edit_contrasenia_1),
                true, 10, 1, HEditText.Tipo.PASS_TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.edit_contrasenia_1_line)));

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().loading(true);
                getContextMenu().gethCoDi().login(campos.get(0).getText(), campos.get(1).getText(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        FCLoginResponse response = (FCLoginResponse)data[0];
                        HCoDi.numCliente = response.getUsuario().getNumCliente();
                        HCoDi.nomCliente = response.getUsuario().getNombre() + " " +  response.getUsuario().getApPaterno() + " " +  response.getUsuario().getApMaterno();

                        if(response.getCodigo() == 0) {
                            getContext().loading(false);
                            getContextMenu().getTokenFC(new IFunction<String>() {
                                @Override
                                public void execute(String... data) {

                                    getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                                        @Override
                                        public void execute(Object[] data) {

                                            FCConsultaCuentasResponse response = (FCConsultaCuentasResponse)data[0];
                                            SessionApp.getInstance().setCuentasCoDi(response.getCuentas());
                                            HCoDi.usuario = campos.get(0).getText();
                                            HCoDi.password = campos.get(1).getText();

                                            //TODO : ELIMINAR LINEAS COMENTADAS
                                            //getContext().setFragment(Fragment_codi_menu.newInstance());
                                            getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance());
                                        }
                                    });
                                }
                            });

                        } else {
                            getContext().loading(false);
                            getContext().alert(HCoDi.TAG, response.getDescripcion());
                        }

                    }
                });
            }
        });


    }

    public void validate(){

        btn_continuar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_continuar.setEnabled(true);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
