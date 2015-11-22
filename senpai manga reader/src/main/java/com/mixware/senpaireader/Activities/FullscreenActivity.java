package com.mixware.senpaireader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mixware.senpaireader.Gets.getMangas;
import com.mixware.senpaireader.R;


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


    public void nextActivity(int ret) {
        if (ret != 0)
            (new getMangas(this)).execute(""); //Error en la descarga
        Intent mIntent;
        mIntent = new Intent(FullscreenActivity.this, MangaList.class);
        startActivity(mIntent);
    }
}
