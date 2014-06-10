package com.mixware.senpaimangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pargon on 08/06/2014.
 */
public class getPagina extends AsyncTask<String,String,String> {
    String url;
    Bitmap imagen;
    MangaView mActivity;
    public getPagina(String nImagen, MangaView viewManga) {
        url = nImagen;
        mActivity = viewManga;
    }

    @Override
    protected String doInBackground(String... strings) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Element element = doc.getElementsByTag("img").first();
            String img = element.toString().split("src=\"")[1].split("\"")[0];
            URL imageUrl = null;
            HttpURLConnection conn = null;
            imageUrl = new URL(img);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (IOException e) {
            Log.i("getPagina TASK", "OCURRIO ALGUN ERROR");
        }
    return "";
    }

    protected void onPostExecute(String res) {
        mActivity.nextImage(imagen);
    }

}
