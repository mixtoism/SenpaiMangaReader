package com.mixware.senpaireader.scrappers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mixware.senpaireader.Model.Capitulo;
import com.mixware.senpaireader.Model.Manga;
import com.mixware.senpaireader.Parallel.Parallel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author neluso
 */
public class SubManga {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";

    public static ArrayList<String> getPaginas(Capitulo c) throws IOException{
        ArrayList<String> paginas = new ArrayList<>();
        Document doc = Jsoup.connect(c.getEnlace()).get();
        Elements el = doc.getElementsByTag("select");
        el = el.get(0).getAllElements();
        String enlace = doc.getElementsByTag("img").get(3).toString();
        enlace = enlace.split("src=\"")[1].split("\"")[0];
        enlace = enlace.substring(0, enlace.length() - 5);
        for(int i=1; i<el.size();i++) {
            paginas.add(enlace + i + ".jpg");
        }
        return paginas;
    }
    public static ArrayList<Capitulo> getCapitulos(Manga m)throws IOException{
        final List cap = Collections.synchronizedList(new ArrayList<Capitulo>());
        Document doc = Jsoup.connect(m.getEnlace() + "/completa").userAgent(USER_AGENT).get();
        Elements el = doc.getElementsByClass("b468");
        Element mElement = el.get(0);
        mElement = mElement.getElementsByTag("table").get(0);
        el = mElement.getElementsByTag("td");
        Parallel.blockingFor(el, new Parallel.Operation<Element>() {
            @Override
            public void perform(Element mElement) {
                String linea = mElement.toString();
                try {
                    String enlace = linea.split("href=\"")[1].split("\"")[0];
                    String numero = enlace.split("/")[4];
                    enlace = "http://submanga.com/c/" + enlace.split("/")[5];
                    boolean check;
                    try {
                        Integer.parseInt(numero);
                        check = true;
                    } catch (NumberFormatException numEx) {
                        check = false;
                    }
                    if (check) {
                        Capitulo c = new Capitulo(enlace, numero);
                        cap.add(c);
                    }
                } catch (IndexOutOfBoundsException ex) {
                }
            }
        });

        ArrayList<Capitulo> c = new ArrayList<>();
        Collections.sort(cap, new Capitulo(null,null));
        c.addAll(cap);
        return c;
    }

    /**
     * Recoge los mangas de Submanga - version paralela
     * @return All mangas in web page
     * @throws IOException
     */
    public static ArrayList<Manga> getMangas() throws IOException {
        final List mangas =  Collections.synchronizedList( new ArrayList<Manga>(10000) );
        Document doc = Jsoup.connect("http://submanga.com/series").userAgent(USER_AGENT).get();
        Elements el = doc.getElementsByClass("b468");
        Element mElement = el.get(0);
        mElement = mElement.getElementsByTag("table").get(0);
        el = mElement.getElementsByTag("tr");

        Parallel.blockingFor(el, new Parallel.Operation<Element>() {
            @Override
            public void perform(Element mElement) {
                Elements el2 = mElement.getElementsByTag("a");
                if (!el2.isEmpty()) {
                    String linea = el2.first().toString();
                    String nombre = linea.split("/b> ")[1].split("</a")[0];
                    String enlace = linea.split("href=\"")[1].split("\"")[0];
                    Manga m = new Manga(enlace, nombre);
                    //Politica de Google, nada de pornografía
                    if (!nombre.toLowerCase().contains("hentai"))
                        mangas.add(m);
                }
            }
        });
        ArrayList<Manga> aL = (new ArrayList<Manga>());
        aL.addAll(mangas);
        return aL;
    }
}