package mx.devapps.utils.components;

import android.os.Process;
import android.os.StrictMode;

import java.net.URL;

import mx.devapps.utils.interfaces.ILongOperation;

public class HWebService {

    private HActivity context;

    private static String URL = "";

    private HRestClient client;

    public HWebService(){

        client = new HRestClient(URL, null);

        client.setHOST("");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public HWebService(HActivity context){

        this.context = context;

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

        this.URL = URL;
        client.setURL(URL);

        try {
            java.net.URL url = new java.net.URL(URL);
            client.setHOST(url.getHost());
        } catch (Exception ex) { }

    }

    public HRestClient getClient() {
        return client;
    }

}
