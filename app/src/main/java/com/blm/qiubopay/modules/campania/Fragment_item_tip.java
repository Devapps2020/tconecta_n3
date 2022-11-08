package com.blm.qiubopay.modules.campania;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;

import mx.devapps.utils.components.HFragment;

public class Fragment_item_tip extends HFragment {

    private QPAY_TipsAdvertising_publicities tip;

    public static Fragment_item_tip newInstance(Object... data) {
        Fragment_item_tip fragment = new Fragment_item_tip();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_item_carrusel", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            tip = new Gson().fromJson(getArguments().getString("Fragment_item_carrusel"), QPAY_TipsAdvertising_publicities.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_item_tip, container, false));
    }

    @Override
    public void initFragment() {

        ImageView img_logo = getView().findViewById(R.id.img_logo);
        TextView text_title = getView().findViewById(R.id.text_title);
        TextView text_message = getView().findViewById(R.id.text_message);

        if(tip != null) {
            Glide.with(getContext())
                    .load(tip.getImage())
                    .centerCrop()
                    .into(img_logo);
            text_title.setText(tip.getTitle());
            text_message.setText(tip.getDescription());
        }

    }

}