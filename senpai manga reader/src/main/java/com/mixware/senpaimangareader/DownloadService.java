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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class DownloadService extends Service {

    SharedPreferences preferences;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager mNM;
    public static boolean serviceState=false;
    public ArrayList<String> enlaces[];
    public getPagina getPagin[][];
    public Manga manga[];
    public Capitulo capitulo[];
    public String path[];
    private static int MaxDescargas = 4;


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        Message msg;
        int id;
        @Override
        public void handleMessage(Message msg) {
            this.msg = msg;
            this.id=msg.arg2;
            downloadFile(id);
        }
        public void doFinish() {
            mBuilder.setProgress(0,0,false)
                    .setContentText("Descargado");
            Intent mIntent = new Intent(DownloadService.this,OfflineViewer.class);
            mIntent.putExtra("path",new File(path[id]));

            mIntent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this.getBaseContext(), 0,
                    mIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder.setContentIntent(contentIntent);
            mNM.notify(id,mBuilder.build());
            stopSelf(msg.arg1);
        }
    }


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


    int nHilos = 0;
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        Log.d("SERVICE-ONCOMMAND", "onStartCommand");
        synchronized (this) {
            manga = new Manga[MaxDescargas];
            capitulo = new Capitulo[MaxDescargas];
            path = new String[MaxDescargas];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int idHilo;
                    synchronized (this) {
                        idHilo = nHilos - 1;
                        Log.i("Nuevo",idHilo+"");
                    }
                    Bundle extra = intent.getExtras();
                    if (extra != null) {
                        manga[idHilo] = (Manga) extra.getSerializable("manga");
                        capitulo[idHilo] = (Capitulo) extra.getSerializable("capitulo");

                        path[idHilo] = getExternalFilesDir(null).toString() + "/download/" + manga[idHilo].getNombre() + "/" + capitulo[idHilo].getCapitulo();
                    }

                    Message msg = mServiceHandler.obtainMessage();
                    msg.arg1 = startId;
                    msg.arg2 = idHilo;
                    mServiceHandler.sendMessage(msg);

                }
            }).start();
        nHilos++;
        }
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }



    @Override
    public void onDestroy() {

        Log.d("SERVICE-DESTROY","DESTORY");
        serviceState=false;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void downloadFile(int id){

        File root =new File(path[id]);

        if (!root.exists() || !root.isDirectory()) root.mkdirs();
        getNumImagenes getPag = new getNumImagenes(this.capitulo[id],DownloadService.this,id);
        getPag.execute("");
    }



    NotificationCompat.Builder mBuilder;
    public void showAndLoad(ArrayList<String> paginas, Bitmap imagen,int id) {
        enlaces[id] = paginas;
        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(manga[id].getNombre() + " " + capitulo[id].getCapitulo())
                .setContentText("Descargando")
                .setSmallIcon(R.drawable.ic_launcher);
        writeToDisk(imagen, 0,id);
         getPagin = new getPagina[paginas.size()][MaxDescargas];
            getPagin[1][id] = new getPagina(enlaces[id].get(1),DownloadService.this,1,id);
            getPagin[1][id].execute("");
        }

    public synchronized void nextImage(Bitmap imagen, int i,int id) {
        if(imagen== null){
            getPagina gp = new getPagina(enlaces[id].get(i),DownloadService.this,i,id);
            gp.execute("");
        }
        else {
            writeToDisk(imagen, i,id);
            if(i+1<getPagin.length)(new getPagina(enlaces[id].get(i + 1),DownloadService.this,i+1,id)).execute("");
            else mServiceHandler.doFinish();

        }
    }

    public void writeToDisk(Bitmap im, int i,int id){
        String estado = Environment.getExternalStorageState();
        if(estado.equals(Environment.MEDIA_MOUNTED)) {
            OutputStream fOut = null;
            File file = new File(path[id], i+".snp");
            try {
                fOut = new FileOutputStream(file);
                im.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
                mBuilder.setProgress(enlaces[id].size(),i,false);
                mNM.notify(id,mBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
