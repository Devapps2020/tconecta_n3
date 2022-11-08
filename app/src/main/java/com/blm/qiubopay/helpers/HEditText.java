package com.blm.qiubopay.helpers;

import android.os.Build;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.utils.Utils;

import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HEditText {

    private EditText editText;
    private TextView textError;
    private boolean required;
    private int maxLength;
    private int minLength;
    private Tipo tipo;
    private boolean enabled;
    private String number;
    private int max = 0;
    private ITextChanged iText;
    private String identifier = "";
    private boolean setText = false;

    public enum Tipo{
        TEXTO,
        TEXTO_Ñ,
        NUMERO,
        MONEDA,
        MONEDA_SC,
        EMAIL,
        PASS,
        PASS_TEXTO,
        TEXTO_SN,
        NONE,
        FOLIO,
        RFC,
        PASS_NONE,
        TIP,
        FECHA_TARJETA
    }

    public HEditText(EditText editText, boolean required, int maxLength, int minLength, Tipo tipo,
                     final ITextChanged iTextChanged, boolean predictiveKeyboard, TextView... textError){

        this.required = required;
        this.maxLength = maxLength;
        this.max = maxLength;
        this.minLength = minLength;
        this.tipo = tipo;
        this.editText = editText;
        this.textError = (textError != null && textError.length > 0) ?  textError[0] : null;

        this.editText.setLongClickable(false);
        this.editText.cancelLongPress();

        iText = iTextChanged != null ? iTextChanged : new ITextChanged() {
            @Override
            public void onChange() {

            }

            @Override
            public void onMaxLength() {

            }
        };

        if(!predictiveKeyboard) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                editText.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        switch (tipo){
            case FECHA_TARJETA:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case TIP:
            case NUMERO:
            case MONEDA:
            case MONEDA_SC:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case PASS:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case PASS_TEXTO:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case PASS_NONE:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }

        setInputFilter();
    }

    public String getTextDouble() {
        return editText.getText().toString().replaceAll(",", "").replace("$", "").trim();
    }

    public void setTextMonto(String text) {

        if(text == null || text.isEmpty())
            text = "0";

        tipo = Tipo.NONE;
        editText.setText(Utils.paserCurrency(text));
        editText.setTag(text.replaceAll(",", "").replace("$", "").trim());
        tipo = Tipo.MONEDA;
    }


    public HEditText(EditText editText, boolean required, int maxLength, int minLength, Tipo tipo,
                     final ITextChanged iTextChanged, TextView... textError){

        this.required = required;
        this.maxLength = maxLength;
        this.max = maxLength;
        this.minLength = minLength;
        this.tipo = tipo;
        this.editText = editText;
        this.textError = (textError != null && textError.length > 0) ?  textError[0] : null;

        this.editText.setLongClickable(false);
        this.editText.cancelLongPress();

        iText = iTextChanged != null ? iTextChanged : new ITextChanged() {
            @Override
            public void onChange() {

            }

            @Override
            public void onMaxLength() {

            }
        };


        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            editText.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);


        switch (tipo){
            case FECHA_TARJETA:
                editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case TIP:
            case NUMERO:
            case MONEDA:
            case MONEDA_SC:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case PASS:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case PASS_TEXTO:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case PASS_NONE:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }

        setInputFilter();

    }

    public void activateLongClickable(){
        this.editText.setLongClickable(true);
    }
    public void disableLongClickable(){
        this.editText.setLongClickable(false);
    }

    public boolean isValid() {

        boolean result = false;

        if(required){
            switch (tipo) {
                case EMAIL:

                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                    Matcher matcher = pattern.matcher(editText.getText().toString().trim());
                    result = matcher.matches();

                    break;
                case RFC:

                    result = editText.getText().toString().trim().matches("^[a-zA-Z]{3,4}(\\d{6})((\\D|\\d){2,3})?$");

                    break;
                case FECHA_TARJETA:
                case TIP:
                case FOLIO:
                case NONE:
                case PASS:
                case PASS_TEXTO:
                case PASS_NONE:
                case TEXTO:
                case TEXTO_Ñ:
                case TEXTO_SN:
                case NUMERO:

                    if(editText.getText().toString().trim().length() > 0){
                        if(minLength > 0){
                            if(editText.getText().toString().trim().length() >= minLength)
                                result = true;
                        }else{
                            if(editText.getText().length() > 0)
                                result = true;
                        }

                    }

                    break;
                case MONEDA:
                case MONEDA_SC:

                    if(editText.getTag() != null && editText.getTag().toString().trim().length() > 0){
                        double valor = Double.parseDouble(editText.getTag().toString());

                        if(minLength > 0){
                            if(valor >= (minLength/100.00))
                                result = true;
                        }else{
                            if(valor > 0)
                                result = true;
                        }
                    }

                    break;
            }


        }else
            result = true;

        if(textError != null){
            if(!result)
                textError.setVisibility(View.VISIBLE);
            else
                textError.setVisibility(View.GONE);
        }

        return result;
    }

    public void setInputFilter(){

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                //if(!required)
                //    return src;

                switch (tipo) {

                    case EMAIL:

                        //20200805 RSB. Imp. Corregir src para email para android 10
                        String newSrc = src.toString();
                        if(newSrc.length()>1)
                            newSrc = newSrc.substring(newSrc.length()-1);

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9@.\\-\\_]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + newSrc).length() <= maxLength) //Limitar número de caracteres
                            if(newSrc.length() == 0 || newSrc.matches("[a-zA-Z0-9@.\\-\\_]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace

                        break;

                    case PASS_TEXTO:

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace


                        break;
                    case RFC:
                    case TEXTO:

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9 ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9 ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace

                        break;
                    case TEXTO_Ñ:

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9ñÑ ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9ñÑ ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace

                        break;
                    case FOLIO:

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9\\-]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z0-9\\-]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace

                        break;
                    case TEXTO_SN:

                        if(maxLength == 0){
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                        }else if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            if(src.length() == 0 || src.toString().matches("[a-zA-Z ]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace

                        break;
                    case TIP:
                    case NUMERO:

                        if(setText==true) {
                            //texto = src.toString();
                            setText = false;
                            return src.equals("$0.00") ? "0" : src.toString().replace("$","").replace(".00","");
                        }

                        if(maxLength == 0){
                            if(src.length() == 0  || src.toString().matches("[0-9]+")){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                            return src;
                        }else
                        if((dest.toString() + src.toString()).length() <= maxLength) { //Limitar número de caracteres
                            if (src.length() == 0 || src.toString().matches("[0-9]+")) {

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            } // for backspace
                        }

                        break;
                    case MONEDA:

                        if((dest.toString().trim() + src.toString().trim()).length() > maxLength){
                            iText.onMaxLength();
                            break;
                        }

                        maxLength = max;

                        //String texto = editText.getText().toString();
                        String texto = "";
                        if(setText==true) {
                            texto = src.toString().trim();
                            if(texto.equals("NaN"))
                                texto = "0.00";
                            setText = false;
                        }
                        else
                            texto = editText.getText().toString();

                        if(src.toString().trim().equals(texto.trim()))
                            return texto;

                        texto = texto.trim().replace("$", "").replace(",", "").replace(".","") + src;

                        try {

                            if(!enabled){

                                if(src.length() == 0)
                                    texto = texto.substring(0, texto.length() - 1 );

                                number = new Formatter().format("%03d", Integer.parseInt(texto)).toString();

                                number = paserCurrency(  number.substring(0, number.length() - 2) + "." + number.substring(number.length() - 2));

                                enabled = true;

                            } else {

                                enabled = false;

                                return src;
                            }

                        }catch (Exception ex){
                            return "";
                        }

                        editText.setTag(number.replace("$", "").replace(",",""));

                        editText.setText(number);

                        iText.onChange();

                        break;
                    case MONEDA_SC:

                        if((dest.toString().trim() + src.toString().trim()).length() > maxLength){
                            iText.onMaxLength();
                            break;
                        }

                        maxLength = max;

                        String textoo = editText.getText().toString().replace(".00","");

                        if(src.toString().trim().equals(textoo.trim()))
                            return textoo;

                        textoo = textoo.trim().replace("$", "").replace(",", "").replace(".","") + src;

                        try {

                            if(!enabled){

                                if(((String) src).isEmpty())
                                    textoo = textoo.substring(0, textoo.length() - 1 );

                                if(!textoo.isEmpty()){

                                    number = new Formatter().format("%03d", Integer.parseInt(textoo)).toString();

                                    number = paserCurrency(number);

                                } else
                                    number = "";

                                enabled = true;

                            } else {

                                enabled = false;

                                return src;
                            }

                        }catch (Exception ex){
                            return "";
                        }

                        editText.setTag(number.replace("$", "").replace(",",""));

                        editText.setText(number);

                        iText.onChange();

                        break;
                    case PASS:

                        if(maxLength == 0){
                            if(src.length() == 0){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                            return src;
                        }else
                        if((dest.toString() + src.toString()).toString().length() <= maxLength) //Limitar número de caracteres
                        {
                            new Handler().postDelayed(new Runnable() { public void run(){
                                iText.onChange();
                            }}, 5);

                            if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                iText.onMaxLength();

                            return src;
                        }
                        break;
                    case PASS_NONE:
                    case NONE:

                        if(maxLength == 0){
                            if(src.length() == 0){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace
                            return src;
                        }else {

                            if((dest.toString() + src.toString()).toString().length() <= maxLength) //Limitar número de caracteres
                            {
                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                return src;
                            }

                        }

                        break;

                    case FECHA_TARJETA:

                        if(maxLength == 0){
                            if(src.length() == 0){

                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                return src;
                            } // for backspace

                            return src;

                        }else {

                            if((dest.toString() + src.toString()).length() <= maxLength) //Limitar número de caracteres
                            {


                                new Handler().postDelayed(new Runnable() { public void run(){
                                    iText.onChange();
                                }}, 5);

                                if(src.length() > 0 && (dest.toString() + src.toString()).length() == maxLength)
                                    iText.onMaxLength();

                                if(editText.getText().length() == 2 && src.length() > 0)
                                    return "/" + src ;

                                if(src.toString().matches("[0-9]+"))
                                    return src;

                            }

                        }
                        break;

                }


                editText.setSelection(editText.getText().length());

                return "";
            }
        };

        editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(maxLength)});
    }

    public void setiText(ITextChanged iText) {
        this.iText = iText;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getTextError() {
        return textError;
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public Integer getTextInt() {
        return Integer.parseInt(editText.getText().toString().trim().replaceAll("\\$", "")
                .replace(",", "").replace(".00", ""));
    }

    public String getTextDecimal() {
        return editText.getText().toString().trim()
                .replaceAll("\\$", "")
                .replace(",", "");
    }

    public String getTextInteger() {
        return editText.getText().toString().trim()
                .replaceAll("\\$", "")
                .replace(",", "")
                .replace(".00", "");
    }

    public void setText(String text) {
        if(text != null)
            editText.setText(text.replace("null", ""));
        else
            editText.setText("");
    }

    public void setNewText(String text) {
        setText = true;
        editText.setText(text);
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String paserCurrency(String dato) {

        if(dato.isEmpty())
            return "";

        try {

            double value = Double.parseDouble(dato.replace(",", "").replace("$", ""));

            if(value == 0)
                return "";

            return java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

        } catch (Exception ex) {
            return "";
        }

    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getIdentifierInt() {

        if(identifier == null || identifier.isEmpty())
            return 0;

        return Integer.parseInt(identifier);
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public ITextChanged getiText() {
        return iText;
    }
}
