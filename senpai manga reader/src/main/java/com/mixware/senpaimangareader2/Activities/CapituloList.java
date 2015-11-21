package com.mixware.senpaimangareader2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mixware.senpaimangareader2.CapituloListListener;
import com.mixware.senpaimangareader2.DownloadCapitulo;
import com.mixware.senpaimangareader2.Gets.getCapitulos;
import com.mixware.senpaimangareader2.Model.Capitulo;
import com.mixware.senpaimangareader2.Model.Manga;
import com.mixware.senpaimangareader2.R;
import com.mixware.senpaimangareader2.adapters.CapituloAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class CapituloList extends ActionBarActivity implements DownloadCapitulo,CapituloListListener {

    private CapituloAdapter mAdapter;
    Manga m;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m = (Manga) this.getIntent().getSerializableExtra("manga");
        CogerCapitulos();
        mAdapter = new CapituloAdapter(this,m,this);

        setContentView(R.layout.activity_capitulo_list);
        this.setTitle(m.getNombre());
        lv = (ListView) findViewById(R.id.listChap);

    }

    @Override
    public void CogerCapitulos() {
        getCapitulos task = new getCapitulos(this,m);
        task.execute("");
    }

    @Override
    public void addCapitulos (ArrayList<Capitulo> caps) {
        lv.setAdapter(mAdapter);
        mAdapter.addAll(caps);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.capitulo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id ==  R.id.action_settings) {
            return true;
        }
        if (id == R.id.actualizar) {
            Intent mIntent = new Intent(this,CapituloList.class);
            mIntent.putExtra("manga",m);
            startActivity(mIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPause(){
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter.writeReaded();
            }
        }).start();
    }

    @Override
    public void startReading(Class mClass, Serializable path) {
        Intent mIntent = new Intent(CapituloList.this,mClass);
        mIntent.putExtra("capitulo",path);
        startActivity(mIntent);
   }


    @Override
    public void startDownloadService(Class mClass, Capitulo c) {
        Intent mIntent = new Intent(CapituloList.this,mClass);
        mIntent.putExtra("manga",m);
        mIntent.putExtra("capitulo",c);
        startService(mIntent);

    }
}
