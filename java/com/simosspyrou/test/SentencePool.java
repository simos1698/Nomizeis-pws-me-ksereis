package com.simosspyrou.test;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class  SentencePool {

    public Context context;
    public Hashtable<Integer, String> sentences;
    public Hashtable<Integer, String> pool;
    public ArrayList <Integer> used_indexes;
    public String TAG = "sents";

    public SentencePool(Context con)  {
        sentences = new Hashtable<>();
        pool = new Hashtable<>();
        used_indexes = new ArrayList<>();
        context = con;

        try {
            InputStream is = context.getAssets().open("sentences.txt");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                sentences.put(i, line);
                i++;
            }

            is = context.openFileInput("used_sentences.txt");
            isr = new InputStreamReader(is, "UTF-8");
            bufferedReader = new BufferedReader(isr);
            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                used_indexes.add(Integer.parseInt(line));
            }
        } catch (FileNotFoundException e) {
            Log.i("Simos", "file not found");
        } catch (UnsupportedEncodingException e) {
            Log.i("Simos", "unsupported encoding");
        } catch (IOException e) {
            Log.i("Simos", "io exception");

        }
        pool.putAll(sentences);
        if (sentences.size() - used_indexes.size() > 40){
            for (int i:used_indexes){
                pool.remove(i);
            }
        }
        else {
            resetPool();
        }

        Log.i("Simos", "SentencePool: Μέγεθος pool ερωτήσεων:" + pool.size());

    }

    public void resetPool(){
        used_indexes.clear();
        pool.putAll(sentences);
        try {
            OutputStreamWriter ow = new OutputStreamWriter(context.openFileOutput("used_sentences.txt", Context.MODE_PRIVATE));
            ow.write("");
            ow.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomSentence() {

        if (pool.size()<40){
            resetPool();
        }

        Integer[] keysAsArray = pool.keySet().toArray(new Integer[pool.size()]);
        int ramdomKey = keysAsArray[new Random().nextInt(keysAsArray.length)];
        Log.i(TAG, "getRandomSentence:" + ramdomKey);
        used_indexes.add(ramdomKey);
        pool.remove(ramdomKey);
        Log.i(TAG, String.valueOf(pool.size()));

        OutputStreamWriter ow = null;
        try {
            ow = new OutputStreamWriter(context.openFileOutput("used_sentences.txt", Context.MODE_APPEND));
            ow.append(String.valueOf(ramdomKey));
            ow.append("\n");
            ow.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sentences.get(ramdomKey);

    }



}

