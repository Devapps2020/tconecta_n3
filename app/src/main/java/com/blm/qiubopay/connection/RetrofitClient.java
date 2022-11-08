package com.blm.qiubopay.connection;

import com.blm.qiubopay.utils.Globals;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    //d5670ce8-ea15-449f-b3af-8270defd4d0e
    private static final String BASE_URL = Globals.HOST+"/";
    //private static final String BASE_URL = Globals.HOST;
    //private static final String BASE_URL = "http://qtcsit2.qiubo.mx:8080/service/";
    //private static final String BASE_URL = "https://run.mocky.io/v3/";

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
