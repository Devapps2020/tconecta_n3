package com.blm.qiubopay.modules.chambitas.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhoto;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecyclerPreguntaAbiertaAdapter  extends RecyclerView.Adapter<RecyclerPreguntaAbiertaAdapter.ViewHolder> {


    private ArrayList<QPAY_ActiveCampaign_Questions> items;
    private RecyclerPreguntaAbiertaAdapter.OnItemClickListener onItemClickListener;
    private Activity activity;
    private RecyclerPreguntaAbiertaAdapter.OnEditTextChanged onEditTextChanged;
    public ArrayList<CViewEditText> campos = new ArrayList<>();
    Pattern mPattern = Pattern.compile("([0-9]{0,6})(\\.[0-9]{0,2})?");

    public RecyclerPreguntaAbiertaAdapter(ArrayList<QPAY_ActiveCampaign_Questions> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    public void setOnItemClickListener(RecyclerPreguntaAbiertaAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTextChanged(RecyclerPreguntaAbiertaAdapter.OnEditTextChanged onEditTextChanged) {
        this.onEditTextChanged = onEditTextChanged;
    }


    @NonNull
    @Override
    public RecyclerPreguntaAbiertaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        RecyclerPreguntaAbiertaAdapter.ViewHolder vh;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta_abierta, parent, false);
        vh = new RecyclerPreguntaAbiertaAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPreguntaAbiertaAdapter.ViewHolder holder, int position) {
        final QPAY_ActiveCampaign_Questions item = this.items.get(position);

        holder.textInputLayout.setErrorTextAppearance(R.style.InputError_Scarlet);
        holder.textInputLayout.setError("Campo obligatorio");
        holder.text_input_layout_label.setText("Campo obligatorio");
        holder.text_input_layout_label.setVisibility(View.VISIBLE);

        holder.tv_pregunta.setText(item.getQuestion());

        if (item.getIsNumeric().equals("0")){
            holder.et_respuesta.addTextChangedListener(holder.alphanumericTextWatcher);

            holder.alphanumericTextWatcher.updatePosition(position);
            holder.alphanumericTextWatcher.updateItemId(item.getId());
            holder.alphanumericTextWatcher.updateInputType(item.getIsNumeric());
            holder.alphanumericTextWatcher.getTextView(holder.text_input_layout_label);
            holder.alphanumericTextWatcher.getTextInputLayout(holder.textInputLayout);
            holder.et_respuesta.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (item.getIsNumeric().equals("1")){
            holder.et_respuesta.addTextChangedListener(holder.numberTextWatcher);

            holder.numberTextWatcher.updateItemId(item.getId());
            holder.numberTextWatcher.updatePosition(position);
            holder.numberTextWatcher.updateInputType(item.getIsNumeric());
            holder.numberTextWatcher.getTextView(holder.text_input_layout_label);
            holder.numberTextWatcher.getTextInputLayout(holder.textInputLayout);
            holder.et_respuesta.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.et_respuesta.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        }

    }

    private class NumberTextWatcher implements TextWatcher {

        private EditText editText;
        private TextInputLayout textInputLayout;
        private TextView textView;
        private int position;
        private String id;
        private String inputType;

        public void updatePosition(int position) {
            this.position = position;
        }

        public void updateItemId(String id) {
            this.id = id;
        }

        public void updateInputType(String inputType) {
            this.inputType = inputType;
        }

        public void getTextInputLayout(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }
        public void getTextView(TextView textView) {
            this.textView = textView;
        }

        public NumberTextWatcher(EditText editText) {
            this.editText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            editText.removeTextChangedListener(this);

            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(
                        CharSequence source,
                        int start,
                        int end,
                        Spanned dest,
                        int dstart,
                        int dend) {

                    String result =
                            dest.subSequence(0, dstart)
                                    + source.toString()
                                    + dest.subSequence(dend, dest.length());

                    Matcher matcher = mPattern.matcher(result);

                    if (!matcher.matches()) return dest.subSequence(dstart, dend);

                    return null;
                }
            };
            editText.setFilters(new InputFilter[]{filter});



            editText.addTextChangedListener(this);

            onEditTextChanged.onTextChanged(position, s.toString(), Integer.valueOf(id),textInputLayout,textView,inputType);

        }

        public void afterTextChanged(Editable s) {

        }

    }

    private class AlphanumericTextWatcher implements TextWatcher {
        private EditText editText;
        private TextInputLayout textInputLayout;
        private TextView textView;
        private int position;
        private String id;
        private String inputType;

        public void updatePosition(int position) {
            this.position = position;
        }

        public void updateItemId(String id) {
            this.id = id;
        }

        public void updateInputType(String inputType) {
            this.inputType = inputType;
        }

        public void getTextInputLayout(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }
        public void getTextView(TextView textView) {
            this.textView = textView;
        }


        public AlphanumericTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // do smth
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            onEditTextChanged.onTextChanged(position,charSequence.toString(),Integer.valueOf(id),textInputLayout,textView,inputType);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // do smth
            InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    String stringSource = source.toString();
                    String stringDest = dest.toString();
                    if (stringSource.equals(" ")) {
                        if (stringDest.length() == 0)
                            return "";
                        if (stringDest.length() >= 1)
                            if ((dstart > 0 && editable.charAt(dstart - 1) == ' ') || (editable.length() >  dstart && editable.charAt(dstart) == ' ') || dstart == 0)
                                return "";
                    }
                    return null;
                }
            };
            editText.setFilters(new InputFilter[]{filter});
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

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public EditText et_respuesta;
        public TextView tv_pregunta;
        public TextInputLayout textInputLayout;
        public TextView text_input_layout_label;

        public AlphanumericTextWatcher alphanumericTextWatcher;
        public NumberTextWatcher numberTextWatcher;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_pregunta = (TextView) itemView.findViewById(R.id.tv_pregunta);
            et_respuesta = (EditText) itemView.findViewById(R.id.et_respuesta);
            textInputLayout = (TextInputLayout) itemView.findViewById(R.id.textInputLayout_respuesta);
            text_input_layout_label = (TextView) itemView.findViewById(R.id.text_input_layout_label);

            numberTextWatcher = new NumberTextWatcher(et_respuesta);
            alphanumericTextWatcher = new AlphanumericTextWatcher(et_respuesta);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, QPAY_ChallengePhoto viewModel);
    }

    public interface OnEditTextChanged {
        // here component_id is editTextId (findView by Id)
        void onTextChanged( int position, String charSeq, int itemId, TextInputLayout textInputLayout, TextView textView, String inputType);
    }
}
