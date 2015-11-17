package com.mixware.senpaimangareader;

import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaimangareader.scrappers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * Created by pargon on 08/06/2014.
 */
public class getMangas extends AsyncTask<String,String,String> {


    private FullscreenActivity mActivity;
    ArrayList<Manga> mangas;
    public static int font;
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
                //ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                mangas.add(new Manga("",""));
            }
            else {
                 mangas = SubManga.getMangas();

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(mangas);
            }
        } catch (IOException ex) {
            mangas = null;
            ex.printStackTrace();
             Log.i("getMangas TASK","ERROR DOWNLOADING");

        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        mActivity.nextActivity(mangas);
    }
}
