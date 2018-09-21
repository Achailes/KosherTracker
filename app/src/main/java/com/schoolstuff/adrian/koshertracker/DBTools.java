package com.schoolstuff.adrian.koshertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;

    static final String TABLE_NAME = "Restaurants";
    static final String NAME_ROW = "Name";
    static final String CITY_ROW = "City";
    static final String ADDRESS_ROW = "Address";
    static final String TYPE_ROW = "Type";
    static final String CERT_ROW = "Certification";
    static final String PHONE_ROW = "Phone";
    static final String ID_ROW = "_Index";

    private Context mContext;
    private SQLiteDatabase db;

    public DBTools(Context context) {
        super(context, "restaurants.db", null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " +
                TABLE_NAME + " (" +
                ID_ROW + " Integer primary key autoincrement, " +
                NAME_ROW + " text, " +
                CITY_ROW + " text, " +
                ADDRESS_ROW + " text, " +
                TYPE_ROW + " text, " +
                CERT_ROW + " Integer, " +
                PHONE_ROW + " text);";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        super.onDowngrade(db, oldVer, newVer);
    }

    public boolean insert_data(DataIndexer data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data_values = new ContentValues();
        data_values.put(NAME_ROW, data.name);
        data_values.put(CITY_ROW, data.city);
        data_values.put(ADDRESS_ROW, data.address);
        data_values.put(TYPE_ROW, data.type);
        data_values.put(CERT_ROW, data.cert);
        data_values.put(PHONE_ROW, data.phone);
        long success = db.insert(TABLE_NAME, null, data_values);
        db.close();
        if (success != -1)
            return true;
        return false;
    }

    public DataIndexer fetch_data(long id) {
        String query = "Select * from " + TABLE_NAME + " where " + ID_ROW + " ='" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        DataIndexer data = new DataIndexer();
        cursor.moveToFirst();
        data.name = cursor.getString(cursor.getColumnIndex(NAME_ROW));
        data.city = cursor.getString(cursor.getColumnIndex(CITY_ROW));
        data.address = cursor.getString(cursor.getColumnIndex(ADDRESS_ROW));
        data.type = cursor.getString(cursor.getColumnIndex(TYPE_ROW));
        data.cert = cursor.getInt(cursor.getColumnIndex(CERT_ROW));
        data.phone = cursor.getString(cursor.getColumnIndex(PHONE_ROW));
        return data;
    }
    public boolean delete_table(){
        SQLiteDatabase db=this.getWritableDatabase();
        boolean success=db.delete(TABLE_NAME,null,null)!=-1;
        db.execSQL("delete from SQLITE_SEQUENCE where name = '"+TABLE_NAME+"'");
        db.close();
        return success;
    }
    public int get_count(){
        String query="select * from " +TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        int count=cursor.getCount();
        db.close();
        return count;
    }
    public void openRead(){
        this.db=getReadableDatabase();
    }
    public void openWrite(){
        this.db=getWritableDatabase();
    }

    public void close(){
        this.db.close();
    }
}
