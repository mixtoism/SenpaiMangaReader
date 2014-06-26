package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pargon on 08/06/2014.
 */
public class getMangas extends AsyncTask<String,String,String> {
    private static String url[] = {"http://esmangaonline.com/lista-de-manga/","http://esmanga.com/manga-list"};
    private FullscreenActivity mActivity;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
    ArrayList<Manga> mangas;
    static int font = 1;
    public getMangas(FullscreenActivity fa) {
        mActivity = fa;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            mangas = new ArrayList<Manga>();
            String path = mActivity.getExternalFilesDir(null)+"/mangas.dat";
            if((new File(path)).exists()) { // if already has an offline copy
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                mangas = (ArrayList<Manga>)ois.readObject();
            }
            else {
                switch (font) {
                    case 0:
                        getMangas_ESMangaOnline();
                        break;
                    case 1:
                        getMangas_EsManga();
                        break;
                    default:
                        break;
                }

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(mangas);
            }
        } catch (IOException ex) {
            mangas = null;
             Log.i("getMangas TASK","ERROR DOWNLOADING");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
            mActivity.nextActivity(mangas);
    }

    /**
     * Get the list of mangas from esmangaonline.com
     * TODO: check if it still works when page is online
     */
    private void getMangas_ESMangaOnline() throws IOException {
        Document doc = Jsoup.connect(url[0]).get();
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


    }

    /**
     * Get the list of mangas from esmanga.com
     */
    private void getMangas_EsManga() throws IOException {
        Document doc = Jsoup.connect(url[1]).userAgent(USER_AGENT).get();
        Element el = doc.getElementsByClass("pagination").first();
        el = el.getElementsByTag("ul").first();
        Elements e = el.getElementsByTag("a");
        el = e.get(e.size()-2);
        int n_pags = Integer.parseInt(el.toString().split("pag/")[1].split("\"")[0]);
        for(int j = 1; j <= n_pags; j++) {
            doc = Jsoup.connect(url[1]+"/pag/"+j).userAgent(USER_AGENT).get();
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
        Collections.sort(mangas,new CompareMangas());

    }
}
