package com.mixware.senpaimangareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class FullscreenActivity extends Activity {

    private getMangas getmangas;
    public boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getmangas = new getMangas(this);
        setContentView(R.layout.activity_fullscreen);
        if(DEBUG) try {
            File filename = new File(Environment.getExternalStorageDirectory()+"/logfile.txt");
            filename.createNewFile();
            String cmd = "logcat -d -f "+filename.getAbsolutePath();
      //      Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getmangas.execute("");
    }


    public void nextActivity(final ArrayList<Manga> mangas) {
        if (mangas == null || mangas.isEmpty()) (new getMangas(this)).execute("");
        else {

            //This is an intent to keep the UI working well
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent mIntent = new Intent(FullscreenActivity.this, MangaList.class);
                    mIntent.putExtra("lista", mangas);
                    startActivity(mIntent);
                }
            }).start();
        }
    }
}
