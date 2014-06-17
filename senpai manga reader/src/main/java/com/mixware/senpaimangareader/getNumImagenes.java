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
    public getNumImagenes(Capitulo chap, MangaView m) {
        this.chap = chap;
        this.mActivity = m;
        downloadService = null;
    }

    public getNumImagenes(Capitulo chap, DownloadService downloadService) {
        this.chap = chap;
        this.downloadService = downloadService;
        this.mActivity = null;
    }
    @Override
    protected String doInBackground(String... strings) {
        paginas = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(chap.getEnlace()).get();
            Elements elements = doc.getElementsByClass("controls");
            for(Element el : elements) {
                Elements melements = el.getElementsByTag("a");
                for(Element melement : melements) {
                    String s = melement.toString().split("href=\"")[1].split("\"")[0];
                    if(paginas.contains(s))break;
                    paginas.add(s);
                }
            }
            Element element = doc.getElementsByTag("img").first();
            String img = element.toString().split("src=\"")[1].split("\"")[0];
            URL imageUrl = null;
            HttpURLConnection conn = null;
            imageUrl = new URL(img);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
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
}
