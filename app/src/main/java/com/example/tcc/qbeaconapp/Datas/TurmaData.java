package com.example.tcc.qbeaconapp.Datas;

/**
 * Created by hugoduarte on 26/03/18.
 */

public class TurmaData {

    private Integer id;
    private String professor;
    private String disciplina;
    private ReservaData reserva1;
    private ReservaData reserva2;

    public TurmaData() {

    }

    public TurmaData(Integer id, String professor, String disciplina, ReservaData reserva1, ReservaData reserva2) {
        super();
        this.id = id;
        this.professor = professor;
        this.disciplina = disciplina;
        this.reserva1 = reserva1;
        this.reserva2 = reserva2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public ReservaData getReserva1() {
        return reserva1;
    }

    public void setReserva1(ReservaData reserva1) {
        this.reserva1 = reserva1;
    }

    public ReservaData getReserva2() {
        return reserva2;
    }

    public void setReserva2(ReservaData reserva2) {
        this.reserva2 = reserva2;
    }

    @Override
    public String toString() {
        return professor + " - " + disciplina;
    }
}
