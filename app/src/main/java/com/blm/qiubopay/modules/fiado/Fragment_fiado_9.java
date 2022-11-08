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
import com.blm.qiubopay.listeners.fiado.IRecibo;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.PQPrefijo;
import com.blm.qiubopay.models.fiado.QPAY_Recibo_Request;
import com.blm.qiubopay.models.fiado.QPAY_Recibo_Response;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_9 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<CViewEditText> campos;
    public static ArrayList<PQPrefijo> list;

    private Button btn_send;


    public static Fragment_fiado_9 newInstance(Object... data) {
        Fragment_fiado_9 fragment = new Fragment_fiado_9();
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

        view = inflater.inflate(R.layout.fragment_fiado_9, container, false);

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

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_email))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.EMAIL)
                .setHint(R.string.text_fiado_15)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_phone))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_fiado_16)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));



        campos.get(0).setEnabled(false);
        campos.get(1).setEnabled(false);

        campos.get(0).setText(Fragment_fiado_3.selection.getFiado_mail());
        campos.get(1).setText(Fragment_fiado_3.selection.getFiado_cellphone());

        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recibo(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Recibo\nenviado", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Listo";
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

    }

    private void validate() {

        btn_send.setEnabled(true);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        btn_send.setEnabled(true);
    }

    public void recibo(final IFunction function) {

        context.loading(true);

        QPAY_Recibo_Request data = new QPAY_Recibo_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_payment_id(Fragment_fiado_3.selection.getFiado_payment_id());
        data.setFiado_mail( campos.get(0).getText());
        data.setFiado_cellphone( campos.get(1).getText());

        Logger.d(new Gson().toJson(data));


        IRecibo service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Recibo_Response recibo_response = gson.fromJson(json, QPAY_Recibo_Response.class);

                        Logger.d(new Gson().toJson(recibo_response));

                        if (recibo_response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {

                            if (recibo_response.getQpay_code().equals("017")
                                    || recibo_response.getQpay_code().equals("018")
                                    || recibo_response.getQpay_code().equals("019")
                                    || recibo_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                getContext().alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if (recibo_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //context.alertAEONBlockedUser();
                            } else {
                                context.alert(recibo_response.getQpay_description());
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

        service.recibo(data);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

