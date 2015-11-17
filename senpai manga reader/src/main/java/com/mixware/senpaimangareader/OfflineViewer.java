package com.mixware.senpaimangareader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixware.senpaimangareader.util.SystemUiHider;

import java.io.File;


public class OfflineViewer extends Activity implements MangaReader{
    private MangaPageViewAttacher mAttacher;
    private ImageView image;
    public Bitmap actual,anterior,siguiente;
    public int nActual;
    private File path;
    int nElemens;
    View mTopMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OFFLINE VIEWER","onCreate");
        setContentView(R.layout.activity_offline_viewer);
        nActual = 0;
        path = (File) getIntent().getSerializableExtra("capitulo");
        nElemens = path.listFiles().length;
        image = (ImageView) this.findViewById(R.id.offline_manga_page);
        mAttacher = new MangaPageViewAttacher(image,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                actual = loadFromDisk(0);
                runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       image.setImageBitmap(actual);
                       mAttacher.update();
                       actualizarTexto();
                   }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                siguiente = loadFromDisk(1);
            }
        }).start();


        mTopMenu = findViewById(R.id.manga_top);
        mTopMenu.setVisibility(View.INVISIBLE);
        mTopMenu.findViewById(R.id.backToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineViewer.this.finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    @Override
    public void showTopNavegationBar() {
        if(mTopMenu.getVisibility() == View.INVISIBLE) {
            mTopMenu.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTopMenu.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();
        }
        else
            mTopMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void nextImage() {
        if(this.siguiente != null) {
            nActual++;
            this.anterior = this.anterior.createBitmap(this.actual);
            this.actual = this.actual.createBitmap(this.siguiente);
            this.image.setImageBitmap(actual);
            mAttacher.update();
            if (nActual + 1 < nElemens)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        siguiente = loadFromDisk(nActual + 1);
                    }

                }).start();
            else
                this.siguiente = null;
        }
    }

    @Override
    public void previousImage() {
        if (anterior != null) {
            this.siguiente = siguiente.createBitmap(this.actual);
            this.actual = this.actual.createBitmap(this.anterior);
            nActual--;
            this.image.setImageBitmap(actual);
            mAttacher.update();
            if (nActual > 0) new Thread(new Runnable() {
                @Override
                public void run() {
                    anterior = loadFromDisk(nActual - 1);
                }

            }).start();
            else this.anterior = null;
        }
    }


    @Override
    public void actualizarTexto() {
        ((TextView)(mTopMenu.findViewById(R.id.pagdepag))).setText(nActual + " de " + (nElemens-1));
    }

    public Bitmap loadFromDisk(int idx) {
       return BitmapFactory.decodeFile(path.getAbsolutePath() + "/" + idx + ".snp");

    }
}
