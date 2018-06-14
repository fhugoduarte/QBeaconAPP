package com.example.tcc.qbeaconapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.ItemListView;
import com.example.tcc.qbeaconapp.Datas.ReservaData;
import com.example.tcc.qbeaconapp.Datas.SalaData;
import com.example.tcc.qbeaconapp.Datas.SalaProximaData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.AdapterListView;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.SalaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private TextView salaNome;
    private TextView detalhesSala;
    private TextView turmaAtual;
    private Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //int id_sala_proxima = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        salaNome = (TextView) view.findViewById(R.id.salaNome);
        detalhesSala = (TextView) view.findViewById(R.id.detalhesSala);
        turmaAtual = (TextView) view.findViewById(R.id.turmaAtual);
        communicator = (Communicator) getActivity();

        getSalaProxima(id_sala_proxima);

        return(view);
    }

    private void getSalaProxima(int id){

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
                    setSalaProxima(salaData);
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

    private void setSalaProxima(SalaProximaData salaProxima){
        salaNome.setText(salaProxima.getNome());

        detalhesSala.setText("( " + salaProxima.getBloco() + "/" + salaProxima.getCampus() + "/" + salaProxima.getInstituicao() + " )");

        turmaAtual.setText(salaProxima.turma.getNome());
    }

}
