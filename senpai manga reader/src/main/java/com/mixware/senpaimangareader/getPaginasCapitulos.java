package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class getPaginasCapitulos extends AsyncTask<String,String,String> {
    public final static String USER_AGENT = getMangas.USER_AGENT;
    public static final int font = getMangas.font;
    public getPaginasCapitulos(CapituloList mCaps, Manga m) {
        this.mCaps = mCaps;
        this.m = m;
    }

    private CapituloList mCaps;
    private Manga m;
    private ArrayList<String> pags_cap;
    @Override
    protected String doInBackground(String... strings) {
     try{
         pags_cap = new ArrayList<String>();

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
         else {
            pags_cap.add(m.getEnlace());
        }
    } catch (IOException ex) {
        Log.i("getPaginasCapitulo TASK","OCURRIO ALGUN TIPO DE ERROR"+ex.getMessage());
    }
    return null;
    }
    @Override
    protected void onPostExecute(String res) {
        if(pags_cap != null) {
            Log.i("getPaginasCapitulo TASK","STARTING NEXT ACTIVITY");
            mCaps.CogerCapitulos(pags_cap);

        }else;
    }
}
