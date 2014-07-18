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
            mChaps = new ArrayList<Capitulo>();

            switch (font) {
                case 0:
                    getCapitulos_EsMangaOnline();
                    break;
                case 1:
                    getCapitulos_EsManga();
                    break;
                case 2:
                    getCapitulos_EsMangaHere();
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

    public void getCapitulos_EsMangaOnline() throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("con");
        Element element = elements.first();
        elements = element.getElementsByTag("ul");
        int x = elements.size() == 4 ? 2 : 1;
        element = elements.get(x);
        elements = element.getElementsByTag("a");
        for (Element e : elements) {
            String s = e.toString();
            s = s.split("href=\"")[1].split("\"")[0];
            mChaps.add(new Capitulo(s));
        }
    }

    public void getCapitulos_EsManga() throws IOException {
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Element el = doc.getElementById("capitulos");
        Elements e = el.getElementsByTag("a");
        Iterator<Element> iterator = e.iterator();
        while(iterator.hasNext()) {
            el = iterator.next();
            String href = el.toString().split("href=\"")[1].split("\"")[0];
            String nombre ="Capítulo " + href.split("/c")[1].split("\"")[0].split("/")[0];
            mChaps.add(new Capitulo("http://esmanga.com/" + href,nombre));
            iterator.next();
        }
    }

    public void getCapitulos_EsMangaHere() throws IOException {
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Element el = doc.getElementsByTag("section").get(2).getElementsByTag("ul").get(1);
        Elements elements = el.getElementsByTag("li");
        for(Element mElement : elements) {
            String str = mElement.toString();
            String enlace = str.split("href=\"")[1].split("\"")[0];
            String n_cap = enlace.split("/c")[1];
            mChaps.add(new Capitulo("http://es.mangahere.co"+enlace,n_cap));
        }
    }
}
