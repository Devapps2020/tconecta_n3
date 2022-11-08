package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


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
import com.blm.qiubopay.listeners.fiado.INewClient;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.fiado.QPAY_Cliente;
import com.blm.qiubopay.models.fiado.QPAY_Detail_Cliente_Request;
import com.blm.qiubopay.models.fiado.QPAY_New_Cliente_Response;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_3 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;

    public static IFunction reload;
    public static List<QPAY_Cliente> list;
    public static List<QPAY_Cliente> buscar;

    private ListView list_clientes;
    private CViewEditText editBuscar;
    public TextView text_name;
    public static int option;
    private ClientesAdapter adapter;
    public static QPAY_Cliente selection;

    public static Fragment_fiado_3 newInstance(Object... data) {
        Fragment_fiado_3 fragment = new Fragment_fiado_3();
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

        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_fiado_3, container, false);

        initFragment();

        return view;
    }

    public void initFragment() {

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });
        text_name = view.findViewById(R.id.text_client_name);

        switch (option) {
            case 1:
                text_name.setText(Html.fromHtml("Clientes con <br><b>Adeudo<b>"));
                break;
            case 2:
                text_name.setText(Html.fromHtml("Todos los <br><b>Clientes<b>"));
                break;
            case 3:
                text_name.setText(Html.fromHtml("Clientes <br><b>Existentes<b>"));
                break;

        }

        buscar = list;

        list_clientes = view.findViewById(R.id.list_clientes);
        adapter = new ClientesAdapter(context, buscar);
        list_clientes.setAdapter(adapter);

        list_clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                selection = buscar.get(position);

                switch (option) {
                    case 1:
                    case 2:
                        context.setFragment(Fragment_fiado_5.newInstance());
                        break;
                    case 3:
                        Fragment_fiado_10.isNew = false;
                        context.setFragment(Fragment_fiado_10.newInstance());
                        break;
                }
            }
        });

        editBuscar = CViewEditText.create(view.findViewById(R.id.edit_buscar))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint("Buscar")
                .setIcon(R.drawable.ic_search)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        buscar = buscar(text);

                        adapter = new ClientesAdapter(context, buscar);
                        list_clientes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });

        reload = new IFunction() {
            @Override
            public void execute(Object[] data) {

                Fragment_fiado_1.getClientes(false, context,option == 1, new IFunction<List<QPAY_Cliente>>() {
                    @Override
                    public void execute(List<QPAY_Cliente>[] data) {
                        editBuscar.setText("");
                        list = data[0];
                        adapter = new ClientesAdapter(context, list);
                        list_clientes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        };

    }

    public List<QPAY_Cliente> buscar(String buscar){

        if(buscar.isEmpty()){
            return list;
        } else {

            ArrayList<QPAY_Cliente> filtro = new ArrayList();

            for (QPAY_Cliente item : list){
                if(item.getFiado_name().toLowerCase().contains(buscar.toLowerCase())){
                    filtro.add(item);
                }

            }

            return filtro;
        }
    }

    public void detalleCliente(final IFunction function) {

        context.loading(true);

        QPAY_Detail_Cliente_Request data = new QPAY_Detail_Cliente_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_mail(selection.getFiado_mail());

        INewClient service = null;
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
                        QPAY_New_Cliente_Response new_cliente_response = gson.fromJson(json, QPAY_New_Cliente_Response.class);

                        if (new_cliente_response.getQpay_response().equals("true")) {

                            selection = new_cliente_response.getQpay_object()[0];

                            if(function != null)
                                function.execute();

                        } else {

                            if (new_cliente_response.getQpay_code().equals("017")
                                    || new_cliente_response.getQpay_code().equals("018")
                                    || new_cliente_response.getQpay_code().equals("019")
                                    || new_cliente_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
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

                            } else if (new_cliente_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //   context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(new_cliente_response.getQpay_description());
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

        service.newCliente(data);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class ClientesAdapter extends ArrayAdapter<QPAY_Cliente> {

        List<QPAY_Cliente> clientes;

        public ClientesAdapter(Context context, List<QPAY_Cliente> clientes) {
            super(context, 0, clientes);
            this.clientes = clientes;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_fiado, parent, false);

            TextView name = convertView.findViewById(R.id.text_name);
            name.setText(clientes.get(position).getFiado_name());

            return convertView;
        }

    }
}

