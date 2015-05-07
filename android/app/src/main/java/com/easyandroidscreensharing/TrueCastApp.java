package com.easyandroidscreensharing;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by axels on 5/7/2015.
 */
public class TrueCastApp extends Application {

    private static TrueCastApp instance;
    private static Activity act = null;

    public static TrueCastApp getInstance() {
        return instance;
    }

    public static void setCurrentActivity( Activity a ){
        act = a;
    }

    public static Activity getCurrentActivity(){
        return act;
    }


    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
