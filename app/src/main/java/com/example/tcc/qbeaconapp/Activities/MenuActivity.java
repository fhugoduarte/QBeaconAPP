package com.example.tcc.qbeaconapp.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.UsuarioData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.BeaconFinder;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.example.tcc.qbeaconapp.Services.UsuarioService;
import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Communicator, BeaconConsumer {

    private TextView nome;
    private TextView email;
    private Toolbar toolbar;
    private BeaconManager beaconManager;

    public static final String IBEACON = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    //declaração de variáveis de permissão
    protected static final String TAG = "permission granted";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    public static int ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //verificar se o dispositivo tem bluetooth e se está funcionando.
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            //Hardware Bluetooth não está funcionando!
        } else {
            //Hardware Bluetooth está funcionando!
            if(!btAdapter.isEnabled()) {
                //Solicitando ativação do Bluetooth...
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
            } else {
                //Bluetooth já ativado!
            }
        }

        //pedir permissão para localização nos dispositivos com android M ou superior
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Android M Permission check 
            if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Este aplicativo precisa de acesso a sua localização para funcionar de maneira apropriada!");
                builder.setMessage("Por favor, garanta o acesso a localição para que este aplicativo possa iniciar a detecção dos Beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        //inicializando beaconManager, passando o contexto "this"
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setAndroidLScanningDisabled(true);
        //passar o layout (protocolo), a qual o beacon irá ser configurado, para o beaconManager
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON));
        beaconManager.setForegroundScanPeriod(6000);
        //##Alterar o intervalo de tempo que os beacons são scaneados! Atenção: dependendo dos
        //##valores selecionados poderá haver ainda mais imprecisão!

        //startar o beaconManager  (bind = start)
        beaconManager.bind(this);

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

    //método para solicitar a ativação da localização do usuário
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Funcionalidade limitada!");
                    builder.setMessage("Enquanto o acesso a localização não for fornecido, este aplicativo não será capaz de identificar Beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    //Método para solicitar que o usuário ative o bluetooth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                //usuário ativou o bluetooth
            }
            else {
                //usuário não ativou o bluetooth! Continuar pedindo até que ele o faça!
                onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
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

    public void logout(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.TOKEN_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        sharedPreferences = getSharedPreferences(Config.DISCIPLINAS_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        sharedPreferences = getSharedPreferences(Config.TODAS_TURMAS_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        sharedPreferences = getSharedPreferences(Config.MINHAS_TURMAS_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        sharedPreferences = getSharedPreferences(Config.USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
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
        if (id == R.id.action_atualizar) {
            Intent intent = new Intent(MenuActivity.this, CarregaDadosActivity.class);
            startActivity(intent);

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
            } else if (id == R.id.navSalas) {
                toolbar.setTitle("Salas");
                fragment = new SalasFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                fragmentTransaction.commit();
            } else if (id == R.id.logout){
                logout();
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            } else{
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

    @Override
    public void responde(int id, Fragment fragment, String titulo) {

       Bundle bundle =  new Bundle();
       bundle.putInt("id", id);

       toolbar.setTitle(titulo);
       fragment.setArguments(bundle);
       FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       fragmentTransaction.replace(R.id.fragmentLayout, fragment);
       fragmentTransaction.commit();

    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            //detecta todos os beacons proximos ao dispositivo android
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                //verifica se algum beacon foi detectado
                if (beacons.size() > 0) {
                    //pegar o primeiro beacon que foi detectado e passar para o próximo
                    Beacon firstBeacon = beacons.iterator().next();
                    //pega a primeira parte do UUID do beacon (8bits) e tira apenas a parte da informação da sala.
                    String UUID = firstBeacon.getId1().toUuid().toString().substring(1, 8);
                    String salaID = "";
                    for (int i=0; i<UUID.length(); i++) {
                        char c = UUID.charAt(i);
                        if(c != '0'){
                            salaID = UUID.substring(i, UUID.length());
                            break;
                        }
                    }
                    Log.i(TAG, "ID da Sala " + salaID);

                    Log.i(TAG, "Distância do Beacon encontrado: " + firstBeacon.getDistance() + " metros.");
                    //chamada do método para capturar os dados mandados pelo beacon e mostrá-los na tela.
                    //onBeaconFinded(firstBeacon);
                }else{
                    Log.i("ERRO: ", "Não encontramos o Beacon");
                }
                //# pode se criar uma condição "else" para quando um beacon não é encontrato,
                //ou mostrar uma mensagem pedindo ao usuário para se aproximar mais dos beacons.
            }
        });

        try {
            //tenta identificar um beacon de uma "região" especifica #região precisa ser declarada anteriormente
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

}
