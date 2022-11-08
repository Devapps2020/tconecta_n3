package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.blm.qiubopay.listeners.fiado.IFiado;
import com.blm.qiubopay.listeners.fiado.IListDebts;
import com.blm.qiubopay.listeners.fiado.IRecordatorio;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.fiado.QPAY_Fiado;
import com.blm.qiubopay.models.fiado.QPAY_Fiado_Request;
import com.blm.qiubopay.models.fiado.QPAY_Fiado_Response;
import com.blm.qiubopay.models.fiado.QPAY_List_Debts_Request;
import com.blm.qiubopay.models.fiado.QPAY_List_Debts_Response;
import com.blm.qiubopay.models.fiado.QPAY_Recordatorio_Request;
import com.blm.qiubopay.models.fiado.QPAY_Recordatorio_Response;
import com.blm.qiubopay.models.fiado.QPAY_Update_Cliente_Request;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_5 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;

    private ArrayList<CViewEditText> campos;
    private ArrayList<CViewEditText> campos_reglas;
    private ArrayList<CViewEditText> campos_detalle;
    private ArrayList<CViewEditText> campos_perfil;

    private Button btn_confirm;
    private Button btn_regla;
    private Button btn_pago;
    private Button btn_recordatorio;
    private Button btn_perfil;

    private boolean limitarMonto;
    private boolean limitarTiempo;
    private boolean cobrarInteres;

    private RadioButton radLimitarMonto;
    private RadioButton radLimitarTiempo;
    private RadioButton radCobrarInteres;
    private Button text_cancell;

    private AdeudosAdapter adapter;
    public static String deudaTotal;
    public LinearLayout layout_detalle_perfil;
    public LinearLayout layout_detalle_tab;

    public TextView text_label_2;
    LinearLayout ln_detail;

    Double total = 0.0;

    public static Fragment_fiado_5 newInstance(Object... data) {
        Fragment_fiado_5 fragment = new Fragment_fiado_5();
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

        view = inflater.inflate(R.layout.fragment_fiado_5, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        Logger.d(new Gson().toJson(Fragment_fiado_3.selection));

        text_label_2 = view.findViewById(R.id.text_label_2);
        TextView txt_fiar_nombre = view.findViewById(R.id.txt_fiar_nombre);

        txt_fiar_nombre.setText(Fragment_fiado_3.selection.getFiado_name());
        text_label_2.setText("Reglas de fiado\npara " + Fragment_fiado_3.selection.getFiado_name());

        LinearLayout ln_trust = view.findViewById(R.id.ln_trust);
        ln_detail = view.findViewById(R.id.ln_detail);
        LinearLayout ln_profile = view.findViewById(R.id.ln_profile);

        LinearLayout ln_tab1 = view.findViewById(R.id.ln_tab1);
        LinearLayout ln_tab2 = view.findViewById(R.id.ln_tab2);
        LinearLayout ln_tab3 = view.findViewById(R.id.ln_tab3);

        ln_trust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(layout_detalle_perfil.getVisibility() == View.VISIBLE)
                    return;

                ln_tab1.setVisibility(View.VISIBLE);
                ln_tab2.setVisibility(View.GONE);
                ln_tab3.setVisibility(View.GONE);

                ln_trust.setBackgroundColor(context.getResources().getColor(R.color.colorGrayLight));
                ln_detail.setBackgroundColor(Color.WHITE);
                ln_profile.setBackgroundColor(Color.WHITE);

                campos.get(0).setText("");
                campos.get(1).setText("");

            }
        });

        ln_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {

                if(layout_detalle_perfil.getVisibility() == View.VISIBLE)
                    return;

                getAdeudos(new IFunction<QPAY_List_Debts_Response>() {
                    @Override
                    public void execute(QPAY_List_Debts_Response... data) {

                        ln_tab1.setVisibility(View.GONE);
                        ln_tab2.setVisibility(View.VISIBLE);
                        ln_tab3.setVisibility(View.GONE);

                        ln_trust.setBackgroundColor(Color.WHITE);
                        ln_detail.setBackgroundColor(context.getResources().getColor(R.color.colorGrayLight));
                        ln_profile.setBackgroundColor(Color.WHITE);

                        ListView list_pedidos = view.findViewById(R.id.list_adeudos);
                        adapter = new AdeudosAdapter(context, Arrays.asList(data[0].getQpay_object()));
                        list_pedidos.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Utils.setListViewHeightBasedOnChildren(list_pedidos);

                        total = 0.0;

                        for(QPAY_Fiado fiado : data[0].getQpay_object()) {
                            total += Double.parseDouble(fiado.getFiado_debt_amount());
                        }

                        tabDetalle(total);

                        deudaTotal = campos_detalle.get(0).getTextDouble();

                        list_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                Fragment_fiado_6.fiado = adapter.getItem(position);
                                context.setFragment(Fragment_fiado_6.newInstance());
                            }
                        });

                    }
                });

            }
        });

        ln_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(layout_detalle_perfil.getVisibility() == View.VISIBLE)
                    return;

                ln_tab1.setVisibility(View.GONE);
                ln_tab2.setVisibility(View.GONE);
                ln_tab3.setVisibility(View.VISIBLE);

                ln_trust.setBackgroundColor(Color.WHITE);
                ln_detail.setBackgroundColor(Color.WHITE);
                ln_profile.setBackgroundColor(context.getResources().getColor(R.color.colorGrayLight));

            }
        });

        tabReglas();
        tabFiar();
        tabPerfil();

        if(Fragment_fiado_10.isNew) {
            ln_detail.performClick();
            Fragment_fiado_10.isNew = false;
        }

    }

    public void tabReglas() {

        campos_reglas = new ArrayList<>();

        layout_detalle_perfil = view.findViewById(R.id.layout_detalle_perfil);
        layout_detalle_tab = view.findViewById(R.id.layout_detalle_tab);

        ImageView tab_reglas_perfil = view.findViewById(R.id.tab_reglas_perfil);
        Button text_cancel = view.findViewById(R.id.text_cancel);

        btn_regla = view.findViewById(R.id.btn_regla);
        btn_regla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QPAY_Update_Cliente_Request data = new QPAY_Update_Cliente_Request();
                data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());

                if(limitarMonto) {
                    if(!campos_reglas.get(0).getText().isEmpty())
                        data.setMaxAmount(campos_reglas.get(0).getTextDouble());
                    else
                        data.setMaxAmount("0");
                }

                if(limitarTiempo) {
                    if(!campos_reglas.get(1).getText().isEmpty())
                        data.setFiado_debt_days(campos_reglas.get(1).getTextDouble());
                    else
                        data.setFiado_debt_days("0");
                }

                if(cobrarInteres) {
                    if(!campos_reglas.get(2).getText().isEmpty())
                        data.setFiado_debt_interest(campos_reglas.get(2).getTextDouble());
                    else
                        data.setFiado_debt_interest("0");
                }

                if(data.getFiado_debt_interest() != null && !data.getFiado_debt_interest().isEmpty()) {

                    Double interes = Double.parseDouble(data.getFiado_debt_interest());

                    if(interes > 0.0 && interes < 1.0) {
                        context.alert("El interés debe ser mayor o igual a 1%");
                        return;
                    }
                }

                if(data.getMaxAmount() != null && !data.getMaxAmount().isEmpty()) {

                    Double monto = Double.parseDouble(data.getMaxAmount());

                    if(monto > 0.0 && monto < 1.0) {
                        context.alert("El monto debe ser mayor o igual a $1.00");
                        return;
                    }
                }

                Fragment_fiado_10.updateClient(context, data, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
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

        tab_reglas_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_detalle_perfil.getVisibility() == View.VISIBLE) {
                    layout_detalle_tab.setVisibility(View.VISIBLE);
                    layout_detalle_perfil.setVisibility(View.GONE);
                } else {
                    layout_detalle_perfil.setVisibility(View.VISIBLE);
                    layout_detalle_tab.setVisibility(View.GONE);
                }

            }
        });

        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_detalle_tab.setVisibility(View.VISIBLE);
                layout_detalle_perfil.setVisibility(View.GONE);
            }
        });

        campos_reglas.add(CViewEditText.create(view.findViewById(R.id.edit_monto_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(11)
                .setHint("")
                .setSuffix("MXN")
                .setType(CViewEditText.TYPE.CURRENCY)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validateReglas();
                    }
                }));

        campos_reglas.add(CViewEditText.create(view.findViewById(R.id.edit_dias_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(3)
                .setSuffix("Días")
                .setHint("")
                .setType(CViewEditText.TYPE.NUMBER)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validateReglas();
                    }
                }));

        campos_reglas.add(CViewEditText.create(view.findViewById(R.id.edit_interes_perfil))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(3)
                .setHint("")
                .setSuffix("%")
                .setType(CViewEditText.TYPE.NUMBER)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validateReglas();
                    }
                }));

        radLimitarMonto = view.findViewById(R.id.radLimitarMonto);
        radLimitarMonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (limitarMonto) {
                    limitarMonto = false;
                    campos_reglas.get(0).setText("");
                } else
                    limitarMonto = true;

                campos_reglas.get(0).setEnabled(limitarMonto);
                radLimitarMonto.setChecked(limitarMonto);
                validateReglas();
            }
        });

        radLimitarTiempo = view.findViewById(R.id.radLimitarTiempo);
        radLimitarTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (limitarTiempo) {
                    limitarTiempo = false;
                    campos_reglas.get(1).setText("");
                } else
                    limitarTiempo = true;

                campos_reglas.get(1).setEnabled(limitarTiempo);
                radLimitarTiempo.setChecked(limitarTiempo);
                validateReglas();
            }
        });

        radCobrarInteres = view.findViewById(R.id.radCobrarInteres);
        radCobrarInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cobrarInteres) {
                    cobrarInteres = false;
                    campos_reglas.get(2).setText("");
                } else
                    cobrarInteres = true;

                campos_reglas.get(2).setEnabled(cobrarInteres);
                radCobrarInteres.setChecked(cobrarInteres);
                validateReglas();
            }
        });

        if(Fragment_fiado_3.selection.getMaxAmount() != null &&  !Fragment_fiado_3.selection.getMaxAmount().isEmpty()) {
            campos_reglas.get(0).setTextMonto(Fragment_fiado_3.selection.getMaxAmount());
            radLimitarMonto.setChecked(limitarMonto = true);
        } else
            campos_reglas.get(0).setEnabled(limitarMonto);

        if(Fragment_fiado_3.selection.getPaymentDays() != null &&  !Fragment_fiado_3.selection.getPaymentDays().isEmpty()) {
            radLimitarTiempo.setChecked(limitarTiempo = true);
            campos_reglas.get(1).setText(Fragment_fiado_3.selection.getPaymentDays());
        } else
            campos_reglas.get(1).setEnabled(limitarTiempo);

        if(Fragment_fiado_3.selection.getInterest() != null &&  !Fragment_fiado_3.selection.getInterest().isEmpty()) {
            radCobrarInteres.setChecked(cobrarInteres = true);
            campos_reglas.get(2).setText(Fragment_fiado_3.selection.getInterest());
        } else
            campos_reglas.get(2).setEnabled(cobrarInteres);

    }

    public void tabFiar() {

        campos = new ArrayList<>();

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_mount_trust))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(25)
                .setHint("")
                .setSuffix("MXN")
                .setText("")
                .setType(CViewEditText.TYPE.CURRENCY)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validate();
                    }
                }));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_detail))
                .setRequired(true)
                .setMinimum(2)
                .setMaximum(50)
                .setHint("")
                .setText("")
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validate();
                    }
                }));

        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_fiado_10.fiar(context, campos.get(0).getTextDouble(), campos.get(1).getText(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        campos.get(0).setText("");
                        campos.get(1).setText("");
                        context.alert("Fiado Exitoso", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Listo";
                            }

                            @Override
                            public void onClick() {
                                ln_detail.performClick();

                            }
                        });
                    }
                });
            }
        });

        text_cancell = view.findViewById(R.id.text_cancell);
        text_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.backFragment();
            }
        });

    }

    public void tabDetalle(Double deuda) {

        btn_pago = view.findViewById(R.id.btn_partial_payment);
        btn_recordatorio = view.findViewById(R.id.btn_send_remender);

        campos_detalle = new ArrayList<>();

        campos_detalle.add(CViewEditText.create(view.findViewById(R.id.edit_total_debt))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(11)
                .setSuffix("MXN")
                .setHint("")
                .setType(CViewEditText.TYPE.NONE)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                    }
                }).setEnabled(false).setText(Utils.paserCurrency(deuda + "")));

        btn_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_fiado_7.deuda = deuda + "";
                context.setFragment(Fragment_fiado_7.newInstance());
            }
        });

        btn_recordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordatorio(context, null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Recordatorio\nEnviado", "Listo");
                    }
                });
            }
        });

        validateDetalle();

        btn_pago.setEnabled(deuda > 0);
        btn_recordatorio.setEnabled(deuda > 0);

    }

    public void tabPerfil() {

        btn_perfil = view.findViewById(R.id.btn_save_changes);

        campos_perfil = new ArrayList<>();

        campos_perfil.add(CViewEditText.create(view.findViewById(R.id.edit_email))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint("Email")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validatePerfil();
                    }
                }));

        campos_perfil.add(CViewEditText.create(view.findViewById(R.id.edit_phone))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setAlert(R.string.text_input_required)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint("Teléfono celular")
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validatePerfil();
                    }
                }));

        campos_perfil.get(0).setText(Fragment_fiado_3.selection.getFiado_mail());
        campos_perfil.get(1).setText(Fragment_fiado_3.selection.getFiado_cellphone());

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QPAY_Update_Cliente_Request data = new QPAY_Update_Cliente_Request();
                data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());
                data.setFiado_new_mail( campos_perfil.get(0).getText());
                data.setFiado_new_cellphone( campos_perfil.get(1).getText());

                Fragment_fiado_10.updateClient(context, data, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Datos actualizados");
                        Fragment_fiado_3.selection.setFiado_mail(campos_perfil.get(0).getText());
                        Fragment_fiado_3.selection.setFiado_cellphone(campos_perfil.get(1).getText());
                    }
                }, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        campos_perfil.get(0).setText(Fragment_fiado_3.selection.getFiado_mail());
                        campos_perfil.get(1).setText(Fragment_fiado_3.selection.getFiado_cellphone());
                    }
                });

            }
        });

        validatePerfil();

    }

    private void validate() {

        btn_confirm.setEnabled(false);
        text_cancell.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        text_cancell.setEnabled(true);
        btn_confirm.setEnabled(true);

    }

    private void validateReglas() {

        btn_regla.setEnabled(false);

        if(limitarMonto && !campos_reglas.get(0).isValid())
            return;

        if(limitarTiempo && !campos_reglas.get(1).isValid())
            return;

        if(cobrarInteres && !campos_reglas.get(2).isValid())
            return;

        btn_regla.setEnabled(true);
    }

    private void validateDetalle() {

        btn_pago.setEnabled(false);
        btn_recordatorio.setEnabled(false);

        for(int i=0; i<campos_detalle.size(); i++)
            if(!campos_detalle.get(i).isValid())
                return;

        btn_pago.setEnabled(true);
        btn_recordatorio.setEnabled(true);
    }

    private void validatePerfil() {

        btn_perfil.setEnabled(false);

        for(int i=0; i<campos_perfil.size(); i++)
            if(!campos_perfil.get(i).isValid())
                return;

        btn_perfil.setEnabled(true);
    }

    public void detallePrestamos(final IFunction<QPAY_Fiado_Response> function) {

        context.loading(true);

        QPAY_Fiado_Request data = new QPAY_Fiado_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());
        data.setFiado_debt_id(Fragment_fiado_3.selection.getFiado_debt_id());

        IFiado service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.d(new Gson().toJson(result));


                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Fiado_Response fiado_response = gson.fromJson(json, QPAY_Fiado_Response.class);

                        if (fiado_response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute(fiado_response);

                        } else {

                            context.loading(false);

                            if (fiado_response.getQpay_code().equals("017")
                                    || fiado_response.getQpay_code().equals("018")
                                    || fiado_response.getQpay_code().equals("019")
                                    || fiado_response.getQpay_code().equals("020")) {
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

                            } else if (fiado_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //context.showAlertAEONBlockedUser();
                            } else {
                                //context.showAlert(fiado_response.getQpay_description());
                                function.execute();
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

        service.fiadoDetail(data);

    }

    public static void recordatorio(HActivity context, String debid, final IFunction function) {

        context.loading(true);

        QPAY_Recordatorio_Request data = new QPAY_Recordatorio_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());
        data.setFiado_debt_id(debid);

        IRecordatorio service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Recordatorio_Response recordatorio_response = gson.fromJson(json, QPAY_Recordatorio_Response.class);

                        if (recordatorio_response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();



                        } else {

                            if (recordatorio_response.getQpay_code().equals("017")
                                    || recordatorio_response.getQpay_code().equals("018")
                                    || recordatorio_response.getQpay_code().equals("019")
                                    || recordatorio_response.getQpay_code().equals("020")) {
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

                            } else if (recordatorio_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(recordatorio_response.getQpay_description());
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

        service.recordatorio(data);

    }

    public void getAdeudos(final IFunction<QPAY_List_Debts_Response> function) {

        context.loading(true);

        QPAY_List_Debts_Request data = new QPAY_List_Debts_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());

        IListDebts service = null;
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
                        QPAY_List_Debts_Response fiado_response = gson.fromJson(json, QPAY_List_Debts_Response.class);

                        if (fiado_response.getQpay_response().equals("true")) {

                            if(fiado_response.getQpay_object() == null)
                                fiado_response.setQpay_object(new QPAY_Fiado[]{});

                            if(function != null)
                                function.execute(fiado_response);

                        } else {

                            if (fiado_response.getQpay_code().equals("017")
                                    || fiado_response.getQpay_code().equals("018")
                                    || fiado_response.getQpay_code().equals("019")
                                    || fiado_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        // context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if (fiado_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                // context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(fiado_response.getQpay_description());
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

        service.listDebts(data);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class AdeudosAdapter extends ArrayAdapter<QPAY_Fiado> {

        public AdeudosAdapter(Context context, List<QPAY_Fiado> adeudos) {
            super(context, 0, adeudos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaccion, parent, false);

            QPAY_Fiado deb = getItem(position);

            ImageView img_2 = convertView.findViewById(R.id.img_2);
            ImageView iv_flecha = convertView.findViewById(R.id.iv_flecha);
            ImageView img_operation = convertView.findViewById(R.id.img_operation);
            TextView text_amount = convertView.findViewById(R.id.text_amount);

            text_amount.setVisibility(View.INVISIBLE);
            img_operation.setVisibility(View.GONE);
            img_2.setVisibility(View.GONE);
            iv_flecha.setVisibility(View.VISIBLE);

            TextView text_titulo = convertView.findViewById(R.id.text_titulo);
            TextView text_fecha = convertView.findViewById(R.id.text_fecha);

            text_titulo.setText(Utils.paserCurrency(deb.getFiado_debt_amount()));
            text_fecha.setText(deb.getFiado_creation_date());

            return convertView;
        }

    }

}

