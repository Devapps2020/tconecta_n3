package com.blm.qiubopay.activities;

import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_ACCEPT_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_DECLINE_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_NEW_ORDER;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.activities.orders.OrderActivity;
import com.blm.qiubopay.connection.ApiEnko;
import com.blm.qiubopay.connection.EnkoRetrofitClient;
import com.blm.qiubopay.evo.EvoController;
import com.blm.qiubopay.fragments.FullScreenFragment;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.listeners.IBalanceInquiry;
import com.blm.qiubopay.listeners.IGetActiveCampaignNew;
import com.blm.qiubopay.listeners.IGetSalesByRetailerId;
import com.blm.qiubopay.listeners.IGetTipsAdvertising;
import com.blm.qiubopay.listeners.IPromotions;
import com.blm.qiubopay.listeners.connectionreport.IConnectionReport;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.LastLogin;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryPetition;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryResponse;
import com.blm.qiubopay.models.bimbo.PromotionRequest;
import com.blm.qiubopay.models.bimbo.PromotionResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignNewCountResponse;
import com.blm.qiubopay.models.connectionreport.QPAY_Report_Response;
import com.blm.qiubopay.models.enko.EnkoUser;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.QPAY_LinkedUserStatus;
import com.blm.qiubopay.models.QPAY_LinkedUsersResponse;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailerResponse;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertisingNewCountResponse;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertisingResponse;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.modules.Fragment_aprende_1;
import com.blm.qiubopay.modules.Fragment_ofertas_bimbo_1;
import com.blm.qiubopay.modules.Fragment_registro_financiero_cus_2;
import com.blm.qiubopay.modules.Fragment_transacciones_multi_1;
import com.blm.qiubopay.modules.campania.Fragment_tips;
import com.blm.qiubopay.modules.reportes.Fragment_menu_reportes;
import com.blm.qiubopay.modules.tienda.Fragment_menu_ganancias;
import com.blm.qiubopay.modules.Fragment_pago_financiero_N3;
import com.blm.qiubopay.modules.tienda.Fragment_mis_compras;
import com.blm.qiubopay.services.FCMBackgroundService;
import com.blm.qiubopay.utils.SoundUtils;
import com.chaos.view.PinView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.nexgo.oaf.api.communication.Communication;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.notification.MyOpenHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IBalance;
import com.blm.qiubopay.listeners.ICatalogInfo;
import com.blm.qiubopay.listeners.IDongleCost;
import com.blm.qiubopay.listeners.IFCToken;
import com.blm.qiubopay.listeners.IFinancialVas;
import com.blm.qiubopay.listeners.IGetCommissionReport;
import com.blm.qiubopay.listeners.IGetLastFinancialTransactions;
import com.blm.qiubopay.listeners.IGetQiuboPaymentList;
import com.blm.qiubopay.listeners.IGetSellerUser;
import com.blm.qiubopay.listeners.IGetTodayTotalAmount;
import com.blm.qiubopay.listeners.ILogin;
import com.blm.qiubopay.listeners.ISendActivationCodeToPhone;
import com.blm.qiubopay.models.CsHeader;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Balance;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.QPAY_CommissionReport;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_Privileges;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_TodayTotalAmount;
import com.blm.qiubopay.models.QPAY_TodayTotalAmountResponse;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.CatalogoInfoDTO;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.bimbo.SellerUserRequest;
import com.blm.qiubopay.models.bimbo.SellerUserResponse;
import com.blm.qiubopay.models.bimbo.TokenDTO;
import com.blm.qiubopay.models.dongle.QPAY_DongleCostResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsResponse;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentListResponse;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecordResponse;
import com.blm.qiubopay.models.profile.QPAY_Profile;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.Fragment_cancelacion_1;
import com.blm.qiubopay.modules.Fragment_compra_dongle_1;
import com.blm.qiubopay.modules.Fragment_registro_financiero_cus_1;
import com.blm.qiubopay.modules.Fragment_soporte_bimbo_1;
import com.blm.qiubopay.modules.financiero.Fragment_conecta_dispositivo;
import com.blm.qiubopay.modules.financiero.Fragment_pago_financiero_tarjeta;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_0;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_2;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_3;
import com.blm.qiubopay.modules.fincomun.login.Fragment_fincomun_login;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_1;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_2;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_3;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_originacion_simulador;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_fincomun_1;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_fincomun_2;
import com.blm.qiubopay.modules.fincomun.retiro.Fragment_retiro_tarjeta_fincomun_2;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.home.Fragment_menu_inicio;
import com.blm.qiubopay.modules.login.Fragment_login_pin;
import com.blm.qiubopay.modules.reportes.Fragment_menu_reportes;
import com.blm.qiubopay.modules.tienda.Fragment_menu_tienda;
import com.blm.qiubopay.modules.login.Fragment_login_pin;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCCreditoOXXORequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCPlanPagosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAnalisisRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBeneficiariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBuscarSolicitudesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCConsultaOfertaBimboRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.Solicitud.FCAddInfoUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.Solicitud.FCConsultaCreditosRequest;
import mx.com.fincomun.origilib.Http.Request.Recompras.FCListaRecomprasRequest;
import mx.com.fincomun.origilib.Http.Request.Recompras.FCRecompraCreditoRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCConsultaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Http.Response.Catalogos.FCCatalogosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCCreditoOXXOResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCPlanPagosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAnalisisResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBuscarSolicitudesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCConsultaOfertaBimboResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCAddInfoUsuarioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Http.Response.Recompras.FCListaRecomprasResponse;
import mx.com.fincomun.origilib.Http.Response.Recompras.FCRecompraCreditoResponse;
import mx.com.fincomun.origilib.Model.Catalogos.AllCatalogos;
import mx.com.fincomun.origilib.Model.FCENVIRONMENT;
import mx.com.fincomun.origilib.Model.InitEnvironmentFC;
import mx.com.fincomun.origilib.Model.Originacion.Analisis;
import mx.com.fincomun.origilib.Model.Originacion.BuscarSolicitud;
import mx.com.fincomun.origilib.Model.Originacion.Credito.PlanPagos;
import mx.com.fincomun.origilib.Model.Originacion.Credito.ReferenciaOXXO;
import mx.com.fincomun.origilib.Model.Originacion.OfertaBimbo;
import mx.com.fincomun.origilib.Model.Originacion.Solicitud.AgregarUsuarioBimbo;
import mx.com.fincomun.origilib.Model.Originacion.Solicitud.ConsultaCredito;
import mx.com.fincomun.origilib.Model.Originacion.Version.VersionProd;
import mx.com.fincomun.origilib.Model.Recompras.ListaRecompras;
import mx.com.fincomun.origilib.Model.Recompras.RecompraCredito;
import mx.com.fincomun.origilib.Model.SDKFC;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosArchivo;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosBeneficiarios;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosCliente;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosDirecciones;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosIne;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosNegocio;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosPersonales;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosReferencia;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosSimulador;
import mx.com.fincomun.origilib.Objects.DHBeneficiario;
import mx.com.fincomun.origilib.Objects.Recompras.DHMovimientosRecompras;
import mx.com.fincomun.origilib.Objects.Recompras.DHPorcentajesRecompras;
import mx.com.fincomun.origilib.Objects.Referencia.DHReferencia;
import mx.com.fincomun.origilib.Objects.Solicitud.DHSolicitud;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HWebService;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.ILongOperation;
import mx.devapps.utils.interfaces.IRequestPermissions;

import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends HLocActivity {

    private String TAG = "MenuActivity";
    public IFunction device_action = null;
    public Communication communication = null;
    public boolean isConnected = false;
    public boolean isDisconnectedFromSuccessfulTransaction = false;
    private HCoDi hCoDi;
    private Dialog progress;
    protected int index = 0;
    public EvoController evoController;


    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public static IFunction<String> alertCodi ;

    private AlertDialog alertFC=null, alertOCR = null;

    private CardView layout_menu_home;
    private FloatingActionButton btn_float_help;

    public static IFunction validacionFinanciera;
    public static IFunction pagoTarjeta;
    public static IFunction despuesPago;

    //Variable para deep link, 1ra fase chambitas
    public static String appLink;
    public static boolean active = false;
    public AlertDialog alertDialog;

    //2021-12-14 RSB. Por ahora se requiere para no generar consultas constantes
    public static boolean countChambitas;
    public static boolean countPublicity;
    public boolean wasCouponDetailsOpen = false;
    public boolean wasCouponDetailsOpenBadgeTest = false;

    public FullScreenFragment fullScreenFragment;


    public IFunction<Bitmap> setImage = new IFunction<Bitmap>() {
        @Override
        public void execute(Bitmap... data) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        evoController = null;

        if(!AppPreferences.isLogin()) {
            getContext().startActivity(LoginActivity.class, true);
            return;
        }
        getContext().setHostFragmentId(R.id.nav_host_fragment);
        configMenuBottom();
        initMenuBottom();
        configAlertCodi();
        buildFirebaseFunctions();
        SDKFC.getVersion();
        switch (Globals.environment){
            case QA:
                InitEnvironmentFC.getInstance(FCENVIRONMENT.QA);
                break;
            case PRD:
                InitEnvironmentFC.getInstance(FCENVIRONMENT.PROD);
                break;
        }



        //QPAY_UserProfile user = AppPreferences.getUserProfile();
        //user.getQpay_object()[0].setQpay_bimbo_id("910652315");
        //user.getQpay_object()[0].setQpay_mail("luis.flores@zappyit.com.mx");user.getQpay_object()[0].setQpay_cellphone("5584524895");
        //AppPreferences.setUserProfile(user);


        //validatePerfil();

        //initFC();

    }

    public void configAlertCodi() {

        alertCodi = new IFunction<String>() {
            @Override
            public void execute(String... data) {

                try {

                    showAlertCODI(data[0]);

                    NotificationManagerCompat.from(getContext()).cancelAll();

                } catch (Exception ex) {

                }

            }
        };

    }

    public void configMenuBottom() {

        String tags [] = {"NavHostFragment"};

        getContext().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                String tag = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getClass().getSimpleName();
                if(Arrays.asList(tags).contains(tag)) {
                    showMenuBottom();
                    setNotificacions(findViewById(R.id.numero_notification));
                } else {
                    hideMenuBottom();
                }
            }
        });

    }

    public void initMenuBottom() {

        layout_menu_home = getContext().findViewById(R.id.layout_menu_home);
        btn_float_help = getContext().findViewById(R.id.btn_float_help);

        btn_float_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_soporte_bimbo_1.newInstance());
            }
        });

        ArrayList<ImageView> options = new ArrayList();
        ArrayList<TextView> texts = new ArrayList();

        options.add(getContext().findViewById(R.id.img_inicio));
        options.add(getContext().findViewById(R.id.img_pedidos));
        options.add(getContext().findViewById(R.id.img_pagos));
        options.add(getContext().findViewById(R.id.img_tickets_bimbo));

        texts.add(getContext().findViewById(R.id.txt_inicio));
        texts.add(getContext().findViewById(R.id.txt_pedidos));
        texts.add(getContext().findViewById(R.id.txt_pagos));
        texts.add(getContext().findViewById(R.id.txt_tickets_bimbo));

        options.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(MenuActivity.class, true);
                activeMenuBottom(0, options, texts);
            }
        });

        texts.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(MenuActivity.class, true);
                activeMenuBottom(0, options, texts);
            }
        });

        options.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().setFragment(Fragment_menu_tienda.newInstance(), true);
                activeMenuBottom(1, options, texts);

            }
        });

        texts.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().setFragment(Fragment_menu_tienda.newInstance(), true);
                activeMenuBottom(1, options, texts);

            }
        });

        options.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppPreferences.isAdmin()){
                    getCashiers();
                } else {
                    getContext().setFragment(Fragment_menu_reportes.newInstance(), true);
                }

                activeMenuBottom(2, options, texts);
            }
        });

        texts.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppPreferences.isAdmin()){
                    getCashiers();
                } else {
                    getContext().setFragment(Fragment_menu_reportes.newInstance(), true);
                }

                activeMenuBottom(2, options, texts);
            }
        });

        options.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //220327 RSB. Acceso directo a Tickets Bimbo
                /*validateVersion(new IFunction(){
                    @Override
                    public void execute(Object[] data) {
                        activeMenuBottom(3, options, texts);
                        validateData();
                    }
                });*/
                getTicketsBimbo();
            }
        });

        texts.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //220327 RSB. Acceso directo a Tickets Bimbo
                getTicketsBimbo();
            }
        });

        //220327 RSB. Acceso directo a Tickets Bimbo
        LinearLayout llTicketsBimbo = getContext().findViewById(R.id.ll_tickets_bimbo);
        if(AppPreferences.getUserProfile()!=null){
            String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
            if(bimboId!=null && !bimboId.isEmpty()){
                llTicketsBimbo.setVisibility(View.VISIBLE);
            } else {
                llTicketsBimbo.setVisibility(View.GONE);
                ViewGroup.LayoutParams p = btn_float_help.getLayoutParams();
                if (p instanceof RelativeLayout.LayoutParams) {
                    Resources r = getContext().getResources();
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            60,
                            r.getDisplayMetrics()
                    );
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)p;
                    lp.setMargins(lp.leftMargin,lp.topMargin,lp.rightMargin,px);
                    btn_float_help.setLayoutParams(lp);
                }
            }
        }

        getContext().setDefaultBack(new IFunction() {
            @Override
            public void execute(Object[] data) {
                options.get(0).performClick();
            }
        });

        getContext().setFinishBack(new IFunction() {
            @Override
            public void execute(Object[] data) {
                //20210427 RSB. Homologacion. Valida salir n3
                if(getContext().validateCloseTaps()){
                    getContext().alert(R.string.alert_message_close_app, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cerrar";
                        }
                        @Override
                        public void onClick() {
                            getContext().finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }
                        @Override
                        public void onClick() {

                        }
                    });
                }

            }
        });
    }

    //220327 RSB. Acceso directo a Tickets Bimbo
    private void getTicketsBimbo(){
        getCompras(new IFunction<QPAY_SalesRetailerResponse>() {
            @Override
            public void execute(QPAY_SalesRetailerResponse... data) {
                Fragment_mis_compras.list = Arrays.asList(data[0].getQpay_object());

                if(Fragment_mis_compras.list == null || Fragment_mis_compras.list.isEmpty()){
                    getContext().alert("No se encontraron compras");
                    return;
                }

                getContext().setFragment(Fragment_mis_compras.newInstance());
            }
        });
    }

    private void validateData() {
        validatePerfil(new IFunction(){
            @Override
            public void execute(Object[] data) {
                String value = String.valueOf(data[0]);
                switch (value){
                    case "OFERTA":
                        setFragment(Fragment_fincomun_oferta.newInstance());
                        break;
                    default:
                        setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                        break;
                }

            }
        });
    }

    public void activeMenuBottom(int option, List<ImageView> options, List<TextView> texts) {

        for (TextView txt : texts)
            txt.setTextColor(getContext().getResources().getColor(R.color.carolina_blue));

        for (ImageView img : options)
            img.setColorFilter(ContextCompat.getColor(getContext(), R.color.carolina_blue));

        texts.get(option).setTextColor(getContext().getResources().getColor(R.color.clear_blue));
        options.get(option).setColorFilter(ContextCompat.getColor(getContext(), R.color.clear_blue));

    }

    public void showMenuBottom() {
        btn_float_help.setVisibility(View.VISIBLE);
        layout_menu_home.setVisibility(View.VISIBLE);
    }

    public void hideMenuBottom() {
        btn_float_help.setVisibility(View.GONE);
        layout_menu_home.setVisibility(View.GONE);
    }

    /*NUEVO SERVICIO DE CARGA DE SALDO*/
    public void cargarSaldo(boolean getVasBalance,
                            boolean getBenefictsBalance,
                            boolean getFinancialBalance,
                            final IFunction function){
        getContext().loading(true);

        try {

            QPAY_BalanceInquiryPetition petition = new QPAY_BalanceInquiryPetition();

            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            petition.setQpay_mail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
            petition.setQpay_balance_vas(getVasBalance ? "1" : "0");
            petition.setQpay_balance_financial(getFinancialBalance ? "1" : "0");
            petition.setQpay_balance_benefits(getBenefictsBalance ? "1" : "0");

            IBalanceInquiry balance = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {


                    if (result instanceof ErrorResponse) {
                        getContext().loading(false);
                        if(function != null)
                            function.execute();

                        //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM UNSUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getVasTransactions().setNoExitosos(transactionsModel.getVasTransactions().getNoExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);
                    } else {

                        //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM SUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getVasTransactions().setExitosos(transactionsModel.getVasTransactions().getExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BalanceInquiryResponse.QPAY_BalanceInquiryResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BalanceInquiryResponse balanceResponse = gson.fromJson(json, QPAY_BalanceInquiryResponse.class);

                        if (balanceResponse.getQpay_response().equals("true")) {

                            if(getVasBalance) {
                                String balance = "Saldo $" + balanceResponse.getQpay_object()[0].getBalance().getBalance().replace("MXN", "");
                                AppPreferences.setKinetoBalance(balance);
                            }

                            if(getFinancialBalance) {
                                String financialBalance = "Saldo $" + balanceResponse.getQpay_object()[0].getToday().getQpay_total_txn().replace("MXN", "");
                                AppPreferences.setFinancialBalance(financialBalance);
                            }

                            if(getBenefictsBalance) {
                                String balanceComissions = "Saldo $" + balanceResponse.getQpay_object()[0].getCommissions().getTotalCommissions().replace("MXN", "");
                                AppPreferences.setBenefitsBalance(balanceComissions);
                            }

                        } else {

                            if (AppPreferences.getKinetoBalance().isEmpty()) {
                                AppPreferences.setKinetoBalance("Saldo $0.00");
                            }

                            if (AppPreferences.getFinancialBalance().isEmpty()) {
                                AppPreferences.setFinancialBalance("Saldo $0.00");
                            }

                            if (AppPreferences.getBenefitsBalance().isEmpty()) {
                                AppPreferences.setBenefitsBalance("Saldo $0.00");
                            }

                            if (!validaSesionForBalancePetition(balanceResponse.getQpay_code(), balanceResponse.getQpay_description()))
                                return;
                        }

                        final double monto = Double.parseDouble(AppPreferences.getKinetoBalance().replace("Saldo","").trim().replace(",","").replace("$",""));
                        Tools.initialVasInfo();

                        if(AppPreferences.getFinancialVasInfo().getUpdateVasAmountsToday().equals("0") && !AppPreferences.isCashier()) {
                            cargarVasFinancieroMontosMinimos(new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    Tools.setVasInfo(false, true, monto, true);

                                    Log.d("VAS financiero", "Termina consulta VAS.");

                                    if(function != null)
                                        function.execute();

                                }
                            });
                        }else{
                            Tools.setVasInfo(false, true, monto, false);
                            if(function != null)
                                function.execute();
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error);
                    if(function != null)
                        function.execute();

                    //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM UNSUCCESSFUL
                    TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                    transactionsModel.getVasTransactions().setNoExitosos(transactionsModel.getVasTransactions().getNoExitosos() + 1);
                    AppPreferences.setTodayTransactions(transactionsModel);
                }

            }, getContext());

            //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM TOTAL
            TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
            transactionsModel.getVasTransactions().setTotales(transactionsModel.getVasTransactions().getTotales() + 1);
            AppPreferences.setTodayTransactions(transactionsModel);
            balance.getBalance(petition);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            //getContext().alert(R.string.general_error_catch);
            if(function != null)
                function.execute();
        }
    }



    public void cargarSaldoBeneficios(boolean isOnlyForCheckBalance, final IFunction function){

        try {

            QPAY_CommissionReport qpay_balance = new QPAY_CommissionReport();
            qpay_balance.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetCommissionReport balance = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BalanceResponse.QPAY_BalanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_CommissionReportResponse balanceResponse = gson.fromJson(json, QPAY_CommissionReportResponse.class);

                        if (balanceResponse.getQpay_response().equals("true")) {

                            String balance = "Saldo $" + balanceResponse.getQpay_object()[0].getTotalCommissions().replace("MXN", "");
                            AppPreferences.setBenefitsBalance(balance);

                        } else {

                            if (AppPreferences.getBenefitsBalance().isEmpty()) {
                                AppPreferences.setBenefitsBalance("Saldo $0.00");
                            }

                            if(isOnlyForCheckBalance) {
                                if (!validaSesionForBalancePetition(balanceResponse.getQpay_code(), balanceResponse.getQpay_description()))
                                    return;
                            }else {
                                if (!validaSesion(balanceResponse.getQpay_code(), balanceResponse.getQpay_description()))
                                    return;
                            }
                        }

                        final double monto = Double.parseDouble(AppPreferences.getKinetoBalance().replace("Saldo","").trim().replace(",","").replace("$",""));
                        Tools.initialVasInfo();

                        if(AppPreferences.getFinancialVasInfo().getUpdateVasAmountsToday().equals("0") && !AppPreferences.isCashier()) {
                            cargarVasFinancieroMontosMinimos(new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    Tools.setVasInfo(false, true, monto, true);

                                    Log.d("VAS financiero", "Termina consulta VAS.");

                                }
                            });
                        }else{
                            Tools.setVasInfo(false, true, monto, false);
                        }

                        if(function != null)
                            function.execute(balanceResponse);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error);
                }

            }, getContext());

            balance.getCommissionReport(qpay_balance);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            //getContext().alert(R.string.general_error_catch);
        }

    }

    public void cargarVasFinancieroMontosMinimos(final IFunction function){

        getContext().loading(true);

        QPAY_FinancialVasGetAmountsPetition qpay_object = new QPAY_FinancialVasGetAmountsPetition();

        qpay_object.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        try {

            IFinancialVas petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //getContext().alert(R.string.general_error);
                        if(function != null)
                            function.execute();
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_FinancialVasGetAmountsResponse.QPAY_FinancialVasGetAmountsResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_FinancialVasGetAmountsResponse response = gson.fromJson(json, QPAY_FinancialVasGetAmountsResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            Tools.setFinancialVasAmounts(response);
                        }

                        if(function != null)
                            function.execute();

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error_catch);
                    if(function != null)
                        function.execute();
                }

            }, getContext());

            petition.getMinimumAmounts(qpay_object);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            //getContext().alert(R.string.general_error_catch);
            if(function != null)
                function.execute();
        }

    }


    public void validateTelemetryLogin(){
        //20201125 RSB. Forzar localización para N3
        if(Tools.isN3Terminal() && !AppPreferences.hasTodayLocation()){
            ((HLocActivity) getContext()).setForceLocation(true);
            ((HLocActivity) getContext()).obtainLocation(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    AppPreferences.setTodayLastLocation(CApplication.getLastLocation());
                    ((HLocActivity) getContext()).setForceLocation(false);
                }
            });
        }
        if (AppPreferences.hasTodayLocation() && !AppPreferences.todayLocationIsSent()) {
            prepareForLogin(true);
        } else if (!AppPreferences.isTodayLastLogin()) {
            prepareForLogin(false);
        }

    }


    public void prepareForLogin(final boolean sendsDate) {
        QPAY_Login loginData = new QPAY_Login(getContext());
        loginData.setQpay_mail(AppPreferences.getUserCredentials().getUser());
        loginData.setQpay_password(Tools.encodeSHA256(AppPreferences.getUserCredentials().getPwd()));
        loginData.setQpay_fcmId(AppPreferences.getFCM());
        loginData.setQpay_telemetry("1");
        if(sendsDate){
            loginData.getQpay_device_info()[0].setQpay_geo_x(String.valueOf(AppPreferences.getTodayLastLocation().getLongitude()));
            loginData.getQpay_device_info()[0].setQpay_geo_y(String.valueOf(AppPreferences.getTodayLastLocation().getLatitude()));
        }

        if(Tools.isN3Terminal()){
            //Se sobre escribe el número de serie.
            loginData.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));
            //IccId
            loginData.getQpay_device_info()[0].setQpay_icc_id(Tools.getIccId(getContext()));
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                login(loginData, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        if(sendsDate){
                            LastLocation lastLoc = AppPreferences.getTodayLastLocation();
                            if (lastLoc != null) {
                                lastLoc.setAlreadySent(true);
                                AppPreferences.setTodayLastLocation(lastLoc);
                            }
                        }
                        // Tras ser exitoso el login colocamos la fecha en la que se efectúo
                        AppPreferences.setDateLastLogin(new LastLogin(Tools.getTodayDate()));
                        QPAY_UserCredentials credentials = AppPreferences.getUserCredentials();
                        credentials.setFcmId(AppPreferences.getFCM());
                        AppPreferences.setUserCredentials(credentials);
                    }
                });
            }
        };
        runnable.run();

        //TODO. Transactions Counter. Call QTC service
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                syncTransactions(getApplicationContext());
            }
        };
        runnable2.run();
    }

    public boolean validaSesion(String code, String description, IFunction... function) {
        return validaSesion(getContext(), code, description, function);
    }

    public boolean validaSesionForBalancePetition(String code, String description, IFunction... function) {
        return validaSesionForBalancePetition(getContext(), code, description, function);
    }

    public static boolean validaSesion(HActivity context, String code, String description, IFunction... function) {

        if(code.equals("007") || code.equals("017") || code.equals("018") || code.equals("019")  || code.equals("020")){
            AppPreferences.setCloseSessionFlag("1",context.getResources().getString(R.string.message_logout));
            AppPreferences.Logout(context);
            context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    context.startActivity(LoginActivity.class, true);
                }
            });

            return false;
        } else if(description.toUpperCase().contains("CONNECTIVITY:HTTP".toUpperCase())){

             String message = "Tu dispositivo ha sido bloqueado\npor favor repórtalo vía whatsApp\n55 4212 1414";

            context.alertHTML(message, new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    if(function.length > 0)
                        function[0].execute();
                }
            });

            return false;

        } else {
            context.alert(description, new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    if(function.length > 0)
                        function[0].execute();
                }
            });
        }

        return true;
    }

    public static boolean validaSesionForBalancePetition(HActivity context, String code, String description, IFunction... function) {

        if(code.equals("007") || code.equals("017") || code.equals("018") || code.equals("019")  || code.equals("020")){
            AppPreferences.setCloseSessionFlag("1",context.getResources().getString(R.string.message_logout));
            AppPreferences.Logout(context);
            context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    context.startActivity(LoginActivity.class, true);
                }
            });

            return false;
        } else if(description.toUpperCase().contains("CONNECTIVITY:HTTP".toUpperCase())){

            String message = "Tu dispositivo ha sido bloqueado\npor favor repórtalo vía whatsApp\n55 4212 1414";

            context.alertHTML(message, new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    if(function.length > 0)
                        function[0].execute();
                }
            });

            return false;

        } else {

            if(function.length > 0)
                function[0].execute();

        }

        return true;
    }

    public String getSaldo() {

        String monto = AppPreferences.getKinetoBalance();

        if(monto == null || monto.isEmpty())
            return "$0.00";
        else
            return Utils.paserCurrency(monto.replace("Saldo", "").trim());

    }

    public String getSaldoFinanciero() {

        String monto = AppPreferences.getFinancialBalance();

        if(monto == null || monto.isEmpty())
            return "$0.00";
        else
            return Utils.paserCurrency(monto.replace("Saldo", "").trim());

    }

    public String getSaldoBeneficios() {

        String monto = AppPreferences.getBenefitsBalance();

        if(monto == null || monto.isEmpty())
            return "$0.00";
        else
            return Utils.paserCurrency(monto.replace("Saldo", "").trim());

    }

    public String getMiSaldo() {
        return getSaldo();
    }

    public String getMisBeneficios() {
        return getSaldoBeneficios();
    }

    public boolean validateUserOperation(boolean isSchedule) {

        boolean isValid = true;

        //Si el rol es cajero se valida si tiene permiso en el día y hora que solicita la operación
        if(AppPreferences.isCashier()) {

            QPAY_Privileges privileges = AppPreferences.getUserPrivileges();

            if(isSchedule) {

                String[] daysArray = privileges.getDaysArray();
                //Si no existe se cargan valores default
                if (daysArray[0] == null) {
                    for( int i=0; i<=6; i++){
                        daysArray[i] = "1";
                    }
                }
                String dayValue = daysArray[Utils.getIntDayOfWeek()];

                //Valida día
                if(dayValue.compareTo("1")==0) {

                    //Valida horario
                    String startTime = privileges.getStartTime();
                    startTime = (startTime!=null && !startTime.isEmpty() ? startTime.replace(":","") : "0000");
                    String endTime = privileges.getEndTime();
                    endTime = (endTime!=null && !endTime.isEmpty() ? endTime.replace(":","") : "2359");
                    String time = Utils.getTime().replace(":","");

                    int iStartTime = Integer.valueOf(startTime);
                    int iEndTime = Integer.valueOf(endTime);
                    int iTime = Integer.valueOf(time);

                    if( iTime < iStartTime || iTime > iEndTime ) {
                        isValid = false;
                    }

                } else {
                    isValid = false;
                }

            } else {

                String cashCollect = privileges.getCashCollection();
                cashCollect = cashCollect != null ? cashCollect.trim() : "";

                if(cashCollect.compareTo("1")==0) {
                    isValid = true;
                } else {
                    isValid = false;
                }

            }


        }

        return isValid;

    }

    public void initHome() {
        getContext().startActivity(MenuActivity.class, true);
    }

    public void authPIN(IFunction authPIN, boolean... force){

        if(force.length > 0 && force[0]) {
            Fragment_login_pin.authPIN = authPIN;
            getContext().setFragment(Fragment_login_pin.newInstance());
            return;
        }

        if(HTimerApp.getTimer().isCancel()){

            AppPreferences.isLogin(false);

            Fragment_login_pin.authPIN = authPIN;
            getContext().setFragment(Fragment_login_pin.newInstance());
        } else {
            authPIN.execute();
        }

    }

    int getQpay_activation_7 = 0;

    public void servicioFinanciero(final int servicio){

        //RSB 20191128. Corrección a Financiero
        if("1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3())
                && "1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7())){

        } else {

            if (AppPreferences.isCashier()) {
                getContext().alert(R.string.cajero_sin_acceso);
            } else {
                getContext().alert("¿Desea activar el cobro con tarjeta?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Activar";
                    }

                    @Override
                    public void onClick() {

                        /*RegisterActivity.getAllDataRecord(getContext(), new IFunction<QPAY_AllDataRecordResponse>() {
                            @Override
                            public void execute(QPAY_AllDataRecordResponse... data) {
                                getContext().setFragment(Fragment_registro_financiero_cus_1.newInstance());
                            }
                        });*/
                        //2021-12-15 RSB. Quitar opcion para seleccionar abrir cuenta e ir directamente a registrar cuenta
                        SessionApp.getInstance().setFinancialInformation(null);
                        getContext().setFragment(Fragment_registro_financiero_cus_2.newInstance());

                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Cancelar";
                    }

                    @Override
                    public void onClick() {

                    }
                });
            }

            return;
        }

        if(getQpay_activation_7 == 0){
            if(!"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7())){
                getContext().alert("Verificar estado de la cuenta", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Verificar";
                    }

                    @Override
                    public void onClick() {

                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Cancelar";
                    }

                    @Override
                    public void onClick() {
                        QPAY_Login login = new QPAY_Login(getContext());
                        login.setQpay_mail(AppPreferences.getUserCredentials().getUser());
                        login.setQpay_password(AppPreferences.getUserCredentials().getPwd());

                        //RSB_20191105. Parte del cambio de obtener información del dongle
                        //En el login se eliminan los datos del dongle para no ser actualizados por "ND"
                        //en el primer acceso
                        login.getQpay_device_info()[0].setQpay_dongle_sn(null);
                        login.getQpay_device_info()[0].setQpay_dongle_mac(null);
                        login.getQpay_device_info()[0].setQpay_dongle_firmware_version(null);
                        login.getQpay_device_info()[0].setQpay_dongle_model(null);

                        login(login, new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getQpay_activation_7 = 1;
                                servicioFinanciero(servicio);
                                getQpay_activation_7 = 0;
                            }
                        });
                    }
                });
                return;
            }

        } else {
            switch (AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7()){
                /*case "1":
                    break;*/
                case "0":
                    getContext().alert("Su cuenta aún no ha sido procesada, por favor inténtelo más tarde.");
                    return;
                case "2":
                    getContext().alert("Hubo un error al activar su cuenta financiera, por favor contacte a su administrador.");
                    return;
                case "3":{
                    getContext().alert("Su cuenta ha sido procesada con éxito, ahora es tiempo de activarla.", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            getContext().alert(getContext().getResources().getString(R.string.text_enviar_codigo) + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Enviar código";
                                }

                                @Override
                                public void onClick() {
                                    //getContext().setFragment(Fragment_registro_financiero_6.newInstance());
                                    senCode();

                                }
                            }, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Cancelar";
                                }

                                @Override
                                public void onClick() {

                                }
                            });
                        }
                    });
                }
                return;
                /*default:
                    getContext().showAlert("Hubo un error al activar su cuenta financiera, por favor contacte a su administrador.");
                    return;*/
            }
        }

        if(servicio != 0) {
            if(!Tools.isN3Terminal()) {
                if (!AppPreferences.isDeviceRegistered()) {
                    getContext().alert(getContext().getResources().getString(R.string.alert_message_register_dongle_question), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Vincular";
                        }

                        @Override
                        public void onClick() {

                            getContext().requestPermissions(new IRequestPermissions() {
                                @Override
                                public void onPostExecute() {

                                    device_action = new IFunction() {
                                        @Override
                                        public void execute(Object[] data) {

                                            switch (servicio) {
                                                case 0:
                                                    listarTransacciones();
                                                    break;
                                                case 1:
                                                    if (Tools.isN3Terminal())
                                                        getContext().setFragment(Fragment_pago_financiero_N3.newInstance(false, null));
                                                            /*else
                                                            {
                                                                if(AppPreferences.getDevice().getName().contains("SP530"))
                                                                    getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(false));
                                                                else
                                                                    getContext().setFragment(Fragment_pago_financiero_1.newInstance(false));
                                                            }*/
                                                    //getContext().setFragment(Fragment_pago_financiero_1.newInstance(false));
                                                    //getContext().setFragment(EmvActivity.newInstance(false));
                                                    break;
                                                case 2:
                                                    if (Tools.isN3Terminal())
                                                        getContext().setFragment(Fragment_pago_financiero_N3.newInstance(true, null));
                                                            /*else
                                                            {
                                                                if(AppPreferences.getDevice().getName().contains("SP530"))
                                                                    getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(true));
                                                                else
                                                                    getContext().setFragment(Fragment_pago_financiero_1.newInstance(true));
                                                            }*/
                                                    //getContext().setFragment(Fragment_pago_financiero_1.newInstance(true));
                                                    //getContext().setFragment(EmvActivity.newInstance(false));
                                                    break;
                                            }

                                        }
                                    };

                                            /*try {
                                                if(((String)theData[0]).equals("ACCEPT"))
                                                    getContext().setFragment(Fragment_conectar_dispositivo_1.newInstance());
                                                else if(((String)theData[0]).equals("BUY"))
                                                    getContext().setFragment(Fragment_compra_dongle_1.newInstance());

                                            } catch (Exception ex) {

                                                Logger.e(ex.getMessage());

                                            }*/


                                }
                            }, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.READ_PHONE_STATE});

                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Comprar lector de tarjetas";
                        }

                        @Override
                        public void onClick() {

                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }

                        @Override
                        public void onClick() {

                        }
                    });

                    return;
                }

            }
        }

        switch (servicio){
            case 0:
                listarTransacciones();
                break;
            case 1:
                if(Tools.isN3Terminal())
                    getContext().setFragment(Fragment_pago_financiero_N3.newInstance(false, null));
                /*else
                {
                    if(AppPreferences.getDevice().getName().contains("SP530"))
                        getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(false));
                    else
                        getContext().setFragment(Fragment_pago_financiero_1.newInstance(false));
                }*/
                //getContext().setFragment(EmvActivity.newInstance(false));
                break;
            case 2:
                if(Tools.isN3Terminal())
                    getContext().setFragment(Fragment_pago_financiero_N3.newInstance(true, null));
                /*else
                {
                    if(AppPreferences.getDevice().getName().contains("SP530"))
                        getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(true));
                    else
                        getContext().setFragment(Fragment_pago_financiero_1.newInstance(true));
                }*/
                //getContext().setFragment(EmvActivity.newInstance(false));
                break;
        }

    }

    private void buildMessageCashier(final int servicio) {

        //Se arma la funcion del Dispositivo
        final IFunction functionDevice = new IFunction() {
            @Override
            public void execute(Object[] data) {

                switch (servicio) {
                    case 0:
                        listarTransacciones();
                        break;
                    case 1:
                        getContext().backFragment();
                        if (Tools.getModel().equals(Globals.NEXGO_N3)) {
                            //getContext().setFragment(Fragment_pago_financiero_N3.newInstance(false));
                        } else {
                            if (AppPreferences.getDevice().getName().contains("SP530")) {
                                //getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(false));
                            } else
                                getContext().setFragment(Fragment_pago_financiero_tarjeta.newInstance(false));
                        }
                        break;
                    case 2:
                        getContext().backFragment();
                        if (Tools.getModel().equals(Globals.NEXGO_N3)) {
                            //getContext().setFragment(Fragment_pago_financiero_N3.newInstance(true));
                        } else {
                            if (AppPreferences.getDevice().getName().contains("SP530")) {
                                //getContext().setFragment(Fragment_pago_financiero_SP530.newInstance(true));
                            }  else
                                getContext().setFragment(Fragment_pago_financiero_tarjeta.newInstance(true));
                        }
                        break;
                }

            }
        };

        //Si el rol es cajero muestra vincular
        if(AppPreferences.isCashier()) {

            getContext().alert(getContext().getResources().getString(R.string.alert_message_register_dongle_question), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Vincular";
                        }

                        @Override
                        public void onClick() {

                            getContext().requestPermissions(new IRequestPermissions() {
                                @Override
                                public void onPostExecute() {
                                    device_action = functionDevice;
                                    getContext().setFragment(Fragment_conecta_dispositivo.newInstance());
                                }
                            }, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.READ_PHONE_STATE});

                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }

                        @Override
                        public void onClick() {

                        }
                    });

            //Si es otro rol muestra la opción de comprar lector
        } else {

            getContext().requestPermissions(new IRequestPermissions() {
                @Override
                public void onPostExecute() {

                    device_action = functionDevice;

                    getContext().alert(getContext().getResources().getString(R.string.alert_message_register_dongle_question), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Vincular";
                        }

                        @Override
                        public void onClick() {
                            getContext().setFragment(Fragment_conecta_dispositivo.newInstance());
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Comprar lector de tarjetas";
                        }

                        @Override
                        public void onClick() {
                            comprarDongle();
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }

                        @Override
                        public void onClick() {

                        }
                    });

                }
            }, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.READ_PHONE_STATE});

        }

    }

    public void listarTransacciones(){

        QPAY_VisaEmvRequest authSeed = new QPAY_VisaEmvRequest();
        authSeed.getCspHeader().setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        authSeed.getCspHeader().setNullObjects();
        authSeed.setCspBody(null);

        getContext().loading(true);

        try {

            IGetLastFinancialTransactions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_VisaResponse responseTxn = gson.fromJson(json, QPAY_VisaResponse.class);

                        final ArrayList<QPAY_VisaEmvResponse> cancelaciones = new ArrayList();
                        final ArrayList<QPAY_VisaEmvResponse> ventas = new ArrayList();

                        if(responseTxn.getQpay_object() == null || responseTxn.getQpay_object().length == 0){
                            getContext().alert("No existen transacciones a cancelar.");
                            return;
                        }

                        for(int i=0; i<responseTxn.getQpay_object().length;i++) {
                            if((responseTxn.getQpay_object()[i].getCspHeader().getProductId().equals("13001") || responseTxn.getQpay_object()[i].getCspHeader().getProductId().equals("13005"))) {
                                if(responseTxn.getQpay_object()[i].getCspBody().getTxDate()!=null && !responseTxn.getQpay_object()[i].getCspBody().getTxDate().trim().equals("null")) {
                                    if (responseTxn.getQpay_object()[i].getCspHeader().getTxTypeId().equals("140") || responseTxn.getQpay_object()[i].getCspBody().getAmt().toString().contains("-"))//Es una cancelación
                                        cancelaciones.add(responseTxn.getQpay_object()[i]);
                                    else{//Es una venta
                                        if("00".equals(responseTxn.getQpay_object()[i].getCspBody().getCode())
                                                || "0C".equals(responseTxn.getQpay_object()[i].getCspBody().getCode())
                                                || "0".equals(responseTxn.getQpay_object()[i].getCspBody().getCode()))
                                            ventas.add(responseTxn.getQpay_object()[i]);
                                    }
                                }
                            }
                        }

                        if(ventas.isEmpty()){
                            getContext().alert("No existen transacciones a cancelar.");
                            return;
                        }

                        responseTxn.ventas = delCancelatedTxns(ventas, cancelaciones);

                        responseTxn.cancelaciones = cancelaciones;

                        if(responseTxn.ventas.isEmpty()){
                            getContext().alert("No existen transacciones a cancelar.");
                            return;
                        }

                        if(responseTxn.getQpay_response().equals("true")){
                            getContext().setFragment(Fragment_cancelacion_1.newInstance(responseTxn));
                        } else
                            validaSesion(getContext(), responseTxn.getQpay_code(), "No existen transacciones a cancelar.");

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getLastFinancialTransactions(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    private ArrayList<QPAY_VisaEmvResponse> delCancelatedTxns(ArrayList<QPAY_VisaEmvResponse> txnList, ArrayList<QPAY_VisaEmvResponse> cancelTxnList) {

        try{

            ArrayList<QPAY_VisaEmvResponse> temporalTxnList = txnList;
            QPAY_VisaEmvResponse sellTxn;
            QPAY_VisaEmvResponse cancelTxn;
            for(int i=0; i<cancelTxnList.size(); i++)
            {
                cancelTxn = cancelTxnList.get(i);
                for(int j=0; j<txnList.size(); j++)
                {
                    sellTxn = txnList.get(j);

                    if(cancelTxn.getCspBody().getTxId().trim().equals(sellTxn.getCspHeader().getRspId().trim()))
                    {
                        //Esta venta ya fue cancelada
                        temporalTxnList.remove(j);
                    }
                }
            }
            if(txnList.size() != temporalTxnList.size())
                txnList = temporalTxnList;

        }catch (Exception e){
            return txnList;
        }

        return txnList;
    }

    public void senCode(){

        getContext().loading(true);

        try {

            QPAY_Seed seed = new QPAY_Seed();

            seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            ISendActivationCodeToPhone petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    }else{

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BaseResponse.QPAY_BaseResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse baseResponse = gson.fromJson(json, QPAY_BaseResponse.class);

                        if(baseResponse.getQpay_response().equals("true")) {

                            getContext().alert("Se ha enviado un código de activación a su celular, por favor ingrese el número para concluir el proceso.", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    //getContext().setFragment(Fragment_registro_financiero_6.newInstance());
                                }
                            });

                            AppPreferences.setMerchantActivationFlag("1");

                        }else{
                            getContext().alert("Hubo un error al enviar su código de activación. Mensaje: "+baseResponse.getQpay_description()+"");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.doSendCode(seed);

        }catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }
    }

    public void getQiuboPaymentList(IFunction function){

        final ArrayList list_txr = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetQiuboPaymentList petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_QiuboPaymentListResponse.QPAY_QiuboPaymentListExcluder()).create();
                        String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                        QPAY_QiuboPaymentListResponse response = gson.fromJson(json, QPAY_QiuboPaymentListResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            for (int i = 0; i < response.getQpay_object().length; i++) {
                                if("1".equals(response.getQpay_object()[i].getActive()))
                                    list_txr.add(response.getQpay_object()[i]);
                            }

                            function.execute(list_txr);

                        } else {
                            validaSesion(getContext(), response.getQpay_code(), response.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.doGetList(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    /*public void validateEnkoUser() {

        QPAY_Profile appPreferences = AppPreferences.getUserProfile().getQpay_object()[0];
        String blmId = appPreferences.getQpay_blm_id().trim();
        String firstName = appPreferences.getQpay_name().trim().split(" ")[0];
        String lastName = appPreferences.getQpay_father_surname().trim().split(" ")[0];
        String email = appPreferences.getQpay_mail().trim();
        String phone = appPreferences.getQpay_cellphone().trim();

        String urlEnko = AppPreferences.getEnkoUrl() != null ? AppPreferences.getEnkoUrl() : Globals.URL_ENKO;

        String strUrl = urlEnko + Globals.URL_ENKO_ONBOARDING + "&username=" + blmId + "&firstname=" + firstName  +
                "&lastname=" + lastName + "&email=" + email + "&tel=" + phone;

        HWebService service = new HWebService(getContext());
        service.setURL(strUrl);

        service.execute(new ILongOperation() {

            @Override
            public String doInBackground() {
                return service.getClient()._GET("");
            }

            @Override
            public void onPreExecute() {
                getContext().loading(true);
            }

            @Override
            public void onPostExecute(int code, String result) {

                if(code == Utils.CODE) {
                   getContext().setFragment(Fragment_aprende_1.newInstance());
                } else {
                    getContext().alert(R.string.general_error);
                }

                getContext().loading(false);

            }
        });

    }*/

    /**
     * Metodo de registro para Enko 2.0
     */
    public void registerEnko(){

        getContext().loading(true);
        ApiEnko service = EnkoRetrofitClient.getRetrofit(Globals.URL_ENKO_2).create(ApiEnko.class);
        EnkoUser enkoUser = new EnkoUser();
        QPAY_Profile appPreferences = AppPreferences.getUserProfile().getQpay_object()[0];
        String firstName = appPreferences.getQpay_name().trim().split(" ")[0];
        String lastName = appPreferences.getQpay_father_surname().trim().split(" ")[0];
        String phone = appPreferences.getQpay_cellphone().trim();
        enkoUser.setFirst_name(firstName);
        enkoUser.setLast_name(lastName);
        enkoUser.setAlly_code("qiubo");
        enkoUser.setPhone_number("+52"+phone);
        Call<EnkoUser> call = service.registerEnko(enkoUser);
        call.enqueue(new Callback<EnkoUser>() {
            @Override
            public void onResponse(Call<EnkoUser> call, Response<EnkoUser> response) {
                getContext().loading(false);
                Buffer buffer = new Buffer();
                try {
                    call.request().body().writeTo(buffer);
                    System.out.println("Register Enko Request: "+buffer.readUtf8());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        AppPreferences.setIsRegisteredEnko(true);
                        String urlEnko = Globals.URL_ENKO_2_TABS + "register?token=" + response.body().getToken();
                        getContext().setFragment(Fragment_browser.newInstanceEnko(urlEnko));
                    }else{
                        getContext().alert(R.string.general_error);
                    }
                }else if (response.code() == 400){

                    AppPreferences.setIsRegisteredEnko(true);
                    loginEnko();
                }else{
                    getContext().alert(R.string.general_error);
                }
            }

            @Override
            public void onFailure(Call<EnkoUser> call, Throwable t) {
                getContext().loading(false);
                getContext().alert(R.string.general_error);
            }
        });
    }

    public void loginEnko(){

        //context.onLoading(true);
        ApiEnko service = EnkoRetrofitClient.getRetrofit(Globals.URL_ENKO_2).create(ApiEnko.class);
        EnkoUser enkoUser = new EnkoUser();
        QPAY_Profile appPreferences = AppPreferences.getUserProfile().getQpay_object()[0];
        String firstName = appPreferences.getQpay_name().trim().split(" ")[0];
        String lastName = appPreferences.getQpay_father_surname().trim().split(" ")[0];
        String phone = appPreferences.getQpay_cellphone().trim();
        enkoUser.setFirst_name(firstName);
        enkoUser.setLast_name(lastName);
        enkoUser.setAlly_code("qiubo");
        enkoUser.setPhone_number("+52"+phone);
        Call<EnkoUser> call = service.loginEnko(enkoUser);
        call.enqueue(new Callback<EnkoUser>() {
            @Override
            public void onResponse(Call<EnkoUser> call, Response<EnkoUser> response) {
                Buffer buffer = new Buffer();
                try {
                    call.request().body().writeTo(buffer);
                    System.out.println("Login Enko Request: "+buffer.readUtf8());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //context.onLoading(false);
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        if (response.body().getToken().isEmpty()){
                            getContext().alert(R.string.general_error);
                        }else{
                            String urlEnko = Globals.URL_ENKO_2_TABS + "register?token=" + response.body().getToken();
                            getContext().setFragment(Fragment_browser.newInstanceEnko(urlEnko));
                        }
                    }else{
                        getContext().alert(R.string.general_error);
                    }
                }else{
                    getContext().alert(R.string.general_error);
                }
            }

            @Override
            public void onFailure(Call<EnkoUser> call, Throwable t) {
                getContext().loading(false);
                getContext().alert(R.string.general_error);
            }
        });
    }


    public void login(final QPAY_Login data, final IFunction function){

        getContext().loading(true);

        try {

            ILogin registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //getContext().alert(R.string.general_error);
                    }else{

                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {

                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        }else{
                            validaSesion(getContext(), userProfile.getQpay_code(), userProfile.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error);
                }
            }, getContext());

            registerUserService.login(data);

        }catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }
    }

    public void loadingConectando(boolean show, String texto, IFunction function) {

        if(show)
            getContext().loading(true, texto);
        else
            getContext().loading(false);

        if(function != null)
            function.execute();

    }

    public void loadingConectando(String texto, IAlertButton function) {

        getContext().loading(true, texto, function);

    }

    /*************************/

    public void getTokenFC(IFunction<String> function){

        getContext().loading(true);

        try {

            TokenDTO token = new TokenDTO();
            token.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IFCToken petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);
                        if (function != null) {
                            function.execute(null);
                        }
                    } else {

                        String json = new Gson().toJson(result);
                        TokenDTO response = new Gson().fromJson(json, TokenDTO.class);

                        if (response.getQpay_response().equals("true")) {

                            if (function != null)
                                function.execute(response.getQpay_object()[0].getToken());

                        } else {
                            getContext().loading(false);
                            if (function != null) {
                                function.execute(null);
                            }
                            validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    if (function != null) {
                        function.execute(null);
                    }
                }
            }, getContext());

            petition.getToken(token);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            if (function != null) {
                function.execute(null);
            }
        }

    }

    public void catalogs(IFunction function){

        getContext().loading(true);

        if(SessionApp.getInstance().getCatalog() != null) {
            getContext().loading(false);
            function.execute();
            return;
        }

        AllCatalogos catalogos = new AllCatalogos(getContext());
        catalogos.getAllCatalogos(new AllCatalogos.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCCatalogosResponse response = new Gson().fromJson( String.valueOf(Object), FCCatalogosResponse.class);
                getContext().loading(false);
                if (response != null && response.getRespuesta().getCodigo() == 0){
                    SessionApp.getInstance().setCatalog(response);
                    function.execute();
                }
            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });

    }

    public void consultaCreditos(String token,IFunction function) {
        getContext().loading(true);
        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCConsultaCreditosRequest fcConsultaCreditosRequest = new FCConsultaCreditosRequest();
        fcConsultaCreditosRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcConsultaCreditosRequest.setBimboID(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        Logger.d("REQUEST : "  + new Gson().toJson(fcConsultaCreditosRequest));

        ConsultaCredito consultaCredito = new ConsultaCredito(getContext());
        consultaCredito.postConsultaCredito(fcConsultaCreditosRequest, new ConsultaCredito.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                Logger.d("RESPONSE : "  + Object);
                getContext().loading(false);
                try {

                    FCConsultaCreditosResponse response = (FCConsultaCreditosResponse) Object;

                    Logger.d("RESPONSE : "  + new Gson().toJson(response));

                    if(response.getCodigo() > 1) {
                        getContext().alert(response.getDescripcion());

                    } else {
                        if(response == null)
                            throw new Exception();
                        else {
                            if (response.getListaCreditos() == null
                                    || response.getListaCreditos().isEmpty()){
                                consultaOfertaBimbo(token,false,function);

                            }else {
                                SessionApp.getInstance().setFcConsultaCreditosResponse(response);
                                getRecompras(token,function);
                            }

                        }
                    }

                } catch (Exception ex) {
                    FCConsultaCreditosResponse response = new FCConsultaCreditosResponse();
                    function.execute("CREDITO");
                }

            }
            @Override
            public void onFailure(String mensaje) {
                FCConsultaCreditosResponse response = new FCConsultaCreditosResponse();
                getRecompras(token,function);
                getContext().loading(false);
            }
        });


    }

    public void loadCreditos(FCConsultaCreditosResponse response,IFunction function) {
        SessionApp.getInstance().setFcConsultaCreditosResponse(response);
        getTokenFC((String... text) -> {
            consultaOfertaBimbo(text[0], (response.getListaCreditos() != null && !response.getListaCreditos().isEmpty()),function);
        });
    }

    public void consultaOfertaBimbo(String token, boolean next,IFunction function) {
        getContext().loading(true);

        FCConsultaOfertaBimboRequest fcConsultaOfertaBimboRequest = new FCConsultaOfertaBimboRequest();
        fcConsultaOfertaBimboRequest.setTokenJwt(token);
        fcConsultaOfertaBimboRequest.setBimboID(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        Logger.d("REQUEST : "  + new Gson().toJson(fcConsultaOfertaBimboRequest));

        OfertaBimbo ofertaBimbo = new OfertaBimbo(getContext());
        ofertaBimbo.postOfertaBimbo(fcConsultaOfertaBimboRequest, new OfertaBimbo.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                getContext().loading(false);

                try {

                    FCConsultaOfertaBimboResponse response = (FCConsultaOfertaBimboResponse)Object;
                    Logger.d("RESPONSE : "  + new Gson().toJson(response));

                    if(response.getOfertaBimbo() == null || response.getOfertaBimbo().isEmpty()){
                        function.execute("OFERTA");
                        getContext().loading(false);

                    } else {
                        SessionApp.getInstance().setFcConsultaOfertaBimboResponse(response);
                        if (SessionApp.getInstance().getFcRequestDTO() != null){
                            if (SessionApp.getInstance().getFcRequestDTO().getReferencias() != null){
                                SessionApp.getInstance().getFcRequestDTO().setReferencias(null);
                                SessionApp.getInstance().getFcRequestDTO().setBeneficiarios(null);
                            }
                        }
                        function.execute("OFERTA");
                        getContext().loading(false);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    getContext().loading(false);
                }
            }
            @Override
            public void onFailure(String mensaje) {
                 getContext().alert(mensaje, new IAlertButton() {
                     @Override
                     public String onText() {
                         return "Aceptar";
                     }

                     @Override
                     public void onClick() {
                        initHome();
                     }
                 });
                getContext().loading(false);
            }
        });
    }

    public void nextFincomun(FCConsultaOfertaBimboResponse response) {
        getContext().loading(false);
        SessionApp.getInstance().setFcConsultaOfertaBimboResponse(response);
        getContext().setFragment(Fragment_prestamos_fincomun_1.newInstance());
    }

    public void validatePerfil(IFunction function) {

        sethCoDi(new HCoDi(this));

        getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... token) {

                if (token != null) {
                    if (!AppPreferences.getRegisterBimboIdFc()) {
                        agregarUsusario(token[0], new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                consultaBandera(token[0], function);
                            }
                        });
                    } else {
                        consultaBandera(token[0], function);
                    }
                }

            }
        });

    }

    public void consultaBandera(String token,IFunction function) {

        gethCoDi().consultaBandera(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(), token, new mx.devapps.utils.interfaces.IFunction() {
            @Override
            public void execute(Object[] data) {
                FCConsultaBanderaFCILAperturaResponse response = (FCConsultaBanderaFCILAperturaResponse)data[0];
                if(response.getCodigo() == 0) {
                    getContext().loading(false);

                    SessionApp.getInstance().setFcBanderas(response);

                    Fragment_prestamos_fincomun_1.banderas = response;
                    HCoDi.numCliente = response.getNumCliente();

                    SessionApp.getInstance().setListaRecompras(null);
                    SessionApp.getInstance().setFcConsultaCreditosResponse(null);
                    SessionApp.getInstance().setFcBuscarSolicitudesResponse(null);
                    SessionApp.getInstance().setFcConsultaOfertaBimboResponse(null);

                    if (response.getTieneCreditos() == 0) {
                        buscarSolicitud(token, function,true);
                    } else {
                        consultaCreditos(token, function);
                    }
                }else{
                    getContext().alert(response.getDescripcion());
                }
            }
        });

    }

    public void initFC(IFunction function) {

        getTokenFC((String... text) -> {
            if (text != null) {
                consultaCreditos(text[0], function);
            }
        });

    }

    public HCoDi gethCoDi() {
        return hCoDi;
    }

    public void sethCoDi(HCoDi hCoDi) {
        this.hCoDi = hCoDi;
    }

    public void setDataSpinner(EditSpinner spiner, ArrayList<ModelSpinner> data, IFunction<String> function){

        ArrayList<String> texts = new ArrayList();

        for(ModelSpinner dat: data)
            texts.add(dat.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.item_spinner, texts);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener((parent, view, position, id) -> {
            if(function != null)
                function.execute(data.get(position).getValue());
        });
    }

    public void showAlertCODI(String text, final IClickView... listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_push_codi_1, null);

        Button btn_cerrar = view.findViewById(R.id.btn_cerrar);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if(listener.length > 0)
                    listener[0].onClick();

            }
        });

        text_mensaje.setText(text);

        builder.setView(view);
        builder.setCancelable(false);

        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.getWindow().setLayout(R.dimen._400sdp, R.dimen._600sdp); //Controlling width and height.
        alertFC.show();

        builder = null;

    }

    public void setNotificacions(TextView... numero_notification) {

        if(numero_notification.length > 0) {

            FCMBackgroundService.iFunction = new IFunction() {
                @Override
                public void execute(Object[] data) {

                    if(numero_notification[0] != null) {

                        MyOpenHelper db =new MyOpenHelper(getContext());

                        int num = db.countNotifications();

                        if(num > 0){
                            numero_notification[0].setText(num + "");
                            numero_notification[0].setVisibility(View.VISIBLE);
                        }else{
                            numero_notification[0].setVisibility(View.GONE);
                        }
                    }

                }
            };

        }

        if(FCMBackgroundService.iFunction != null)
            FCMBackgroundService.iFunction.execute();

    }

    public void getProveedores(IFunction function){

        validaBimboId(new IFunction() {
            @Override
            public void execute(Object[] data) {
                consultaProveedores(function);
            }
        });

    }

    public void consultaProveedores(IFunction function){

        if(SessionApp.getInstance().getSellerUserResponse() != null && SessionApp.getInstance().getSellerUserResponse().getQpay_object().length != 0) {
            function.execute();
            return;
        }

        getContext().loading(true);

        try {

            SellerUserRequest sellerUserRequest = new SellerUserRequest();
            sellerUserRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

            IGetSellerUser petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if(result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);
                    } else {

                        String json = new Gson().toJson(result);
                        SellerUserResponse response = new Gson().fromJson(json, SellerUserResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object() == null || response.getQpay_object().length == 0) {

                                getContext().alert(R.drawable.warning, R.color.mango, R.string.text_register_25, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return getContext().getResources().getString(R.string.accept_button);
                                    }
                                    @Override
                                    public void onClick() {

                                    }
                                });

                                return;
                            }

                            SessionApp.getInstance().setSellerUserResponse(response);
                            function.execute();

                        } else {
                            validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }
                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getSellerUser(sellerUserRequest);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void comprarDongle() {

        getContext().loading(true);

        try {

            IDongleCost sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {

                        getContext().alert(R.string.general_error, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                initHome();
                            }
                        });

                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_DongleCostResponse.QPAY_DongleCostResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_DongleCostResponse dongleCostResponse = gson.fromJson(json, QPAY_DongleCostResponse.class);

                        if (dongleCostResponse.getQpay_response().equals("true")) {

                            Fragment_compra_dongle_1.costo = dongleCostResponse.getQpay_object()[0].getCost() + "";

                            getContext().setFragment(Fragment_compra_dongle_1.newInstance());

                        } else {

                            validaSesion(dongleCostResponse.getQpay_code(), dongleCostResponse.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);

                }
            }, getContext());

            sale.doGetDongleCost();

        } catch (Exception e) {

            getContext().loading(false);
            e.printStackTrace();

            getContext().alert(R.string.general_error_catch);

        }

    }

    public void showAlertINE(int doc, boolean front, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(getContext()).inflate(front ? R.layout.item_alert_ine : R.layout.item_alert_ine_rev, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView title = view.findViewById(R.id.title);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertOCR.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        if(front) {

            if(doc == 1)
                title.setText("Captura el frente de tu INE/IFE");
            else
                title.setText("Captura el frente de tu INE/IFE");

        } else {

            if(doc == 1)
                title.setText("Ahora captura el reverso\nde tu INE/IFE");
            else
                title.setText("Ahora captura el reverso\nde tu INE/IFE");

        }
        
        builder.setView(view);
        builder.setCancelable(false);

        alertOCR = builder.create();
        alertOCR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertOCR.show();

        builder = null;

    }

    public void showPicker(int title, boolean menor, final IClickView... listener) {

        getContext().closeLoading();

        if (datePickerDialog != null && datePickerDialog.isShowing())
            datePickerDialog.dismiss();

        Date date = new Date();
        date.setYear(date.getYear() - (menor ? 18 : 0));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dia = dayOfMonth + "";
                String mes = (month + 1) + "";

                dia = dia.length() == 1 ? "0" + dia : dia;
                mes = mes.length() == 1 ? "0" + mes : mes;

                if (listener.length >= 1)
                    listener[0].onClick(dia + "/" + mes + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.setTitle(title);
        datePickerDialog.setCancelable(false);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        datePickerDialog.show();

    }

    public void showAlertLayout(int layout_id, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(layout_id, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertLayout(int layout_id, String text, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(layout_id, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView text_description = view.findViewById(R.id.text_description);

        if (text_description != null)
            text_description.setText(text);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertLayout(int layout_id, String text, String code, String code_c, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(layout_id, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView text_description = view.findViewById(R.id.text_description);

        TextView text_code = view.findViewById(R.id.text_code);
        TextView text_code_c = view.findViewById(R.id.text_code_c);


        if (text_code != null)
            text_code.setText(code);

        if (text_code_c != null)
            text_code_c.setText(code_c);

        if (text_description != null)
            text_description.setText(text);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertSMS(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_code, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        Button btn_reenviar = view.findViewById(R.id.btn_reenviar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);

        PinView pin = view.findViewById(R.id.edit_pin);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick(pin.getText().toString());

            }
        });

        btn_reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 1)
                    listener[1].onClick();

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFC.dismiss();
            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertPromotor(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_code_promotor, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);

        HEditText code = new HEditText((EditText) view.findViewById(R.id.edt_promotor_code),
                false, 50, 0, HEditText.Tipo.NUMERO, new ITextChanged() {
            @Override
            public void onChange() {

            }

            @Override
            public void onMaxLength() {

            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFC.dismiss();
                if (listener.length > 0)
                    listener[0].onClick(code.getText().toString());

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFC.dismiss();
            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertRetiro_step_1(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_retiro_1, null);

        Button btn_solicitar = view.findViewById(R.id.btn_solicitar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);

        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();
                backFragment();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void showAlertRetiro_step_2(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_retiro_2, null);

        Button btn_siguiente = view.findViewById(R.id.btn_siguiente);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);
        LinearLayout layout_otros_datos = view.findViewById(R.id.layout_otros_datos);

        CardView card_para_mi = view.findViewById(R.id.card_para_mi);
        CardView card_para_otro = view.findViewById(R.id.card_para_otro);

        ITextChanged iTextChanged = null;

        HEditText nombre = new HEditText((EditText) view.findViewById(R.id.edit_nombre),
                true, 50, 3, HEditText.Tipo.TEXTO, null);

        HEditText telefono = new HEditText((EditText) view.findViewById(R.id.edit_telefono),
                true, 10, 10, HEditText.Tipo.NUMERO, null);

        HEditText compania = new HEditText((EditText) view.findViewById(R.id.edt_compania),
                true, 50, 1, HEditText.Tipo.NONE, null);

        EditSpinner spcompania = view.findViewById(R.id.edt_compania);

        ArrayList<ModelSpinner> companias = new ArrayList();

        companias.add(new ModelSpinner("MOVISTAR"));
        companias.add(new ModelSpinner("IUSACELL"));
        companias.add(new ModelSpinner("TELCEL"));
        companias.add(new ModelSpinner("UNEFON"));
        companias.add(new ModelSpinner("NEXTEL"));

        setDataSpinner(spcompania, companias, data -> compania.setIdentifier("" + data[0]));

        layout_otros_datos.setVisibility(View.GONE);

        card_para_mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card_para_mi.setBackground(getContext().getResources().getDrawable(R.drawable.background_card_blue_ligth));
                card_para_otro.setBackground(getContext().getResources().getDrawable(R.drawable.background_round_withe));

                Fragment_retiro_tarjeta_fincomun_2.paraAlguienMas = false;

                btn_siguiente.setEnabled(false);
                btn_siguiente.setVisibility(View.VISIBLE);
                layout_otros_datos.setVisibility(View.GONE);

                nombre.setRequired(false);
                telefono.setRequired(false);

                nombre.setText("");
                telefono.setText("");
                compania.setText("");
            }
        });

        card_para_otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card_para_otro.setBackground(getContext().getResources().getDrawable(R.drawable.background_card_blue_ligth));
                card_para_mi.setBackground(getContext().getResources().getDrawable(R.drawable.background_round_withe));

                Fragment_retiro_tarjeta_fincomun_2.paraAlguienMas = true;

                btn_siguiente.setEnabled(false);
                btn_siguiente.setVisibility(View.VISIBLE);
                layout_otros_datos.setVisibility(View.VISIBLE);

                nombre.setRequired(true);
                telefono.setRequired(true);

                nombre.setText("");
                telefono.setText("");
                compania.setText("");
            }
        });

        btn_siguiente.setEnabled(false);
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick(nombre.getText(), telefono.getText(), compania.getText());

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();
                backFragment();

            }
        });

        iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {

                if (nombre.isValid() && telefono.isValid() && compania.isValid())
                    btn_siguiente.setEnabled(true);
                else
                    btn_siguiente.setEnabled(false);

            }

            @Override
            public void onMaxLength() {
            }
        };

        nombre.setiText(iTextChanged);
        telefono.setiText(iTextChanged);
        compania.setiText(iTextChanged);

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public void validaBimboId(IFunction function) {

        if(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() == null ||
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id().isEmpty()) {

            getContext().alert(R.drawable.warning, R.color.mango, R.string.text_register_24, new IAlertButton() {
                @Override
                public String onText() {
                    return getContext().getResources().getString(R.string.accept_button);
                }
                @Override
                public void onClick() {

                }
            });

        } else {
            function.execute();
        }

    }

    public void validaBimboIdFC(IFunction function) {

        if(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() == null ||
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id().isEmpty()) {

            getContext().alert(R.drawable.warning, R.color.mango, R.string.text_register_24, new IAlertButton() {
                @Override
                public String onText() {
                    return getContext().getResources().getString(R.string.accept_button);
                }
                @Override
                public void onClick() {

                }
            });

        } else {
            function.execute();
        }

    }

    public void disConnect(){

        try {

            isConnected = false;

            if(communication != null){
                communication.disConnect(null);
                communication = null;
            }

        }catch (Exception ex) {

        }

    }

    public void closeAlertFC() {
        if (alertFC != null && alertFC.isShowing()){
            alertFC.dismiss();
        }
    }

    public void showPickerYear(int title, final IClickView... listener) {

        if (datePickerDialog != null && datePickerDialog.isShowing())
            datePickerDialog.dismiss();

        Date date = new Date();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dia = dayOfMonth + "";
                String mes = (month + 1) + "";

                dia = dia.length() == 1 ? "0" + dia : dia;
                mes = mes.length() == 1 ? "0" + mes : mes;

                if (listener.length >= 1)
                    listener[0].onClick(year);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.setTitle(title);
        datePickerDialog.setCancelable(false);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("month","id","android")).setVisibility(View.GONE);
        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);

        datePickerDialog.show();

    }

    public void showTimer(int title, String timeToDisplay, final IClickView... listener) {

        if (timePickerDialog != null && timePickerDialog.isShowing())
            timePickerDialog.dismiss();

        int hora, minutos, segundos;
        final Calendar c= Calendar.getInstance();

        hora    =c.get(Calendar.HOUR_OF_DAY);
        minutos =c.get(Calendar.MINUTE);
        segundos=c.get(Calendar.SECOND);

        if(timeToDisplay!=null && !timeToDisplay.isEmpty()) {
            String[] timeArray = timeToDisplay.split(":");
            if(timeArray.length > 0)
                hora = Integer.valueOf(timeArray[0]);
            if(timeArray.length > 1)
                minutos = Integer.valueOf(timeArray[1]);
        }

        timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //ehora.setText(hourOfDay+":"+minute);
                //Log.i("",hourOfDay + ":" + minute);
                String horaS = "" + hourOfDay;
                if(horaS.length() == 1)
                    horaS = "0" + horaS.trim();

                String minuteS = "" + minute;
                if(minuteS.length() == 1)
                    minuteS = "0" + minuteS.trim();// + "0";

                if (listener.length >= 1)
                    listener[0].onClick(horaS + ":" + minuteS);

            }
        },hora,minutos,false);

        //timePickerDialog.show();

        timePickerDialog.setTitle(title);
        timePickerDialog.setCancelable(false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        timePickerDialog.show();

    }

    /**
     * Se construyen las funciones de Firebase cuando se reciban notificaciones o back Data
     */
    private void buildFirebaseFunctions() {

        //RSB 20200214. POC FCM Multiuser Multidevice
        FCMBackgroundService.fcmMultiFunction = new IFunction() {
            @Override
            public void execute(Object[] data) {
                String notificationType = data[0].toString();
                String message = data[1].toString();
                QPAY_UserProfile profile = AppPreferences.getUserProfile();

                if(notificationType != null){
                    switch (notificationType){
                        //Estas son notificaciones de multiusuario
                        case Globals
                                .NotificationType.NOTIFICATION_LINK_REQUEST: //Usuario admin recibe notificación de operador intentando asociarse

                            profile.getQpay_object()[0].setQpay_user_type(Globals.ROL_PATRON);
                            AppPreferences.setUserProfile(profile);
                            break;

                        case Globals
                                .NotificationType.NOTIFICATION_UNLINK_MESSAGE: //Usuario admin recibe notificación de operador se ha desvinculado por si mismo

                            break;

                        case Globals
                                .NotificationType.NOTIFICATION_BASIC_USER: //Usuario admin recibe notificación se ha quedado sin operadores

                            profile.getQpay_object()[0].setQpay_user_type(Globals.ROL_NORMAL);
                            AppPreferences.setUserProfile(profile);
                            break;

                        //Estas son data de multiusuario, los data no generan notificación
                        case Globals
                                .NotificationType.DATA_LINK:  //Usuario operador recibe data y hace logout porque el admin ha aceptado la asociación a su cuenta

                        case Globals
                                .NotificationType.DATA_PRIVILEGES: //Usuario operador recibe data y hace logout porque el admin cambio sus privilegios

                        case Globals
                                .NotificationType.DATA_UNLINK: //Usuario operador recibe data y hace logout por que el patrón lo elimino de su cuenta

                            //Edita el appPreference antes por que si la app no esta abierta, el alert no se muestra,
                            //de esta manera si no pasa por el alert, entonces se ejecuta logout al abrir la aplicación
                            AppPreferences.setCloseSessionFlag("1",message);

                            try {

                                //Este alert se muestra solo si la app esta abierta y en el Home, de lo contrario no se mostrará
                                getContext().alert(message, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        NotificationManagerCompat.from(getContext()).cancelAll();

                                        AppPreferences.setCloseSessionFlag("0",null);
                                        AppPreferences.Logout(getContext());
                                        getContext().startActivity(LoginActivity.class, true);
                                    }
                                });

                            } catch (Exception ex) {

                                ex.printStackTrace();

                                toast(message);

                                NotificationManagerCompat.from(getContext()).cancelAll();

                                AppPreferences.setCloseSessionFlag("0",null);
                                AppPreferences.Logout(getContext());
                                getContext().startActivity(LoginActivity.class, true);

                            }



                            break;
                    }

                }

            }
        };

    }

    public void agregarUsusario(String token, IFunction execute){

        FCAddInfoUsuarioRequest fcAddInfoUsuarioRequest = new FCAddInfoUsuarioRequest();

        QPAY_UserProfile profile = AppPreferences.getUserProfile();

        fcAddInfoUsuarioRequest.setTokenJwt(token);
        fcAddInfoUsuarioRequest.setPriNombre(profile.getQpay_object()[0].getQpay_name());
        fcAddInfoUsuarioRequest.setSegNombre("");
        fcAddInfoUsuarioRequest.setTerNombre("");
        fcAddInfoUsuarioRequest.setApePaterno(profile.getQpay_object()[0].getQpay_father_surname());
        fcAddInfoUsuarioRequest.setApeMaterno(profile.getQpay_object()[0].getQpay_mother_surname());
        fcAddInfoUsuarioRequest.setBimboID(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
        fcAddInfoUsuarioRequest.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        fcAddInfoUsuarioRequest.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());

        Logger.d("REQUEST : "  + new Gson().toJson(fcAddInfoUsuarioRequest));

        AgregarUsuarioBimbo agregarUsuarioBimbo = new AgregarUsuarioBimbo(getContext());
        agregarUsuarioBimbo.postConsultaCredito(fcAddInfoUsuarioRequest, new AgregarUsuarioBimbo.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                FCAddInfoUsuarioResponse response = (FCAddInfoUsuarioResponse) Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getCodigo() < -1) {
                    getContext().alert(response.getRespuesta());
                } else {
                    AppPreferences.setRegisterBimboIdFc(true);
                    execute.execute();
                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
            }
        });

    }

    /**
     * Obtiene los cajeros asociados a la cuenta
     */
    private void getCashiers() {

        QPAY_LinkedUserStatus linkedUsers = new QPAY_LinkedUserStatus();
        linkedUsers.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        linkedUsers.setQpay_link_status("1"); //Estatus vinculado, cambiar a constantes

        getContext().loading(true);

        try {

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_LinkedUsersResponse response = gson.fromJson(json, QPAY_LinkedUsersResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            Fragment_transacciones_multi_1.users = response;
                            getContext().setFragment(Fragment_transacciones_multi_1.newInstance(), true);
                        } else {
                            validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getLinkedUsers(linkedUsers);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        IntentFilter filter = new IntentFilter("tconecta.sendOnDemand");
        registerReceiver(mOnDemandReceiver, filter);
        IntentFilter newOrderFilter = new IntentFilter("tconecta.sendNewOrder");
        registerReceiver(mNewOrderReceiver, newOrderFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mOnDemandReceiver);
            unregisterReceiver(mNewOrderReceiver);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private BroadcastReceiver mOnDemandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("tconecta.onDemand");
            switch (receivedText) {
                case "onDemand":
                    String strCampaignId = intent.getStringExtra("tconecta.campaignId");
                    int campaignId = Integer.parseInt(strCampaignId);
                    AppPreferences.saveCampaignId(campaignId);
                    break;
            }
        }
    };

    private BroadcastReceiver mNewOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("tconecta.newOrder");
            if (receivedText.equals(NOTIFICATION_NEW_ORDER) ||
                    receivedText.equals(NOTIFICATION_ACCEPT_ORDER) ||
                    receivedText.equals(NOTIFICATION_DECLINE_ORDER)) {
                if (!receivedText.equals(NOTIFICATION_DECLINE_ORDER))
                    startTimerForOrders();
                SoundUtils soundUtils = new SoundUtils();
                soundUtils.executeSound(MenuActivity.this);
            }
        }
    };

    private static int timerCounter = 0;
    public static Timer myTimer;

    public void startTimerForOrders() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!OrderActivity.pendingOrdersList.isEmpty() && OrderActivity.pendingOrdersList.size() > 0) {
                            AppPreferences.saveNewOrder("1", NOTIFICATION_NEW_ORDER);
                            SoundUtils soundUtils = new SoundUtils();
                            soundUtils.executeSound(MenuActivity.this);
                            if (timerCounter++ >= 2) {
                                myTimer.cancel();
                            }
                        }
                    }
                });
            }
        };
        myTimer = new Timer();
        myTimer.schedule(timerTask, 300000, 300000);
    }

    public static void stopTimerForOrders() {
        if (myTimer != null) myTimer.cancel();
    }

    public void showConfirmBimbo(String title, String text, String accept, String cancel, final IClickView... listener) {

        if (alertDialog != null && alertDialog.isShowing())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_confirm_bimbo, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);

        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener.length > 0)
                    listener[0].onClick();

                alertDialog.dismiss();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener.length > 1)
                    listener[1].onClick();

                alertDialog.dismiss();
            }
        });

        text_title.setText(title);
        text_mensaje.setText(text);

        btn_aceptar.setText(accept);

        btn_cancelar.setText(cancel);

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    public void showAlertBimbo(String title, String text, final IClickView... listener) {

        if (alertDialog != null && alertDialog.isShowing())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_bimbo, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        text_title.setText(title);
        text_mensaje.setText(text);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                if(listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    public void showAlertBimbo(int title, int text,int text_button, final IClickView... listener) {

        if (alertDialog != null && alertDialog.isShowing())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_bimbo, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        text_title.setText(title);
        text_mensaje.setText(text);
        btn_aceptar.setText(text_button);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                if(listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    public void showAlertBimbo(String title, String text, String text_button, final IClickView... listener) {

        if (alertDialog != null && alertDialog.isShowing())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_bimbo, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        text_title.setText(title);
        text_mensaje.setText(text);
        btn_aceptar.setText(text_button);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                if(listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    public void getPromotions(){

        getContext().loading(true);

        try {

            PromotionRequest promotionRequest = new PromotionRequest();
            promotionRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            //sellerUserRequest.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IPromotions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        PromotionResponse response = new Gson().fromJson(json, PromotionResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            Fragment_ofertas_bimbo_1.promotionResponse = response;
                            getContext().setFragment(Fragment_ofertas_bimbo_1.newInstance());
                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                getContext().alert(getContext().getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(getContext());
                                        getContext().startActivity(MenuActivity.class, true);
                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //getContext().showAlertAEONBlockedUser();
                            } else {
                                getContext().alert(response.getQpay_description());
                            }

                            getContext().loading(false);

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getPromotions(promotionRequest);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }


    public void getCountPublicidad(IFunction functionCountPublicidad, IFunction functionError) {

        getContext().loading(true);

        QPAY_Seed qpay_seed = new QPAY_Seed();
        qpay_seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        IGetTipsAdvertising iTipsListener = null;

        try {
            iTipsListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        Logger.e(((ErrorResponse) result).getMessage());
                        //getContext().alert(R.string.general_error);
                        if (functionError!=null)
                            functionError.execute();
                    } else {

                        String json = new Gson().toJson(result);;
                        QPAY_TipsAdvertisingNewCountResponse response = new Gson().fromJson(json, QPAY_TipsAdvertisingNewCountResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            String publicidadCount = response.getQpay_object()[0].getActive_publicities();
                            if(publicidadCount!=null && !publicidadCount.isEmpty()){
                                AppPreferences.setPublicidadCounter(publicidadCount);
                                setCountPublicity(false);
                                functionCountPublicidad.execute(publicidadCount);
                            }

                        } else {
                            //validaSesion(response.getQpay_code(), response.getQpay_description());
                            Logger.e("Error en QTC para obtener las campañas: " + response.getQpay_description());
                        }

                    }

                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    if (functionError!=null)
                        functionError.execute();
                }
            }, getContext());

            iTipsListener.getActiveTipsCampaignCount(qpay_seed);

        } catch (Exception e) {
            Logger.e(e.getMessage());
            getContext().loading(false);
            if (functionError!=null)
                functionError.execute();
        }

    }

    public boolean isCountChambitas() {
        return countChambitas;
    }

    public void setCountChambitas(boolean countChambitas) {
        this.countChambitas = countChambitas;
    }

    public boolean isCountPublicity() {
        return countPublicity;
    }

    public void setCountPublicity(boolean countPublicity) {
        this.countPublicity = countPublicity;
    }

    public void buscarSolicitud(String token,IFunction function,boolean isInit) {
        getContext().loading(true);
        FCBuscarSolicitudesRequest fcBuscarSolicitudesRequest = new FCBuscarSolicitudesRequest();
        fcBuscarSolicitudesRequest.setCliente(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
        fcBuscarSolicitudesRequest.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(fcBuscarSolicitudesRequest));


        BuscarSolicitud buscarSolicitud = new BuscarSolicitud(getContext());
        buscarSolicitud.postBuscarSolicitud(fcBuscarSolicitudesRequest, new BuscarSolicitud.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                getContext().loading(false);
                FCBuscarSolicitudesResponse response = (FCBuscarSolicitudesResponse) Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getSolicitudes() == null || response.getSolicitudes().isEmpty())
                    consultaOfertaBimbo(token,false,function);
                else {
                    ArrayList<DHSolicitud> solicitudes = new ArrayList<>();
                    DHSolicitud item = response.getSolicitudes().get(0);
                    if ((item.getIdEtapaSolicitud() == 1
                            && item.getSolicitud().toUpperCase().equalsIgnoreCase("PENDIENTE"))
                            || (item.getIdEtapaSolicitud() == 2
                            && item.getSolicitud().toUpperCase().equalsIgnoreCase("PENDIENTE"))) {
                        solicitudes.add(item);
                    }
                    if (solicitudes.isEmpty() && isInit){
                        consultaOfertaBimbo(token,false,function);
                    }else {
                        SessionApp.getInstance().setFcBuscarSolicitudesResponse(response);
                        function.execute("SOLICITUD");
                    }

                }


                getContext().loading(false);
            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    private void getRecompras(String token,IFunction function) {
        getContext().loading(true);

        ListaRecompras listaRecompras = new ListaRecompras(getContext());
        FCListaRecomprasRequest request = new FCListaRecomprasRequest(
                HCoDi.numCliente,
                token
        );

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        listaRecompras.postListaRecompras(request, new ListaRecompras.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                getContext().loading(false);

                FCListaRecomprasResponse response = (FCListaRecomprasResponse)Object;
                List<DHMovimientosRecompras> data = new ArrayList<>();

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response == null || response.getMovimientos().isEmpty()){

                    function.execute("CREDITO");
                }else{
                    SessionApp.getInstance().setNumClienteRecompra(response.getNum_cliente());
                    for (DHMovimientosRecompras item: response.getMovimientos()) {
                        for (DHPorcentajesRecompras pc: response.getPorcentajes()) {
                            if (pc.getNum_credito() == item.getIdCuenta()){
                                item.setBandera_porcentaje(pc.getBandera_porcentaje());
                                item.setPorcentaje(pc.getPorcentaje().intValue());
                                if (pc.getBandera_porcentaje() == 1 || pc.getPorcentaje().intValue() >= 50) {
                                    data.add(item);
                                }
                            }
                        }
                    }

                    if (data.size() > 0){
                        SessionApp.getInstance().setListaRecompras(data);
                        buscarSolicitud(token,function,false);
                       // function.execute("CREDITO");
                    }else {
                        function.execute("CREDITO");
                    }
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                function.execute("CREDITO");
            }
        });
    }

    public void postAnalisis(String token, DHSolicitud item, IFunction function, Fragment_fincomun_oferta.Type type) {
        getContext().loading(true);
        Analisis analisis = new Analisis(getContext());

        FCAnalisisRequest request = new FCAnalisisRequest(
                item.getFolio(),
                item.getNumCliente(),
                token
        );
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        analisis.postAnalisis(request, new Analisis.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                FCAnalisisResponse response = (FCAnalisisResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response == null){
                    getContext().alert("Error al consultar información de la solicitud");

                }else{
                    if (response.getRespuesta().getCodigo() == 0){
                        prepareData(response,function,type);

                    }else{
                        getContext().alert(response.getRespuesta().getDescripcion().get(0));
                        getContext().loading(false);

                    }
                }

            }

            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });
    }

    private void prepareData(FCAnalisisResponse response, IFunction function, Fragment_fincomun_oferta.Type type) {
        switch (type) {
            case RECOMPRA:
                break;
            default:
                SessionApp.getInstance().getFcRequestDTO().setFolio(response.getDatosFolio().getFolio());
            break;
        }
        SessionApp.getInstance().getFcRequestDTO().setNumCliente(response.getNumCliente());

        FCSimuladorRequest simuladorRequest = new FCSimuladorRequest();
        if (response.getDatosOferta() != null && !response.getDatosOferta().isEmpty()) {
            SessionApp.getInstance().getFcRequestDTO().setTasa(response.getDatosOferta().get(0).getTasa());
        }else {
            SessionApp.getInstance().getFcRequestDTO().setTasa(75.0);
        }
        DHDatosSimulador simulador = response.getDatosSimulador();
        simuladorRequest.setIdTipoCredito(simulador.getId_tipo_prod_cred());
        simuladorRequest.setIdModCredito(simulador.getId_mod_credito());
        simuladorRequest.setIdTipoProducto(Integer.parseInt(simulador.getId_product_credito()));
        simuladorRequest.setMontoPrestamo(BigDecimal.valueOf(simulador.getMontoPrestamo()).toBigInteger());
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos(response.getDatosSimulador().getNumPagos());
        simuladorRequest.setDestino("311");
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago(response.getDatosSimulador().getFrecuenciaPago());
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        simuladorRequest.setCuota(response.getDatosSimulador().getCuota());
        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);

        FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();

        DHDatosCliente datosCliente = response.getDatosCliente();
        SessionApp.getInstance().getFcRequestDTO().setEmailAnalisis(datosCliente.getEmail());
        DHDatosDirecciones direccion = datosCliente.getDireccion();
        DHDatosIne datosIne = response.getDatosIne();

        DHDatosPersonales datosPersonales = response.getDatosPersonales();
        DHDatosNegocio datosNegocio = response.getDatosNegocio();

        SessionApp.getInstance().setDatosPersonales(datosPersonales);
        SessionApp.getInstance().setDatosNegocio(datosNegocio);

        if (direccion != null) {
            fcIdentificacionRequest.setNumCliente(response.getNumCliente());
            fcIdentificacionRequest.setNombre(datosCliente.getNombre());
            fcIdentificacionRequest.setApellidoPaterno(datosCliente.getApellidoPaterno());
            fcIdentificacionRequest.setApellidoMaterno(datosCliente.getApellidoMaterno());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            SimpleDateFormat d2 = new SimpleDateFormat("dd/MM/yyyy");

            try {
                fcIdentificacionRequest.setFechaNacimiento(d2.format(df.parse(datosCliente.getFechaNacimiento())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            fcIdentificacionRequest.setNumCliente(response.getNumCliente());
            fcIdentificacionRequest.setCalle(direccion.getCalle());
            fcIdentificacionRequest.setCalle_1(direccion.getCalle1());
            fcIdentificacionRequest.setCalle_2(direccion.getCalle2());
            fcIdentificacionRequest.setNumExterior(direccion.getNumExterior());
            fcIdentificacionRequest.setNumInterior(direccion.getNumInterior());
            fcIdentificacionRequest.setManzana(direccion.getManzana());
            fcIdentificacionRequest.setLote(direccion.getLote());

            fcIdentificacionRequest.setEstado(direccion.getEstado());
            fcIdentificacionRequest.setEntidad(String.valueOf(direccion.getIdEntidad()));
            fcIdentificacionRequest.setMunicipio(String.valueOf(direccion.getIdMunicipio()));
            fcIdentificacionRequest.setCp(direccion.getCp());
            fcIdentificacionRequest.setColonia(direccion.getColonia());

            try {
                fcIdentificacionRequest.setAntiguedad(d2.format(df.parse(direccion.getAntiguedad())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fcIdentificacionRequest.setLocalidad(String.valueOf(direccion.getIdLocalidad()));

            fcIdentificacionRequest.setCurp(datosIne.getCurp());
            fcIdentificacionRequest.setClaveElector(datosIne.getClaveElector());
            fcIdentificacionRequest.setRfc(datosCliente.getRfc());
            fcIdentificacionRequest.setCic(datosIne.getCic());
            fcIdentificacionRequest.setOcr(datosIne.getOcr());
            fcIdentificacionRequest.setAnioEmision(datosIne.getAnioEmision());
            fcIdentificacionRequest.setAnioRegistro(datosIne.getAnioRegistro());
            fcIdentificacionRequest.setId_actividad_general(datosIne.getIdActividadGeneral());
            fcIdentificacionRequest.setId_actividad_economica(datosIne.getIdActividadEconomica());
            fcIdentificacionRequest.setIdIdentificacion("1");

            SessionApp.getInstance().getFcRequestDTO().setGenero("N");

            FCComprobantesNegocioRequest fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
            fcComprobantesNegocioRequest.setFolio(response.getFolio());
            fcComprobantesNegocioRequest.setNomina(response.getNomina());


            ArrayList<String> imgNegocio = new ArrayList<>();
            ArrayList<String> imgIdentificacion = new ArrayList<>();
            for (DHDatosArchivo archivo: response.getDatosArchivos()){
                if (archivo.getNombreArchivo().contains("FOTO_NEGOCIO_1")){
                    imgNegocio.add(0,archivo.getBase64());
                }else if (archivo.getNombreArchivo().contains("INE_F")
                        || archivo.getNombreArchivo().contains("INE_QR_F")
                        || archivo.getNombreArchivo().contains("IFE_F")){
                    imgIdentificacion.add(0,archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("INE_T")
                        || archivo.getNombreArchivo().contains("INE_QR_T")
                        || archivo.getNombreArchivo().contains("IFE_T")){
                    imgIdentificacion.add(1,archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("COMPROBANTE_DOMICILIO")){
                    fcComprobantesNegocioRequest.setComprobanteDomicilio(archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("ESTADO_CUENTA")){
                    fcComprobantesNegocioRequest.setEstadoCuenta(archivo.getBase64());
                }
            }
            if (imgNegocio.isEmpty()){
                imgNegocio.add(0,"");
                imgNegocio.add(1,"");
            }else{
                imgNegocio.add(1,"");
            }
            fcComprobantesNegocioRequest.setImagenNegocio(imgNegocio);

            if (!TextUtils.isEmpty(fcComprobantesNegocioRequest.getComprobanteDomicilio())
                    && fcComprobantesNegocioRequest.getComprobanteDomicilio().equalsIgnoreCase(fcComprobantesNegocioRequest.getImagenNegocio().get(0))){
                fcComprobantesNegocioRequest.setComprobanteDomicilio("");
            }
            fcIdentificacionRequest.setImagen(imgIdentificacion);
            SessionApp.getInstance().getFcRequestDTO().setIdentificacion(fcIdentificacionRequest);
            SessionApp.getInstance().getFcRequestDTO().setComprobante_negocio(fcComprobantesNegocioRequest);

            FCReferenciasRequest fcReferenciasRequest = new FCReferenciasRequest();
            fcReferenciasRequest.setFolio(response.getFolio());
            ArrayList<DHReferencia> referencias = new ArrayList<>();
            for (DHDatosReferencia referencia: response.getDatosReferencias()){
                referencias.add(new DHReferencia(
                        referencia.getNombre(),
                        referencia.getApellidoPaterno(),
                        referencia.getApellidoMaterno(),
                        referencia.getIdParentesco(),
                        referencia.getTelefono(),
                        (type == Fragment_fincomun_oferta.Type.RECOMPRA)?0:referencia.getIdReferencia()));
            }
            fcReferenciasRequest.setReferencias(referencias);
            SessionApp.getInstance().getFcRequestDTO().setReferencias(fcReferenciasRequest);

            FCBeneficiariosRequest fcBeneficiariosRequest = new FCBeneficiariosRequest();
            fcBeneficiariosRequest.setFolio(response.getFolio());
            ArrayList<DHBeneficiario> beneficiarios = new ArrayList<>();
            for (DHDatosBeneficiarios beneficiario: response.getDatosBeneficiarios()){

                beneficiarios.add(new DHBeneficiario(
                        beneficiario.getTelefono(),
                        beneficiario.getNombre(),
                        beneficiario.getApellidoPaterno(),
                        beneficiario.getApellidoMaterno(),
                        beneficiario.getFecha_nacimiento(),
                        beneficiario.getIdParentesco(),
                        beneficiario.getPorcentaje(),
                        (type == Fragment_fincomun_oferta.Type.RECOMPRA)?0:beneficiario.getId_beneficiario()
                        ));

            }
            fcBeneficiariosRequest.setBeneficiarios(beneficiarios);
            SessionApp.getInstance().getFcRequestDTO().setBeneficiarios(fcBeneficiariosRequest);

        }

        if(response.getDatosTelefonicos() != null){
            SessionApp.getInstance().getFcRequestDTO().setDatosTelefonicos(response.getDatosTelefonicos());
        }



        getContext().loading(false);
        if(function != null){
            function.execute();
        }
    }


    public void getFolioRecompra(String token,IFunction function){
        getContext().loading(true);

        RecompraCredito recompraCredito = new RecompraCredito(getContext());

        FCRecompraCreditoRequest request = new FCRecompraCreditoRequest(
                String.valueOf(SessionApp.getInstance().getRecompra().getIdCuenta()),
                token
        );
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        recompraCredito.postRecompraCredito(request, new RecompraCredito.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);

                FCRecompraCreditoResponse response = (FCRecompraCreditoResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response != null && response.getRespuesta().getCodigo() == 0){
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    DHSolicitud solicitud = new DHSolicitud();
                    solicitud.setFolio(response.getFolioAntiguo());
                    solicitud.setNumCliente(SessionApp.getInstance().getNumClienteRecompra());
                    postAnalisis(token,solicitud,function, Fragment_fincomun_oferta.Type.RECOMPRA);
                }else {
                    getContext().alert("Ocurrió un error al consultar la información");
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });
    }

    public void showNoOfferFC(final IClickView... listener){

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_no_offer, null);

        Button btn_offer = view.findViewById(R.id.btn_offer);
        Button btn_acept = view.findViewById(R.id.btn_acept);


        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        btn_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 1)
                    listener[1].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();
    }

    public void saveRegister(CApplication.ACTION action) {
        CApplication.setAnalytics(action);
    }

    public void getNumClienteExistente(String token,IFunction function) {
        getContext().loading(true);

        ListaRecompras listaRecompras = new ListaRecompras(getContext());
        FCListaRecomprasRequest request = new FCListaRecomprasRequest(
                HCoDi.numCliente,
                token
        );

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        listaRecompras.postListaRecompras(request, new ListaRecompras.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                getContext().loading(false);

                FCListaRecomprasResponse response = (FCListaRecomprasResponse)Object;
                List<DHMovimientosRecompras> data = new ArrayList<>();

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response == null || TextUtils.isEmpty(response.getNum_cliente())){

                    function.execute(null);
                }else{
                    function.execute(response.getNum_cliente());
                }

                getContext().loading(false);

            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                function.execute(null);
            }
        });
    }

    public void validateVersion(IFunction function){
        getContext().loading(true);

        VersionProd versionProd = new VersionProd(getContext());

        versionProd.getVersionProd(new VersionProd.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);

                String value = String.valueOf(e).replace("V","");


                if (value.equalsIgnoreCase(SDKFC.getVersion())){
                    function.execute();
                } else {
                    if(Tools.getModel().equals(Globals.NEXGO_N3) || Tools.getModel().equals("N3") || (AppPreferences.getDevice() != null && AppPreferences.getDevice().getName().contains("SP530"))){
                        getContext().alert("Seguimos mejorando para ti, por favor actualiza la aplicación para continuar",
                                new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "CERRAR";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });
                    }else {
                        getContext().alert("Seguimos mejorando para ti, por favor actualiza la aplicación para continuar",
                                new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "ACTUALIZAR";
                                    }

                                    @Override
                                    public void onClick() {
                                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName())));

                                    }
                                },
                                new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "CERRAR";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                Runnable runOnUI = new Runnable() {
                    @Override
                    public void run() {
                        if (fullScreenFragment != null) {
                            fullScreenFragment.getContext().getSupportFragmentManager().popBackStack();
                        }
                    }
                };
                Runnable runOnThread = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            runOnUiThread(runOnUI);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runOnThread);
                thread.start();
            }
        });
    }

    public void getRefOxxo(String idCredito,Boolean exigible,Double monto,String token,IFunction function){
        getContext().loading(true);

        ReferenciaOXXO refenciaOXXO = new ReferenciaOXXO(getContext());
        FCCreditoOXXORequest request = new FCCreditoOXXORequest();
        request.setNumCreditoIsi(idCredito);
        request.setMontoTotal(exigible);
        request.setMonto(monto);
        request.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        refenciaOXXO.postReferenciaOXXO(request, new ReferenciaOXXO.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);

                FCCreditoOXXOResponse response = (FCCreditoOXXOResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response == null || response.getCodigo() != 0){

                    function.execute(null);
                }else{
                    function.execute(new Gson().toJson(response));
                }

                getContext().loading(false);
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                function.execute(null);
            }
        });

    }

    public void getPlanPagos(String numeroCredito, String token, IFunction function) {
        getContext().loading(true);

        PlanPagos planPagos = new PlanPagos(getContext());
        FCPlanPagosRequest request = new FCPlanPagosRequest(
           numeroCredito,
           token
        );
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        planPagos.postPlanPagos(request,new PlanPagos.onRequest(){
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);

                FCPlanPagosResponse response = (FCPlanPagosResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response != null && response.getCodigo() == 0){
                    function.execute(new Gson().toJson(response));
                }else{
                    function.execute(null);
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                function.execute(null);
            }
        });
    }

    public void getCompras(IFunction<QPAY_SalesRetailerResponse> function){

        getContext().loading(true);

        try {

            QPAY_SalesRetailer data = new QPAY_SalesRetailer();
            data.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

            IGetSalesByRetailerId petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_SalesRetailerResponse response = new Gson().fromJson(json, QPAY_SalesRetailerResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getSalesByRetailerId(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    public void showAlertLayoutBitacora(int layout_id,int src, String title_str, String description_str, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getContext()).inflate(layout_id, null);

        TextView title = view.findViewById(R.id.title);
        if (title != null){
            title.setText(title_str);
        }
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);
        if (text_mensaje != null){
            text_mensaje.setText(description_str);
        }

        ImageView icon = view.findViewById(R.id.iv_icon);
        if (icon != null){
            icon.setImageResource(src);
        }
        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }

    public static void syncTransactions(Context context) {
        if (AppPreferences.getYesterdayTransactions().getFecha() != null && Tools.getTodayDateWithMoreOrMinusDays(-1, "dd/MM/yyyy").equalsIgnoreCase(AppPreferences.getYesterdayTransactions().getFecha())) {
            Log.d("Send:", "YesterdayTransactions");
            report(AppPreferences.getYesterdayTransactions(), context, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    Log.d("data: ", data.toString());
                    //Execute solo se activa si el success es true
                    //Limpiamos lo de ayer y volvemos a sicronizar
                    AppPreferences.setYesterdayTransactions(new TransactionsModel());
                    syncTransactions(context);
                }
            });
        }
        else if (AppPreferences.getYesterdayTransactions().getFecha() == null) {
            if (AppPreferences.getTodayTransactions().getFecha() != null
                    && Tools.getTodayDateWithMoreOrMinusDays(-1, "dd/MM/yyyy")
                    .equalsIgnoreCase(AppPreferences.getTodayTransactions().getFecha())) {
                //pasamos el objeto hoy al objeto ayer y limpiamos today
                AppPreferences.setYesterdayTransactions(AppPreferences.getTodayTransactions());
                AppPreferences.setTodayTransactions(new TransactionsModel());
                syncTransactions(context);
            }
            else if (AppPreferences.getTodayTransactions().getFecha() == null ) {
                TransactionsModel tmp = AppPreferences.getTodayTransactions();
                tmp.setFecha(Tools.getTodayDate("dd/MM/yyyy"));
                if ( tmp.getSeed() == null && AppPreferences.getUserProfile() != null) {
                    tmp.setSeed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                    AppPreferences.setTodayTransactions(tmp);
                    syncTransactions(context);
                }
            }
        }
        else if (!Tools.getTodayDateWithMoreOrMinusDays(-1, "dd/MM/yyyy").equalsIgnoreCase(AppPreferences.getYesterdayTransactions().getFecha())
                && Tools.getTodayDate("dd/MM/yyyy").equalsIgnoreCase(AppPreferences.getYesterdayTransactions().getFecha())) {
            report(AppPreferences.getYesterdayTransactions(), context, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    Log.d("data: ", data.toString());
                    //Execute solo se activa si el success es true
                    //Limpiamos lo de ayer y volvemos a sicronizar
                    AppPreferences.setYesterdayTransactions(new TransactionsModel());
                    syncTransactions(context);
                }
            });
        }
    }

    public static void report(TransactionsModel data, Context context, final IFunction function) {

        // context.loading(true);

        IConnectionReport service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    if (result instanceof ErrorResponse) {
                        Log.d("Error:", ((ErrorResponse) result).getMessage());
                    }
                    else {
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Report_Response response = gson.fromJson(json, QPAY_Report_Response.class);
                        if (response.getQpay_response().equals("true")) {
                            if(function != null)
                                function.execute();
                        }
                        else {
                            if (response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")) {
                                Log.d("ErrorResponse: ", response.getQpay_code());
                            }
                            else if (response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                Log.d("ErrorResponse: ", "UNAUTHORIZED:HTTP");
                            }
                            else {
                                Log.d("ErrorResponse: ", response.getQpay_code());
                            }
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    //context.loading(false);
                    //context.alert(R.string.general_error);
                    Log.d("Error:", result.toString());
                }
            }, context);
        } catch (Exception e) {
            //context.loading(false);
            // context.alert(R.string.general_error_catch);
            Log.d("Error:", e.getMessage());
        }

        service.report(data);
    }
}