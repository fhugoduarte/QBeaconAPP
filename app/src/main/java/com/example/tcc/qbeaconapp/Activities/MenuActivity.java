package com.example.tcc.qbeaconapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.UsuarioData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.example.tcc.qbeaconapp.Services.UsuarioService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nome;
    private TextView email;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setarUsuarioLogado();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            onNavigationItemSelected(null);
        }
    }

    public void setarUsuarioLogado(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        UsuarioService usuarioService =
                ServiceGenerator.createService(UsuarioService.class, token);

        Call<UsuarioData> call = usuarioService.buscar();

        call.enqueue(new Callback<UsuarioData>() {
            @Override
            public void onResponse(Call<UsuarioData> call, Response<UsuarioData> response) {
                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String usuario = gson.toJson(response.body());

                    SharedPreferences sharedPreferences =
                            getSharedPreferences(Config.USER_KEY, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor =
                            sharedPreferences.edit();
                    editor.putString(Config.USER_KEY, usuario);
                    editor.commit();

                    nome = (TextView) findViewById(R.id.nomeTextView);
                    nome.setText(response.body().getNome());

                    email = (TextView) findViewById(R.id.emailTextView);
                    email.setText(response.body().getEmail());

                }

            }

            @Override
            public void onFailure(Call<UsuarioData> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getBaseContext(),
                    "SETTINGS",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        if(item == null){
            toolbar.setTitle("Home");
            fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, fragment);
            fragmentTransaction.commit();
        }else {
            int id = item.getItemId();

            if (id == R.id.navMinhasTurmas) {
                toolbar.setTitle("Minhas Turmas");
                fragment = new MinhasTurmasFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                fragmentTransaction.commit();
            } else if (id == R.id.navTodasTurmas) {
                toolbar.setTitle("Todas Turmas");
                fragment = new TodasTurmasFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                fragmentTransaction.commit();

            } else if (id == R.id.navDisciplinas) {
                toolbar.setTitle("Disciplinas");
                fragment = new DisciplinasFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                fragmentTransaction.commit();
            }else{
                toolbar.setTitle("Home");
                fragment = new HomeFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                fragmentTransaction.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
