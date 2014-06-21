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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class CapituloAdapter implements ListAdapter{
    Context mContext;
    Manga m;
    ArrayList<Capitulo> mItems;
    public boolean availableOffLine;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)   mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.list_manga_item_view,null);
        final TextView tv = (TextView) view.findViewById(R.id.nombre_manga);
        final Capitulo chap = (Capitulo) this.getItem(i);
        final ImageButton btnVisto = (ImageButton) view.findViewById(R.id.imageButton);
        final ImageButton btnBajar = (ImageButton) view.findViewById(R.id.imageButton2);
        final int nCap = i;
        String path = mContext.getExternalFilesDir(null)+"/download/"+m.getNombre()+"/"+ chap.getCapitulo();
        final File f = new File(path);
        final boolean[] availableOffLine = {(f.exists() && f.isDirectory())};
        if(availableOffLine[0])
            btnBajar.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_action_discard));

        final View finalView = view;
        finalView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (!availableOffLine[0]) {
                       Intent mIntent = new Intent(mContext, MangaView.class);
                       mIntent.putExtra("capitulo", chap);
                       mContext.startActivity(mIntent);
                   } else {
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               Intent mIntent = new Intent(mContext, OfflineViewer.class);
                               mIntent.putExtra("path", f);
                               mContext.startActivity(mIntent);
                           }
                       }).start();
                   }
               }
        });

        btnBajar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              if (!availableOffLine[0]) {
                  Toast.makeText(mContext, "Descargando", Toast.LENGTH_LONG).show();
                  Intent mIntent = new Intent(mContext, DownloadService.class);
                  mIntent.putExtra("manga", m);
                  mIntent.putExtra("capitulo", mItems.get(nCap));
                  mContext.startService(mIntent);
             } else {
                     Toast.makeText(mContext, "Eliminando", Toast.LENGTH_LONG).show();
                     new Thread(new Runnable() {

                                @Override
                                public void run() {
                               File files[] = f.listFiles();
                               for (int i = 0; i < files.length; i++) {
                                  files[i].delete();
                               }
                               f.delete();
                       }
                     }).start();
                     availableOffLine[0] = !availableOffLine[0];
                       btnBajar.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_download));
                  }
               }
           });

           btnVisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String path = mContext.getExternalFilesDir(null)+"/"+m.getNombre()+".dat";
                    final File f = new File(path);
                    if(!f.exists()){
                        try {
                            f.createNewFile();
                            FileOutputStream fos = new FileOutputStream(f);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            ArrayList<Capitulo> list = new ArrayList<Capitulo>();
                            list.add(mItems.get(i));
                            oos.writeObject(list);
                            btnVisto.setBackgroundColor(0xFF8800);
                            oos.close();
                            fos.close();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                     try {
                         FileInputStream fis = null;
                         fis = new FileInputStream(f);

                         ObjectInputStream ois = new ObjectInputStream(fis);
                         ArrayList<Capitulo> list = (ArrayList<Capitulo>) ois.readObject();
                         ois.close();
                         fis.close();
                         if(list.contains(mItems.get(i))){
                            list.remove(mItems.get(i));
                            btnVisto.setBackgroundColor(0xfccece);
                         }
                         else {
                            FileOutputStream fos = new FileOutputStream(f);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            list.add(mItems.get(i));
                            oos.writeObject(list);
                             oos.close();
                             fos.close();
                            btnVisto.setBackgroundColor(0xFF8800);
                         }
                    } catch (Exception e) {
                         e.printStackTrace();
                     }
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
