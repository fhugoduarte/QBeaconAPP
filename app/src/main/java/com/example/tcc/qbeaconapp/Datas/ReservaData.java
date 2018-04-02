package com.example.tcc.qbeaconapp.Datas;


/**
 * Created by hugoduarte on 26/03/18.
 */

public class ReservaData {

    private Integer id;
    private String horario;
    private String sala;
    private String turma;

    public ReservaData() {

    }

    public ReservaData(Integer id, String horario, String sala, String turma) {
        super();
        this.id = id;
        this.horario = horario;
        this.sala = sala;
        this.turma = turma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String toStringTurma() {
        return "Sala: " + sala + "\n " + "Horario: " + horario;
    }

    public String toStringSala() {
        return "Horario: " + horario + "\n" + "Turma: " + turma;
    }

}
