package com.blm.qiubopay.modules.fincomun.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_2;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_6;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_menu;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.credito.Fragment_fincomun_detalle_credito;
import com.blm.qiubopay.modules.fincomun.operacionesqr.Fragment_fincomun_operaciones_qr;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_detalle_fincomun_1;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaDispositivoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAperturaCuentaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCEnvioTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaTokenRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaDispositivoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaUsuarioResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAperturaCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaTokenResponse;
import mx.com.fincomun.origilib.Http.Response.Login.FCLoginResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCPlanPagosResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Objects.Credito.DHPlanPago;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_login extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private TextView tv_forgot_password,tv_title,tv_user;
    private LinearLayout ll_user;
    public enum TypeLogin{
        ORIGINACION,
        CODI,
        APERTURA
    }
    public static TypeLogin type = TypeLogin.ORIGINACION;
    private Boolean isLoading  =false;

    public static Fragment_fincomun_login newInstance() {
        Fragment_fincomun_login fragment = new Fragment_fincomun_login();

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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_oferta"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_login, container, false),R.drawable.background_splash_header_1);
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
        tv_title = getView().findViewById(R.id.tv_title);
        ll_user = getView().findViewById(R.id.ll_user);

        tv_forgot_password = getView().findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... data) {
                        sendToken(data[0]);
                    }
                });
            }
        });
        campos = new ArrayList();
        campos.add(FCEditText.create(getView().findViewById(R.id.et_password))
                .setMinimum(8)
                .setMaximum(8)
                .setType(FCEditText.TYPE.TEXT_PASS)
                .setHint("Introduce tu contraseña")
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setAlert(R.string.text_input_required));

        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;

                    switch (type) {
                        case APERTURA:
                            if (TextUtils.isEmpty(campos.get(0).getText()) || !campos.get(0).getText().matches("^(?=\\w*\\d)(?=\\w*[A-Z])\\S{8}$")) {
                                campos.get(0).setRequired(true);
                                isLoading = false;
                                getContextMenu().alert("Ingresa una contraseña valida");
                            } else {
                                altaUsuario();
                            }

                            break;
                        default:
                            campos.get(0).setRequired(true);
                            if (campos.get(0).isValid()) {
                                postLogin();
                            } else {
                                isLoading = false;
                                getContextMenu().alert("Ingresa una contraseña valida");
                            }
                            break;
                    }
                }
            }
        });

        configView();
    }

    private void configView() {
        switch (type){
            case APERTURA:
                tv_title.setText("Para tu seguridad, crea una contraseña para visualizar el crédito");
                tv_user.setText("Usuario: "+ AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());
                tv_forgot_password.setVisibility(View.GONE);
                ll_user.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void postLogin() {
        getContext().loading(true);

        getContextMenu().gethCoDi().login(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id(), campos.get(0).getText(), new IFunction() {
            @Override
            public void execute(Object[] data) {

                if (data!=null) {
                    FCLoginResponse response = (FCLoginResponse) data[0];
                    Logger.d("RESPONSE : " + new Gson().toJson(response));

                    HCoDi.numCliente = response.getUsuario().getNumCliente();
                    HCoDi.nomCliente = response.getUsuario().getNombre() + " " + response.getUsuario().getApPaterno() + " " + response.getUsuario().getApMaterno();

                    if (response.getCodigo() == 0) {
                        getContext().loading(false);
                        switch (type) {
                            case CODI:
                                String user = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
                                getContextMenu().getTokenFC(new IFunction<String>() {
                                    @Override
                                    public void execute(String... data) {

                                        getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {

                                                FCConsultaCuentasResponse response = (FCConsultaCuentasResponse) data[0];
                                                SessionApp.getInstance().setCuentasCoDi(response.getCuentas());
                                                HCoDi.usuario = user;
                                                HCoDi.password = campos.get(0).getText();
                                                getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance());
                                            }
                                        });
                                    }
                                });
                                break;
                            case ORIGINACION:
                                getContextMenu().getTokenFC((String... info) -> {
                                    getContextMenu().getPlanPagos(SessionApp.getInstance().getDhListaCreditos().getNumeroCredito(), info[0], new IFunction() {

                                        @Override
                                        public void execute(Object[] data) {
                                            if (data != null && data[0] != null) {
                                                Fragment_fincomun_detalle_credito.detalle = new Gson().fromJson(String.valueOf(data[0]), FCPlanPagosResponse.class);
                                            }
                                            getContext().setFragment(Fragment_fincomun_detalle_credito.newInstance());
                                        }
                                    });
                                });
                                break;
                        }
                        isLoading = false;

                    } else {
                        getContext().loading(false);
                        getContext().alert(response.getDescripcion());
                    }
                }else{
                    isLoading = false;
                }
            }
        });
    }

    private void sendToken(String token) {
        FCEnvioTokenRequest request = new FCEnvioTokenRequest();
        request.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
        request.setNumCliente(HCoDi.numCliente);
        request.setTipoSol(1);

        getContextMenu().gethCoDi().envioToken(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                getContext().loading(false);

                getContextMenu().gethCoDi().confirmToken(null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        FCValidaTokenRequest valida = new FCValidaTokenRequest();
                        valida.setTokenJwt(token);
                        valida.setToken((String) data[0]);
                        valida.setNumCliente(HCoDi.numCliente);
                        valida.setTipoSol(1);

                        getContext().loading(true);

                        getContextMenu().gethCoDi().validaToken(valida, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                FCValidaTokenResponse responseValida = (FCValidaTokenResponse)data[0];

                                if(responseValida.getCodigo() != 0) {
                                    getContext().alert("Fincomún", responseValida.getDescripcion());
                                    return;
                                }

                                request.setTipoSol(2);

                                getContextMenu().gethCoDi().envioToken(request, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {

                                        getContextMenu().gethCoDi().confirmToken("Ingresa el código que te enviamos a tu correo",new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {

                                                valida.setToken((String) data[0]);
                                                valida.setTipoSol(2);

                                                getContext().loading(true);

                                                getContextMenu().gethCoDi().validaToken(valida, new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {

                                                        FCValidaTokenResponse responseValida = (FCValidaTokenResponse)data[0];

                                                        if(responseValida.getCodigo() != 0) {
                                                            getContext().loading(false);
                                                            getContext().alert("Fincomún", responseValida.getDescripcion());
                                                            return;
                                                        }

                                                        getContextMenu().setFragment(Fragment_fincomun_cambio_contrasenia.newInstance());
                                                    }
                                                });

                                            }
                                        });


                                    }
                                });

                            }
                        });

                    }
                });


            }
        });
    }

    public void altaUsuario() {

        getContext().loading(true);

        FCAltaUsuarioRequest request = new FCAltaUsuarioRequest();
        request.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setContrasena(campos.get(0).getText());
        request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());

        String numCliente = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone();
        if (!TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())){
            numCliente = SessionApp.getInstance().getFcBanderas().getNumCliente();
        }
        request.setNumCliente(numCliente);
        request.setUsuario(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());

        Logger.d(new Gson().toJson(request));

        getContext().loading(true);
        getContextMenu().gethCoDi().altaUsuario(request, new IFunction() {
            @Override
            public void execute(Object[] data) {
                if (data!= null) {
                    FCAltaUsuarioResponse response = (FCAltaUsuarioResponse) data[0];
                    Logger.d(new Gson().toJson(response));

                    if (response.codigo != 0) {
                        getContext().alert(response.getDescripcion());
                        getContext().loading(false);
                        return;
                    }

                    getContextMenu().getTokenFC(new IFunction<String>() {
                        @Override
                        public void execute(String... data) {
                            if (data!=null) {
                                altaDispositivo(data[0]);
                            }else{
                                isLoading = false;
                            }
                        }
                    });
                }else {
                    isLoading = false;
                }

            }
        });

    }

    public void altaDispositivo(String token) {

        String numCliente = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone();
        if (!TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())){
            numCliente = SessionApp.getInstance().getFcBanderas().getNumCliente();
        }
        FCAltaDispositivoRequest request = new FCAltaDispositivoRequest(
                numCliente,
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(),
                token);

        Logger.d(new Gson().toJson(request));

        getContextMenu().gethCoDi().altaDispositivo(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                if (data!=null) {
                    FCAltaDispositivoResponse response = (FCAltaDispositivoResponse) data[0];
                    Logger.d(new Gson().toJson(response));

                    if (response.codigo != 0) {
                        getContext().alert(response.getDescripcion());
                        getContext().loading(false);
                        return;
                    }

                    getContextMenu().gethCoDi().actualizaBandera(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(), token, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            getContext().loading(false);


                            getContextMenu().alert("Contraseña creada, inicia sesión para ver el detalle de tu crédito", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "CERRAR";
                                }

                                @Override
                                public void onClick() {
                                    getContextMenu().initHome();
                                }
                            });

                        }
                    });
                    isLoading = false;
                }else {
                    isLoading = false;
                }
            }
        });
    }

}
