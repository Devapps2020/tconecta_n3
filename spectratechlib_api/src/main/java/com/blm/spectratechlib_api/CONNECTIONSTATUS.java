package com.blm.spectratechlib_api;

/**
 * Connection status class for Bluetooth
 */
public enum CONNECTIONSTATUS {

    DISCONNECTED(0),
    CONNECTED(1);

    private final int intValue;
    private CONNECTIONSTATUS(int value) {
        intValue = value;
    }
    public int toInt() {
        return intValue;
    }
}
