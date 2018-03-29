package com.example.tcc.qbeaconapp.Datas;

import java.util.List;

/**
 * Created by hugoduarte on 16/03/18.
 */

public class SalaData {

    private Integer id;
    private String nome;
    private String bloco;
    private String campus;
    private String instituicao;
    private Integer beacon;
    private List<ReservaData> reservas;

    public SalaData() {

    }

    public SalaData(Integer id, String nome, String bloco, String campus,
                    String instituicao, Integer beacon, List<ReservaData> reservas) {
        super();
        this.id = id;
        this.nome = nome;
        this.bloco = bloco;
        this.campus = campus;
        this.instituicao = instituicao;
        this.beacon = beacon;
        this.reservas = reservas;
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

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public Integer getBeacon() {
        return beacon;
    }

    public void setBeacon(Integer beacon) {
        this.beacon = beacon;
    }

    public List<ReservaData> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaData> reservas) {
        this.reservas = reservas;
    }

    @Override
    public String toString() {
        return  nome + " ( " + bloco + "/" + campus + "/" + instituicao + " )";
    }

}
