package com.cocos.bcx_sdk.bcx_wallet.fc.io;

public class data_stream_size_encoder implements base_encoder {
    private int mnSize = 0;

    @Override
    public void write(byte[] data) {
        mnSize += data.length;
    }

    @Override
    public void write(byte[] data, int off, int len) {
        mnSize += len;
    }

    @Override
    public void write(byte data) {
        mnSize += 1;
    }

    public int getSize() {
        return mnSize;
    }
}
