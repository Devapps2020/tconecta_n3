package com.blm.qiubopay.modules.chambitas;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;
import com.blm.qiubopay.models.chambitas.retos.OpenQuestion.Question;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeAnswer;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhoto;
import com.blm.qiubopay.models.chambitas.retos.TipoReto;
import com.blm.qiubopay.modules.chambitas.adapter.RecyclerOrdenamientoAdapter;
import com.blm.qiubopay.modules.chambitas.adapter.RecyclerPreguntaAbiertaAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_chambitas_pregunta_abierta extends HFragment {
    public static ArrayList<QPAY_ActiveCampaign_Questions> items = new ArrayList<>();
    public static IFunction execute;
    public static TipoReto tipoReto;
    public Button btn_contestar;
    private RecyclerView rv_chambitas;
    private RecyclerPreguntaAbiertaAdapter adapter;

    HashMap<Integer,String> map = new HashMap<>();

    public ArrayList<Question> openQuestion = new ArrayList<Question>();

    Pattern mPatternDigits = Pattern.compile("([0-9]{0,6})?(\\.)");
    Pattern mPatternPoint = Pattern.compile("(\\.)");


    public static Fragment_chambitas_pregunta_abierta newInstance() {
        return new Fragment_chambitas_pregunta_abierta();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_recycler, container, false), R.drawable.background_splash_header_1);
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

        rv_chambitas = (RecyclerView) getView().findViewById(R.id.rv_chambitas);
        rv_chambitas.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerPreguntaAbiertaAdapter(items,getActivity());
        adapter.setOnTextChanged(this::onTextChanged);
        rv_chambitas.setAdapter(adapter);

        btn_contestar = (Button) getView().findViewById(R.id.btn_contestar);
        btn_contestar.setEnabled(false);
        btn_contestar.setText(R.string.text_chambitas_25);
        btn_contestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question;
                openQuestion.clear();
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    String value = entry.getValue();
                    Integer key = entry.getKey();
                    question = new Question();
                    for (QPAY_ActiveCampaign_Questions q : items) { // Aquí recorre el array de preguntas
                        if (key.toString().equals(q.getId())) { // Valida si el id de la pregunta coincide con el del array
                            question.setQuestionId(Integer.valueOf(q.getId()));
                        }
                    }
                    question.setAnswer(value);
                    openQuestion.add(question);
                }

                execute.execute(openQuestion);
            }
        });

    }

    public void validate(HashMap<Integer, String> hashMap) {
        btn_contestar.setEnabled(getStatus(hashMap));
    }

    private boolean getStatus(HashMap<Integer,String>  hashMap){

        boolean status;

        Log.d("hashMap", String.valueOf(hashMap.values()));

        if ( hashMap.values().contains("") || hashMap.values().isEmpty() || items.size() != hashMap.size()  ) {
            status = false;
        }else{
            status = true;
        }
        return status;
    }
    public void setError(TextInputLayout textInputLayout, TextView textView){
        textInputLayout.setErrorTextAppearance(R.style.InputError_Scarlet);
        textInputLayout.setError("Campo obligatorio");
        textView.setText("Campo obligatorio");
        textView.setVisibility(View.VISIBLE);
    }

    public void desactiveError(TextInputLayout textInputLayout, TextView textView){
        textView.setVisibility(View.GONE);
        textInputLayout.setErrorEnabled(false);
    }


    public void onTextChanged( int position, String charSeq, int itemId, TextInputLayout textInputLayout, TextView textView, String inputType) {

        Matcher matcherDigits = mPatternDigits.matcher(charSeq);
        Matcher matcherPoint = mPatternPoint.matcher(charSeq);
        Log.d("Position", String.valueOf(position));
        Log.d("Value",String.valueOf(charSeq));

        if ((!(charSeq.trim().length() > 0)) && (inputType.equals("0"))){ // inputType = 0 entrada alfanúmerica
            if (textInputLayout != null) {
                 setError(textInputLayout,textView);
            }
            map.remove(itemId);
        }else if((matcherDigits.matches() || matcherPoint.matches() || charSeq.isEmpty()) && (inputType.equals("1"))){  // inputType = 1 entrada numerica
            if (textInputLayout != null) {
                setError(textInputLayout,textView);
            }
            map.remove(itemId);
        }else{
            desactiveError(textInputLayout,textView);
            map.put(itemId,charSeq);
        }

        validate(map);



    }

}
