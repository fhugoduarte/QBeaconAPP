package com.example.tcc.qbeaconapp.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.DisciplinaData;
import com.example.tcc.qbeaconapp.Datas.ItemListView;
import com.example.tcc.qbeaconapp.Datas.SalaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.AdapterListView;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
import com.example.tcc.qbeaconapp.Services.SalaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hugoduarte on 28/03/18.
 */

public class SalasFragment extends Fragment {

    private ListView listSalas;
    private Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salas, container, false);

        listSalas = (ListView) view.findViewById(R.id.listSalas);
        communicator = (Communicator) getActivity();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SALAS_KEY, MODE_PRIVATE);
        String salasJson =  sharedPreferences.getString(Config.SALAS_KEY, "");

        if(salasJson == ""){
            setSalas();
        }else{
            adicionaSalas(salasJson);
        }

        return view;
    }

    private void setSalas(){

        final FragmentActivity fragment = this.getActivity();

        SharedPreferences sharedPreferences = fragment.getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        SalaService salaService =
                ServiceGenerator.createService(SalaService.class, token);

        Call<List<SalaData>> call = salaService.listar();
        call.enqueue(new Callback<List<SalaData>>() {
            @Override
            public void onResponse(Call<List<SalaData>> call, Response<List<SalaData>> response) {
                SharedPreferences sharedPreferences1 =
                        fragment.getSharedPreferences(Config.SALAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){
                    Gson gson = new Gson();

                    String salas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.SALAS_KEY, salas);
                    editor.commit();

                    adicionaSalas(salas);
                }else{
                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.SALAS_KEY, "");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<SalaData>> call, Throwable t) {
                SharedPreferences sharedPreferences1 =
                        fragment.getSharedPreferences(Config.SALAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.SALAS_KEY, "");
                editor.commit();

                Toast.makeText(fragment,
                        "Erro ao recuperar as Salas!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void adicionaSalas(String salasJson){
        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<SalaData>>(){}.getType();
        final List<SalaData> salas = gson.fromJson(salasJson, collectionType);

        List<ItemListView> itens = new ArrayList<ItemListView>();

        for (SalaData sala: salas) {
            itens.add(new ItemListView(sala.getId(), sala.getNome(), sala.toString(), R.drawable.ic_edit_location_black_24dp));
        }

        AdapterListView adapterListView = new AdapterListView(getContext(), itens);

        listSalas.setAdapter(adapterListView);
        listSalas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemListView item = (ItemListView) listSalas.getItemAtPosition(i);
                communicator.responde(item.getId(), new DetalhesSalaFragment(), item.getTitulo());
            }
        });
    }

}
