package com.mixware.senpaimangareader;

import com.mixware.senpaimangareader.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MangaView extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private ImageView imageView;
    PhotoViewAttacher mAttacher;
    private ArrayList<String> enlaces;
    private Bitmap imagen;
    public int nImagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manga_view);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        imageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new PhotoViewAttacher(imageView);
        Intent origin = this.getIntent();
        Capitulo chap = (Capitulo) origin.getSerializableExtra("capitulo");
        getNumImagenes task = new getNumImagenes(chap,this);
        task.execute("");

        this.imageView.setOnTouchListener(new ImageView.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("", "ENTRO AL EVENTO");

                if(imagenCargada) {
                    imagenCargada = false;
                    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(imagen);
                    imageView.invalidate();
                    mAttacher.update();
                    arrancarEvento();
                }
                return true;
            }
        });
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    private void arrancarEvento() {
        Log.i("Enlace",enlaces.get(nImagen));
        getPagina pag = new getPagina(enlaces.get(nImagen),this);
        pag.execute("");
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    public void showAndLoad(ArrayList<String> enlaces,Bitmap imagen) {
        nImagen = 2;
        Log.i("",""+enlaces.size());
        this.imageView.setImageBitmap(imagen);
        imageView.invalidate();
        mAttacher.update();
        this.enlaces = enlaces;
        getPagina pag = new getPagina(this.enlaces.get(nImagen),this);
        pag.execute("");
    }
    boolean imagenCargada = false;
    public void nextImage(Bitmap imagen) {
        if(nImagen < enlaces.size() ) {
            nImagen++;
            this.imagen = imagen;
            imagenCargada = true;
        }
    }
}
