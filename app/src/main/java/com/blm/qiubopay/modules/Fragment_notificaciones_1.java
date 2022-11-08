package com.blm.qiubopay.modules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.NotificationManagerCompat;

import com.blm.qiubopay.activities.orders.OrderActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.utils.Globals;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.notification.MyOpenHelper;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.QPAY_Notification;
import com.blm.qiubopay.services.FCMBackgroundService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_notificaciones_1 extends HFragment implements IMenuContext {

    public static IFunction iFunctionUpdate;

    private static final String CHAMBITAS_TITLE = "chambita";
    private static final String NEW_ORDER_TITLE = "pedido";

    private View view;
    private MenuActivity context;
    private Object data;

    private ListView list_notifications;
    private ArrayList<QPAY_Notification> notifications;

    private MyOpenHelper db;

    public static Fragment_notificaciones_1 newInstance(Object... data) {
        Fragment_notificaciones_1 fragment = new Fragment_notificaciones_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_notificaciones_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_notificaciones_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_notificaciones_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        NotificationManagerCompat.from(context).cancelAll();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                if(iFunctionUpdate!=null)
                    iFunctionUpdate.execute();
                getContext().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_NOTIFICACIONES_inician);

        list_notifications = view.findViewById(R.id.list_notifications);

        FCMBackgroundService.iFunction = new IFunction() {
            @Override
            public void execute(Object[] data) {

                NotificationManagerCompat.from(context).cancelAll();

                setList();
            }
        };

        db=new MyOpenHelper(context);

        setList();

    }

    public void setList(){

        try {

            db.deleteNotificationNull();

            notifications = db.getNotifications();

            Collections.reverse(notifications);

            ArrayAdapter adapter = new NotificationAdapter(context, notifications);

            list_notifications.setAdapter(adapter);

            list_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    leer(notifications.get(position).getMessageId());
                    if (notifications.get(position).getTitle().toLowerCase().contains("pidelo")){
                        String body = notifications.get(position).getBody();
                        String number = body.substring(body.indexOf(":")+1).replaceAll("\\s+","");

                    }else{
                    }
                    setList();
                    list_notifications.scrollListBy(position);
                }
            });

        }catch (Exception ex){}

    }

    public void leer(String id){
        db.updateNotification(id);
    }


    public void eliminar(QPAY_Notification data){

        try {

            MyOpenHelper db=new MyOpenHelper(context);
            db.deleteNotification(data.getMessageId());
            setList();

            FCMBackgroundService.iFunction.execute();


        }catch (Exception ex){ }

    }

    public class NotificationAdapter extends ArrayAdapter<QPAY_Notification> {

        public NotificationAdapter(Context context, List<QPAY_Notification> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);

            QPAY_Notification notification = notifications.get(position);

            TextView text_title = convertView.findViewById(R.id.text_title);
            TextView text_fecha = convertView.findViewById(R.id.text_fecha);
            ImageView img_activa = convertView.findViewById(R.id.img_activa);

            text_title.setText(notification.getTitle());
            text_fecha.setText(notification.getDate());

            if(notification.isStatus()){
                img_activa.setVisibility(View.VISIBLE);
            } else {
                img_activa.setVisibility(View.GONE);
            }

            ImageView img_delete = convertView.findViewById(R.id.img_delete);
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.alert("¿Eliminar notificación?", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "SI";
                        }

                        @Override
                        public void onClick() {
                            eliminar(notification);
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

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    img_activa.setVisibility(View.GONE);
                    db.updateNotification(notification.getMessageId());
                    String notificationTitle = notification.getTitle()!=null && !notification.getTitle().isEmpty() ? notification.getTitle().toLowerCase().trim() : "";
                    if(notificationTitle.contains(CHAMBITAS_TITLE)){
                        context.alert(getContext().getResources().getDrawable(R.drawable.illustrations_notificaciones_115_x_95_2), notification.getTitle(), notification.getBody(), new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Ir a Chambitas";
                            }

                            @Override
                            public void onClick() {
                                AppPreferences.setAppLink(null);
                                MenuActivity.appLink = Globals.F_CHAMBITAS;
                                getContextMenu().initHome();
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Cerrar";
                            }
                            @Override
                            public void onClick() { }
                        });
                    } else if (notificationTitle.toLowerCase().contains(NEW_ORDER_TITLE)) {
                        startActivity(new Intent(getContext(), OrderActivity.class));
                    } else {
                        context.alert(getContext().getResources().getDrawable(R.drawable.illustrations_notificaciones_115_x_95_2), notification.getTitle(), notification.getBody());
                    }
                }
            });

            return convertView;
        }
    }


    @Override
    public boolean onBackPressed() {
        if(iFunctionUpdate!=null)
            iFunctionUpdate.execute();
        return super.onBackPressed();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

