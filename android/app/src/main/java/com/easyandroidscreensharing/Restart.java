package com.easyandroidscreensharing;

/**
 * Created by axels on 5/7/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Restart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.exit(0);
    }
    public static void doRestart(Activity anyActivity) {
        anyActivity.startActivity(new Intent(anyActivity.getApplicationContext(), Restart.class));
    }
}