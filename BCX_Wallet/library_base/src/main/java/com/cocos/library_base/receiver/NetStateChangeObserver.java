package com.cocos.library_base.receiver;

public interface NetStateChangeObserver {
    void onNetDisconnected();
    void onNetConnected(NetworkType networkType);
}