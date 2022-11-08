package com.blm.qiubopay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.blm.qiubopay.R;
import com.blm.qiubopay.models.pidelo.LineItem;
import com.blm.qiubopay.models.pidelo.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<LineItem> lineItems;
    private List<LineItem> lineItemsCopy;
    private ProductsAdapter.ListItemClickListener mClick;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public void setData(List<LineItem> lineItems) {
        this.lineItems = lineItems;
        this.lineItemsCopy = new ArrayList<>();
        this.lineItemsCopy.addAll(lineItems);
    }

    public ProductsAdapter (ProductsAdapter.ListItemClickListener listener) {
        this.mClick = listener;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item_layout, viewGroup, false);
        return new ProductsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsAdapter.ViewHolder holder, int i) {

        final LineItem lineItem = lineItems.get(i);
        final Product product = lineItem.getProducto();

        holder.product_name.setText(product.getNombre());
        holder.distribuitorTv.setText(lineItem.getDistribuidor());
        holder.sku_tv.setText("SKU "+product.getSku());
        holder.available_tv.setText(product.getStock() + " disponibles");
        holder.price_tv.setText("$"+product.getPresentacion().get(0).getPrecio());
        if (product.getPresentacion().get(0).getCodigo_medida().equalsIgnoreCase("M002")){
            holder.box_tv.setText("Unidad");
        }else{
            holder.unit_price.setVisibility(View.VISIBLE);
            holder.box_tv.setText("Caja con "+product.getPresentacion().get(0).getCantiad_caja());
            if (product.getPresentacion().size()==2){
                holder.unit_price.setText("$"+product.getPresentacion().get(1).getPrecio()+"/pieza");
            }else {
                holder.unit_price.setText(""+product.getPresentacion().get(1).getPrecio()/product.getPresentacion().get(0).getCantiad_caja()+"/pieza");
            }
        }

        if (!product.getImagen().isEmpty() || product.getImagen() == null){
            Picasso.get().load(product.getImagen()).into(holder.product_img);
        }else{
            holder.product_img.setBackgroundColor(holder.product_img.getContext().getResources().getColor(R.color.coloTextPidelo));
        }

    }

    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    public void filter(String text) {
        lineItems.clear();
        if(text.isEmpty()){
            lineItems.addAll(lineItemsCopy);
        } else{
            text = text.toLowerCase();
            for (LineItem lineItem : lineItemsCopy){
                if (lineItem.getProducto().getNombre().toLowerCase().contains(text)){
                    lineItems.add(lineItem);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private Button addToCartBtn;
        private TextView product_name, distribuitorTv, sku_tv, box_tv, available_tv, price_tv, unit_price;
        private ImageView product_img;

        private ViewHolder(View view){
            super(view);
            addToCartBtn = view.findViewById(R.id.addToCartBtn);
            product_name = view.findViewById(R.id.product_name);
            distribuitorTv = view.findViewById(R.id.distribuitorTv);
            sku_tv = view.findViewById(R.id.sku_tv);
            box_tv = view.findViewById(R.id.box_tv);
            available_tv = view.findViewById(R.id.available_tv);
            price_tv = view.findViewById(R.id.price_tv);
            unit_price = view.findViewById(R.id.unit_price);
            product_img = view.findViewById(R.id.product_img);

            //view.setOnClickListener(this);
            addToCartBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClick.onListItemClick(position);
        }
    }
}
