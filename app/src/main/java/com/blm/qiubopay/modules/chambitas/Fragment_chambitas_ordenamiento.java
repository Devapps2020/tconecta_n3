package com.blm.qiubopay.modules.chambitas;

import static com.liveperson.infra.utils.Utils.getResources;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.models.Entry;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Answers;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;

import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeAnswer;
import com.blm.qiubopay.models.chambitas.retos.TipoReto;
import com.blm.qiubopay.modules.chambitas.adapter.RecyclerOrdenamientoAdapter;
import com.blm.qiubopay.utils.MarginItemDecoration;
import com.j256.ormlite.stmt.query.In;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_chambitas_ordenamiento extends HFragment implements RecyclerOrdenamientoAdapter.OnEditTextChanged{

    public static ArrayList<QPAY_ActiveCampaign_Questions> items = new ArrayList<>();
    public static IFunction execute;
    public static TipoReto tipoReto;
    public static String indication;
    public Button btn_contestar;
    public ImageView iv_apoyo_visual;
    public TextView tv_instrucciones;
    private RecyclerView rv_chambitas;
    private RecyclerOrdenamientoAdapter adapter;


    HashMap<Integer, Integer> map;

    public ArrayList<QPAY_ChallengeAnswer> order;


    public static Fragment_chambitas_ordenamiento newInstance() {
        return new Fragment_chambitas_ordenamiento();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_recycler, container, false), R.drawable.background_splash_header_1);
    }


    @Override
    public void initFragment() {
        map = new HashMap<>();
        map.clear();
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
        tv_instrucciones = (TextView) getView().findViewById(R.id.tv_instrucciones);
        tv_instrucciones.setVisibility(indication != null ? (!indication.isEmpty() ? View.VISIBLE : View.GONE) : View.GONE);
        tv_instrucciones.setText(indication != null ? indication : "");

        iv_apoyo_visual = (ImageView) getView().findViewById(R.id.iv_apoyo_visual);
        // JLH Se deja apoyo visual  por si se pide en el futuro

        rv_chambitas = (RecyclerView) getView().findViewById(R.id.rv_chambitas);
        rv_chambitas.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (tipoReto == TipoReto.ORDENAMIENTO_PREGUNTA) {
            rv_chambitas.addItemDecoration(new MarginItemDecoration(getContext(),0));
        }

        adapter = new RecyclerOrdenamientoAdapter(items,getActivity(),tipoReto);
        adapter.setOnTextChanged(this::onTextChanged);
        rv_chambitas.setAdapter(adapter);


        btn_contestar = (Button) getView().findViewById(R.id.btn_contestar);
        btn_contestar.setEnabled(false);
        btn_contestar.setText(R.string.text_chambitas_27);
        btn_contestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute.execute(order);
            }
        });

    }

    //JLH: Se cambian validaciones por cambio en la definición del modelo de petición
    public void validate(HashMap<Integer, Integer> hashMap) {
        btn_contestar.setEnabled(false);

        btn_contestar.setEnabled(getStatus(hashMap));
        if (getStatus(hashMap)) { // Si todas la validaciones son correctas
            getContext().hideKeyboard();    // Dismiss teclado
            QPAY_ChallengeAnswer challengeAnswer;
            for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) { // for para recorrer values del hashmap
                Integer value = entry.getValue(); // Aqui obtiene los valores List<Integer>, (1,81) , la primer posc. es la respuesta del usuario, la segunda es el id
                Integer key = entry.getKey();
                challengeAnswer = new QPAY_ChallengeAnswer();
                for (QPAY_ActiveCampaign_Questions question : items) { // Aquí recorre el array de preguntas
                    if (key.toString().equals(question.getId())) { // Valida si el id de la pregunta coincide con el del array
                        challengeAnswer.setQuestionId(question.getId());
                        for (QPAY_ActiveCampaign_Answers answer : question.getAnswers()) { // Si solo si hay coincidencia recorre ahora el array de respuestas
                            if (value.toString().equals(answer.getAnswer())) { //  Valida si coincide la respuesta que metio el usuario vs la del array de resp.
                                challengeAnswer.setAnswerId(answer.getId());
                            }
                        }
                    }
                }
                order.add(challengeAnswer);
            }
        } else {
            order = new ArrayList<QPAY_ChallengeAnswer>();
        }


    }

    private boolean getStatus(HashMap<Integer, Integer>  hashMap){

        boolean status = true;
        Set valueSet = new HashSet(hashMap.values());

        if (hashMap.containsValue(0)){                          // Valida que no contenga el 0
            status = false;
        } else if (hashMap.values().size()!=valueSet.size()){   // Valida que no haya números repetidos
            status = false;
        } else if (items.size() != hashMap.size()){            // Valida el tamaño del arreglo
            status = false;
        }else if (isGreater(hashMap)){                         // Valida que no sea mayor al tamaño del array
            status = false;
        }
            return status;
    }

    private  boolean isGreater(HashMap<Integer, Integer> hashMap){
        boolean status = false;
        for ( Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
            Integer value = entry.getValue();
            if (value > hashMap.size()){
                 status = true;
            }
        }
            return  status;
    }



        @Override
        public void onTextChanged( int position, String charSeq, int itemId, EditText et) {
            Log.d("EditText",charSeq)  ;
            Log.d("Position", String.valueOf(position));
            Log.d("itemId", String.valueOf(itemId));


            int num = 0;
            try{
                if(charSeq != null){
                    num = Integer.parseInt(charSeq);
                }
            }catch(NumberFormatException nfe){
                nfe.getLocalizedMessage();
            }
            if (map != null ){
                // iterate each entry of hashmap
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

                    if(entry.getKey() != itemId) {
                        if (entry.getValue() == num){
                            Log.d("hashMap", String.valueOf(map))  ;
                            Log.d("num", String.valueOf(num))  ;

                            getContext().alert("No puede haber números repetidos");
                            et.getText().clear();
                            return;
                        }

                    }
                }

                    if (num > items.size()){
                        getContext().alert("No puede haber números mayores que " + items.size());
                        et.getText().clear();
                        return;
                    }


            }
            if (num == 0 && map != null){
                map.remove(itemId);
            }else{
                map.put(itemId, num);
            }


                validate(map);
        }
}
