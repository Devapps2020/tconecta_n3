package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_buzon_1;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_notificaciones_1;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_quiero_cobrar_1;
import com.blm.qiubopay.utils.Globals;

import mx.com.fincomun.origilib.Http.Response.Banxico.ValidaCuenta.FCValidacionCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_fincomun_acciones_qr extends HFragment implements IMenuContext {

    public Fragment_fincomun_acciones_qr() {
    }

    public static Fragment_fincomun_acciones_qr newInstance(){
        return new Fragment_fincomun_acciones_qr();
    }

    private CardView cv_cobro_qr, cv_pago_qr, cv_notificaciones,cv_buzon;
    private LinearLayout ll_cobro_qr, ll_pago_qr, ll_notificacion, ll_buzon;
    private ImageView imv_cobro_qr, imv_pago_qr, imv_notificacion, imv_buzon;
    private TextView tv_cobro_qr, tv_pago_qr, tv_notificacion, tv_buzon,tv_registra_cuenta;

    private View.OnClickListener  onClickListenerPago  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iniciarCoDi(false, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContextMenu().requestPermissions(new IRequestPermissions() {
                        @Override
                        public void onPostExecute() {
                            //showImageOptionDialog();
                            getContext().setFragment(Fragment_fincomun_pago_solicitud.newInstance());


                        }
                    }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });
                }
            });
        }
    };

    private View.OnClickListener  onClickListenerCobro  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iniciarCoDi(false, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContextMenu().requestPermissions(new IRequestPermissions() {
                        @Override
                        public void onPostExecute() {
                            quieroCobrar();
                        }
                    }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });
                }
            });
        }
    };


    private View.OnClickListener onClickNotificaciones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iniciarCoDi(false, new IFunction() {
                @Override
                public void execute(Object[] data) {

                    if(AppPreferences.getCodiOmision())
                        getContext().setFragment(Fragment_codi_notificaciones_1.newInstance());
                    else
                        getContext().alert("CoDi", "¿Registrar aplicación por omisión?", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Si";
                            }

                            @Override
                            public void onClick() {

                                getContext().loading(true);
                                getContextMenu().gethCoDi().registroAppPorOmision(new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContext().loading(false);
                                        AppPreferences.setCodiOmision(true);
                                        getContext().setFragment(Fragment_codi_notificaciones_1.newInstance());
                                    }
                                });

                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "No";
                            }

                            @Override
                            public void onClick() {

                            }
                        });
                }
            });
        }
    };

    private View.OnClickListener onClickBuzon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iniciarCoDi(false, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContext().setFragment(Fragment_codi_buzon_1.newInstance());
                }
            });
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_menu_1, container, false),R.drawable.background_splash_header_2);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
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
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });

        cv_cobro_qr = getView().findViewById(R.id.cv_cobro_qr);
        cv_cobro_qr.setOnClickListener(onClickListenerCobro);

        ll_cobro_qr = getView().findViewById(R.id.ll_cobro_qr);
        ll_cobro_qr.setOnClickListener(onClickListenerCobro);

        imv_cobro_qr = getView().findViewById(R.id.imv_cobro_qr);
        imv_cobro_qr.setOnClickListener(onClickListenerCobro);

        tv_cobro_qr = getView().findViewById(R.id.tv_cobro_qr);
        tv_cobro_qr.setOnClickListener(onClickListenerCobro);

        cv_pago_qr = getView().findViewById(R.id.cv_pago_qr);
        cv_pago_qr.setOnClickListener(onClickListenerPago);

        ll_pago_qr = getView().findViewById(R.id.ll_pago_qr);
        ll_pago_qr.setOnClickListener(onClickListenerPago);

        imv_pago_qr = getView().findViewById(R.id.imv_pago_qr);
        imv_pago_qr.setOnClickListener(onClickListenerPago);

        tv_pago_qr = getView().findViewById(R.id.tv_pago_qr);
        tv_pago_qr.setOnClickListener(onClickListenerPago);

        cv_notificaciones= getView().findViewById(R.id.cv_notificaciones);
        ll_notificacion= getView().findViewById(R.id.ll_notificacion);
        imv_notificacion= getView().findViewById(R.id.imv_notificacion);
        tv_notificacion= getView().findViewById(R.id.tv_notificacion);

        cv_notificaciones.setOnClickListener(onClickNotificaciones);
        ll_notificacion.setOnClickListener(onClickNotificaciones);
        imv_notificacion.setOnClickListener(onClickNotificaciones);
        tv_notificacion.setOnClickListener(onClickNotificaciones);

        cv_buzon= getView().findViewById(R.id.cv_buzon);
        ll_buzon= getView().findViewById(R.id.ll_buzon);
        imv_buzon= getView().findViewById(R.id.imv_buzon);
        tv_buzon= getView().findViewById(R.id.tv_buzon);

        cv_buzon.setOnClickListener(onClickBuzon);
        ll_buzon.setOnClickListener(onClickBuzon);
        imv_buzon.setOnClickListener(onClickBuzon);
        tv_buzon.setOnClickListener(onClickBuzon);

        tv_registra_cuenta = getView().findViewById(R.id.tv_registra_cuenta);
        tv_registra_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCoDi(true, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        QPAY_Register register = new QPAY_Register();
                        register.setFolio("-");
                        register.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                        register.setRegisterType(Globals.REGISTER_TYPE_FC_CODI);

                        RegisterActivity.createRegister(getContext(), register, new IFunction<QPAY_BaseResponse>() {
                            @Override
                            public void execute(QPAY_BaseResponse... data) {

                            }
                        });

                    }
                });
            }
        });

    }


    private void  iniciarCoDi(boolean reset, IFunction function) {
        //VERIFICAR
        getContextMenu().sethCoDi(new HCoDi((MenuActivity) getContext()));

        if(!AppPreferences.getCodiClabe().isEmpty() && !reset) {

            getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    function.execute();
                }
            });

            return;
        }
        getContext().alert("CoDi", "Usar códigos QR para pagar y cobrar\nen establecimientos o para solicitar\ndinero a otras personas.", new IAlertButton() {
            @Override
            public String onText() {
                return "Registrar";
            }

            @Override
            public void onClick() {

                getContext().loading(true);

                try {
                    HDatabase db = new HDatabase(getContext());
                    db.deleteAll(NotificationDTO.class);
                } catch (Exception ex) {

                }

                getContextMenu().gethCoDi().registroInicial(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().loading(false);
                        getContextMenu().gethCoDi().confirmSMS(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getContext().loading(true);
                                getContextMenu().gethCoDi().crearKeysource((String) data[0], new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().gethCoDi().guardarKeysource(new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {
                                                getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {

                                                        getContextMenu().gethCoDi().registroSubsecuente(new IFunction() {
                                                            @Override
                                                            public void execute(Object[] data) {

                                                                getContext().loading(false);
                                                                getContextMenu().gethCoDi().selectCuenta(new IFunction<DHCuenta>() {
                                                                    @Override
                                                                    public void execute(DHCuenta[] cuentas) {
                                                                        getContext().loading(true);

                                                                        getContextMenu().gethCoDi().validacionCuenta(cuentas[0], new IFunction() {
                                                                            @Override
                                                                            public void execute(Object[] data) {
                                                                                FCValidacionCuentaResponse respuesta = (FCValidacionCuentaResponse) data[0];
                                                                                AppPreferences.setCodiRastreo(respuesta.getClaveRastreo());
                                                                                AppPreferences.setCodiClabe(cuentas[0].getClabeCuenta());
                                                                                AppPreferences.setCuentaCodi(cuentas[0]);
                                                                                HCoDi.cuentaSeleccionada = cuentas[0];
                                                                                getContext().loading(false);

                                                                                getContext().alert("CoDi", "La validación de la cuenta tardará unos minutos. Recibirás una notificación por parte de Banxico", new IAlertButton() {
                                                                                    @Override
                                                                                    public String onText() {
                                                                                        return "Aceptar";
                                                                                    }

                                                                                    @Override
                                                                                    public void onClick() {
                                                                                        function.execute();
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
                                });
                            }
                        });
                    }
                });

            }
        }, new IAlertButton() {
            @Override
            public String onText() {
                return "Cancelar";
            }

            @Override
            public void onClick() {

            }
        });


    }

    private void quieroCobrar() {

        if(!AppPreferences.getCodiRegister()){
            getContext().loading(true);

            getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                @Override
                public void execute(Object[] data) {

                    getContextMenu().gethCoDi().statusValidacionCuenta(new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            FCRespuesta respuesta = (FCRespuesta) data[0];
                            getContext().loading(false);

                            if(respuesta.getCodigo() == 1) {
                                AppPreferences.setCodiRegister(true);
                                getContext().setFragment(Fragment_fincomun_cobro_solicitud.newInstance());
                            } else
                                getContext().alert("Fincomún", respuesta.getDescripcion().get(0));

                        }
                    });
                }
            });
        } else {
            getContext().setFragment(Fragment_fincomun_cobro_solicitud.newInstance());
        }

    }

}
