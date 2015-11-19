package com.mixware.senpaimangareader2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mixware.senpaimangareader2.adapters.MangaAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class MangaList extends ActionBarActivity implements MangaListListener{

    private MangaAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Listado de Mangas");
        SharedPreferences sp;
        ArrayList<Manga> mangas = null;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        int font = Integer.parseInt(sp.getString("source","1"));

            String path = getExternalFilesDir(null)+"/mangas.dat";
            if( (new File(path)).exists()) { // if already has an offline copy
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(path));
                    mangas = (ArrayList<Manga>)ois.readObject();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        setContentView(R.layout.activity_manga_list);
        mAdapter = new MangaAdapter(mangas,this,this);

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

    @Override
    public void mangaSelected(Manga m) {
        Intent mIntent = new Intent(MangaList.this,CapituloList.class);
        mIntent.putExtra("manga",m);
        startActivity(mIntent);

    }
}
