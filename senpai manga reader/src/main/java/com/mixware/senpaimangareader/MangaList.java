package com.mixware.senpaimangareader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class MangaList extends ActionBarActivity {

    private MangaAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Listado de Mangas");
        ArrayList<Manga> mangas = (ArrayList<Manga>) this.getIntent().getSerializableExtra("lista");
        setContentView(R.layout.activity_manga_list);
        mAdapter = new MangaAdapter(mangas,this);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CapitulosManga(i);
            }
        });
    }

    public void CapitulosManga(int i) {
        Manga m = (Manga) mAdapter.getItem(i);
        Intent mIntent = new Intent(this,CapituloList.class);
        mIntent.putExtra("manga",m);
        startActivity(mIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manga_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return onQueryTextChange(s);
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent mIntent = new Intent(MangaList.this,Settings.class);
            startActivity(mIntent);
            return true;
        }
        else if(id == R.id.actualizar) {
            File f = new File(getExternalFilesDir(null)+"/mangas.dat");
            f.delete();

            Intent mIntent = new Intent(MangaList.this,FullscreenActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
