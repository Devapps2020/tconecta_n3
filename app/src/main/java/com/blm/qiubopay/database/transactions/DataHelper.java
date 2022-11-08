package com.blm.qiubopay.database.transactions;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.database.model.QP_BD_ROW;
import com.blm.qiubopay.database.model.SERVICE_BD_ROW;
import com.blm.qiubopay.database.model.TAE_BD_ROW;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.QPAY_StartTxn_object;
import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.tools.Tools;

import java.util.ArrayList;


/**
 * Created by VSM on 08/12/2016.
 */

public class DataHelper {

    public static enum DB_TXR_TYPE{
        FINANCIAL_TXR,
        TAE_TXR,
        SERVICE_TXR,
        PQ_TXR,
        CYA_TXR
    }

    //private Rc4 enc;
    //private BeanConfiguration beanConfiguration;
    private static final int versionBD = 1;
    //private Tools tools;

    //Constantes de la base de datos.
    private static final String DATABASE_NAME="db001.db"; // nombre de base de datos
    private static final String DATABASE_TABLE1 = "T001"; //tabla de transacciones financieras
    private static final String DATABASE_TABLE2 = "T002"; //tabla de transacciones TAE
    private static final String DATABASE_TABLE3 = "T003"; //tabla de transacciones de pago de servicios
    private static final String DATABASE_TABLE4 = "T004"; //tabla de transacciones de pagosConecta
    private static final String DATABASE_TABLE5 = "T005"; //tabla de cargos y abonos

    //NOMBRES DE LAS COLUMNAS DEL LA TABLA T001
    private static final String T1_F001 = "F001";
    private static final String T1_F002 = "F002";
    private static final String T1_F003 = "F003";
    private static final String T1_F004 = "F004";//Respuesta del procesamiento
    private static final String T1_F005 = "F005";//Respuesta en la consulta de transacciones
    
    //CAMPOS DE LA CLASE
    private Context context;
    private SQLiteDatabase db;

    //QUERY QUE CREA LA TABLA DE TRANSACCIONES FINANCIERAS
    private static final String T001_CREATE="CREATE TABLE " + DATABASE_TABLE1+" ("+
            T1_F001+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            T1_F002+" text,"+
            T1_F003+" text,"+
            T1_F004+" text,"+
            T1_F005+" text"+
            ");";


    //QUERY PARA CREAR LA TABLA DE TRANSACCIONES TAE
    private static final String T002_CREATE="CREATE TABLE " + DATABASE_TABLE2+" ("+
            T1_F001+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            T1_F002+" text,"+
            T1_F003+" text,"+
            T1_F004+" text,"+
            T1_F005+" text"+
            ");";

    //QUERY PARA CREAR LA TABLA DE TRANSACCIONES DE PAGO DE SERVICIOS
    private static final String T003_CREATE="CREATE TABLE " + DATABASE_TABLE3+" ("+
            T1_F001+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            T1_F002+" text,"+
            T1_F003+" text,"+
            T1_F004+" text,"+
            T1_F005+" text"+
            ")";

    //QUERY PARA CREAR LA TABLA DE TRANSACCIONES DE PAGOSConecta
    private static final String T004_CREATE = "CREATE TABLE " + DATABASE_TABLE4 + " ("+
            T1_F001 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            T1_F002 + " text," +
            T1_F003 + " text," +
            T1_F004 + " text,"+
            T1_F005 + " text" +
            ")";

    //QUERY PARA CREAR LA TABLA DE TRANSACCIONES DE PAGOSConecta
    private static final String T005_CREATE = "CREATE TABLE " + DATABASE_TABLE5 + " ("+
            T1_F001 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            T1_F002 + " text," +
            T1_F003 + " text," +
            T1_F004 + " text,"+
            T1_F005 + " text" +
            ")";


    private SQLiteStatement insertGenericStmt;

    public static String getInsertQuery(String table_name){
        return "insert into " + table_name + " ( "+
                //T1_F001 + ", " +
                T1_F002 + ", " +
                T1_F003 + ", " +
                T1_F004 + ", " +
                T1_F005 + ") values ( ?, ?, ?, ?)";
    }

    //STATEMENT PARA DELETES
    private static final String DELETE_T001 = "delete from " + DATABASE_TABLE1;
    private static final String DELETE_T002 = "delete from " + DATABASE_TABLE2;
    private static final String DELETE_T003 = "delete from " + DATABASE_TABLE3;
    private static final String DELETE_T004 = "delete from " + DATABASE_TABLE4;
    private static final String DELETE_T005 = "delete from " + DATABASE_TABLE5;


    //*****************CONSTRUCTOR DE LA CLASE*********************
    public DataHelper(Context context){

        this.context = context;
        DataHelper.MySQLiteHelper dbHelper = new DataHelper.MySQLiteHelper(this.context);
        this.db = dbHelper.getWritableDatabase();

        delOldTransactions();
    }

    public void delAllTransactions(){
        db.execSQL(DELETE_T001);
        db.execSQL(DELETE_T002);
        db.execSQL(DELETE_T003);
        db.execSQL(DELETE_T004);
        db.execSQL(DELETE_T005);
    }

    public void insertLocalTransaction(DB_TXR_TYPE type, String date, String data0, String data1, String data2){

        String newDate = "";

        switch (type){
            case FINANCIAL_TXR:
                //data0 = Tipo de transaccion
                this.insertGenericStmt=this.db.compileStatement(getInsertQuery(DATABASE_TABLE1));
                break;
            case TAE_TXR:
                //data0 = carrier
                this.insertGenericStmt=this.db.compileStatement(getInsertQuery(DATABASE_TABLE2));
                break;
            case SERVICE_TXR:
                this.insertGenericStmt=this.db.compileStatement(getInsertQuery(DATABASE_TABLE3));
                break;
            case PQ_TXR:
                this.insertGenericStmt=this.db.compileStatement(getInsertQuery(DATABASE_TABLE4));
                break;
            case CYA_TXR:
                this.insertGenericStmt=this.db.compileStatement(getInsertQuery(DATABASE_TABLE5));
                break;
        }

        //data1: Respuesta de cuando la transacción es realizada
        //data2: Respuesta de cuando la transacción es consultada

        if(null == data1)
            data1 = "ND";
        else if(null == data2)
            data2 = "ND";

        try{
            if(date.contains(" ") && date.length()>10)
                //this.insertGenericStmt.bindString(1, Tools.getDbDateFormat(date));
                newDate = Tools.getDbDateFormat(date);
            else
                //this.insertGenericStmt.bindString(1, date);
                newDate = date;
            this.insertGenericStmt.bindString(1, Tools.getSqliteDateFormat(newDate));
            this.insertGenericStmt.bindString(2, data0);
            this.insertGenericStmt.bindString(3, data1);
            this.insertGenericStmt.bindString(4, data2);

            //this.updateStmt_T001.bindString(3, xml);
            this.insertGenericStmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void delOldTransactions(){
        db.execSQL(DELETE_T001 + " WHERE F002 < " + "'"  + Tools.getTodayDateWithMoreOrMinusDays(-7) + "'");
        db.execSQL(DELETE_T002 + " WHERE F002 < " + "'"  + Tools.getTodayDateWithMoreOrMinusDays(-7) + "'");
        db.execSQL(DELETE_T003 + " WHERE F002 < " + "'"  + Tools.getTodayDateWithMoreOrMinusDays(-7) + "'");
        db.execSQL(DELETE_T004 + " WHERE F002 < " + "'"  + Tools.getTodayDateWithMoreOrMinusDays(-7) + "'");
        db.execSQL(DELETE_T005 + " WHERE F002 < " + "'"  + Tools.getTodayDateWithMoreOrMinusDays(-7) + "'");
    }


    public ArrayList<Object> getLocalTransactions(DB_TXR_TYPE type){
        //TAE_BD_ROW row = null;
        ArrayList<Object> arrayList = null;
        String table = "";

        switch (type){
            case FINANCIAL_TXR:
                table = DATABASE_TABLE1;
                break;
            case TAE_TXR:
                table = DATABASE_TABLE2;
                break;
            case SERVICE_TXR:
                table = DATABASE_TABLE3;
                break;
            case PQ_TXR:
                table = DATABASE_TABLE4;
                break;
            case CYA_TXR:
                table = DATABASE_TABLE5;
                break;
        }

        //ARMO LA CONSULTA
        //Cursor cursor=this.db.query(DATABASE_TABLE2, columnasResultantes, null, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT F002, F003, F004, F005 FROM '" + table + "' WHERE F002 >= Date(?) ORDER BY F001 DESC", new String[] {Tools.getTodayDateWithMoreOrMinusDays(-7)});
        if(cursor.moveToFirst()){
            arrayList = new ArrayList<Object>();
            do{

                switch (type){
                    case FINANCIAL_TXR:
                        arrayList.add(getFinancialRow(cursor));
                        break;
                    case TAE_TXR:
                        arrayList.add(getTaeRow(cursor));
                        break;
                    case SERVICE_TXR:
                        arrayList.add(getServiceRow(cursor));
                        break;
                    case PQ_TXR:
                        arrayList.add(getQpRow(cursor));
                        break;
                    case CYA_TXR:
                        //table = DATABASE_TABLE5;
                        break;
                }
                //System.out.println(beanProduct.toString());

            }while(cursor.moveToNext());
        }

        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }

        return arrayList;
    }

    private FINANCIAL_BD_ROW getFinancialRow(Cursor cursor){
        FINANCIAL_BD_ROW row = new FINANCIAL_BD_ROW();
        Gson gson = new Gson();

        String data1, data2;

        row.setDate(cursor.getString(cursor.getColumnIndexOrThrow(T1_F002)));
        row.setType(cursor.getString(cursor.getColumnIndexOrThrow(T1_F003)));

        data1 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F004));
        data2 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F005));

        if(!data1.equals("ND"))
            row.setResponse1(gson.fromJson(data1, CustomMitecTransaction.class));

        if(!data2.equals("ND"))
            row.setResponse2(gson.fromJson(data2, QPAY_VisaEmvResponse.class));

        return row;
    }

    private QP_BD_ROW getQpRow(Cursor cursor){
        QP_BD_ROW row = new QP_BD_ROW();
        Gson gson = new Gson();

        String data1, data2;

        row.setDate(cursor.getString(cursor.getColumnIndexOrThrow(T1_F002)));
        row.setType(cursor.getString(cursor.getColumnIndexOrThrow(T1_F003)));

        data1 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F004));
        data2 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F005));

        if(!data1.equals("ND"))
            row.setResponse1(gson.fromJson(data1, QPAY_StartTxn_object.class));

        if(!data2.equals("ND"))
            row.setResponse2(gson.fromJson(data2, QPAY_Pago_Qiubo_object.class));

        return row;
    }

    private TAE_BD_ROW getTaeRow(Cursor cursor){
        TAE_BD_ROW row = new TAE_BD_ROW();
        Gson gson = new Gson();

        String data1, data2;

        row.setDate(cursor.getString(cursor.getColumnIndexOrThrow(T1_F002)));
        row.setCarrier(cursor.getString(cursor.getColumnIndexOrThrow(T1_F003)));

        data1 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F004));
        data2 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F005));

        if(!data1.equals("ND"))
            row.setResponse1(gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(T1_F004)), QPAY_TaeSaleResponseFirst.class));

        if(!data2.equals("ND"))
            row.setResponse2(gson.fromJson(data2, TaeSale.class));

        return row;
    }

    private SERVICE_BD_ROW getServiceRow(Cursor cursor){
        SERVICE_BD_ROW row = new SERVICE_BD_ROW();
        Gson gson = new Gson();

        String data1, data2;

        row.setDate(cursor.getString(cursor.getColumnIndexOrThrow(T1_F002)));
        row.setType(cursor.getString(cursor.getColumnIndexOrThrow(T1_F003)));

        data1 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F004));
        data2 = cursor.getString(cursor.getColumnIndexOrThrow(T1_F005));

        if(!data1.equals("ND"))
            row.setResponse1(gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(T1_F004)), QPAY_TaeSaleResponseFirst.class));

        if(!data2.equals("ND")){
            gson = new GsonBuilder().setExclusionStrategies(new ServicePayment.ServicePaymentExcluder()).create();
            row.setResponse2(gson.fromJson(data2, ServicePayment.class));
        }



        return row;
    }

    //FUNCIONES DE BASE DE DATOS
    private static class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context){
            super(context, DATABASE_NAME,null, versionBD);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            createDataBase(db);
        }

        private void createDataBase(SQLiteDatabase db){

            //Se crean las tablas
            db.execSQL(T001_CREATE);
            db.execSQL(T002_CREATE);
            db.execSQL(T003_CREATE);
            db.execSQL(T004_CREATE);
            db.execSQL(T005_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            //Aun no definido la parte de actualizacion
        }
    }
}

