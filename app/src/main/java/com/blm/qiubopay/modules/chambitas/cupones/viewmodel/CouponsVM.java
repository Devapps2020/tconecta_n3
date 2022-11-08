package com.blm.qiubopay.modules.chambitas.cupones.viewmodel;

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
import com.blm.qiubopay.models.base.BimboIdBaseRequest;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.models.coupons.CouponDetailsRequest;
import com.blm.qiubopay.models.coupons.CouponsCountResponse;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsVM extends ViewModel {

    private Context context;
    private Activity activity;
    private DataAccess dataAccess;
    private String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

    public CouponsVM(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        dataAccess = new DataAccess(context);
    }

    public MutableLiveData<BaseResponse<CouponDetailsListResponse>> _couponsDetailsResponse = new MutableLiveData<>();

    private void returnCouponsDetailsEmpty() {
        _couponsDetailsResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCouponsListByUser() {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        BimboIdBaseRequest bimboIdBaseRequest = new BimboIdBaseRequest(seed, bimboId);
        dataAccess.getCouponsListByUser(new Callback<BaseResponse<CouponDetailsListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CouponDetailsListResponse>> call, Response<BaseResponse<CouponDetailsListResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponsDetailsResponse.setValue(response.body());
                        else returnCouponsDetailsEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponsDetailsEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponsDetailsResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponsDetailsEmpty();
                    }
                } else returnCouponsDetailsEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CouponDetailsListResponse>> call, Throwable throwable) {
                returnCouponsDetailsEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, bimboIdBaseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(bimboIdBaseRequest));
    }

    public MutableLiveData<BaseResponse<CouponDetailsListResponse>> _couponsAllDetailsResponse = new MutableLiveData<>();

    private void returnCouponAllDetailsEmpty() {
        _couponsAllDetailsResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCouponAllDetailsByUser() {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        BimboIdBaseRequest bimboIdBaseRequest = new BimboIdBaseRequest(seed, bimboId);
        dataAccess.getCouponAllDetailsByUser(new Callback<BaseResponse<CouponDetailsListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CouponDetailsListResponse>> call, Response<BaseResponse<CouponDetailsListResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponsAllDetailsResponse.setValue(response.body());
                        else returnCouponAllDetailsEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponAllDetailsEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponsAllDetailsResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponAllDetailsEmpty();
                    }
                } else returnCouponAllDetailsEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CouponDetailsListResponse>> call, Throwable throwable) {
                returnCouponAllDetailsEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, bimboIdBaseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(bimboIdBaseRequest));
    }

    public MutableLiveData<BaseResponse<CouponDetailsListResponse>> _couponDetailByUserResponse = new MutableLiveData<>();

    private void returnCouponDetailByUserEmpty() {
        _couponsDetailsResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCouponDetailByUser(String couponId) {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        CouponDetailsRequest couponDetailsRequest = new CouponDetailsRequest(seed, bimboId, couponId);
        dataAccess.getCouponDetailByUser(new Callback<BaseResponse<CouponDetailsListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CouponDetailsListResponse>> call, Response<BaseResponse<CouponDetailsListResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponDetailByUserResponse.setValue(response.body());
                        else returnCouponDetailByUserEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponDetailByUserEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponDetailByUserResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponDetailByUserEmpty();
                    }
                } else returnCouponDetailByUserEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CouponDetailsListResponse>> call, Throwable throwable) {
                returnCouponDetailByUserEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, couponDetailsRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(couponDetailsRequest));
    }

    public MutableLiveData<BaseResponse<Boolean>> _couponPrintedResponse = new MutableLiveData<>();

    private void returnCouponPrintedEmpty() {
        _couponPrintedResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void setStatusCouponPrinted(String couponId) {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        CouponDetailsRequest couponDetailsRequest = new CouponDetailsRequest(seed, bimboId, couponId);
        dataAccess.setStatusCouponPrinted(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponPrintedResponse.setValue(response.body());
                        else returnCouponPrintedEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponPrintedEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponPrintedResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponPrintedEmpty();
                    }
                } else returnCouponPrintedEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable throwable) {
                returnCouponPrintedEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, couponDetailsRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(couponDetailsRequest));
    }

    public MutableLiveData<BaseResponse<Boolean>> _couponsIsValidTerminalResponse = new MutableLiveData<>();

    private void returnCouponsIsValidTerminalEmpty() {
        _couponsIsValidTerminalResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void isValidTerminalforCoupons() {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        BimboIdBaseRequest bimboIdBaseRequest = new BimboIdBaseRequest(seed, bimboId);
        dataAccess.isValidTerminalforCoupons(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponsIsValidTerminalResponse.setValue(response.body());
                        else returnCouponsIsValidTerminalEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponsIsValidTerminalEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponsIsValidTerminalResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponsIsValidTerminalEmpty();
                    }
                } else returnCouponsIsValidTerminalEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable throwable) {
                returnCouponsIsValidTerminalEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, bimboIdBaseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(bimboIdBaseRequest));
    }

    public MutableLiveData<BaseResponse<CouponsCountResponse>> _couponsCountResponse = new MutableLiveData<>();

    private void returnCouponsCountEmpty() {
        _couponsCountResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getCountCouponsbyUser() {
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        BimboIdBaseRequest bimboIdBaseRequest = new BimboIdBaseRequest(seed, bimboId);
        dataAccess.getCountCouponsbyUser(new Callback<BaseResponse<CouponsCountResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CouponsCountResponse>> call, Response<BaseResponse<CouponsCountResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _couponsCountResponse.setValue(response.body());
                        else returnCouponsCountEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnCouponsCountEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _couponsCountResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnCouponsCountEmpty();
                    }
                } else returnCouponsCountEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<CouponsCountResponse>> call, Throwable throwable) {
                returnCouponsCountEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, bimboIdBaseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(bimboIdBaseRequest));
    }

    public boolean validateSession(String code, String description, IFunction... function) {
        return MenuActivity.validaSesion(((HActivity)context), code, description, function);
    }

}
