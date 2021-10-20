package com.simosspyrou.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetUp extends AppCompatActivity {

    Boolean f1 = Boolean.FALSE;
    Boolean f2 = Boolean.FALSE;
    ArrayList<String> playerNames = new ArrayList<>();
    public ArrayList<EditText> nameTextfields = new ArrayList<EditText>();
    int[] nameFocuses = {1,1,1,1,1,1,1,1};
    public int playerN;
    public int turnN_selected;
    public Button ok;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Αρχικοποιήσεις
        ok = findViewById(R.id.okbutton);
        ok.setEnabled(false);
        final ArrayList<View> players = new ArrayList<>();
        final ArrayList<View> forms = new ArrayList<>();
        final ArrayList<View> turns = new ArrayList<>();
        final ArrayList<EditText> nameTextfields = new ArrayList<>();

        players.add(findViewById(R.id.p1));
        players.add(findViewById(R.id.p2));
        players.add(findViewById(R.id.p3));
        players.add(findViewById(R.id.p4));
        players.add(findViewById(R.id.p5));
        players.add(findViewById(R.id.p6));

        nameTextfields.add((EditText)findViewById(R.id.name1)); //1 means it's hasn't been focused on yet
        nameTextfields.add((EditText)findViewById(R.id.name2));
        nameTextfields.add((EditText)findViewById(R.id.name3));
        nameTextfields.add((EditText)findViewById(R.id.name4));
        nameTextfields.add((EditText)findViewById(R.id.name5));
        nameTextfields.add((EditText)findViewById(R.id.name6));
        nameTextfields.add((EditText)findViewById(R.id.name7));
        nameTextfields.add((EditText)findViewById(R.id.name8));

        forms.add(findViewById(R.id.linearLayout1));
        forms.add(findViewById(R.id.linearLayout2));
        forms.add(findViewById(R.id.linearLayout3));
        forms.add(findViewById(R.id.linearLayout4));
        forms.add(findViewById(R.id.linearLayout5));
        forms.add(findViewById(R.id.linearLayout6));
        forms.add(findViewById(R.id.linearLayout7));
        forms.add(findViewById(R.id.linearLayout8));

        turns.add(findViewById(R.id.turn1));
        turns.add(findViewById(R.id.turn2));
        turns.add(findViewById(R.id.turn3));

        for (View f: forms)
            f.setVisibility(View.GONE);



        //Δημιουργία Listeners
        View.OnClickListener selector_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1 = Boolean.TRUE;
                playerN = players.indexOf(v) + 3;
                v.setSelected(true);
                reset_other_sels(v, players);
                createPlayerForms(playerN, forms, nameTextfields);
                if (f2) {
                    ok.setEnabled(true);
                    ok.setAlpha(1);
                }

            }};

        View.OnClickListener selector_listener_2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2 = Boolean.TRUE;
                v.setSelected(true);
                reset_other_sels(v, turns);
                if (f1) {
                    ok.setEnabled(true);
                    ok.setAlpha(1);
                }
            }
        };

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                int nIndex = nameTextfields.indexOf(v);
                EditText editText = (EditText) findViewById(v.getId());
                if (nameFocuses[nIndex] == 1) {
                    editText.setText("");
                    nameFocuses[nIndex] = 0;
                }

            }
        };

        //καλείται όταν τελιώνει το set up
        View.OnClickListener button_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerNames.removeAll(playerNames); //διαγραφή προηγούμενων ονομάτων

                for (View t : turns) {
                    if (t.isSelected())
                        getTurnN(turns.indexOf(t));
                }
                EditText text = findViewById(R.id.name1);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name2);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name3);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name4);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name5);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name6);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name7);
                playerNames.add(text.getText().toString());
                text = findViewById(R.id.name8);
                playerNames.add(text.getText().toString());

                Boolean completed = Boolean.TRUE;
                for (int i = 0; i <= playerN - 1; i++)
                    if (playerNames.get(i).equals(" ")) completed = Boolean.FALSE;
                if (!completed) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetUp.this);
                    builder.setTitle("Κάτσε λίγο...");
                    builder.setMessage("Πρέπει να συμπληρώσεις τα ονόματα για καθέ παίκτη!");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ArrayList<String> found = new ArrayList<String>();
                    for (int i = 0; i <= playerNames.size() - 1; i++) {
                        if (playerNames.get(i).equals(" "))
                            found.add(playerNames.get(i));
                    }
                    playerNames.removeAll(found);

                    Intent intent = new Intent(SetUp.this, Gameplay.class);
                    intent.putStringArrayListExtra("players", playerNames);
                    intent.putExtra("Nturns", turnN_selected);
                    ArrayList<Integer> scores = new ArrayList<Integer>();
                    for (int i = 0; i <= playerNames.size() - 1; i++)
                        scores.add(0);
                    intent.putIntegerArrayListExtra("scores", scores);

                    Log.i("Simos", "setup completed, players passed:");
                    for (String p: playerNames) {
                        Log.i("Simos", p);
                    }
                    startActivity(intent);  //εκκίνηση gameplay
                }
            }
        };


        //Εφαρμογή listeners
        for (View v:players)
            v.setOnClickListener(selector_listener);
        for (View t:turns)
            t.setOnClickListener(selector_listener_2);
        for (EditText n:nameTextfields)
            n.setOnFocusChangeListener(focusChangeListener);
        ok.setOnClickListener(button_listener);


    }

    private void createPlayerForms(int numP, ArrayList<View> forms, ArrayList<EditText> nameFields)  {

        for (int i=0; i<=7; i++) {
            if (i<numP)
                forms.get(i).setVisibility(View.VISIBLE);
            else {
                forms.get(i).setVisibility(View.GONE);
                nameFields.get(i).setText(" ");

            }
        }

    }

    public void reset_other_sels(View k, ArrayList<View> views) {
        for (View v:views)
            if (v!=k) v.setSelected(false);
    };

    public void getTurnN(int x){
        switch (x){
            case 0:
                turnN_selected = 2;
                break;
            case 1:
                turnN_selected = 4;
                break;
            case 2:
                turnN_selected = 8;
                break;
        }
    }

}
