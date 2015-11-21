package com.mixware.senpaimangareader2.scrappers;

import com.mixware.senpaimangareader2.CompareMangas;
import com.mixware.senpaimangareader2.Model.Capitulo;
import com.mixware.senpaimangareader2.Model.Manga;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pargon on 17/11/2015.
 */
public class MangaHere {
    private final static String url = "http://es.mangahere.co/mangalist/";
    private final static String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
    public static ArrayList<Manga> getMangas() throws IOException {

        ArrayList<Manga> mangas = new ArrayList<Manga>();
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get().normalise();
        Element el = doc.getElementsByClass("nopic_list").first();
        Element primeraColumna = el.getElementsByClass("list_manga").first();
        Element segundaColumna = el.getElementsByClass("list_manga").get(1);
        for(char a = 'A'; a <= 'Z'; a++) {
            el = primeraColumna.getElementById("tag_"+a);
            el = el != null ? el : segundaColumna.getElementById("tag_" + a);
            Elements elements = el.getElementsByTag("li");
            for(Element mElement : elements) {
                String linea = mElement.toString();
                String nombre = linea.split("rel=\"")[1].split("\"")[0];
                String enlace = linea.split("href=\"")[1].split("\"")[0];
                mangas.add(new Manga(enlace,nombre));
            }
        }
        for(char b = '0'; b < '9'; b++) {
            el = segundaColumna.getElementById("tag_" + b);
            Elements elements = el.getElementsByTag("li");
            for(Element mElement : elements) {
                String linea = mElement.toString();
                String nombre = linea.split("rel=\"")[1].split("\"")[0];
                String enlace = linea.split("href=\"")[1].split("\"")[0];
                mangas.add(new Manga(enlace,nombre));
            }
        }
        Collections.sort(mangas, new CompareMangas());
        return mangas;
    }

    public static ArrayList<Capitulo> getCapitulos(String url) throws IOException {
        ArrayList<Capitulo> mChaps = new ArrayList<Capitulo>();
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Element el = doc.getElementsByTag("section").get(2).getElementsByTag("ul").get(1);
        Elements elements = el.getElementsByTag("li");
        for(Element mElement : elements) {
            String str = mElement.toString();
            String enlace = str.split("href=\"")[1].split("\"")[0];
            String n_cap = enlace.split("/c")[1].split("/")[0];
            mChaps.add(new Capitulo("http://es.mangahere.co"+enlace,"Capítulo "+n_cap));
        }
        return mChaps;
    }
}
