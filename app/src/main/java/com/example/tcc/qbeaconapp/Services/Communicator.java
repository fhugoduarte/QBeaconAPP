package com.example.tcc.qbeaconapp.Services;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by hugoduarte on 27/03/18.
 */

public interface Communicator {
    public void responde(int id, Fragment fragment, String titulo);

}
