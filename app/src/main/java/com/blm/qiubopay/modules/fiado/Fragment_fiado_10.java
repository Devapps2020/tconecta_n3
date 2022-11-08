package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.fiado.IFiar;
import com.blm.qiubopay.listeners.fiado.IUpdateClient;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.PQPrefijo;
import com.blm.qiubopay.models.fiado.QPAY_Fiar_Request;
import com.blm.qiubopay.models.fiado.QPAY_Fiar_Response;
import com.blm.qiubopay.models.fiado.QPAY_Update_Cliente_Request;
import com.blm.qiubopay.models.fiado.QPAY_Update_Cliente_Response;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_10 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    public static ArrayList<PQPrefijo> list;

    private ArrayList<CViewEditText> campos;
    private ArrayList<CViewEditText> campos_perfil;

    public static boolean isNew;
    private Button btn_confirm;
    private Button text_cancell;
    private Button btn_regla;

    private boolean limitarMonto;
    private boolean limitarTiempo;
    private boolean cobrarInteres;

    private RadioButton radLimitarMonto;
    private RadioButton radLimitarTiempo;
    private RadioButton radCobrarInteres;

    public static Fragment_fiado_10 newInstance(Object... data) {
        Fragment_fiado_10 fragment = new Fragment_fiado_10();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_fiado_10, container, false);

        initFragment();

        return view;
    }

    public void initFragment() {

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        Logger.d(new Gson().toJson(Fragment_fiado_3.selection));
        campos = new ArrayList();

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };
        campos_perfil = new ArrayList();

        text_cancell = view.findViewById(R.id.text_cancell);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        LinearLayout layout_detalle_perfil = view.findViewById(R.id.layout_detalle_perfil);
        LinearLayout layout_detalle_tab = view.findViewById(R.id.layout_detalle_tab);

        ImageView tab_reglas_perfil = view.findViewById(R.id.tab_reglas_perfil);
        TextView text_label_2 = view.findViewById(R.id.text_label_2);
        TextView txt_fiar_nombre = view.findViewById(R.id.txt_fiar_nombre);

        txt_fiar_nombre.setText(Html.fromHtml("Fiar a <br><b>" + Fragment_fiado_3.selection.getFiado_name() + "</b>"));
        text_label_2.setText("Reglas de fiado\npara " + Fragment_fiado_3.selection.getFiado_name());

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_mount_trust))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(25)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_fiado_17)
                .setSuffix("MXN")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_detail))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setHint(R.string.text_fiado_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos_perfil.add(CViewEditText.create(view.findViewById(R.id.edit_monto_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(11)
                .setHint("")
                .setSuffix("MXN")
                .setType(CViewEditText.TYPE.CURRENCY)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validatePerfil();
                    }
                }));

        campos_perfil.add(CViewEditText.create(view.findViewById(R.id.edit_dias_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(3)
                .setHint("")
                .setSuffix("Dias")
                .setType(CViewEditText.TYPE.NUMBER)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validatePerfil();
                    }
                }));

        campos_perfil.add(CViewEditText.create(view.findViewById(R.id.edit_interes_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(3)
                .setHint("")
                .setSuffix("%")
                .setType(CViewEditText.TYPE.NUMBER)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validatePerfil();
                    }
                }));


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiar(context, campos.get(0).getTextDouble(), campos.get(1).getText(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Fiado Exitoso", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Listo";
                            }

                            @Override
                            public void onClick() {
                                isNew = true;
                                context.setFragment(Fragment_fiado_5.newInstance(),true);
                            }
                        });
                    }
                });
            }
        });

        btn_regla = view.findViewById(R.id.btn_regla);
        btn_regla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QPAY_Update_Cliente_Request request = new QPAY_Update_Cliente_Request();
                request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                request.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());

                if(limitarMonto) {
                    if(!campos_perfil.get(0).getText().isEmpty())
                        request.setMaxAmount(campos_perfil.get(0).getTextDouble());
                    else
                        request.setMaxAmount("0");
                }

                if(limitarTiempo) {
                    if(!campos_perfil.get(1).getText().isEmpty())
                        request.setFiado_debt_days(campos_perfil.get(1).getTextDouble());
                    else
                        request.setFiado_debt_days("0");
                }

                if(cobrarInteres) {
                    if(!campos_perfil.get(2).getText().isEmpty())
                        request.setFiado_debt_interest(campos_perfil.get(2).getTextDouble());
                    else
                        request.setFiado_debt_interest("0");
                }

                if(request.getFiado_debt_interest() != null && !request.getFiado_debt_interest().isEmpty()) {

                    Double interes = Double.parseDouble(request.getFiado_debt_interest());

                    if(interes > 0.0 && interes < 1.0) {
                        context.alert("El interés debe ser mayor o igual a 1%");
                        return;
                    }
                }

                if(request.getMaxAmount() != null && !request.getMaxAmount().isEmpty()) {

                    Double monto = Double.parseDouble(request.getMaxAmount());

                    if(monto > 0.0 && monto < 1.0) {
                        context.alert("El monto debe ser mayor o igual a $1.00");
                        return;
                    }
                }

                updateClient(context, request, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        Fragment_fiado_3.selection.setInterest(request.getFiado_debt_interest());
                        Fragment_fiado_3.selection.setMaxAmount(request.getMaxAmount());
                        Fragment_fiado_3.selection.setPaymentDays(request.getFiado_debt_days());

                        layout_detalle_perfil.setVisibility(View.GONE);
                        layout_detalle_tab.setVisibility(View.VISIBLE);
                    }
                }, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                    }
                });
            }
        });

        text_label_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_reglas_perfil.performClick();
            }
        });

        text_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setFragment(Fragment_fiado_1.newInstance());
            }
        });

        tab_reglas_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_detalle_perfil.getVisibility() == View.VISIBLE) {
                    layout_detalle_tab.setVisibility(View.VISIBLE);
                    layout_detalle_perfil.setVisibility(View.GONE);
                } else {
                    layout_detalle_tab.setVisibility(View.GONE);
                    layout_detalle_perfil.setVisibility(View.VISIBLE);
                }

            }
        });

        radLimitarMonto = view.findViewById(R.id.radLimitarMonto);
        radLimitarMonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (limitarMonto) {
                    limitarMonto = false;
                    campos_perfil.get(0).setText("");
                } else
                    limitarMonto = true;

                campos_perfil.get(0).setEnabled(limitarMonto);
                radLimitarMonto.setChecked(limitarMonto);
                validatePerfil();
            }
        });

        radLimitarTiempo = view.findViewById(R.id.radLimitarTiempo);
        radLimitarTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (limitarTiempo) {
                    limitarTiempo = false;
                    campos_perfil.get(1).setText("");
                } else
                    limitarTiempo = true;

                campos_perfil.get(1).setEnabled(limitarTiempo);
                radLimitarTiempo.setChecked(limitarTiempo);
                validatePerfil();
            }
        });

        radCobrarInteres = view.findViewById(R.id.radCobrarInteres);
        radCobrarInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cobrarInteres) {
                    cobrarInteres = false;
                    campos_perfil.get(2).setText("");
                } else
                    cobrarInteres = true;

                campos_perfil.get(2).setEnabled(cobrarInteres);
                radCobrarInteres.setChecked(cobrarInteres);
                validatePerfil();
            }
        });

        if(Fragment_fiado_3.selection.getMaxAmount() != null &&  !Fragment_fiado_3.selection.getMaxAmount().isEmpty()) {
            campos_perfil.get(0).setTextMonto(Fragment_fiado_3.selection.getMaxAmount());
            radLimitarMonto.setChecked(limitarMonto = true);
        } else
            campos_perfil.get(0).setEnabled(limitarMonto);

        if(Fragment_fiado_3.selection.getPaymentDays() != null &&  !Fragment_fiado_3.selection.getPaymentDays().isEmpty()) {
            radLimitarTiempo.setChecked(limitarTiempo = true);
            campos_perfil.get(1).setText(Fragment_fiado_3.selection.getPaymentDays());
        } else
            campos_perfil.get(1).setEnabled(limitarTiempo);

        if(Fragment_fiado_3.selection.getInterest() != null &&  !Fragment_fiado_3.selection.getInterest().isEmpty()) {
            radCobrarInteres.setChecked(cobrarInteres = true);
            campos_perfil.get(2).setText(Fragment_fiado_3.selection.getInterest());
        } else
            campos_perfil.get(2).setEnabled(cobrarInteres);

        if(isNew) {
            tab_reglas_perfil.performClick();
        }

    }

    private void validate() {

        btn_confirm.setEnabled(false);
        //text_cancell.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        //text_cancell.setEnabled(true);
        btn_confirm.setEnabled(true);
    }

    private void validatePerfil() {

        btn_regla.setEnabled(false);

        for(int i=0; i<campos_perfil.size(); i++)
            if(!campos_perfil.get(i).isValid())
                return;

        btn_regla.setEnabled(true);
    }

    public static void fiar(HActivity context, String monto, String detalle, final IFunction function) {

        Double dmonto = Double.parseDouble(monto);

        if(dmonto > 0.0 && dmonto < 1.0) {
            context.alert("El monto debe ser mayor o igual a $1.00");
            return;
        }

        context.loading(true);

        QPAY_Fiar_Request data = new QPAY_Fiar_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_debt_amount(monto);
        data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());
        data.setFiado_debt_detail(detalle);

        IFiar service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.d(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Fiar_Response fiar_response = gson.fromJson(json, QPAY_Fiar_Response.class);

                        if (fiar_response.getQpay_response().equals("true")) {

                            //Fragment_fiado_3.reload.execute();

                            if(function != null)
                                function.execute();

                        } else {

                            if (fiar_response.getQpay_code().equals("017")
                                    || fiar_response.getQpay_code().equals("018")
                                    || fiar_response.getQpay_code().equals("019")
                                    || fiar_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if (fiar_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                // context.alertAEONBlockedUser();
                            } else {
                                context.alert(fiar_response.getQpay_description());
                            }


                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        service.fiar(data);

    }

    public static void updateClient(HActivity context, QPAY_Update_Cliente_Request data, final IFunction function, final IFunction error) {

        context.loading(true);

        IUpdateClient service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    Logger.d(new Gson().toJson(result));

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Update_Cliente_Response update_cliente_response = gson.fromJson(json, QPAY_Update_Cliente_Response.class);

                        if (update_cliente_response.getQpay_response().equals("true")) {

                            if(Fragment_fiado_3.reload != null)
                                Fragment_fiado_3.reload.execute();

                            if(function != null)
                                function.execute();

                        } else {

                            if (update_cliente_response.getQpay_code().equals("017")
                                    || update_cliente_response.getQpay_code().equals("018")
                                    || update_cliente_response.getQpay_code().equals("019")
                                    || update_cliente_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });
                            } else if (update_cliente_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                // context.alertAEONBlockedUser();
                            } else {
                                context.alert(update_cliente_response.getQpay_description());
                            }


                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        service.updateClient(data);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

