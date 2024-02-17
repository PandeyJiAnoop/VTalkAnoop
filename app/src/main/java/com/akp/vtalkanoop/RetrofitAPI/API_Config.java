package com.akp.vtalkanoop.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class API_Config {
    private static String URL = "https://vtalklive.com/api/Vtalk/";

    public static ApiService getApiClient_ByPost()
    {
        Gson gson = new GsonBuilder() .setLenient() .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        OkHttpClient.Builder client1 = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client1.addNetworkInterceptor(loggingInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .client(client1.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService getApiClient_ByGet() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        OkHttpClient.Builder client1 = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client1.addNetworkInterceptor(loggingInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .client(client1.build())
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(ApiService.class);
    }

}
