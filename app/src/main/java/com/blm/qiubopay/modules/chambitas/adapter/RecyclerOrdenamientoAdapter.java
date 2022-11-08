package com.blm.qiubopay.modules.chambitas.adapter;

import static com.liveperson.infra.utils.Utils.getResources;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhoto;
import com.blm.qiubopay.models.chambitas.retos.TipoReto;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerOrdenamientoAdapter extends RecyclerView.Adapter<RecyclerOrdenamientoAdapter.ViewHolder>  {

    private ArrayList<QPAY_ActiveCampaign_Questions> items;
    private OnItemClickListener onItemClickListener;
    private Activity activity;
    private TipoReto tipoReto;
    private OnEditTextChanged onEditTextChanged;

    public RecyclerOrdenamientoAdapter(ArrayList<QPAY_ActiveCampaign_Questions> items, Activity activity, TipoReto tipoReto) {
        this.items = items;
        this.activity = activity;
        this.tipoReto = tipoReto;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTextChanged(OnEditTextChanged onEditTextChanged) {
        this.onEditTextChanged = onEditTextChanged;
    }

    @Override
    public RecyclerOrdenamientoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vh;
        switch (tipoReto){
            case ORDENAMIENTO_IMAGEN:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordenamiento_imagen, parent, false);
                vh = new ViewHolder(v,TipoReto.ORDENAMIENTO_IMAGEN);
                return vh;
            case ORDENAMIENTO_PREGUNTA:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordenamiento_pregunta, parent, false);
                vh = new ViewHolder(v,TipoReto.ORDENAMIENTO_PREGUNTA);
                return vh;
            default:
                throw new RuntimeException("NUNCA DEBE CAER AQUÍ");
        }
    }

    public void updateData(ArrayList<QPAY_ActiveCampaign_Questions> viewModels) {
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerOrdenamientoAdapter.ViewHolder holder, int position) {
        final QPAY_ActiveCampaign_Questions item = this.items.get(position);


        switch (tipoReto){
            case ORDENAMIENTO_IMAGEN:
                holder.et_posicion_img.setTag(position);
                // ImageView
                Glide.with(holder.iv_ordenamiento_imagen.getContext())
                        .load(item.getQuestion()) // image url
                        .error(R.drawable.no_disponible)  // any image in case of error
                        .override(300, 300) // resizing
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.iv_ordenamiento_imagen);  // imageview object
                // EditText
                holder.et_posicion_img.setInputType(InputType.TYPE_CLASS_NUMBER);
                holder.et_posicion_img.setKeyListener(DigitsKeyListener.getInstance("123456789"));
                holder.et_posicion_img.setSingleLine(true);

                Log.d("item Ord Img", String.valueOf(item.getId()));

                    holder.et_posicion_img.addTextChangedListener(new TextWatcher() {

                        @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        Log.d("afterTextChanged", String.valueOf(editable));
                        Log.d("tag", String.valueOf(holder.et_posicion_img.getTag()));
                        int pos = (int) holder.et_posicion_img.getTag();
                            // your code here
                            onEditTextChanged.onTextChanged(pos, holder.et_posicion_img.getText().toString(), Integer.valueOf(item.getId()), holder.et_posicion_img);

                    }
                });

                break;
            case ORDENAMIENTO_PREGUNTA:
                holder.tv_opcion.setText(item.getQuestion());
                holder.et_posicion.setInputType(InputType.TYPE_CLASS_NUMBER);
                holder.et_posicion.setKeyListener(DigitsKeyListener.getInstance("123456789"));
                holder.et_posicion.setSingleLine(true);
                holder.et_posicion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        onEditTextChanged.onTextChanged(holder.getAdapterPosition(), holder.et_posicion.getText().toString(), Integer.valueOf(item.getId()), holder.et_posicion);
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

   @Override
    public int getItemViewType(int position) {
       return position;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_ordenamiento_imagen;
        public EditText et_posicion, et_posicion_img;
        public TextView tv_opcion;


        public ViewHolder(View itemView, TipoReto tipoReto) {
            super(itemView);
            switch (tipoReto){
                case ORDENAMIENTO_IMAGEN:
                    iv_ordenamiento_imagen = (ImageView) itemView.findViewById(R.id.iv_ordenamiento_imagen);
                    et_posicion_img = (EditText) itemView.findViewById(R.id.et_posicion_img);
                case ORDENAMIENTO_PREGUNTA:
                    tv_opcion = (TextView) itemView.findViewById(R.id.tv_opcion);
                    et_posicion = (EditText) itemView.findViewById(R.id.et_posicion);
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, QPAY_ChallengePhoto viewModel);
    }

    public interface OnEditTextChanged {
        // here component_id is editTextId (findView by Id)
        void onTextChanged( int position, String charSeq, int itemId, EditText et);
    }



}

