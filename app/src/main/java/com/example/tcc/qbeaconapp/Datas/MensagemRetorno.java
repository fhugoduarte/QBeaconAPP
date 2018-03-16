package com.example.tcc.qbeaconapp.Datas;

/**
 * Created by hugoduarte on 16/03/18.
 */

public class MensagemRetorno {

    private String mensagem;

    public MensagemRetorno(){
    }

    public MensagemRetorno(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
