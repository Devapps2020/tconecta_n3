package com.blm.qiubopay.helpers;

import android.content.Context;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.DeviceInfo;

//Methods implemented on N3 flavour only
public class N3Helper {

    public static DeviceEngine getDeviceEngine(Context context){
        return APIProxy.getDeviceEngine(context);
    }

    public static DeviceInfo getDeviceInfo(Context context){
        return APIProxy.getDeviceEngine(context).getDeviceInfo();
    }

    public static String getSn(Context context){
        return getDeviceInfo(context).getSn();
    }

}