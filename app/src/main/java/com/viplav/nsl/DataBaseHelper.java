package com.viplav.nsl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final String TABLE_NAME = "WhatsApp";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "MESSAGE";
    public static final String COL_3 = "TIME";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,MESSAGE TEXT,TIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String Name,String Message,String Time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,Name);
        contentValues.put(COL_2,Message);
        contentValues.put(COL_3,Time);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public void deleteData(String name)    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "='" + name+"'", null);
        db.close();
    }

    public Cursor getAllData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" WHERE NAME = '"+name+"'",null);
        return res;
    }

    public String[] GetListName()
    {
        try {
            ArrayList<String> names = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DISTINCT NAME from " + TABLE_NAME, null);
            while (res.moveToNext()) {
                names.add(res.getString(0));
            }
            return Arrays.copyOf(names.toArray(), names.size(), String[].class);
        }
        catch(java.lang.NullPointerException e){
            return null;
        }
    }
}

