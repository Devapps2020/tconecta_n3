package com.blm.qiubopay.modules.tienda;

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

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetCheckTicketDetail;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketDetail;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClient;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClientResponse;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClient_Object;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsDetailResponse;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsDetail_Object;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_mis_compras_ticket extends HFragment {


    public static  List<QPAY_CheckTicketsDetail_Object> list = new ArrayList<>();
    public static String total = "";

    ListView list_compras_ticket;


    public static Fragment_mis_compras_ticket newInstance() {
        Fragment_mis_compras_ticket fragment = new Fragment_mis_compras_ticket();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_purchases_ticket, container, false));
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        }).showLogo();

        TextView text_costo_total = getView().findViewById(R.id.text_costo_total);
        text_costo_total.setText(total);

        list_compras_ticket = getView().findViewById(R.id.list_compras_ticket);
        ComprasTicketAdapter adapter = new ComprasTicketAdapter(getContext(), list);
        list_compras_ticket.setAdapter(adapter);

        list_compras_ticket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_CheckTicketsDetail_Object data = list.get(position);


            }
        });

        Utils.setListViewHeightBasedOnChildren(list_compras_ticket);


    }

    public class ComprasTicketAdapter extends ArrayAdapter<QPAY_CheckTicketsDetail_Object> {

        List<QPAY_CheckTicketsDetail_Object> compras;

        public ComprasTicketAdapter(Context context, List<QPAY_CheckTicketsDetail_Object> compras) {
            super(context, 0, compras);
            this.compras = compras;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_CheckTicketsDetail_Object compra = compras.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_ticket , parent, false);

            TextView text_description = convertView.findViewById(R.id.text_description);
            TextView text_costo = convertView.findViewById(R.id.text_costo);
            TextView text_cantidad = convertView.findViewById(R.id.text_cantidad);
            TextView text_costo_total = convertView.findViewById(R.id.text_costo_total);

            TextView text_costo_tax = convertView.findViewById(R.id.text_costo_tax);

            text_description.setText(Html.fromHtml("<b>" + compra.getProduct_code() + "</b>") + compra.getProduct_desc());
            text_costo.setText(Utils.paserCurrency(compra.getSale_price()));
            text_cantidad.setText(compra.getSale_quantity().trim());
            text_costo_tax.setText(Utils.paserCurrency(compra.getTax()));

            double total = new Double(compra.getInvoice_line_amt()) + new Double(compra.getTax());

            text_costo_total.setText(Utils.paserCurrency(total + ""));


            return convertView;
        }
    }



}

