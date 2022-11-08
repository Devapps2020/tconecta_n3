package com.blm.qiubopay.connection;

import com.blm.qiubopay.parsers.IGenericParser;

public abstract class GenericConnection implements IConnectionManagerDelegate {

    private IGenericParser parser;
    private IGenericConnectionDelegate genericConnectionDelegate;

    public void setParser(IGenericParser parser) {
        this.parser = parser;
    }

    public GenericConnection(IGenericConnectionDelegate genericConnectionDelegate) throws Exception {

        if (genericConnectionDelegate != null){

            this.genericConnectionDelegate = genericConnectionDelegate;

        } else {

            throw new Exception("GenericConnection concrete classes must have an IGenericConnectionDelegate implementation");

        }

    }

    @Override
    public void onConnectionSucceeded(Object result) {

        if (this.parser != null) {

            String stringToParse = (String) result;

            result = this.parser.parseWithString(stringToParse);

        }

        this.genericConnectionDelegate.onConnectionEnded(result);

    }

    @Override
    public void onConnectionFailed(ConnectionManager.ConnectionManagerErrorEnum connectionManagerError) {

        this.genericConnectionDelegate.onConnectionFailed(connectionManagerError);

    }

}
