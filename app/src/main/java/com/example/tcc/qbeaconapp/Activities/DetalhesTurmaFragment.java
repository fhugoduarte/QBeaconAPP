package com.example.tcc.qbeaconapp.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.qbeaconapp.Datas.MensagemRetorno;
import com.example.tcc.qbeaconapp.Datas.TurmaData;
import com.example.tcc.qbeaconapp.Datas.UsuarioData;
import com.example.tcc.qbeaconapp.R;
import com.example.tcc.qbeaconapp.Services.Communicator;
import com.example.tcc.qbeaconapp.Services.Config;
import com.example.tcc.qbeaconapp.Services.DisciplinaService;
import com.example.tcc.qbeaconapp.Services.ServiceGenerator;
import com.example.tcc.qbeaconapp.Services.TurmaService;
import com.example.tcc.qbeaconapp.Services.UsuarioService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hugoduarte on 28/03/18.
 */

public class DetalhesTurmaFragment extends Fragment {

    private TextView nome;
    private Communicator communicator;
    private Button entrarButton;
    private TextView reserva1;
    private TextView reserva2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        int id_turma = getArguments().getInt("id");

        View view = inflater.inflate(R.layout.fragment_detalhes_turma, container, false);

        nome = (TextView) view.findViewById(R.id.turmaNome);
        communicator = (Communicator) getActivity();
        entrarButton = (Button) view.findViewById(R.id.entrarButton);
        reserva1 = (TextView) view.findViewById(R.id.reserva1);
        reserva2 = (TextView) view.findViewById(R.id.reserva2);

        getTurma(id_turma);

        return(view);
    }

    private void getTurma(int id_turma){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        TurmaService turmaService =
                ServiceGenerator.createService(TurmaService.class, token);

        Call<TurmaData> call = turmaService.buscar(id_turma);
        call.enqueue(new Callback<TurmaData>() {
            @Override
            public void onResponse(Call<TurmaData> call, Response<TurmaData> response) {
                if(response.isSuccessful()){
                    TurmaData turmaData = (TurmaData) response.body();
                    setTurma(turmaData);
                }else{
                    Toast.makeText(getContext(),
                            "Erro ao achar a turma!",
                            Toast.LENGTH_SHORT).show();

                    communicator.responde(0, new TodasTurmasFragment(), "Turmas");
                }
            }

            @Override
            public void onFailure(Call<TurmaData> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Erro ao achar a turma!",
                        Toast.LENGTH_SHORT).show();

                communicator.responde(0, new TodasTurmasFragment(), "Turmas");
            }
        });

    }

    private void setTurma(final TurmaData turma){

        nome.setText(turma.getProfessor() + "( " + turma.getDisciplina() + " )");

        if(turma.getReserva1() == null && turma.getReserva2() == null){
            reserva1.setText("Não possuí maiores detalhes");
        }else{
            if(turma.getReserva1() != null) {
                reserva1.setText(turma.getReserva1().toStringTurma());
            }
            if(turma.getReserva2() != null) {
                reserva2.setText(turma.getReserva2().toStringTurma());
            }
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.USER_KEY, MODE_PRIVATE);
        String usuarioJson = sharedPreferences.getString(Config.USER_KEY, "");

        Gson gson = new Gson();
        UsuarioData usuarioLogado = gson.fromJson(usuarioJson, UsuarioData.class);

        if(usuarioLogado.getTurmas().contains(turma.getId())){
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.MULTIPLY);
            entrarButton.getBackground().setColorFilter(colorFilter);
            entrarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "Você já está cadastrado nesta turma!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.parseColor("#551A8B"), PorterDuff.Mode.MULTIPLY);
            entrarButton.getBackground().setColorFilter(colorFilter);
            entrarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entrarTurma(turma);
                }
            });
        }

    }

    private void entrarTurma(final TurmaData turma){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.TOKEN_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.TOKEN_KEY, "");

        UsuarioService usuarioService =
                ServiceGenerator.createService(UsuarioService.class, token);

        Call<MensagemRetorno>call = usuarioService.entrarTurma(turma.getId());
        call.enqueue(new Callback<MensagemRetorno>() {
            @Override
            public void onResponse(Call<MensagemRetorno> call, Response<MensagemRetorno> response) {
                if(response.isSuccessful() && response.body().getMensagem().equals(Config.SUCESSO_CADASTRO_USUARIO)){
                    Toast.makeText(getContext(),
                            response.body().getMensagem(),
                            Toast.LENGTH_SHORT).show();
                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.MULTIPLY);
                    entrarButton.getBackground().setColorFilter(colorFilter);
                    entrarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(),
                                    "Você já está cadastrado nesta turma!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(getContext(),
                            "Não foi possível entrar na turma!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MensagemRetorno> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Não foi possível entrar na turma!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
