package com.blm.qiubopay.modules.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.database.notification.MyOpenHelper;
import com.blm.qiubopay.listeners.IGetRollProducts;
import com.blm.qiubopay.models.rolls.QPAY_RollsCostResponse;
import com.blm.qiubopay.modules.Fragment_compra_material_1;
import com.blm.qiubopay.modules.Fragment_compra_rollos_1;
import com.blm.qiubopay.modules.Fragment_multi_patron_1;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_ticket;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewOption;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetDynamicQuestions;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.questions.QPAY_CampaignRequest;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestionsResponse;
import com.blm.qiubopay.modules.Fragment_acerca_de;
import com.blm.qiubopay.modules.Fragment_info_negocio_1;
import com.blm.qiubopay.modules.Fragment_info_personal_1;
import com.blm.qiubopay.modules.Fragment_multi_1;
import com.blm.qiubopay.modules.Fragment_multi_cajero_1;
import com.blm.qiubopay.modules.Fragment_notificaciones_1;
import com.blm.qiubopay.modules.campania.Fragment_preguntas;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.registro.Fragment_registro_pin;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.GsonBuilder;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_cuenta_1 extends HFragment implements IMenuContext {

    private Object data;
    private Uri uri;
    IFunction functionMulti;
    private final String MENU_MULTI = "Cajero / Patrón";
    private final String MENU_MULTI_PATRON = "Mis cajeros";
    private final String MENU_MULTI_CAJERO = "Mi Patrón";

    private TextView nombre_usuario;

    public static Fragment_cuenta_1 newInstance(Object... data) {
        Fragment_cuenta_1 fragment = new Fragment_cuenta_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_perfil_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_perfil_1"), Object.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().initHome();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_inician);

        nombre_usuario = getView().findViewById(R.id.nombre_usuario);
        nombre_usuario.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name());

        ImageView notification = getView().findViewById(R.id.notification);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_notificaciones_1.newInstance());
            }
        });


        //20210704 RSB. Pendings. Número de notificaciones en cuenta
        //getContextMenu().setNotificacions(getView().findViewById(R.id.no_notificaciones));
        TextView tvNoNotificaciones = getView().findViewById(R.id.no_notificaciones);
        Fragment_notificaciones_1.iFunctionUpdate = new IFunction() {
            @Override
            public void execute(Object[] data) {
                MyOpenHelper db =new MyOpenHelper(getContext());

                int num = db.countNotifications();

                if(num > 0){
                    tvNoNotificaciones.setText(num + "");
                    tvNoNotificaciones.setVisibility(View.VISIBLE);
                }else{
                    tvNoNotificaciones.setVisibility(View.GONE);
                }
            }
        };
        Fragment_notificaciones_1.iFunctionUpdate.execute();

        TextView btn_notificaciones = getView().findViewById(R.id.btn_notificaciones);
        btn_notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_notificaciones_1.newInstance());
            }
        });

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().onBackPressed();
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_personal)).setText(R.string.text_option_1).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "PERSONAL"));
                getContext().setFragment(Fragment_info_personal_1.newInstance());
            }
        });

        //210518 RSB Acuerdos homologacion. Ocultar informacion negocio para cajero
        CViewOption.create(getView().findViewById(R.id.layout_negocio)).setText(R.string.text_option_2).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "NEGOCIO"));
                getContext().setFragment(Fragment_info_negocio_1.newInstance());
            }
        }).setVisible(!AppPreferences.isCashier());

        CViewOption.create(getView().findViewById(R.id.layout_restaurar_pin)).setText(R.string.text_option_4).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                getContext().alert(getContext().getResources().getString(R.string.alert_message_restore_pin_question), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {

                        getContextMenu().authPIN(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getContext().backFragment();

                                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "CAMBIAR PIN"));
                                CApplication.setAnalytics(CApplication.ACTION.CB_CAMBIAR_PIN_inician);
                                getContext().setFragment(Fragment_registro_pin.newInstance());
                            }
                        }, true);

                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });

            }
        });

        validarPermisosUsuario();

        CViewOption.create(getView().findViewById(R.id.layout_compra_dongle)).setText(R.string.text_option_11).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "COMPRA DE ROLLOS"));
                getRollProducts();
            }
        }).setVisible(!AppPreferences.isCashier());

        CViewOption.create(getView().findViewById(R.id.layout_desvincular_dongle)).setText(R.string.text_option_3).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                getContext().alert(getContext().getResources().getString(R.string.alert_message_eliminate_device_question), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "CAMBIAR PIN"));
                        CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "DESVINCULAR LECTOR DE TARJETAS"));
                        AppPreferences.deleteDevice();
                        v.setVisibility(View.GONE);
                        getContextMenu().disConnect();
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });

            }
        }).setVisible(AppPreferences.isDeviceRegistered());

        CViewOption.create(getView().findViewById(R.id.layout_compra_material)).setText(R.string.text_option_12).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "COMPRA DE MATERIAL"));
                getContext().setFragment(Fragment_compra_material_1.newInstance());
                //getRollProducts();
            }
        }).setVisible(!AppPreferences.isCashier());

        CViewOption.create(getView().findViewById(R.id.layout_acerca)).setText(R.string.text_option_7).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "ACERCA DE"));
                getContext().setFragment(Fragment_acerca_de.newInstance());
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_faq)).setText(R.string.text_option_8).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "PREGUNTAS FRECUENTES"));
                getContext().setFragment(Fragment_browser.newInstance(Globals.URL_FAQS));
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_encuentas)).setText(R.string.text_option_10).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                dynamicQuestions(Globals.PROFILE_QUESTIONS, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().backFragment();
                    }
                });
            }
        }).setVisible(false);

        CViewOption.create(getView().findViewById(R.id.layout_cerrar_session)).setText(R.string.text_option_9).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                getContext().alert(getContext().getResources().getString(R.string.message_logout_question), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {

                        CApplication.generateNewFCMToken();

                        CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "CERRAR SESION"));
                        AppPreferences.Logout(getContext());
                        getContext().startActivity(LoginActivity.class, true);
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });

            }
        }).setIcon(R.drawable.icons_log_out);

    }

    private void validarPermisosUsuario() {

        String name = nombre_usuario.getText().toString();

        String rol = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_user_type() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_user_type() : Globals.ROL_NORMAL;

        //20210703 RSB. Pendings. Ocultar Cajero/Patron hasta nueva version, por ahora solo para usuarios Normales para no inhabilitar a los que ya tienen la funcionalidad.
        // (Not Visible layout_multi)
        switch (rol) {
            case Globals.ROL_NORMAL:
                nombre_usuario.setText(Html.fromHtml("<b>" + name + "</b>"));
                CViewOption.create(getView().findViewById(R.id.layout_multi)).setText(MENU_MULTI).onClick(new CViewOption.IClick() {
                    @Override
                    public void onClick(View view) {
                        CApplication.setAnalytics(CApplication.ACTION.CB_MULTIUSUARIO_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + CApplication.ACTION.MENU_MULTIUSUARIO.name()));
                        getContext().setFragment(Fragment_multi_1.newInstance());
                    }
                }).setVisible(false);
                break;
            case Globals.ROL_PATRON:
                nombre_usuario.setText(Html.fromHtml("<b>" + name + "</b><br>Patrón"));
                CViewOption.create(getView().findViewById(R.id.layout_multi)).setText(MENU_MULTI_PATRON).onClick(new CViewOption.IClick() {
                    @Override
                    public void onClick(View view) {
                        CApplication.setAnalytics(CApplication.ACTION.CB_MULTIUSUARIO_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + CApplication.ACTION.PATRON.name()));
                        getContext().setFragment(Fragment_multi_cajero_1.newInstance());
                    }
                });
                break;
            case Globals.ROL_CAJERO:
                nombre_usuario.setText(Html.fromHtml("<b>" + name + "</b><br>Cajero"));
                CViewOption.create(getView().findViewById(R.id.layout_multi)).setText(MENU_MULTI_CAJERO).onClick(new CViewOption.IClick() {
                    @Override
                    public void onClick(View view) {
                        CApplication.setAnalytics(CApplication.ACTION.CB_MULTIUSUARIO_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + CApplication.ACTION.CAJERO.name()));
                        getContext().setFragment(Fragment_multi_patron_1.newInstance());
                    }
                });
                break;
        }

    }

    public void dynamicQuestions(String tag, IFunction function) {

        getContext().loading(true);

        try {

            QPAY_CampaignRequest qpay_seed = new QPAY_CampaignRequest();
            qpay_seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            qpay_seed.setType(tag);
            qpay_seed.setClient_channel_id("1");

            IGetDynamicQuestions questions = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result);

                    Log.e("response", "" + gson);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        QPAY_DynamicQuestionsResponse response_txn = new Gson().fromJson(gson, QPAY_DynamicQuestionsResponse.class);

                        if("000".equals(response_txn.getQpay_code())) {
                            Fragment_preguntas.setContinue(function);
                            Fragment_preguntas.setData(response_txn.getQpay_object()[0]);
                            getContext().setFragment(Fragment_preguntas.newInstance(), true);
                        } else {
                            getContext().alert(response_txn.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(qpay_seed));

            questions.getDynamicQuestions(qpay_seed);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    private void getRollProducts(){

        getContext().loading(true);

        try {

            IGetRollProducts petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_RollsCostResponse.QPAY_RollsCostResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_RollsCostResponse response = gson.fromJson(json, QPAY_RollsCostResponse.class);

                        if(response.getQpay_response().equals("true")){
                            getContext().setFragment(Fragment_compra_rollos_1.newInstance(response));
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(),response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getRollProduts();

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}

