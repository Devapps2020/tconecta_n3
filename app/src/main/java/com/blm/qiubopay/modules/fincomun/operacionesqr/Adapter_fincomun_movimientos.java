package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Objects.FCil.DHMovimiento;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.components.HActivity;

public class Adapter_fincomun_movimientos extends RecyclerView.Adapter<Adapter_fincomun_movimientos.ViewHolder> implements IAccionesMovimientos.Data{

    private ArrayList<DHMovimiento>  listTransacciones = new ArrayList<>();
    private IAccionesMovimientos.GUI accionesMovimientos;
    private HActivity context;
    private DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    public Adapter_fincomun_movimientos(ArrayList<DHMovimiento> listTransacciones, IAccionesMovimientos.GUI accionesMovimientos, HActivity context) {
        this.listTransacciones = listTransacciones;
        this.accionesMovimientos = accionesMovimientos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fincomun_codi_movimiento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int selected = position;
        holder.cl_card.setOnClickListener(v -> accionesMovimientos.onItemClick(listTransacciones.get(selected)));
        holder.im_qr.setOnClickListener(v -> accionesMovimientos.onItemClick(listTransacciones.get(selected)));
        holder.tv_tipo_pago.setOnClickListener(v -> accionesMovimientos.onItemClick(listTransacciones.get(selected)));
        holder.tv_fecha.setOnClickListener(v -> accionesMovimientos.onItemClick(listTransacciones.get(selected)));
        holder.tv_monto.setOnClickListener(v -> accionesMovimientos.onItemClick(listTransacciones.get(selected)));

        if (!listTransacciones.get(position).getNatura().contains("1")){
            holder.tv_monto.setText( "+$" + formatter.format(Double.valueOf(listTransacciones.get(position).getCantidad())));
            holder.tv_fecha.setText(listTransacciones.get(position).getFecha());
            holder.im_qr.setImageResource(R.drawable.icon_qr_blue);
            holder.tv_tipo_pago.setText("Cobro");
        }else{
            holder.tv_monto.setText( "-$" + formatter.format(Double.valueOf(listTransacciones.get(position).getCantidad())));
            holder.tv_fecha.setText(listTransacciones.get(position).getFecha());
            holder.im_qr.setImageResource(R.drawable.icon_qr_red);
            holder.tv_tipo_pago.setText("Pago");
        }
    }

    @Override
    public int getItemCount() {
        return listTransacciones.size();
    }


    @Override
    public void removeAllItems() {
        listTransacciones.clear();
    }

    @Override
    public void removeItem(int position) {
        listTransacciones.remove(position);
    }

    @Override
    public <T> void addItem(T item) {
        listTransacciones.add((DHMovimiento) item);
    }

    @Override
    public <T> void addList(List<T> items) {
        for (T t : items){
            listTransacciones.add((DHMovimiento) t);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout cl_card;
        private final ImageView im_qr;
        private final TextView tv_tipo_pago, tv_fecha, tv_monto;

        public ViewHolder(View view) {
            super(view);
            cl_card = view.findViewById(R.id.cl_card);
            im_qr = view.findViewById(R.id.im_qr);
            tv_tipo_pago = view.findViewById(R.id.tv_tipo_pago);
            tv_fecha = view.findViewById(R.id.tv_fecha);
            tv_monto = view.findViewById(R.id.tv_monto);
        }

    }

}
