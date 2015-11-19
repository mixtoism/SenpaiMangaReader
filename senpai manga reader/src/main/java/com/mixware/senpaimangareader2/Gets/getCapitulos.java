package com.mixware.senpaimangareader2.Gets;

import android.os.AsyncTask;
import android.util.Log;

import com.mixware.senpaimangareader2.Capitulo;
import com.mixware.senpaimangareader2.CapituloList;
import com.mixware.senpaimangareader2.Manga;
import com.mixware.senpaimangareader2.Utilidades;
import com.mixware.senpaimangareader2.scrappers.SubManga;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class getCapitulos extends AsyncTask<String,String,String>{
    private String url;
    CapituloList mActivity;
    Boolean online;
    public static  int font = getMangas.font;
    ArrayList<Capitulo> mChaps;
    Manga m;
    public getCapitulos(CapituloList capituloList, Manga s) {
        url = s.getEnlace();
        m = s;
        mActivity = capituloList;
    }

    @Override
    protected String doInBackground(String... strings) {

        if(Utilidades.checkInternetConnection(mActivity.getApplicationContext())==Utilidades.TYPE_NOCONNECTION) {
            online = false;
            mChaps = modoOffline();
        }
        else {
            online = true;
            try {mChaps =  modoOnline();}
            catch (IOException e) {}
        }
        return null;
    }
    @Override
    protected void onPostExecute(String res) {
        if(mChaps != null) {
            Log.i("getMangas TASK","STARTING NEXT ACTIVITY");
            mActivity.addCapitulos(mChaps);

        }
    }
    private ArrayList<Capitulo> modoOnline() throws IOException{
        return SubManga.getCapitulos(m);
    }
    private ArrayList<Capitulo> modoOffline() {
        String path = mActivity.getApplicationContext().getExternalFilesDir(null) + "/download/"+m.getNombre()+"/";
        ArrayList<String> pags_cap = new ArrayList<>();
        File file = new File(path); //Is a directory
        if(file.isDirectory()) { // Should be redundant
            File files [] = file.listFiles();
            for (int i = 0; i < files.length; i++){
                if(file.isDirectory()) {
                    pags_cap.add(files[i].getName());
                }
            }
        }
        ArrayList<Capitulo> mCaps = new ArrayList<Capitulo>();
        for(String s : pags_cap) {
            mCaps.add(new Capitulo(null,s));
        }
        return mCaps;
    }






}
