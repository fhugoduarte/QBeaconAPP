package com.example.tcc.qbeaconapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.example.tcc.qbeaconapp.R;

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
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        final String email = editTextEmail.getText().toString().trim();
        final String senha = editTextSenha.getText().toString().trim();



    }

}

