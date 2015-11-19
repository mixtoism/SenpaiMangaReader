package com.mixware.senpaimangareader2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mixware.senpaimangareader2.adapters.MangaAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class mangasFragment extends Fragment implements MangaListListener {
    ListView lv;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MangaAdapter mAdapter;
    private Context mContext;
    public View v;

    private ListSelectionListener mListener;

    // TODO: Rename and change types of parameters
    public static mangasFragment newInstance(String param1, String param2) {
        mangasFragment fragment = new mangasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public mangasFragment() {
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.manga_fragment,container,false);
        SharedPreferences sp;
        ArrayList<Manga> mangas = null;
        //mContext = getActivity().getApplicationContext();
        lv = (ListView) v.findViewById(R.id.fragmentMangaList);

        sp = PreferenceManager.getDefaultSharedPreferences(mContext); //Maybe
        int font = Integer.parseInt(sp.getString("source","1"));
        String path = mContext.getExternalFilesDir(null)+"/mangas.dat";
        if( (new File(path)).exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(path));
                mangas = (ArrayList<Manga>)ois.readObject();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        mAdapter = new MangaAdapter(mangas,mContext,this);

        // TODO: Change Adapter to display your content
        lv.setAdapter(mAdapter);
        return v;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ListSelectionListener) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void mangaSelected(Manga m) {
        mListener.onItemSelected(m);
    }


    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface ListSelectionListener {
        public void onItemSelected(Manga manga);
    }

}
