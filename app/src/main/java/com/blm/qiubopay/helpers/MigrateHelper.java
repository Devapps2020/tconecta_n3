package com.blm.qiubopay.helpers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MigrateHelper {

    private static final String TAG = "MigrateHelper";

    private final String filename = "data_migration.txt";
    File myExternalFile;

    Context context;

    public MigrateHelper(Context context){
        this.context = context;
    }


    public boolean validateFileExist(){

        File downloads = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        if (!downloads.exists() && !downloads.mkdirs())
            Log.e(TAG,"No se pudo crear la carpeta");

        myExternalFile = new File(downloads, filename);

        if(myExternalFile.exists()) {
            Log.d(TAG,"El archivo ya existe");
            return true;
        } else {
            Log.d(TAG,"El archivo no existe");
            return false;
        }

    }


    public void createFile(){

        try {
            if(!validateFileExist()) {
                myExternalFile.createNewFile();
                Log.d(TAG,"Se creo archivo nuevo");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean writeFile(String s){

        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(s.getBytes());
            fos.close();
            Log.d(TAG,"Se escribe en el archivo");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    private String readFile(){

        String myData="";
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG,myData);
        return myData;
    }


    public void deleteFile(){

        if(validateFileExist()){
            myExternalFile.delete();
            Log.d(TAG,"Borrado de archivo");
        }

    }


    public void generateBackup(){

        createFile();

        QPAY_UserCredentials credentials = AppPreferences.getUserCredentials();
        if(credentials!=null){
            String json = new Gson().toJson(credentials);
            json = Tools.stringToHexString(json);
            if(writeFile(json)){
                //QTC Service on Beta
            } else {
                Log.d(TAG,"Tras error procede a borrar el archivo");
                myExternalFile.delete();
            }
        }

    }

    public QPAY_UserCredentials readBackup(){

        QPAY_UserCredentials credentials = null;

        if(validateFileExist()){
            String json = readFile();
            json = Tools.hexStringToString(json);
            Log.d(TAG,"Credenciales: " + json);
            credentials = new Gson().fromJson(json, QPAY_UserCredentials.class);
        }

        return credentials;
    }

}
