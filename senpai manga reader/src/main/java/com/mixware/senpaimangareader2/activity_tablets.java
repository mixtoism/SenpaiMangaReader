package com.mixware.senpaimangareader2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mixware.senpaimangareader2.Model.Capitulo;
import com.mixware.senpaimangareader2.Model.Manga;

import java.io.Serializable;

public class activity_tablets extends ActionBarActivity implements mangasFragment.ListSelectionListener,capitulosFragment.OnCapituloSelected{

    private capitulosFragment capitulos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_tablets);
        capitulos = (capitulosFragment) getFragmentManager().findFragmentById(R.id.fragment2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_tablets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Manga manga) {
        capitulos.setManga(manga);
        //TODO: STABLISH COMUNICATION WITH CAPITULOS FRAGMENT
    }


    @Override
    public void onStartDownloading(Class mClass, Capitulo c, Manga m) {
        Intent mIntent = new Intent(activity_tablets.this,mClass);
        mIntent.putExtra("capitulo",c);
        mIntent.putExtra("manga",m);
        startService(mIntent);
    }

    @Override
    public void onStartReading(Class mClass, Serializable c) {
        Intent mIntent = new Intent(activity_tablets.this,mClass);
        mIntent.putExtra("capitulo",c);
        startActivity(mIntent);

    }
}

