package com.mixware.senpaireader.Gets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaireader.Activities.MangaView;
import com.mixware.senpaireader.Utilidades;
import com.mixware.senpaireader.services.DownloadService;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask for fetching the image from the web page
 * Created by pargon on 08/06/2014.
 */
public class getPagina extends AsyncTask<String,String,String> {
    String url;
    Bitmap imagen;
    int i;
    byte buf [];
    MangaView mActivity;
    DownloadService mService;
    InputStream inputStream;

    public static final String USER_AGENT = "";
    public static int font = getMangas.font;
    public getPagina(String nImagen, MangaView viewManga, int i) {
        url = nImagen;
        mActivity = viewManga;
        mService = null;
        this.i = i;
        font = Utilidades.getSource(mActivity);
    }

    public getPagina(String nImagen,DownloadService service, int i) {
        this.url = nImagen;
        this.mService = service;
        mActivity = null;
        this.i = i;
        font = Utilidades.getSource(mService);
    }

    @Override
    protected String doInBackground(String... strings) {
        Document doc;
        try {
            URL imageUrl;
            HttpURLConnection conn;
            imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadImage(conn.getInputStream());
        } catch (IOException e) {

        }
    return "";
    }

    private void loadImage(InputStream inputStream){
        try {
            imagen = BitmapFactory.decodeStream(inputStream);
        } catch (OutOfMemoryError e) {
            Log.i("IMAGEIO:","Out of memory, this shouldn't happen!!");

            loadImage(inputStream);
        }
    }

    protected void onPostExecute(String res) {
        if (mActivity != null) {mActivity.nextImage(imagen,i); }
        else if(mService != null) { mService.nextImage(imagen,i);}
    }

}
