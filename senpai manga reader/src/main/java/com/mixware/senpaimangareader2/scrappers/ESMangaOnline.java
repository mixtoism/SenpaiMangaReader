package com.mixware.senpaimangareader2.scrappers;

import com.mixware.senpaimangareader2.Capitulo;
import com.mixware.senpaimangareader2.Manga;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pargon on 17/11/2015.
 */
public class ESMangaOnline {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
    private static final String url = "http://esmangaonline.com/lista-de-manga/";

    public static ArrayList<Manga> getMangas() throws IOException {
        ArrayList<Manga> mangas = new ArrayList<Manga>();
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Elements el = doc.getElementsByClass("det");
        for (Element element : el) {
            Element mElement = element.getElementsByClass("mng_det_pop").first();
            String a = mElement.toString();
            //a.split(" "); // {a,href,class,tittle}
            String href = a.split(" ")[1].substring(6);
            href = href.split("\"")[0];
            if (href.equals("")) break;

            String titulo = (a.split("title=\"")[1]).split("\"")[0];
            mangas.add(new Manga(href, titulo));
        }
        return mangas;
    }

    public static ArrayList<Capitulo> getCapitulos(String url) throws IOException {
        ArrayList<Capitulo> mChaps = new ArrayList<Capitulo>();
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
        return mChaps;
    }
}
