package com.example.tcc.qbeaconapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.MensagemRetorno;
import com.example.tcc.qbeaconapp.Datas.UsuarioData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.UsuarioService;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private AppCompatButton buttonCadastrar;
    private AppCompatButton buttonVoltar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = (EditText) findViewById(R.id.nomeInput);
        editTextEmail = (EditText) findViewById(R.id.emailInput);
        editTextSenha = (EditText) findViewById(R.id.senhaInput);

        buttonCadastrar = (AppCompatButton) findViewById(R.id.cadastroButton);
        buttonVoltar = (AppCompatButton) findViewById(R.id.voltarButton);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void cadastrar(){

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioService = retrofit.create(UsuarioService.class);

        UsuarioData usuario = new UsuarioData();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setEmail(editTextEmail.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());

        Call<MensagemRetorno> call = usuarioService.cadastrar(usuario);

        call.enqueue(new Callback<MensagemRetorno>() {
            @Override
            public void onResponse(Call<MensagemRetorno> call, Response<MensagemRetorno> response) {

                if(response.isSuccessful() &&
                        response.body().getMensagem().toString().equals(Config.SUCESSO_CADASTRO_USUARIO)){

                    Toast.makeText(getBaseContext(),
                            Config.SUCESSO_CADASTRO_USUARIO,
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<MensagemRetorno> call, Throwable t) {
                Toast.makeText(CadastroActivity.this,
                        Config.ERRO_CADASTRO_USUARIO,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
