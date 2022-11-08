package com.blm.qiubopay.connection;

import com.blm.qiubopay.utils.Globals;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnkoRetrofitClient {

    private static Retrofit retrofit;

    private static Retrofit retrofitMock;

    //d5670ce8-ea15-449f-b3af-8270defd4d0e
    //private static final String BASE_URL = Globals.HOST+"/";
    //private static final String BASE_URL = Globals.HOST;
    //private static final String BASE_URL = "http://qtcsit2.qiubo.mx:8080/service/";
    //private static final String BASE_URL = "https://run.mocky.io/v3/";

    public static Retrofit getRetrofit(String url){
        if (retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }
}

