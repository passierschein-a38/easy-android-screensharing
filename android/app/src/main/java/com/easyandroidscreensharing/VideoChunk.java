package com.easyandroidscreensharing;
import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * Created by renard on 18/12/14.
 */
public class VideoChunk {
	private final byte[] mData;
	private final int mFlags;
	private final long mTimeUs;

	public VideoChunk(byte[] buffer, int flags, long presentationTimeUs) {
		mData = buffer;
		this.mFlags = flags;
		this.mTimeUs = presentationTimeUs;
	}

	public VideoChunk(ByteBuffer buffer, int flags, long presentationTimeUs) {
		mData = new byte[buffer.remaining()];
		buffer.get(mData);
		this.mFlags = flags;
		this.mTimeUs = presentationTimeUs;
	}

    public boolean isConfigFrame(){
        return (getFlags()&MediaCodec.BUFFER_FLAG_CODEC_CONFIG)==MediaCodec.BUFFER_FLAG_CODEC_CONFIG;
    }

    public boolean isKeyFrame(){
        return (getFlags()&MediaCodec.BUFFER_FLAG_KEY_FRAME)==MediaCodec.BUFFER_FLAG_KEY_FRAME;
    }




	public byte[] getData() {
		return mData;
	}

	public long getTimeUs() {
		return mTimeUs;
	}

	public int getFlags() {
		return mFlags;
	}

}
