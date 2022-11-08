package com.blm.qiubopay.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckInternetConnectionHelper {
    public static boolean isAvailable(Context context) {
        Boolean back = false;
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectMgr != null) {
                NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
                if (netInfo != null) {
                    for (NetworkInfo net : netInfo) {
                        if (net.getState() == NetworkInfo.State.CONNECTED) {
                            back = true;
                        }
                    }
                }
            } else {
                Log.d("NETWORK", "No network available");
            }
        }catch (Exception e){
            back = false;
        }
        return back;
    }
}
