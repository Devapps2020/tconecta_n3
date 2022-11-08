package com.blm.qiubopay.modules.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.List;

public class Fragment_pedidos_bimbo_2 extends HFragment implements IMenuContext {

    ListView list_productos;
    public static List<QPAY_GetInventory_Object> list = new ArrayList<>();
    private PedidoRequest order;
    private RelativeLayout layout_car;
    private TextView text_number_car;
    ImageView img_buy_car;

    Button btn_seguir;
    Button btn_confirmar;

    public static String nombre = "";

    public static Fragment_pedidos_bimbo_2 newInstance(Object... data) {
        Fragment_pedidos_bimbo_2 fragment = new Fragment_pedidos_bimbo_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_deseos_bimbo_3", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pedidos_bimbo_2, container, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        try { updateBuyCar(); } catch (Exception ex) { }
    }

    @Override
    public void initFragment()  {

        //2021-12-21 RSB. Analytics unilever
        CApplication.setAnalytics(CApplication.ACTION.Market_EligeTusProductos);

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView text_title_categoria = getView().findViewById(R.id.text_title_categoria);
        text_title_categoria.setText(nombre);

        TextView text_personal = getView().findViewById(R.id.text_personal);

        if(!"Cuidado Personal".toUpperCase().trim().equals(nombre.toUpperCase().trim()))
            text_personal.setVisibility(View.GONE);

        layout_car = getView().findViewById(R.id.layout_car);
        text_number_car = getView().findViewById(R.id.text_number_car);

        //Utils.setListViewHeightBasedOnChildren(list_productos);


        list_productos = getView().findViewById(R.id.list_productos);
        ProdAdapter adapter = new ProdAdapter(getContext(), list);
        list_productos.setAdapter(adapter);

        list_productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                QPAY_GetInventory_Object data = list.get(position);
                getContext().setFragment(Fragment_pedidos_bimbo_2.newInstance(data));
            }
        });

        img_buy_car = getView().findViewById(R.id.img_buy_car);

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);
        btn_seguir = getView().findViewById(R.id.btn_seguir);

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

        btn_seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().backFragment();
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

        order = AppPreferences.getOrder(Globals.CATEGORIAS.TODAS.ordinal());

        if(order.getProduct() == null || order.getProduct().isEmpty()){
            img_buy_car.setEnabled(false);
            text_number_car.setVisibility(View.INVISIBLE);
            text_number_car.setText("0");
            order.setProduct(new ArrayList());
        } else {
            int count = 0;
            for (QPAY_GetInventory_Object pedido : order.getProduct())
                count += pedido.getQuantity();

            if(count > 0) {
                img_buy_car.setEnabled(true);
                text_number_car.setVisibility(View.VISIBLE);
            } else {
                img_buy_car.setEnabled(false);
                text_number_car.setVisibility(View.INVISIBLE);
            }

            text_number_car.setText(count + "");
        }

    }

    public void saveOrder(PedidoRequest order) {

        List<QPAY_GetInventory_Object> pedidos = new ArrayList();

        for (QPAY_GetInventory_Object ped: order.getProduct())
            if (ped.getQuantity() > 0)
                pedidos.add(ped);

        order.setProduct(pedidos);

        AppPreferences.setOrder(Globals.CATEGORIAS.TODAS.ordinal(), order);

        updateBuyCar();
    }

    public class ProdAdapter extends ArrayAdapter<QPAY_GetInventory_Object> {

        List<QPAY_GetInventory_Object> productos;

        public ProdAdapter(Context context, List<QPAY_GetInventory_Object> producto) {
            super(context, 0, producto);
            this.productos = producto;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            QPAY_GetInventory_Object producto = productos.get(position);

            Log.d("PRUEBA 1","Prueba"+productos);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_producto_bimbo , parent, false);

            ImageView img_producto = convertView.findViewById(R.id.img_producto);
            TextView text_nombre = convertView.findViewById(R.id.text_nombre);
            TextView text_piezas = convertView.findViewById(R.id.text_piezas);
            TextView text_costo = convertView.findViewById(R.id.text_costo);
            final NumberPicker cantidad = convertView.findViewById(R.id.edit_cantidad);
            LinearLayout layout_cantidad = convertView.findViewById(R.id.layout_cantidad);
            ImageView btn_remove = convertView.findViewById(R.id.btn_remove);
            Button btn_add = convertView.findViewById(R.id.btn_add);

            cantidad.setValue(0);

            text_nombre.setText(producto.getLongName());
            text_piezas.setText(producto.getQuantity() + " piezas");
            text_costo.setText(Utils.paserCurrency(producto.getPrice() + ""));

            QPAY_GetInventory_Object load_pedido = order.getProduct(producto.getIdProductInt());

            if(load_pedido != null) {
                cantidad.setValue(load_pedido.getQuantity());
                layout_cantidad.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.GONE);
                text_piezas.setText((producto.getQuantity()) + " piezas");
            }else{
                btn_add.setVisibility(View.VISIBLE);
                layout_cantidad.setVisibility(View.GONE);
                text_piezas.setText((producto.getQuantity()) + " piezas");
            }

        cantidad.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {

                Integer aux = 0;

                QPAY_GetInventory_Object pedido = order.getProduct(producto.getIdProductInt());
                value = cantidad.getValue();

                if(pedido == null) {
                    pedido = producto;
                    pedido.setIdProductInt(producto.getIdProductInt());
                } else {

                    if(value >= producto.getQuantity())
                        aux = order.getProduct(producto.getIdProductInt()).getQuantity();

                    order.getProduct(producto.getIdProductInt()).setQuantity(0);
                    saveOrder(order);
                }

                if(value > 0) {
                    pedido.setPrice(producto.getPrice());
                    pedido.setQuantity(value);
                    pedido.setTotalAmount(value * producto.getPrice());
                } else {
                    pedido.setQuantity(value);
                }

                order.getProduct().add(pedido);

                saveOrder(order);

            }
        });


            Utils.setImageProduct(productos.get(position).getSku() + "", img_producto);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    layout_cantidad.setVisibility(View.VISIBLE);
                    btn_add.setVisibility(View.GONE);
                    cantidad.setValue(0);
                }
            });

            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    layout_cantidad.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                    cantidad.setValue(0);
                    getContextMenu().hideKeyboard();
                    QPAY_GetInventory_Object pedido = order.getProduct(producto.getIdProductInt());
                    if(pedido!=null) {
                        pedido.setQuantity(0);
                        saveOrder(order);
                    }
                }
            });



            return convertView;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


}

