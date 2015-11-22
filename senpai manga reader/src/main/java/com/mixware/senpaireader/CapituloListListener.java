package com.mixware.senpaireader;


import com.mixware.senpaireader.Model.Capitulo;

import java.io.Serializable;

/**
 * Created by pargon on 02/08/2014.
 */
public interface CapituloListListener {
    public void startReading(Class mClass,Serializable capitulo);
    public void startDownloadService(Class mClass,Capitulo c);
}
