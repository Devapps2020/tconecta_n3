package com.blm.qiubopay.modules.registro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.helpers.MigrateHelper;
import com.blm.qiubopay.listeners.IBalanceInquiry;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryPetition;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryResponse;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CLoginOption;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IBalance;
import com.blm.qiubopay.listeners.IGetCommissionReport;
import com.blm.qiubopay.listeners.IGetTodayTotalAmount;
import com.blm.qiubopay.models.CsHeader;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Balance;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.QPAY_CommissionReport;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_Pin;
import com.blm.qiubopay.models.QPAY_TodayTotalAmount;
import com.blm.qiubopay.models.QPAY_TodayTotalAmountResponse;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_registro_pin  extends HFragment {

    private QPAY_Pin data;
    private ArrayList<CViewEditText> campos = null;
    private Button btn_validar = null;
    private Switch swt_biometric = null;

    //210805 RSB. Migracion.
    private MigrateHelper migrateHelper;

    public static Fragment_registro_pin newInstance() {
        return new Fragment_registro_pin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_registro_pin, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        campos = new ArrayList();

        btn_validar = getView().findViewById(R.id.btn_validar);
        swt_biometric = getView().findViewById(R.id.swt_biometric);

        ImageView help_pin = getView().findViewById(R.id.help_pin);
        help_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().alert("El código PIN solo es de 4 dígitos numéricos");
            }
        });

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_1))
                .setRequired(true)
                .setMinimum(4)
                .setMaximum(4)
                .setType(CViewEditText.TYPE.NUMBER_PASS)
                .setHint(R.string.text_access_16)
                .setAlert(R.string.text_input_required)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(!campos.get(1).getText().isEmpty() && !text.equals(campos.get(1).getText())){
                            campos.get(1).setText("");
                            campos.get(1).setShowPass(false);
                        }

                        if(text.length() == 4)
                            validate.onChange(text);

                    }
                }));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_2))
                .setRequired(true)
                .setMinimum(4)
                .setMaximum(4)
                .setType(CViewEditText.TYPE.NUMBER_PASS)
                .setHint(R.string.text_access_17)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_access_18)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(validate));

        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePIN();
            }
        });

        CLoginOption.create(getContext()).onBiometric(null, new CLoginOption.IBiometric() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError(boolean enabled, String message) {

                if(!enabled)
                    swt_biometric.setVisibility(View.GONE);

            }
        });

        swt_biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!swt_biometric.isChecked())
                    btn_validar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savePIN();
                        }
                    });
                else
                    CLoginOption.create(getContext()).onBiometric(btn_validar, new CLoginOption.IBiometric() {
                        @Override
                        public void onSuccess() {
                            savePIN();
                        }
                        @Override
                        public void onError(boolean enabled, String message) {

                        }
                    });

            }
        });

        //210805 RSB. Migracion. AutoPin y borrado de archivo
        migrateHelper = new MigrateHelper(getContext());
        if(migrateHelper.validateFileExist()){
            //migrateHelper.deleteFile();
            QPAY_UserCredentials credentials = migrateHelper.readBackup();
            cargarSaldo(true,true,true,new IFunction() {
                @Override
                public void execute(Object[] obj) {

                    data = new QPAY_Pin();
                    data.setPin(credentials.getPin());
                    data.setBiometric(swt_biometric.isChecked());
                    AppPreferences.isLogin(true);
                    AppPreferences.setPin(data);
                    migrateHelper.deleteFile();
                    getContext().startActivity(MenuActivity.class, true);

                }
            });
        }

    }

    public void savePIN() {

        cargarSaldo(true,true,true,new IFunction() {
            @Override
            public void execute(Object[] obj) {

                data = new QPAY_Pin();
                data.setPin(campos.get(0).getText());
                data.setBiometric(swt_biometric.isChecked());
                AppPreferences.isLogin(true);
                AppPreferences.setPin(data);
                getContext().startActivity(MenuActivity.class, true);

            }
        });

    }

    public void validate(String text) {

        btn_validar.setEnabled(false);

        for(CViewEditText edit: campos)
            if(!edit.isValid())
                return;

        if(campos.size() > 0 && !campos.get(0).getText().equals(campos.get(1).getText())) {
            campos.get(1).activeError();
            return;
        }

        btn_validar.setEnabled(true);

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
                        if(function!=null){
                            function.execute();
                        }
                    } else {

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

                        }

                        /*final double monto = Double.parseDouble(AppPreferences.getKinetoBalance().replace("Saldo","").trim().replace(",","").replace("$",""));
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
                        }*/

                        if(function != null)
                            function.execute(balanceResponse);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //getContext().alert(R.string.general_error);
                    if(function!=null){
                        function.execute();
                    }
                }

            }, getContext());

            balance.getBalance(petition);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            //getContext().alert(R.string.general_error_catch);
            if(function!=null){
                function.execute();
            }
        }
    }

}