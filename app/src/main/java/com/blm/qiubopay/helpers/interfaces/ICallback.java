package com.blm.qiubopay.helpers.interfaces;

public interface ICallback<T>{

    void onSuccess(T... data);

    void onError(T... data);

}
