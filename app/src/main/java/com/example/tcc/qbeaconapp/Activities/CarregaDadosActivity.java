package com.example.tcc.qbeaconapp.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.AuthToken;
import com.example.tcc.qbeaconapp.Datas.DisciplinaData;
import com.example.tcc.qbeaconapp.Datas.MensagemRetorno;
import com.example.tcc.qbeaconapp.Datas.TurmaData;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.example.tcc.qbeaconapp.Services.TurmaService;
import com.example.tcc.qbeaconapp.Services.UsuarioService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarregaDadosActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void atualizarDados(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        final String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioService = retrofit.create(UsuarioService.class);

        AuthToken authToken = new AuthToken(token);

        Call<MensagemRetorno> call = usuarioService.validarToken(authToken);
        call.enqueue(new Callback<MensagemRetorno>() {
            @Override
            public void onResponse(Call<MensagemRetorno> call, Response<MensagemRetorno> response) {
                if(response.isSuccessful() && response.body().getMensagem() == Config.TOKEN_VALIDO){
                    setTodasTurmas(token);
                    setMinhasTurmas(token);
                    setDisciplinas(token);
                }
                Intent intent = new Intent(CarregaDadosActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<MensagemRetorno> call, Throwable t) {
                Intent intent = new Intent(CarregaDadosActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    private void setTodasTurmas(String token){

        TurmaService turmaService =
                ServiceGenerator.createService(TurmaService.class, token);

        Call<List<TurmaData>> call = turmaService.listar();
        call.enqueue(new Callback<List<TurmaData>>() {
            @Override
            public void onResponse(Call<List<TurmaData>> call, Response<List<TurmaData>> response) {
                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.TODAS_TURMAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String turmas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.TODAS_TURMAS_KEY, turmas);
                    editor.commit();

                }else {

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.TODAS_TURMAS_KEY, "");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<TurmaData>> call, Throwable t) {

                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.TODAS_TURMAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.TODAS_TURMAS_KEY, "");
                editor.commit();

                Toast.makeText(getBaseContext(),
                        "Erro ao recuperar as Turmas!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setMinhasTurmas(String token){

        TurmaService turmaService =
                ServiceGenerator.createService(TurmaService.class, token);

        Call<List<TurmaData>> call = turmaService.listarMinhasTurmas();
        call.enqueue(new Callback<List<TurmaData>>() {
            @Override
            public void onResponse(Call<List<TurmaData>> call, Response<List<TurmaData>> response) {
                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.MINHAS_TURMAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String turmas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.MINHAS_TURMAS_KEY, turmas);
                    editor.commit();

                }else {

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.MINHAS_TURMAS_KEY, "");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<TurmaData>> call, Throwable t) {

                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.MINHAS_TURMAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.MINHAS_TURMAS_KEY, "");
                editor.commit();

                Toast.makeText(getBaseContext(),
                        "Erro ao recuperar as suas Turmas!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setDisciplinas(String token){

        DisciplinaService disciplinaService =
                ServiceGenerator.createService(DisciplinaService.class, token);

        Call<List<DisciplinaData>> call = disciplinaService.listar();
        call.enqueue(new Callback<List<DisciplinaData>>() {
            @Override
            public void onResponse(Call<List<DisciplinaData>> call, Response<List<DisciplinaData>> response) {
                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.DISCIPLINAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String disciplinas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.DISCIPLINAS_KEY, disciplinas);
                    editor.commit();

                }else {

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.DISCIPLINAS_KEY, "");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<DisciplinaData>> call, Throwable t) {

                SharedPreferences sharedPreferences1 =
                        getSharedPreferences(Config.DISCIPLINAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.DISCIPLINAS_KEY, "");
                editor.commit();

                Toast.makeText(getBaseContext(),
                        "Erro ao recuperar as Disciplinas!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

}
