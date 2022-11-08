package com.blm.qiubopay.modules.fincomun.codi;

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
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

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

public class Fragment_codi_quiero_pagar_1 extends HFragment implements IMenuContext {

    DHCuenta seleccionada = new DHCuenta();
    public static ModelObjetoCobro modelCobro;
    public static NotificationDTO notification;
    public static ObjetoPagoDevolucion modelPagoDevolucion;

    private ArrayList<HEditText> campos;
    private Button btn_payment;
    private Button btn_posponer;
    private Button btn_rechazar;

    public static Fragment_codi_quiero_pagar_1 newInstance(Object... data) {
        Fragment_codi_quiero_pagar_1 fragment = new Fragment_codi_quiero_pagar_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_codi_quiero_pagar_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_quiero_pagar_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        btn_payment = getView().findViewById(R.id.btn_payment);
        btn_posponer = getView().findViewById(R.id.btn_posponer);
        btn_rechazar = getView().findViewById(R.id.btn_rechazar);

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

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name_beneficiary),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_beneficiary)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_write_amount),
                true, 11, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_write_amount)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_payment_concept),
                true, 100, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_payment_concept)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_reference),
                true, 7, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_reference)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_cuenta_select),
                true, 500, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_cuenta)));

        campos.get(0).setText(modelCobro.getV().getNAM());
        campos.get(2).setText(modelCobro.getDES());

        if(modelCobro.getAMO() != null && modelCobro.getAMO() > 0) {
            campos.get(1).setText(Utils.paserCurrency(modelCobro.getAMO() + ""));
            campos.get(1).getEditText().setTag(modelCobro.getAMO() + "");
            campos.get(1).getEditText().setEnabled(false);
        }

        campos.get(1).setTipo(HEditText.Tipo.MONEDA);
        campos.get(0).getEditText().setEnabled(false);
        campos.get(2).getEditText().setEnabled(false);

        campos.get(3).getEditText().setText(modelCobro.getREF() + "");
        campos.get(3).getEditText().setEnabled(false);

        validate();

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagar(AccionTransaccion.PAGO);
            }
        });

        btn_posponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagar(AccionTransaccion.POSPONER);
            }
        });

        btn_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagar(AccionTransaccion.RECHAZO);
            }
        });

        if(notification != null && notification.getStatus() == NotificationDTO.STATUS.POSTPONED.ordinal())
            btn_posponer.setVisibility(View.GONE);

        setCuentas();

    }

    public void validate(){

        btn_payment.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_payment.setEnabled(true);

    }

    public void setCuentas() {

        ArrayList<ModelSpinner> infoCuenta = new ArrayList();

        for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi())
            infoCuenta.add(new ModelSpinner(cue.getNombreCuenta(), "" + cue.getClabeCuenta()));

        EditSpinner spCuenta = getView().findViewById(R.id.edt_cuenta_select);
        spCuenta.setLines(2);
        spCuenta.setSingleLine(false);

     /*   context.setDataSpinner(spCuenta, infoCuenta, new IFunction<String>() {
            @Override
            public void execute(String... data) {

                if(data[0] == null || data[0].isEmpty())
                    return;

                for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi()){
                    if(data[0].equals(cue.getClabeCuenta())) {
                        seleccionada.setNombreCuenta(cue.getNombreCuenta());
                        seleccionada.setNumeroCuenta(cue.getNumeroCuenta());
                        seleccionada.setClabeCuenta(cue.getClabeCuenta());
                        seleccionada.setSaldo(cue.getSaldo());
                        validate();
                        return;
                    }
                }
            }
        });*/

    }

    public void pagar(AccionTransaccion type) {

        if(notification != null) {

            CoDi coDi = new CoDi(getContext());

            InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());

            infoSolicitudPago.getInformacionCobroNoPresencial().setMt(new Float(campos.get(1).getTextDecimal()));

            getContextMenu().getTokenFC((String... text) -> {
                if (text != null) {
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
                                        getContext().setFragment(Fragment_codi_menu.newInstance());

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
                                        getContext().setFragment(Fragment_codi_menu.newInstance());

                                    }
                                });

                            } else {
                                getContext().alert("Fincomún", response.getDescripcion());
                            }
                        }
                    });
                }
            });

        } else  {

            if(campos.get(1).getEditText().isEnabled())
                modelCobro.setAMO(Double.parseDouble(campos.get(1).getTextDecimal()));

            getContextMenu().getTokenFC((String... text) -> {
                if (text != null) {
                    getContextMenu().gethCoDi().pago(type, campos.get(3).getText(), text[0], modelCobro, seleccionada, new IFunction() {
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
                                        getContext().setFragment(Fragment_codi_menu.newInstance());

                                    }
                                });


                            } else {
                                getContext().alert("Fincomún", response.getDescripcion());
                            }
                        }
                    });
                }
            });

        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

