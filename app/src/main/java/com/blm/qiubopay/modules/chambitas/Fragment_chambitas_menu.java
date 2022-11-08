package com.blm.qiubopay.modules.chambitas;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetActiveCampaignNew;
import com.blm.qiubopay.listeners.IGetCampaignActive;
import com.blm.qiubopay.listeners.IGetCampaignDone;
import com.blm.qiubopay.listeners.IGetCampaignProgress;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignNew;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignProgress;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_CampaignDone;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_CampaignDoneResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_CampaignNewResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_CampaignProgressResponse;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_chambitas_menu extends HFragment implements IMenuContext {


    public static Fragment_chambitas_menu newInstance() {
        return new Fragment_chambitas_menu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_1, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().initHome();
            }
        });

        CardView card_new_challenge = getView().findViewById(R.id.card_new_challenge);
        CardView card_challenge_active = getView().findViewById(R.id.card_challenge_active);
        CardView card_challenge_compliment = getView().findViewById(R.id.card_challenge_compliment);

        TextView tv_bg_cb_nuevas = getView().findViewById(R.id.tv_bg_cb_nuevas);
        TextView tv_bg_cb_activas = getView().findViewById(R.id.tv_bg_cb_activas);
        TextView tv_bg_cb_cumplidas = getView().findViewById(R.id.tv_bg_cb_cumplidas);

        TextView tv_title_cb_nuevas = getView().findViewById(R.id.tv_title_cb_nuevas);
        TextView tv_title_cb_activas = getView().findViewById(R.id.tv_title_cb_activas);
        TextView tv_title_cb_completadas = getView().findViewById(R.id.tv_title_cb_completadas);


        tv_bg_cb_nuevas.setVisibility( getStatusActive() ? View.VISIBLE : View.GONE);
        tv_bg_cb_activas.setVisibility( getStatusCurrent()  ?  View.VISIBLE : View.GONE);
        tv_bg_cb_cumplidas.setVisibility( getStatusCompleted() ? View.VISIBLE : View.GONE);

        tv_bg_cb_nuevas.setText( getStatusActive()  ? AppPreferences.getChambitasCounter() : "");
        tv_bg_cb_activas.setText( getStatusCurrent() ? AppPreferences.getChambitasCurrentCounter() : "");
        tv_bg_cb_cumplidas.setText( getStatusCompleted()  ? AppPreferences.getChambitasCompletedCounter() : "");


        tv_title_cb_nuevas.setText(setTextRow(0));
        tv_title_cb_activas.setText(setTextRow(1));
        tv_title_cb_completadas.setText(setTextRow(2));


        card_new_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getNewChambitas(new IFunction<QPAY_ActiveCampaignResponse>() {
                    @Override
                    public void execute(QPAY_ActiveCampaignResponse... data) {
                        Fragment_chambitas_campania.type = 1;
                        Fragment_chambitas_campania.list = Arrays.asList(data[0].getQpay_object());

                        if(Fragment_chambitas_campania.list == null && Fragment_chambitas_campania.list.isEmpty()) {
                            getContext().alert("No existen campañas nuevas");
                            return;
                        }

                        //2021-12-14 RSB. Al entrar a chambitas nuevas activa para consultar al regresar al home la consulta
                        //getContextMenu().setCountChambitas(true);

                        getContext().setFragment(Fragment_chambitas_campania.newInstance());
                    }
                });


            }
        });

        card_challenge_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgressChambitas(new IFunction<QPAY_ActiveCampaignResponse>() {
                    @Override
                    public void execute(QPAY_ActiveCampaignResponse... data) {
                        Fragment_chambitas_campania.type = 2;
                        Fragment_chambitas_campania.list = Arrays.asList(data[0].getQpay_object());

                        if(Fragment_chambitas_campania.list == null && Fragment_chambitas_campania.list.isEmpty()) {
                            getContext().alert("No existen campañas activas");
                            return;
                        }

                        getContext().setFragment(Fragment_chambitas_campania.newInstance());
                    }
                });

            }
        });

        card_challenge_compliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChambitasDone(new IFunction<QPAY_ActiveCampaignResponse>() {
                    @Override
                    public void execute(QPAY_ActiveCampaignResponse... data) {
                        Fragment_chambitas_campania.type = 3;
                        Fragment_chambitas_campania.list = Arrays.asList(data[0].getQpay_object());

                        if(Fragment_chambitas_campania.list == null && Fragment_chambitas_campania.list.isEmpty()) {
                            getContext().alert("No existen campañas cumplidas");
                            return;
                        }

                        getContext().setFragment(Fragment_chambitas_campania.newInstance());
                    }
                });

            }
        });

    }

    private boolean getStatusActive(){
        return !(AppPreferences.getChambitasCounter().isEmpty() || AppPreferences.getChambitasCounter().equals("0"));
    }

    private boolean getStatusCurrent(){
        return !(AppPreferences.getChambitasCurrentCounter().isEmpty() || AppPreferences.getChambitasCurrentCounter().equals("0"));
    }

    private boolean getStatusCompleted(){
        return !(AppPreferences.getChambitasCompletedCounter().isEmpty() || AppPreferences.getChambitasCompletedCounter().equals("0"));
    }
    
    private String setTextRow(Integer typeCampaign){
        String text = "";
        switch (typeCampaign){
            case 0: // Chambitas Nuevas
                Boolean chambitasCounter = AppPreferences.getChambitasCounter().equals("1");
                 text = getStatusActive() ? ("Tienes " + AppPreferences.getChambitasCounter() + (!AppPreferences.getChambitasCounter().equals("1") ? " chambitas" : " chambita")  +(!chambitasCounter ? " próximas" : " próxima")  +" a caducar") :  getResources().getString(R.string.text_chambitas_16);
                 break;
            case 1: // Chambitas Activas
                Boolean chambitasCurrentCounter = AppPreferences.getChambitasCurrentCounter().equals("1");
                text = getStatusCurrent() ? "Termina " + (!AppPreferences.getChambitasCurrentCounter().equals("1") ? "las " : "la") + (!chambitasCurrentCounter  ? AppPreferences.getChambitasCurrentCounter() + " chambitas" : " chambita")   +  (!chambitasCurrentCounter ? " que te falten y obten tu premio" : " que te falta y obten tu premio") :  getResources().getString(R.string.text_chambitas_17);
                 break;
            case 2: // Chambitas Cumplidas
                 text = getStatusCompleted() ? getResources().getString(R.string.text_chambitas_23) :  getResources().getString(R.string.text_chambitas_18);
                 break;
        }
        return text;
    }


    public void getNewChambitas(IFunction<QPAY_ActiveCampaignResponse> function){

        getContext().loading(true);

        try {

            QPAY_ActiveCampaignNew data = new QPAY_ActiveCampaignNew();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetActiveCampaignNew petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_ActiveCampaignResponse response = new Gson().fromJson(json, QPAY_ActiveCampaignResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getActiveCampaignNew(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getProgressChambitas(IFunction<QPAY_ActiveCampaignResponse> function){

        getContext().loading(true);

        try {

            QPAY_ActiveCampaignProgress data = new QPAY_ActiveCampaignProgress();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetCampaignProgress petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_ActiveCampaignResponse response = new Gson().fromJson(json, QPAY_ActiveCampaignResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getCampaignProgress(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getChambitasDone(IFunction<QPAY_ActiveCampaignResponse> function){

        getContext().loading(true);

        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -7);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            QPAY_CampaignDone data = new QPAY_CampaignDone();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setMin_date(format.format(cal.getTime()));

            IGetCampaignDone petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_ActiveCampaignResponse response = new Gson().fromJson(json, QPAY_ActiveCampaignResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getCampaignDone(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}