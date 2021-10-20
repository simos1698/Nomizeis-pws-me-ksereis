package com.simosspyrou.test;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

class Ad extends Application {

    //test id: ca-app-pub-3940256099942544/1033173712
    //MONETIZED id: ca-app-pub-9288806676990528/9717709745

    private static InterstitialAd interstitialAd;

    public static void GenerateAd(Context context) {
        Log.i("Simos", "Φόρτωμα διαφήμσης...");
        MobileAds.initialize(context, "ca-app-pub-3940256099942544/1033173712");
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
    }

    public static InterstitialAd getAd() {
        return interstitialAd;
    }
}