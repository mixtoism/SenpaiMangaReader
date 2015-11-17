    package com.mixware.senpaimangareader;

    import android.app.Activity;
    import android.app.Fragment;
    import android.content.Context;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ListView;

    import java.io.Serializable;
    import java.util.ArrayList;

    /**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class capitulosFragment extends Fragment implements DownloadCapitulo, CapituloListListener {

    View v;
    Manga m;
    ListView lv;
    Context mContext;
    CapituloAdapter mAdapter;
    getPaginasCapitulos task;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCapituloSelected mListener;

    // TODO: Rename and change types of parameters
    public static capitulosFragment newInstance(String param1, String param2) {
        capitulosFragment fragment = new capitulosFragment();
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
    public capitulosFragment() {
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
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.capitulos_fragment_list,container,false);
        return v;

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCapituloSelected) activity;
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



    public void setManga(Manga m) {
        this.m = m;
        if(v != null && lv == null) {
            lv = (ListView) v.findViewById(R.id.fragmentCapituloList);
        }
        mAdapter = new CapituloAdapter(mContext,m,this);
        task = new  getPaginasCapitulos(this,m);
        task.doInBackground("");

    }

        @Override
        public Context getApplicationContext() {
           return mContext;
    }

        @Override
        public void CogerCapitulos(ArrayList<String> paginas) {
            if(paginas == null || paginas.isEmpty()) new getPaginasCapitulos(this,m).execute("");
            for(String s : paginas) {
                //getCapitulos caps = new getCapitulos(this,s);
                //caps.execute();
            }
            task.cancel(true);
        }

        @Override
        public void CogerCapitulosOffline(ArrayList<String> nCaps) {
            ArrayList<Capitulo> mCaps = new ArrayList<Capitulo>();

            for(String s : nCaps) {
                mCaps.add(new Capitulo(null, s));
            }
            mAdapter.addAll(mCaps);
            lv.setAdapter(mAdapter);

        }

        @Override
        public void addCapitulos(ArrayList<Capitulo> capitulos) {

           mAdapter.addAll(capitulos);
          lv.setAdapter(mAdapter);
        }

        @Override
        public void startReading(Class mClass, Serializable capitulo) {
            mListener.onStartReading(mClass, capitulo);
        }

        @Override
        public void startDownloadService(Class mClass, Capitulo c) {
            mListener.onStartDownloading(mClass, c, m);
        }


        public interface OnCapituloSelected {
        // TODO: Update argument type and name
        public void onStartDownloading(Class mClass,Capitulo c,Manga m);
        public void onStartReading(Class mClass,Serializable c);
    }


}
