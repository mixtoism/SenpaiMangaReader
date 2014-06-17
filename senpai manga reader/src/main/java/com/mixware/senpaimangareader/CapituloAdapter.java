package com.mixware.senpaimangareader;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class CapituloAdapter implements ListAdapter{
    Context mContext;
    Manga m;
    ArrayList<Capitulo> mItems;

    public CapituloAdapter(Context mContext,Manga manga) {
        this.mItems = new ArrayList<Capitulo>();
        this.mContext = mContext;
        this.m = manga;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)   mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.list_manga_item_view,null);
        TextView tv = (TextView) view.findViewById(R.id.nombre_manga);
        final Capitulo chap = (Capitulo) this.getItem(i);
        ImageButton btnVisto = (ImageButton) view.findViewById(R.id.imageButton);
        ImageButton btnBajar = (ImageButton) view.findViewById(R.id.imageButton2);
        final int nCap = i;

        String path = mContext.getExternalFilesDir(null)+"/download/"+m.getNombre()+"/"+ chap.getCapitulo();
        final File f = new File(path);
        final boolean availableOffLine = (f.exists() && f.isDirectory());
        if(availableOffLine)
            btnVisto.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_action_discard));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!availableOffLine) {
                    Intent mIntent = new Intent(mContext, MangaView.class);
                    mIntent.putExtra("capitulo", chap);
                    mContext.startActivity(mIntent);
                }
                else {
                    Intent mIntent = new Intent(mContext,OfflineViewer.class);
                    mIntent.putExtra("path",f);
                }

            }
        });

        btnBajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Descargando",Toast.LENGTH_LONG).show();
                Intent mIntent = new Intent(mContext,DownloadService.class);
                mIntent.putExtra("manga",m);
                mIntent.putExtra("capitulo",mItems.get(nCap));
                mContext.startService(mIntent);
            }
        });

        btnVisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Button2 pressed",Toast.LENGTH_LONG).show();
            }
        });
        tv.setText(mItems.get(i).getCapitulo());
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    public boolean addAll(ArrayList<Capitulo> aL) {
       return this.mItems.addAll(aL);
    }
}
