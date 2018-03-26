package com.example.tcc.qbeaconapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.DisciplinaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
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
 * Created by hugoduarte on 24/03/18.
 */

public class DisciplinasFragment extends Fragment {

    private ListView listDisciplinas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_disciplinas, container, false);

        listDisciplinas = (ListView) view.findViewById(R.id.listDisciplinas);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.DISCIPLINAS_KEY, MODE_PRIVATE);
        String disciplinasJson =  sharedPreferences.getString(Config.DISCIPLINAS_KEY, "");

        if(disciplinasJson == ""){
            setDisciplinas();
        }else{
            adicionaDisciplinas(disciplinasJson);
        }


        return(view);
    }

    private void setDisciplinas(){

        final FragmentActivity fragment = this.getActivity();

        SharedPreferences sharedPreferences = fragment.getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        DisciplinaService disciplinaService =
                ServiceGenerator.createService(DisciplinaService.class, token);

        Call<List<DisciplinaData>> call = disciplinaService.listar();
        call.enqueue(new Callback<List<DisciplinaData>>() {
            @Override
            public void onResponse(Call<List<DisciplinaData>> call, Response<List<DisciplinaData>> response) {
                SharedPreferences sharedPreferences1 =
                        fragment.getSharedPreferences(Config.DISCIPLINAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String disciplinas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.DISCIPLINAS_KEY, disciplinas);
                    editor.commit();

                    adicionaDisciplinas(disciplinas);
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
                        fragment.getSharedPreferences(Config.DISCIPLINAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.DISCIPLINAS_KEY, "");
                editor.commit();

                Toast.makeText(fragment,
                        "Erro ao recuperar as Disciplinas!",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void adicionaDisciplinas(String disciplinasJson){
        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<DisciplinaData>>(){}.getType();
        final List<DisciplinaData> disciplinas = gson.fromJson(disciplinasJson, collectionType);

        ArrayAdapter<DisciplinaData> adapter =
                new ArrayAdapter<DisciplinaData>(this.getContext(),
                        android.R.layout.simple_list_item_1, disciplinas);

        listDisciplinas.setAdapter(adapter);
        listDisciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(),
                        "TESTE: " + disciplinas.get(i).getNome(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
