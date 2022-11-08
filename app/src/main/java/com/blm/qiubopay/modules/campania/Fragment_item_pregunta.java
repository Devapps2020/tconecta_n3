package com.blm.qiubopay.modules.campania;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.listeners.ICreateCampaignAnswers;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.questions.QPAY_CampaignAnswers;
import com.blm.qiubopay.models.questions.QPAY_CampaignAnswers_answers;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestions_answers;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestions_questions;
import com.blm.qiubopay.modules.registro.Fragment_registro_1;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_item_pregunta extends HFragment {

    private QPAY_DynamicQuestions_questions question;
    List<LinearLayout> items = new ArrayList();
    QPAY_CampaignAnswers_answers response;

    public static Fragment_item_pregunta newInstance(Object... data) {
        Fragment_item_pregunta fragment = new Fragment_item_pregunta();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_item_pregunta", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            question = new Gson().fromJson(getArguments().getString("Fragment_item_pregunta"), QPAY_DynamicQuestions_questions.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_item_pregunta, container, false));
    }

    @Override
    public void initFragment() {

        LinearLayout layout_preguntas = getView().findViewById(R.id.layout_preguntas);

        TextView text_pregunta = getView().findViewById(R.id.text_pregunta);
        text_pregunta.setText(question.getQuestion());
        TextView text_number = getView().findViewById(R.id.text_number_pregunta);
        text_number.setText(Html.fromHtml("Responder\n<b>pregunta " + (question.getPosition() + 1) +"/" + Fragment_preguntas.getViewPager().getAdapter().getCount() + "</b>"));

        int position = 0;
        for (QPAY_DynamicQuestions_answers que : question.getAnswers()) {
            que.setPosition(position++);
            LinearLayout item = (LinearLayout) getContext().getLayoutInflater().inflate(R.layout.view_pregunta, null);
            TextView title = item.findViewById(R.id.text_respuesta);
            title.setText(que.getAnswer());

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    response = new QPAY_CampaignAnswers_answers();
                    response.setAnswerId(que.getId());
                    response.setQuestionId(question.getId());
                    activarPregunta(que.getPosition());
                }
            });

            layout_preguntas.addView(item);
            items.add(item);
        }

        Button btn_siguiente = getView().findViewById(R.id.btn_siguiente);
        Button btn_omitir = getView().findViewById(R.id.btn_omitir);

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseQuestions(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        Fragment_preguntas.setPositionPager(question.getPosition());
                    }
                });
            }
        });

        btn_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_preguntas.setPositionPager(Fragment_preguntas.getViewPager().getAdapter().getCount());
            }
        });

    }

    public void activarPregunta(int position) {

        Typeface face_regular = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
        Typeface face_medium = ResourcesCompat.getFont(getContext(), R.font.roboto_medium);

        for (LinearLayout item: items) {
            TextView title = item.findViewById(R.id.text_respuesta);
            View view_focus = item.findViewById(R.id.view_focus);
            title.setTypeface(face_regular);
            view_focus.setVisibility(View.GONE);
        }

        TextView title = items.get(position).findViewById(R.id.text_respuesta);
        View view_focus = items.get(position).findViewById(R.id.view_focus);
        title.setTypeface(face_medium);
        view_focus.setVisibility(View.VISIBLE);

    }

    public void responseQuestions(IFunction function){

        getContext().loading(true);

        try {

            QPAY_CampaignAnswers campaignAnswers = new QPAY_CampaignAnswers();

            campaignAnswers.setId(question.getCampaignId());
            campaignAnswers.setAnswers(new QPAY_CampaignAnswers_answers[] { response });
            campaignAnswers.setQpay_mail(Fragment_registro_1.newUser.getQpay_mail());

            ICreateCampaignAnswers answers = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        QPAY_BaseResponse response = new Gson().fromJson(gson, QPAY_BaseResponse.class);

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(campaignAnswers));

            answers.createCampaignAnswer(campaignAnswers);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

}