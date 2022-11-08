package com.blm.qiubopay.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.squareup.picasso.Picasso;
import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.pidelo.LineItem;
import com.blm.qiubopay.models.pidelo.NewOrder;
import com.blm.qiubopay.utils.PideloUtilty;

import java.util.ArrayList;
import java.util.List;

public class ProductDialog extends AppCompatDialogFragment {

    private Button plusBtn, minusBtn, addBtn;
    private TextView skuTv, productTv, stockTv, boxTv, priceTv, unitPriceTv, distrubutorTv, quantityTv, messageTv;
    private ImageView imageView, clearImg;
    private LinearLayout customBtn_layout;
    private RelativeLayout relativeLayout;
    LoadingDialogPidelo loadingDialogPidelo;

    private DialogListener listenerActivity;
    private DialogListener listenerFragment;

    private LineItem lineItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.WideDialog);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lineItem = (LineItem) bundle.getSerializable("lineItem");
        }
        loadingDialogPidelo = new LoadingDialogPidelo(getActivity(), getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.product_item_layout, null);
        builder.setView(view);

        relativeLayout = view.findViewById(R.id.product_relative_layout);
        distrubutorTv = view.findViewById(R.id.distribuitorTv);
        plusBtn = view.findViewById(R.id.plusBtn);
        quantityTv = view.findViewById(R.id.quantityTv);
        minusBtn = view.findViewById(R.id.minusBtn);
        addBtn = view.findViewById(R.id.addToCartBtn);
        skuTv = view.findViewById(R.id.sku_tv);
        productTv = view.findViewById(R.id.product_name);
        stockTv = view.findViewById(R.id.available_tv);
        boxTv = view.findViewById(R.id.box_tv);
        priceTv = view.findViewById(R.id.price_tv);
        unitPriceTv = view.findViewById(R.id.unit_price);
        imageView = view.findViewById(R.id.product_img);
        clearImg = view.findViewById(R.id.clear_img);
        customBtn_layout = view.findViewById(R.id.custom_btn_layout);
        messageTv = view.findViewById(R.id.message_tv);

        skuTv.setGravity(Gravity.START);
        productTv.setGravity(Gravity.START);

        clearImg.setVisibility(View.VISIBLE);
        customBtn_layout.setVisibility(View.VISIBLE);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        productTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        distrubutorTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        skuTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        boxTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        stockTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        priceTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        unitPriceTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        layoutParams.setMargins(40, 30, 40, 30);

        relativeLayout.setLayoutParams(layoutParams);

        final AlertDialog alertDialog = builder.create();

        plusBtn.setOnClickListener(view14 -> {
            int quantity = PideloUtilty.getQuantity(quantityTv);
            int newQuantity = PideloUtilty.addQuantity(quantity);
            double newPrice;
            if (newQuantity >= lineItem.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                newPrice = newQuantity * lineItem.getProducto().getPresentacion().get(0).getPrecio_volumen();
            }else{
                newPrice = newQuantity * lineItem.getProducto().getPresentacion().get(0).getPrecio();
            }

            lineItem.setCantidad(newQuantity);
            priceTv.setText("$"+(double)Math.round(newPrice * 100) / 100);

            PideloUtilty.setQuantityTv(quantityTv, newQuantity);
        });

        minusBtn.setOnClickListener(view13 -> {
            int quantity = PideloUtilty.getQuantity(quantityTv);
            if (quantity > 1){
                int newQuantity = PideloUtilty.restQuantity(quantity);
                double newPrice;
                if (newQuantity >= lineItem.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                    newPrice = newQuantity * lineItem.getProducto().getPresentacion().get(0).getPrecio_volumen();
                }else{
                    newPrice = newQuantity * lineItem.getProducto().getPresentacion().get(0).getPrecio();
                }

                priceTv.setText("$"+(double)Math.round(newPrice * 100) / 100);
                lineItem.setCantidad(newQuantity);

                PideloUtilty.setQuantityTv(quantityTv, newQuantity);
            }
        });

        clearImg.setOnClickListener(view12 -> alertDialog.cancel());

        addBtn.setOnClickListener(view1 -> {

            /*loadingDialogPidelo.startLoading();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {*/
            if (listenerActivity != null){
                listenerActivity.addToCart(lineItem);
            }

            if (listenerFragment != null){
                listenerFragment.addToCart(lineItem);
            }

            alertDialog.dismiss();
                /*}
            }, 200);*/


        });

        setInfoInView();

        if (checkProductInCart()){
            customBtn_layout.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText("Este producto ya existe en tu carrito");
        }else if (lineItem.getProducto().getStock() == 0){
            customBtn_layout.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText("No hay suficientes productos disponibles");
        }
        return alertDialog;

    }

    private void setInfoInView() {

        skuTv.setText("SKU "+lineItem.getProducto().getSku());
        productTv.setText(lineItem.getProducto().getNombre());
        stockTv.setText(lineItem.getProducto().getStock()+" disponibles");
        //boxTv.setText(lineItem.getProducto().());
        priceTv.setText("$"+lineItem.getProducto().getPresentacion().get(0).getPrecio());
        //unitPriceTv.setText(lineItem.getProducto().getSku());
        distrubutorTv.setText(lineItem.getDistribuidor());
        quantityTv.setText(String.valueOf(lineItem.getCantidad()));

        if (!lineItem.getProducto().getImagen().isEmpty() || lineItem.getProducto().getImagen() == null){
            Picasso.get().load(lineItem.getProducto().getImagen()).into(imageView);
        }else{
            imageView.setBackgroundColor(imageView.getContext().getResources().getColor(R.color.coloTextPidelo));
        }

        if (lineItem.getProducto().getPresentacion().get(0).getCodigo_medida().equalsIgnoreCase("M002")){
            boxTv.setText("Unidad");
        }else{
            unitPriceTv.setVisibility(View.VISIBLE);
            boxTv.setText("Caja con "+lineItem.getProducto().getPresentacion().get(0).getCantiad_caja());
            if (lineItem.getProducto().getPresentacion().size()==2){
                unitPriceTv.setText("$"+lineItem.getProducto().getPresentacion().get(1).getPrecio()+"/pieza");
            }else {
                unitPriceTv.setText(""+lineItem.getProducto().getPresentacion().get(1).getPrecio()/lineItem.getProducto().getPresentacion().get(0).getCantiad_caja()+"/pieza");
            }
        }
        /*if (lineItem.getProducto().getPresentacion().get(0).getCodigo_medida().equalsIgnoreCase("M002")){
            boxTv.setText("Unidad");
        }else{
            boxTv.setText("Caja con "+lineItem.getProducto().getPresentacion().get(0).getCantiad_caja());

        }*/

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listenerActivity = (DialogListener) context;
            listenerFragment = (DialogListener) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }

    }

    public interface DialogListener{
        void addToCart(LineItem lineItem);
    }

    public boolean checkProductInCart(){
        List<NewOrder> cartList = AppPreferences.readCartList();
        for (NewOrder newOrder : new ArrayList<>(cartList)){
            if (newOrder.getIdentidad_empresa().equalsIgnoreCase(lineItem.getIdentidad_empresa())){
                for (LineItem lineItem1 : new ArrayList<>(newOrder.getDetalle())){
                    if(lineItem1.getProducto().getSku().equals(lineItem.getProducto().getSku())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
