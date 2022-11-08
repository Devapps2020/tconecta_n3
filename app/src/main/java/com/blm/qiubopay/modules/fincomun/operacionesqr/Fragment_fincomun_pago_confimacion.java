package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_menu;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Request.Pago.AccionTransaccion;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.ObjetoPagoDevolucion;
import mx.com.fincomun.origilib.Http.Response.Pago.FCPagoResponse;
import mx.com.fincomun.origilib.Model.Banxico.CoDi;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.InfoSolicitudPago;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_pago_confimacion extends HFragment implements IMenuContext {

    private DHCuenta seleccionada = new DHCuenta();
    private ModelObjetoCobro modelCobro;
    private NotificationDTO notification;
    private ObjetoPagoDevolucion modelPagoDevolucion;
    private RelativeLayout rl_detalle;
    private ArrayList<FCEditText> campos = new ArrayList<>();
    private String folioCodi = "-";
    private LinearLayout ll_sinmonto;


    private TextView tv_monto, tv_des_monto,tv_referencia, tv_concepto;
    private Button btn_confirmar, btn_rechazar;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    private AlertDialog alertFC = null;

    private boolean isMontoEnabled = false;

    public static Fragment_fincomun_pago_confimacion newInstance(@NotNull String params, @NotNull String folioCodi){
        Fragment_fincomun_pago_confimacion fragment = new Fragment_fincomun_pago_confimacion();
        Bundle args = new Bundle();
        args.putString("param",params);
        args.putString("folioCodi",folioCodi);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_pago_confirmacion, container, false),R.drawable.background_splash_header_2);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            // TODO : Deserializar información
            modelCobro = new Gson().fromJson(getArguments().getString("param"), ModelObjetoCobro.class);
            folioCodi = getArguments().getString("folioCodi");
            modelCobro.setFCIdentificadorCobro(folioCodi);
            isMontoEnabled = modelCobro.getAMO() != null;

        }

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

        rl_detalle = getView().findViewById(R.id.rl_detalle);
        rl_detalle.setVisibility(View.INVISIBLE);

        tv_monto = getView().findViewById(R.id.tv_monto);
        tv_des_monto = getView().findViewById(R.id.tv_des_monto);

        tv_referencia = getView().findViewById(R.id.tv_referencia);
        tv_concepto = getView().findViewById(R.id.tv_concepto);
        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        ll_sinmonto = getView().findViewById(R.id.ll_sinmonto);

        btn_rechazar = getView().findViewById(R.id.btn_rechazar);
        btn_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().backFragment();
            }
        });

        if(modelCobro != null){
            tv_referencia.setText(String.valueOf(modelCobro.getREF()));
            tv_concepto.setText(modelCobro.getDES());
            btn_confirmar.setEnabled(true);
            rl_detalle.setVisibility(View.VISIBLE);
        }else{
            //TODO POP DE DATOS INCOMPLETOS
            btn_confirmar.setEnabled(false);
            showPopup(1, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    //Todo : Lanzar al inicio
                }
            });
        }

        btn_confirmar.setOnClickListener(v -> {
            if(!isMontoEnabled) {
                campos.get(0).setRequired(true);
                if (!campos.get(0).isValid() || Double.valueOf(campos.get(0).getTextDouble()).compareTo(0.0) < 0) {
                    getContextMenu().alert("Ingresa un monto valido");
                    return;
                }
                modelCobro.setAMO(Double.valueOf(campos.get(0).getTextDouble()));
            }

            pagar(AccionTransaccion.PAGO);
        });

        if (SessionApp.getInstance().getCuentasCoDi() != null && !SessionApp.getInstance().getCuentasCoDi().isEmpty()){
            seleccionada = SessionApp.getInstance().getCuentasCoDi().get(0);
        }else{
            // TODO : POP NO TIENE CUENTAS
            btn_confirmar.setEnabled(false);
            showPopup(1, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    showPopup(1, new IClickView() {
                        @Override
                        public void onClick(Object... data) {
                            getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(),true);
                        }
                    });
                }
            });
        }

        FCEditText.ITextChanged validate = new FCEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };


        //  0 => Monto
        campos.add(FCEditText.create(getView().findViewById(R.id.et_monto))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.CURRENCY)
                .setHint("Monto")
                .setAlert(R.string.text_input_required)
                .setIcon(R.drawable.icons_cr_dito)
                .setTextChanged(validate));

        if (!isMontoEnabled){
            ll_sinmonto.setVisibility(View.VISIBLE);
            tv_des_monto.setVisibility(View.GONE);
            tv_monto.setVisibility(View.GONE);
            campos.get(0).setRequired(false);
        }else{
            tv_monto.setText(currencyFormatter.format(modelCobro.getAMO()));
            campos.get(0).setRequired(false);
        }

    }




    public void pagar(AccionTransaccion type) {

        if(notification != null) {

            CoDi coDi = new CoDi(getContext());

            InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());

            infoSolicitudPago.getInformacionCobroNoPresencial().setMt(new Float(campos.get(1).getTextDecimal()));

            getContextMenu().getTokenFC((String... text) -> {
                if(text !=null) {
                    getContextMenu().gethCoDi().pago(type, campos.get(3).getText(), text[0], infoSolicitudPago, HCoDi.cuentaSeleccionada, new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            getContext().loading(false);

                            FCPagoResponse response = (FCPagoResponse) data[0];
                            if (response.getCodigo() == 0) {

                                getContext().alert("CoDi", response.getDescripcion(), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        try {
                                            HDatabase db = new HDatabase(getContext());

                                            if (type == AccionTransaccion.POSPONER) {
                                                notification.setStatus(NotificationDTO.STATUS.POSTPONED.ordinal());
                                                db.createOrUpdate(notification);
                                            } else {
                                                db.deleteById(NotificationDTO.class, notification.getNotifivationId());
                                            }

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        modelCobro = null;
                                        notification = null;
                                        getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(), true);

                                    }
                                });


                            } else {
                                getContext().alert("Fincomún", response.getDescripcion());
                            }
                        }
                    });
                }
            });

        } else if(modelPagoDevolucion != null) {

            getContextMenu().getTokenFC((String... text) -> {
                if (text != null) {
                    getContextMenu().gethCoDi().pago(type, campos.get(3).getText(), text[0], modelPagoDevolucion, HCoDi.cuentaSeleccionada, new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            getContext().loading(false);

                            FCPagoResponse response = (FCPagoResponse) data[0];
                            if (response.getCodigo() == 0) {

                                getContext().alert("CoDi", response.getDescripcion(), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        modelCobro = null;
                                        notification = null;
                                        getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(), true);

                                    }
                                });

                            } else {
                                getContext().alert("Fincomún", response.getDescripcion());
                            }
                        }
                    });
                }
            });

        } else{

            //if(campos.get(1).getEditText().isEnabled())
            //    modelCobro.setAMO(Double.parseDouble(campos.get(1).getTextDecimal()));

            getContextMenu().getTokenFC((String... text) -> {
                if (text != null) {
                    getContextMenu().gethCoDi().pago(type, String.valueOf(modelCobro.getREF()), text[0], modelCobro, seleccionada, new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            getContext().loading(false);

                            FCPagoResponse response = (FCPagoResponse) data[0];
                            if (response.getCodigo() == 0) {

                                showPopup(0, new IClickView() {
                                    @Override
                                    public void onClick(Object... data) {
                                        getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(), true);
                                    }
                                });


                            } else {
                                //getContext().alert("Fincomún", response.getDescripcion());
                                showPopup(1, new IClickView() {
                                    @Override
                                    public void onClick(Object... data) {
                                        getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(), true);

                                    }
                                });
                            }
                        }
                    });
                }
            });

        }

    }



    private void showPopup(int estatus, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_no_offer, null);

        Button btn_aceptar = view.findViewById(R.id.btn_offer);
        btn_aceptar.setText("Aceptar");
        Button btn_contactar = view.findViewById(R.id.btn_acept);
        btn_contactar.setText("Contactar");

        ImageView imv_icon = view.findViewById(R.id.imv_icon);
        imv_icon.setImageBitmap(null);
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        switch (estatus){
            case 0 :  //Exito
                    btn_contactar.setVisibility(View.GONE);
                    imv_icon.setBackground(getContext().getDrawable(R.drawable.icons_check));
                    text_title.setText(getContext().getResources().getString(R.string.FC_txt_exito_header));
                    text_mensaje.setText(getContext().getResources().getString(R.string.FC_txt_exito_desc));
                break;

            case 1:  //Error
                    btn_contactar.setVisibility(View.VISIBLE);
                    imv_icon.setBackground(getContext().getDrawable(R.drawable.warning));
                    text_title.setText(getContext().getResources().getString(R.string.FC_txt_error_header));
                    text_mensaje.setText(getContext().getResources().getString(R.string.FC_txt_error_desc));
                break;
        }



        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        btn_contactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFC.dismiss();
                getContext().setFragment(Fragment_fincomun_operaciones_qr.newInstance(),true);
            }
        });



        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }



    private void validate() {
        for (FCEditText item: campos){
            if(!item.isValid()){
                btn_confirmar.setEnabled(false);
                return;
            }
        }
        btn_confirmar.setEnabled(true);
    }
}
