package com.blm.qiubopay.database.operative;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.blm.qiubopay.models.operative.OperativeStatus;
import com.blm.qiubopay.models.operative.PayType;
import com.blm.qiubopay.models.operative.restaurant.DbDetail;
import com.blm.qiubopay.models.operative.restaurant.DbOrder;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

public class RestaurantDataHelper {

    private static final int versionBD = 1;
    //private Tools tools;

    //Constantes de la base de datos.
    private static final String DATABASE_NAME="db002.db"; // nombre de base de datos de restaurante
    private static final String DATABASE_TABLE1 = "T001"; //tabla de comandas
    private static final String DATABASE_TABLE2 = "T002"; //tabla de detalle de comanda

    //CAMPOS DE LA CLASE
    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertGenericStmt;

    //QUERY PARA CREAR LA TABLA DE COMANDAS
    private static final String T001_CREATE = "CREATE TABLE " + DATABASE_TABLE1 + " ("+
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "DATE" + " text," +
            "HOUR" + " text," +
            "AMOUNT" + " double,"+
            "COMMENSAL_NO" + " integer," +
            "STATUS" + " text" +
            ")";

    //QUERY PARA CREAR LA TABLA DE DETALLE POR COMANDA
    private static final String T002_CREATE = "CREATE TABLE " + DATABASE_TABLE2 + " ("+
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "FK_ORDER" + " integer," +
            "DATE" + " text," +
            "PAYMENT_TYPE" + " text," +
            "COMMENSALS_NO" + " integer," +
            "FOLIO" + " text," +
            "AUTH" + " text," +
            "AMOUNT" + " double,"+
            "TIP_AMOUNT" + " double,"+
            "TIP_PERCENT" + " double,"+
            "TOTAL" + " double"+
            ")";

    public static String getInsertOrderQuery(){
        return "insert into " + "T001" + " ( "+
                "DATE" + ", " +
                "HOUR" + ", " +
                "AMOUNT" + ", " +
                "COMMENSAL_NO" + ", " +
                "STATUS" + ") values ( ?, ?, ?, ?, ?)";
    }

    public static String getInsertOrderDetailQuery(){
        return "insert into " + "T002" + " ( "+
                "FK_ORDER" + ", " +
                "DATE" + ", " +
                "PAYMENT_TYPE" + ", " +
                "COMMENSALS_NO" + ", " +
                "FOLIO" + ", " +
                "AUTH" + ", " +
                "AMOUNT" + ", " +
                "TIP_AMOUNT" + ", " +
                "TIP_PERCENT" + ", " +
                "TOTAL" + ") values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    //STATEMENT PARA DELETES
    private static final String DELETE_T001 = "delete from " + DATABASE_TABLE1;
    private static final String DELETE_T002 = "delete from " + DATABASE_TABLE2;

    public DbOrder getLastOpenOrder()
    {
        DbOrder dbOrder = null;

        //ARMO LA CONSULTA
        Cursor cursor = db.rawQuery("SELECT ID, DATE, HOUR, AMOUNT, COMMENSAL_NO, STATUS FROM '" + DATABASE_TABLE1 + "' WHERE STATUS = ?  ORDER BY ID DESC LIMIT 1", new String[] {"1"});
        if(cursor.moveToFirst()){
            dbOrder = new DbOrder();

            dbOrder.setOrder_id(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            dbOrder.setDate(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));
            dbOrder.setHour(cursor.getString(cursor.getColumnIndexOrThrow("HOUR")));
            dbOrder.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT")));
            dbOrder.setCommensal_number(cursor.getInt(cursor.getColumnIndexOrThrow("COMMENSAL_NO")));
            dbOrder.setStatus(OperativeStatus.OPEN);
        }

        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }

        return dbOrder;
    }

    public DbOrder getOrderInfo(int id)
    {
        DbOrder dbOrder = null;

        //ARMO LA CONSULTA
        Cursor cursor = db.rawQuery("SELECT ID, DATE, HOUR, AMOUNT, COMMENSAL_NO, STATUS FROM '" + DATABASE_TABLE1 + "' WHERE ID = ? ", new String[] {""+id});
        if(cursor.moveToFirst()){
            dbOrder = new DbOrder();

            dbOrder.setOrder_id(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            dbOrder.setDate(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));
            dbOrder.setHour(cursor.getString(cursor.getColumnIndexOrThrow("HOUR")));
            dbOrder.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT")));
            dbOrder.setCommensal_number(cursor.getInt(cursor.getColumnIndexOrThrow("COMMENSAL_NO")));
            dbOrder.setStatus(OperativeStatus.OPEN);
        }

        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }

        return dbOrder;
    }

    public Boolean insertOrderDetail(int id_order, int commensals, double tip_percent, double amount, double tip_amount, double total_amount, PayType type, String folio, String auth){
        Boolean back = true;
        String paymentType = "";
        this.insertGenericStmt=this.db.compileStatement(getInsertOrderDetailQuery());

        switch (type){
            case CREDIT:
                paymentType = "Credito";
                break;
            case DEBIT:
                paymentType = "Debito";
                break;
            case CASH:
                paymentType = "Efectivo";
                break;
        }

        try{

            this.insertGenericStmt.bindLong(1, id_order);
            this.insertGenericStmt.bindString(2, Utils.getTimestamp());
            this.insertGenericStmt.bindString(3, paymentType);
            this.insertGenericStmt.bindLong(4, commensals);
            this.insertGenericStmt.bindString(5, folio);
            this.insertGenericStmt.bindString(6, auth);
            this.insertGenericStmt.bindDouble(7, amount);
            this.insertGenericStmt.bindDouble(8, tip_amount);
            this.insertGenericStmt.bindDouble(9, tip_percent);
            this.insertGenericStmt.bindDouble(10, total_amount);

            this.insertGenericStmt.execute();
        }catch(Exception e){
            back = false;
            e.printStackTrace();
        }

        return back;
    }

    public Boolean insertOrder(double amount, int commensal){
        Boolean back = true;
        this.insertGenericStmt=this.db.compileStatement(getInsertOrderQuery());

        try{
            this.insertGenericStmt.bindString(1, Utils.getTimestamp());
            this.insertGenericStmt.bindString(2, Tools.getTodayHour());
            this.insertGenericStmt.bindDouble(3, amount);
            this.insertGenericStmt.bindLong(4, commensal);
            this.insertGenericStmt.bindString(5, "1");

            //this.updateStmt_T001.bindString(3, xml);
            this.insertGenericStmt.execute();
        }catch(Exception e){
            back = false;
            e.printStackTrace();
        }

        return back;
    }

    public ArrayList<DbDetail> getOrderDetail(int orderId)
    {
        ArrayList<DbDetail> dbDetailArrayList = null;
        DbDetail dbDetail;

        //ARMO LA CONSULTA
        Cursor cursor = db.rawQuery("SELECT FK_ORDER, DATE, PAYMENT_TYPE, COMMENSALS_NO, FOLIO, AUTH, AMOUNT, TIP_AMOUNT, TIP_PERCENT, TOTAL FROM '" + DATABASE_TABLE2 + "' WHERE FK_ORDER = ? ORDER BY ID ASC", new String[] {""+orderId});
        if(cursor.moveToFirst()){
            dbDetailArrayList = new ArrayList<DbDetail>();
            do{
                dbDetail = new DbDetail();
                String type;

                dbDetail.setFk_order(cursor.getInt(cursor.getColumnIndexOrThrow("FK_ORDER")));
                dbDetail.setDate(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));

                type = cursor.getString(cursor.getColumnIndexOrThrow("PAYMENT_TYPE"));
                if(type.equals("Credito"))
                    dbDetail.setPayment_type(PayType.CREDIT);
                else if(type.equals("Debito"))
                    dbDetail.setPayment_type(PayType.DEBIT);
                else if(type.equals("Efectivo"))
                    dbDetail.setPayment_type(PayType.CASH);

                dbDetail.setCommensals_no(cursor.getInt(cursor.getColumnIndexOrThrow("COMMENSALS_NO")));
                dbDetail.setFolio(cursor.getString(cursor.getColumnIndexOrThrow("FOLIO")));
                dbDetail.setAuth(cursor.getString(cursor.getColumnIndexOrThrow("AUTH")));
                dbDetail.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT")));
                dbDetail.setTip_amount(cursor.getDouble(cursor.getColumnIndexOrThrow("TIP_AMOUNT")));
                dbDetail.setTip_percent(cursor.getDouble(cursor.getColumnIndexOrThrow("TIP_PERCENT")));
                dbDetail.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL")));

                dbDetailArrayList.add(dbDetail);
            }while(cursor.moveToNext());
        }

        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }

        return dbDetailArrayList;
    }

    public Boolean isAnOpenOrder(){
        Boolean back = false;

        //ARMO LA CONSULTA
        Cursor cursor = db.rawQuery("SELECT * FROM '" + DATABASE_TABLE1 + "' WHERE STATUS = ?", new String[] {"1"});
        if(cursor.moveToFirst()){
            back = true;
        }

        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }

        return back;
    }

    public void delTransactions(int id){
        db.execSQL(DELETE_T001 + " WHERE ID = " + "'"  + id + "'");
        db.execSQL(DELETE_T002 + " WHERE FK_ORDER = " + "'"  + id + "'");
    }

    public void delAllTransactions(){
        db.execSQL(DELETE_T001);
        db.execSQL(DELETE_T002);
    }

    public boolean updateDetails(int rowId)
    {
        ContentValues args = new ContentValues();
        args.put("STATUS", "0");
        return db.update(DATABASE_TABLE1, args, "ID" + "=" + rowId, null)>0;
    }

    //*****************CONSTRUCTOR DE LA CLASE*********************
    public RestaurantDataHelper(Context context){

        this.context = context;
        MySQLiteHelper dbHelper = new MySQLiteHelper(this.context);
        this.db = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(this.db != null){
            this.db.close();
        }
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
            Log.d("","");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            //Aun no definido la parte de actualizacion
            Log.d("","");
        }
    }
}
