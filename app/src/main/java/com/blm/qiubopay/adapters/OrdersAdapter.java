package com.blm.qiubopay.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.dialogs.AlertMagnatic;
import com.blm.qiubopay.models.pidelo.LineItemOrder;
import com.blm.qiubopay.models.pidelo.Order;
import com.blm.qiubopay.utils.PideloUtilty;

import java.util.List;

import static android.view.View.GONE;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> ordersList;
    private OrdersAdapter.ListItemClickListener mClick;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String action);
    }
    public OrdersAdapter (OrdersAdapter.ListItemClickListener listener) {
        this.mClick = listener;
    }

    public void setData(List<Order> ordersList) {this.ordersList = ordersList; }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersAdapter.ViewHolder holder, int i) {

        final Order order = ordersList.get(i);
        setInitialState(order, holder);
        setDates(order, holder);

        boolean isEditable = false;
        if (order.getEstado().equalsIgnoreCase("nuevo")){
            isEditable = true;
        }

        setSeekBar(holder.seekBar, order.getEstado().toLowerCase().trim(), holder.estatus_tv);

        if (!isEditable) {
            holder.cancelTv.setVisibility(GONE);
        }

        holder.products_linearlayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(holder.products_linearlayout.getContext());

        for(int j = 0; j < order.getDetalle().size(); j++){

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 30);

            View custLayout= inflater.inflate(R.layout.product_row, null, false);
            Button addBtn = custLayout.findViewById(R.id.plus_btn_row);
            Button minusBtn = custLayout.findViewById(R.id.minus_btn_row);
            Button deleteBtn = custLayout.findViewById(R.id.delete_btn);
            final TextView quantityTv = custLayout.findViewById(R.id.quantity_tv_row);
            TextView product_name = custLayout.findViewById(R.id.product_name_row);
            TextView product_price_row = custLayout.findViewById(R.id.product_price_row);

            if (!isEditable){
                addBtn.setVisibility(View.INVISIBLE);
                minusBtn.setVisibility(View.INVISIBLE);
                deleteBtn.setVisibility(View.INVISIBLE);
            }

            quantityTv.setText(String.valueOf(order.getDetalle().get(j).getCantidad()));
            product_name.setText(order.getDetalle().get(j).getProducto().getNombre());
            final double[] precio = {0.0};
            if(order.getDetalle().get(j).getCantidad() >= order.getDetalle().get(j).getProducto().getPresentacion().get(0).getCantidad_volumen()){
                precio[0] = order.getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio_volumen();
            }else{
                precio[0] = order.getDetalle().get(j).getProducto().getPresentacion().get(0).getPrecio();
            }
            int cantidad = order.getDetalle().get(j).getCantidad();
            product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(cantidad, precio[0]));


            int finalJ = j;
            deleteBtn.setOnClickListener(view -> {
                String message = "";
                if (order.getDetalle().size() == 1){
                     message = "Si eliminas este elemento, se cancelará el pedido. ¿Seguro deseas continuar?";
                }else{
                    message = "¿Deseas eliminar el producto?";
                }

                showAlert(holder.total_tv_order.getContext(), message, i, custLayout, finalJ, holder.total_tv_order);
            });

            addBtn.setOnClickListener(view -> {
                int quantity = PideloUtilty.getQuantity(quantityTv);
                int newQuantity = PideloUtilty.addQuantity(quantity);
                order.getDetalle().get(finalJ).setCantidad(newQuantity);
                if (newQuantity >= order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getCantidad_volumen()){
                    precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio_volumen();
                }else{
                    precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                }
                //precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(newQuantity, precio[0]));
                PideloUtilty.setQuantityTv(quantityTv, newQuantity);
                holder.total_tv_order.setText("$"+setOrderTotal(i));

            });

            minusBtn.setOnClickListener(view -> {
                int quantity = PideloUtilty.getQuantity(quantityTv);
                if (quantity > 1){
                    int newQuantity = PideloUtilty.restQuantity(quantity);
                    order.getDetalle().get(finalJ).setCantidad(newQuantity);
                    if (newQuantity >= order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getCantidad_volumen()){
                        precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio_volumen();
                    }else{
                        precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                    }
                    //precio[0] = order.getDetalle().get(finalJ).getProducto().getPresentacion().get(0).getPrecio();
                    product_price_row.setText("$"+PideloUtilty.changeDoubleFormat(newQuantity, precio[0]));
                    PideloUtilty.setQuantityTv(quantityTv, newQuantity);
                    holder.total_tv_order.setText("$"+setOrderTotal(i));
                }
            });

            holder.products_linearlayout.addView(custLayout, layoutParams);
        }

        buttonsListeners(holder);

        hideAndShowButtons(order, holder);
    }

    private void buttonsListeners( OrdersAdapter.ViewHolder holder ){
        holder.save_changes_btn.setOnClickListener(view -> {
            String observations = holder.observationsTv.getText().toString();
            ordersList.get(holder.getAdapterPosition()).setObservaciones(observations);
            mClick.onListItemClick(holder.getAdapterPosition(), "guardar");

        });
        holder.cancelTv.setOnClickListener(view -> {
            mClick.onListItemClick(holder.getAdapterPosition(), "cancel");
            //cancelOrder(holder.cancelTv.getContext(), holder.getAdapterPosition());
        });

        holder.pay_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onListItemClick(holder.getAdapterPosition(), "pay");
            }
        });
    }

    private void setDates(Order order, OrdersAdapter.ViewHolder holder ){
        holder.initial_date_tv.setText("Realizado: "+order.getFecha_creacion().getDate());
        if (order.getFecha_entregado() == null){
            holder.delivery_date_tv.setText("Entregado: ---");
        }else{
            holder.delivery_date_tv.setText("Entregado: "+order.getFecha_entregado());
        }
    }

    private void setInitialState(Order order, OrdersAdapter.ViewHolder holder ){
        holder.order_tv.setText(order.getOrden());
        holder.distribuidor_tv_order.setText(order.getDistribuidor());
        if (order.getEstado().equalsIgnoreCase("completado")){
            holder.estatus_tv.setText("Entregado");
        }else{
            holder.estatus_tv.setText(order.getEstado());
        }
        holder.total_tv_order.setText("$"+order.getTotal());
        holder.addTv.setPaintFlags(holder.addTv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.cancelTv.setPaintFlags(holder.cancelTv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.pay_tv.setPaintFlags(holder.pay_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.expandBtn.setPaintFlags(holder.expandBtn.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.remaining_total_tv.setText("$ "+order.getSaldo_pendiente()+"MXN");
        holder.distributor_inside.setText(order.getDistribuidor());

    }

    private void hideAndShowButtons(Order order, OrdersAdapter.ViewHolder holder){
        if (order.getEstado().equalsIgnoreCase("cancelado")){
            holder.pay_tv.setVisibility(GONE);
            holder.save_changes_btn.setVisibility(GONE);
            holder.observationsTv.setVisibility(GONE);
            holder.addTv.setVisibility(GONE);
        }else if(order.getEstado().equalsIgnoreCase("nuevo")){
            holder.pay_tv.setVisibility(GONE);
            holder.save_changes_btn.setVisibility(View.VISIBLE);
            holder.cancelTv.setVisibility(View.VISIBLE);
            //holder.observationsTv.setVisibility(View.VISIBLE);
            holder.addTv.setVisibility(View.VISIBLE);
        }else if (order.getEstado().equalsIgnoreCase("ruta")){
            holder.pay_tv.setVisibility(View.VISIBLE);
            //holder.pay_tv.setVisibility(GONE);
            holder.save_changes_btn.setVisibility(GONE);
            holder.observationsTv.setVisibility(GONE);
            holder.addTv.setVisibility(GONE);
        }else{
            holder.pay_tv.setVisibility(GONE);
            holder.save_changes_btn.setVisibility(GONE);
            holder.observationsTv.setVisibility(GONE);
            holder.addTv.setVisibility(GONE);
        }

        if (order.getSaldo_pendiente()== 0){
            holder.pay_tv.setVisibility(GONE);
        }
    }

    private void cancelOrder(Context context, int position){
        PideloUtilty.showDialog(context, "Confirmar cancelación", "¿Deseas cancelar el pedido?",
                "Sí, cancelar pedido", "No", new AlertMagnatic() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        /*ordersList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, ordersList.size());*/
                        //PideloUtilty.deleteProduct(adapterPosition, j);
                        mClick.onListItemClick(position, "cancel");
                        dialog.dismiss();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }, true);
    }

    private void deleteItem(int position, View custLayout, int j, TextView total_order_tv){
        Order order = ordersList.get(position);
        if (order.getDetalle().size() != 1){
            custLayout.setVisibility(GONE);
            order.getDetalle().remove(j);
            total_order_tv.setText("$"+setOrderTotal(position));
        }else{
            /*ordersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, ordersList.size());*/
            mClick.onListItemClick(position, "cancel");
        }
    }

    private double setOrderTotal(int i){

        double total = 0;
        for (LineItemOrder productoPedido : ordersList.get(i).getDetalle()){
            System.out.println("producto "+productoPedido.getProducto().getNombre());
            double precio;
            if (productoPedido.getCantidad() >= productoPedido.getProducto().getPresentacion().get(0).getCantidad_volumen()){
                precio = productoPedido.getCantidad() * productoPedido.getProducto().getPresentacion().get(0).getPrecio_volumen();
            }else{
                precio = productoPedido.getCantidad() * productoPedido.getProducto().getPresentacion().get(0).getPrecio();
            }
            total +=  precio;
        }


        ordersList.get(i).setTotal((double) Math.round(total * 100) / 100);

        return (double) Math.round(total * 100) / 100;
    }

    private void showAlert(Context context, String text, int adapterPosition, View custLayout, int j, TextView total_order_tv){

        PideloUtilty.showDialog(context, "", text, "Sí, eliminar producto",
                "No", new AlertMagnatic() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        deleteItem(adapterPosition, custLayout, j, total_order_tv);
                        dialog.dismiss();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                }, true);
    }

    private void setSeekBar(SeekBar seekBar, String status, TextView status_tv){
        seekBar.setMax(120);
        Context context = status_tv.getContext();
        switch (status){
            case "nuevo":
                seekBar.setProgress(0);
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.coloTextPidelo)));
                break;
            case "bodega":
                seekBar.setProgress(30);
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPidelo)));
                break;
            case "ruta":
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRouteStatus)));
                seekBar.setProgress(75);
                break;
            case "completado":
                seekBar.setProgress(120);
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGreenPidelo)));
                break;
            case "cancelado":
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRed)));
                break;
            case "ventas":
                status_tv.setTextColor(context.getResources().getColor(R.color.white));
                status_tv.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SeekBar seekBar;
        private LinearLayout products_linearlayout;
        private Button expandBtn, save_changes_btn;
        private RelativeLayout expandableView;
        private TableLayout tableLayout;
        private TextView addTv, cancelTv, observationsTv, order_tv, distribuidor_tv_order,
                total_tv_order, estatus_tv, initial_date_tv, delivery_date_tv, remaining_total_tv,
                pay_tv, distributor_inside;

        private ViewHolder(View view){
            super(view);
            seekBar = view.findViewById(R.id.seekBar);
            products_linearlayout = view.findViewById(R.id.products_linearlayout);
            expandBtn = view.findViewById(R.id.expandBtn);
            expandableView = view.findViewById(R.id.expandableView);
            tableLayout = view.findViewById(R.id.table_layout);
            addTv = view.findViewById(R.id.addTv);
            cancelTv = view.findViewById(R.id.cancelTv);
            observationsTv = view.findViewById(R.id.observationsTv);
            order_tv = view.findViewById(R.id.order_tv);
            distribuidor_tv_order = view.findViewById(R.id.distribuidor_tv_order);
            total_tv_order = view.findViewById(R.id.total_tv_order);
            estatus_tv = view.findViewById(R.id.estatus_tv);
            save_changes_btn = view.findViewById(R.id.save_changes_btn);
            initial_date_tv = view.findViewById(R.id.initial_date_tv);
            delivery_date_tv = view.findViewById(R.id.delivery_date_tv);
            remaining_total_tv = view.findViewById(R.id.remaining_total_tv);
            pay_tv = view.findViewById(R.id.pay_tv);
            distributor_inside = view.findViewById(R.id.distributor_inside);

            expandBtn.setOnClickListener(this);
            addTv.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.expandBtn:
                    hideAndShowDetail();
                    break;

                case R.id.addTv:
                    hideAndShowObservations();
                    break;
            }

        }

        private void hideAndShowDetail(){
            if (expandableView.getVisibility() == GONE){
                TransitionManager.beginDelayedTransition(tableLayout, new AutoTransition());
                expandBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_24dp, 0);
                expandBtn.setText("Cerrar");
                expandableView.setVisibility(View.VISIBLE);
            }else{
                TransitionManager.beginDelayedTransition(tableLayout, new AutoTransition());
                expandBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_24dp, 0);
                expandBtn.setText("Detalle");
                expandableView.setVisibility(GONE);
            }
        }

        private void hideAndShowObservations(){
            if (observationsTv.getVisibility() == GONE){
                observationsTv.setVisibility(View.VISIBLE);
            }else{
                observationsTv.setVisibility(GONE);
            }
        }
    }
}
