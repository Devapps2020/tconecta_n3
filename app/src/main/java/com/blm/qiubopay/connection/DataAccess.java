package com.blm.qiubopay.connection;

import static com.blm.qiubopay.utils.Globals.HOST;
import static com.blm.qiubopay.utils.Globals.HOST_RS;

import android.content.Context;

import com.blm.qiubopay.models.base.BaseRequest;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.base.BimboIdBaseRequest;
import com.blm.qiubopay.models.chambitas.campañas.CampaignsActiveNewCount;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.models.coupons.CouponDetailsRequest;
import com.blm.qiubopay.models.coupons.CouponsCountResponse;
import com.blm.qiubopay.models.ondemand.OnDemandRequest;
import com.blm.qiubopay.models.order.AcceptOrderRequest;
import com.blm.qiubopay.models.order.AcceptOrderResponse;
import com.blm.qiubopay.models.order.DeclineOrderRequest;
import com.blm.qiubopay.models.order.DeclineOrderResponse;
import com.blm.qiubopay.models.order.GetOrderAcceptanceResponse;
import com.blm.qiubopay.models.order.OrderDetailsRequest;
import com.blm.qiubopay.models.order.OrderDetailsResponse;
import com.blm.qiubopay.models.order.OrderRequest;
import com.blm.qiubopay.models.order.OrderResponse;
import com.blm.qiubopay.models.order.SetOrderAcceptanceRequest;
import com.blm.qiubopay.models.publicity.CampaignsActiveCount;
import com.blm.qiubopay.models.questions.CampaignViewedRequest;
import com.blm.qiubopay.models.carousel.CarouselCampaignsRequest;
import com.blm.qiubopay.models.carousel.QPayBaseResponse;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DataAccessible {

    void getCarrouselCampaigns(Callback<QPayBaseResponse> callback, @Body CarouselCampaignsRequest seed);

    void getCampaignActive(Callback<QPayBaseResponse> callback, @Body CarouselCampaignsRequest seed);

    void getCampaignsActiveNewCount(Callback<BaseResponse<CampaignsActiveNewCount>> callback, @Body BaseRequest seed);

    void getCampaignsActiveCount(Callback<BaseResponse<CampaignsActiveCount>> callback, @Body BaseRequest seed);

    void getCampaignById(Callback<QPayBaseResponse> callback, @Body OnDemandRequest campaignId);

    void createCampaignViewed(Callback<BaseResponse<String>> callback, @Body CampaignViewedRequest campaignViewedRequest);

}

interface DataAccessibleQTC {

    void getOrders(Callback<BaseResponse<OrderResponse>> callback, @Body OrderRequest orderRequest);

    void getOrderDetails(Callback<BaseResponse<OrderDetailsResponse>> callback, @Body OrderDetailsRequest orderDetailsRequest);

    void acceptOrder(Callback<BaseResponse<AcceptOrderResponse>> callback, @Body AcceptOrderRequest acceptOrderRequest);

    void declineOrder(Callback<BaseResponse<DeclineOrderResponse>> callback, @Body DeclineOrderRequest declineOrderRequest);

    void getOrderAcceptance(Callback<BaseResponse<GetOrderAcceptanceResponse>> callback, @Body BaseRequest baseRequest);

    void setOrderAcceptance(Callback<BaseResponse<String>> callback, @Body SetOrderAcceptanceRequest setOrderAcceptanceRequest);

    void deliverOrder(Callback<BaseResponse<AcceptOrderResponse>> callback, @Body DeclineOrderRequest declineOrderRequest);

    void getCouponsListByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, @Body BimboIdBaseRequest bimboIdBaseRequest);

    void getCouponAllDetailsByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, @Body BimboIdBaseRequest bimboIdBaseRequest);

    void getCouponDetailByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, @Body CouponDetailsRequest couponDetailsRequest);

    void setStatusCouponPrinted(Callback<BaseResponse<Boolean>> callback, @Body CouponDetailsRequest couponDetailsRequest);

    void isValidTerminalforCoupons(Callback<BaseResponse<Boolean>> callback, @Body BimboIdBaseRequest bimboIdBaseRequest);

    void getCountCouponsbyUser(Callback<BaseResponse<CouponsCountResponse>> callback, @Body BimboIdBaseRequest bimboIdBaseRequest);

}

interface RetrofitDataAccesible {

    @POST("api/v1/tp/getCarrouselCampaigns")
    Call<QPayBaseResponse> getCarrouselCampaigns(@Body CarouselCampaignsRequest seed);

    @POST("api/v1/tp/getCampaignActive")
    Call<QPayBaseResponse> getCampaignActive(@Body CarouselCampaignsRequest seed);

    //Chambitas
    @POST("api/v1/ch/getAllCampaignsActiveNewCount")
    Call<BaseResponse<CampaignsActiveNewCount>> getCampaignsActiveNewCount(@Body BaseRequest seed);

    // Publicidad
    @POST("api/v1/tp/getCampaignsActiveCount")
    Call<BaseResponse<CampaignsActiveCount>> getCampaignsActiveCount(@Body BaseRequest seed);

    @POST("api/v1/tp/getCampaignById")
    Call<QPayBaseResponse> getCampaignById(@Body OnDemandRequest campaignId);

    @POST("api/v1/tp/createCampaignViewed")
    Call<BaseResponse<String>> createCampaignViewed(@Body CampaignViewedRequest campaignViewedRequest);

    @POST("api/v1/ru/getCouponsListByUser")
    Call<BaseResponse<CouponDetailsListResponse>> getCouponsListByUser(@Body BimboIdBaseRequest bimboIdBaseRequest);

    @POST("api/v1/ru/getCouponAllDetailsByUser")
    Call<BaseResponse<CouponDetailsListResponse>> getCouponAllDetailsByUser(@Body BimboIdBaseRequest bimboIdBaseRequest);

    @POST("api/v1/ru/getCouponDetailByUser")
    Call<BaseResponse<CouponDetailsListResponse>> getCouponDetailByUser(@Body CouponDetailsRequest couponDetailsRequest);

    @POST("api/v1/ru/setStatusCouponPrinted")
    Call<BaseResponse<Boolean>> setStatusCouponPrinted(@Body CouponDetailsRequest couponDetailsRequest);

    @POST("api/v1/ru/isValidTerminalforCoupons")
    Call<BaseResponse<Boolean>> isValidTerminalforCoupons(@Body BimboIdBaseRequest bimboIdBaseRequest);

    @POST("api/v1/ru/getCountCouponsbyUser")
    Call<BaseResponse<CouponsCountResponse>> getCountCouponsbyUser(@Body BimboIdBaseRequest bimboIdBaseRequest);

}

interface RetrofitDataAccesibleQTC {

    @POST("api/v1/b2c/getOrders")
    Call<BaseResponse<OrderResponse>> getOrders(@Body OrderRequest orderRequest);

    @POST("api/v1/b2c/getOrderDetails")
    Call<BaseResponse<OrderDetailsResponse>> getOrderDetails(@Body OrderDetailsRequest orderDetailsRequest);

    @POST("api/v1/b2c/acceptOrder")
    Call<BaseResponse<AcceptOrderResponse>> acceptOrder(@Body AcceptOrderRequest acceptOrderRequest);

    @POST("api/v1/b2c/declineOrder")
    Call<BaseResponse<DeclineOrderResponse>> declineOrder(@Body DeclineOrderRequest declineOrderRequest);

    @POST("api/v1/b2c/getOrderAcceptance")
    Call<BaseResponse<GetOrderAcceptanceResponse>> getOrderAcceptance(@Body BaseRequest baseRequest);

    @POST("api/v1/b2c/setOrderAcceptance")
    Call<BaseResponse<String>> setOrderAcceptance(@Body SetOrderAcceptanceRequest setOrderAcceptanceRequest);

    @POST("api/v1/b2c/deliverOrder")
    Call<BaseResponse<AcceptOrderResponse>> deliverOrder(@Body DeclineOrderRequest declineOrderRequest);

}

public class DataAccess implements DataAccessible, DataAccessibleQTC {

    private final Context context;
    private final Retrofit retrofit;
    private final Retrofit retrofitQTC;
    private RetrofitDataAccesible retrofitDataAccessible;
    private RetrofitDataAccesibleQTC retrofitDataAccessibleQTC;
    private String BASE_URL = HOST_RS + "/";
    private String BASE_URL_QTC = HOST + "/";

    // TODO: Timeout
    public DataAccess(Context context/*, Boolean isShortTimeout*/) {
        this.context = context;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        String url = BASE_URL;
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        retrofitDataAccessible = retrofit.create(RetrofitDataAccesible.class);

        String urlQTC = BASE_URL_QTC;
        retrofitQTC = new Retrofit.Builder()
                .baseUrl(urlQTC)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        retrofitDataAccessibleQTC = retrofitQTC.create(RetrofitDataAccesibleQTC.class);
    }

    @Override
    public void getCarrouselCampaigns(Callback<QPayBaseResponse> callback, CarouselCampaignsRequest seed) {
        retrofitDataAccessible.getCarrouselCampaigns(seed).enqueue(callback);
    }

    @Override
    public void getCampaignActive(Callback<QPayBaseResponse> callback, CarouselCampaignsRequest seed) {
        retrofitDataAccessible.getCampaignActive(seed).enqueue(callback);
    }

    @Override
    public void getCampaignsActiveNewCount(Callback<BaseResponse<CampaignsActiveNewCount>> callback, BaseRequest seed) {
        retrofitDataAccessible.getCampaignsActiveNewCount(seed).enqueue(callback);
    }

    @Override
    public void getCampaignsActiveCount(Callback<BaseResponse<CampaignsActiveCount>> callback, BaseRequest seed) {
        retrofitDataAccessible.getCampaignsActiveCount(seed).enqueue(callback);
    }

    @Override
    public void getCampaignById(Callback<QPayBaseResponse> callback, OnDemandRequest campaignId) {
        retrofitDataAccessible.getCampaignById(campaignId).enqueue(callback);
    }

    @Override
    public void createCampaignViewed(Callback<BaseResponse<String>> callback, CampaignViewedRequest campaignViewedRequest) {
        retrofitDataAccessible.createCampaignViewed(campaignViewedRequest).enqueue(callback);
    }

    @Override
    public void getOrders(Callback<BaseResponse<OrderResponse>> callback, OrderRequest orderRequest) {
        retrofitDataAccessibleQTC.getOrders(orderRequest).enqueue(callback);
    }

    @Override
    public void getOrderDetails(Callback<BaseResponse<OrderDetailsResponse>> callback, OrderDetailsRequest orderDetailsRequest) {
        retrofitDataAccessibleQTC.getOrderDetails(orderDetailsRequest).enqueue(callback);
    }

    @Override
    public void acceptOrder(Callback<BaseResponse<AcceptOrderResponse>> callback, AcceptOrderRequest acceptOrderRequest) {
        retrofitDataAccessibleQTC.acceptOrder(acceptOrderRequest).enqueue(callback);
    }

    @Override
    public void declineOrder(Callback<BaseResponse<DeclineOrderResponse>> callback, DeclineOrderRequest declineOrderRequest) {
        retrofitDataAccessibleQTC.declineOrder(declineOrderRequest).enqueue(callback);
    }

    @Override
    public void getOrderAcceptance(Callback<BaseResponse<GetOrderAcceptanceResponse>> callback, BaseRequest baseRequest) {
        retrofitDataAccessibleQTC.getOrderAcceptance(baseRequest).enqueue(callback);
    }

    @Override
    public void setOrderAcceptance(Callback<BaseResponse<String>> callback, SetOrderAcceptanceRequest setOrderAcceptanceRequest) {
        retrofitDataAccessibleQTC.setOrderAcceptance(setOrderAcceptanceRequest).enqueue(callback);
    }

    @Override
    public void deliverOrder(Callback<BaseResponse<AcceptOrderResponse>> callback, DeclineOrderRequest declineOrderRequest) {
        retrofitDataAccessibleQTC.deliverOrder(declineOrderRequest).enqueue(callback);
    }

    @Override
    public void getCouponsListByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, BimboIdBaseRequest bimboIdBaseRequest) {
        retrofitDataAccessible.getCouponsListByUser(bimboIdBaseRequest).enqueue(callback);
    }

    @Override
    public void getCouponAllDetailsByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, BimboIdBaseRequest bimboIdBaseRequest) {
        retrofitDataAccessible.getCouponAllDetailsByUser(bimboIdBaseRequest).enqueue(callback);
    }

    @Override
    public void getCouponDetailByUser(Callback<BaseResponse<CouponDetailsListResponse>> callback, CouponDetailsRequest couponDetailsRequest) {
        retrofitDataAccessible.getCouponDetailByUser(couponDetailsRequest).enqueue(callback);
    }

    @Override
    public void setStatusCouponPrinted(Callback<BaseResponse<Boolean>> callback, CouponDetailsRequest couponDetailsRequest) {
        retrofitDataAccessible.setStatusCouponPrinted(couponDetailsRequest).enqueue(callback);
    }

    @Override
    public void isValidTerminalforCoupons(Callback<BaseResponse<Boolean>> callback, BimboIdBaseRequest bimboIdBaseRequest) {
        retrofitDataAccessible.isValidTerminalforCoupons(bimboIdBaseRequest).enqueue(callback);
    }

    @Override
    public void getCountCouponsbyUser(Callback<BaseResponse<CouponsCountResponse>> callback, BimboIdBaseRequest bimboIdBaseRequest) {
        retrofitDataAccessible.getCountCouponsbyUser(bimboIdBaseRequest).enqueue(callback);
    }

}
