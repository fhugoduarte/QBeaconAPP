package com.example.tcc.qbeaconapp.Datas;

/**
 * Created by hugoduarte on 26/03/18.
 */

public class AuthToken {

    private String token;

    public AuthToken(){
    }

    public AuthToken(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
