package com.blm.qiubopay.components;

import static com.liveperson.infra.utils.Utils.getResources;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;

import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.devapps.utils.components.HSpinner;

public class CViewEditText {

    final String MATCH_RFC = "^[a-zA-Z]{3,4}(\\d{6})((\\D|\\d){2,3})?$";
    final String MATCH_EMAIL = "[-\\\\w\\\\.]+@\\\\w+\\\\.\\\\w+";
    final String MATCH_TEXT = "[a-zA-Z0-9ñÑ ]+";
    final String MATCH_TEXT_SN = "[a-zA-Z ]+";
    final String MATCH_CURP = "[0-9]+";

    View view;
    TextInputLayout text_input_layout;
    TextInputEditText text_input_layout_edit;
    AppCompatImageView text_input_layout_icon;
    HSpinner text_input_layout_select;
    LinearLayout text_input_layout_touch;

    TextView text_input_layout_label;
    TYPE type;
    ITextChanged changed;
    Boolean required;
    Integer maximum;
    Integer minimum;
    String error;
    String alert;
    Boolean showpass;
    Boolean isvalid;
    Boolean keyboard;

    public static CViewEditText create(View view) {
        return new CViewEditText(view);
    }

    private CViewEditText(View view) {

        this.view = view;
        this.text_input_layout = this.view.findViewById(R.id.text_input_layout);
        this.text_input_layout_edit = this.view.findViewById(R.id.text_input_layout_edit);
        this.text_input_layout_icon = this.view.findViewById(R.id.text_input_layout_icon);
        this.text_input_layout_label = this.view.findViewById(R.id.text_input_layout_label);
        this.text_input_layout_select = this.view.findViewById(R.id.text_input_layout_select);
        this.text_input_layout_touch = this.view.findViewById(R.id.text_input_layout_touch);

        this.changed = new ITextChanged() {
            @Override
            public void onChange(String text) {

            }
        };

        this.required = false;
        this.maximum = 0;
        this.minimum = 0;
        this.error = "";
        this.alert = "";
        this.showpass = false;
        this.isvalid = false;
        this.keyboard = true;
        this.type = TYPE.NONE;

        this.text_input_layout_edit.setFilters(new InputFilter[] {});
        this.text_input_layout_edit.setText("");
        this.text_input_layout.setPrefixText("");
        this.text_input_layout.setSuffixText("");
        this.text_input_layout.setErrorIconDrawable(null);

    }

    private CViewEditText build() {

        ViewGroup.LayoutParams params = this.text_input_layout.getLayoutParams();
        switch (this.type){
            case FECHA_TARJETA:
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                this.text_input_layout_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                this.text_input_layout_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case CURRENCY:
            case CURRENCY_SD:

                maximum = 11;
                minimum = 1;

                this.text_input_layout_edit.setOnFocusChangeListener((view1, b) -> {
                    text_input_layout_edit.setSelection(text_input_layout_edit.getText().length());
                });

            case NUMBER:
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                this.text_input_layout_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case NUMBER_PASS:
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case TEXT_PASS:
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case MULTI_TEXT:
                params.height = (int) this.text_input_layout.getResources().getDimension(R.dimen.text_area_height);
                this.text_input_layout.setLayoutParams(params);
                this.text_input_layout_edit.setGravity(Gravity.TOP);
                this.text_input_layout_edit.setSingleLine(false);
                this.text_input_layout_edit.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case SPINNER:
                params.height = (int) this.text_input_layout.getResources().getDimension(R.dimen.text_spinner_height);
                this.text_input_layout.setLayoutParams(params);
                this.text_input_layout_edit.setSingleLine(false);
                this.text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
        }

        setInputFilter(getText());
        setValid(validate(getText().length()), getText());

        return this;
    }

    public CViewEditText setPredictive(Boolean predictive) {

        if(!predictive) {
            this.text_input_layout_edit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            this.text_input_layout_edit.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            this.text_input_layout_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                this.text_input_layout_edit.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        return this.build();
    }

    public String getTextDouble() {
        return this.text_input_layout_edit.getText().toString().replaceAll(",", "").replace("$", "").trim();
    }

    public void setTextMonto(String text) {

        if(text == null || text.isEmpty())
            text = "0";

        type = TYPE.NONE;
        this.text_input_layout_edit.setText(Utils.paserCurrency(text));
        this.text_input_layout_edit.setTag(text.replaceAll(",", "").replace("$", "").trim());
        type = TYPE.CURRENCY;
    }

    public CViewEditText setType(TYPE type) {
        this.type = type;

        switch (this.type){
            case EMAIL:
                break;
            default:
                return setPredictive(false);
        }

        return this.build();
    }

    public CViewEditText setTypeInit(TYPE type) {
        this.type = type;

        switch (this.type){
            case EMAIL:
                break;
            default:
                return setPredictive(false);
        }

        return this;
    }

    public  void setShowPass(Boolean showPass) {
        this.text_input_layout_icon.performClick();
    }

    public CViewEditText setHint(String hint) {
        this.text_input_layout.setHint(hint);
        return this.build();
    }

    public CViewEditText setHint(Integer hint) {
        this.text_input_layout.setHint(this.view.getResources().getString(hint));
        return this.build();
    }

    public CViewEditText setHintInit(Integer hint) {
        this.text_input_layout.setHint(this.view.getResources().getString(hint));
        return this;
    }

    public CViewEditText setTextChanged(ITextChanged changed) {
        this.changed = changed;
        return this.build();
    }

    public CViewEditText setTextChangedInit(ITextChanged changed) {
        this.changed = changed;
        return this;
    }

    public CViewEditText setRequired(Boolean required) {
        this.required = required;
        return this.build();
    }

    public CViewEditText setRequiredInit(Boolean required) {
        this.required = required;
        return this;
    }

    public CViewEditText setMaximum(Integer maximum) {
        this.maximum = maximum;
        return this.build();
    }

    public CViewEditText setMaximumInit(Integer maximum) {
        this.maximum = maximum;
        return this;
    }

    public CViewEditText setMinimum(Integer minimum) {
        this.minimum = minimum;
        return this.build();
    }

    public CViewEditText setMinimumInit(Integer minimum) {
        this.minimum = minimum;
        return this;
    }

    public CViewEditText setSpinner(List<ModelItem> items) {

        if(this.text_input_layout_select != null && items != null) {

            this.text_input_layout_select.setPrompt(text_input_layout_edit.getHint());

            this.text_input_layout_icon = this.view.findViewById(R.id.text_input_layout_icon_select);
            if(this.text_input_layout_icon != null) {
                this.text_input_layout_icon.setVisibility(View.VISIBLE);
            }

            List<String> labels = new ArrayList();

            for (ModelItem item: items)
                labels.add(item.getName());

            ArrayAdapter<String> adapter = new ArrayAdapter(text_input_layout_edit.getContext(), R.layout.item_spinner, labels.toArray());

            this.text_input_layout_touch.setVisibility(View.VISIBLE);
            this.text_input_layout_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text_input_layout_select.performClick();
                    hideKeyboard();
                }
            });

            this.text_input_layout_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    text_input_layout_edit.setTag(items.get(position).getValue());
                    text_input_layout_edit.setText(items.get(position).getName());
                    hideKeyboard();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            this.text_input_layout_edit.setEnabled(false);
            this.text_input_layout_select.setAdapter(adapter);

            this.type = TYPE.SPINNER;

        }

        return this.build();
    }

    public CViewEditText removeSpinner() {

        this.text_input_layout_icon = this.view.findViewById(R.id.text_input_layout_icon_select);
        if(this.text_input_layout_icon != null) {
            this.text_input_layout_icon.setVisibility(View.GONE);
        }

        if(this.text_input_layout_touch != null ) {

            this.text_input_layout_touch.setVisibility(View.GONE);
            this.text_input_layout_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text_input_layout_select.performClick();
                    hideKeyboard();
                }
            });

        }

        this.text_input_layout_edit.setEnabled(true);

        return this.build();
    }

    public CViewEditText setDatePicker(boolean... menor) {

        if(this.text_input_layout_touch != null) {

            this.text_input_layout_select.setPrompt(text_input_layout_edit.getHint());

            this.text_input_layout_icon = this.view.findViewById(R.id.text_input_layout_icon_select);
            if(this.text_input_layout_icon != null) {
                this.text_input_layout_icon.setVisibility(View.VISIBLE);
            }

            this.text_input_layout_touch.setVisibility(View.VISIBLE);
            this.text_input_layout_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePicker(menor.length > 0 ? menor[0] : false);
                    hideKeyboard();
                }
            });

        }

        return this.build();
    }

    public CViewEditText setDatePickerReport() {

        if(this.text_input_layout_touch != null) {

            this.text_input_layout_select.setPrompt(text_input_layout_edit.getHint());

            this.text_input_layout_icon = this.view.findViewById(R.id.text_input_layout_icon_select);
            if(this.text_input_layout_icon != null) {
                this.text_input_layout_icon.setVisibility(View.VISIBLE);
            }

            this.text_input_layout_touch.setVisibility(View.VISIBLE);
            this.text_input_layout_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerReport();
                    hideKeyboard();
                }
            });

            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String d = String.format("%2s", day).replace(' ', '0');
            String m = String.format("%2s", (month + 1)).replace(' ', '0');
            this.text_input_layout_edit.setText(d + "/" + m + "/" + year);

        }

        return this;
    }

    public CViewEditText setIcon(Integer icon) {
        text_input_layout_icon.setImageDrawable(view.getResources().getDrawable(icon));
        this.text_input_layout_icon.setVisibility(View.VISIBLE);
        return this.build();
    }

        public CViewEditText setIconPass(Integer show, Integer hide) {

        if(this.type == TYPE.NUMBER_PASS || this.type == TYPE.TEXT_PASS) {
            this.text_input_layout_icon.setVisibility(View.VISIBLE);
            this.text_input_layout_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(showpass) {

                        text_input_layout_icon.setImageDrawable(view.getResources().getDrawable(hide));

                        switch (type){
                            case NUMBER_PASS:
                                text_input_layout_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                break;
                            case TEXT_PASS:
                                text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                break;
                        }

                    } else {

                        text_input_layout_icon.setImageDrawable(view.getResources().getDrawable(show));

                        switch (type){
                            case NUMBER_PASS:
                                text_input_layout_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                                break;
                            case TEXT_PASS:
                                text_input_layout_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                break;
                        }

                    }

                    showpass = !showpass;

                    text_input_layout_edit.setSelection(getText().length());


                }
            });
        }

        return this.build();
    }

    public CViewEditText setError(String error) {
        this.error = error;
        return this.build();
    }

    public void setError(String error, boolean isBuild) {
        this.error = error;
    }

    public void activeError() {
        text_input_layout.setError(error);
        text_input_layout_label.setText(error);
        text_input_layout_label.setVisibility(View.VISIBLE);
    }

    public CViewEditText deactiveError(){
        return this.build();
    }

    public CViewEditText setError(Integer error) {
        this.error = this.view.getResources().getString(error);
        return this.build();
    }

    public CViewEditText setAlert(String alert) {
        this.alert = alert;
        return this.build();
    }

    public CViewEditText setAlert(Integer alert) {
        this.alert = this.view.getResources().getString(alert);
        return this.build();
    }

    public CViewEditText setAlertInit(Integer alert) {
        this.alert = this.view.getResources().getString(alert);
        return this;
    }

    public CViewEditText setText(String text) {

        this.text_input_layout_edit.setText(text);

        if(text != null && !text.isEmpty()) {
            this.text_input_layout_edit.requestFocus();
        }

        return this.build();
    }

    public CViewEditText setTextDecimal(String text) {

        TYPE old = type;

        setType(TYPE.NONE);

        this.text_input_layout_edit.setText(parserCurrency(text));

        if(text != null && !text.isEmpty()) {
            this.text_input_layout_edit.requestFocus();
        }

        setType(old);

        return this.build();
    }

    public CViewEditText setEnabled(Boolean enabled) {
        this.text_input_layout_edit.setEnabled(enabled);
        return this.build();
    }

    public CViewEditText setEnabledInit(Boolean enabled) {
        this.text_input_layout_edit.setEnabled(enabled);
        return this;
    }
    public CViewEditText setHorizontalScroll() {
        this.text_input_layout_edit.setFocusable(false);
        this.text_input_layout_edit.setClickable(false);
        this.text_input_layout_edit.setFocusableInTouchMode(false);
        this.text_input_layout_edit.setEnabled(true);
        this.text_input_layout_edit.setKeyListener(null);
        this.text_input_layout_edit.setHorizontallyScrolling(true);

        return this;
    }




    public String getText() {
        return this.text_input_layout_edit.getText().toString().trim();
    }

    public String getTag() {
        return this.text_input_layout_edit.getTag() != null ? this.text_input_layout_edit.getTag().toString().trim() : "";
    }

    public void setTag(String tag) {
        this.text_input_layout_edit.setTag(tag);
    }

    public CViewEditText setSuffix(String prefix) {
        this.text_input_layout.setSuffixText(prefix);
        return this.build();
    }

    public CViewEditText setPrefix(String prefix) {
        this.text_input_layout.setPrefixText(prefix);
        return this.build();
    }

    public String getTextDecimal() {
        return this.text_input_layout_edit.getText().toString().trim()
                .replaceAll("\\$", "")
                .replaceAll(",", "");
    }

    public String getTextInteger() {

        if(this.text_input_layout_edit.getText().toString().trim().contains("."))
            return this.text_input_layout_edit.getText().toString().trim()
                    .replaceAll("\\$", "")
                    .replaceAll(",", "").split(".")[0];
        else
            return this.text_input_layout_edit.getText().toString().trim()
                    .replaceAll("\\$", "")
                    .replaceAll(",", "");

    }

    private Boolean validate(int size) {

        Boolean result = true;

        if(required) {

            switch (type) {
                case FECHA_TARJETA:
                    if(this.text_input_layout_edit.getText().toString().trim().length() > 0){
                        if(maximum > 0){
                            if(this.text_input_layout_edit.getText().toString().trim().length() >= minimum)
                                result = true;
                        }else{
                            if(this.text_input_layout_edit.getText().length() > 0)
                                result = true;
                        }

                    }
                    break;
                case EMAIL:
                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                    Matcher matcher = pattern.matcher(getText());
                    result = matcher.matches();
                    break;
                case RFC:
                    result = getText().matches(MATCH_RFC);
                    break;
                case CURRENCY:

                    if(!getTextDecimal().isEmpty()) {
                        double monto = Double.valueOf(getTextDecimal());
                        result = monto > 0;
                    }

                    break;
                case CURRENCY_SD:

                    if(!getTextDecimal().isEmpty()) {
                        double montosn = Double.valueOf(getTextDecimal());
                        result = montosn >= 1;
                    }

                    break;
                default:
                    break;
            }

            if(!result || getText().isEmpty() || (minimum > 0 && size < minimum && result)) {

                text_input_layout.setError(alert);
                text_input_layout_label.setText(alert);
                text_input_layout_label.setVisibility(View.VISIBLE);
                return false;
            }

        }

        if(size +1 > maximum && keyboard) {
            hideKeyboard();
            keyboard = false;
        } else
            keyboard = true;

        text_input_layout_label.setText(null);
        text_input_layout_label.setVisibility(View.GONE);
        text_input_layout.setError(null);

        return true;
    }

    public Boolean isValid() {
        return required ? isvalid : true;
    }

    private void setValid(Boolean isvalid, String text) {

        this.isvalid = isvalid;

        try {
            changed.onChange(text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setInputFilter(String text) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {

                //Log.e("CharSequence", "src: " + src + " - " + "start: " + start + " - " + "end: " + end + " - " + "dest: " + dest + " - " + "dstart: " + dstart + " - " + "dend: " + dend);

                if(type == TYPE.CURRENCY || type == TYPE.CURRENCY_SD)
                    if(src.length() > 1)
                        return src;

                switch (type) {

                    case FECHA_TARJETA:

                        if(src.length() > 0 && (dest.toString() + src.toString()).length() == maximum)
                            changed.onChange(text);

                        if(text_input_layout_edit.getText().length() == 2 && src.length() > 0)
                            return "/" + src ;

                        if(src.toString().matches("[0-9]+"))
                            return src;

                        break;
                    case EMAIL:

                        if(src.toString().matches("[a-zA-Z0-9@.\\-\\_]+"))
                            return src;

                        break;
                    case TEXT_PASS:

                        if(src.toString().matches("[a-zA-Z0-9]+"))
                            return src;

                        break;
                    case CURRENCY:

                        try {

                            String text = dest.toString().trim().replace("$", "").replace(",", "").replace(".","") + src;

                            if (text.length()==0)
                                return "";

                            if(src.length() == 0)
                                text = text.substring(0, text.length() - 1 );

                            if(text.length() > 8) {
                                hideKeyboard();
                                return "";
                            }

                            text = new Formatter().format("%03d", Integer.parseInt(text)).toString();

                            text = text.substring(0, text.length() - 2) + "." + text.substring(text.length() - 2);

                            setText(parserCurrency(text));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        break;
                    case CURRENCY_SD:

                        try {

                            String textsn = dest.toString().trim().replace(".00", "").replace("$", "").replace(",", "") + src;

                            if(src.length() == 0)
                                textsn = textsn.substring(0, textsn.length() - 1 );

                            textsn = textsn + ".00";

                            setText(parserCurrency(textsn));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        break;

                    case TEXT_SN:

                        if(!src.toString().matches(MATCH_TEXT_SN) || spaceEnd(dest.toString(), src.toString()))
                            return "";
                        else
                            return src;
                    case TEXT:

                        if(!src.toString().matches(MATCH_TEXT) || spaceEnd(dest.toString(), src.toString()))
                            return "";
                        else
                            return src;

                    default:
                        return src;
                }

                return "";
            }
        };

        this.text_input_layout_edit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(maximum)});

        this.text_input_layout_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                try {

                    text_input_layout_edit.setSelection(s.toString().length());

                    if(type != TYPE.CURRENCY && type != TYPE.CURRENCY_SD){
                        setValid(validate(s.length()),  s.toString());
                    }

                } catch (Exception ex){ }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String parserCurrency(String dato) {

        if(dato.isEmpty())
            return "";

        try {

            double value = Double.parseDouble(dato.replace(",", "").replace("$", ""));

            if(value == 0)
                return "";

            return NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

        } catch (Exception ex) {
            return "";
        }

    }

    private void datePicker (boolean menor) {

        Date date = new Date();
        date.setYear(date.getYear() - (menor ? 18 : 0));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.text_input_layout_edit.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String d = String.format("%2s", day).replace(' ', '0');
                String m = String.format("%2s", (month + 1)).replace(' ', '0');

                text_input_layout_edit.setText(d + "/" + m + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.setTitle("Seleccionar Fecha");
        datePickerDialog.setCancelable(false);

        datePickerDialog.show();

    }


    private void datePickerReport () {

        Date date = new Date();

        final Calendar calendarMin = Calendar.getInstance();;
        calendarMin.setTime(date);
        calendarMin.add(Calendar.DAY_OF_MONTH,-30);

        final Calendar calendarMax = Calendar.getInstance();;
        calendarMax.setTime(date);
        calendarMax.add(Calendar.DAY_OF_MONTH,-1);

        int year = calendarMax.get(Calendar.YEAR);
        int month = calendarMax.get(Calendar.MONTH);
        int day = calendarMax.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.text_input_layout_edit.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String d = String.format("%2s", day).replace(' ', '0');
                String m = String.format("%2s", (month + 1)).replace(' ', '0');
                text_input_layout_edit.setText(d + "/" + m + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(calendarMin.getTimeInMillis());

        datePickerDialog.setTitle("Seleccionar Fecha");
        datePickerDialog.setCancelable(false);

        datePickerDialog.show();

    }


    public enum TYPE{
        TEXT,
        TEXT_PASS,
        TEXT_SN,
        NUMBER,
        NUMBER_PASS,
        CURRENCY,
        CURRENCY_SD,
        EMAIL,
        RFC,
        CURP,
        NONE,
        MULTI_TEXT,
        SPINNER,
        FECHA_TARJETA
    }

    public interface ITextChanged {
        void onChange(String text);
    }

    public boolean spaceEnd(String text, String src) {

        if(text != null && !text.isEmpty())
            return text.substring(text.length() - 1).trim().isEmpty() && src.trim().isEmpty();

        return false;
    }


}
