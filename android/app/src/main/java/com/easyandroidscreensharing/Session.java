package com.easyandroidscreensharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Session extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        TrueCastApp.setCurrentActivity(this);

        final Intent stopIntent = new Intent(this, MainActivity.class);
        stopIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        stopIntent.putExtra("stop", true);


        Button btn = (Button)findViewById(R.id.stopbutton);
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(stopIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
