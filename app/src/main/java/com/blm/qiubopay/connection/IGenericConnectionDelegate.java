package com.blm.qiubopay.connection;

public interface IGenericConnectionDelegate {

    void onConnectionEnded(Object result);

    void onConnectionFailed(Object result);

}
