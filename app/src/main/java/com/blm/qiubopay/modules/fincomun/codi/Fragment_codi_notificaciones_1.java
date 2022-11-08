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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.utils.Utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.fincomun.origilib.Http.Request.Pago.AccionTransaccion;
import mx.com.fincomun.origilib.Http.Response.Pago.FCPagoResponse;
import mx.com.fincomun.origilib.Model.Banxico.CoDi;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelVendedor;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.InfoSolicitudPago;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_codi_notificaciones_1 extends HFragment implements IMenuContext {

    private SwipeMenuListView list_notifications;
    private List<NotificationDTO> notifications;
    private ArrayAdapter adapter;

    public static Fragment_codi_notificaciones_1 newInstance(Object... data) {
        Fragment_codi_notificaciones_1 fragment = new Fragment_codi_notificaciones_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_notificaciones_1, container, false),R.drawable.background_splash_header_1);
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

        LinearLayout tab_1 = getView().findViewById(R.id.tab_1);
        LinearLayout tab_2 = getView().findViewById(R.id.tab_2);
        TextView text_1 = getView().findViewById(R.id.text_1);
        TextView text_2 = getView().findViewById(R.id.text_2);
        View line_1 = getView().findViewById(R.id.line_1);
        View line_2 = getView().findViewById(R.id.line_2);


        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_1.setEnabled(false);
                tab_2.setEnabled(true);

                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.GONE);
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                setDataNotificaciones(true);

            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_1.setEnabled(true);
                tab_2.setEnabled(false);

                line_1.setVisibility(View.GONE);
                line_2.setVisibility(View.VISIBLE);
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                setDataNotificaciones(false);

            }
        });

        setDataNotificaciones(true);

    }

    public void setDataNotificaciones(boolean isPago) {

        notifications = new ArrayList();

        try {

            Map<String, Object> where = new HashMap<>();

            if(isPago)
                where.put("type", NotificationDTO.TYPE.PAYMENT_REQUEST.ordinal());
            else
                where.put("type", NotificationDTO.TYPE.REPAYMENT.ordinal());

            HDatabase db = new HDatabase(getContext());
            notifications = db.query(NotificationDTO.class, where);

        } catch (SQLException e) {
            e.printStackTrace();
        }

         ArrayList<Date> fechas = new ArrayList();

                                for (NotificationDTO dev : notifications) {

                                    try {
                                        fechas.add(dev.getDate());
                                    } catch (Exception ex) {

                                    }
                                }

                                Collections.sort(fechas);
                                Collections.reverse(fechas);

                                List<NotificationDTO> order = new ArrayList();

                                for (Date dat : fechas) {
                                    for (NotificationDTO dev : notifications) {
                                        if(dev.getDate().equals(dat)) {
                                            order.add(dev);
                                            break;
                                        }

                                    }
                                }

                                notifications = order;

        adapter = new NotificationCobroCoDiAdapter(getContext(), notifications);
        list_notifications.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem itemPagar = new SwipeMenuItem(getContext());
                itemPagar.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboGrayLight1)));
                itemPagar.setWidth(dp2px(90));
                itemPagar.setTitle("PAGAR");
                itemPagar.setTitleSize(14);
                itemPagar.setTitleColor(Color.BLACK);
                menu.addMenuItem(itemPagar);

                SwipeMenuItem itemPosponer = new SwipeMenuItem(getContext());
                itemPosponer.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboGrayDark)));
                itemPosponer.setWidth(dp2px(90));
                itemPosponer.setTitle("POSPONER");
                itemPosponer.setTitleSize(14);
                itemPosponer.setTitleColor(Color.WHITE);

                if(isPago)
                    menu.addMenuItem(itemPosponer);

                SwipeMenuItem itemRechazar = new SwipeMenuItem(getContext());
                itemRechazar.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.colorBimboRedBimbo)));
                itemRechazar.setWidth(dp2px(90));
                itemRechazar.setTitle("RECHAZAR");
                itemRechazar.setTitleSize(14);
                itemRechazar.setTitleColor(Color.WHITE);

                if(isPago)
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
                        pagarNotificacion(notifications.get(position));
                        break;
                    case 1:
                        posponeNotificacion(notifications.get(position));
                        break;
                    case 2:
                        rechazarNotificacion(notifications.get(position));
                        break;
                }
                return false;
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void pagarNotificacion(NotificationDTO notification) {

        CoDi coDi = new CoDi(getContext());
        InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());
        Fragment_codi_quiero_pagar_1.notification = notification;
        Fragment_codi_quiero_pagar_1.modelCobro = new ModelObjetoCobro("", infoSolicitudPago.getInformacionCobroNoPresencial().getCc(), (double) infoSolicitudPago.getInformacionCobroNoPresencial().getMt(), 0l,
                new Long(infoSolicitudPago.getInformacionCobroNoPresencial().getIdCE()), 0, 0,
                    new ModelVendedor( infoSolicitudPago.getInformacionCobroNoPresencial().getV().getNb(), "", 0, 0, ""));

        Logger.d(new Gson().toJson(infoSolicitudPago));

        getContext().setFragment(Fragment_codi_quiero_pagar_1.newInstance());
    }

    public void rechazarNotificacion(NotificationDTO notification) {

        CoDi coDi = new CoDi(getContext());
        InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                getContextMenu().gethCoDi().pago(AccionTransaccion.RECHAZO, infoSolicitudPago.getInformacionCobroNoPresencial().getIdCE(), text[0], infoSolicitudPago, HCoDi.cuentaSeleccionada, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContext().loading(false);

                        FCPagoResponse response = (FCPagoResponse) data[0];
                        if (response.getCodigo() == 0) {

                            notification.setStatus(NotificationDTO.STATUS.REJECTED.ordinal());

                            try {

                                HDatabase db = new HDatabase(getContext());
                                db.deleteById(NotificationDTO.class, notification.getNotifivationId());

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            setDataNotificaciones(true);
                            getContext().alert("CoDi", "Pago rechazado", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return null;
                                }

                                @Override
                                public void onClick() {

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

    public void posponeNotificacion(NotificationDTO notification) {

        CoDi coDi = new CoDi(getContext());
        InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                getContextMenu().gethCoDi().pago(AccionTransaccion.POSPONER, infoSolicitudPago.getInformacionCobroNoPresencial().getIdCE(), text[0], infoSolicitudPago, HCoDi.cuentaSeleccionada, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContext().loading(false);

                        FCPagoResponse response = (FCPagoResponse) data[0];
                        if (response.getCodigo() == 0) {

                            notification.setStatus(NotificationDTO.STATUS.POSTPONED.ordinal());

                            try {

                                HDatabase db = new HDatabase(getContext());
                                db.createOrUpdate(notification);
                                adapter.notifyDataSetChanged();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            setDataNotificaciones(true);

                            getContext().alert("CoDi", "Pago pospuesto", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return null;
                                }

                                @Override
                                public void onClick() {

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

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class NotificationCobroCoDiAdapter extends ArrayAdapter<NotificationDTO> {

        CoDi coDi = new CoDi(getContext());

        public NotificationCobroCoDiAdapter(Context context, List<NotificationDTO> users) {
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

            NotificationDTO notification = getItem(position);

            InfoSolicitudPago infoSolicitudPago = coDi.descifrarSolicitudPago(notification.getNotificationJSON());

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

            String stringDate = new SimpleDateFormat("MMM dd yyyy HH:mm").format(notification.getDate());
            String[] date = stringDate.split(" ");

            text_dia.setText(date[1]);
            text_mes.setText(date[0].toUpperCase().replace(".", ""));
            text_hora.setText(date[3]);

            img_more.setVisibility(View.GONE);
            img_ok.setVisibility(View.GONE);
            img_rejected.setVisibility(View.GONE);

            if(notification.getStatus() == NotificationDTO.STATUS.ACTIVE.ordinal() || notification.getStatus() == NotificationDTO.STATUS.POSTPONED.ordinal() ) {
                img_more.setVisibility(View.VISIBLE);
            } else if(notification.getStatus() == NotificationDTO.STATUS.PAID.ordinal())
                img_ok.setVisibility(View.VISIBLE);
            else if(notification.getStatus() == NotificationDTO.STATUS.REJECTED.ordinal())
                img_rejected.setVisibility(View.VISIBLE);

            img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list_notifications.smoothOpenMenu(position);
                }
            });

            String stringDateFinal = new SimpleDateFormat("MMM dd yyyy HH:mm").format(new Date(infoSolicitudPago.getInformacionCobroNoPresencial().getHl()));
            text_expira.setText(stringDateFinal.toUpperCase() + " Hrs");
            text_titulo.setText(Utils.paserCurrency(infoSolicitudPago.getInformacionCobroNoPresencial().getMt() + ""));
            text_beneficiario.setText(infoSolicitudPago.getInformacionCobroNoPresencial().getV().getNb());
            text_concepto.setText(infoSolicitudPago.getInformacionCobroNoPresencial().getCc());

            return convertView;
        }
    }

}

