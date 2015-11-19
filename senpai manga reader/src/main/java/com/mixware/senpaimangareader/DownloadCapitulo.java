package com.mixware.senpaimangareader;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by pargon on 02/08/2014.
 */
public interface DownloadCapitulo {

    public Context getApplicationContext();
    public void CogerCapitulos();
    public void addCapitulos(ArrayList<Capitulo> capitulos);
}
