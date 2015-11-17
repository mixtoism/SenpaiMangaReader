package com.mixware.senpaimangareader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


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
