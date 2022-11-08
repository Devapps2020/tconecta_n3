package com.blm.qiubopay.modules.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrder_Object;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.devapps.utils.components.HFragment;

public class Fragment_pedidos_bimbo_5 extends HFragment implements IMenuContext {

    public static PedidoRequest order;
    ListView list_pedidos;
    private String brand = "";
    Map<String, QPAY_GetOrder_Object> tickets = new HashMap<>();

    public static Fragment_pedidos_bimbo_5 newInstance() {
        return new Fragment_pedidos_bimbo_5();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pedidos_bimbo_5, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        //2021-12-21 RSB. Analytics unilever
        CApplication.setAnalytics(CApplication.ACTION.Market_MisPedidos);

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        for(QPAY_GetInventory_Object ord : order.getProduct()) {

            QPAY_GetOrder_Object order = new QPAY_GetOrder_Object();

            if(!tickets.containsKey(ord.getIdTicket())){
                order.setProducts(new ArrayList<>());
                order.getProducts().add(ord);
                order.setIdOrder(ord.getIdOrder());
                order.setIdTicket(ord.getIdTicket() + " - " + ord.getOrganizationName() );
                order.setQuantity(ord.getQuantity());
                order.setTotalAmount(ord.getTotalAmount());
                tickets.put(ord.getIdTicket(), order);
            } else {
                order = tickets.get(ord.getIdTicket());
                order.getProducts().add(ord);
                order.setQuantity(order.getQuantity() + ord.getQuantity());
                order.setTotalAmount(order.getTotalAmount() + ord.getTotalAmount());
            }

        }

        List<QPAY_GetOrder_Object> list = new ArrayList<>();

        for (Map.Entry<String, QPAY_GetOrder_Object> entry : tickets.entrySet()) {
            list.add(entry.getValue());
        }

        Collections.sort(list, new Comparator<QPAY_GetOrder_Object>() {
            public int compare(QPAY_GetOrder_Object o1, QPAY_GetOrder_Object o2) {
                return o2.getIdTicket().compareTo(o1.getIdTicket());
            }
        });

        list_pedidos = getView().findViewById(R.id.list_pedidos);
        PedAdapter adapter = new PedAdapter(getContext(), list);
        list_pedidos.setAdapter(adapter);

        list_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PedidoRequest order = new PedidoRequest();
                order.setProduct(list.get(position).getProducts());
                Fragment_pedidos_bimbo_4.numero = list.get(position).getProducts().get(0).getIdTicket();
                Fragment_pedidos_bimbo_4.order = order;
                getContext().setFragment(Fragment_pedidos_bimbo_4.newInstance());
            }
        });

    }

    public class PedAdapter extends ArrayAdapter<QPAY_GetOrder_Object> {

        List<QPAY_GetOrder_Object> pedidos;

        public PedAdapter(Context context, List<QPAY_GetOrder_Object> pedido) {
            super(context, 0, pedido);
            this.pedidos = pedido;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_GetOrder_Object pedido = pedidos.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pedido , parent, false);

            TextView text_name = convertView.findViewById(R.id.text_name);
            TextView text_desc = convertView.findViewById(R.id.text_description);
            TextView text_total = convertView.findViewById(R.id.text_total);

            text_name.setText(Html.fromHtml("<b>Ticket:</b> " + pedido.getIdTicket()));
            text_desc.setText(Html.fromHtml("<b>Can.:</b> " + pedido.getQuantity()));
            text_total.setText(Html.fromHtml("<b>Total:</b> " + Utils.paserCurrency(pedido.getTotalAmount() + "")));

            return convertView;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}