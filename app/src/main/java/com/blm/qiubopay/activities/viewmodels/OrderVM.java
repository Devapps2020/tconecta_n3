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
import com.blm.qiubopay.models.base.BaseRequest;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.order.AcceptOrderRequest;
import com.blm.qiubopay.models.order.AcceptOrderResponse;
import com.blm.qiubopay.models.order.DeclineOrderRequest;
import com.blm.qiubopay.models.order.DeclineOrderResponse;
import com.blm.qiubopay.models.order.GetOrderAcceptanceResponse;
import com.blm.qiubopay.models.order.OrderDetailsRequest;
import com.blm.qiubopay.models.order.OrderDetailsResponse;
import com.blm.qiubopay.models.order.OrderRequest;
import com.blm.qiubopay.models.order.OrderResponse;
import com.blm.qiubopay.models.order.ProductsAcceptModel;
import com.blm.qiubopay.models.order.SetOrderAcceptanceRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderVM extends ViewModel {

    private Context context;
    private Activity activity;
    private DataAccess dataAccess;
    private String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

    public OrderVM(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        dataAccess = new DataAccess(context);
    }

    public MutableLiveData<BaseResponse<OrderResponse>> _orderFilteredResponse = new MutableLiveData<>();
    private void returnOrderFilteredEmpty() {
        _orderFilteredResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getFilteredOrders(String pageAfter, String pageBefore, String status, String phone) {
        OrderRequest orderRequest = new OrderRequest(seed, pageAfter, pageBefore, status, phone);
        dataAccess.getOrders(new Callback<BaseResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderResponse>> call, Response<BaseResponse<OrderResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _orderFilteredResponse.setValue(response.body());
                        else returnOrderFilteredEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnOrderFilteredEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _orderFilteredResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnOrderFilteredEmpty();
                    }
                } else returnOrderFilteredEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderResponse>> call, Throwable throwable) {
                returnOrderFilteredEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, orderRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(orderRequest));
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

    public MutableLiveData<BaseResponse<OrderDetailsResponse>> _orderDetailsResponse = new MutableLiveData<>();
    private void returnOrderDetailsEmpty() {
        _orderDetailsResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
    }

    public void getOrderDetails(String orderId) {
        OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest(seed, orderId);
        dataAccess.getOrderDetails(new Callback<BaseResponse<OrderDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderDetailsResponse>> call, Response<BaseResponse<OrderDetailsResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _orderDetailsResponse.setValue(response.body());
                        else returnOrderDetailsEmpty();
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnOrderDetailsEmpty();
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _orderDetailsResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnOrderDetailsEmpty();
                    }
                } else returnOrderDetailsEmpty();
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderDetailsResponse>> call, Throwable throwable) {
                returnOrderDetailsEmpty();
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, orderDetailsRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(orderDetailsRequest));
    }

    public MutableLiveData<BaseResponse<AcceptOrderResponse>> _acceptOrderResponse = new MutableLiveData<>();
    private void returnAcceptOrderEmpty(String qpayDescription) {
        if (qpayDescription != null && !qpayDescription.isEmpty()) {
            _acceptOrderResponse.setValue(new BaseResponse("", "", qpayDescription, new ArrayList<>()));
        } else {
            _acceptOrderResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
        }
    }

    public void acceptOrder(String orderId, ArrayList<ProductsAcceptModel> productsAcceptModels, Double totalAmount, Integer time) {
        AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest(seed, orderId, productsAcceptModels, totalAmount, time);
        dataAccess.acceptOrder(new Callback<BaseResponse<AcceptOrderResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<AcceptOrderResponse>> call, Response<BaseResponse<AcceptOrderResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _acceptOrderResponse.setValue(response.body());
                        else returnAcceptOrderEmpty(response.body().getQpayDescription());
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnAcceptOrderEmpty(response.body().getQpayDescription());
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _acceptOrderResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnAcceptOrderEmpty("");
                    }
                } else returnAcceptOrderEmpty("");
            }

            @Override
            public void onFailure(Call<BaseResponse<AcceptOrderResponse>> call, Throwable throwable) {
                returnAcceptOrderEmpty("");
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, acceptOrderRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(acceptOrderRequest));
    }

    public MutableLiveData<BaseResponse<DeclineOrderResponse>> _declineOrderResponse = new MutableLiveData<>();
    private void returnDeclineOrderEmpty(String qpayDescription) {
        if (qpayDescription != null && !qpayDescription.isEmpty()) {
            _declineOrderResponse.setValue(new BaseResponse("", "", qpayDescription, new ArrayList<>()));
        } else {
            _declineOrderResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
        }
    }

    public void declineOrder(String orderId) {
        DeclineOrderRequest declineOrderRequest = new DeclineOrderRequest(seed, orderId);
        dataAccess.declineOrder(new Callback<BaseResponse<DeclineOrderResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DeclineOrderResponse>> call, Response<BaseResponse<DeclineOrderResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _declineOrderResponse.setValue(response.body());
                        else returnDeclineOrderEmpty(response.body().getQpayDescription());
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnDeclineOrderEmpty(response.body().getQpayDescription());
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _declineOrderResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnDeclineOrderEmpty("");
                    }
                } else returnDeclineOrderEmpty("");
            }

            @Override
            public void onFailure(Call<BaseResponse<DeclineOrderResponse>> call, Throwable throwable) {
                returnDeclineOrderEmpty("");
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, declineOrderRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(declineOrderRequest));
    }

    public MutableLiveData<BaseResponse<GetOrderAcceptanceResponse>> _getOrderAcceptanceResponse = new MutableLiveData<>();
    private void returnGetOrderAcceptanceEmpty(String qpayDescription) {
        if (qpayDescription != null && !qpayDescription.isEmpty()) {
            _getOrderAcceptanceResponse.setValue(new BaseResponse("", "", qpayDescription, new ArrayList<>()));
        } else {
            _getOrderAcceptanceResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
        }
    }

    public void getOrderAcceptance() {
        BaseRequest baseRequest = new BaseRequest(seed);
        dataAccess.getOrderAcceptance(new Callback<BaseResponse<GetOrderAcceptanceResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<GetOrderAcceptanceResponse>> call, Response<BaseResponse<GetOrderAcceptanceResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _getOrderAcceptanceResponse.setValue(response.body());
                        else returnDeclineOrderEmpty(response.body().getQpayDescription());
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnGetOrderAcceptanceEmpty(response.body().getQpayDescription());
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _getOrderAcceptanceResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnDeclineOrderEmpty("");
                    }
                } else returnDeclineOrderEmpty("");
            }

            @Override
            public void onFailure(Call<BaseResponse<GetOrderAcceptanceResponse>> call, Throwable throwable) {
                returnGetOrderAcceptanceEmpty("");
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, baseRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(baseRequest));
    }

    public MutableLiveData<BaseResponse<String>> _setOrderAcceptanceResponse = new MutableLiveData<>();
    private void returnSetOrderAcceptanceEmpty(String qpayDescription) {
        if (qpayDescription != null && !qpayDescription.isEmpty()) {
            _setOrderAcceptanceResponse.setValue(new BaseResponse("", "", qpayDescription, new ArrayList<>()));
        } else {
            _setOrderAcceptanceResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
        }
    }

    public void setOrderAcceptance(String acceptB2cOrders, String b2cLat, String b2cLon) {
        SetOrderAcceptanceRequest setOrderAcceptanceRequest = new SetOrderAcceptanceRequest(seed, acceptB2cOrders, b2cLat, b2cLon);
        dataAccess.setOrderAcceptance(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        _setOrderAcceptanceResponse.setValue(response.body());
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnSetOrderAcceptanceEmpty(response.body().getQpayDescription());
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _setOrderAcceptanceResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnDeclineOrderEmpty("");
                    }
                } else returnDeclineOrderEmpty("");
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                returnSetOrderAcceptanceEmpty("");
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, setOrderAcceptanceRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(setOrderAcceptanceRequest));
    }

    public MutableLiveData<BaseResponse<AcceptOrderResponse>> _deliverOrderResponse = new MutableLiveData<>();
    private void returnDeliverOrderEmpty(String qpayDescription) {
        if (qpayDescription != null && !qpayDescription.isEmpty()) {
            _deliverOrderResponse.setValue(new BaseResponse("", "", qpayDescription, new ArrayList<>()));
        } else {
            _deliverOrderResponse.setValue(new BaseResponse("", "", "", new ArrayList<>()));
        }
    }

    public void deliverOrder(String orderId) {
        DeclineOrderRequest declineOrderRequest = new DeclineOrderRequest(seed, orderId);
        dataAccess.deliverOrder(new Callback<BaseResponse<AcceptOrderResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<AcceptOrderResponse>> call, Response<BaseResponse<AcceptOrderResponse>> response) {
                if (response.body() != null) {
                    Logger.d(RESPONSE_LOG + new Gson().toJson(response.body()));
                    if (response.body().getQpayResponse().equals("true")) {
                        if (response.body().getQpayObject() != null) _deliverOrderResponse.setValue(response.body());
                        else returnDeliverOrderEmpty(response.body().getQpayDescription());
                    } else {
                        if (response.body().getQpayCode().equals("002")) {
                            returnDeliverOrderEmpty(response.body().getQpayDescription());
                        } else if (!validateSession(response.body().getQpayCode(), response.body().getQpayDescription())) {
                            _deliverOrderResponse.setValue(new BaseResponse(response.body().getQpayResponse(),
                                    response.body().getQpayCode(), response.body().getQpayDescription(), null));
                        } else returnDeliverOrderEmpty("");
                    }
                } else returnDeliverOrderEmpty("");
            }

            @Override
            public void onFailure(Call<BaseResponse<AcceptOrderResponse>> call, Throwable throwable) {
                returnDeliverOrderEmpty("");
                Logger.d(RESPONSE_LOG + throwable.getMessage());
            }
        }, declineOrderRequest);
        Logger.d(REQUEST_LOG + new Gson().toJson(declineOrderRequest));
    }

    public boolean validateSession(String code, String description, IFunction... function) {
        return MenuActivity.validaSesion(((HActivity)context), code, description, function);
    }

}