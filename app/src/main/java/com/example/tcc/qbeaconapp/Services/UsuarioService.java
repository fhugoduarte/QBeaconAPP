package com.example.tcc.qbeaconapp.Services;

import com.example.tcc.qbeaconapp.Datas.MensagemRetorno;
import com.example.tcc.qbeaconapp.Datas.UsuarioData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hugoduarte on 16/03/18.
 */

public interface UsuarioService {

    @POST("usuario")
    Call<MensagemRetorno>cadastrar(@Body UsuarioData usuario);
}
