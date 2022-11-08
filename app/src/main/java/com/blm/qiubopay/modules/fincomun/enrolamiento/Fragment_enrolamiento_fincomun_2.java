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

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_fincomun_1;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaDispositivoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCEnvioTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaTokenRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaDispositivoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaUsuarioResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaTokenResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_enrolamiento_fincomun_2 extends HFragment implements IMenuContext {


    private ArrayList<HEditText> campos;
    private Object data;

    private Button btn_continuar;

    public static Fragment_enrolamiento_fincomun_2 newInstance(Object... data) {
        Fragment_enrolamiento_fincomun_2 fragment = new Fragment_enrolamiento_fincomun_2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_enrolamiento_fincomun_2, container, false),R.drawable.background_splash_header_1);
    }

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

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edit_contrasenia_2),
                true, 10, 1, HEditText.Tipo.PASS_TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.edit_contrasenia_2_line)));

        btn_continuar = getView().findViewById(R.id.btn_continuar);
        Button btn_cancelar = getView().findViewById(R.id.btn_cancelar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!campos.get(1).getText().equals(campos.get(2).getText())) {
                    getContext().alert(HCoDi.TAG, "Las contraseñas no coinciden", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            campos.get(1).getEditText().setText("");
                            campos.get(2).getEditText().setText("");

                        }
                    });
                    return;
                }

                altaUsuario();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getContextMenu().initHome();
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

    public void altaUsuario() {

        getContext().loading(true);

        FCAltaUsuarioRequest request = new FCAltaUsuarioRequest();
        request.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setContrasena(campos.get(1).getText());
        request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
        request.setNumCliente(Fragment_prestamos_fincomun_1.banderas.getNumCliente());
        request.setUsuario(campos.get(0).getText());

        Logger.d(new Gson().toJson(request));

        getContext().loading(true);
        getContextMenu().gethCoDi().altaUsuario(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                FCAltaUsuarioResponse response = (FCAltaUsuarioResponse)data[0];
                if(response.codigo != 0) {
                    getContext().alert(HCoDi.TAG, response.getDescripcion());
                    getContext().loading(false);
                    return;
                }


                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... data) {
                        altaDispositivo(data[0]);
                    }
                });

            }
        });

    }

    public void altaDispositivo(String token) {

        FCAltaDispositivoRequest request = new FCAltaDispositivoRequest(
                Fragment_prestamos_fincomun_1.banderas.getNumCliente(),
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(), token);

        Logger.d(new Gson().toJson(request));

        getContextMenu().gethCoDi().altaDispositivo(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                FCAltaDispositivoResponse response = (FCAltaDispositivoResponse)data[0];
                if(response.codigo != 0) {
                    getContext().alert(HCoDi.TAG, response.getDescripcion());
                    getContext().loading(false);
                    return;
                }

                FCEnvioTokenRequest request = new FCEnvioTokenRequest();
                request.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
                request.setNumCliente(Fragment_prestamos_fincomun_1.banderas.getNumCliente());
                request.setTipoSol(1);

                getContextMenu().gethCoDi().envioToken(request, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContext().loading(false);

                        getContextMenu().gethCoDi().confirmToken(null, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                getContext().loading(true);
                                FCValidaTokenRequest request = new FCValidaTokenRequest();
                                request.setNumCliente(Fragment_prestamos_fincomun_1.banderas.getNumCliente());
                                request.setTipoSol(1);
                                request.setToken((String) data[0]);
                                request.setTokenJwt(token);

                                getContextMenu().gethCoDi().validaToken(request, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {

                                        FCValidaTokenResponse response = (FCValidaTokenResponse)data[0];
                                        getContext().alert(HCoDi.TAG, response.getDescripcion(), new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                          //      getContextMenu().initFC();

                                            }
                                        });


                                        getContext().loading(false);

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
