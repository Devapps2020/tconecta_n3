package com.blm.qiubopay.modules.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IOrder;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PedidoDTO;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.bimbo.PedidoResponse;
import com.blm.qiubopay.models.bimbo.ProductoDTO;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_pedidos_bimbo_3 extends HFragment implements IMenuContext {

    private PedidoRequest order;
    TextView text_subtotal;
    TextView text_total;
    double total;
    public static boolean directo;


    public static Fragment_pedidos_bimbo_3 newInstance(Object... data) {
        Fragment_pedidos_bimbo_3 fragment = new Fragment_pedidos_bimbo_3();
        Bundle args = new Bundle();

        if (data.length > 0)
            args.putString("Fragment_pedidos_bimbo_3", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pedidos_bimbo_3, container, false));
    }

    public void initFragment() {

        //2021-12-21 RSB. Analytics unilever
        CApplication.setAnalytics(CApplication.ACTION.Market_ConfirmaTuPedido);

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        Button btn_send = getView().findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().showConfirmBimbo("Confirmación de pedido", "Tu pedido actual es de " + Utils.paserCurrency("" + total) + "\n ¿Desea enviar el pedido?", "Enviar pedido", "Modificar Pedido", new IClickView() {
                    @Override
                    public void onClick(Object... data) {
                        order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());
                        sendOrder(order);
                    }
                }, new IClickView() {
                    @Override
                    public void onClick(Object... data) {

                        if(!directo)
                            getContextMenu().backFragment();

                        getContextMenu().backFragment();

                        directo = false;

                    }
                });
            }
        });

        order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());

        text_subtotal = getView().findViewById(R.id.text_subtotal);
        text_total = getView().findViewById(R.id.text_total);

        setList();

    }

    public void setList() {

        ListView list_pedidos = getView().findViewById(R.id.list_pedidos);
        ArrayAdapter adapter = new PedidosAdapter(getContext(), order.getProduct());
        list_pedidos.setAdapter(adapter);

        Utils.setListViewHeightBasedOnChildren(list_pedidos);

        updateMontos();
    }

    public void updateMontos() {

        total = 0;

        for (QPAY_GetInventory_Object pro : order.getProduct())
            total += pro.getTotalAmount();

        text_subtotal.setText(Utils.paserCurrency("" + total));
        text_total.setText(Utils.paserCurrency("" + total));

        if(total == 0) {
            getContextMenu().showAlertBimbo("Incrementa tus ventas", "Has eliminado todos tus producto.", new IClickView() {
                @Override
                public void onClick(Object... data) {
                    getContextMenu().initHome();
                }
            });
        }

    }

    public void sendOrder(PedidoRequest request){

        getContextMenu().loading(true);

        try {

            request.setIdBimbo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            request.setBlmId(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());

            List<QPAY_GetInventory_Object> products = new ArrayList<>();

            for(QPAY_GetInventory_Object pro : request.getProduct()) {
                QPAY_GetInventory_Object producto = new QPAY_GetInventory_Object();
                producto.setIdProductInt(pro.getIdProductInt());
                producto.setIdOrganization(pro.getIdOrganization());
                producto.setSku(pro.getSku());
                producto.setIdCategory(pro.getIdCategory());
                producto.setPriceInit(pro.getPrice());
                producto.setTotalAmount(pro.getTotalAmount());
                producto.setQuantity(pro.getQuantity());
                products.add(producto);
            }

            request.setProduct(products);

            IOrder petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        PedidoResponse response = new Gson().fromJson(json, PedidoResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            Fragment_pedidos_bimbo_4.numero = response.getQpay_description();
                            Fragment_pedidos_bimbo_4.order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());
                            AppPreferences.clearOrder(Globals.CATEGORIAS.TODAS.ordinal());
                            getContext().setFragment(Fragment_pedidos_bimbo_4.newInstance(), true);

                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContextMenu().loading(false);

                    getContextMenu().alert(R.string.general_error);
                }
            }, getContext());

            petition.sendOrder(request);

        } catch (Exception e) {
            e.printStackTrace();

            getContextMenu().loading(false);

            getContextMenu().alert(R.string.general_error_catch);
        }

    }

    public void saveOrder(PedidoRequest order) {

        List<QPAY_GetInventory_Object> pedidos = new ArrayList();

        for (QPAY_GetInventory_Object ped : order.getProduct())
            if (ped.getQuantity() > 0)
                pedidos.add(ped);

        order.setProduct(pedidos);

        AppPreferences.setOrder(Globals.CATEGORIAS.TODAS.ordinal(), order);

        this.order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());

        setList();

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class PedidosAdapter extends ArrayAdapter<QPAY_GetInventory_Object> {

        List<QPAY_GetInventory_Object> pedidos;

        public PedidosAdapter(Context context, List<QPAY_GetInventory_Object> data) {
            super(context, 0, data);

            this.pedidos = data;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContextMenu().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_ticket_bimbo, parent, false);

            QPAY_GetInventory_Object pedido = pedidos.get(position);

            Button btn_add = convertView.findViewById(R.id.btn_add);
            LinearLayout layout_cantidad = convertView.findViewById(R.id.layout_cantidad);
            TextView cantidad = convertView.findViewById(R.id.edit_cantidad);
            TextView text_nombre = convertView.findViewById(R.id.text_nombre);
            TextView text_piezas = convertView.findViewById(R.id.text_piezas);
            TextView text_costo = convertView.findViewById(R.id.text_costo);
            ImageView btn_remove_pedi = convertView.findViewById(R.id.btn_remove_pedi);

            TextView text_cantidad_label = convertView.findViewById(R.id.text_cantidad_label);

            cantidad.setText(pedido.getQuantity() + "");
            cantidad.setEnabled(false);
            text_nombre.setText(pedido.getShortName());
            text_piezas.setText(pedido.getSize() + " piezas");
            text_costo.setText("Monto: " + Utils.paserCurrency(pedido.getTotalAmount() + ""));

            text_cantidad_label.setVisibility(View.GONE);
            btn_add.setVisibility(View.GONE);
            layout_cantidad.setVisibility(View.VISIBLE);

            btn_remove_pedi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order.getProduct(pedido.getIdProductInt()).setQuantity(0);
                    saveOrder(order);
                }
            });

            return convertView;
        }

    }

}