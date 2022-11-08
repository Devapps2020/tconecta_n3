package com.blm.qiubopay.components;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.utils.Utils;

import mx.devapps.utils.interfaces.IFunction;

public class CViewSaldo {

    private LinearLayout saldoLayout;
    private TextView saldoTextView;
    private ImageView showImageView;
    private ImageView showRefresh;

    public static boolean isSaldoHidden;

    public static CViewSaldo create(MenuActivity context, View view, boolean showIconPass) {
        return new CViewSaldo(context, view, showIconPass);
    }

    private CViewSaldo(MenuActivity context, View view, boolean showIconPass) {

        saldoLayout = view.findViewById(R.id.saldo_layout);
        saldoTextView = view.findViewById(R.id.saldo_textview);
        showImageView = view.findViewById(R.id.show_imageview);
        showRefresh = view.findViewById(R.id.show_refresh);

        saldoTextView.setText(getSaldo());

        if(showIconPass){
            showRefresh.setVisibility(View.GONE);
            showImageView.setVisibility(View.VISIBLE);

            if(isSaldoHidden){
                saldoTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showImageView.setImageDrawable(view.getResources().getDrawable(R.drawable.show));
            } else {
                saldoTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showImageView.setImageDrawable(view.getResources().getDrawable(R.drawable.show_off));
            }

            showImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isSaldoHidden){
                        saldoTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        showImageView.setImageDrawable(view.getResources().getDrawable(R.drawable.show));
                        isSaldoHidden = true;
                    } else {
                        context.authPIN(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                saldoTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                showImageView.setImageDrawable(view.getResources().getDrawable(R.drawable.show_off));
                                isSaldoHidden = false;
                                context.backFragment();
                            }
                        },true);
                    }
                }
            });

        } else {
            showRefresh.setVisibility(View.VISIBLE);
            showImageView.setVisibility(View.GONE);
        }

    }

    public String getSaldo() {

        String monto = AppPreferences.getKinetoBalance();

        if(monto == null || monto.isEmpty())
            return "$0.00";
        else
            return Utils.paserCurrency(monto.replace("Saldo", "").trim());

    }

    public interface IClick {
        void onClick();
    }

}
