package com.mixware.senpaimangareader2;


import com.mixware.senpaimangareader2.Model.Capitulo;

import java.io.Serializable;

/**
 * Created by pargon on 02/08/2014.
 */
public interface CapituloListListener {
    public void startReading(Class mClass,Serializable capitulo);
    public void startDownloadService(Class mClass,Capitulo c);
}
