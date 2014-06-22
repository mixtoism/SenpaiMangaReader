package com.mixware.senpaimangareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mixware.senpaimangareader.util.SystemUiHider;

import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    private getMangas getmangas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getmangas = new getMangas(this);
        setContentView(R.layout.activity_fullscreen);

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.getmangas.execute("");
    }


    public void nextActivity(ArrayList<Manga> mangas) {
        Intent mIntent = new Intent(this,MangaList.class);
        mIntent.putExtra("lista",mangas);

        startActivity(mIntent);
        this.getmangas.cancel(true);
    }
}
