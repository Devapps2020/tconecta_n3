package com.blm.qiubopay.utils;

import android.os.Process;
import android.os.StrictMode;

import com.blm.qiubopay.helpers.interfaces.ILongOperation;
import com.blm.qiubopay.helpers.views.HRestClient;

import mx.devapps.utils.components.HActivity;

public class WebService {

    public enum Method {
        GET,
        POST
    }

    private HActivity context;

    private static String URL = "";

    private HRestClient client;

    public WebService(HActivity context){

        this.context = context;

        client = new HRestClient(URL, null);

        client.setHOST("");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
            StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public WebService(){

        client = new HRestClient(URL, null);

        client.setHOST("");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void execute(final ILongOperation operation){

        client.exeLongOperation(new ILongOperation() {
            @Override
            public String doInBackground() {

                return operation.doInBackground();
            }
            @Override
            public void onPreExecute() {
                operation.onPreExecute();
            }
            @Override
            public void onPostExecute(int code, String result) {

                Process.killProcess(Process.THREAD_PRIORITY_BACKGROUND);

                operation.onPostExecute(code, result);

            }
        });

    }

    public void setURL(String URL) {
        client.setURL(URL);
    }

    public HRestClient getClient() {
        return client;
    }

}
