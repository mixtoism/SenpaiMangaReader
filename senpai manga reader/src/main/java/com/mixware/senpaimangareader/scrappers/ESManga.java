package com.mixware.senpaimangareader.scrappers;

import com.mixware.senpaimangareader.Capitulo;
import com.mixware.senpaimangareader.CompareMangas;
import com.mixware.senpaimangareader.Manga;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by pargon on 17/11/2015.
 */
public class ESManga {
    public static final String USER_AGENT ="Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
    public static final String url = "http://esmanga.com/manga-list";
    public static ArrayList<Manga> getMangas() throws IOException {
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Element el = doc.getElementsByClass("pagination").first();
        el = el.getElementsByTag("ul").first();
        Elements e = el.getElementsByTag("a");
        el = e.get(e.size()-2);
        int n_pags = Integer.parseInt(el.toString().split("pag/")[1].split("\"")[0]);
        ArrayList<Manga> mangas = new ArrayList<Manga>();
        for(int j = 1; j <= n_pags; j++) {
            doc = Jsoup.connect(url+"/pag/"+j).userAgent(USER_AGENT).get();
            el = doc.getElementById("barra-principal");
            el = el.getElementsByClass("row").first();
            el = el.getElementsByTag("div").first();
            e = el.getElementsByTag("a");
            for(Element mElement : e) {
                String s = mElement.toString();
                String href = s.split("a href=\"")[1].split("\"")[0];
                String titulo = s.split("title=\"")[1].split("\"")[0];
                mangas.add(new Manga("http://esmanga.com/" + href,titulo));
            }
        }
        Collections.sort(mangas, new CompareMangas());
        return mangas;
    }

    public static ArrayList<Capitulo> getCapitulos(String url) throws IOException {
        ArrayList<Capitulo> mChaps = new ArrayList<Capitulo>();
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
        return mChaps;
    }

}
