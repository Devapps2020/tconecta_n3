package com.blm.qiubopay.modules.fincomun.codi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.models.bimbo.QrCodi;
import com.blm.qiubopay.models.bimbo.QrDTO;
import com.blm.qiubopay.utils.Utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Http.Response.Transferencias.FCTransferenciasResponse;
import mx.com.fincomun.origilib.Model.Banxico.CoDi;
import mx.com.fincomun.origilib.Model.Banxico.ItemStore;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_codi_buzon_1 extends HFragment implements IMenuContext {

    private ListView list_notifications;
    List<NotificationDTO> notifications;

    public static Fragment_codi_buzon_1 newInstance(Object... data) {
        Fragment_codi_buzon_1 fragment = new Fragment_codi_buzon_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



      @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_buzon_1, container, false),R.drawable.background_splash_header_1);
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
        LinearLayout tab_3 = getView().findViewById(R.id.tab_3);

        TextView text_1 = getView().findViewById(R.id.text_1);
        TextView text_2 = getView().findViewById(R.id.text_2);
        TextView text_3 = getView().findViewById(R.id.text_3);

        View line_1 = getView().findViewById(R.id.line_1);
        View line_2 = getView().findViewById(R.id.line_2);
        View line_3 = getView().findViewById(R.id.line_3);

        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab_1.setEnabled(false);
                tab_2.setEnabled(true);
                tab_3.setEnabled(true);

                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.GONE);
                line_3.setVisibility(View.GONE);

                text_3.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));

                setDataNotificaciones(1);

            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab_1.setEnabled(true);
                tab_2.setEnabled(false);
                tab_3.setEnabled(true);

                line_1.setVisibility(View.GONE);
                line_2.setVisibility(View.VISIBLE);
                line_3.setVisibility(View.GONE);

                text_3.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                setDataNotificaciones(2);

            }
        });

        tab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab_1.setEnabled(true);
                tab_2.setEnabled(true);
                tab_3.setEnabled(false);

                line_1.setVisibility(View.GONE);
                line_2.setVisibility(View.GONE);
                line_3.setVisibility(View.VISIBLE);

                text_3.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                setDataNotificaciones(3);

            }
        });

        setDataNotificaciones(1);

    }

    public void setDataNotificaciones(int option) {

        notifications = new ArrayList();

        try {

            HDatabase db = new HDatabase(getContext());

            switch (option) {
                case 1:

                    notifications = db.queryNot(NotificationDTO.class, "type", NotificationDTO.TYPE.PAYMENT_REQUEST.ordinal());

                    Collections.reverse(notifications);
                    ArrayAdapter adapter = new NotificationCoDiAdapter(getContext(), notifications);

                    list_notifications.setAdapter(adapter);

                    list_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });

                    break;
                case 2:

                    getContextMenu().getTokenFC(new IFunction<String>() {
                        @Override
                        public void execute(String... data) {
                            if (data != null) {
                                getContextMenu().gethCoDi().transferencias(data[0], new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        FCTransferenciasResponse response = (FCTransferenciasResponse) data[0];

                                        for (DHDatosTransferencia trx : response.getListTransacciones()) {

                                            Logger.d(new Gson().toJson(trx));

                                            NotificationDTO not = new NotificationDTO();
                                            not.setType(NotificationDTO.TYPE.NONE.ordinal());
                                            not.setTitle("Folio: " + trx.getFolioCodi());
                                            not.setBody("Estatus: " + trx.getDescripcionDelEstatus());
                                            not.setNotificationJSON(new Gson().toJson(trx));
                                            //2020-12-07 10:21:00.0

                                            try {
                                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(trx.getFechaOperacion());
                                                not.setDate(date);
                                            } catch (Exception ex) {
                                            }

                                            notifications.add(not);
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
                                                if (dev.getDate().equals(dat)) {
                                                    order.add(dev);
                                                    break;
                                                }

                                            }
                                        }

                                        notifications = order;


                                        //Collections.reverse(notifications);
                                        ArrayAdapter adapter = new NotificationCoDiAdapter(getContext(), notifications);
                                        list_notifications.setAdapter(adapter);

                                        getContext().loading(false);

                                    }
                                });
                            }
                        }
                    });


                    break;
                case 3:

                    notifications = new ArrayList();

                    List<QrDTO> qrs = db.getAll(QrDTO.class);

                    for(QrDTO qr: qrs) {
                        NotificationDTO not = new NotificationDTO();
                        not.setType(NotificationDTO.TYPE.QR.ordinal());
                        not.setDate(qr.getDate());
                        not.setStatus(qr.getStatus());
                        not.setDecodeJSON(qr.getQrJson());
                        notifications.add(not);
                    }

                    Collections.reverse(notifications);
                    adapter = new NotificationCoDiAdapter(getContext(), notifications);

                    list_notifications.setAdapter(adapter);

                    list_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });


                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class NotificationCoDiAdapter extends ArrayAdapter<NotificationDTO> {

        CoDi coDi = new CoDi(getContext());

        public NotificationCoDiAdapter(Context context, List<NotificationDTO> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //convertView = LayoutInflater.from(context).inflate(R.layout.item_notification_codi, parent, false);

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification_codi, parent, false);
            }

            NotificationDTO notification = getItem(position);

            if(notification.getType() == NotificationDTO.TYPE.ACCOUNT_VALIDATION.ordinal()) {
                FCRespuesta response = coDi.descifrarValidacionCuenta(notification.getNotificationJSON());
                notification.setBody(response.getDescripcion().get(0));
            }

            if(notification.getType() == NotificationDTO.TYPE.QR.ordinal()) {
                QrCodi qr = new Gson().fromJson(notification.getDecodeJSON(), QrCodi.class);

                if(qr.getMonto().isEmpty())
                    notification.setTitle("Sin Monto");
                else
                    notification.setTitle(Utils.paserCurrency(qr.getMonto()));

                notification.setBody(qr.getConcepto());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DHDatosTransferencia transferencia = new DHDatosTransferencia();

                        transferencia.setFolioCodi(qr.getIc().getIDC());
                        transferencia.setClaveDeRastreo(qr.getIc().getSER());
                        transferencia.setEstatus(qr.getEstatus());
                        transferencia.setDescripcionDelEstatus(qr.getConcepto());
                        transferencia.setNombreDelOrdenante("");
                        transferencia.setNombreDelBeneficiario(HCoDi.nomCliente);
                        transferencia.setMonto(qr.getMonto().isEmpty() ? 0.0 : Double.parseDouble(qr.getMonto()));
                        transferencia.setTipoAviso("QR");

                        transferencia.setDigitoVerOrd(ItemStore.INSTANCE.getDv() + "");
                        transferencia.setNumCelularOrd(ItemStore.INSTANCE.getAlias());

                        transferencia.setDigitoVerBen(ItemStore.INSTANCE.getDv() + "");
                        transferencia.setNumCelularBen(ItemStore.INSTANCE.getAlias());

                        String stringDate = new SimpleDateFormat("MMM dd yyyy HH:mm").format(notification.getDate());
                        transferencia.setFechaOperacion(stringDate);
                        qr.setFechaOperacion(stringDate);

                        Fragment_codi_detalle_1.transferencia = transferencia;
                        Fragment_codi_detalle_1.codigoQR = qr;
                        getContextMenu().setFragment(Fragment_codi_detalle_1.newInstance());
                    }
                });
            }

            if(notification.getType() == NotificationDTO.TYPE.NONE.ordinal())  {
                //InfoNotificacion response = coDi.descifrarPushInfo(notification.getNotificationJSON());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment_codi_detalle_1.transferencia = new Gson().fromJson(notification.getNotificationJSON(), DHDatosTransferencia.class);
                        getContextMenu().setFragment(Fragment_codi_detalle_1.newInstance());
                    }
                });
            }

            TextView text_dia = convertView.findViewById(R.id.text_dia);
            TextView text_mes = convertView.findViewById(R.id.text_mes);
            TextView text_titulo = convertView.findViewById(R.id.text_titulo);
            TextView text_mensaje = convertView.findViewById(R.id.text_mensaje);
            TextView text_hora = convertView.findViewById(R.id.text_hora);

            text_titulo.setText(notification.getTitle());
            text_mensaje.setText(notification.getBody());

            String stringDate = new SimpleDateFormat("MMM dd yyyy HH:mm").format(notification.getDate());
            String[] date = stringDate.split(" ");

            text_dia.setText(date[1]);
            text_mes.setText(date[0].toUpperCase().replace(".", ""));
            text_hora.setText(date[3]);

            return convertView;
        }
    }

}

