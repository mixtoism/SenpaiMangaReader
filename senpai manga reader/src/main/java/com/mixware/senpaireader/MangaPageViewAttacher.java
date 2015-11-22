package com.mixware.senpaireader;

import android.view.View;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by pargon on 11/06/2014.
 */
public class MangaPageViewAttacher extends PhotoViewAttacher {

    private MangaReader mangaView;
    public MangaPageViewAttacher(ImageView imageView, final MangaReader mangaView) {
        super(imageView);
        this.mangaView = mangaView;
        this.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mangaView.showTopNavegationBar();
            }
        });
        this.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                mangaView.showTopNavegationBar();
            }
        });
    }

    public void onFling(float startX, float startY, float velocityX,
                        float velocityY) {
        if(getScale() == getMinimumScale()) {
            if(velocityX > 0) mangaView.nextImage();
            else  mangaView.previousImage();
            mangaView.actualizarTexto();
        }
        else {
            super.onFling(startX,startY,velocityX,velocityY);
        }

    }
}
