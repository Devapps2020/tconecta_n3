package com.blm.qiubopay.modules.restaurante;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.operative.RestaurantDataHelper;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.operative.PayType;
import com.blm.qiubopay.models.operative.restaurant.DbDetail;
import com.blm.qiubopay.models.operative.restaurant.DbOrder;
import com.blm.qiubopay.models.operative.restaurant.QPAY_SaveTipResponse;
import com.blm.qiubopay.models.operative.restaurant.QPAY_TipOrder;
import com.blm.qiubopay.printers.N3PrinterHelper;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;


public class Fragment_restaurante_3 extends HFragment implements IMenuContext {

    private View view;
    //private HomeActivity context;
    private String data;

    private QPAY_TipOrder order;
    private QPAY_SaveTipResponse saveTipResponse;
    private ArrayList<DbDetail> list;

    public static Fragment_restaurante_3 newInstance(Object... data) {
        Fragment_restaurante_3 fragment = new Fragment_restaurante_3();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_restaurante_3", new Gson().toJson(data[0]));
        if(data.length > 1)
            args.putString("Fragment_restaurante_3_1", new Gson().toJson(data[1]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //this.context = (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            order = new Gson().fromJson(getArguments().getString("Fragment_restaurante_3"), QPAY_TipOrder.class);
            saveTipResponse = new Gson().fromJson(getArguments().getString("Fragment_restaurante_3_1"), QPAY_SaveTipResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_restaurante_3, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().initHome();
            }
        });

        RestaurantDataHelper restaurantDataHelper = new RestaurantDataHelper(getContext());
        DbOrder dbOrder = restaurantDataHelper.getOrderInfo(Integer.parseInt(order.getSell_number()));

        list = restaurantDataHelper.getOrderDetail(dbOrder.getOrder_id());

        ListView list_comanda = view.findViewById(R.id.list_comanda);

        ArrayAdapter adapter  = new DetailAdapter(getContext(), list);

        list_comanda.setAdapter(adapter);

        TextView order_id = view.findViewById(R.id.order_id);
        TextView monto_orden = view.findViewById(R.id.monto_orden);
        TextView numero_clientes = view.findViewById(R.id.numero_clientes);
        TextView fecha_orden = view.findViewById(R.id.fecha_orden);

        order_id.setText(order.getSell_number());
        monto_orden.setText(Utils.paserCurrency(order.getTotal().toString()));
        numero_clientes.setText(""+dbOrder.getCommensal_number());
        fecha_orden.setText(dbOrder.getDate());

        Button btn_terminar = view.findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        //20210115 RSB. Improvements 0121. Ticket restaurante
        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.shareVoucherRestaurante(getContext(), saveTipResponse.getQpay_object()[0].getTicket_number());

            }
        });

        if(Tools.isN3Terminal()) {
            btn_compartir.setVisibility(View.GONE);

            Toast.makeText(getContext(), "Imprimiendo Ticket.", Toast.LENGTH_SHORT).show();
            N3PrinterHelper printer = new N3PrinterHelper(getContextMenu());
            printer.printRestaurantTicket(list, order, dbOrder);
        }

    }

    public class DetailAdapter extends ArrayAdapter<DbDetail> {

        public DetailAdapter(Context context, List<DbDetail> detail) {
            super(context, 0, detail);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurante_order_detail_item, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView subtotal = (TextView) convertView.findViewById(R.id.subtotal);
            TextView propina = (TextView) convertView.findViewById(R.id.propina);
            TextView total = (TextView) convertView.findViewById(R.id.total);
            TextView personas_cubiertas = (TextView) convertView.findViewById(R.id.personas_cubiertas);

            DbDetail detail = getItem(position);

            name.setText("Persona " + (position+1) + " - Forma de pago: " + (detail.getPayment_type() == PayType.CASH ? "EFECTIVO" : "TARJETA"));
            personas_cubiertas.setText("("+detail.getCommensals_no()+")");
            subtotal.setText(Utils.paserCurrency(detail.getAmount().toString()));
            propina.setText(Utils.paserCurrency(detail.getTip_amount().toString()));
            total.setText(Utils.paserCurrency(detail.getTotal().toString()));

            return convertView;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}

