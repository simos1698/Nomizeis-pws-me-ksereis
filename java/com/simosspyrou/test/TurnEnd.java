package com.simosspyrou.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import static java.lang.System.arraycopy;
import static java.lang.System.console;

public class TurnEnd extends AppCompatActivity {
    public ArrayList<String> names_ordered;
    public ArrayList<Integer> scores_ordered;
    public ArrayList<String> names_original;
    public ArrayList<Integer> scores_original;
    public ArrayList<TextView> name_forms;
    public ArrayList<TextView> score_forms;
    public ArrayList<View> players;
    private int turns;
    private int turns_left;

    private InterstitialAd interstitialAd;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_end);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        interstitialAd = Ad.getAd();
        if (interstitialAd.isLoaded()){
            Log.i("Simos", "onCreate: exei fortwsei");
            interstitialAd.show();
        }

        //Αρχικοποίησεις
        names_original = new ArrayList<>();
        scores_original = new ArrayList<>();
        names_ordered = new ArrayList<>();
        scores_ordered = new ArrayList<>();
        name_forms = new ArrayList<>();
        score_forms = new ArrayList<>();
        players = new ArrayList<>();


        //---  Λήψη πληροφοριών  ---
        //#1 Αριθμός συνολικών σειρών
        //#2 Λίστα με τα ονόματα των παικτών
        //#3 Λίστα με τα σκορ των παικτών

        Intent intent = getIntent();
        turns = intent.getIntExtra("Nturns", 2);
        turns_left = intent.getIntExtra("Nturns_left", 0);
        names_original = intent.getStringArrayListExtra("players");
        scores_original = intent.getIntegerArrayListExtra("scores");
        for (int i = 0; i<= scores_original.size()-1; i++){
            scores_ordered.add(i, scores_original.get(i));
            names_ordered.add(i, names_original.get(i));
        }


        //Ταξινόμηση των ονομάτων με βάση τα σκόρ
        for (int i = 0; i<= scores_ordered.size()-2; i++){
            for (int j = i+1; j<= scores_ordered.size()-1; j++){
                if(scores_ordered.get(j)> scores_ordered.get(i)){
                    int temp1 = scores_ordered.get(i);
                    String temp2 = names_ordered.get(i);
                    scores_ordered.set(i, scores_ordered.get(j));
                    names_ordered.set(i, names_ordered.get(j));
                    scores_ordered.set(j, temp1);
                    names_ordered.set(j, temp2);
                }
            }
        }

        //Έυρεση των TextView με τα ονόματα
        name_forms = new ArrayList<>();
        name_forms.add((TextView) findViewById(R.id.name1));
        name_forms.add((TextView) findViewById(R.id.name2));
        name_forms.add((TextView) findViewById(R.id.name3));
        name_forms.add((TextView) findViewById(R.id.name4));
        name_forms.add((TextView) findViewById(R.id.name5));
        name_forms.add((TextView) findViewById(R.id.name6));
        name_forms.add((TextView) findViewById(R.id.name7));
        name_forms.add((TextView) findViewById(R.id.name8));

        //Έυρεση των TextView με τα σκορ
        score_forms = new ArrayList<>();
        score_forms.add((TextView) findViewById(R.id.score1));
        score_forms.add((TextView) findViewById(R.id.score2));
        score_forms.add((TextView) findViewById(R.id.score3));
        score_forms.add((TextView) findViewById(R.id.score4));
        score_forms.add((TextView) findViewById(R.id.score5));
        score_forms.add((TextView) findViewById(R.id.score6));
        score_forms.add((TextView) findViewById(R.id.score7));
        score_forms.add((TextView) findViewById(R.id.score8));

        //Έυρεση των View του κάθε παίκτη
        players = new ArrayList<>();
        players.add(findViewById(R.id.player1));
        players.add(findViewById(R.id.player2));
        players.add(findViewById(R.id.player3));
        players.add(findViewById(R.id.player4));
        players.add(findViewById(R.id.player5));
        players.add(findViewById(R.id.player6));
        players.add(findViewById(R.id.player7));
        players.add(findViewById(R.id.player8));

        for (View p: players){
            p.setVisibility(View.GONE);
        }

        //Καταχώρηση κειμένου στα textViews και εμφάνιση
        for (String n: names_ordered){
            TextView name_form = name_forms.get(names_ordered.indexOf(n));
            name_form.setText(n);
            TextView score_form = score_forms.get(names_ordered.indexOf(n));
            score_form.setText(String.valueOf(scores_ordered.get(names_ordered.indexOf(n))));
            players.get(names_ordered.indexOf(n)).setVisibility(View.VISIBLE);
        }


        Button next = findViewById(R.id.ok);

        //---  Λιστενερς  ---

        //Button Listener
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //εκκίνηση παιχνιδού με ίδιες εισόδους
                Intent new_gameplay = new Intent(TurnEnd.this, Gameplay.class);
                new_gameplay.putExtra("Nturns", turns);
                new_gameplay.putExtra("Nturns_left", turns_left - 1);
                new_gameplay.putStringArrayListExtra("players", names_original);
                new_gameplay.putIntegerArrayListExtra("scores", scores_original);

                startActivity(new_gameplay);
            }
        });


    }
}
