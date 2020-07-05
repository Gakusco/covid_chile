package com.ubb.covidchile.Retrofit;

import com.ubb.covidchile.Common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceClient {

    public static WebServiceClient instance;
    private WebService webService;
    private Retrofit retrofit;

    private WebServiceClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_COVID_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webService = retrofit.create(WebService.class);
    }

    public static WebServiceClient getInstance() {
        if (instance == null) instance = new WebServiceClient();
        return instance;
    }

    public WebService getWebService() {
        return webService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
