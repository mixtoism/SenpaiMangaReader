package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class getPaginasCapitulos extends AsyncTask<String,String,String> {
    public final static String USER_AGENT = getMangas.USER_AGENT;
    public static final int font = getMangas.font;
    private CapituloList mCaps;
    private Manga m;
    private ArrayList<String> pags_cap;
    private Boolean online = null;

    public getPaginasCapitulos(CapituloList mCaps, Manga m) {
        this.mCaps = mCaps;
        this.m = m;
    }


    @Override
    protected String doInBackground(String... strings) {
        pags_cap = new ArrayList<String>();

        if(Utilidades.checkInternetConnection(mCaps.getApplicationContext())==Utilidades.TYPE_NOCONNECTION) {
            online = false;
            modoOffline();
        }
        else {
            online = true;
            modoOnLine();
        }
    return null;
    }

    @Override
    protected void onPostExecute(String res) {
        if(online) mCaps.CogerCapitulos(pags_cap);
           else mCaps.CogerCapitulosOffline(pags_cap);
    }

    /**
     * If you are connected to the internet, you can see all chapters and download them or view them offline
     */
    private void modoOnLine() {
        try{
            if(font == 0) {
                int num_pags = 1;
                String s_previo = "";
                String s = "";
                Document doc = Jsoup.connect(m.getEnlace()).get();
                Elements el = doc.getElementsByClass("pgg");

                Element first = el.first();
                if (first != null) {
                    Elements enlaces = first.getElementsByTag("li"); // las cuatro lineas, faltara parsearlas
                    for (Element elem : enlaces) {
                        s = elem.getElementsByTag("a").toString();
                        if (!s.equals("")) {
                            s = (s.split("href=\"")[1]).split("\"")[0];
                            if (pags_cap.contains(s)) break;
                            s_previo = s;
                            pags_cap.add(s); // Tenemos el enlace a la pagina de capitulos
                        }
                    }
                } else {
                    s = m.getEnlace() + "chapter-list/1/";
                    pags_cap.add(s);
                }
            }// Segunda tarea, obtencion del numero de paginas de capitulos que hay
            else { //Solo hay una pagina con el numero de capitulos
                pags_cap.add(m.getEnlace());
            }
        } catch (IOException ex) {
            Log.i("getPaginasCapitulo TASK","OCURRIO ALGUN TIPO DE ERROR"+ex.getMessage());
        }
    }

    /**
     * If you are offline you can still watch the chapters you have downloaded
     */
    private void modoOffline() {
        String path = mCaps.getApplicationContext().getExternalFilesDir(null) + "/download/"+m.getNombre()+"/";

        File file = new File(path); //Is a directory
        if(file.isDirectory()) { // Should be redundant
            File files [] = file.listFiles();
            for (int i = 0; i < files.length; i++){
                if(file.isDirectory()) {
                    pags_cap.add(files[i].getName());
                }
            }
        }



    }
}
