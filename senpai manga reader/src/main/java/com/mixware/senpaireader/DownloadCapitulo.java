package com.mixware.senpaireader;

import android.content.Context;

import com.mixware.senpaireader.Model.Capitulo;

import java.util.ArrayList;

/**
 * Created by pargon on 02/08/2014.
 */
public interface DownloadCapitulo {

    public Context getApplicationContext();
    public void CogerCapitulos();
    public void addCapitulos(ArrayList<Capitulo> capitulos);
}
