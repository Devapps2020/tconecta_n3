package com.blm.qiubopay.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blm.qiubopay.models.sepomex.SepomexInformation;

import java.util.ArrayList;

public class DataBaseAccessHelper {

    private DataBaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAccessHelper instance;
    Cursor c = null;

    private DataBaseAccessHelper(Context context){
        this.openHelper = new DataBaseOpenHelper(context);
    }

    public static DataBaseAccessHelper getInstance(Context context){
        if(instance == null){
            instance = new DataBaseAccessHelper(context);
        }
        return instance;
    }


    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(this.db != null){
            this.db.close();
        }
    }

    public String getAddress(String field){
        c = db.rawQuery("select codigo from catalogo", new String[]{});
        StringBuffer buffer = new StringBuffer();

        while (c.moveToNext()){
            String address = c.getString(0);
            buffer.append("" + address);
        }
        return buffer.toString();
    }

    public ArrayList<SepomexInformation> findPostalCode(String postalCode){
        c = db.rawQuery("select * from catalogo where d_codigo = '" + postalCode.trim() +"'", new String[]{});
        StringBuffer buffer = new StringBuffer();

        SepomexInformation sepomexInformation = new SepomexInformation();
        ArrayList<SepomexInformation> sepomexList = new ArrayList<>();

        while (c.moveToNext()){
            //String address = c.getString(0);
            //buffer.append("" + address);
            sepomexInformation = new SepomexInformation();
            sepomexInformation.setD_codigo( c.getString(0));
            sepomexInformation.setD_asenta( c.getString(1));
            sepomexInformation.setD_tipo_asenta( c.getString(2));
            sepomexInformation.setD_mnpio( c.getString(3));
            sepomexInformation.setD_estado( c.getString(4));
            sepomexInformation.setD_ciudad( c.getString(5));
            sepomexInformation.setD_CP( c.getString(6));
            sepomexInformation.setC_estado( c.getString(7));
            sepomexInformation.setC_oficina( c.getString(8));
            sepomexInformation.setC_CP( c.getString(9));
            sepomexInformation.setC_tipo_asenta( c.getString(10));
            sepomexInformation.setC_mnpio( c.getString(11));
            sepomexInformation.setId_asenta_cpcons(c.getString(12));
            sepomexInformation.setD_zona( c.getString(13));
            /*if(row.length == 15)
                sepomexInformation.setC_cve_ciudad(row[14]);*/

            sepomexList.add(sepomexInformation);
        }
        return sepomexList;
    }

}
