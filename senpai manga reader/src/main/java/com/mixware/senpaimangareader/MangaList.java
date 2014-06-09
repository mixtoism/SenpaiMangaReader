package com.mixware.senpaimangareader;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.view.View.OnTouchListener;


public class MangaList extends ListActivity {

    private MangaAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Listado de Mangas");
        ArrayList<Manga> mangas = (ArrayList<Manga>) this.getIntent().getSerializableExtra("lista");
        setContentView(R.layout.activity_manga_list);
        mAdapter = new MangaAdapter(mangas,this);
        this.setListAdapter(mAdapter);
        ListView list = this.getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             CapitulosManga(i);
            }
        });
    }

    public void CapitulosManga(int i) {
       Log.i("","Entro en capitulosmanga");
        Manga m = (Manga) mAdapter.getItem(i);
       Intent mIntent = new Intent(this,CapituloList.class);
       mIntent.putExtra("manga",m);
       startActivity(mIntent);
    }

}
