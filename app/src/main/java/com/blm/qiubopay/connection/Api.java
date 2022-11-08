package com.blm.qiubopay.connection;

import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.pidelo.CancelOrder;
import com.blm.qiubopay.models.pidelo.NewOrder;
import com.blm.qiubopay.models.pidelo.Order;
import com.blm.qiubopay.models.pidelo.OrderModification;
import com.blm.qiubopay.models.pidelo.OrderPayment;
import com.blm.qiubopay.models.pidelo.RequestListOrders;
import com.blm.qiubopay.models.pidelo.RequestOrder;
import com.blm.qiubopay.responses.ResponseCancelOrder;
import com.blm.qiubopay.responses.ResponseCreateOrder;
import com.blm.qiubopay.responses.ResponseDistribuitor;
import com.blm.qiubopay.responses.ResponseOrder;
import com.blm.qiubopay.responses.ResponseOrderModifcation;
import com.blm.qiubopay.responses.ResponseOrderPayment;
import com.blm.qiubopay.responses.ResponseUpdateOrder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/getProducts")
    //https://run.mocky.io/v3/a5097160-ff97-4ec6-91d9-23a5f1c0a528
    //@POST("a5097160-ff97-4ec6-91d9-23a5f1c0a528")
    //https://run.mocky.io/v3/06d4496a-6adb-45ed-9436-e976484b1551
    Call<ResponseDistribuitor> getDistributors(@Body QPAY_Seed qpay_seed);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/listOrders")
    //@POST("1f5eb0b8-5aaf-4a90-92cd-e89d0bbd76eb")
    Call<ResponseOrder> getOrders(@Body RequestListOrders requestListOrders);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/createOrder")
    //@POST("1670e0c3-cf86-46ba-b258-931d1204db2a")
    Call<ResponseCreateOrder> createOrder(@Body NewOrder newOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/cancelOrder")
    //@POST("bb6c2cf3-adf4-43a1-bdca-0a5e8c25528b")
    Call<ResponseCancelOrder> cancelOrder(@Body CancelOrder order);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/getOrder")
    //@POST("1f5eb0b8-5aaf-4a90-92cd-e89d0bbd76eb")
    Call<ResponseOrder> getOrder(@Body RequestOrder order);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/confirmUpdateOrder")
    Call<ResponseOrderModifcation> modifyOrder(@Body OrderModification orderModification);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/paymentOrder")
    Call<ResponseOrderPayment> paymentOrder(@Body OrderPayment orderPayment);

    @Headers({"Content-Type: application/json"})
    @POST("api/v1/po/updateOrder")
    //@POST("98363051-c5d2-4a6e-b89a-cfb473971347")
    Call<ResponseUpdateOrder> updateOrder(@Body Order order);


}
