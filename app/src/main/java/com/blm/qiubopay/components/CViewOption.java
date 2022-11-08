package com.blm.qiubopay.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;


public class CViewOption {

    View layout_view;
    LinearLayout btn_option;
    TextView text_option;
    ImageView img_indicador;

    public static CViewOption create(View view) {
        return new CViewOption(view);
    }

    private CViewOption(View view) {
        layout_view = view;
        btn_option = view.findViewById(R.id.btn_option);
        text_option = view.findViewById(R.id.text_option);
        img_indicador = view.findViewById(R.id.img_indicador);

    }

    public CViewOption onClick(IClick click) {

        if(btn_option != null) {
            btn_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(layout_view);
                }
            });
        }

        return this;
    }

    public CViewOption setText(String text) {

        if(text_option != null) {
            text_option.setText(text);
        }

        return this;
    }

    public CViewOption setIcon(Integer icon) {

        if(img_indicador != null) {
            img_indicador.setRotation(0);
            img_indicador.setImageDrawable(img_indicador.getContext().getResources().getDrawable(icon));
        }

        return this;
    }

    public CViewOption setVisible(boolean show) {

        if(layout_view != null) {
            layout_view.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        return this;
    }

    public CViewOption setText(Integer text) {

        if(text_option != null) {
            text_option.setText(text);
        }

        return this;
    }

    public interface IClick {
        void onClick(View view);
    }

}
