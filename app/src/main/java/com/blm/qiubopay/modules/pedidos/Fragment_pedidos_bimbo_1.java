package com.blm.qiubopay.modules.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IGetInventory;
import com.blm.qiubopay.listeners.IGetOrder;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories_Object;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventoryResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrder;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrderResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization_Object;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IFunction;

public class Fragment_pedidos_bimbo_1 extends HFragment implements IMenuContext {

    ListView list_categorias;
    public static  List<QPAY_GetOrganization_Object> organizations = new ArrayList<>();
    public static  List<QPAY_GetCategories_Object> list = new ArrayList<>();

    private TextView text_number_car, text_indicator_car;
    private ImageView img_buy_car;
    Button btn_confirmar;

    private PedidoRequest order;



    public static Fragment_pedidos_bimbo_1 newInstance(Object... data) {
       Fragment_pedidos_bimbo_1 fragment = new Fragment_pedidos_bimbo_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_deseos_bimbo_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pedidos_bimbo_1, container, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        try { updateBuyCar(); } catch (Exception ex) { }
    }

    @Override
    public void initFragment(){

        //2021-12-21 RSB. Analytics unilever
        CApplication.setAnalytics(CApplication.ACTION.Market_Categorias);

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());

                if(order.getProduct() == null || order.getProduct().isEmpty()){
                    getContext().alert("Agregar al menos 1 producto para continuar!");
                    return;
                }

                Fragment_pedidos_bimbo_3.directo = false;
                getContext().setFragment(Fragment_pedidos_bimbo_3.newInstance());

            }
        });



        text_indicator_car = getView().findViewById(R.id.text_indicator_car);
        text_number_car = getView().findViewById(R.id.text_number_car);
        img_buy_car = getView().findViewById(R.id.img_buy_car);
        img_buy_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pedidos_bimbo_3.directo = true;
                getContext().setFragment(Fragment_pedidos_bimbo_3.newInstance());
            }
        });
        Button btn_pedidos = getView().findViewById(R.id.btn_pedidos);

        btn_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOders();

            }
        });

        list_categorias = getView().findViewById(R.id.list_categorias);
        CatAdapter adapter = new CatAdapter(getContext(), list);
        list_categorias.setAdapter(adapter);

        list_categorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_GetCategories_Object data = list.get(position);
                Fragment_pedidos_bimbo_2.nombre = data.getName();
                getProducts(data.getIdCategory(),new IFunction<QPAY_GetInventoryResponse>() {
                    @Override
                    public void execute(QPAY_GetInventoryResponse... data) {
                        Fragment_pedidos_bimbo_2.list = ((QPAY_GetInventoryResponse) data[0]).getQpay_object()[0];

                        if(Fragment_pedidos_bimbo_2.list == null || Fragment_pedidos_bimbo_2.list.isEmpty()){
                            getContextMenu().alert("No se encontraron productos en esta categoría");
                            return;
                        }
                        else{
                            getContext().setFragment(Fragment_pedidos_bimbo_2.newInstance());
                        }

                    }
                });
            }
        });

        updateBuyCar();

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                onResume();
            }
        });

    }

    public void updateBuyCar() {

        PedidoRequest order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());

        if(order.getProduct() == null || order.getProduct().isEmpty()){
            text_number_car.setVisibility(View.INVISIBLE);
            text_indicator_car.setVisibility(View.INVISIBLE);
            img_buy_car.setEnabled(false);
        } else {
            int count = 0;
            for (QPAY_GetInventory_Object pedido : order.getProduct())
                count += pedido.getQuantity();

            if(count == 0){
                img_buy_car.setEnabled(false);
                text_indicator_car.setVisibility(View.INVISIBLE);
                text_number_car.setVisibility(View.INVISIBLE);
            } else {
                img_buy_car.setEnabled(true);
                text_indicator_car.setVisibility(View.VISIBLE);
                text_number_car.setVisibility(View.VISIBLE);
            }

            text_number_car.setText(count + "");
        }

    }

    public class CatAdapter extends ArrayAdapter<QPAY_GetCategories_Object> {

        List<QPAY_GetCategories_Object> categorias;

        public CatAdapter(Context context, List<QPAY_GetCategories_Object> categoria) {
            super(context, 0, categoria);
            this.categorias = categoria;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_GetCategories_Object categoria = categorias.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria , parent, false);

            TextView text_categoria = convertView.findViewById(R.id.text_categoria);
            text_categoria.setText(categoria.getName());



            return convertView;
        }
    }

    public void getProducts(String idCategory, IFunction<QPAY_GetInventoryResponse> function){

        getContext().loading(true);

        try {

            QPAY_GetInventory data = new QPAY_GetInventory();
            data.setIdCategory(idCategory);

            IGetInventory petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetInventoryResponse response = new Gson().fromJson(json, QPAY_GetInventoryResponse.class);

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

            petition.getInventory(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getOders(){

        getContext().loading(true);

        try {


            QPAY_GetOrder data = new QPAY_GetOrder();
            data.setIdCategory(null);
            data.setIdBimbo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

            IGetOrder petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetOrderResponse response = new Gson().fromJson(json, QPAY_GetOrderResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            PedidoRequest order = new PedidoRequest();
                            order.setProduct(response.getQpay_object()[0]);
                            Fragment_pedidos_bimbo_5.order = order;
                            getContextMenu().setFragment(Fragment_pedidos_bimbo_5.newInstance());

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

            petition.getOrder(data);

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

