package com.blm.qiubopay.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.pidelo.NewOrder;
import com.blm.qiubopay.utils.PideloUtilty;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<NewOrder> cartList;
    private CartAdapter.ListItemClickListener mClick;
    private boolean pressedButton;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public CartAdapter (CartAdapter.ListItemClickListener listener) {
        this.mClick = listener;
    }

    public void setData(List<NewOrder> cartList) {this.cartList = cartList; }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.carrito_item_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int i) {

        final NewOrder newOrder = cartList.get(i);
        holder.chosen_layout.removeAllViews();
        holder.distributor_name_tv.setText(newOrder.getDistributor());
        holder.letter_tv.setText(String.valueOf(newOrder.getDistributor().charAt(0)));
        holder.total_order_tv.setText("$"+PideloUtilty.getTotalByOrder(i));
        LayoutInflater inflater = LayoutInflater.from(holder.chosen_layout.getContext());

        for(int j = 0; j < newOrder.getDetalle().size(); j++){

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 30);

            View custLayout= inflater.inflate(R.layout.product_row, null, false);

            Button addBtn = custLayout.findViewById(R.id.plus_btn_row);
            Button deleteBtn = custLayout.findViewById(R.id.delete_btn);
            Button minusBtn = custLayout.findViewById(R.id.minus_btn_row);

            final TextView quantityTv = custLayout.findViewById(R.id.quantity_tv_row);
            TextView product_name = custLayout.findViewById(R.id.product_name_row);
            TextView product_price_row = custLayout.findViewById(R.id.product_price_row);

            quantityTv.setText(String.valueOf(newOrder.getDetalle().get(j).getCantidad()));
            product_name.setText(newOrder.getDetalle().get(j).getProducto().getNombre());
            int cantidad = newOrder.getDetalle().get(j).getCantidad();
            final double[] precio = getPrice(newOrder, cantidad, j);
            product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(cantidad, precio[0]));

            int finalJ = j;
            deleteBtn.setOnClickListener(view -> {
                showAlert(holder.chosen_layout.getContext(), "¿Deseas eliminar el producto?", custLayout, holder.total_order_tv,
                        finalJ, holder.getAdapterPosition());
            });

            addBtn.setOnClickListener(view -> {
                int quantity = PideloUtilty.getQuantity(quantityTv);
                int newQuantity = PideloUtilty.addQuantity(quantity);
                PideloUtilty.changeQuantity(i, finalJ, newQuantity);
                holder.total_order_tv.setText("$"+PideloUtilty.getTotalByOrder(i));
                if (newQuantity >= newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getCantidad_volumen()){
                    precio[0] = newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio_volumen();
                }else{
                    precio[0] = newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                }
                product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(newQuantity, precio[0]));
                PideloUtilty.setQuantityTv(quantityTv, newQuantity);
                mClick.onListItemClick(holder.getAdapterPosition());
            });

            minusBtn.setOnClickListener(view -> {

                int quantity = PideloUtilty.getQuantity(quantityTv);
                if (quantity > 1){
                    int newQuantity = PideloUtilty.restQuantity(quantity);
                    PideloUtilty.setQuantityTv(quantityTv, newQuantity);
                    if (newQuantity >= newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getCantidad_volumen()){
                        precio[0] = newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio_volumen();
                    }else{
                        precio[0] = newOrder.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                    }
                    product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(newQuantity, precio[0]));
                    PideloUtilty.changeQuantity(i, finalJ, newQuantity);
                    holder.total_order_tv.setText("$"+PideloUtilty.getTotalByOrder(i));
                    PideloUtilty.setQuantityTv(quantityTv, newQuantity);
                    mClick.onListItemClick(holder.getAdapterPosition());
                }
            });

            holder.chosen_layout.addView(custLayout, layoutParams);
        }

    }

    private double[] getPrice(NewOrder newOrder, int cantidad , int j){
        final double[] precio = {0};
        if (cantidad >= newOrder.getDetalle().get(j).getProducto().getPresentacion().get(0).getCantidad_volumen()){
            precio[0] = newOrder.getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio_volumen();
        }else{
            precio[0] = newOrder.getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio();
        }

        return precio;
    }

    private void deleteItem(View custLayout, TextView total_order_tv, int j, int adapterPosition){
        if (PideloUtilty.getSizeListOfProducts(adapterPosition) == 1){
            cartList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, PideloUtilty.getCartSize());
            PideloUtilty.deleteProduct(adapterPosition, j);

        }else{
            PideloUtilty.deleteProduct(adapterPosition, j);
            custLayout.setVisibility(View.GONE);
            total_order_tv.setText("$"+PideloUtilty.getTotalByOrder(adapterPosition));
        }
        mClick.onListItemClick(adapterPosition);
    }

    private boolean showAlert(Context context, String text, View custLayout, TextView total_order, int j, int adapterPosition){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(text);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si, eliminar producto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pressedButton = true;
                        deleteItem(custLayout, total_order, j, adapterPosition);
                        dialogInterface.cancel();
                    }
                }
        );

        builder1.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pressedButton = false;
                        dialogInterface.cancel();
                    }
                }
        );

        AlertDialog alert11 = builder1.create();

        alert11.setOnShowListener(arg0 -> {
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPidelo));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPidelo));

        });
        alert11.show();
        return true;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout chosen_layout;
        private TextView letter_tv, distributor_name_tv, total_order_tv;
        private RelativeLayout parent_layout_cart;

        private ViewHolder(View view){
            super(view);
            chosen_layout = view.findViewById(R.id.chosen_layout);
            letter_tv = view.findViewById(R.id.letter_tv);
            distributor_name_tv = view.findViewById(R.id.distributor_name_tv);
            total_order_tv = view.findViewById(R.id.total_order_tv);
            parent_layout_cart = view.findViewById(R.id.parent_layout_cart);

        }

    }
}
