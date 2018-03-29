package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.DisciplinaData;
import com.example.tcc.qbeaconapp.Datas.TurmaData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by hugoduarte on 26/03/18.
 */

public interface TurmaService {

    @Headers("Content-Type: application/json")
    @GET("turma")
    Call<List<TurmaData>> listar();

    @Headers("Content-Type: application/json")
    @GET("turma/minhas_turmas")
    Call<List<TurmaData>> listarMinhasTurmas();

    @Headers("Content-Type: application/json")
    @GET("turma/{id}")
    Call<TurmaData>buscar(@Path("id") int id);

}
