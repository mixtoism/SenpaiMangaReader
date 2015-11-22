package com.mixware.senpaireader.Gets;

import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaireader.Activities.FullscreenActivity;
import com.mixware.senpaireader.Model.Manga;
import com.mixware.senpaireader.Utilidades;
import com.mixware.senpaireader.scrappers.SubManga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * Created by pargon on 08/06/2014.
 */
public class getMangas extends AsyncTask<String,String,String> {

    boolean DEBUG  = true;
    private FullscreenActivity mActivity;
    ArrayList<Manga> mangas;
    public static int font;
    int code;
    public getMangas(FullscreenActivity fa) {
        mActivity = fa;
        font = Utilidades.getSource(mActivity);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            mangas = new ArrayList<Manga>();
            String path = mActivity.getExternalFilesDir(null)+"/mangas.dat";
            if((new File(path)).exists()) { // if already has an offline copy
                code = 0;
            }
            else {
                if (font == 0)
                    mangas = SubManga.getMangas();
                //if (font == 1)
                    //mangas = Scrapper.getMangas();
                else //Valor por defecto
                    mangas = SubManga.getMangas();

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(mangas);
            }
        } catch (IOException ex) {
            code = 1;
            Log.i("getMangas TASK","ERROR DOWNLOADING");

        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        mActivity.nextActivity(code);
    }
}
