package com.mixware.senpaimangareader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class CapituloList extends ActionBarActivity {

    private CapituloAdapter mAdapter;
    Manga m;
    ListView lv;
    getPaginasCapitulos task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = (Manga) this.getIntent().getSerializableExtra("manga");
        mAdapter = new CapituloAdapter(this,m);

        setContentView(R.layout.activity_capitulo_list);
        this.setTitle(m.getNombre());
        task = new getPaginasCapitulos(this,m);
        task.execute("");
        lv = (ListView) findViewById(R.id.listChap);

    }


    public void CogerCapitulos(ArrayList<String> paginas) {
        if(paginas == null || paginas.isEmpty()) new getPaginasCapitulos(this,m).execute("");
        for(String s : paginas) {
            getCapitulos caps = new getCapitulos(this,s);
            caps.execute();
        }
        task.cancel(true);
    }

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

    /**
     * Le pasa los capitulos offline al adapter
     * @param nCaps chapter, only his name
     */
    public void CogerCapitulosOffline(ArrayList<String> nCaps) {
        ArrayList<Capitulo> mCaps = new ArrayList<Capitulo>();
        for(String s : nCaps) {
            mCaps.add(new Capitulo(null,s));
        }
        mAdapter.addAll(mCaps);
        lv.setAdapter(mAdapter);
    }
}
