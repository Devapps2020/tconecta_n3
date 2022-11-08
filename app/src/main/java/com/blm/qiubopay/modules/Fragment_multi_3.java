package com.blm.qiubopay.modules;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkCode;
import com.blm.qiubopay.models.QPAY_ValidateCodeResponse;
import com.blm.qiubopay.utils.WSHelper;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_multi_3 extends HFragment {

    private View view;
    private MenuActivity context;

    private CViewEditText edit_codigo;
    private Button btn_continuar;


    private LinkedUser linkedUser;

    public Fragment_multi_3() {
        // Required empty public constructor
    }

    public static Fragment_multi_3 newInstance(Object... data) {
        Fragment_multi_3 fragment = new Fragment_multi_3();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_multi_3", new Gson().toJson(data[0]));

        fragment.setArguments(args);*/
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

        //if (getArguments() != null)
        //data = new Gson().fromJson(getArguments().getString("Fragment_multi_3"), QPAY_NewUser.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_tc_3, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        CApplication.setAnalytics(CApplication.ACTION.CB_AGREGAR_PATRON_inician);


        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                context.backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        edit_codigo = (CViewEditText.create(view.findViewById(R.id.edit_multi_codigo))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_multi_3_codehint)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_continuar = view.findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final QPAY_LinkCode linkCode = new QPAY_LinkCode();
                linkCode.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                linkCode.setQpay_link_code(edit_codigo.getText());

                validateCode(linkCode, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        if(linkedUser == null){ return; }

                        String nombrePatron = linkedUser.getQpay_name() + " "
                                + linkedUser.getQpay_father_surname() + " "
                                + linkedUser.getQpay_mother_surname();

                        String negocio = linkedUser.getQpay_merchant_name();

                        String mensajeConfirmacion;
                        if (negocio!=null && !negocio.isEmpty()) {
                            mensajeConfirmacion = getString(R.string.multi_3_confirm_1)
                                    .replace("**negocio**",negocio)
                                    .replace("**patron**",nombrePatron);
                        } else {
                            mensajeConfirmacion = getString(R.string.multi_3_confirm_2)
                                    .replace("**patron**",nombrePatron);
                        }

                        context.alert(mensajeConfirmacion, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {

                                CApplication.setAnalytics(CApplication.ACTION.CB_AGREGAR_PATRON_agregan);

                                confirmLinkUser(linkCode, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.alert(R.string.multi_3_success, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {

                                                context.initHome();

                                            }
                                        });
                                    }
                                });

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
        });

        LinearLayout layout_escanner = view.findViewById(R.id.layout_multi_scaner);

        layout_escanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {

                        ScanActivity.action = new ZBarScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result result) {

                                if (result == null)
                                    return;

                                try {

                                    //String codigo = result.getContents();

                                    edit_codigo.setText("" + result.getContents());

                                    btn_continuar.setEnabled(true);

                                }catch (Exception e) {}
                            }
                        };

                        context.startActivity(ScanActivity.class, false);

                    }
                }, new String[] {Manifest.permission.CAMERA});
            }
        });



    }

    /**
     * Metodo para validar el código y mostrar la información del Patrón
     * @param data
     * @param function
     */
    public void validateCode(QPAY_LinkCode data, final IFunction function) {

        context.loading(true);

        IMultiUserListener validateCodeListener = null;
        try {
            validateCodeListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_ValidateCodeResponse validateCodeResponse = gson.fromJson(json, QPAY_ValidateCodeResponse.class);

                        if (validateCodeResponse.getQpay_response().equals("true")) {

                            linkedUser = validateCodeResponse.getQpay_object()[0];

                            if(function != null)
                                function.execute();

                        } else {

                            if("007".equals(validateCodeResponse.getQpay_code()))
                                validateCodeResponse.setQpay_code("001");

                            context.validaSesion(validateCodeResponse.getQpay_code(), validateCodeResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        validateCodeListener.validateLinkCode(data);

    }

    /**
     * Metodo para confirmar el vinculo hacia el administrador
     * @param data
     * @param function
     */
    private void confirmLinkUser(QPAY_LinkCode data, final IFunction function) {

        context.loading(true);

        IMultiUserListener confirmLinkUserListener = null;
        try {
            confirmLinkUserListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_ValidateCodeResponse validateCodeResponse = gson.fromJson(json, QPAY_ValidateCodeResponse.class);

                        if (validateCodeResponse.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            context.validaSesion(validateCodeResponse.getQpay_code(), validateCodeResponse.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        confirmLinkUserListener.confirmLinkUser(data);

    }

    private void validate(){

        if(edit_codigo.isValid()){
            btn_continuar.setEnabled(true);
    } else {
        btn_continuar.setEnabled(false);
    }
    }

}
