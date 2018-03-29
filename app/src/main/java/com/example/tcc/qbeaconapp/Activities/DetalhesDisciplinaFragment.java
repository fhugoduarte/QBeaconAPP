package com.example.tcc.qbeaconapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.tcc.qbeaconapp.Datas.ItemListView;
import com.example.tcc.qbeaconapp.Datas.TurmaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.AdapterListView;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hugoduarte on 27/03/18.
 */

public class DetalhesDisciplinaFragment extends Fragment {

    private TextView nome;
    private Communicator communicator;
    private ListView listTurmas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        int id_disciplina = getArguments().getInt("id");

        View view = inflater.inflate(R.layout.fragment_detalhes_disciplina, container, false);

        listTurmas = (ListView) view.findViewById(R.id.listTurmas);
        nome = (TextView) view.findViewById(R.id.disciplinaNome);
        communicator = (Communicator) getActivity();

        getDisciplina(id_disciplina);

        return(view);
    }

    private void getDisciplina(int id){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        DisciplinaService disciplinaService =
                ServiceGenerator.createService(DisciplinaService.class, token);

        Call<DisciplinaData> call = disciplinaService.buscar(id);
        call.enqueue(new Callback<DisciplinaData>() {
            @Override
            public void onResponse(Call<DisciplinaData> call, Response<DisciplinaData> response) {
                if(response.isSuccessful()){
                    DisciplinaData disciplinaData = response.body();
                    setDisciplina(disciplinaData);
                }else{
                    Toast.makeText(getContext(),
                            "Erro ao achar a disciplina!",
                            Toast.LENGTH_SHORT).show();

                    communicator.responde(0, new DisciplinasFragment(), "Disciplinas");
                }
            }

            @Override
            public void onFailure(Call<DisciplinaData> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Erro ao achar a disciplina!",
                        Toast.LENGTH_SHORT).show();

                communicator.responde(0, new DisciplinasFragment(), "Disciplinas");
            }
        });

    }

    private void setDisciplina(DisciplinaData disciplina){

        nome.setText(disciplina.getNome());
        final List<TurmaData> turmas = disciplina.getTurmas();

        List<ItemListView> itens = new ArrayList<ItemListView>();

        for (TurmaData turma: turmas) {
            itens.add(new ItemListView(turma.getId(), turma.toString(), turma.toString(), R.drawable.ic_school_black_24dp));
        }

        AdapterListView adapterListView = new AdapterListView(getContext(), itens);

        listTurmas.setAdapter(adapterListView);
        listTurmas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ItemListView item = (ItemListView) listTurmas.getItemAtPosition(i);
                communicator.responde(item.getId(), new DetalhesTurmaFragment(), item.getTitulo());
            }
        });

    }

}
