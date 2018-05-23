package com.daya.iap.onestore;

interface ServiceConnectionListener {
    void onConnected();

    void onDisconnected();

    void onErrorNeedUpdateException();
}
