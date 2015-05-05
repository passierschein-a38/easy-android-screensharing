package com.easyandroidscreensharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends Activity implements EncoderAsyncTask.MediaCodecListener{



    private static final short GET_MEDIA_PROJECTION_CODE = 986;
    //private static final String SOCKET_SERVER_IP = "78.47.15.190";
    private static final String SOCKET_SERVER_IP = "10.228.67.65";
    //private static final String SOCKET_SERVER_IP = "192.168.178.27";

    private EncoderAsyncTask mEncoderAsyncTask;
    private SenderAsyncTask mSenderAsyncTask;
    private MediaCodecFactory mMediaCodecFactory;

    private int height = 0;
    private int width = 0;

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        height = metrics.heightPixels /2;
        width = metrics.widthPixels /2;

        mMediaCodecFactory = new MediaCodecFactory(width, height);

        ImageButton show = (ImageButton)findViewById(R.id.imageButtonShow);
        show.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),GET_MEDIA_PROJECTION_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode==GET_MEDIA_PROJECTION_CODE){
            try {
                MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                final MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                mEncoderAsyncTask = new EncoderAsyncTask(this, mediaProjection, mMediaCodecFactory);
                mEncoderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                mSenderAsyncTask = new SenderAsyncTask(SOCKET_SERVER_IP);
                mSenderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onData(VideoChunk info) {
        if(mSenderAsyncTask!=null){
            mSenderAsyncTask.addChunk(info);
        }
    }

    @Override
    public void onEnd() {

    }
}
