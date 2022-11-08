package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.reportes.FinancialReportTxn;
import com.blm.qiubopay.utils.Utils;

import java.util.List;

public class FinancialReportAdapter extends RecyclerView.Adapter<FinancialReportAdapter.ViewHolder> {

    private List<FinancialReportTxn> reportItems;
    private FinancialReportAdapter.ListTxnClickListener mClick;

    public interface ListTxnClickListener {
        void onListTxnClick(int clickedItemIndex);
    }

    public FinancialReportAdapter(ListTxnClickListener mClick) {
        this.mClick = mClick;
    }

    public void setData(List<FinancialReportTxn> reportItems) {
        this.reportItems = reportItems;
    }

    @NonNull
    @Override
    public FinancialReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.financial_report_item_layout, viewGroup, false);
        return new FinancialReportAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FinancialReportAdapter.ViewHolder holder, int i) {

        final FinancialReportTxn transactionItem = reportItems.get(i);

        if(transactionItem.getMarca().toUpperCase().contains("TRASPASO")){

            String importe = transactionItem.getImporte()!=null ? transactionItem.getImporte().toString() : "";
            importe = !importe.isEmpty() ? Utils.paserCurrency(importe) :"";

            holder.ivCard.setVisibility(View.GONE);
            holder.layoutCommission.setVisibility(View.GONE);
            holder.layoutNetAmount.setVisibility(View.GONE);
            holder.tvCardAut.setText("Transferencia de saldo");
            holder.tvDate.setText(transactionItem.getFechaHora());
            holder.tvAmount.setText(importe);

        } else {

            Integer resource = R.drawable.ic_card;
            switch(transactionItem.getMarca().toUpperCase()){
                case "VI":
                    resource = R.drawable.card_visa;
                    break;
                case "MC":
                    resource = R.drawable.card_mastercard;
                    break;
                case "AMEX":
                    resource = R.drawable.card_amex;
                    break;
                case "FV":
                    resource = R.drawable.card_edenred_copy;
                case "FE":
                    resource = R.drawable.card_edenred;
                    break;
            }

            String cardAut = " ****" + transactionItem.getTarjeta() + " AUT " + transactionItem.getAutorizacion();

            String amount = transactionItem.getImporte()!=null ? transactionItem.getImporte().toString() : "";
            amount = !amount.isEmpty() ? Utils.paserCurrency(amount) :"";
            String tasaDescuento = transactionItem.getTasaDescuento()!=null ? transactionItem.getTasaDescuento().toString() : "";
            tasaDescuento = !tasaDescuento.isEmpty() ? Utils.paserCurrency(tasaDescuento) :"";
            String importeNeto = transactionItem.getImporteNeto()!=null ? transactionItem.getImporteNeto().toString() : "";
            importeNeto = !importeNeto.isEmpty() ? Utils.paserCurrency(importeNeto) :"";

            if(transactionItem.getTipoMovimiento()!=null){
                holder.tvTipoMovimiento.setVisibility(View.VISIBLE);
                holder.tvTipoMovimiento.setText(transactionItem.getTipoMovimiento());
            }
            holder.ivCard.setImageResource(resource);
            holder.tvCardAut.setText(cardAut);
            holder.tvAmount.setText(amount);
            holder.tvDate.setText(transactionItem.getFechaHora());
            holder.tvCommission.setText(tasaDescuento);
            holder.tvNetAmount.setText(importeNeto);

        }

    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTipoMovimiento;
        private ImageView ivCard;
        private TextView tvCardAut;
        private TextView tvAmount;
        private TextView tvDate;
        private TextView tvCommission;
        private TextView tvStatus;
        private TextView tvNetAmount;
        private LinearLayout layoutCommission;
        private LinearLayout layoutNetAmount;
        private LinearLayout layoutFinanciero;

        private ViewHolder(View view){
            super(view);
            tvTipoMovimiento = view.findViewById(R.id.tv_tipo_movimiento);
            ivCard = view.findViewById(R.id.iv_card);
            tvCardAut = view.findViewById(R.id.tv_card_aut);
            tvAmount = view.findViewById(R.id.tv_amount);
            tvDate = view.findViewById(R.id.tv_date);
            tvCommission = view.findViewById(R.id.tv_commission);
            tvStatus = view.findViewById(R.id.tv_status);
            tvNetAmount = view.findViewById(R.id.tv_net_amount);
            layoutCommission = view.findViewById(R.id.layout_commission);
            layoutNetAmount = view.findViewById(R.id.layout_net_amount);
            layoutFinanciero = view.findViewById(R.id.layout_item_financiero);
            layoutFinanciero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onListTxnClick(getAdapterPosition());
                }
            });

        }


    }
}

