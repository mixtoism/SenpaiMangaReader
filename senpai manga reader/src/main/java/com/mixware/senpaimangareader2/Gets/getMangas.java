package com.mixware.senpaimangareader2.Gets;

import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaimangareader2.Activities.FullscreenActivity;
import com.mixware.senpaimangareader2.Model.Manga;
import com.mixware.senpaimangareader2.Utilidades;
import com.mixware.senpaimangareader2.scrappers.SubManga;

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
