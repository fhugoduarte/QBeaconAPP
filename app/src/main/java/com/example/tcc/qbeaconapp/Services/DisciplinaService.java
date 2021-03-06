package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.DisciplinaData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by hugoduarte on 26/03/18.
 */

public interface DisciplinaService {

    @Headers("Content-Type: application/json")
    @GET("disciplina")
    Call<List<DisciplinaData>>listar();

    @Headers("Content-Type: application/json")
    @GET("disciplina/{id}")
    Call<DisciplinaData>buscar(@Path("id") int id);

}
