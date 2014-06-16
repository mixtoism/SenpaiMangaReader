package com.mixware.senpaimangareader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CapituloList extends ActionBarActivity {

    private CapituloAdapter mAdapter;
    Manga m;
    ListView lv;
    getPaginasCapitulos task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CapituloAdapter(this);
        setContentView(R.layout.activity_capitulo_list);
        m = (Manga) this.getIntent().getSerializableExtra("manga");
        this.setTitle(m.getNombre());
        task = new getPaginasCapitulos(this,m);
        task.execute("");
        lv = (ListView) findViewById(R.id.listChap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CapituloList.this,"Pulsado",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void nextActivity(int i) {
        Capitulo chap = (Capitulo) mAdapter.getItem(i);
        Intent mIntent = new Intent(CapituloList.this, MangaView.class);
        mIntent.putExtra("capitulo", chap);
        startActivity(mIntent);
    }

    public void CogerCapitulos(ArrayList<String> paginas) {
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
