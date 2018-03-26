package com.example.tcc.qbeaconapp.Activities;

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

import com.example.tcc.qbeaconapp.Datas.TurmaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.example.tcc.qbeaconapp.Services.TurmaService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hugoduarte on 24/03/18.
 */

public class MinhasTurmasFragment extends Fragment {

    private ListView listMinhasTurmas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_minhas_turmas, container, false);

        listMinhasTurmas = (ListView) view.findViewById(R.id.listMinhasTurmas);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.MINHAS_TURMAS_KEY, MODE_PRIVATE);
        String turmasJson =  sharedPreferences.getString(Config.MINHAS_TURMAS_KEY, "");

        if(turmasJson == ""){
            setTurmas();
        }else {
            adicionaTurmas(turmasJson);
        }



        return(view);
    }

    private void setTurmas(){

        final FragmentActivity fragment = this.getActivity();

        SharedPreferences sharedPreferences = fragment.getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        TurmaService turmaService =
                ServiceGenerator.createService(TurmaService.class, token);

        Call<List<TurmaData>> call = turmaService.listarMinhasTurmas();
        call.enqueue(new Callback<List<TurmaData>>() {
            @Override
            public void onResponse(Call<List<TurmaData>> call, Response<List<TurmaData>> response) {
                SharedPreferences sharedPreferences1 =
                        fragment.getSharedPreferences(Config.MINHAS_TURMAS_KEY, MODE_PRIVATE);

                if(response.isSuccessful()){

                    Gson gson = new Gson();

                    String turmas = gson.toJson(response.body());

                    SharedPreferences.Editor editor =
                            sharedPreferences1.edit();
                    editor.putString(Config.MINHAS_TURMAS_KEY, turmas);
                    editor.commit();

                    adicionaTurmas(turmas);

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
                        fragment.getSharedPreferences(Config.MINHAS_TURMAS_KEY, MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        sharedPreferences1.edit();
                editor.putString(Config.MINHAS_TURMAS_KEY, "");
                editor.commit();

                Toast.makeText(fragment,
                        "Erro ao recuperar as suas Turmas!",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void adicionaTurmas(String turmasJson){
        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<TurmaData>>() {
        }.getType();
        final List<TurmaData> turmas = gson.fromJson(turmasJson, collectionType);

        ArrayAdapter<TurmaData> adapter =
                new ArrayAdapter<TurmaData>(this.getContext(),
                        android.R.layout.simple_list_item_1, turmas);

        listMinhasTurmas.setAdapter(adapter);
        listMinhasTurmas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(),
                        "TESTE: " + turmas.get(i).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
