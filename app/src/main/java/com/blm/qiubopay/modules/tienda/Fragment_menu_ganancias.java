package com.blm.qiubopay.modules.tienda;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.CalcularActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.DynamicDataSaleResponse;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_CommissionReport_Object;
import com.blm.qiubopay.models.QPAY_CommissionReport_Tae;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsDetail_Object;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_ganancias extends HFragment implements IMenuContext {

    public static QPAY_CommissionReport_Object response;
    public List<QPAY_CommissionReport_Tae> list = new ArrayList<>();

    private ListView list_ganancias;

    public static Fragment_menu_ganancias newInstance() {
        return new Fragment_menu_ganancias();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_ganancias, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo().setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView text_costo_total = getView().findViewById(R.id.text_costo_total);
        text_costo_total.setText(Utils.paserCurrency(getContextMenu().getMisBeneficios()));

        response.getTAE().addAll(response.getBP());
        list.addAll(response.getTAE());

        list_ganancias = getView().findViewById(R.id.list_ganancias);
        gananciasAdapter adapter = new gananciasAdapter(getContext(), list);
        list_ganancias.setAdapter(adapter);

        list_ganancias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_CommissionReport_Tae data = list.get(position);


            }
        });

        Utils.setListViewHeightBasedOnChildren(list_ganancias);

    }

    public class gananciasAdapter extends ArrayAdapter<QPAY_CommissionReport_Tae> {

        List<QPAY_CommissionReport_Tae> compras;

        public gananciasAdapter(Context context,List<QPAY_CommissionReport_Tae> ganancia) {
            super(context,0, ganancia);
            this.compras=ganancia;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_CommissionReport_Tae ganancia = compras.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_ganancias , parent, false);

            TextView text_description = convertView.findViewById(R.id.text_description);
            TextView text_date = convertView.findViewById(R.id.text_date);
            TextView text_monto = convertView.findViewById(R.id.text_monto);
            TextView text_costo = convertView.findViewById(R.id.text_costo);

            text_description.setText(Html.fromHtml(ganancia.getProduct().toUpperCase()));
            text_date.setText(ganancia.getTransactionDate());
            text_monto.setText(Utils.paserCurrency(ganancia.getTransactionAmount()));
            text_costo.setText(Utils.paserCurrency(ganancia.getCommissionAmount()));

            return convertView;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}