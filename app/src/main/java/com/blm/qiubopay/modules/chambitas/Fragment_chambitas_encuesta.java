package com.blm.qiubopay.modules.chambitas;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeAnswer;
import com.google.android.material.textfield.TextInputLayout;
import com.blm.qiubopay.models.chambitas.retos.TipoReto;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_chambitas_encuesta extends HFragment implements IMenuContext {

    public static TipoReto tipoReto;
    public static  String encuestaImg;
    public static List<QPAY_ActiveCampaign_Questions> list = new ArrayList<>();
    public List<QPAY_ChallengeAnswer> answers;
    public static IFunction execute;
    public Button btn_contestar;
    public ImageView iv_apoyo_visual;




    public HashMap<Integer,Boolean> map = new HashMap<>();
    public HashMap<Integer,String> mapAnswers = new HashMap<>();







    public static Fragment_chambitas_encuesta newInstance() {
        return new Fragment_chambitas_encuesta();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_survey, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .showLogo()
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        iv_apoyo_visual = (ImageView) getView().findViewById(R.id.iv_apoyo_visual);
        iv_apoyo_visual.setVisibility(tipoReto == TipoReto.ENCUESTA ? ( (encuestaImg != null && !encuestaImg.isEmpty()) ?   View.VISIBLE : View.GONE): View.GONE);

        Glide.with(iv_apoyo_visual.getContext())
        .load(encuestaImg)
        .error(R.drawable.no_disponible)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(iv_apoyo_visual);

        RecyclerView list_survey = getView().findViewById(R.id.list_survey);
        list_survey.setLayoutManager(new LinearLayoutManager(getActivity()));
        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = getContext().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen._1sdp);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        list_survey.addItemDecoration(itemDecoration);


        answers = new ArrayList();

        for(QPAY_ActiveCampaign_Questions item : list) {
            answers.add(new QPAY_ChallengeAnswer());
        }


        QuestionsAdapter adapter = new QuestionsAdapter(list,getContext());
        list_survey.setAdapter(adapter);

        btn_contestar = getView().findViewById(R.id.btn_contestar);
        btn_contestar.setEnabled(false);

        btn_contestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendData();
                execute.execute(answers);
            }
        });

    }

    // JLH : Refactor adapter, se pasa de listView a RecyclerView
    public  class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

        private List<QPAY_ActiveCampaign_Questions> items;
        private Activity activity;

        public QuestionsAdapter(List<QPAY_ActiveCampaign_Questions> items, Activity activity) {
            this.items = items;
            this.activity = activity;
        }

        @NonNull
        @Override
        public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey, parent, false);
            return new QuestionsAdapter.ViewHolder(v, new CustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
            final QPAY_ActiveCampaign_Questions item = this.items.get(holder.getAdapterPosition());

            holder.til_other.setErrorTextAppearance(R.style.InputError_Scarlet);
            holder.til_other.setError("Campo obligatorio");
            holder.text_input_layout_label.setText("Campo obligatorio");
            holder.text_input_layout_label.setVisibility(View.VISIBLE);

            holder.text_question.setText(item.getQuestion());

            for(int i=0; i<item.getAnswers().size(); i++) {
                RadioButton rad_answer = new RadioButton(getContext());
                rad_answer.setText(item.getAnswers().get(i).getAnswer());
                holder.rad_group.addView(rad_answer);

                Integer adapterPosition = holder.getAdapterPosition();
                holder.customEditTextListener.updateQuestionId(Integer.parseInt(item.getId()));
                holder.customEditTextListener.getTextInputLayout(holder.til_other);
                holder.customEditTextListener.getTextView(holder.text_input_layout_label);
                holder.customEditTextListener.getEditText(holder.et_other_answer);
                int finalI = i;
                Boolean isOpenQuestion = item.getAnswers().get(finalI).getIsOpenQuestion();

                rad_answer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.til_other.clearFocus();

                        if (isOpenQuestion){
                            holder.et_other_answer.setVisibility(View.VISIBLE);
                            holder.til_other.setVisibility(View.VISIBLE);
                            map.put(Integer.parseInt(item.getId()),item.getAnswers().get(finalI).getIsOpenQuestion());

                        }else{
                            holder.til_other.setVisibility(View.GONE);
                            holder.et_other_answer.setText("");
                            mapAnswers.remove(Integer.parseInt(item.getId()));
                            answers.get(holder.getAdapterPosition()).setAnswer(null);
                            map.remove(Integer.parseInt(item.getId()));
                        }

                        answers.get(adapterPosition).setQuestionId(item.getId());
                        answers.get(adapterPosition).setAnswerId(item.getAnswers().get(finalI).getId());

                        validate();

                    }
                });

                if(item.getAnswers().get(finalI).getId().equals(answers.get(adapterPosition).getAnswerId())) {
                    rad_answer.setChecked(true);
                    validate();
                }

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
            public RadioGroup rad_group;
            public RadioButton rad_answer;
            public TextView text_question;
            public EditText et_other_answer;
            public TextInputLayout til_other;
            public TextView text_input_layout_label;
            public CustomEditTextListener customEditTextListener;


            public ViewHolder(View itemView, CustomEditTextListener customEditTextListener) {
                super(itemView);
                rad_group = (RadioGroup) itemView.findViewById(R.id.rad_group);
                text_question = (TextView) itemView.findViewById(R.id.text_question);
                rad_answer =  new RadioButton(getContext());
                et_other_answer = (EditText) itemView.findViewById(R.id.et_other_answer);
                til_other = (TextInputLayout) itemView.findViewById(R.id.til_other);
                this.customEditTextListener = customEditTextListener;
                et_other_answer.addTextChangedListener(customEditTextListener);
                text_input_layout_label = (TextView) itemView.findViewById(R.id.text_input_layout_label);

            }
        }


        private class CustomEditTextListener implements TextWatcher {
            private int questionId;
            private TextInputLayout textInputLayout;
            private TextView textView;
            private EditText editText;


            public void updateQuestionId(int questionId) {
                this.questionId = questionId;
            }

            public void getTextInputLayout(TextInputLayout textInputLayout){
                this.textInputLayout = textInputLayout;
            }

            public void getTextView(TextView textView){
                this.textView = textView;
            }

            public void getEditText(EditText editText){
                this.editText = editText;
            }


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do smth
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if (!(charSequence.toString().trim().length() > 0)) {
                    setError(textInputLayout,textView);
                    mapAnswers.remove(questionId);

                }else{
                    desactiveError(textInputLayout,textView);
                    mapAnswers.put(questionId,charSequence.toString());

                }
                validate();
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


    }

    public void validate() {

        btn_contestar.setEnabled(false);

        for(QPAY_ChallengeAnswer ans : answers ){
            if(ans.getAnswerId() == null || ans.getAnswerId().isEmpty()){  // Primer filtro, checa que venga el answerId
                return;
            }
        }
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()){   // Recorre el map que trae lo sig: <Int(Id), Bool(isOpenQuestion)>  (el id es de la pregunta)
            if (entry.getValue()){   // Si la bandera viene en true (isOpenQuestion)
                if (mapAnswers.size() == 0){ // Si el map que tra la respuestas viene con un longuitud de 0 retorna
                    return;
                }
                if (map.size() != mapAnswers.size()){  // Si difieren las longuitudes entonces retorna
                    return;
                }
            }
            }

            btn_contestar.setEnabled(true); // Si se llegó hasta este punto es porque  esta OK entonces se procede mandar las resp. abiertas
    }

    public void setError(TextInputLayout textInputLayout, TextView textView){
        textInputLayout.setErrorTextAppearance(R.style.InputError_Scarlet);
        textInputLayout.setError("Campo obligatorio");
        textView.setText("Campo obligatorio");
        textView.setVisibility(View.VISIBLE);
    }

    public void desactiveError(TextInputLayout textInputLayout, TextView textView){
        textInputLayout.setErrorEnabled(false);
        textView.setVisibility(View.GONE);
    }



    public void appendData(){
        for(int i=0; i<answers.size(); i++) {
            for (Map.Entry<Integer, String> entryAnswers : mapAnswers.entrySet()) {
                if (answers.get(i).getQuestionId().equals(entryAnswers.getKey().toString())) {
                    answers.get(i).setAnswer(entryAnswers.getValue());
                }
            }
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}