package com.blm.qiubopay.modules.tienda;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetCheckTicketDetail;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketDetail;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClient_Object;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsDetailResponse;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_mis_compras_detalle extends HFragment implements IMenuContext {

    public static String name = "";
    public static  List<QPAY_CheckTicketsClient_Object> list = new ArrayList<>();

    ListView list_compras_detalle;

    public static Fragment_mis_compras_detalle newInstance() {
        Fragment_mis_compras_detalle fragment = new Fragment_mis_compras_detalle();
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
        return super.onCreated(inflater.inflate(R.layout.fragment_purchases_2, container, false));
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

        TextView text_title = getView().findViewById(R.id.text_title);
        text_title.setText(Html.fromHtml("Mis compras<br><b>" + name + "</b>"));

        list_compras_detalle = getView().findViewById(R.id.list_compras_detalle);
        ComprasDetalleAdapter adapter = new ComprasDetalleAdapter(getContext(), list);
        list_compras_detalle.setAdapter(adapter);

        list_compras_detalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_CheckTicketsClient_Object data = list.get(position);
                double total = new Double(data.getInv_total()) + new Double(data.getInv_tot_tax());
                Fragment_mis_compras_ticket.total = Utils.paserCurrency(total + "");

                getTicket(data.getInvoice_number(), new IFunction<QPAY_CheckTicketsDetailResponse>() {
                    @Override
                    public void execute(QPAY_CheckTicketsDetailResponse... data) {
                        Fragment_mis_compras_ticket.list = Arrays.asList(data[0].getQpay_object());
                        getContext().setFragment(Fragment_mis_compras_ticket.newInstance());
                    }
                });

            }
        });

    }

    public class ComprasDetalleAdapter extends ArrayAdapter<QPAY_CheckTicketsClient_Object> {

        List<QPAY_CheckTicketsClient_Object> compras;

        public ComprasDetalleAdapter(Context context, List<QPAY_CheckTicketsClient_Object> compras) {
            super(context, 0, compras);
            this.compras = compras;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_CheckTicketsClient_Object compra = compras.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_compras_datail , parent, false);

            TextView text_fecha = convertView.findViewById(R.id.text_fecha);
            TextView text_amount = convertView.findViewById(R.id.text_amount);
            TextView text_ruta = convertView.findViewById(R.id.text_ruta);
            TextView text_amount_tax = convertView.findViewById(R.id.text_amount_tax);

            try {

                String year = compra.getDoc_date().substring(0, 4);
                String month = compra.getDoc_date().substring(4, 6);
                String day = compra.getDoc_date().substring(6, 8);

                text_fecha.setText("Fecha: " + day +"/" + month + "/" + year );

            } catch (Exception ex) {
                text_fecha.setText("Fecha: " + compra.getDoc_date());
            }

            text_ruta.setText("Ruta: " + compra.getRoute_identifier());
            text_amount_tax.setText("IEPS: " + Utils.paserCurrency(compra.getInv_tot_tax() + ""));

            double total = new Double(compra.getInv_total()) + new Double(compra.getInv_tot_tax());

            text_amount.setText("Total: " + Utils.paserCurrency(total + ""));


            return convertView;
        }
    }

    public void getTicket(String folio_number, IFunction<QPAY_CheckTicketsDetailResponse> function){

        getContext().loading(true);

        try {

            QPAY_CheckTicketDetail data = new QPAY_CheckTicketDetail();
            data.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            data.setFolio_number(folio_number);

            IGetCheckTicketDetail petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_CheckTicketsDetailResponse response = new Gson().fromJson(json, QPAY_CheckTicketsDetailResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getCheckTicketDetail(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}

