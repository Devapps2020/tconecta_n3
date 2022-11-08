package com.blm.qiubopay.helpers;

import android.os.AsyncTask;

import com.blm.qiubopay.helpers.interfaces.ILongOperation;
import com.blm.qiubopay.helpers.views.HRestClient;


public class HLongOperation extends AsyncTask<Void, String, String> {

    private ILongOperation iLongOperation;

    public HLongOperation(ILongOperation iLongOperation){
        this.iLongOperation = iLongOperation;
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.iLongOperation.doInBackground();
    }

    @Override
    protected void onPostExecute(String result) {
        this.iLongOperation.onPostExecute(HRestClient.code, result);
    }

    @Override
    protected void onPreExecute() {
        this.iLongOperation.onPreExecute();
    }

}