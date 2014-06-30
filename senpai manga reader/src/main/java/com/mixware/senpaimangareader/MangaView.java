package com.mixware.senpaimangareader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixware.senpaimangareader.util.SystemUiHider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MangaView extends Activity implements MangaReader{


    private boolean finish = false;
    private int nActual;
    private Thread t;
    private getPagina pag[];
    private int numHilos = 0;
    private int maximo = -1;

    private ImageView imageView;
    private MangaPageViewAttacher mAttacher;
    private ArrayList<String> enlaces;

    private Bitmap siguiente,actual,anterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent origin = this.getIntent();
        Capitulo chap = (Capitulo) origin.getSerializableExtra("capitulo");
        getNumImagenes task = new getNumImagenes(chap,this);
        task.execute("");

        setContentView(R.layout.activity_manga_view);
        findViewById(R.id.manga_top).setVisibility(View.INVISIBLE);
        findViewById(R.id.manga_top).findViewById(R.id.backToMenu).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dofinish();
                return true;
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new MangaPageViewAttacher(imageView,this);
    }



    private void dofinish(){
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    /**
     * It is called when the first image is called
     * @param enlaces Enlaces a imagenes
     * @param imagen La primera imagen
     */
    public void showAndLoad(ArrayList<String> enlaces, Bitmap imagen) {
        nActual = 0;
        this.imageView.setImageBitmap(imagen);
        imageView.invalidate();
        mAttacher.update();
        actual = imagen;
        this.enlaces = enlaces;
        pag = new getPagina[enlaces.size()];
        actualizarTexto();
        lanzarHilo(1);

       t = new Thread(r);
       t.start();
    }

    /**
     * Stores the image
     * @param imagen la i-esima imagen
     * @param i index
     */
    public synchronized void nextImage(final Bitmap imagen, final int i) {
        if(imagen == null) {
            pag[i] = new getPagina(enlaces.get(i),this,i);
            pag[i].execute();
            return;
        } //Reset the thread, if it have failed
        if(i == 1) {
            siguiente = imagen;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeimageToDisk(imagen, i);
            }
        }).start();

        numHilos++;
        maximo = maximo > i ? maximo : i;

    }
    public final int hilosMAX = 3;

    public Runnable r = new Runnable() {
        @Override
        public void run() {
            while(!finish)
                if(numHilos == hilosMAX) {
                    numHilos = 0;
                    lanzarHilo(maximo+1);
                }
        }
    };

    /**
     * Lanza los siguientes cinco hilos
     * @param ini inicio
     */
    public void lanzarHilo(int ini) {
        for(int i = ini; (i < ini + hilosMAX) &&( i < enlaces.size());i++) {
            Log.i("Lanzo",""+i);
            pag[i] = new getPagina(this.enlaces.get(i),this,i);
            pag[i].execute("");
            if(i == enlaces.size()-1) finish = true;

        }
    }


    public void nextImage() {

        if(this.siguiente != null) {
            nActual++;
            this.anterior = Bitmap.createBitmap(this.actual);
            this.actual = Bitmap.createBitmap(this.siguiente);
            this.imageView.setImageBitmap(actual);
            mAttacher.update();
            if (nActual + 1 < enlaces.size())
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        siguiente = readImageFromDisk(nActual + 1);
                    }

                }).start();
            else
                this.siguiente = null;
        }
    }


    public void previousImage() {
        if (anterior != null) {
            this.siguiente = Bitmap.createBitmap(this.actual);
            this.actual = Bitmap.createBitmap(this.anterior);
            nActual--;
            this.imageView.setImageBitmap(actual);
            mAttacher.update();
            if (nActual > 0) new Thread(new Runnable() {
                @Override
                public void run() {
                    anterior = readImageFromDisk(nActual - 1);
                }

            }).start();
            else this.anterior = null;
        }
    }

    public void actualizarTexto() {
        ((TextView)(findViewById(R.id.manga_top).findViewById(R.id.pagdepag))).setText(nActual + " de " + enlaces.size());
    }



    public void writeimageToDisk(Bitmap image,int idx) {

        String estado = Environment.getExternalStorageState();

         if(estado.equals(Environment.MEDIA_MOUNTED)) {
             try {
             File f = getExternalFilesDir(null);
             if(f != null) {
                 String path = f.toString();

                 OutputStream fOut;
                 File file = new File(path, idx + ".snp");

                 fOut = new FileOutputStream(file);
                 image.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                 fOut.flush();
                 fOut.close();
             }

             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
    }



    public Bitmap readImageFromDisk (int idx) {

        String estado = Environment.getExternalStorageState();
        Bitmap bitmap = null;
        if(estado.equals(Environment.MEDIA_MOUNTED) || estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            File ruta_sd = getExternalFilesDir(null);
            if(ruta_sd != null)
            bitmap = BitmapFactory.decodeFile(ruta_sd.getAbsolutePath()+"/"+idx+".snp");

        }
        return bitmap;
    }

    public void showTopNavegationBar() {
        final View v = findViewById(R.id.manga_top);
        if(v.getVisibility()==View.INVISIBLE) {
            v.setVisibility(View.VISIBLE); //wont hide on time
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            hideTopNavegationBar();
                        }
                    });
                }
            }).start();
        }
        else hideTopNavegationBar();
    }
    public void hideTopNavegationBar() {
        findViewById(R.id.manga_top).setVisibility(View.INVISIBLE);}


    @Override
    public void onStop() {
        super.onStop();

        for(int i = 0; i < pag.length; i++) {
            (new File(getExternalFilesDir(null),i+".snp")).delete();
            if(pag[i]!=null)pag[i].cancel(true);
        }
    }

}
