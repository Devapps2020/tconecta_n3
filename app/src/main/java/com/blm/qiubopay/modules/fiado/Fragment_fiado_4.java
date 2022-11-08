package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.fiado.IClient;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.PQPrefijo;
import com.blm.qiubopay.models.fiado.QPAY_Cliente;
import com.blm.qiubopay.models.fiado.QPAY_Cliente_Request;
import com.blm.qiubopay.models.fiado.QPAY_Cliente_Response;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_4 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<CViewEditText> campos;
    public static ArrayList<PQPrefijo> list;

    Button btn_cancell;
    Button btn_confirm;

    public static Fragment_fiado_4 newInstance(Object... data) {
        Fragment_fiado_4 fragment = new Fragment_fiado_4();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_fiado_4, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        campos = new ArrayList();


        btn_confirm = view.findViewById(R.id.btn_confirm);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_nombre))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.personal_info_1)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_email))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_fiado_15)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_telefono))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_tipo_cuenta)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClient(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("¿Quieres\nfiarle a " + campos.get(0).getText() + " ahora?", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Si";
                            }

                            @Override
                            public void onClick() {
                                Fragment_fiado_3.selection = new QPAY_Cliente();
                                Fragment_fiado_3.selection.setFiado_mail(campos.get(1).getText());
                                Fragment_fiado_3.selection.setFiado_name(campos.get(0).getText());
                                Fragment_fiado_3.selection.setFiado_cellphone(campos.get(2).getText());
                                Fragment_fiado_10.isNew = true;
                                context.setFragment(Fragment_fiado_10.newInstance());
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Despues";
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().initHome();
                            }
                        });

                    }
                });
            }
        });

        btn_cancell = view.findViewById(R.id.btn_cancell);
        btn_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.backFragment();
                context.backFragment();
            }
        });


    }

    public void createClient(final IFunction function) {

        context.loading(true);

        QPAY_Cliente_Request data = new QPAY_Cliente_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_cellphone(campos.get(2).getText());
        data.setFiado_mail(campos.get(1).getText());
        data.setFiado_name(campos.get(0).getText());

        IClient service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.d(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Cliente_Response cliente_response = gson.fromJson(json, QPAY_Cliente_Response.class);

                        if (cliente_response.getQpay_response().equals("true") && cliente_response.getQpay_code().equals("000")) {

                            if(function != null)
                                function.execute(cliente_response);


                        } else {

                            if (cliente_response.getQpay_code().equals("017")
                                    || cliente_response.getQpay_code().equals("018")
                                    || cliente_response.getQpay_code().equals("019")
                                    || cliente_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return ("Aceptar");
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });

                            } else if (cliente_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                // context.alertAEONBlockedUser();
                            } else {
                                context.alert(cliente_response.getQpay_description());
                            }


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

        service.client(data);

    }


    public void validate(String text) {

        btn_confirm.setEnabled(false);
        btn_cancell.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        btn_confirm.setEnabled(true);
        btn_cancell.setEnabled(true);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity)getContext();
    }
}

