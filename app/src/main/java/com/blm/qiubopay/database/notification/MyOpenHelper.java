package com.blm.qiubopay.database.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blm.qiubopay.models.QPAY_Notification;

import java.util.ArrayList;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String COMMENTS_TABLE_CREATE = "CREATE TABLE QPAY_Notification(messageId TEXT, title TEXT, body TEXT, status INTEGER, date TEXT)";
    private static final String DB_NAME = "notifications.sqlite";
    private static final int DB_VERSION = 2;
    private SQLiteDatabase db;

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMENTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            if (newVersion > oldVersion)
                db.execSQL("ALTER TABLE QPAY_Notification ADD COLUMN date TEXT DEFAULT ' '");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Insertar un nuevo comentario
    public void insertNotification(QPAY_Notification row){
        ContentValues cv = new ContentValues();
        cv.put("messageId", row.getMessageId());
        cv.put("body", row.getBody());
        cv.put("title", row.getTitle());
        cv.put("status", "1");
        cv.put("date", row.getDate());

        db.insert("QPAY_Notification", null, cv);
    }

    //Borrar un comentario a partir de su id
    public void deleteNotification(String id){
        String[] args = new String[]{id};
        db.delete("QPAY_Notification", "messageId=?", args);
    }

    //Borrar un comentario a partir de su id
    public void deleteNotificationNull(){
        db.delete("QPAY_Notification", "messageId is null or messageId = ''", null);
    }

    public void updateNotification(String id){
        String[] args = new String[]{id};
        ContentValues cv = new ContentValues();
        cv.put("status","0");
        db.update("QPAY_Notification", cv, "messageId=?", args);

    }

    public int countNotifications(){
        String countQuery = "SELECT  * FROM QPAY_Notification WHERE status = 1";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<QPAY_Notification> getNotifications(){
        //Creamos el cursor
        ArrayList<QPAY_Notification> lista =new ArrayList<QPAY_Notification>();
        Cursor c = db.rawQuery("select messageId, title, body, status, date from QPAY_Notification", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {

                QPAY_Notification com =new QPAY_Notification(
                        c.getString(c.getColumnIndex("messageId")),
                                c.getString(c.getColumnIndex("title")),
                                        c.getString(c.getColumnIndex("body")),
                                                c.getInt(c.getColumnIndex("status")),
                                                    c.getString(c.getColumnIndex("date")));

                        lista.add(com);

            } while (c.moveToNext());
        }

        //Cerramos el cursor
        c.close();
        return lista;
    }
}
