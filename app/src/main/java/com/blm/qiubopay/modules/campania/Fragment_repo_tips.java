package com.blm.qiubopay.modules.campania;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.PublicityReportAdapter;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetTipsAdvertising;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IViewTipsAdvertising;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertisingResponse;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_object;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;
import com.blm.qiubopay.models.questions.QPAY_CampaignAnswers;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_repo_tips#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_repo_tips extends HFragment implements IMenuContext {

    private static final String TAG = "reporte_repo_tips";

    private RecyclerView publicityRecyclerView;

    private PublicityReportAdapter reportAdapter;

    private List<QPAY_TipsAdvertising_publicities> listPublicities;

    private QPAY_TipsAdvertisingResponse responseTips;

    public static Fragment_repo_tips newInstance() {
        return new Fragment_repo_tips();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_repo_tips, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().initHome();
                    }
                });

        publicityRecyclerView = getView().findViewById(R.id.publicity_recyclerview);
        listPublicities = new ArrayList<QPAY_TipsAdvertising_publicities>();

        getListTipsAdvertising();

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    private void setReportAdapter(){

        reportAdapter = new PublicityReportAdapter(new PublicityReportAdapter.ListPublicityClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {
                QPAY_TipsAdvertising_publicities item = listPublicities.get(clickedItemIndex);

                Fragment_tips.setContinue(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().backFragment();
                    }
                });

                QPAY_TipsAdvertising_object campaignNew;
                //Se busca la campaña que le corresponde a la publicidad para reutilizar fragment_tips
                for(QPAY_TipsAdvertising_object campaign : responseTips.getQpay_object()){
                    for(QPAY_TipsAdvertising_publicities publicity : campaign.getPublicities()){
                        if(item.getId()==publicity.getId()){
                            //Creamos una campaña y asignamos valores
                            campaignNew = new QPAY_TipsAdvertising_object();
                            campaignNew.setId(campaign.getId());
                            campaignNew.setName(campaign.getName());
                            campaignNew.setActivation_date(campaign.getActivation_date());
                            campaignNew.setExpiration_date(campaign.getExpiration_date());
                            campaignNew.setStatus(campaign.getStatus());
                            //Se coloca solo esa publicidad dentro de la campaña
                            QPAY_TipsAdvertising_publicities publicities[] = {publicity};
                            campaignNew.setPublicities(publicities);
                            //Se asigna la campaña
                            Fragment_tips.setData(campaignNew);
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    tipsAdvertisingViewed(campaign.getId());
                                }
                            };
                            runnable.run();
                            getContext().setFragment(Fragment_tips.newInstance());
                            break;
                        }
                    }
                }

            }
        });
        reportAdapter.setData(listPublicities);
        publicityRecyclerView.setAdapter(reportAdapter);
        publicityRecyclerView.setHasFixedSize(true);
        publicityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    public void getListTipsAdvertising() {

        getContext().loading(true);

        QPAY_Seed qpay_seed = new QPAY_Seed();
        qpay_seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        try {

            IGetTipsAdvertising advertising = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result);

                    if (result instanceof ErrorResponse) {
                        alertWithoutInfo(R.string.general_error);
                    } else {

                        responseTips = new Gson().fromJson(gson, QPAY_TipsAdvertisingResponse.class);

                        if("000".equals(responseTips.getQpay_code())) {

                            if(responseTips.getQpay_object()[0] == null || responseTips.getQpay_object()[0].getPublicities() == null || responseTips.getQpay_object()[0].getPublicities().length == 0) {
                                alertWithoutInfo(R.string.text_repo_tips_no_info);
                            } else {

                                for(QPAY_TipsAdvertising_object campaign : responseTips.getQpay_object()){
                                    for(QPAY_TipsAdvertising_publicities publicity : campaign.getPublicities()){
                                        listPublicities.add(publicity);
                                    }
                                }

                                if(listPublicities.size()>0){
                                    setReportAdapter();
                                }

                            }

                        } else {
                            alertWithoutInfo(R.string.text_repo_tips_no_info);
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    alertWithoutInfo(R.string.general_error);
                }

            }, getContext());

            advertising.getAllTipsAdvertising(qpay_seed);

        } catch (Exception e) {
            Logger.e(e.getMessage());
            getContext().loading(false);
            alertWithoutInfo(R.string.general_error_catch);
        }

    }

    private void alertWithoutInfo(int message){
        getContext().alert(message, new IAlertButton() {
            @Override
            public String onText() {
                return "Aceptar";
            }

            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });
    }

    public void tipsAdvertisingViewed(int id){
        enableGetCountPublicity();
        try {
            QPAY_CampaignAnswers campaignAnswers = new QPAY_CampaignAnswers();
            campaignAnswers.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            campaignAnswers.setId(id);
            campaignAnswers.setSkipped(false);
            IViewTipsAdvertising answers = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    Log.d("Fragment", result.toString());
                }

                @Override
                public void onConnectionFailed(Object result) {

                }
            }, getContext());
            Log.e("request", "" + new Gson().toJson(campaignAnswers));
            answers.viewTipsAdvertising(campaignAnswers);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void enableGetCountPublicity(){
        try{
            //2021-12-16 RSB. Se habilita la consulta de publicidad para el badge
            ((MenuActivity)getContext()).setCountPublicity(true);
        } catch (Exception e){
            Logger.e(e.getMessage());
        }
    }

}