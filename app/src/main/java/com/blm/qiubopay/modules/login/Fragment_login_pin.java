package com.blm.qiubopay.modules.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.listeners.IBalanceInquiry;
import com.blm.qiubopay.listeners.IFinancialVas;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryPetition;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsResponse;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.BuildConfig;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CLoginOption;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.listeners.IBalance;
import com.blm.qiubopay.listeners.IGetCommissionReport;
import com.blm.qiubopay.listeners.IGetTipsAdvertising;
import com.blm.qiubopay.listeners.IGetTodayTotalAmount;
import com.blm.qiubopay.listeners.ILogin;
import com.blm.qiubopay.models.CsHeader;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.LastLogin;
import com.blm.qiubopay.models.QPAY_Balance;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.QPAY_CommissionReport;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_TodayTotalAmount;
import com.blm.qiubopay.models.QPAY_TodayTotalAmountResponse;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertisingResponse;
import com.blm.qiubopay.modules.campania.Fragment_tips;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;

import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.components.otp.PinView;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_login_pin extends HFragment {

    public static boolean login;
    public static IFunction authPIN;

    private Button btn_acceder;
    private int intentoas = 0;
    private int intentosBiometric = 0;
    private PinView edit_pin = null;

    private LinearLayout layout_pin;
    private LinearLayout layout_biometric;
    private ImageView btn_biometric;
    private Button btn_pin;
    private CLoginOption biometric = null;
    private TextView text_error_pin;

    private boolean first = true;

    private static final int MAX_PIN_INTENT = 8;

    public static Fragment_login_pin newInstance(Object... data) {
        Fragment_login_pin fragment = new Fragment_login_pin();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_login_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_login_pin, container, false));
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showTitle(Globals.VERSION);

        if(!Fragment_login_pin.login) {
            CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
                @Override
                public void onClick() {
                    getContext().backFragment();
                }
            });
        } else {
            AppPreferences.isLogin(false);
        }

        layout_pin = getView().findViewById(R.id.layout_pin);
        layout_biometric = getView().findViewById(R.id.layout_biometric);
        btn_biometric = getView().findViewById(R.id.btn_biometric);
        btn_pin = getView().findViewById(R.id.btn_pin);
        btn_acceder = getView().findViewById(R.id.btn_acceder);
        edit_pin = getView().findViewById(R.id.edit_pin);
        text_error_pin = getView().findViewById(R.id.text_error_pin);

        TextView btn_close = getView().findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().alert(getContext().getResources().getString(R.string.message_logout_question), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        CApplication.generateNewFCMToken();
                        CApplication.setAnalytics(CApplication.ACTION.CB_PERFIL_seleccionan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "CERRAR SESION"));
                        AppPreferences.Logout(getContext());
                        getContext().startActivity(LoginActivity.class, true);
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() { }
                });
            }
        });

        //if(!login)
            btn_close.setVisibility(View.GONE);

        //20201125 RSB. Forzar localización para N3
        if(Tools.isN3Terminal()){
            ((HLocActivity) getContext()).setForceLocation(true);
            ((HLocActivity) getContext()).obtainLocation(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    AppPreferences.setTodayLastLocation(CApplication.getLastLocation());
                }
            });
        }

        Fragment_login_pin.login = false;

        if(AppPreferences.getPin() != null && AppPreferences.getPin().getBiometric()) {

            biometric =  CLoginOption.create(getContext()).onBiometric(btn_biometric, new CLoginOption.IBiometric() {
                @Override
                public void onSuccess() {

                    if(authPIN != null) {

                        HTimerApp.getTimer().start(getContext());
                        authPIN.execute();
                        authPIN = null;

                        return;
                    }

                    getContext().startActivity(MenuActivity.class, true);

                }
                @Override
                public void onError(boolean enabled, String message) {

                    if(!enabled) {
                        btn_pin.setVisibility(View.GONE);
                        layout_biometric.setVisibility(View.GONE);
                        layout_pin.setVisibility(View.VISIBLE);
                    } else {
                        btn_pin.setVisibility(View.VISIBLE);
                        layout_biometric.setVisibility(View.VISIBLE);
                        layout_pin.setVisibility(View.GONE);

                        if(first) {

                            first = false;

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_biometric.performClick();
                                }
                            }, 2500);

                        }
                        
                    }

                    if(enabled) {

                        ++intentosBiometric;

                        if(intentosBiometric < MAX_PIN_INTENT) {

                        } else {

                            biometric.cancelAuthentication();

                            getContext().alert(message + "\nPor seguridad se ha cerrado su sesión", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContext().startActivity(LoginActivity.class, true);
                                }
                            });

                            AppPreferences.Logout(getContext());
                        }

                    }

                }
            });

            btn_acceder.setVisibility(View.GONE);
            btn_pin.setVisibility(View.VISIBLE);
            btn_pin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_pin.setVisibility(View.GONE);
                    layout_biometric.setVisibility(View.GONE);
                    layout_pin.setVisibility(View.VISIBLE);
                    btn_acceder.setVisibility(View.VISIBLE);
                }
            });

        } else {
            btn_acceder.setVisibility(View.VISIBLE);
            btn_pin.setVisibility(View.GONE);
            layout_biometric.setVisibility(View.GONE);
            layout_pin.setVisibility(View.VISIBLE);

        }

        edit_pin.setChanged(new PinView.ITextChanged() {
            @Override
            public void onChange(int length) {
                if(edit_pin.isValid())
                    btn_acceder.setEnabled(true);
                else
                    btn_acceder.setEnabled(false);
            }
        });

        btn_acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pin = edit_pin.getText().toString();

                if(AppPreferences.getPin().getPin().equals(pin)) {

                    AppPreferences.isLogin(true);

                    if(authPIN != null) {

                        HTimerApp.getTimer().start(getContext());
                        authPIN.execute();
                        authPIN = null;

                        return;
                    }

                    getContext().startActivity(MenuActivity.class, true);

                } else {

                    edit_pin.requestFocus();

                    int contIntento = MAX_PIN_INTENT - ++intentoas;

                    String intento = contIntento == 1 ? " intento " : " intentos ";

                    if(intentoas < MAX_PIN_INTENT)
                        error("Código erróneo. Tiene " + (contIntento) + intento + " más.");
                    else {

                        getContext().alert(R.drawable.warning, R.color.scarlet, "Acceso denegado", "Has superado el número máximo de intentos permitidos, por seguridad se ha cerrado su sesión.", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                getContext().startActivity(LoginActivity.class, true);
                            }
                        });

                        AppPreferences.Logout(getContext());
                    }

                }

            }
        });

        btn_pin.setText("");

    }

    public void validate() {

        text_error_pin.setVisibility(View.GONE);

        if(edit_pin.getText().toString().length() < 4){
            btn_acceder.setEnabled(false);
            return;
        }

        btn_acceder.setEnabled(true);
    }

    public void error(String text) {
        final View lineError = getView().findViewById(R.id.line_error);
        lineError.setBackgroundColor(getResources().getColor(R.color.color_error));
        text_error_pin.setVisibility(View.VISIBLE);
        edit_pin.setText("");
        text_error_pin.setText(text);
    }


}

