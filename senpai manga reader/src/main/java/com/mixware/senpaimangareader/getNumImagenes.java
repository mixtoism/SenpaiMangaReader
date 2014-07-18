package com.mixware.senpaimangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class getNumImagenes extends AsyncTask<String,String,String>{
    ArrayList<String> paginas;
    Capitulo chap;
    MangaView mActivity;
    DownloadService downloadService;
    Bitmap imagen;
    public final static String USER_AGENT = getMangas.USER_AGENT;
    public final static int font = getMangas.font;
    int id;
    public getNumImagenes(Capitulo chap, MangaView m) {
        this.chap = chap;
        this.mActivity = m;
        downloadService = null;
    }

    public getNumImagenes(Capitulo chap, DownloadService downloadService) {
        this.chap = chap;
        this.downloadService = downloadService;
        this.mActivity = null;
        this.id = id;
    }
    @Override
    protected String doInBackground(String... strings) {
        paginas = new ArrayList<String>();
        try {

            switch (font) {
                case 0:
                    getNumImagenes_EsMangaOnline();
                    break;
                case 1:
                    getNumImagenes_EsManga();
                    break;
                case 2:
                    getNumImagenes_EsMangaHere();
                    break;
            }
        } catch (IOException ex) {
            Log.i("getNumImagenes TASK", "OCURRIO ALGUN ERROR"+ex.toString());
            doInBackground("");
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("TASK",paginas.isEmpty()?"vacio" :"no vacio");
        if(mActivity != null) mActivity.showAndLoad(paginas,imagen);
        else if(downloadService != null) downloadService.showAndLoad(paginas,imagen);
    }

    public void getNumImagenes_EsMangaOnline() throws IOException {
        Document doc = Jsoup.connect(chap.getEnlace()).get();
        Elements elements = doc.getElementsByClass("controls");
        for (Element el : elements) {
            Elements melements = el.getElementsByTag("a");
            for (Element melement : melements) {
                String s = melement.toString().split("href=\"")[1].split("\"")[0];
                if (paginas.contains(s)) break;
                paginas.add(s);
            }
        }
        getImagen(doc);
    }

    public void getNumImagenes_EsManga() throws IOException {
        Document doc = Jsoup.connect(chap.getEnlace()).userAgent(USER_AGENT).get();
        Elements el = doc.getElementsByClass("tools-view");
        int n_pags = el.last().getElementsByTag("select").last().getElementsByTag("option").size();
        for(int i = 0; i < n_pags; i++) {
            paginas.add(chap.getEnlace()+(i+1)+".html");
        }
        getImagen(doc);
    }

    public void getNumImagenes_EsMangaHere() throws IOException {
        Document doc = Jsoup.connect(chap.getEnlace()).userAgent(USER_AGENT).get();
        Element el = doc.getElementsByTag("select").get(1);
        int n_pags = el.getElementsByTag("option").size();
        for(int i = 0; i < n_pags; i++) {
            paginas.add(chap.getEnlace() + (i + 1) + ".html");
        }
    }

    public void getImagen(Document doc) throws IOException {
        Element element = doc.getElementsByTag("img").get((font == 0 || font == 2 )? 0 : 1);
        String img = ((font == 0 || font == 2 ) ? "":"http://esmanga.com/" )+ element.toString().split("src=\"")[1].split("\"")[0];

        URL imageUrl = null;
        HttpURLConnection conn = null;
        imageUrl = new URL(img);
        conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        imagen = BitmapFactory.decodeStream(conn.getInputStream());
    }
}
