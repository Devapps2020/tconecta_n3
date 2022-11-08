package com.blm.qiubopay.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.dialogs.AlertMagnatic;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.pidelo.LineItem;
import com.blm.qiubopay.models.pidelo.NewOrder;

import java.util.ArrayList;
import java.util.List;

public class PideloUtilty {

    public static int getQuantity(TextView textView){
        String quantityString = textView.getText().toString();
        return Integer.parseInt(quantityString);
    }

    public static int addQuantity(int quantity){
        quantity += 1;
        return quantity;
    }

    public static void setQuantityTv(TextView textView, int quantity){
        String quantityString = String.valueOf(quantity);
        textView.setText(quantityString);
    }

    public static int restQuantity(int quantity) {
        quantity -= 1;
        return quantity;
    }

    public static void setTotal(){
        List<NewOrder> cartList = AppPreferences.readCartList();
        for (NewOrder newOrder : cartList){
            double total = 0;
            for (LineItem lineItem1: newOrder.getDetalle()){
                int cantidad = lineItem1.getCantidad();
                double precio;
                if (cantidad >= lineItem1.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                    precio = lineItem1.getProducto().getPresentacion().get(0).getPrecio_volumen();
                }else{
                    precio = lineItem1.getProducto().getPresentacion().get(0).getPrecio();
                }
                total += cantidad * precio;
            }

            newOrder.setTotal_pedido( (double)Math.round(total * 100) / 100);
        }

        AppPreferences.setCartList(cartList);
    }

    public static double getTotal(){
        List<NewOrder> cartList = AppPreferences.readCartList();
        double totalOrders = 0;
        for (NewOrder newOrder : cartList){
            double total = 0;
            for (LineItem lineItem1: newOrder.getDetalle()){
                int cantidad = lineItem1.getCantidad();
                double precio;
                if (cantidad >= lineItem1.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                    precio = lineItem1.getProducto().getPresentacion().get(0).getPrecio_volumen();
                }else{
                    precio = lineItem1.getProducto().getPresentacion().get(0).getPrecio();
                }
                total += cantidad * precio;
            }
            totalOrders += total;

        }
        return (double) Math.round(totalOrders * 100) / 100;
    }

    public static double getTotalByOrder(int i){
        List<NewOrder> cartList = AppPreferences.readCartList();
        double total = 0;
        for (LineItem lineItem : new ArrayList<>(cartList.get(i).getDetalle())){
            int cantidad = lineItem.getCantidad();
            double precio;
            if (cantidad >= lineItem.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                precio = lineItem.getProducto().getPresentacion().get(0).getPrecio_volumen();
            }else{
                precio = lineItem.getProducto().getPresentacion().get(0).getPrecio();
            }
            total += cantidad * precio;
        }
        return (double) Math.round(total * 100) / 100;

    }

    public static void deleteProduct(int i, int j){
        List<NewOrder> cartList = AppPreferences.readCartList();
        if (cartList.get(i).getDetalle().size() == 1){
            cartList.remove(i);
        }else{
            cartList.get(i).getDetalle().remove(j);
        }
        AppPreferences.setCartList(cartList);
        setTotal();
    }

    public static void deleteOrder(int i){
        List<NewOrder> cartList = AppPreferences.readCartList();
        cartList.remove(i);
        AppPreferences.setCartList(cartList);
        setTotal();
    }

    public static void deleteAll(){
        List<NewOrder> cartList = new ArrayList<>();
        AppPreferences.setCartList(cartList);
    }

    public static void changeQuantity(int i, int j, int quantity){
        List<NewOrder> cartList = AppPreferences.readCartList();
        cartList.get(i).getDetalle().get(j).setCantidad(quantity);
        AppPreferences.setCartList(cartList);
        setTotal();
    }

    public static int getSizeListOfProducts(int i){
        List<NewOrder> cartList = AppPreferences.readCartList();
        return cartList.get(i).getDetalle().size();
    }

    public static int getCartSize(){
        List<NewOrder> cartList = AppPreferences.readCartList();
        return cartList.size();
    }

    public static void showDialog(Context mContext, String title, String msg, String positiveBtnCaption,
                                  String negativeBtnCaption, final AlertMagnatic target, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveBtnCaption, target::PositiveMethod)
                .setNegativeButton(negativeBtnCaption, target::NegativeMethod);

        AlertDialog alert = builder.create();
        alert.setOnShowListener(arg0 -> {

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorPidelo));
            if (flag){
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.colorPidelo));
            }else{
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            }

        });
        alert.setCancelable(false);
        alert.show();

    }

    public static double changeDoubleFormat(int quantity, double price){
        return (double) Math.round(quantity*price * 100) / 100;
    }
}

