package com.blm.qiubopay.modules.fincomun.codi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Pago.AccionTransaccion;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.FCMovsDevolucionResponse;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.ObjetoPagoDevolucion;
import mx.com.fincomun.origilib.Http.Response.Pago.FCPagoResponse;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelVendedor;
import mx.com.fincomun.origilib.Model.Devoluciones.Devoluciones;
import mx.com.fincomun.origilib.Objects.Devoluciones.DHMovDevolucion;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_codi_devoluciones extends HFragment implements IMenuContext {

    private SwipeMenuListView list_notifications;
    private List<DHMovDevolucion> notifications;

    public static Fragment_codi_devoluciones newInstance(Object... data) {
        Fragment_codi_devoluciones fragment = new Fragment_codi_devoluciones();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_devoluciones_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        list_notifications = getView().findViewById(R.id.list_notifications);

        EditText edit_fecha_inico = getView().findViewById(R.id.edit_fecha_inico);
        EditText edit_fecha_fin = getView().findViewById(R.id.edit_fecha_fin);

        LinearLayout layout_fecha_inicio = getView().findViewById(R.id.layout_fecha_inico);
        layout_fecha_inicio.setVisibility(View.GONE);
        layout_fecha_inicio.setOnClickListener(view -> {

           //VERIFICAR
            getContextMenu().showPicker(R.string.text_fecha, false, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    String date = ((String)data[0]).replaceAll("/", "-");
                    edit_fecha_inico.setText(date);
                    consultarDevoluciones(edit_fecha_inico.getText().toString(), edit_fecha_fin.getText().toString());
                }
            });
        });

        LinearLayout layout_fecha_fin = getView().findViewById(R.id.layout_fecha_fin);
        layout_fecha_inicio.setVisibility(View.GONE);
        layout_fecha_fin.setOnClickListener(view -> {
            //VERIFICAR
            getContextMenu().showPicker(R.string.text_fecha, false, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    String date = ((String)data[0]).replaceAll("/", "-");
                    edit_fecha_fin.setText(date);
                    consultarDevoluciones(edit_fecha_inico.getText().toString(), edit_fecha_fin.getText().toString());
                }
            });
        });

    }

    public void consultarDevoluciones(String inicio, String fin) {

        if(inicio.isEmpty() || fin.isEmpty())
            return;

        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {
                if (data !=null) {
                    getContextMenu().gethCoDi().devoluciones(inicio, fin, data[0], new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            FCMovsDevolucionResponse response = (FCMovsDevolucionResponse) data[0];

                            getContext().loading(false);

                            if (response.getCodigo() != 0) {
                                getContext().alert("Fincomún", response.getDescripcion());
                                return;
                            }

                            notifications = response.getMovimientos();

                            ArrayList<Date> fechas = new ArrayList();

                            for (DHMovDevolucion dev : notifications) {

                                try {
                                    Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dev.getFecha());
                                    fechas.add(fecha);
                                    dev.setFecha(dev.getFecha() + "x" + fecha.getTime());
                                } catch (Exception ex) {

                                }
                            }

                            Collections.sort(fechas);
                            Collections.reverse(fechas);

                            List<DHMovDevolucion> order = new ArrayList();

                            for (Date dat : fechas) {
                                for (DHMovDevolucion dev : notifications) {
                                    if (dev.getFecha().contains(dat.getTime() + "")) {
                                        dev.setFecha(dev.getFecha().split("x")[0]);
                                        order.add(dev);
                                        break;
                                    }

                                }
                            }

                            notifications = order;

                            setDataNotificaciones();

                        }
                    });
                }
            }
        });

    }

    public void setDataNotificaciones() {

        ArrayAdapter adapter = new NotificationDevolucionCoDiAdapter(getContext(), notifications);
        list_notifications.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem itemPagar = new SwipeMenuItem(getContext());
                itemPagar.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboGrayLight1)));
                itemPagar.setWidth(dp2px(90));
                itemPagar.setTitle("DEVOLVER");
                itemPagar.setTitleSize(14);
                itemPagar.setTitleColor(Color.BLACK);
                menu.addMenuItem(itemPagar);

                SwipeMenuItem itemPosponer = new SwipeMenuItem(getContext());
                itemPosponer.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboGrayDark)));
                itemPosponer.setWidth(dp2px(90));
                itemPosponer.setTitle("POSPONER");
                itemPosponer.setTitleSize(14);
                itemPosponer.setTitleColor(Color.WHITE);

                SwipeMenuItem itemRechazar = new SwipeMenuItem(getContext());
                itemRechazar.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboRedBimbo)));
                itemRechazar.setWidth(dp2px(90));
                itemRechazar.setTitle("RECHAZAR");
                itemRechazar.setTitleSize(14);
                itemRechazar.setTitleColor(Color.WHITE);
                menu.addMenuItem(itemRechazar);

            }
        };

        list_notifications.setMenuCreator(creator);
        list_notifications.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        list_notifications.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        pagarDevolucion(notifications.get(position));
                        break;
                    case 1:
                        rechazarNotificacion(AccionTransaccion.POSPONER, notifications.get(position));
                        break;
                    case 2:
                        rechazarNotificacion(AccionTransaccion.RECHAZO, notifications.get(position));
                        break;

                }
                return false;
            }
        });

    }

    public void rechazarNotificacion(AccionTransaccion type,DHMovDevolucion notification) {

        Devoluciones devoluciones = new Devoluciones(getContext());
        ObjetoPagoDevolucion objetoPagoDevolucion = devoluciones.generarObjetoCobro(notification);

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                getContextMenu().gethCoDi().pago(type, "", text[0], objetoPagoDevolucion, HCoDi.cuentaSeleccionada, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContext().loading(false);

                        FCPagoResponse response = (FCPagoResponse) data[0];
                        if (response.getCodigo() == 1) {

                            String message = type == AccionTransaccion.RECHAZO ? "Rechazo exitoso" : "Posponer exitoso";

                            getContext().alert("CoDi", message, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void pagarDevolucion(DHMovDevolucion notification) {

        Devoluciones devoluciones = new Devoluciones(getContext());
        ObjetoPagoDevolucion objetoPagoDevolucion = devoluciones.generarObjetoCobro(notification);

        Fragment_codi_quiero_pagar_1.modelPagoDevolucion = objetoPagoDevolucion;
        Fragment_codi_quiero_pagar_1.modelCobro = new ModelObjetoCobro("", "Devolución",
                (double) objetoPagoDevolucion.getModelObjetoCobro().getAMO(), 0l, new Long(objetoPagoDevolucion.getModelObjetoCobro().getREF()), 0, 0,
                new ModelVendedor( objetoPagoDevolucion.getModelObjetoCobro().getV().getNAM(), "", 0, 0, ""));

        getContext().setFragment(Fragment_codi_quiero_pagar_1.newInstance());

    }

    @Override
    public MenuActivity getContextMenu() {
        return null;
    }

    public class NotificationDevolucionCoDiAdapter extends ArrayAdapter<DHMovDevolucion> {

        public NotificationDevolucionCoDiAdapter(Context context, List<DHMovDevolucion> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //convertView = LayoutInflater.from(context).inflate(R.layout.item_notification_codi_pago, parent, false);

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification_codi_pago, parent, false);
            }

            DHMovDevolucion notification = notifications.get(position);

            ImageView img_more = convertView.findViewById(R.id.img_more);
            ImageView img_ok = convertView.findViewById(R.id.img_ok);
            ImageView img_rejected = convertView.findViewById(R.id.img_rejected);

            TextView text_dia = convertView.findViewById(R.id.text_dia);
            TextView text_mes = convertView.findViewById(R.id.text_mes);
            TextView text_titulo = convertView.findViewById(R.id.text_titulo);
            TextView text_concepto = convertView.findViewById(R.id.text_concepto);
            TextView text_beneficiario = convertView.findViewById(R.id.text_beneficiario);
            TextView text_hora = convertView.findViewById(R.id.text_hora);
            TextView text_expira = convertView.findViewById(R.id.text_expira);

            Date fecha = new Date();

            try {
                fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(notification.getFecha());
            } catch (Exception ex) {}

            String stringDate = new SimpleDateFormat("MMM dd yyyy HH:mm").format(fecha);

            String[] date = stringDate.split(" ");

            text_expira.setText("---");
            text_titulo.setText(Utils.paserCurrency(notification.getCantidad() + ""));
            text_beneficiario.setText(notification.getNomBen());
            text_concepto.setText(notification.getConcepto());

            text_dia.setText(date[1]);
            text_mes.setText(date[0].toUpperCase().replace(".", ""));
            text_hora.setText(date[3]);

            img_more.setVisibility(View.GONE);
            img_ok.setVisibility(View.GONE);
            img_rejected.setVisibility(View.GONE);

            if("0".equals(notification.getEstatusDev())) {
                img_more.setVisibility(View.VISIBLE);
            } else
                img_ok.setVisibility(View.VISIBLE);

            img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list_notifications.smoothOpenMenu(position);
                }
            });

            return convertView;
        }
    }

}

