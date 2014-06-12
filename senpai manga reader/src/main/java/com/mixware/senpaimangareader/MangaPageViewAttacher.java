package com.mixware.senpaimangareader;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by pargon on 11/06/2014.
 */
public class MangaPageViewAttacher extends PhotoViewAttacher {

    private MangaView mangaView;
    public MangaPageViewAttacher(ImageView imageView,MangaView mangaView) {
        super(imageView);
        this.mangaView = mangaView;
    }

    public void onFling(float startX, float startY, float velocityX,
                        float velocityY) {
        if(getScale() == getMinimumScale()) {
            Log.i("MangaAttacher","ENTRO EN EL onFling");
            if(velocityX > 0) {
                mangaView.nextImage();
            }
            else {
                Log.i("MangaAttacher","Pues no, pero al menos entro");
                mangaView.previousImage();
            }
        }
        else {
            super.onFling(startX,startY,velocityX,velocityY);
        }

    }
    public boolean onTouch(View v,MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN)
            mangaView.showTopNavegationBar();
        return super.onTouch(v,e);
    }
}
