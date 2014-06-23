package com.mixware.senpaimangareader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by pargon on 23/06/2014.
 */
public class Utilidades {
    public static final int TYPE_NOCONNECTION = 0x00000000;

    /**
     * Checks the network connection
     * @param mContext
     * @return @link(TYPE_NOCONNECTION)
     */
    public static int checkInternetConnection(Context mContext){
       ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo ni = cm.getActiveNetworkInfo();
       if (ni == null) return TYPE_NOCONNECTION;
       return ni.getType();
    }

    public static AlertDialog.Builder showConectionDialog(Activity mActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("La aplicación mucho tráfico de red, recomendamos el uso de redes wifi")
                .setTitle("Alerta sobre el uso de red")
                .setInverseBackgroundForced(true)
                .setNeutralButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder;

    }
}