package com.mixware.senpaimangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pargon on 08/06/2014.
 */
public class getPagina extends AsyncTask<String,String,String> {
    String url;
    Bitmap imagen;
    int i;
    MangaView mActivity;
    int cont;
    public getPagina(String nImagen, MangaView viewManga,int i) {
        url = nImagen;
        mActivity = viewManga;
        this.i = i;
        cont = 0;
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
            loadImage(conn.getInputStream());

        } catch (IOException e) {
            cont++;
            Log.i("getPagina TASK", "OCURRIO ALGUN ERROR con el hilo" + this.i + "Reintentando...");
            if(cont<4)doInBackground("");
        }
    return "";
    }
    private void loadImage(InputStream inputStream) throws OutOfMemoryError{
        try {
            imagen = BitmapFactory.decodeStream(inputStream);
        } catch (OutOfMemoryError e) {
            Log.i("IMAGEIO:", e.toString());
            mActivity.LiberarPrimera();
            loadImage(inputStream);
        }


    }

    protected void onPostExecute(String res) {
        mActivity.nextImage(imagen,i);
    }

}
