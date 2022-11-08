package com.blm.qiubopay.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.connection.Api;
import com.blm.qiubopay.connection.RetrofitClient;
import com.blm.qiubopay.dialogs.AlertMagnatic;
import com.blm.qiubopay.dialogs.LoadingDialogPidelo;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.pidelo.LineItem;
import com.blm.qiubopay.models.pidelo.NewOrder;
import com.blm.qiubopay.responses.ResponseCreateOrder;
import com.blm.qiubopay.utils.PideloUtilty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout linearLayout_items;
    private LinearLayout linearLayout_products;
    private int selectedIndex;
    private List<NewOrder> newOrders;
    private Button next_btn, confirm;
    private TextView checkout_total, remaingin_checkout_tv, distributor_name, cancel_order_tv, conditions_tv;
    private List<TextView> arrTextViews = new ArrayList<>();
    private LoadingDialogPidelo loadingDialogPidelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Confirmar pedidos");

        loadingDialogPidelo = new LoadingDialogPidelo(this, getApplicationContext());

        linearLayout_items = findViewById(R.id.linearlayout_items);
        linearLayout_products = findViewById(R.id.linearlayout_products);
        next_btn = findViewById(R.id.next_btn);
        checkout_total = findViewById(R.id.checkout_total);
        remaingin_checkout_tv = findViewById(R.id.remaingin_checkout_tv);
        confirm = findViewById(R.id.confirm);
        distributor_name = findViewById(R.id.distributor_name);
        cancel_order_tv = findViewById(R.id.cancel_order_tv);
        cancel_order_tv.setPaintFlags(cancel_order_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        conditions_tv = findViewById(R.id.conditions_tv);

        setInitialState();

    }

    public void setInitialState() {
        newOrders = AppPreferences.readCartList();
        selectedIndex = 0;

        next_btn.setEnabled(false);
        distributor_name.setText(newOrders.get(selectedIndex).getDistributor());
        conditions_tv.setText("Se cobrará un adelanto de $"+newOrders.get(selectedIndex).getMonto_reserva()+ " pesos.");

        arrTextViews.clear();
        setNumberOfItems();
        setProductsView(selectedIndex);
        setInitialTextsAndColors();
        setButtonsListeners();
        //changeTextViewsStyle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setInitialTextsAndColors(){
        checkout_total.setText("$"+ (double) PideloUtilty.getTotalByOrder(selectedIndex) + " MXN");
        remaingin_checkout_tv.setText("$"+ (double) PideloUtilty.getTotalByOrder(selectedIndex) + " MXN");
        next_btn.getBackground().setAlpha(128);
        confirm.getBackground().setAlpha(255);
        confirm.setEnabled(true);
        if (newOrders.size() == 1){
            next_btn.setText("Terminar");
        }
    }

    private void setButtonsListeners(){
        confirm.setOnClickListener(view -> {
            confirmOrder();
        });

        next_btn.setOnClickListener(view -> {
            if (selectedIndex+1 == newOrders.size()){
                PideloUtilty.deleteAll();
                finish();
            }else{

                //setInitialState();
                selectedIndex += 1;
                setProductsView(selectedIndex);
                arrTextViews.get(selectedIndex).setBackgroundResource(R.drawable.circle_button);
                arrTextViews.get(selectedIndex).setTextColor(Color.WHITE);
                distributor_name.setText(newOrders.get(selectedIndex).getDistributor());
                setInitialTextsAndColors();

                if (selectedIndex == newOrders.size()-1){
                    next_btn.setEnabled(false);
                    next_btn.setText("Terminar");
                }
                //changeTextViewsStyle();


            }
        });

        cancel_order_tv.setOnClickListener(view -> {

            PideloUtilty.showDialog(this, "", "¿Deseas cancelar el pedido?",
                    "Sí, cancelar pedido", "No", new AlertMagnatic() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            if (newOrders.size() == 1) {
                                PideloUtilty.deleteOrder(selectedIndex);
                                dialog.dismiss();
                                finish();

                            }else if (newOrders.size()-1 == selectedIndex){
                                confirm.setEnabled(false);
                                confirm.getBackground().setAlpha(128);
                                next_btn.setEnabled(true);
                                next_btn.getBackground().setAlpha(255);
                            }else{
                                PideloUtilty.deleteOrder(selectedIndex);
                                linearLayout_items.removeAllViews();
                                setInitialState();
                            }
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }, true);
        });
    }

    /*private void changeTextViewsStyle(){
        int i = 0;
        System.out.println("selectedIndex "+selectedIndex);
        for (TextView textView : arrTextViews){
            if (selectedIndex == i) {
                System.out.println("hola");
            }else{
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(getResources().getColor(R.color.coloTextPidelo));
            }
            i++;
        }
    }*/

    private void setNumberOfItems(){
        linearLayout_items.removeAllViews();
        for(int i = 1; i<=newOrders.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);

            View itemView = getLayoutInflater().inflate(R.layout.checkout_row, null);
            TextView itemTv =  itemView.findViewById(R.id.itemTv);
            itemTv.setText(String.valueOf(i));
            if(i != 1){
                itemTv.setBackgroundColor(Color.TRANSPARENT);
                itemTv.setTextColor(getResources().getColor(R.color.coloTextPidelo));
            }

            arrTextViews.add(itemTv);

            linearLayout_items.addView(itemView);
        }
    }

    private void setProductsView(int i){

        NewOrder newOrder = newOrders.get(i);
        linearLayout_products.removeAllViews();
        int j = 0;
        for (LineItem lineItem : newOrder.getDetalle()){

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 30);

            View custLayout = getLayoutInflater().inflate(R.layout.product_row, null, false);
            setProductTexts(custLayout, lineItem, j);

            linearLayout_products.addView(custLayout, layoutParams);

            j++;
        }
    }

    private void setProductTexts(View view, LineItem lineItem, int j) {
        TextView quantityTv = view.findViewById(R.id.quantity_tv_row);
        TextView product_name = view.findViewById(R.id.product_name_row);
        TextView product_price_row = view.findViewById(R.id.product_price_row);

        int cantidad = lineItem.getCantidad();
        double precio;
        if (cantidad >= lineItem.getProducto().getPresentacion().get(0).getCantidad_volumen()){
            precio = lineItem.getProducto().getPresentacion().get(0).getPrecio_volumen();
        }else{
            precio = lineItem.getProducto().getPresentacion().get(0).getPrecio();
        }

        quantityTv.setText(String.valueOf(lineItem.getCantidad()));
        product_name.setText(lineItem.getProducto().getNombre());
        double formattedTotal = (double)Math.round(cantidad*precio * 100) / 100;
        product_price_row.setText("$"+formattedTotal);

        setProductButtons(view, quantityTv, product_price_row, j, precio);
    }

    private void setProductButtons(View view, TextView quantityTv, TextView product_price_row, int j, double precio){
        Button addBtn = view.findViewById(R.id.plus_btn_row);
        Button minusBtn = view.findViewById(R.id.minus_btn_row);
        Button deleteBtn = view.findViewById(R.id.delete_btn);

        addBtn.setOnClickListener(view1 -> {
            int quantity = PideloUtilty.getQuantity(quantityTv);
            int newQuantity = PideloUtilty.addQuantity(quantity);
            updateQuantities(quantityTv, product_price_row, j, newQuantity);
        });

        minusBtn.setOnClickListener(view12 -> {
            int quantity = PideloUtilty.getQuantity(quantityTv);
            if (quantity > 1){
                int newQuantity = PideloUtilty.restQuantity(quantity);
                updateQuantities(quantityTv, product_price_row, j, newQuantity);
            }
        });

        deleteBtn.setOnClickListener(view123 -> {

            PideloUtilty.showDialog(this, "", "¿Deseas eliminar el producto?", "Sí, eliminar producto",
                    "No", new AlertMagnatic() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            deleteItem(view, j);
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }, true);
        });
    }

    private void deleteItem(View view, int j){
        if (PideloUtilty.getSizeListOfProducts(selectedIndex) == 1){
            //PideloUtilty.deleteProduct(selectedIndex, j);
            //linearLayout_items.removeAllViews();
            //newOrders.remove(selectedIndex);
            //setInitialState();
            if (newOrders.size() == 1) {
                PideloUtilty.deleteOrder(selectedIndex);
                finish();

            }else if (newOrders.size()-1 == selectedIndex){
                confirm.setEnabled(false);
                confirm.getBackground().setAlpha(128);
                next_btn.setEnabled(true);
                next_btn.getBackground().setAlpha(255);
            }else{
                PideloUtilty.deleteOrder(selectedIndex);
                linearLayout_items.removeAllViews();
                setInitialState();
            }


        }else{
            PideloUtilty.deleteProduct(selectedIndex, j);
            view.setVisibility(View.GONE);
            double total = PideloUtilty.getTotalByOrder(selectedIndex);
            checkout_total.setText("$"+ total + " MXN");
            remaingin_checkout_tv.setText("$"+ total + " MXN");
            linearLayout_items.removeAllViews();
            setInitialState();

        }
    }

    private void updateQuantities(TextView quantityTv, TextView product_price_row, int j, int newQuantity){
        PideloUtilty.setQuantityTv(quantityTv, newQuantity);
        double precio;
        if ( newQuantity >= newOrders.get(selectedIndex).getDetalle().get(j).getProducto().getPresentacion().get(0).getCantidad_volumen()){
            precio = newOrders.get(selectedIndex).getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio_volumen();
        }else{
            precio = newOrders.get(selectedIndex).getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio();
        }
        double formattedTotal = (double)Math.round(newQuantity* precio * 100) / 100;
        product_price_row.setText("$"+formattedTotal);
        PideloUtilty.changeQuantity(selectedIndex, j, newQuantity);
        newOrders.get(selectedIndex).getDetalle().get(j).setCantidad(newQuantity);
        double total = PideloUtilty.getTotalByOrder(selectedIndex);
        newOrders.get(selectedIndex).setTotal_pedido(total);
        checkout_total.setText("$"+ total + " MXN");
        remaingin_checkout_tv.setText("$"+ total + " MXN");
        PideloUtilty.setQuantityTv(quantityTv, newQuantity);
    }

    private void confirmOrder(){
        if (newOrders.get(selectedIndex).getTotal_pedido() < newOrders.get(selectedIndex).getMonto_reserva()){
            PideloUtilty.showDialog(this, "", "El monto mínimo de compra es de $"+newOrders.get(selectedIndex).getMonto_reserva()+ " pesos. Porfavor modifica tú pedido",
                    "OK", "", new AlertMagnatic() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }, false);
        }else{
            PideloUtilty.showDialog(this, "", "¿Deseas confirmar el pedido?", "Sí, confirmar",
                    "no", new AlertMagnatic() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            sendOrder();
                            dialog.dismiss();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }, true);
        }
    }


    private void sendOrder(){
        loadingDialogPidelo.startLoading();
        NewOrder newOrder = newOrders.get(selectedIndex);
        //newOrder.setTotal_pedido(PideloUtilty.getTotalByOrder(selectedIndex));
        //newOrder.setQpay_seed("1L+YuDq166vW3opNHB4lrRHyF4bKbrg2fuj9FMWCJas=");
        //newOrder.setQpay_seed("blkw/e/209N6r9h7QJI+nRHyF4bKbrg2fuj9FMWCJas=");
        newOrder.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        Gson gson = new Gson();
        String object = gson.toJson(newOrder);
        System.out.println("New Json "+object);

        Api service = RetrofitClient.getRetrofit().create(Api.class);
        Call<ResponseCreateOrder> call = service.createOrder(newOrder);
        call.enqueue(new Callback<ResponseCreateOrder>() {
            @Override
            public void onResponse(Call<ResponseCreateOrder> call, Response<ResponseCreateOrder> response) {

                Buffer buffer = new Buffer();
                try {
                    call.request().body().writeTo(buffer);
                    System.out.println("requestingHola "+buffer.readUtf8());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()){
                    ResponseCreateOrder responseCreateOrder = response.body();
                    if(responseCreateOrder != null){
                        if (responseCreateOrder.getQpay_code().equalsIgnoreCase("000")){
                            loadingDialogPidelo.dismissDialog();
                            String orderNumber = responseCreateOrder.getQpay_object().get(0).getPedido();
                            PideloUtilty.showDialog(CheckoutActivity.this, "", "Pedido confirmado \nNo. Pedido: " + orderNumber,
                                    "Ok", "", new AlertMagnatic() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            confirm.setEnabled(false);
                                            confirm.getBackground().setAlpha(128);
                                            next_btn.setEnabled(true);
                                            next_btn.getBackground().setAlpha(255);
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    }, false);
                        }else{
                            showAlertError(responseCreateOrder.getQpay_description());
                        }

                    }else{
                        showAlertError("Hubo un error, inténtalo más tarde");
                    }
                }else{
                    showAlertError("Hubo un error, inténtalo más tarde");
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateOrder> call, Throwable t) {
                showAlertError("Hubo un error, inténtalo más tarde");
            }
        });
    }

    private void showAlertError(String message){
        loadingDialogPidelo.dismissDialog();
        PideloUtilty.showDialog(this, "", message,
                "OK", "", new AlertMagnatic() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }, false);
    }
}
