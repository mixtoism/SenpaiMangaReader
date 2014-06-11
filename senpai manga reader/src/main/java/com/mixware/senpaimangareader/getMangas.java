package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by pargon on 08/06/2014.
 */
public class getMangas extends AsyncTask<String,String,String> {
    private static String url = "http://esmangaonline.com/lista-de-manga/";
    private FullscreenActivity mActivity;
    ArrayList<Manga> mangas;
    public getMangas(FullscreenActivity fa) {
        mActivity = fa;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            mangas = new ArrayList<Manga>();
            Document doc = Jsoup.connect(url).get();
            Elements el =doc.getElementsByClass("det");
            for( Element element : el) {
                Element mElement = element.getElementsByClass("mng_det_pop").first();
                String a = mElement.toString();
                //a.split(" "); // {a,href,class,tittle}
                String href = a.split(" ")[1].substring(6);
                href = href.split("\"")[0];
                if(href.equals("")) break;

                String titulo = (a.split("title=\"")[1]).split("\"")[0];
                mangas.add(new Manga(href,titulo));
                //Log.i("getMangas TASK","FILES DOWNLOADED");
            } //Hasta aqui, la obtencion del listado de mangas, Task 1
        } catch (IOException ex) {
            mangas = null;
            Log.i("getMangas TASK","ERROR DOWNLOADING");
            doInBackground("");
        }
        return null;    }

    @Override
    protected void onPostExecute(String res) {
        if(mangas != null) {
            Log.i("getMangas TASK","STARTING NEXT ACTIVITY");
            mActivity.nextActivity(mangas);

        }else;
    }
}
