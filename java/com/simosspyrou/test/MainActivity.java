package com.simosspyrou.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    //Admob id: ca-app-pub-9288806676990528~4457876069
    private AdView mAdView;
    Animation uptodown;
    Animation fade_in;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Load διαφήμισης banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        final Button play_button = findViewById(R.id.button);
        final TextView how_to = findViewById(R.id.howto);
        play_button.setVisibility(View.INVISIBLE);
        how_to.setVisibility(View.INVISIBLE);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView logo = findViewById(R.id.logo);
        logo.setAnimation(uptodown);
        uptodown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                play_button.setVisibility(View.VISIBLE);
                how_to.setVisibility(View.VISIBLE);
                play_button.startAnimation(fade_in);
                how_to.startAnimation(fade_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SetUp.class);
                GlobalVars global = new GlobalVars();
                global.setPool(new SentencePool(MainActivity.this));

                startActivity(intent);
            }
        });

        how_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HowToPlayTabbed.class);
                startActivity(intent);
            }
        });

    }


}
