package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.SalaData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by hugoduarte on 28/03/18.
 */

public interface SalaService {

    @Headers("Content-Type: application/json")
    @GET("sala")
    Call<List<SalaData>> listar();

    @Headers("Content-Type: application/json")
    @GET("sala/{id}")
    Call<SalaData>buscar(@Path("id") int id);

}
