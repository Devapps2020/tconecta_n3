package com.blm.qiubopay.services;

import static com.blm.qiubopay.utils.Globals.NotificationType.DATA_ON_DEMAND;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_ACCEPT_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_DECLINE_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_NEW_ORDER;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.database.notification.MyOpenHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.models.QPAY_Notification;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.utils.Globals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Model.Banxico.CoDi;
import mx.com.fincomun.origilib.Objects.Notificaciones.Devoluciones.InfoDevolucion;
import mx.com.fincomun.origilib.Objects.Notificaciones.Info.InfoNotificacion;
import mx.com.fincomun.origilib.Objects.Notificaciones.Info.NotificacionInfo;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.InfoSolicitudPago;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.NotificacionCobro;
import mx.com.fincomun.origilib.Objects.Notificaciones.ValidacionCuenta.NotificacionValidacionCuenta;
import mx.devapps.utils.interfaces.IFunction;

public class FCMBackgroundService extends WakefulBroadcastReceiver {

    private static final String TAG = "FirebaseService";

    public static IFunction iFunction;
    public static IFunction fcmMultiFunction;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (intent.getExtras() != null) {

            String data = (String) intent.getExtras().get("data");

            if (data != null) {

                if (data.contains("infoCuenta") || data.contains("payreq") || data.contains("info") || data.contains("mcDev")) {

                    NotificationDTO notification = new NotificationDTO();
                    notification.setNotificationJSON(data);
                    notification.setStatus(NotificationDTO.STATUS.ACTIVE.ordinal());
                    notification.setDate(new Date());

                    CoDi coDi = new CoDi(context);

                    if (data.contains("infoCuenta")){

                        NotificacionValidacionCuenta notificacionValidacionCuenta = new Gson().fromJson(data, NotificacionValidacionCuenta.class);

                        notification.setType(NotificationDTO.TYPE.ACCOUNT_VALIDATION.ordinal());
                        notification.setTitle(notificacionValidacionCuenta.getTitle());
                        notification.setBody(notificacionValidacionCuenta.getBody());

                        notification.setCr(notificacionValidacionCuenta.getInfoCuenta().getCr());
                        notification.setInfCif(notificacionValidacionCuenta.getInfoCuenta().getInfCif());

                        FCRespuesta response = coDi.descifrarValidacionCuenta(data);
                        notification.setDecodeJSON(new Gson().toJson(response));

                        if(response.getCodigo() == 1)
                            AppPreferences.setCodiRegister(true);
                        else
                            notification.setBody(response.getDescripcion().get(0));

                        saveNotificationCoDi(notification);

                        MenuActivity.alertCodi.execute(notification.getBody());

                        showMessage(notification.getTitle(), notification.getBody());

                    } else if (data.contains("payreq")){

                        NotificacionCobro notificacionCobro = new Gson().fromJson(data, NotificacionCobro.class);

                        notification.setType(NotificationDTO.TYPE.PAYMENT_REQUEST.ordinal());
                        notification.setTitle(notificacionCobro.getTitle());
                        notification.setBody(notificacionCobro.getBody());

                        notification.setId(notificacionCobro.getPayreq().getInfoCif().getId());
                        notification.setMc(notificacionCobro.getPayreq().getInfoCif().getMc());
                        notification.setS(notificacionCobro.getPayreq().getInfoCif().getS());

                        InfoSolicitudPago response = coDi.descifrarSolicitudPago(data);
                        notification.setDecodeJSON(new Gson().toJson(response));

                        saveNotificationCoDi(notification);

                        MenuActivity.alertCodi.execute(notification.getBody());

                        showMessage(notification.getTitle(), notification.getBody());

                    }else if(data.contains("info")){

                        NotificacionInfo notificacionInfo = new Gson().fromJson(data, NotificacionInfo.class);
                        notification.setType(NotificationDTO.TYPE.INFORMATION.ordinal());
                        notification.setTitle(notificacionInfo.getTitle());
                        notification.setBody(notificacionInfo.getBody());

                        notification.setId(notificacionInfo.getInfo().getInfoCif().getId());
                        notification.setMc(notificacionInfo.getInfo().getInfoCif().getMc());
                        notification.setS(notificacionInfo.getInfo().getInfoCif().getS());

                        InfoNotificacion response = coDi.descifrarPushInfo(data);
                        notification.setDecodeJSON(new Gson().toJson(response));

                        saveNotificationCoDi(notification);

                        MenuActivity.alertCodi.execute(notification.getBody());

                        showMessage(notification.getTitle(), notification.getBody());

                    }else if(data.contains("mcDev")) {

                        NotificacionCobro notificacionDev = new Gson().fromJson(data, NotificacionCobro.class);
                        notification.setType(NotificationDTO.TYPE.REPAYMENT.ordinal());
                        notification.setTitle(notificacionDev.getTitle());
                        notification.setBody(notificacionDev.getBody());

                        notification.setId(notificacionDev.getPayreq().getInfoCif().getId());
                        notification.setMc(notificacionDev.getPayreq().getInfoCif().getMc());
                        notification.setS(notificacionDev.getPayreq().getInfoCif().getS());

                        InfoDevolucion response = coDi.descifrarDevolucion(data);
                        notification.setDecodeJSON(new Gson().toJson(response));

                        saveNotificationCoDi(notification);

                        MenuActivity.alertCodi.execute(notification.getBody());

                        showMessage(notification.getTitle(), notification.getBody());

                    }

                }

            } else {

                QPAY_Notification notification = new QPAY_Notification();
                Boolean persistNotification = true;
                AppPreferences.setAppLink(null);

                if(intent.getExtras().get("google.c.a.c_id") != null) {
                    notification.setMessageId(intent.getExtras().get("google.c.a.c_id").toString());
                } else if (intent.getExtras().get("google.message_id") != null) {
                    notification.setMessageId(intent.getExtras().get("google.message_id").toString());
                }

                if(intent.getExtras().get("gcm.notification.title") != null) {
                    notification.setTitle(intent.getExtras().getString("gcm.notification.title"));
                } else if (intent.getExtras().get("title") != null) {
                    notification.setTitle(intent.getExtras().getString("title"));
                }

                if(intent.getExtras().get("gcm.notification.body") != null) {
                    notification.setBody(intent.getExtras().getString("gcm.notification.body"));
                } else if (intent.getExtras().get("body") != null)  {
                    notification.setBody(intent.getExtras().getString("body"));
                }

                //RSB. FCM Multiuser Multidevice
                String notificationType = "";
                String title = "";
                String body = "";
                String orderId = "";
                if(intent.getExtras().get("gcm.notification.notificationType") != null) {
                    notificationType = intent.getExtras().getString("gcm.notification.notificationType");
                    title = intent.getExtras().getString("gcm.notification.title");
                    body = intent.getExtras().getString("gcm.notification.body");
                    orderId = intent.getExtras().getString("gcm.notification.orderId");
                } else if (intent.getExtras().get("notificationType") != null) {
                    notificationType = intent.getExtras().getString("notificationType");
                    title = intent.getExtras().getString("title");
                    body = intent.getExtras().getString("body");
                    orderId = intent.getExtras().getString("orderId");
                }
                Log.d(TAG,"NotificationType: " + notificationType);
                switch (notificationType){
                    //Estas son notificaciones de multiusuario
                    case Globals
                            .NotificationType.NOTIFICATION_LINK_REQUEST: //Usuario admin recibe notificación de operador intentando asociarse
                    case Globals.NotificationType.NOTIFICATION_UNLINK_MESSAGE: //Usuario admin recibe notificación de operador se ha desvinculado por si mismo
                    case Globals.NotificationType.NOTIFICATION_BASIC_USER: //Usuario admin recibe notificación se ha quedado sin operadores
                        persistNotification = true;
                        if (fcmMultiFunction != null) {
                            fcmMultiFunction.execute(notificationType,notification.getBody());
                        }
                        break;
                    //Estas son data de multiusuario, los data no generan notificación automática
                    case Globals
                            .NotificationType.DATA_LINK:  //Usuario operador recibe data y hace logout porque el admin ha aceptado la asociación a su cuenta
                    case Globals
                            .NotificationType.DATA_PRIVILEGES: //Usuario operador recibe data y hace logout porque el admin cambio sus privilegios
                    case Globals
                            .NotificationType.DATA_UNLINK: //Usuario operador recibe data y hace logout por que el patrón lo elimino de su cuenta
                        AppPreferences.setCloseSessionFlag("0",notification.getBody());
                        persistNotification = false;
                        try {
                            if (fcmMultiFunction != null)
                                fcmMultiFunction.execute(notificationType,notification.getBody());
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                    case DATA_ON_DEMAND:
                        sendBroadcast(body);
                        return;
                    case NOTIFICATION_NEW_ORDER:
                        persistNotification = true;
                        AppPreferences.saveNewOrder(orderId, NOTIFICATION_NEW_ORDER);
                        sendOrderBroadcast(orderId, NOTIFICATION_NEW_ORDER);
                        break;
                    case NOTIFICATION_ACCEPT_ORDER:
                        persistNotification = false;
                        AppPreferences.saveNewOrder(orderId, NOTIFICATION_ACCEPT_ORDER);
                        sendOrderBroadcast(orderId, NOTIFICATION_ACCEPT_ORDER);
                        break;
                    case NOTIFICATION_DECLINE_ORDER:
                        persistNotification = false;
                        AppPreferences.saveNewOrder(orderId, NOTIFICATION_DECLINE_ORDER);
                        sendOrderBroadcast(orderId, NOTIFICATION_DECLINE_ORDER);
                         break;
                    case Globals.NotificationType.CHAMBITA_ON_DEMAND:
                        persistNotification = true;
                        break;
                }
                notification.setStatus(true);
                if(!Tools.getModel().equals("N86")) showMessage(notification.getTitle(), notification.getBody());
                //RSB. Se valida si debe persistir la notificacion o no
                if(persistNotification){

                    //Si se debe persistir la notificacion debe redireccionar a la seccion de notificaciones
                    //220427 se solicita quitar esta funcion implementada en sprint 7.5
                    //AppPreferences.setAppLink(Globals.F_NOTIFICACIONES);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    notification.setDate(dateFormat.format(new Date()));
                    MyOpenHelper db =new MyOpenHelper(context);
                    db.insertNotification(notification);
                    // AppPreferences.addNotification(notification);
                    Log.e(TAG, "size : " + db.getNotifications().size() );
                    try {
                        if(iFunction != null)
                            iFunction.execute();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

        }
    }

    private void sendBroadcast(String campaignId) {
        Intent intent = new Intent("tconecta.sendOnDemand");
        intent.putExtra("tconecta.onDemand", "onDemand");
        intent.putExtra("tconecta.campaignId", campaignId);
        context.sendBroadcast(intent);
    }

    private void sendOrderBroadcast(String orderId, String value) {
        Intent intent = new Intent("tconecta.sendNewOrder");
        intent.putExtra("tconecta.newOrder", value);
        intent.putExtra("tconecta.orderId", orderId);
        context.sendBroadcast(intent);
    }

    public void saveNotificationCoDi(Object object) {
        try {
            HDatabase db = new HDatabase(context);
            db.createOrUpdate(object);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showMessage(String title, String body) {

        if(!CApplication.isExecute(context)) {
            AppPreferences.isBackground(AppPreferences.isLogin());
            return;
        }

        NotificationManagerCompat.from(context).cancelAll();

        Intent intentOpen = new Intent();
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentOpen);

        Intent intent = new Intent(context, MenuActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        String channelId = context.getResources().getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.icon_home_app).setContentTitle(title).setContentText(body)
                .setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent).setOnlyAlertOnce(true);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1234567890, notificationBuilder.build());

    }

}