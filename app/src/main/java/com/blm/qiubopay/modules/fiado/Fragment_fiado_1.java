package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.fiado.IListClients;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.fiado.QPAY_Cliente;
import com.blm.qiubopay.models.fiado.QPAY_List_Clientes_Request;
import com.blm.qiubopay.models.fiado.QPAY_List_Clientes_Response;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_1 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;

    public static Fragment_fiado_1 newInstance(Object... data) {
        Fragment_fiado_1 fragment = new Fragment_fiado_1();
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

        view = inflater.inflate(R.layout.fragment_fiado_1, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        LinearLayout ln_new_trust = view.findViewById(R.id.ln_new_trust);
        LinearLayout ln_customer_owed = view.findViewById(R.id.ln_customer_owed);
        LinearLayout ln_all_clients = view.findViewById(R.id.ln_all_clients);

        ln_new_trust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setFragment(Fragment_fiado_2.newInstance());
            }
        });

        ln_customer_owed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClientes(true, context,true, new IFunction<List<QPAY_Cliente>>() {
                    @Override
                    public void execute(List<QPAY_Cliente>[] data) {
                        Fragment_fiado_3.option = 1;
                        Fragment_fiado_3.list = data[0];
                        context.setFragment(Fragment_fiado_3.newInstance());
                    }
                });
            }
        });

        ln_all_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClientes(true, context,false, new IFunction<List<QPAY_Cliente>>() {
                    @Override
                    public void execute(List<QPAY_Cliente>[] data) {
                        Fragment_fiado_3.option = 2;
                        Fragment_fiado_3.list = data[0];
                        context.setFragment(Fragment_fiado_3.newInstance());
                    }
                });
            }
        });

    }

    public static void getClientes(boolean loading, HActivity context, boolean isDebt, final IFunction<List<QPAY_Cliente>> function) {

        context.loading(loading);

        QPAY_List_Clientes_Request data = new QPAY_List_Clientes_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        IListClients sevice = null;
        try {
            sevice = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.d(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_List_Clientes_Response list_clientes_response = gson.fromJson(json, QPAY_List_Clientes_Response.class);

                        if (list_clientes_response.getQpay_response().equals("true")) {

                            if(list_clientes_response.getQpay_object() == null)
                                list_clientes_response.setQpay_object(new QPAY_Cliente[]{});

                            if(function != null)
                                function.execute(Arrays.asList(list_clientes_response.getQpay_object()));
                        } else {

                            if (list_clientes_response.getQpay_code().equals("017")
                                    || list_clientes_response.getQpay_code().equals("018")
                                    || list_clientes_response.getQpay_code().equals("019")
                                    || list_clientes_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.

                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        // context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if (list_clientes_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(list_clientes_response.getQpay_description());
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

        sevice.listClients(data, isDebt);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}