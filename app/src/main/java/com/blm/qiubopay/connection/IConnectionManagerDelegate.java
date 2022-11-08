package com.blm.qiubopay.connection;

public interface IConnectionManagerDelegate
{
    //											//Method to be called when a WebService call is successful,
    //											//		Parser manager or any other process must be performed in the
    //											//		implementation.
    void onConnectionSucceeded(Object result);

    //											//Method to be called when a WebService call failed
    //											//		Any fail process must be performed in the implementation.
    void onConnectionFailed(ConnectionManager.ConnectionManagerErrorEnum connectionManagerError);

}