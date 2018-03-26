package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.DisciplinaData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by hugoduarte on 26/03/18.
 */

public interface DisciplinaService {

    @Headers("Content-Type: application/json")
    @GET("disciplina")
    Call<List<DisciplinaData>>listar();

}
