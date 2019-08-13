package com.abms.af.projeversion02.RestApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {
    private RestApi mRestAApi;

    public RestApiClient (String restApiServiceUrl)
    {
        OkHttpClient.Builder builder= new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .connectTimeout(3,TimeUnit.SECONDS);

        OkHttpClient OkHttpClient=builder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(restApiServiceUrl)
                .client(OkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mRestAApi=retrofit.create(RestApi.class);
    }

    public RestApi getmRestAApi()
    {
        return mRestAApi;
    }

}
