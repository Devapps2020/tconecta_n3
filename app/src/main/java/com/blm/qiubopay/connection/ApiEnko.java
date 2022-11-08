package com.blm.qiubopay.connection;


import com.blm.qiubopay.models.enko.EnkoUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiEnko {
    @Headers({"Content-Type: application/json"})
    @POST("api/mx/simple_register/")
    Call<EnkoUser> registerEnko(@Body EnkoUser enkoUser);

    @Headers({"Content-Type: application/json"})
    @POST("api/mx/login/")
    Call<EnkoUser> loginEnko(@Body EnkoUser enkoUser);
}

