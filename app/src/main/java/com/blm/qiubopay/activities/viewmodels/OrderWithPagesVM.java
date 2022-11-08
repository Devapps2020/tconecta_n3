package com.blm.qiubopay.activities.viewmodels;

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
import com.blm.qiubopay.models.order.OrderRequest;
import com.blm.qiubopay.models.order.OrderResponse;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderWithPagesVM extends ViewModel {

    private Context context;
    private Activity activity;
    private DataAccess dataAccess;
    private String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

    public OrderWithPagesVM(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        dataAccess = new DataAccess(context);
    }

    public MutableLiveData<BaseResponse<OrderResponse>> _orderResponse = new MutableLiveData<>();
    private void returnOrderEmpty() {
        _orderResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getOrders(String pageAfter, String pageBefore, String status, String phone) {
        OrderRequest orderRequest = new OrderRequest(seed, pageAfter, pageBefore, status, phone);
        dataAccess.getOrders(new Callback<BaseResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderResponse>> call, Response<BaseResponse<OrderResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _orderResponse.setValue(response.body());
                        else returnOrderEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnOrderEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _orderResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnOrderEmpty();
                    }
                } else returnOrderEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderResponse>> call, Throwable throwable) {
                returnOrderEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, orderRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(orderRequest));
    }

    public boolean validateSession(String code, String description, IFunction... function) {
        return MenuActivity.validaSesion(((HActivity)context), code, description, function);
    }

}