package com.blm.qiubopay.fragments.viewmodels;

import static com.blm.qiubopay.utils.Globals.REQUEST_LOG;
import static com.blm.qiubopay.utils.Globals.RESPONSE_LOG;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.DataAccess;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.questions.CampaignViewedRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenVM extends ViewModel {

    private Context context;
    private Activity activity;
    private DataAccess dataAccess;
    private String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

    public FullScreenVM(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        dataAccess = new DataAccess(context);
    }

    public MutableLiveData<BaseResponse<String>> _createCampaignResponse = new MutableLiveData<>();

    private void returnCreateCampaignEmpty() {
        _createCampaignResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void createCampaignViewed(Integer id) {
        CampaignViewedRequest campaignViewedRequest = new CampaignViewedRequest(id, seed, null, false);
        dataAccess.createCampaignViewed(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayObject() != null) {
                        if (response.body().getQpayResponse().equals("true")) {
                            if (response.body().getQpayObject() != null) _createCampaignResponse.setValue(response.body());
                            else returnCreateCampaignEmpty();
                        } else {
                            if (response.body().getQpayCode().equals("002")) {
                                returnCreateCampaignEmpty();
                            } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                                _createCampaignResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                        response.body().getQpayCode(), response.body().getQpayDescription(), null));
                            } else returnCreateCampaignEmpty();
                        }
                    } else returnCreateCampaignEmpty();
                } else returnCreateCampaignEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                returnCreateCampaignEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, campaignViewedRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(campaignViewedRequest));
    }

    public boolean validateSession(String code, String description, IFunction... function) {
        return MenuActivity.validaSesion(((HActivity)context), code, description, function);
    }

}