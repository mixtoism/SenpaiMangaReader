package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

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
    public static final String USER_AGENT = getMangas.USER_AGENT;
    public static final int font = getMangas.font;
    ArrayList<Capitulo> mChaps;
    public getCapitulos(CapituloList capituloList, String s) {
        url = s;
        mActivity = capituloList;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            /**
             * Font 0 is ESMangaOnline, ahora está caída
             */
            if(font == 0) {
                mChaps = new ArrayList<Capitulo>();

                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByClass("con");
                Element element = elements.first();
                elements = element.getElementsByTag("ul");
                // System.out.println(elements);
                int x = elements.size() == 4 ? 2 : 1;
                element = elements.get(x);
                elements = element.getElementsByTag("a");
                for (Element e : elements) {
                    String s = e.toString();
                    s = s.split("href=\"")[1].split("\"")[0];
                    mChaps.add(new Capitulo(s));
                }
            }
            else {
                ArrayList<Capitulo> aL = new ArrayList<Capitulo>();

                Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
                Element el = doc.getElementById("capitulos");
                Elements e = el.getElementsByTag("a");
                Iterator<Element> iterator = e.iterator();
                while(iterator.hasNext()) {
                    el = iterator.next();
                    String href = el.toString().split("href=\"")[1].split("\"")[0];
                    String nombre ="Capítulo" + href.split("/c")[1].split("\"")[0];
                    href = "http://esmanga.com/" + href;
                    mChaps.add(new Capitulo(href,nombre));
                    iterator.next();
                    }
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
