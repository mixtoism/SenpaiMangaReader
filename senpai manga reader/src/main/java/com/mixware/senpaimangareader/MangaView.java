package com.mixware.senpaimangareader;

import com.mixware.senpaimangareader.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
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
public class MangaView extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
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
    MangaPageViewAttacher mAttacher;
    private ArrayList<String> enlaces;
    private Bitmap imagen[];

    GestureDetectorCompat mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGestureDetector= new GestureDetectorCompat(this,this);
        mGestureDetector.setOnDoubleTapListener(this);

        setContentView(R.layout.activity_manga_view);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        imageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new MangaPageViewAttacher(imageView,this);
        Intent origin = this.getIntent();
        Capitulo chap = (Capitulo) origin.getSerializableExtra("capitulo");
        getNumImagenes task = new getNumImagenes(chap,this);
        task.execute("");

     /**   this.imageView.setOnTouchListener(new ImageView.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        }); **/
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
        numHilos++;
        maximo = maximo > i ? maximo : i;
        Log.i("CONTROL DE HILOS","Entra el hilo "+i);
        this.imagen[i] = imagen;
        pag[i].cancel(true); // Close the thread
    }
    public Runnable r = new Runnable() {
        @Override
        public void run() {
            while(!finish)
                if(numHilos == 5) {
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
        for(int i = ini; (i < ini + 5) &&( i < enlaces.size());i++) {
            Log.i("Lanzo",""+i);
            pag[i] = new getPagina(this.enlaces.get(i),this,i);
            pag[i].execute("");
            if(i == enlaces.size()-1) finish = true;

        }
    }
    boolean finish = false;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mGestureDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
    private String DEBUG_TAG = "GESTURE DETECTION";

    public void nextImage() {
        if(actual + 1 < enlaces.size() && imagen[actual + 1] != null) {
            actual++;
            this.imageView.setImageBitmap(imagen[actual]);
            mAttacher.update();
        }
    }
    public void previousImage() {
        if(actual - 1 >= 0 && imagen[actual -1 ] != null) {
            actual--;
            this.imageView.setImageBitmap(imagen[actual]);
            mAttacher.update();
        }
    }

}
