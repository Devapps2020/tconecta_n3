package com.blm.qiubopay;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.N3ConfigHelper;
import com.blm.qiubopay.listeners.connectionreport.IConnectionReport;
import com.blm.qiubopay.listeners.fiado.IRecordatorio;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.connectionreport.QPAY_Report_Response;
import com.blm.qiubopay.models.fiado.QPAY_Recordatorio_Request;
import com.blm.qiubopay.models.fiado.QPAY_Recordatorio_Response;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.modules.fiado.Fragment_fiado_3;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.WSHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.blm.qiubopay.activities.SplashActivity;
import com.blm.qiubopay.helpers.AppImages;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.helpers.interfaces.ITimerApp;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.utils.Globals;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.devapps.utils.BuildConfig;
import mx.devapps.utils.HApplication;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.DeviceInfo;

public class CApplication extends HApplication {

    private static FirebaseAnalytics mFirebaseAnalytics;

    private static CApplication  sCApplication;

    public static CApplication getApplication() {
        return sCApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    private static Context appContext;

    private static LastLocation lastLocation;

    public DeviceEngine deviceEngine;

    public DeviceInfo deviceInfo;

    public enum ACTION {
        CB_inicia_app,
        CB_acceder_cuenta,
        CB_crear_cuenta,
        CB_capturar_informacion_personal,
        CB_capturar_informacion_negocio,
        CB_terminos_condiciones,
        CB_capturar_numero_celular,
        CB_capturar_contrasenia,
        CB_registro_exitoso,
        CB_registro_no_exitoso,
        CB_login_exitoso,
        CB_RECOLECCION_BIMBO_noexitoso_RBF03_c,
        CB_login_no_exitoso,
        CB_registrar_pin,
        CB_confirmar_pin,
        CB_inicio_sesion,
        CB_deposito_exitoso,
        CB_recoleccion_bimbo_exitoso,
        CB_retiro_efectivo,
        CB_pago_tarjeta,
        CB_dispositivo_registrado,
        CB_migracion_kineto_exitoso,
        CB_migracion_kineto_no_exitoso,

        MENU_MULTIUSUARIO,
        CAJERO,
        PATRON,
        OPCION,
        MARCA,
        CATEGORIA,
        PRODUCTO,
        MONTO,
        PARA_MI,
        PARA_TODOS,
        REPARTIDOR,
        NUMERO_PRODUCTOS,
        VALOR,

        CB_PEDIDOS_inician,
        CB_PEDIDOS_solicitan,

        CB_CARRO_COMPRAS_click,
        CB_CARRO_COMPRAS_solicitan, //NUMERO_PRODUCTOS
        CB_CARRO_COMPRAS_agregan,
        CB_CARRO_COMPRAS_eliminan,

        CB_PEDIDOS_CREDITO_PESITO_solicitan,

        CB_CREDITO_PESITO_inician,
        CB_CREDITO_PESITO_seleccionan, // REPARTIDOR
        CB_CREDITO_PESITO_solicitan, //REPARTIDOR

        CB_GANA_MAS_inician,
        CB_GANA_MAS_aplican, //PARA_MI, PARA_TODOS

        CB_PAGOS_REGIONALES_inician,
        CB_PAGOS_REGIONALES_qr,
        CB_PAGOS_REGIONALES_pagan, //OPCION, MONTO

        CB_PAGOS_RECARGAS_inician,
        CB_PAGOS_RECARGAS_pagan, //OPCION, MONTO

        CB_PAGOS_SERVICIOS_inician,
        CB_PAGOS_SERVICIOS_pagan, //OPCION, MONTO

        CB_RECOLECCION_BIMBO_inician,
        CB_RECOLECCION_BIMBO_finalizan,
        CB_RECOLECCION_BIMBO_confirmar_RBF02,

        CB_DEPOSITO_BANCARIO_inician,
        CB_DEPOSITO_BANCARIO_finalizan,

        CB_PAGOS_TARJETA_inician,
        CB_PAGOS_TARJETA_cobran,

        CB_RETIRO_EFECTIVO_inician,
        CB_RETIRO_EFECTIVO_retiran,

        CB_CANCELACION_inician,
        CB_CANCELACION_cancelan,

        CB_QUEREMOS_ESCUCHARTE_inician,
        CB_QUEREMOS_ESCUCHARTE_envian,

        CB_ASISTENCIA_TECNICA_inician,
        CB_ASISTENCIA_TECNICA_envian,

        CB_DATOS_PERSONALES_inician,
        CB_DATOS_PERSONALES_finalizan,

        CB_CAMBIAR_PIN_inician,
        CB_CAMBIAR_PIN_cambian,

        CB_MULTIUSUARIO_inician,
        CB_MULTIUSUARIO_seleccionan, //OPCION

        CB_AGREGAR_CAJERO_inician,
        CB_AGREGAR_CAJERO_comparten,

        CB_AGREGAR_PATRON_inician,
        CB_AGREGAR_PATRON_agregan,

        CB_MENU_SUPERIOR_HOME_click,
        CB_MENU_SUPERIOR_VAS_click,
        CB_MENU_SUPERIOR_NOTIFICACIONES_click,
        CB_MENU_SUPERIOR_PERFIL_click,

        CB_MENU_INFERIOR_DEPOSITAR_click,
        CB_MENU_INFERIOR_TRANSACCIONES_click,
        CB_MENU_INFERIOR_REPORTES_click,
        CB_MENU_INFERIOR_SOPORTE_click,

        CB_MENU_INFERIOR_CREDITO_PERSONAL_click,
        CB_MENU_INFERIOR_RECARGAS_PAGOS_click,
        CB_MENU_INFERIOR_QUEREMOS_ESCUCHARTE_click,
        CB_MENU_INFERIOR_ASISTENCIA_TECNICA_click,

        CB_HOME_INCREMENTA_LAS_VENTAS_click,
        CB_HOME_GANA_MAS_click,
        CB_HOME_MAS_BENEFICIOS_click,
        CB_HOME_JUEGA_GANA_click,
        CB_HOME_MIS_PUNTOS_BIMBO_click,

        CB_HOME_AUMENTA_PEDIDO_click, //MARCA, CATEGORIA, PRODUCTO
        CB_ELIGE_MARCA_buscan, //MARCA, CATEGORIA, PRODUCTO

        CB_EVALUANOS_inician,
        CB_EVALUANOS_seleccionan, //REPARTIDOR
        CB_EVALUANOS_califican, //REPARTIDOR

        CB_JUEGA_GANA_inician,

        CB_PUNTOS_BIMBO_inician,

        CB_REPORTES_inician,
        CB_REPORTE_consultan, //OPCION

        CB_MIS_TRANSACCIONES_inician,
        CB_MIS_TRANSACCIONES_consultan, //OPCION

        CB_REGISTRO_FINANCIERO_inician,
        CB_REGISTRO_FINANCIERO_nueva_cuenta,
        CB_REGISTRO_FINANCIERO_tengo_cuenta,

        CB_NOTIFICACIONES_inician,
        CB_NOTIFICACIONES_eliminan,

        CB_PERFIL_inician,
        CB_PERFIL_seleccionan, //OPCION

        CB_FINCOMUN_Inicio_V2,
        CB_FINCOMUN_Simulador_credito, //OPCION
        CB_FINCOMUN_Solicitud_credito, //OPCION
        CB_FINCOMUN_Confirma_credito, //OPCION
        CB_FINCOMUN_ORIGINACION_OFERTA,
        CB_FINCOMUN_ORIGINACION_SIMULADOR,
        CB_FINCOMUN_ORIGINACION_DETALLE_CREDITO,
        CB_FINCOMUN_ORIGINACION_FOTOS,
        CB_FINCOMUN_ORIGINACION_BENEFICIARIOS,
        CB_FINCOMUN_ORIGINACION_CONFIRMACION_SOLICITUD,
        CB_FINCOMUN_ORIGINACION_EXITO_SOLICITUD,

        Market_HazTuPedido,
        Market_Categorias,
        Market_EligeTusProductos,
        Market_ConfirmaTuPedido,
        Market_TicketDePedido,
        Market_MisPedidos,
        CB_ABONO_SALDO_ASF00
    }

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        super.onConfig(SplashActivity.class);

        HActivity.setIcAlert(R.drawable.icon_app_tconecta);
        HActivity.setItemAlert(R.layout.view_alert_modal);
        HActivity.setItemAlertLoading(R.layout.item_alert_loading_bimbo);

        HActivity.setOnFragmentChange(new IFunction<String>() {
            @Override
            public void execute(String[] data) {
                //setAnalytics(data[0]);
            }
        });

        appContext = getApplicationContext();

        initLogger();

        AppPreferences.Init(this);
        AppImages.init(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setUserProperty(1);

        if(Tools.isN3Terminal()) {
            deviceEngine = APIProxy.getDeviceEngine(this);
            deviceInfo = deviceEngine.getDeviceInfo();
            //20210427 RSB. Homologacion. Configura la n3
            new N3ConfigHelper(this,true);
        }

        setAnalytics(ACTION.CB_inicia_app);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful())
                            return;

                        String token = task.getResult().getToken();
                        Log.e("token 1", token);
                        AppPreferences.setFCM(token);

                    }
                });

        HTimerApp.getTimer().setTimer(15, new ITimerApp() {
            @Override
            public void start(HActivity context) {

            }
            @Override
            public void finish(final HActivity context) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initLogger(){

        Logger.addLogAdapter(new AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder()
                        .showThreadInfo(true)
                        .methodCount(1)
                        .tag(getResources().getString(R.string.app_tag))
                        .build()) {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        Logger.addLogAdapter(new AndroidLogAdapter(
                CsvFormatStrategy.newBuilder()
                        .logStrategy(new LogStrategy() {
                            @Override
                            public void log(int priority, @Nullable String tag, @NonNull String message) {
                                //Registrar en log, etc
                            }
                        })
                        .tag(getResources().getString(R.string.app_name))
                        .build()));

    }

    public static void setAnalytics(String action){

        if(!AppPreferences.getAnalytics_activation()) {
            return;
        }

        action = action.replace("Fragment_", "").toUpperCase();

        try {

            Bundle bundle = new Bundle();
            bundle.putString("ENVIRONMENT", Globals.environment.name());

            if(AppPreferences.isPinRegistered()) {
                bundle.putString("BLMID", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());
            }

            mFirebaseAnalytics.logEvent(action, bundle);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void setAnalytics(ACTION action){

        if(!AppPreferences.getAnalytics_activation()) {
            return;
        }

        try{

            Bundle bundle = new Bundle();
            bundle.putString("ENVIRONMENT", Globals.environment.name());

            if(AppPreferences.isPinRegistered()) {
                bundle.putString("BLMID", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());
            }

            mFirebaseAnalytics.logEvent(action.name(), bundle);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void setAnalytics(ACTION action, Map<String, String> data){

        if(!AppPreferences.getAnalytics_activation()) {
            return;
        }

        try{

            Bundle bundle = new Bundle();
            bundle.putString("ENVIRONMENT", Globals.environment.name());

            if(AppPreferences.isPinRegistered()) {
                bundle.putString("BLMID", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());
            }

            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString( entry.getKey(),  entry.getValue().replace("_"," ").toUpperCase());
            }

            mFirebaseAnalytics.logEvent(action.name(), bundle);

            bundle.putString( "TAG",  action.name());

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void setUserProperty(int property){

        if(!AppPreferences.getAnalytics_activation()) {
            return;
        }

        mFirebaseAnalytics.setUserProperty("QPAY_property_user", (property + ""));

        if(AppPreferences.getUserProfile() != null)
            mFirebaseAnalytics.setUserProperty("Qpay_bimbo_id", (AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() + "") );

    }

    public static void generateNewFCMToken() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("registerCloudMessaging", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String token = task.getResult().getToken();
                                    System.out.println("TOKENIZER "+token);
                                    Log.d("FCM_DATA", "App Token: " + token);

                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static LastLocation getLastLocation() {
        return lastLocation;
    }

    public static void setLastLocation(LastLocation lastLocation) {
        CApplication.lastLocation = lastLocation;
    }

    public static boolean isExecute(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (context.getPackageName().equalsIgnoreCase(info.processName) && ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == info.importance)
                return true;
        }
        return false;
    }
}
