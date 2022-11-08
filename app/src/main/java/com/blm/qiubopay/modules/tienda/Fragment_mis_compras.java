package com.blm.qiubopay.modules.tienda;

import android.content.Context;
import android.os.Bundle;
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
import com.blm.qiubopay.listeners.IGetCheckTicketClient;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClient;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClientResponse;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer_Object;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_mis_compras extends HFragment implements IMenuContext {

    ListView list_compras;
    public static  List<QPAY_SalesRetailer_Object> list = new ArrayList<>();

    public static Fragment_mis_compras newInstance() {
        Fragment_mis_compras fragment = new Fragment_mis_compras();
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
        return super.onCreated(inflater.inflate(R.layout.fragment_purchases_1, container, false));
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

        list_compras = getView().findViewById(R.id.list_compras);
        ComprasAdapter adapter = new ComprasAdapter(getContext(), list);
        list_compras.setAdapter(adapter);

        list_compras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_SalesRetailer_Object data = list.get(position);
                getDetalleCompra(data.getOrganization_id(), new IFunction<QPAY_CheckTicketsClientResponse>() {
                    @Override
                    public void execute(QPAY_CheckTicketsClientResponse... reponse) {
                        Fragment_mis_compras_detalle.name = data.getName();
                        Fragment_mis_compras_detalle.list = Arrays.asList(reponse[0].getQpay_object());
                        getContext().setFragment(Fragment_mis_compras_detalle.newInstance());
                    }
                });


            }
        });


    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity)getContext();
    }

    public class ComprasAdapter extends ArrayAdapter<QPAY_SalesRetailer_Object> {

        List<QPAY_SalesRetailer_Object> compras;

        public ComprasAdapter(Context context, List<QPAY_SalesRetailer_Object> compras) {
            super(context, 0, compras);
            this.compras = compras;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_SalesRetailer_Object compra = compras.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_compras , parent, false);

            ImageView img_marca = convertView.findViewById(R.id.img_marca);
            TextView text_marca = convertView.findViewById(R.id.text_marca);
            TextView text_amount = convertView.findViewById(R.id.text_amount);

            switch (compra.getOrganization_id()) {
                case "OBM":
                    compra.setName("Bimbo");
                    text_marca.setText("Mis compras Bimbo");
                    img_marca.setImageDrawable(getContext().getResources().getDrawable(R.drawable.marca_bimbo));
                    break;
                case "OBL":
                    compra.setName("Barcel");
                    text_marca.setText("Mis compras Barcel");
                    img_marca.setImageDrawable(getContext().getResources().getDrawable(R.drawable.marca_barcel));
                    break;
                case "RIC":
                    compra.setName("Ricolino");
                    text_marca.setText("Mis compras Ricolino");
                    img_marca.setImageDrawable(getContext().getResources().getDrawable(R.drawable.marca_ricolino));
                    break;
            }


            text_amount.setText(Utils.paserCurrency(compra.getSale_amount() + ""));

            return convertView;
        }
    }

    public void getDetalleCompra(String organizationId, IFunction<QPAY_CheckTicketsClientResponse> function){

        getContext().loading(true);

        try {

            QPAY_CheckTicketsClient data = new QPAY_CheckTicketsClient();
            data.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            data.setOrganization_id(organizationId);

            IGetCheckTicketClient petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_CheckTicketsClientResponse response = new Gson().fromJson(json, QPAY_CheckTicketsClientResponse.class);

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

            petition.getCheckTicketClient(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

}

