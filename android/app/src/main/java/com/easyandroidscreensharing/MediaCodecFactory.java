package com.easyandroidscreensharing;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import java.io.IOException;

/**
 * Created by renard on 18/12/14.
 */
public class MediaCodecFactory {

	private static final int FRAME_RATE = 20;
	private static final int IFRAME_INTERVAL = 5;
	private static final int BIT_RATE = 500000;
    private final int mWidth;
	private final int mHeight;

	public MediaCodecFactory(int width, int height){
		mWidth = width;
        mHeight = height;
	}

	public MediaCodec createVideoEncoder() throws IOException {
		// Encoded video resolution matches virtual display.
		//MediaFormat encoderFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
        MediaFormat encoderFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
		encoderFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
		encoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
		encoderFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
		encoderFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
		MediaCodec encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
		encoder.configure(encoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		return encoder;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}
}
