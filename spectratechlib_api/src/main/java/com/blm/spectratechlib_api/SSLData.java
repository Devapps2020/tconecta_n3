package com.blm.spectratechlib_api;

import android.content.Context;
import android.content.res.Resources;

import com.spectratech.lib.Logger;
import com.spectratech.lib.tcpip.data.Data_SSLServerLocal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * SSL data object for ApiAppMain
 */
class SSLData {
    private static final String TAG = SSLData.class.getSimpleName();

    public boolean mFlagLocalChannelUseSSL;

    public int mServerCertInputTypeIdx;
    public String mServerKeystoreTypeString;
    public String mServerKeystorePwd;
    public String mServerKeyPwd;
    public int mServerCertId;
    public File mServerCert;

    public boolean mFlagNeedClientAuth;
    public int mCaCertId;
    public File mCaCert;

    public SSLData() {
        reset();
    }

    public void reset() {
        mFlagLocalChannelUseSSL = false;

        mServerCertInputTypeIdx = 0;
        mServerKeystoreTypeString = "BKS";
        mServerKeystorePwd = "";
        mServerKeyPwd = "";
        mServerCertId = 0;
        mServerCert = null;

        mFlagNeedClientAuth = false;
        mCaCertId = 0;
        mCaCert = null;
    }

    public boolean setLocalChannelSSLCerts(File aServerCertFile, String aPassword, String aServerCertFormat, File aCaCertFile) {
        boolean isValidInputStream = isValidInputStream(aServerCertFile);
        if (!isValidInputStream) {
            Logger.w(TAG, "setLocalChannelSSLCerts, aServerCertFile NOT valid");
            return false;
        }

        // cert input as file
        int serverCertInputTypeIdx = 1;

        mServerCertInputTypeIdx = serverCertInputTypeIdx;
        mServerKeystoreTypeString = aServerCertFormat;
        mServerKeystorePwd = aPassword;
        mServerKeyPwd = aPassword;
        mServerCert = aServerCertFile;

        mCaCert = aCaCertFile;
        if (mCaCert != null) {
            mFlagNeedClientAuth = true;
        }
        else {
            mFlagNeedClientAuth = false;
        }

        return true;
    }

    public boolean setLocalChannelSSLCerts(Context context, int aServerCertId, String aPassword, String aServerCertFormat, int aCaCertId) {
        boolean isValidInputStream = isValidInputStream(context, aServerCertId);
        if (!isValidInputStream) {
            Logger.w(TAG, "setLocalChannelSSLCerts, aServerCertId NOT valid");
            return false;
        }

        // cert input as file
        int serverCertInputTypeIdx = 0;

        mServerCertInputTypeIdx = serverCertInputTypeIdx;
        mServerKeystoreTypeString = aServerCertFormat;
        mServerKeystorePwd = aPassword;
        mServerKeyPwd = aPassword;
        mServerCertId = aServerCertId;

        mCaCertId = aCaCertId;
        if (mCaCertId != 0) {
            mFlagNeedClientAuth = true;
        }
        else {
            mFlagNeedClientAuth = false;
        }

        return true;
    }

    public Data_SSLServerLocal getSSLServerDataObject(Context context) {
        Data_SSLServerLocal dataSSLServerLocal;
        InputStream is_ks = getServerCertInputStream(context);
        InputStream is_ca = null;
        if (mFlagNeedClientAuth) {
            is_ca = getCaCertInputStream(context);
        }

        dataSSLServerLocal = new Data_SSLServerLocal(context, is_ks, is_ca, mServerKeystorePwd, mServerKeyPwd);

        dataSSLServerLocal.m_bNeedClientAuth = mFlagNeedClientAuth;

        dataSSLServerLocal.m_keyStoreTypeString=mServerKeystoreTypeString;

        dataSSLServerLocal.m_port = 0;    // any available port

        return dataSSLServerLocal;
    }

    private InputStream getServerCertInputStream(Context context) {
        InputStream is = null;
        switch (mServerCertInputTypeIdx) {
            // file
            case 1: {
                is = getInputStream(mServerCert);
            }
            break;
            // raw
            default: {
                is = getInputStream(context, mServerCertId);
            }
            break;
        }
        return is;
    }

    private InputStream getCaCertInputStream(Context context) {
        InputStream is = null;
        switch (mServerCertInputTypeIdx) {
            // file
            case 1: {
                is  = getInputStream(mCaCert);
            }
            break;
            // raw
            default: {
                is = getInputStream(context, mCaCertId);
            }
            break;
        }
        return is;
    }

    private InputStream getInputStream(Context context, int aId) {
        InputStream is = null;
        if (context==null) {
            Logger.w(TAG, "getInputStream, context is null");
            return is;
        }
        try {
            is = context.getResources().openRawResource(aId);
        }
        catch (Resources.NotFoundException nfe) {
            Logger.w(TAG, "getInputStream, nfe: "+nfe.toString());
        }
        return is;
    }

    private InputStream getInputStream(File aFile) {
        InputStream is =null;
        try {
            FileInputStream fis=new FileInputStream(aFile);
            is=fis;
        }
        catch (FileNotFoundException fnex) {
            Logger.w(TAG, "getInputStreamFromFile, fnex: " + fnex.toString());
        }
        return is;
    }

    static boolean isValidInputStream(Context context, int aFileId) {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(aFileId);
        }
        catch (Resources.NotFoundException nex) {
            Logger.w(TAG, "isValidInputStream, fnex: " + nex.toString());
        }
        if (is != null) {
            return true;
        }
        return false;
    }

    static boolean isValidInputStream(File aFile) {
        InputStream is = null;
        try {
            FileInputStream fis=new FileInputStream(aFile);
            is=fis;
        }
        catch (FileNotFoundException fnex) {
            Logger.w(TAG, "isValidInputStream, fnex: " + fnex.toString());
        }
        if (is != null) {
            return true;
        }
        return false;
    }


}
