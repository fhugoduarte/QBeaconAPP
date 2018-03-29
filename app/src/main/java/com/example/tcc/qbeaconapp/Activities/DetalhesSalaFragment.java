package com.example.tcc.qbeaconapp.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.ItemListView;
import com.example.tcc.qbeaconapp.Datas.ReservaData;
import com.example.tcc.qbeaconapp.Datas.SalaData;
import com.example.tcc.qbeaconapp.Datas.TurmaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.AdapterListView;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
import com.example.tcc.qbeaconapp.Services.SalaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hugoduarte on 28/03/18.
 */

public class DetalhesSalaFragment extends Fragment {

    private TextView nome;
    private TextView detalhes;
    private ListView listReservas;
    private Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int id_sala = getArguments().getInt("id");

        View view = inflater.inflate(R.layout.fragment_detalhes_sala, container, false);

        nome = (TextView) view.findViewById(R.id.salaNome);
        detalhes = (TextView) view.findViewById(R.id.detalhesSala);
        listReservas = (ListView) view.findViewById(R.id.listReservas);
        communicator = (Communicator) getActivity();

        getSala(id_sala);

        return view;
    }

    private void getSala(int id){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        SalaService salaService =
                ServiceGenerator.createService(SalaService.class, token);

        Call<SalaData> call = salaService.buscar(id);
        call.enqueue(new Callback<SalaData>() {
            @Override
            public void onResponse(Call<SalaData> call, Response<SalaData> response) {
                if(response.isSuccessful()){
                    SalaData salaData = (SalaData) response.body();
                    setSala(salaData);
                }else{
                    Toast.makeText(getContext(),
                            "Erro ao achar a sala!",
                            Toast.LENGTH_SHORT).show();

                    communicator.responde(0, new SalasFragment(), "Salas");
                }
            }

            @Override
            public void onFailure(Call<SalaData> call, Throwable t) {

            }
        });

    }

    private void setSala(SalaData sala){
        nome.setText(sala.getNome());
        detalhes.setText("( " + sala.getBloco() + "/" + sala.getCampus() + "/" + sala.getInstituicao() + " )");

        final List<ReservaData> reservas = sala.getReservas();

        List<ItemListView> itens = new ArrayList<ItemListView>();

        for (ReservaData reserva: reservas) {
            itens.add(new ItemListView(reserva.getId(), "", reserva.toString(), R.drawable.ic_info_black_24dp));
        }

        AdapterListView adapterListView = new AdapterListView(getContext(), itens);

        listReservas.setAdapter(adapterListView);
    }

}
