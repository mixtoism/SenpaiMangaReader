package com.mixware.senpaimangareader;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class MangaAdapter implements ListAdapter{
    Context mContext;
    ArrayList<Manga> mItems;
    ArrayList<Manga> hidden = new ArrayList<Manga>();
    String searchTerm;

    public MangaAdapter(ArrayList<Manga> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        searchTerm = "";
        for(Manga m : mItems) hidden.add(m);

    }

    public void filter (String s) {
        mItems = new ArrayList<Manga>();
        for(Manga m : hidden) mItems.add(m);

            for (Manga m : mItems) {
                if (!m.getNombre().contains(s)) {
                    mItems.remove(m);
                }
            }
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
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(mContext);
        tv.setText(mItems.get(i).getNombre());
        return tv;
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
}
