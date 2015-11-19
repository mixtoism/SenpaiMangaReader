package com.mixware.senpaimangareader2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mixware.senpaimangareader2.Gets.getMangas;

import java.util.ArrayList;


public class FullscreenActivity extends Activity {

    private getMangas getmangas;
    public boolean DEBUG = true;
    public boolean BETA_TEST = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getmangas = new getMangas(this);
        setContentView(R.layout.activity_fullscreen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getmangas.execute("");
    }


    public void nextActivity(final ArrayList<Manga> mangas) {
        if (mangas == null || mangas.isEmpty()) (new getMangas(this)).execute("");
        else {


            Intent mIntent;
            //May have problem with custom roms check in cyanogen
            mIntent = new Intent(FullscreenActivity.this, MangaList.class);
            mIntent.putExtra("lista", mangas);
            startActivity(mIntent);

        }
    }
}
