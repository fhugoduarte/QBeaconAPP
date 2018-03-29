package com.example.tcc.qbeaconapp.Datas;

import android.content.ClipData;

/**
 * Created by hugoduarte on 29/03/18.
 */

public class ItemListView {

    private int id;
    private String titulo;
    private String texto;
    private int iconId;

    public ItemListView(){
        this( -1, "", "", -1);
    }

    public ItemListView(int id, String titulo, String texto, int iconId) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.iconId = iconId;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
