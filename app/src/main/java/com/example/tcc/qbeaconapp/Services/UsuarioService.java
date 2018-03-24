package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.MensagemRetorno;
import com.example.tcc.qbeaconapp.Datas.UsuarioData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by hugoduarte on 16/03/18.
 */

public interface UsuarioService {

    @Headers("Content-Type: application/json")
    @POST("usuario")
    Call<MensagemRetorno>cadastrar(@Body UsuarioData usuario);

    @Headers("Content-Type: application/json")
    @POST("usuario/logar")
    Call<Void>logar(@Body UsuarioData usuario);

    @Headers("Content-Type: application/json")
    @GET("usuario")
    Call<UsuarioData>buscar();
}



