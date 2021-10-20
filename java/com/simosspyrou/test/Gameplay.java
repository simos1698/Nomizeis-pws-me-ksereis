package com.simosspyrou.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.Serializable;
import java.util.ArrayList;

public class Gameplay extends AppCompatActivity implements Serializable {

    public ArrayList<Player> players = new ArrayList<>();
    public int turns_left;
    public int total_turns;
    public int currentTurnIndex;
    public ArrayList<Integer> playerScores;
    public int currentPlayerIndex;
    public Button ok;
    public ArrayList<Integer> selectionIndexes ;
    public TextView Swsto;

    private static InterstitialAd interstitialAd;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //---  Λήψη πληροφοριών  ---
        //#1 Αριθμός συνολικών σειρών
        //#2 Αριθμός σειρών που απομένουν
        //#3 Λίστα με τα ονόματα των παικτών
        //#4 Λίστα με τα σκορ των παικτών
        //#5 Ακέραιος με τον δείκτη του ποιος παίκτης παίζει


        final Intent intent = getIntent();
        total_turns = intent.getIntExtra("Nturns", 2);
        turns_left = intent.getIntExtra("Nturns_left", total_turns);
        final ArrayList<String> player_names = intent.getStringArrayListExtra("players");
        final ArrayList<Integer> player_scores = intent.getIntegerArrayListExtra("scores");
        currentPlayerIndex = intent.getIntExtra("playerIndex", 0);


        //---  Αρχικοποίηση  ---

        for (int i=0; i<=player_names.size()-1; i++) {
            players.add(new Player(player_names.get(i)));  //δημιουργία obj παικτών
            players.get(i).setScore(player_scores.get(i));
        }

        //δηλώσεις
        ArrayList<View> player_layouts = new ArrayList<>();
        final ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        final ArrayList<TextView> player_name_forms = new ArrayList<>();
        ArrayList<TextView> player1_yes_no = new ArrayList<>();
        ArrayList<TextView> player2_yes_no = new ArrayList<>();
        ArrayList<TextView> player3_yes_no = new ArrayList<>();
        ArrayList<TextView> player4_yes_no = new ArrayList<>();
        ArrayList<TextView> player5_yes_no = new ArrayList<>();
        ArrayList<TextView> player6_yes_no = new ArrayList<>();
        ArrayList<TextView> player7_yes_no = new ArrayList<>();
        final ArrayList<ArrayList> yes_no = new ArrayList<>();
        selectionIndexes = new ArrayList<>();
        findViewById(R.id.points).setVisibility(View.INVISIBLE);
        Swsto = findViewById(R.id.swsto);
        ok = findViewById(R.id.okbutton);
        ok.setEnabled(false);


        //--- Πίνακες views  ---
        //συνολική γραμμή παίκτη
        player_layouts.add(findViewById(R.id.player1));
        player_layouts.add(findViewById(R.id.player2));
        player_layouts.add(findViewById(R.id.player3));
        player_layouts.add(findViewById(R.id.player4));
        player_layouts.add(findViewById(R.id.player5));
        player_layouts.add(findViewById(R.id.player6));
        player_layouts.add(findViewById(R.id.player7));

        //textview με το όνομα
        player_name_forms.add((TextView) findViewById(R.id.name1));
        player_name_forms.add((TextView) findViewById(R.id.name2));
        player_name_forms.add((TextView) findViewById(R.id.name3));
        player_name_forms.add((TextView) findViewById(R.id.name4));
        player_name_forms.add((TextView) findViewById(R.id.name5));
        player_name_forms.add((TextView) findViewById(R.id.name6));
        player_name_forms.add((TextView) findViewById(R.id.name7));

        //textview με τις απαντήσεις ναι & οχι
        player1_yes_no.add((TextView) findViewById(R.id.yes1));
        player1_yes_no.add((TextView) findViewById(R.id.no1));
        player2_yes_no.add((TextView) findViewById(R.id.yes2));
        player2_yes_no.add((TextView) findViewById(R.id.no2));
        player3_yes_no.add((TextView) findViewById(R.id.yes3));
        player3_yes_no.add((TextView) findViewById(R.id.no3));
        player4_yes_no.add((TextView) findViewById(R.id.yes4));
        player4_yes_no.add((TextView) findViewById(R.id.no4));
        player5_yes_no.add((TextView) findViewById(R.id.yes5));
        player5_yes_no.add((TextView) findViewById(R.id.no5));
        player6_yes_no.add((TextView) findViewById(R.id.yes6));
        player6_yes_no.add((TextView) findViewById(R.id.no6));
        player7_yes_no.add((TextView) findViewById(R.id.yes7));
        player7_yes_no.add((TextView) findViewById(R.id.no7));

        yes_no.add(player1_yes_no);
        yes_no.add(player2_yes_no);
        yes_no.add(player3_yes_no);
        yes_no.add(player4_yes_no);
        yes_no.add(player5_yes_no);
        yes_no.add(player6_yes_no);
        yes_no.add(player7_yes_no);

        checkBoxes.add((CheckBox) findViewById(R.id.checkBox1));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox2));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox3));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox4));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox5));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox6));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox7));

        //---  Επικεφαλίδα  ---

        TextView cp = findViewById(R.id.current_player);
        cp.setText(players.get(currentPlayerIndex % (players.size())).getName());
        TextView tn = findViewById(R.id.turn_name);
        tn.setText("Γύρος " + String.valueOf(total_turns-turns_left+1));


        //---  Παραγωγή ερώτησης  ---

        TextView question = findViewById(R.id.question);
        Log.i("vars", GlobalVars.getPool().toString());
        question.setText(GlobalVars.getPool().getRandomSentence());

        Animation animation = AnimationUtils.loadAnimation(Gameplay.this, R.anim.fade_in);
        question.startAnimation(animation);


        //---  Εμφάνιση layout των παικτών  ---

        boolean found = Boolean.FALSE;
        for(int i=0; i<=players.size() -1; i++) {
            if (found) {
                player_layouts.get(i-1).setVisibility(View.VISIBLE);
                player_name_forms.get(i-1).setText(player_names.get(i));
            }
            else {
                if (i != currentPlayerIndex % players.size()) {
                    player_layouts.get(i).setVisibility(View.VISIBLE);
                    player_name_forms.get(i).setText(player_names.get(i));
                }
                else found = Boolean.TRUE;
            }
            if (i!=players.size() -1) selectionIndexes.add(-1);
        }


        //---  Listener για τα ναι & όχι  ---

        View.OnClickListener yes_no_selector = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<=yes_no.size()-1; i++){
                    if (yes_no.get(i).contains(v)){
                        ((TextView) yes_no.get(i).get(0)).setSelected(false);
                        ((TextView) yes_no.get(i).get(1)).setSelected(false);
                        selectionIndexes.set(i, yes_no.get(i).indexOf(v));
                    }
                }
                v.setSelected(true);
                if (!selectionIndexes.contains(-1)){
                    ok.setEnabled(true);
                    ok.setAlpha(1);
                }
            }
        };
        for (ArrayList<TextView> a:yes_no)
            for (TextView v:a)
                v.setOnClickListener(yes_no_selector);  //προσθήκη Listener

        //---  Παραγωγή διαφήμησης ---

        if ((players.size() - currentPlayerIndex == 1) &&(turns_left > 1))  {
            Ad.GenerateAd(getApplicationContext());
        }

        //---  Listener του OK Button  ---

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView points = findViewById(R.id.points);
                points.setVisibility(View.VISIBLE); //τεξτακι που λέει τους πόντους
                points.setText("0/" + String.valueOf(players.size()-1));
                ((TextView) findViewById(R.id.turn_name)).setText("ΕΛΕΓΧΟΣ ΑΠΑΝΤΗΣΕΩΝ");

                //---  Εκκίνηση στάδιου ελέγχου  ---

                Swsto.setVisibility(View.VISIBLE);
                for(ArrayList<TextView> a: yes_no) {
                    for (TextView t: a)
                        t.setEnabled(false);
                }

                //---  Listener checkbox
                View.OnClickListener checkBoxListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        TextView points = findViewById(R.id.points);

                        if (((CheckBox) v).isChecked()) {
                            String text = String.valueOf(Character.getNumericValue(points.getText().charAt(0)) + 1) + "/" + String.valueOf(players.size()-1);
                            points.setText(text);
                            players.get(currentPlayerIndex % players.size()).givePoint();
                        }
                        else {
                            String text = String.valueOf(Character.getNumericValue(points.getText().charAt(0)) - 1) + "/" + String.valueOf(players.size() - 1);
                            points.setText(text);
                            players.get(currentPlayerIndex % players.size()).reducePoint();
                        }
                    }
                };

                for (CheckBox c: checkBoxes) {
                    c.setOnClickListener(checkBoxListener);  //προσθήκη Listener
                    c.setVisibility(View.VISIBLE);
                }

                //--- Listener δευτερου κουμπιου
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        player_scores.set(currentPlayerIndex % players.size(), players.get(currentPlayerIndex % players.size()).getScore());

                        currentPlayerIndex += 1;
                        Log.i("Simos", "currentPlayerIndex: " + String.valueOf(currentPlayerIndex ));
                        Log.i("Simos", "γύροι που απομένουν: " +String.valueOf(turns_left));

                        if ((turns_left == 1) && (currentPlayerIndex % players.size() ==0)) { //τέλος παιχνιδιού
                            Intent scores_screen = new Intent(Gameplay.this, Scores.class);
                            scores_screen.putStringArrayListExtra("players", player_names);
                            scores_screen.putIntegerArrayListExtra("scores", player_scores);
                            scores_screen.putExtra("Nturns", total_turns);

                            startActivity(scores_screen);
                        }
                        else { //αλλιώς:
                            if (currentPlayerIndex % players.size() !=0) {  //βρισκόμαστε στον ίδιο γύρο

                                Intent new_gameplay = new Intent(Gameplay.this, Gameplay.class);
                                new_gameplay.putExtra("Nturns", total_turns);
                                new_gameplay.putExtra("Nturns_left", turns_left);
                                new_gameplay.putStringArrayListExtra("players", player_names);
                                new_gameplay.putIntegerArrayListExtra("scores", player_scores);
                                new_gameplay.putExtra("playerIndex", currentPlayerIndex);
                                startActivity(new_gameplay); //εκκίνιση σειράς του επόμενου παίχτη
                            } else {  //αλλιώς έναρξη καινούργιου γύρου
                                Log.i("Simos", "neos giros");
                                Intent turn_scores = new Intent(Gameplay.this, TurnEnd.class);

                                turn_scores.putStringArrayListExtra("players", player_names);
                                turn_scores.putIntegerArrayListExtra("scores", player_scores);
                                turn_scores.putExtra("Nturns_left", turns_left);
                                turn_scores.putExtra("Nturns", total_turns);

                                startActivity(turn_scores);

                            }
                        }
                    }
                });

            }
        });


    }

    public class Player{
        public String name;
        public int score;

        public Player(String name) {
            this.name = name;
            this.score = 0;
        }

        public String getName() {
            return this.name;
        }

        public void givePoint() {
            this.score += 1;
        }

        public void reducePoint() {
            this.score -= 1;
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(int score) {
            this.score = score;
        }

    }
}