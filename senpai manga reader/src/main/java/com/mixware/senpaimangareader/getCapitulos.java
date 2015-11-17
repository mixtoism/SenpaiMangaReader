package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaimangareader.scrappers.ESManga;
import com.mixware.senpaimangareader.scrappers.ESMangaOnline;
import com.mixware.senpaimangareader.scrappers.MangaHere;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pargon on 08/06/2014.
 */
public class getCapitulos extends AsyncTask<String,String,String>{
    private String url;
    CapituloList mActivity;
    public static  int font = getMangas.font;
    ArrayList<Capitulo> mChaps;
    public getCapitulos(CapituloList capituloList, String s) {
        url = s;
        mActivity = capituloList;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            mChaps = new ArrayList<Capitulo>();
            font = Utilidades.getSource(mActivity);

            switch (font) {
                case 0:
                    mChaps = ESMangaOnline.getCapitulos(url);
                    break;
                case 1:
                    mChaps = ESManga.getCapitulos(url);
                    break;
                case 2:
                    mChaps = MangaHere.getCapitulos(url);
                    break;
            }

        } catch (IOException ex) {
            Log.i("getCapitulos TASK","OCURRIO ALGUN ERROR buscando las paginas");
        }
        return null;
    }
    @Override
    protected void onPostExecute(String res) {
        if(mChaps != null) {
            Log.i("getMangas TASK","STARTING NEXT ACTIVITY");
            mActivity.addCapitulos(mChaps);

        }else;
    }






}
