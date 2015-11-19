package com.mixware.senpaimangareader2;

import java.util.Comparator;

/**
 * Created by pargon on 24/06/2014.
 */
public class CompareMangas implements Comparator<Manga> {
    @Override
    public int compare(Manga manga, Manga manga2) {
        return manga.getNombre().compareToIgnoreCase(manga2.getNombre());
    }
}
