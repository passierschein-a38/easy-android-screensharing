package com.easyandroidscreensharing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by renard on 22/12/14.
 */
public class SenderAsyncTask extends AsyncTask<Void,Void,Void> {

	private static final String LOG_TAG = SenderAsyncTask.class.getSimpleName();
	private WebSocketTransport _transport;
	LinkedBlockingDeque<VideoChunk> mVideoChunks = new LinkedBlockingDeque<VideoChunk>();

	public void addChunk(VideoChunk chunk) {
        synchronized (mVideoChunks) {
//            boolean isKeyFrame = (chunk.getFlags() & MediaCodec.BUFFER_FLAG_KEY_FRAME) == MediaCodec.BUFFER_FLAG_KEY_FRAME;
//            if (isKeyFrame) {
//
//
//                Log.i(LOG_TAG, "adding keyframe");
//                VideoChunk configChunk = null;
//                for(VideoChunk c: mVideoChunks){
//                    boolean containsConfig = (chunk.getFlags() & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == MediaCodec.BUFFER_FLAG_CODEC_CONFIG;
//                    if(containsConfig){
//                        configChunk = c;
//                        break;
//                    }
//                }
//                mVideoChunks.clear();
//                if(configChunk!=null){
//                    mVideoChunks.add(configChunk);
//                }
//            }
            mVideoChunks.addFirst(chunk);
            if(mVideoChunks.size()>2) {
                Log.i(LOG_TAG, "Chunks: " + mVideoChunks.size());
            }
        }
	}

	SenderAsyncTask(String ip){
        _transport = new WebSocketTransport(ip);
        _transport.connect();
	}

	@Override
	protected Void doInBackground(Void... params) {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);


		while(!isCancelled()){
			VideoChunk chunk = null;
			try {
				//Log.d(LOG_TAG, "waiting for data to send");
				chunk = mVideoChunks.takeLast();
				//Log.d(LOG_TAG,"got data. writing to socket");
				int length = chunk.getData().length;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dataOut = new DataOutputStream(baos);
				//dataOut.writeInt(length);
				//dataOut.writeInt(chunk.getFlags());
				//dataOut.writeLong(chunk.getTimeUs());
				dataOut.write(chunk.getData());

                boolean isConfigFrame = chunk.isConfigFrame();
                boolean isKeyFrame = chunk.isConfigFrame();

				dataOut.flush();
                _transport.sendBinary(baos.toByteArray());

			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
