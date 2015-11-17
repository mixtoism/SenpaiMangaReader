package com.mixware.senpaimangareader;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by pargon on 02/08/2014.
 */
public interface DownloadCapitulo {

    public Context getApplicationContext();
    public void CogerCapitulos(ArrayList<String> list);
    public void CogerCapitulosOffline(ArrayList<String> list);
    public void addCapitulos(ArrayList<Capitulo> capitulos);
    //public void capituloSelected(Capitulo capitulo,boolean availableOffline);
}
