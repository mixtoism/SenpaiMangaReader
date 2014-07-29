package com.mixware.senpaimangareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class OnlineReaderAd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Capitulo c = (Capitulo) getIntent().getSerializableExtra("capitulo");
        setContentView(R.layout.activity_online_reader_ad);
        final InterstitialAd interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-2404835084618867/2386157681");
        AdRequest adRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                Intent mIntent = new Intent(OnlineReaderAd.this.getApplicationContext(),MangaView.class);
                mIntent.putExtra("capitulo",c);
                startActivity(mIntent);
            }
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.online_reader_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
