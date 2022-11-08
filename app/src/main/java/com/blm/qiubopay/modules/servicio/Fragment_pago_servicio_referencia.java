package com.blm.qiubopay.modules.servicio;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.builder.ServiceItemsBuilder;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.listeners.IDishQuery;
import com.blm.qiubopay.listeners.IMegacableQuery;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.listeners.iServicePayment;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.QPAY_ServicePackageResponse;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.models.services.QPAY_ServicePaymentResponse;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_numero;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_ticket;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_servicio_referencia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_servicio_referencia extends HFragment implements IMenuContext {

    private static final String TAG = "servicio_referencia";

    private ArrayList<CViewEditText> campos = null;

    private CompaniaDTO companiaDTO;
    private QPAY_ServicePayment servicePayment;

    private LinearLayout edit_campo_1;
    private LinearLayout edit_campo_2;
    private LinearLayout edit_campo_3;
    private Button btn_continuar;
    private LinearLayout layout_scaner;

    private IFunction functionContinue;

    public Fragment_pago_servicio_referencia() {
        // Required empty public constructor
    }

    public static Fragment_pago_servicio_referencia newInstance(Object... data) {
        Fragment_pago_servicio_referencia fragment = new Fragment_pago_servicio_referencia();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_servicio_referencia_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_servicio_referencia_2", new Gson().toJson(data[1]));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_referencia_1"), CompaniaDTO.class);
            servicePayment = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_referencia_2"), QPAY_ServicePayment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_referencia, container, false), R.drawable.background_splash_header_1);
    }


    public void initFragment() {

        campos = new ArrayList();

        //Init headers

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_servicio_title))
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        CViewSaldo.create(getContextMenu(), getView(), true);

        //Get Screen components

        ImageView ivCompania = getView().findViewById(R.id.iv_compania);
        ivCompania.setImageDrawable(getResources().getDrawable(companiaDTO.getImage()));

        configScreen();

    }


    private void configScreen() {

        edit_campo_1 = getView().findViewById(R.id.edit_campo_1);
        edit_campo_2 = getView().findViewById(R.id.edit_campo_2);
        edit_campo_3 = getView().findViewById(R.id.edit_campo_3);
        layout_scaner = getView().findViewById(R.id.layout_scaner);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        final LinearLayout layout_datos = getView().findViewById(R.id.layout_datos);
        final LinearLayout layout_dish = getView().findViewById(R.id.layout_dish);
        final RadioButton rad_cuenta = getView().findViewById(R.id.rad_cuenta);
        final RadioButton rad_cupon = getView().findViewById(R.id.rad_cupon);

        edit_campo_2.setVisibility(View.GONE);
        edit_campo_3.setVisibility(View.GONE);
        layout_scaner.setVisibility(View.GONE);
        btn_continuar.setEnabled(false);

        layout_dish.setVisibility(View.GONE);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                if (!campos.isEmpty())
                    validate(text);
            }
        };

        functionContinue = null;

        switch (servicePayment.getQpay_product()) {

            // ----------------------------- DECLARACION SERVICIO TELMEX ---------------------------
            case Globals.TELMEX_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                edit_campo_2.setVisibility(View.VISIBLE);

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 10,
                        CViewEditText.TYPE.NUMBER, "Número de teléfono", R.string.text_input_required, validate, ""));

                campos.add(createCampo(R.id.edit_campo_2, true, 1, 2,
                        CViewEditText.TYPE.NUMBER, "Dígito verificador", R.string.text_input_required, validate, ""));

                layout_scaner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {
                                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                                    @Override
                                    public void handleResult(Result result) {
                                        if (result == null)
                                            return;

                                        try {
                                            String resultado = result.getContents().trim();
                                            resultado = resultado.replaceAll("[^0-9]", "").trim();

                                            if (result.getContents().trim().length() != 20) {
                                                getContext().alert("Código inválido");
                                                return;
                                            }
                                            resultado = resultado.substring(0, 10);

                                            campos.get(0).setText(resultado);
                                            String monto = result.getContents().substring(10, result.getContents().trim().length() - 3);
                                            servicePayment.setQpay_amount(monto);

                                        } catch (Exception ex) {
                                            Log.e(TAG, "Scan Error: " + ex.getLocalizedMessage());
                                        }
                                    }
                                };
                                getContext().startActivity(ScanActivity.class, false);
                            }
                        }, new String[]{Manifest.permission.CAMERA});

                    }
                });

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        servicePayment.setQpay_verification_digit(campos.get(1).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // ------------------------------ DECLARACION SERVICIO SKY  ----------------------------
            case Globals.SKY_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 12, 12,
                        CViewEditText.TYPE.NUMBER, "Número de referencia", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // ------------------------------ DECLARACION SERVICIO CFE -----------------------------
            case Globals.CFE_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 12, 30,
                        CViewEditText.TYPE.NUMBER, "Número de servicio o referencia", R.string.text_input_required,
                        new CViewEditText.ITextChanged() {
                            @Override
                            public void onChange(String text) {
                                if (!campos.isEmpty())
                                    validateLengths(12,30);
                            }
                        }, ""));
                activeScanner(30, 30, 0, 0, 30);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {


                        buscarMonto(campos.get(0).getText(), new IFunction() {
                            @Override
                            public void execute(Object[] json) {
                                QPAY_ServicePackageResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePackageResponse.class);
                                if (response.getQpay_response().equals("true")) {
                                    String acountNumber = response.getQpay_object().get(0).getAccountNumber();

                                    if (acountNumber.length() == 30){ // Número de referencia (código de barras) 30 dígitos
                                        servicePayment.setQpay_account_number1(acountNumber.substring(0,10));
                                        servicePayment.setQpay_account_number2(acountNumber.substring(10,20));
                                        servicePayment.setQpay_account_number3(acountNumber.substring(20,30));
                                    }else{                            // Número de servicio 12 dígitos
                                        servicePayment.setQpay_account_number1(acountNumber.substring(0,12));
                                        servicePayment.setQpay_account_number2(null);
                                        servicePayment.setQpay_account_number3(null);
                                    }
                                    servicePayment.setQpay_amount(response.getQpay_object().get(0).getVendorAmount());
                                    companiaDTO.setServices(response.getQpay_object().get(0).getServices());
                                    getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));

                                } else {
                                    getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                }
                            }
                        });
                    }
                };

                break;

            // ------------------------------ DECLARACION SERVICIO DISH ----------------------------
            case Globals.DISH_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 14, 14,
                        CViewEditText.TYPE.NUMBER, "Número de cupón", R.string.text_input_required, validate, ""));
                activeScanner(14,14,0,0,14);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        buscarDish(campos.get(0).getText());
                    }
                };

                break;

            // ---------------------------- DECLARACION SERVICIO TELEVIA ---------------------------
            case Globals.TELEVIA_ID:

                layout_scaner.setVisibility(View.VISIBLE);

                campos.add(createCampo(R.id.edit_campo_1, true, 11, 11,
                        CViewEditText.TYPE.NUMBER, "Número de referencia", R.string.text_input_required, validate, ""));

                layout_scaner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {

                                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                                    @Override
                                    public void handleResult(Result result) {

                                        if (result == null)
                                            return;

                                        try {
                                            String resultado = result.getContents().trim();
                                            resultado = resultado.replaceAll("[^0-9]", "").trim();

                                            if (resultado.length() < 11 || resultado.length() > 12) {
                                                getContext().alert("Referencia inválida");
                                                return;
                                            }
                                            resultado = result.getContents().trim().substring(0, 11);
                                            campos.get(0).setText(resultado);

                                        } catch (Exception ex) {
                                            Log.e(TAG, "Scan Error: " + ex.getLocalizedMessage());
                                        }
                                    }
                                };
                                getContext().startActivity(ScanActivity.class, false);
                            }
                        }, new String[]{Manifest.permission.CAMERA});
                    }
                });

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_paquetes.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // ---------------------------- DECLARACION SERVICIO NATURGY ---------------------------
            case Globals.NATURGY_ID:

                edit_campo_2.setVisibility(View.VISIBLE);

                campos.add(createCampo(R.id.edit_campo_1, true, 14, 14,
                        CViewEditText.TYPE.NUMBER, "Naturgy 1 (14 dígitos)", R.string.text_input_required, validate, ""));

                campos.add(createCampo(R.id.edit_campo_2, true, 14, 14,
                        CViewEditText.TYPE.NUMBER, "Naturgy 2 (14 dígitos)", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number1(campos.get(0).getText());
                        servicePayment.setQpay_account_number2(campos.get(1).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // ---------------------------- DECLARACION SERVICIO IZZI ------------------------------
            case Globals.IZZI_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 5, 10,
                        CViewEditText.TYPE.NUMBER, "Número de referencia", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {

                        String izziReference = campos.get(0).getText();
                        //if(campos.get(0).getText().length() != 10)
                        //izziReference = Tools.getPaddingCharacter((10 - campos.get(0).getText().length()), "0") + izziReference;
                        servicePayment.setQpay_account_number(izziReference);
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO MEGACABLE --------------------------
            case Globals.MEGACABLE_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 10, 26,
                        CViewEditText.TYPE.NUMBER, "Número de suscriptor", R.string.text_input_required,
                        new CViewEditText.ITextChanged() {
                            @Override
                            public void onChange(String text) {
                                if (!campos.isEmpty())
                                    validateLengths(10,26);
                            }
                        } , ""));
                activeScanner(26, 26,0,0,26);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        buscarMegacable(campos.get(0).getText());
                    }
                };

                break;

            // ------------------------- DECLARACION SERVICIO PASE URBANO --------------------------
            case Globals.PASE_URBANO_ID:

                layout_scaner.setVisibility(View.VISIBLE);

                campos.add(createCampo(R.id.edit_campo_1, true, 9, 9,
                        CViewEditText.TYPE.NUMBER, "Número de referencia - IMDM", R.string.text_input_required, validate, ""));

                layout_scaner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {
                                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                                    @Override
                                    public void handleResult(Result result) {

                                        if (result == null)
                                            return;

                                        try {
                                            String resultado = result.getContents().trim();
                                            resultado = resultado.replaceAll("[^0-9]", "").trim();

                                            if (resultado.length() != 9) {
                                                getContext().alert("Referencia inválida");
                                                return;
                                            }
                                            campos.get(0).setText(resultado);

                                        } catch (Exception ex) {
                                            Log.e(TAG, "Scan Error: " + ex.getLocalizedMessage());
                                        }
                                    }
                                };
                                getContext().startActivity(ScanActivity.class, false);
                            }
                        }, new String[]{Manifest.permission.CAMERA});

                    }
                });

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        //Adicionar el prefijo para Pase Urbano
                        servicePayment.setQpay_account_number(companiaDTO.getPrefix() + campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_paquetes.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO TOTALPLAY --------------------------
            case Globals.TOTALPLAY_ID:

                layout_scaner.setVisibility(View.VISIBLE);

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 17,
                        CViewEditText.TYPE.NUMBER, "Número de referencia", R.string.text_input_required,
                        new CViewEditText.ITextChanged() {
                            @Override
                            public void onChange(String text) {
                                if (!campos.isEmpty())
                                    validateLengths(10,17);
                            }
                        }, ""));

                layout_scaner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {
                                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                                    @Override
                                    public void handleResult(Result result) {

                                        if (result == null)
                                            return;

                                        try {
                                            String resultado = result.getContents().trim();
                                            resultado = resultado.replaceAll("[^0-9]", "").trim();

                                            if (resultado.length() == 17 || resultado.length() == 10) {

                                            } else {
                                                getContext().alert("Referencia inválida");
                                                return;
                                            }
                                            campos.get(0).setText(resultado);

                                        } catch (Exception ex) {
                                            Log.e(TAG, "Scan Error: " + ex.getLocalizedMessage());
                                        }
                                    }
                                };
                                getContext().startActivity(ScanActivity.class, false);
                            }
                        }, new String[]{Manifest.permission.CAMERA});

                    }
                });

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO STAR TV ----------------------------
            case Globals.STARTV_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 11,
                        CViewEditText.TYPE.NUMBER, "Número de contrato - 819", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        String starTvNumber = campos.get(0).getText().substring(0, 10);
                        if (validatePrefix(starTvNumber)) {
                            buscarMonto(starTvNumber, new IFunction() {
                                @Override
                                public void execute(Object[] json) {
                                    QPAY_ServicePaymentResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePaymentResponse.class);
                                    if (response.getQpay_response().equals("true")) {
                                        servicePayment.setQpay_vendorReference(response.getQpay_object()[0].getVendorReference());
                                        servicePayment.setQpay_account_number(response.getQpay_object()[0].getAccountNumber());
                                        servicePayment.setQpay_amount(response.getQpay_object()[0].getAmount());
                                        servicePayment.setQpay_name_client(response.getQpay_object()[0].getClient());
                                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                    } else {
                                        getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                    }
                                }
                            });
                        }
                    }
                };

                break;

            // ------------------------- DECLARACION SERVICIO CEA QUERETARO-------------------------
            case Globals.CEA_QRO_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 6, 8,
                        CViewEditText.TYPE.NUMBER, "Número de referencia - 224", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        if (validatePrefix(campos.get(0).getText())) {
                            buscarMonto(campos.get(0).getText(), new IFunction() {
                                @Override
                                public void execute(Object[] json) {
                                    QPAY_ServicePaymentResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePaymentResponse.class);
                                    if (response.getQpay_response().equals("true")) {
                                        servicePayment.setQpay_vendorReference(response.getQpay_object()[0].getVendorReference());
                                        servicePayment.setQpay_account_number(response.getQpay_object()[0].getAccountNumber());
                                        servicePayment.setQpay_amount(response.getQpay_object()[0].getAmount());
                                        servicePayment.setQpay_name_client(response.getQpay_object()[0].getClient());
                                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                    } else {
                                        getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                    }
                                }
                            });
                        }
                    }
                };

                break;

            // ---------------------------- DECLARACION SERVICIO NETWEY ----------------------------
            case Globals.NETWAY_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 10,
                        CViewEditText.TYPE.NUMBER, "Número de cuenta - 232", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        if (validatePrefix(campos.get(0).getText())) {
                            buscarMonto(campos.get(0).getText(), new IFunction() {
                                @Override
                                public void execute(Object[] json) {
                                    QPAY_ServicePackageResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePackageResponse.class);
                                    if (response.getQpay_response().equals("true")) {
                                        servicePayment.setQpay_vendorReference(response.getQpay_object().get(0).getVendorReference());
                                        servicePayment.setQpay_account_number(response.getQpay_object().get(0).getAccountNumber());
                                        companiaDTO.setServices(response.getQpay_object().get(0).getServices());
                                        getContext().setFragment(Fragment_pago_servicio_paquetes.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                    } else {
                                        getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                    }
                                }
                            });
                        }
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO VEOLIA -----------------------------
            case Globals.VEOLIA_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 6, 8,
                        CViewEditText.TYPE.NUMBER, "Número de cuenta", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // -------------------------- DECLARACION SERVICIO AMAZON CASH--------------------------
            case Globals.AMAZON_CASH_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 10,
                        CViewEditText.TYPE.NUMBER, "Número de teléfono", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // ---------------------- DECLARACION SERVICIO OPDM TLALNEPANTLA -----------------------
            case Globals.OPDM_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 8, 9,
                        CViewEditText.TYPE.NUMBER, "Ingresa referencia - 222", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        if (validatePrefix(campos.get(0).getText())) {
                            buscarMonto(campos.get(0).getText(), new IFunction() {
                                @Override
                                public void execute(Object[] json) {
                                    QPAY_ServicePaymentResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePaymentResponse.class);
                                    if (response.getQpay_response().equals("true")) {
                                        servicePayment.setQpay_vendorReference(response.getQpay_object()[0].getVendorReference());
                                        servicePayment.setQpay_account_number(response.getQpay_object()[0].getAccountNumber());
                                        servicePayment.setQpay_amount(response.getQpay_object()[0].getAmount());
                                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                    } else {
                                        getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                    }
                                }
                            });
                        }
                    }
                };

                break;

            // -------------------------- DECLARACION SERVICIO PLAN AT&T ---------------------------
            case Globals.POST_ATT_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 10,
                        CViewEditText.TYPE.NUMBER, "Número celular", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        buscarMonto(campos.get(0).getText(), new IFunction() {
                            @Override
                            public void execute(Object[] json) {
                                QPAY_ServicePaymentResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePaymentResponse.class);
                                if (response.getQpay_response().equals("true")) {
                                    servicePayment.setQpay_account_number(response.getQpay_object()[0].getAccountNumber());
                                    servicePayment.setQpay_amount(response.getQpay_object()[0].getVendorAmount());
                                    getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                } else {
                                    getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                }
                            }
                        });
                    }
                };

                break;

            // ------------------------ DECLARACION SERVICIO PLAN MOVISTAR -------------------------
            case Globals.POST_MOVISTAR_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 10, 10,
                        CViewEditText.TYPE.NUMBER, "Número celular", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO SACMEX -----------------------------
            case Globals.SACMEX_ID:
                // -------------------------- DECLARACION SERVICIO GOB CDMX ----------------------------
            case Globals.GOB_MX_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 20, 32,
                        CViewEditText.TYPE.TEXT, "Línea de captura", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;
            // --------------------------- DECLARACION SERVICIO SIAPAGA -----------------------------

            case Globals.SIAPA_GDL_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 22, 22,
                        CViewEditText.TYPE.NUMBER, "Número de código de barras", R.string.text_input_required, validate, ""));
                activeScanner(22, 22, 0, 0,22);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        servicePayment.setQpay_name_product("siapa");
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;
            // --------------------------- DECLARACION SERVICIO AYDM MTY -----------------------------

            case Globals.AYDM_MTY_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 32, 32,
                        CViewEditText.TYPE.NUMBER, "Número de código de barras", R.string.text_input_required, validate, ""));
                activeScanner(32, 32, 0, 0, 32);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        servicePayment.setQpay_name_product("aydm");
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO GOB EDOMEX-----------------------------

            case Globals.GOB_EDOMEX_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 27, 27,
                        CViewEditText.TYPE.NUMBER, "Número de código de barras", R.string.text_input_required, validate, ""));
                activeScanner(27, 27, 0, 0, 27);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        servicePayment.setQpay_name_product("tesoreriaedomx");
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO CESPT -----------------------------

            case Globals.CESPT_TIJ_ID:

                layout_scaner.setVisibility(View.VISIBLE);
                campos.add(createCampo(R.id.edit_campo_1, true, 29, 29,
                        CViewEditText.TYPE.NUMBER, "Número de código de barras", R.string.text_input_required, validate, ""));
                activeScanner(29, 29, 0, 0, 29);

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        servicePayment.setQpay_account_number(campos.get(0).getText());
                        servicePayment.setQpay_name_product("cespt");
                        getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                    }
                };

                break;

            // --------------------------- DECLARACION SERVICIO BLUE TEL -----------------------------

            case Globals.BLUE_TEL_ID:

                campos.add(createCampo(R.id.edit_campo_1, true, 12, 12,
                        CViewEditText.TYPE.NUMBER, "Número de cuenta", R.string.text_input_required, validate, ""));

                functionContinue = new IFunction() {
                    @Override
                    public void execute(Object[] objects) {
                        String blueTelNumber = campos.get(0).getText().substring(0, 3);
                         buscarMonto(campos.get(0).getText(), new IFunction() {
                            @Override
                            public void execute(Object[] json) {
                                QPAY_ServicePackageResponse response = new GsonBuilder().create().fromJson(json[0].toString(), QPAY_ServicePackageResponse.class);
                                if (response.getQpay_response().equals("true")) {
                                    servicePayment.setQpay_account_number(response.getQpay_object().get(0).getAccountNumber());
                                    servicePayment.setQpay_amount(response.getQpay_object().get(0).getVendorAmount());
                                    companiaDTO.setServices(response.getQpay_object().get(0).getServices());
                                    getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));
                                } else {
                                    getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                                }
                            }
                        });
                    }
                };

                break;

        }

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionContinue != null)
                    functionContinue.execute();

            }
        });

    }


    private CViewEditText createCampo(int campo, boolean required, int minimum, int maximum,
                                      CViewEditText.TYPE type, String hint, int alert,
                                      CViewEditText.ITextChanged validate, String text) {
        return CViewEditText.create(getView().findViewById(campo))
                .setRequired(required)
                .setMinimum(minimum)
                .setMaximum(maximum)
                .setType(type)
                .setHint(hint)
                .setAlert(alert)
                .setTextChanged(validate)
                .setText(text);
    }


    public void validate(String text) {

        btn_continuar.setEnabled(false);

        for (CViewEditText edit : campos)
            if (!edit.isValid())
                return;

        btn_continuar.setEnabled(true);

    }


    private void validateLengths(Integer firstLength, Integer secondLength) {
        

        int accountLength = campos.get(0).getText().length();

        if (accountLength == firstLength || accountLength == secondLength) {
            btn_continuar.setEnabled(true);
        } else {
            btn_continuar.setEnabled(false);
        }

    }


    private boolean validatePrefix(String reference) {
        String prefix = companiaDTO.getPrefix();
        reference = reference.substring(0, prefix.length());
        Log.d(TAG, "Prefijo: " + prefix + " InicioRef: " + reference);
        if (reference.compareTo(prefix) == 0) {
            getContextMenu().alert("Por favor no incluyas los primeros dígitos (" + prefix + ") de tu clave");
            return false;
        } else {
            return true;
        }

    }


    public void buscarDish(String referencia) {

        getContext().loading(true);

        final QPAY_ServicePayment request = new QPAY_ServicePayment();
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        request.setQpay_reference_number(referencia);
        request.setQpay_type_search("2");

        try {
            IDishQuery sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_ServicePaymentResponse.QPAY_ServicePaymentResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_ServicePaymentResponse dishStepOneResponse = gson.fromJson(json, QPAY_ServicePaymentResponse.class);

                        if (dishStepOneResponse.getQpay_response().equals("true")) {

                            servicePayment.setQpay_vendorReference(dishStepOneResponse.getQpay_object()[0].getVendorReference());
                            servicePayment.setQpay_account_number(dishStepOneResponse.getQpay_object()[0].getAccountNumber());
                            servicePayment.setQpay_name_client(dishStepOneResponse.getQpay_object()[0].getClientName());
                            String monto = dishStepOneResponse.getQpay_object()[0].getAmount();
                            servicePayment.setQpay_amount(monto);

                            getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));

                        } else {

                            getContext().loading(false);
                            getContextMenu().validaSesion(dishStepOneResponse.getQpay_code(), dishStepOneResponse.getQpay_description());

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            sale.doDishQuery(request);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    public void buscarMegacable(String referencia) {

        getContext().loading(true);

        final QPAY_ServicePayment request = new QPAY_ServicePayment();
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        request.setQpay_reference_number(referencia);
        request.setQpay_type_search(null);

        try {
            IMegacableQuery sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_ServicePaymentResponse.QPAY_ServicePaymentResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_ServicePaymentResponse dishStepOneResponse = gson.fromJson(json, QPAY_ServicePaymentResponse.class);

                        if (dishStepOneResponse.getQpay_response().equals("true")) {

                            servicePayment.setQpay_vendorReference(dishStepOneResponse.getQpay_object()[0].getVendorReference());
                            servicePayment.setQpay_account_number(dishStepOneResponse.getQpay_object()[0].getAccountNumber());
                            servicePayment.setQpay_name_client(dishStepOneResponse.getQpay_object()[0].getClientName());
                            String monto = dishStepOneResponse.getQpay_object()[0].getAmount();
                            servicePayment.setQpay_amount(monto);

                            getContext().setFragment(Fragment_pago_servicio_monto.newInstance((CompaniaDTO) companiaDTO, (QPAY_ServicePayment) servicePayment));

                        } else {
                            getContext().loading(false);
                            getContextMenu().validaSesion(dishStepOneResponse.getQpay_code(), dishStepOneResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            sale.doMegacableQuery(request);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    public void buscarMonto(String referencia, IFunction functionQuery) {

        getContext().loading(true);

        final QPAY_ServicePayment request = new QPAY_ServicePayment();
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        request.setQpay_reference_number(referencia);
        request.setQpay_product(servicePayment.getQpay_product());

        try {
            iServicePayment queryListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        if (functionQuery != null)
                            functionQuery.execute(json);
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            queryListener.doQueryPayment(request);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    // Función para instanciar la activity del scanner
    public void activeScanner(Integer minLenght, Integer maxLenght, Integer indexCampo0, Integer startIndex, Integer endIndex){
        layout_scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        ScanActivity.action = new ZBarScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result result) {
                                try {
                                    Logger.e("code",result);
                                    if (result == null) {
                                        return;
                                    }
                                    String resultado = result.getContents().trim();
                                    resultado = resultado.replaceAll("[^0-9]", "").trim();

                                    if (resultado.length() < minLenght || resultado.length() > maxLenght) {
                                        getContext().alert("Referencia inválida");
                                        return;
                                    }

                                    setTextFromScanner(resultado, indexCampo0, startIndex, endIndex);

                                } catch (Exception ex) {
                                    Log.e(TAG, "Scan Error: " + ex.getLocalizedMessage());
                                }
                            }
                        };
                        getContext().startActivity(ScanActivity.class, false);
                    }
                },new  String[]{Manifest.permission.CAMERA});
            }
        });
    }

    // Función para descomponer el código devuelto por la activity
    public void setTextFromScanner(String code, Integer indexCampo0, Integer startIndex, Integer endIndex) {
        String barCode = "";
        barCode = code.trim().substring(startIndex, endIndex);
        campos.get(indexCampo0).setText(barCode);
    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}