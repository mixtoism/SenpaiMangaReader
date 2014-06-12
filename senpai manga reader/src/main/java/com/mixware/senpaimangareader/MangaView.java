package com.mixware.senpaimangareader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
public class MangaView extends Activity {

    private ImageView imageView;
    MangaPageViewAttacher mAttacher;
    private ArrayList<String> enlaces;
    private Bitmap imagen[];

    GestureDetectorCompat mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_manga_view);
        findViewById(R.id.manga_top).setVisibility(View.INVISIBLE);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        imageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new MangaPageViewAttacher(imageView,this);
        Intent origin = this.getIntent();
        Capitulo chap = (Capitulo) origin.getSerializableExtra("capitulo");
        getNumImagenes task = new getNumImagenes(chap,this);
        task.execute("");
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    int actual;
    public void showAndLoad(ArrayList<String> enlaces, Bitmap imagen) {
        Log.i("",""+enlaces.size());
        actual = 0;
        this.imageView.setImageBitmap(imagen);
        imageView.invalidate();
        mAttacher.update();
        this.liberados = new boolean[enlaces.size()];
        this.imagen = new Bitmap[enlaces.size()];
        this.imagen[0] = imagen;
        for(int i = 1; i < enlaces.size(); i++) {
            this.imagen[i] = null;
        }
        this.enlaces = enlaces;
        pag = new getPagina[enlaces.size()];

        lanzarHilo(1);
       t = new Thread(r);
       t.start();
    }

    Thread t;
    getPagina pag[];
    int numHilos = 0;
    int maximo = -1;

    public synchronized void nextImage(Bitmap imagen,int i) {
        if(imagen == null) {pag[i] = new getPagina(enlaces.get(i),this,i);pag[i].execute();return;} //Reset the thread, if it have failed
        numHilos++;
        maximo = maximo > i ? maximo : i;
        Log.i("CONTROL DE HILOS","Entra el hilo "+i);
        this.imagen[i] = imagen;
        pag[i].cancel(true); // Close the thread
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
     * @param ini
     */
    public void lanzarHilo(int ini) {
        for(int i = ini; (i < ini + hilosMAX) &&( i < enlaces.size());i++) {
            Log.i("Lanzo",""+i);
            pag[i] = new getPagina(this.enlaces.get(i),this,i);
            pag[i].execute("");
            if(i == enlaces.size()-1) finish = true;

        }
    }
    boolean finish = false;

    private String DEBUG_TAG = "GESTURE DETECTION";

    public void nextImage() {
        if(actual + 1 < enlaces.size() && imagen[actual + 1] != null) {
            actual++;
            this.imageView.setImageBitmap(imagen[actual]);
        }
        else if(actual + 1 < enlaces.size() && liberados[actual +1]) {
            //TODO : Load the image from file
            LiberarPrimera(); //Free the first posible
            actual++;
            imagen[actual] = readImageFromDisk(actual);
            this.imageView.setImageBitmap(imagen[actual]);
        }
        mAttacher.update();
    }
    public void previousImage() {
        if(actual - 1 >= 0 && imagen[actual -1 ] != null) {
            actual--;
            this.imageView.setImageBitmap(imagen[actual]);
        }
        else if(actual - 1 >= 0 && liberados[actual - 1]) {
            liberarUltima(); // Free the last downloaded image
            actual--;
            imagen[actual] = readImageFromDisk(actual);
            this.imageView.setImageBitmap(imagen[actual]);
        }
        mAttacher.update();
    }

    /**
     * Frees memory for mangaReader
     * TODO : Write the image to a file, to be read without reconnecting
     *
     */
    public boolean liberados[];

    //These two methods are necessary because of the little ram that DalvikVM offers to android
    //They don't have to be invoked if ATR is used, but for now, they'll are essential
    /**
     * Saves the last downloaded image and frees the ram space
     * Marks it as liberada
     */
    public void liberarUltima() {
        int con = enlaces.size()-1;
        while(imagen[con] == null && con > actual) con--;
        writeimageToDisk(imagen[con],con);
        imagen[con].recycle();
        imagen[con] = null;
        liberados[con] = true;
    }

    /**
     * Saves the first downloaded image available and frees ram space
     */
    public void LiberarPrimera() {
        int con = 0;
        while(imagen[con] == null && con < actual) con++;
        writeimageToDisk(imagen[con],con);
        imagen[con].recycle();
        imagen[con] = null;
        liberados[con] = true;

    }

    public void writeimageToDisk(Bitmap image,int idx) {
        Log.i("Control de memoria","Entro a write image to disk");
        String estado = Environment.getExternalStorageState();
         if(estado.equals(Environment.MEDIA_MOUNTED)) {
             String path = getExternalFilesDir(null).toString();
             OutputStream fOut = null;
             File file = new File(path, idx+".snp");
             try {
                 fOut = new FileOutputStream(file);
                 image.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                 fOut.flush();
                 fOut.close();
                 Log.i("Control de memoria","Write To Disk");

             } catch (IOException e) {
                 e.printStackTrace();
             }


         }
        //TODO: Implement, and don't forget to add Android permission external storage
    }

    public Bitmap readImageFromDisk (int idx) {
        Log.i("Control de memoria","Entro a read image to disk");

        String estado = Environment.getExternalStorageState();
        Bitmap bitmap = null;
        if(estado.equals(Environment.MEDIA_MOUNTED) || estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            File ruta_sd = getExternalFilesDir(null);
            bitmap = BitmapFactory.decodeFile(ruta_sd.getAbsolutePath()+"/"+idx+".snp");
            Log.i("Control de memoria","Read from disk");
        }
        return bitmap;
    }

    public void showTopNavegationBar() {
        final View v = findViewById(R.id.manga_top);
        if(v.getVisibility()==View.INVISIBLE) {
            v.setVisibility(View.VISIBLE);
            new AsyncTask<String,String,String>() {
                @Override
                protected String doInBackground(String... strings) {
                    try {
                        wait(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                }
                protected void onPostExecute(String res) {
                    v.setVisibility(View.INVISIBLE);
                }
            };
        }
        else v.setVisibility(View.INVISIBLE);
    }

    public void onPause() {
        String estado = Environment.getExternalStorageState();

        for(int i = 0; i < pag.length; i++) {
            File f = new File(""); //TODO: finalize method
            pag[i].cancel(true);
        }

    }

}
