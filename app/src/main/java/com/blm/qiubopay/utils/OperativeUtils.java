package com.blm.qiubopay.utils;

import java.util.Locale;

public class OperativeUtils {

    public static String getAmount(){
        return "";
    }

    public static Double getTipAmount(Double amount, Double tipPercent){
        Double tipAmount = 0.00;

        if(tipPercent > 0)
            tipAmount = (amount * tipPercent) / 100.00;

        return tipAmount;
    }

    public static String paserTip(String dato) {

        String back = "";

        try {
            Double value = Double.parseDouble(dato.replace(",", "").replace("$", ""));

            back = java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

        } catch (Exception ex) {
            back = "0.00";
        }

        return back;
    }
}
