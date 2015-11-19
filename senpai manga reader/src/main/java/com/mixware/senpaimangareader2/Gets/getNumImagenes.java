package com.mixware.senpaimangareader2.Gets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaimangareader2.Capitulo;
import com.mixware.senpaimangareader2.services.DownloadService;
import com.mixware.senpaimangareader2.MangaView;
import com.mixware.senpaimangareader2.scrappers.SubManga;


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
    public final static String USER_AGENT = "";
   // public static int font;
    int id;
    public getNumImagenes(Capitulo chap, MangaView m) {
        this.chap = chap;
        this.mActivity = m;
        downloadService = null;
       // font = Utilidades.getSource(mActivity);
    }

    public getNumImagenes(Capitulo chap, DownloadService downloadService) {
        this.chap = chap;
        this.downloadService = downloadService;
        this.mActivity = null;
       // font = Utilidades.getSource(downloadService);

    }
    @Override
    protected String doInBackground(String... strings) {
        paginas = new ArrayList<String>();
        try {
            paginas = SubManga.getPaginas(chap);
            getImagen(paginas.get(0));
        }  catch (IOException ex) {
            Log.i("getNumImagenes TASK", "OCURRIO ALGUN ERROR"+ex.toString());
            //doInBackground("");
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("TASK", paginas.isEmpty() ? "vacio" : "no vacio");
        if(mActivity != null) mActivity.showAndLoad(paginas,imagen);
        else if(downloadService != null) downloadService.showAndLoad(paginas,imagen);
    }





    public void getImagen(String link) throws IOException {

        URL imageUrl = null;
        HttpURLConnection conn = null;
        imageUrl = new URL(link);
        conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        imagen = BitmapFactory.decodeStream(conn.getInputStream());
    }
}
