package com.example.tcc.qbeaconapp.Datas;

import java.util.List;

/**
 * Created by hugoduarte on 26/03/18.
 */

public class DisciplinaData {

    private Integer id;
    private String nome;
    private List<TurmaData> turmas;

    public DisciplinaData() {

    }

    public DisciplinaData(Integer id, String nome, List<TurmaData> turmas) {
        super();
        this.id = id;
        this.nome = nome;
        this.turmas = turmas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<TurmaData> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<TurmaData> turmas) {
        this.turmas = turmas;
    }

    @Override
    public String toString() {
        return nome;
    }
}
