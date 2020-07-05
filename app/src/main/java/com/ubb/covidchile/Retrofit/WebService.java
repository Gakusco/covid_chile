package com.ubb.covidchile.Retrofit;


import com.ubb.covidchile.Retrofit.Request.RequestWS;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebService {
    @POST("regiones")
    Call<RequestWS> regiones();

    @POST("data/all")
    Call<RequestWS> dataAll();

    @POST("data/{idregion}")
    Call<RequestWS> dataId(@Path("idregion") int idRegion);

    @POST("data/nacional")
    Call<RequestWS> dataNacional();
}

