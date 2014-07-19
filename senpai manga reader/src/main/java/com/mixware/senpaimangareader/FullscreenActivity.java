package com.mixware.senpaimangareader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class FullscreenActivity extends Activity {

    private getMangas getmangas;
    public boolean DEBUG = true;
    public boolean BETA_TEST = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getmangas = new getMangas(this);
        setContentView(R.layout.activity_fullscreen);
        if(DEBUG) try {
            File filename = new File(Environment.getExternalStorageDirectory()+"/logfile.txt");
            filename.createNewFile();
            String cmd = "logcat -d -f "+filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
            if (BETA_TEST) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Esta es una version de tester, solo podrá ser utilizada hasta " +
                        "15/08/2014, después en función de los reportes de usabilidad y mejoras que mandes" +
                        "se te concederá la version premium")
                        .setTitle("Senpai Manga Reader")
                        .setInverseBackgroundForced(true)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Calendar c = Calendar.getInstance();
                                int month = c.get(Calendar.MONTH);
                                if (c.get(Calendar.YEAR) == 2014)
                                    if (month == Calendar.AUGUST) {
                                        int day = c.get(Calendar.DAY_OF_MONTH);
                                        if (day < 15) getmangas.execute("");
                                    }
                                if (month < Calendar.AUGUST) {
                                    getmangas.execute("");
                                }

                            }
                        });
                builder.create().show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!BETA_TEST) getmangas.execute("");
    }


    public void nextActivity(final ArrayList<Manga> mangas) {
        if (mangas == null || mangas.isEmpty()) (new getMangas(this)).execute("");
        else {

            //This is an intent to keep the UI working well

                    Intent mIntent = new Intent(FullscreenActivity.this, MangaList.class);
//                    mIntent.putExtra("lista", mangas);


                    startActivity(mIntent);

        }
    }
}
