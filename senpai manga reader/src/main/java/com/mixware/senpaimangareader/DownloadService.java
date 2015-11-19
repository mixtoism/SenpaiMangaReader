package com.mixware.senpaimangareader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mixware.senpaimangareader.Gets.getNumImagenes;
import com.mixware.senpaimangareader.Gets.getPagina;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class DownloadService extends Service {

    SharedPreferences preferences;

    private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager mNM;
    public static boolean serviceState=false;
    public ArrayList<String> enlaces;
    public getPagina getPagin[];
    public Manga manga;
    public Capitulo capitulo;
    public String path;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        Message msg;
        @Override
        public void handleMessage(Message msg) {
            this.msg = msg;
            downloadFile();
        }
        public void doFinish() {
            mBuilder.setProgress(0,0,false)
                    .setContentText("Descargado");
            Intent mIntent = new Intent(DownloadService.this,OfflineViewer.class);
            mIntent.putExtra("path",new File(path));

            mIntent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this.getBaseContext(), 0,
                    mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mNM.notify(capitulo.hashCode(),mBuilder.build());
            if(colaIntent.size() == 0) {
                running = false;
                stopSelf(msg.arg1);
            }
            else {
                running = false;
                contador = 0;
                comenzar(colaIntent.remove(0),colaFlags.remove(0),colaStartId.remove(0));
            }
        }
    }

    Boolean running;
    boolean waiting;
    @Override
    public void onCreate() {
        serviceState=true;
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        HandlerThread thread = new HandlerThread("ServiceStartArguments",1);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }


    ArrayList<Intent> colaIntent = new ArrayList<Intent>();
    ArrayList<Integer> colaFlags = new ArrayList<Integer>();
    ArrayList<Integer> colaStartId = new ArrayList<Integer>();

    public void comenzar(Intent intent,int flags,int startId) {
        running = true;

        Bundle extra = intent.getExtras();
        if(extra != null){
            manga = (Manga) intent.getSerializableExtra("manga");
            capitulo = (Capitulo) extra.getSerializable("capitulo");
            path = getExternalFilesDir(null).toString()+"/download/"+manga.getNombre()+"/"+capitulo.getCapitulo();

        }

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE-ONCOMMAND", "onStartCommand");
        if(running != null)
        {
            if(running) {
                NotificationCompat.Builder  mBuilder2 = new NotificationCompat.Builder(this);
                mBuilder2.setContentTitle(((Manga) intent.getSerializableExtra("manga")).getNombre() + " " +
                        ((Capitulo) intent.getSerializableExtra("capitulo")).getCapitulo())
                        .setContentText("En cola")
                        .setSmallIcon(R.drawable.ic_launcher);
                mNM.notify(((Capitulo) intent.getSerializableExtra("capitulo")).hashCode(),mBuilder2.build());

                colaIntent.add(intent);
                colaFlags.add(flags);
                colaStartId.add(startId);
                return START_STICKY;
            }
        }

        comenzar(intent,flags,startId);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }



    @Override
    public void onDestroy() {

        Log.d("SERVICE-DESTROY","DESTROY");
        serviceState=false;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void downloadFile(){
        File root =new File(path);
        if (!root.exists() || !root.isDirectory()) root.mkdirs();
        getNumImagenes getPag = new getNumImagenes(this.capitulo,DownloadService.this);
        getPag.execute("");
    }

    NotificationCompat.Builder mBuilder;

    public void showAndLoad(ArrayList<String> paginas, Bitmap imagen) {
        enlaces = paginas;
        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(manga.getNombre() + " " + capitulo.getCapitulo())
                .setContentText("Descargando")
                .setSmallIcon(R.drawable.ic_launcher);
        writeToDisk(imagen, 0);
         getPagin = new getPagina[paginas.size()];
            getPagin[1] = new getPagina(enlaces.get(1),DownloadService.this,1);
            getPagin[1].execute("");
        }

    public synchronized void nextImage(Bitmap imagen, int i) {
        if(imagen== null){
            getPagina gp = new getPagina(enlaces.get(i),DownloadService.this,i);
            gp.execute("");
        }
        else {
            writeToDisk(imagen, i);
            if(i+1<getPagin.length)(new getPagina(enlaces.get(i+1),DownloadService.this,i+1)).execute("");
            else mServiceHandler.doFinish();

        }
    }
    int contador=0;
    public void writeToDisk(Bitmap im, int i){
        contador++;
        String estado = Environment.getExternalStorageState();
        if(estado.equals(Environment.MEDIA_MOUNTED)) {
            OutputStream fOut = null;
            File file = new File(path, i+".snp");
            try {
                fOut = new FileOutputStream(file);
                im.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
                mBuilder.setProgress(enlaces.size(),contador,false);
                mNM.notify(capitulo.hashCode(),mBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
