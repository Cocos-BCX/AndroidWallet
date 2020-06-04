package com.cocos.bcx_sdk.bcx_wallet.fc.io;

import java.nio.ByteBuffer;

public class data_stream_encoder implements base_encoder {
    private ByteBuffer mByteBuffer;

    public data_stream_encoder(int nSize) {
        mByteBuffer = ByteBuffer.allocate(nSize);
    }
    @Override
    public void write(byte[] data) {
        mByteBuffer.put(data);
    }

    @Override
    public void write(byte[] data, int off, int len) {
        mByteBuffer.put(data, off, len);
    }

    @Override
    public void write(byte data) {
        mByteBuffer.put(data);
    }

    public byte[] getData() {
        return mByteBuffer.array();
    }
}
