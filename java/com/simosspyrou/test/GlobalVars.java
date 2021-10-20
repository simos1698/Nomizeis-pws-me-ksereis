package com.simosspyrou.test;

import android.app.Application;

class GlobalVars extends Application {

    static private SentencePool pool;

    public static SentencePool getPool() {
        return pool;
    }

    public static void setPool(SentencePool pool) {
        GlobalVars.pool = pool;
    }
}
