package com.example.tcc.qbeaconapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.UsuarioData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.UsuarioService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private AppCompatButton buttonLogin;
    private AppCompatButton buttonCadastrar;
    private boolean logado = false;
    private SharedPreferences.Editor editor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.emailInput);
        editTextSenha = (EditText) findViewById(R.id.senhaInput);

        buttonLogin = (AppCompatButton) findViewById(R.id.loginButton);
        buttonCadastrar = (AppCompatButton) findViewById(R.id.cadastroButton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                //startActivity(intent);

                Intent intent = new Intent(LoginActivity.this, CarregaDadosActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        final String email = editTextEmail.getText().toString().trim();
        final String senha = editTextSenha.getText().toString().trim();

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioService = retrofit.create(UsuarioService.class);

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail(editTextEmail.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());

        Call<Void> call = usuarioService.logar(usuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){

                    SharedPreferences sharedPreferences =
                            getSharedPreferences(Config.TOKEN_KEY, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor =
                            sharedPreferences.edit();
                    editor.putString(Config.TOKEN_KEY, response.headers().get("Authorization"));
                    editor.commit();

                    Toast.makeText(getBaseContext(),
                            Config.SUCESSO_LOGIN_USUARIO,
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this,
                            Config.ERRO_EMAIL_SENHA,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        Config.ERRO_EMAIL_SENHA,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}

