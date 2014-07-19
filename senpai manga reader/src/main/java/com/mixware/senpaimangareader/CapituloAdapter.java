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
 * NOTE: Ultra patched version. Not sure how it works but it does
 */
public class CapituloAdapter implements ListAdapter{
    Context mContext;
    Manga m;
    ArrayList<Capitulo> mItems;
    ArrayList<Capitulo> readed = new ArrayList<Capitulo>();
    String path;

    public CapituloAdapter(Context mContext,Manga manga) {
        this.mItems = new ArrayList<Capitulo>();
        this.mContext = mContext;
        this.m = manga;
        path = mContext.getExternalFilesDir(null)+ "/" + m.getNombre()+".data";
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(path);
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(f.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(f);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        ArrayList<Capitulo> readed2 = (ArrayList<Capitulo>) ois.readObject();
                        if(readed2 == null);
                        else readed = readed2;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

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
        view  = inflater.inflate(R.layout.list_capitulo_item_view,null);
        final TextView tv = (TextView) view.findViewById(R.id.nombre_manga);
        final Capitulo chap = (Capitulo) this.getItem(i);
        final ImageButton btnVisto = (ImageButton) view.findViewById(R.id.imageButton);

        final ImageButton btnBajar = (ImageButton) view.findViewById(R.id.imageButton2);
        final int nCap = i;

        String path = mContext.getExternalFilesDir(null)+"/download/"+m.getNombre()+"/"+ chap.getCapitulo();
        final File f = new File(path);
        final boolean[] availableOffLine = {(f.exists() && f.isDirectory())};
        if(availableOffLine[0])
            btnBajar.setBackground(mContext.getResources().getDrawable(R.drawable.ic_action_discard));
        btnVisto.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), chap.isReaded() ? R.drawable.ic_action_accept_marked : R.drawable.ic_action_accept));
        final View finalView = view;
        finalView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(!chap.isReaded()) {
                       chap.setReaded(true);
                       readed.add(chap);
                       btnVisto.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_accept_marked));
                   }


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
                  if(Utilidades.checkInternetConnection(mContext)== Utilidades.TYPE_NOCONNECTION) {
                      Toast.makeText(mContext,"No puedes eliminar capitulos si no estás conectado",Toast.LENGTH_LONG).show();
                      //btnBajar.setClickable(false);
                  }
                  else {
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
         }
     });

            btnVisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(chap.isReaded()){
                     chap.setReaded(false);
                     readed.remove(chap);
                     btnVisto.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_accept));
                 }
                 else {
                     chap.setReaded(true);
                     readed.add(chap);
                     btnVisto.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_accept_marked));

                 }
                }
            });

        tv.setText(chap.getCapitulo());
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

    /**
     *
     * @param aL Capitulos a añadir
     * @return true if it could, false, otherwise
     * @version 2
     */
    public boolean addAll(ArrayList<Capitulo> aL) {
       for(Capitulo c: aL) {
           if (readed.contains(c)) {
               c.setReaded(true);
           }
       }
       return this.mItems.addAll(aL);
    }

    public void writeReaded() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File((path)));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(readed);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
