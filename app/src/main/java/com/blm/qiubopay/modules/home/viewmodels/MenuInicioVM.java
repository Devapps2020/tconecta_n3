package com.blm.qiubopay.modules.home.viewmodels;

import static com.blm.qiubopay.utils.Globals.REQUEST_LOG;
import static com.blm.qiubopay.utils.Globals.RESPONSE_LOG;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.DataAccess;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.base.BaseRequest;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.carousel.CarouselCampaignsRequest;
import com.blm.qiubopay.models.carousel.QPayBaseResponse;
import com.blm.qiubopay.models.chambitas.campañas.CampaignsActiveNewCount;
import com.blm.qiubopay.models.ondemand.OnDemandRequest;
import com.blm.qiubopay.models.publicity.CampaignsActiveCount;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuInicioVM extends ViewModel {

    private Context context;
    private Activity activity;
    private DataAccess dataAccess;
    private String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

    public MenuInicioVM(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        dataAccess = new DataAccess(context);
    }

    public MutableLiveData<QPayBaseResponse> _carouselResponse = new MutableLiveData<>();
    private void returnCarrouselEmpty() {
        _carouselResponse.setValue(new QPayBaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCarrouselCampaigns() {
        CarouselCampaignsRequest carouselCampaignsRequest = new CarouselCampaignsRequest(seed);
        dataAccess.getCarrouselCampaigns(new Callback<QPayBaseResponse>() {
            @Override
            public void onResponse(Call<QPayBaseResponse> call, Response<QPayBaseResponse> response) {
                //TODO TRANSACTION COUNTER. OTHERS TRANSACTION SUM SUCCESSFUL
                TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                transactionsModel.getRsTransactions().setExitosos(transactionsModel.getRsTransactions().getExitosos() + 1);
                AppPreferences.setTodayTransactions(transactionsModel);
                AppPreferences.savePublicity(response.body());
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayObject() != null) {
                        if (response.body().getQpayResponse().equals("true")) {
                            if (response.body().getQpayObject() != null) _carouselResponse.setValue(response.body());
                            else returnCarrouselEmpty();
                        } else {
                            if (response.body().getQpayCode().equals("002")) {
                                returnCarrouselEmpty();
                            } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                                _carouselResponse.setValue(new QPayBaseResponse(response.body().getQpayResponse(),
                                        response.body().getQpayCode(), response.body().getQpayDescription(), null));
                            } else returnCarrouselEmpty();
                        }
                    } else returnCarrouselEmpty();
                } else returnCarrouselEmpty();
            }

            @Override
            public void onFailure(Call<QPayBaseResponse> call, Throwable throwable) {
                //TODO TRANSACTION COUNTER. OTHERS TRANSACTION SUM UNSUCCESSFUL
                TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                transactionsModel.getRsTransactions().setNoExitosos(transactionsModel.getRsTransactions().getNoExitosos() + 1);
                AppPreferences.setTodayTransactions(transactionsModel);
                returnCarrouselEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, carouselCampaignsRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(carouselCampaignsRequest));
    }

    public MutableLiveData<BaseResponse<CampaignsActiveNewCount>> _chambitasResponse = new MutableLiveData<>();
    public LiveData<BaseResponse<CampaignsActiveNewCount>> chambitasResponse = _chambitasResponse;
    private void returnChambitasEmpty() {
        _chambitasResponse.setValue(new BaseResponse<CampaignsActiveNewCount>("", "", "", new ArrayList<>()));
    }

    public void getChambitasCount() {
        BaseRequest baseRequest = new BaseRequest(seed);
        dataAccess.getCampaignsActiveNewCount(new Callback<BaseResponse<CampaignsActiveNewCount>>() {
            @Override
            public void onResponse(Call<BaseResponse<CampaignsActiveNewCount>> call, Response<BaseResponse<CampaignsActiveNewCount>> response) {
                if (response.body() != null) {
                    if (response.body().getQpayObject() != null) {
                        Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                        if (response.body().getQpayResponse().equals("true")) {
                            if (response.body().getQpayObject() != null) _chambitasResponse.setValue(response.body());
                            else returnCarrouselEmpty();
                        } else {
                            if (response.body().getQpayCode().equals("002")) {
                                returnCarrouselEmpty();
                            } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                                _chambitasResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                        response.body().getQpayCode(), response.body().getQpayDescription(), null));
                            } else returnCarrouselEmpty();
                        }
                    } else returnChambitasEmpty();
                } else returnChambitasEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CampaignsActiveNewCount>> call, Throwable throwable) {
                returnChambitasEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, baseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(baseRequest));
    }

    public MutableLiveData<BaseResponse<CampaignsActiveCount>> _publicityResponse = new MutableLiveData<>();
    public LiveData<BaseResponse<CampaignsActiveCount>> publicityResponse = _publicityResponse;
    private void returnPublicidadEmpty() {
        _publicityResponse.setValue(new BaseResponse<>("", "", "", new ArrayList<>()));
    }

    // Publicidad
    public void getPublicidadCount() {
        BaseRequest baseRequest = new BaseRequest(seed);
        dataAccess.getCampaignsActiveCount(new Callback<BaseResponse<CampaignsActiveCount>>() {
            @Override
            public void onResponse(Call<BaseResponse<CampaignsActiveCount>> call, Response<BaseResponse<CampaignsActiveCount>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayObject() != null) {
                        if (response.body().getQpayResponse().equals("true")) {
                            if (response.body().getQpayObject() != null) _publicityResponse.setValue(response.body());
                            else returnCarrouselEmpty();
                        } else {
                            if (response.body().getQpayCode().equals("002")) {
                                returnCarrouselEmpty();
                            } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                                _publicityResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                        response.body().getQpayCode(), response.body().getQpayDescription(), null));
                            } else returnCarrouselEmpty();
                        }
                    } else returnPublicidadEmpty();
                } else returnPublicidadEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CampaignsActiveCount>> call, Throwable throwable) {
                returnPublicidadEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, baseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(baseRequest));
    }

    public MutableLiveData<QPayBaseResponse> _campaingnResponse = new MutableLiveData<>();
    private void returnCampaignEmpty() {
        _campaingnResponse.setValue(new QPayBaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCampaignById(String campaignId) {
        OnDemandRequest onDemandRequest = new OnDemandRequest(campaignId);
        dataAccess.getCampaignById(new Callback<QPayBaseResponse>() {
            @Override
            public void onResponse(Call<QPayBaseResponse> call, Response<QPayBaseResponse> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayObject() != null) {
                        if (response.body().getQpayResponse().equals("true")) {
                            if (response.body().getQpayObject() != null) _campaingnResponse.setValue(response.body());
                            else returnCarrouselEmpty();
                        } else {
                            if (response.body().getQpayCode().equals("002")) {
                                returnCarrouselEmpty();
                            } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                                _campaingnResponse.setValue(new QPayBaseResponse(response.body().getQpayResponse(),
                                        response.body().getQpayCode(), response.body().getQpayDescription(), null));
                            } else returnCarrouselEmpty();
                        }
                    } else returnCampaignEmpty();
                } else returnCampaignEmpty();
            }

            @Override
            public void onFailure(Call<QPayBaseResponse> call, Throwable throwable) {
                returnCampaignEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, onDemandRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(onDemandRequest));
    }

    public boolean validateSession(String code, String description, IFunction... function) {
        return MenuActivity.validaSesion(((HActivity)context), code, description, function);
    }

}