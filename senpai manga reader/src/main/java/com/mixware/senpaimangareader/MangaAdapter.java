package com.mixware.senpaimangareader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pargon on 08/06/2014.
 */
public class MangaAdapter extends BaseAdapter implements Filterable{
    Context mContext;
    ArrayList<Manga> mItems;
    ArrayList<Manga> hidden = new ArrayList<Manga>();
    String searchTerm;

    public MangaAdapter(ArrayList<Manga> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        searchTerm = "";
        hidden =(ArrayList<Manga>) mItems.clone();

    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)   mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.list_manga_item_view,null);
        TextView tv = (TextView) view.findViewById(R.id.manga_title);
        tv.setText(mItems.get(i).getNombre());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manga m = mItems.get(i);
                Intent mIntent = new Intent(mContext,CapituloList.class);
                mIntent.putExtra("manga",m);
                mContext.startActivity(mIntent);
            }
        });
        return view;
    }

    @Override
    public boolean isEmpty() {
        return mItems.isEmpty();
    }
    Filter newFilter;
    @Override
    public Filter getFilter() {
        if(newFilter == null) {
            newFilter = new Filter() {

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                    mItems = (ArrayList<Manga>)results.values;
                    notifyDataSetInvalidated();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    mItems = (ArrayList<Manga>)hidden.clone();
                    constraint = constraint.toString().toLowerCase();
                    ArrayList<Manga> filteredFriendList = new ArrayList<Manga>();
                    for(int i=0; i<mItems.size(); i++) {
                        Manga newFriend = mItems.get(i);
                        if(newFriend.getNombre().toLowerCase().contains(constraint)) {
                            filteredFriendList.add(newFriend);
                        }
                    }

                    FilterResults newFilterResults = new FilterResults();
                    newFilterResults.count = filteredFriendList.size();
                    newFilterResults.values = filteredFriendList;
                    return newFilterResults;
                }
            };
        }
        return newFilter;
    }




}


