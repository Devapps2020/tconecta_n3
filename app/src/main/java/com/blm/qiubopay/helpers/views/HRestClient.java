package com.blm.qiubopay.helpers.views;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.blm.qiubopay.BuildConfig;
import com.blm.qiubopay.helpers.HLongOperation;
import com.blm.qiubopay.helpers.interfaces.ILongOperation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HRestClient {

    private HashMap<String, String> headers = new HashMap<>();

    public static int code = 495; //495 Error de certificado SSL

    public static int CONNECT_TIMEOUT = 40000;

    public static int READ_TIMEOUT = 180000;

    private javax.net.ssl.SSLContext SSLContext = null;

    private String URL;

    private String HOST;

    private long elapsedTime;

    private String charset = StandardCharsets.UTF_8.name();

    private String autorization = null;

    private String accept = "application/json";

    private String content_type = "application/json; charset=UTF-8";

    public HRestClient(String URL, InputStream certificate) {

        this.URL = URL;

        setCertificate(certificate);
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String _POST(String dataJSON) {

        code = 404;

        long startTime = System.currentTimeMillis();

        //Logger.i("API URL : " + URL);

        Logger.i("API URL : " + URL + "\nAPI REQUEST : (0:00) "+ dataJSON);

        HttpURLConnection urlConnection_http = null;
        HttpsURLConnection urlConnection_https = null;
        java.net.URL url = null;
        InputStream inputStream = null;
        StringBuilder result = null;

        try {

            url = new URL(URL);

            DataOutputStream dataOutputStream;

            if("http".equals(url.getProtocol())){
                urlConnection_http = (HttpURLConnection) url.openConnection();
                urlConnection_http.setRequestProperty("Content-Type", content_type);
                urlConnection_http.setRequestProperty("Accept", accept);
                urlConnection_http.setRequestProperty("Connection", "close");
                urlConnection_http.setRequestProperty("Authorization", autorization);
                urlConnection_http.setRequestMethod("POST");
                urlConnection_http.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection_http.setReadTimeout(READ_TIMEOUT);

                dataOutputStream = new DataOutputStream(urlConnection_http.getOutputStream());
            }else{
                urlConnection_https = (HttpsURLConnection) url.openConnection();
                urlConnection_https.setRequestProperty("Content-Type", content_type);
                urlConnection_https.setRequestProperty("Accept", accept);
                urlConnection_https.setRequestProperty("Connection", "close");
                urlConnection_https.setRequestProperty("Authorization", autorization);
                urlConnection_https.setRequestMethod("POST");
                urlConnection_https.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection_https.setReadTimeout(READ_TIMEOUT);

                if(SSLContext != null){
                    urlConnection_https.setSSLSocketFactory(SSLContext.getSocketFactory());
                    urlConnection_https.setHostnameVerifier(hostnameVerifier());
                }
                dataOutputStream = new DataOutputStream(urlConnection_https.getOutputStream());
            }

            if(!dataJSON.isEmpty()){
                dataOutputStream.writeBytes(dataJSON);
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            if(urlConnection_http != null){

                code = urlConnection_http.getResponseCode();

                if(code != 200)
                    inputStream = urlConnection_http.getErrorStream();
                else
                    inputStream = urlConnection_http.getInputStream();

            } else{

                code = urlConnection_https.getResponseCode();

                if(code != 200)
                    inputStream = urlConnection_https.getErrorStream();
                else
                    inputStream = urlConnection_https.getInputStream();


            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            inputStream.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }finally {

            if(result == null)
                result = new StringBuilder();

            if(urlConnection_http != null)
                urlConnection_http.disconnect();
            else
                urlConnection_https.disconnect();

        }

        //Logger.i("API STATUS CODE : (" + code + ")");

        elapsedTime = System.currentTimeMillis() - startTime;

        Logger.i("API URL : " + URL + "\nAPI STATUS CODE : (" + code + ")\n" + "API RESPONSE : (" + formatTime(elapsedTime) + ") "+ result.toString());

        return result.toString();

    }

    public String _GET(String params) {

        code = 404;

        long startTime = System.currentTimeMillis();

        Logger.i("API URL : " + URL);

        Logger.i("API REQUEST : (0:00) "+ params);

        HttpURLConnection urlConnection_http = null;
        HttpsURLConnection urlConnection_https = null;
        java.net.URL url = null;
        InputStream inputStream = null;
        StringBuilder result = null;

        try {

            url = new URL(URL + params);

            if("http".equals(url.getProtocol())){
                urlConnection_http = (HttpURLConnection) url.openConnection();
                urlConnection_http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_http.setRequestProperty("Accept", "application/json");
                urlConnection_http.setRequestProperty("Connection", "close");
                urlConnection_http.setRequestMethod("GET");
                urlConnection_http.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection_http.setReadTimeout(READ_TIMEOUT);

            } else {
                urlConnection_https = (HttpsURLConnection) url.openConnection();
                urlConnection_https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_https.setRequestProperty("Accept", "application/json");
                urlConnection_https.setRequestProperty("Connection", "close");
                urlConnection_https.setRequestMethod("GET");
                urlConnection_https.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection_https.setReadTimeout(READ_TIMEOUT);

                if(SSLContext != null){
                    urlConnection_https.setSSLSocketFactory(SSLContext.getSocketFactory());
                    urlConnection_https.setHostnameVerifier(hostnameVerifier());
                }
            }

            if(urlConnection_http != null){

                code = urlConnection_http.getResponseCode();

                if(code != 200)
                    inputStream = urlConnection_http.getErrorStream();
                else
                    inputStream = urlConnection_http.getInputStream();

            } else {

                code = urlConnection_https.getResponseCode();

                if(code != 200)
                    inputStream = urlConnection_https.getErrorStream();
                else
                    inputStream = urlConnection_https.getInputStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            inputStream.close();

        } catch(Exception e){
            e.printStackTrace();
        } finally {

            if(result == null)
                result = new StringBuilder();

            if(urlConnection_http != null)
                urlConnection_http.disconnect();
            else
                urlConnection_https.disconnect();

        }

        Logger.i("API STATUS CODE : (" + code + ")");

        elapsedTime = System.currentTimeMillis() - startTime;

        Logger.i("API RESPONSE : (" + formatTime(elapsedTime) + ") "+ result.toString());

        return result.toString();
    }

    public void exeLongOperation(ILongOperation iLongOperation) {
        new HLongOperation(iLongOperation).execute();
    }

    public void setCertificate(InputStream file){

        if(file == null)
            return;

        try {
            Certificate ca;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(file);

            try {
                ca = cf.generateCertificate(caInput);
                Log.e("Certificate",  ((X509Certificate) ca).getSubjectDN().toString());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext = javax.net.ssl.SSLContext.getInstance("TLS");
            SSLContext.init(null, tmf.getTrustManagers(), null);

        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }

    }

    private HostnameVerifier hostnameVerifier (){

        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier host = HttpsURLConnection.getDefaultHostnameVerifier();
                return BuildConfig.DEBUG ? true :  host.verify(HOST, session);
            }
        };
    }

    public int getCode() {
        return code;
    }

    private static String formatTime(long milliseconds) {
        int min = (int) (milliseconds / 60000);
        int sec = (int) (milliseconds % 60000 / 1000);
        return (min + ":" + (sec <= 9 ? "0" + sec : String.valueOf(sec)));
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getAutorization() {
        return autorization;
    }

    public void setAutorization(String autorization) {
        this.autorization = autorization;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
}
