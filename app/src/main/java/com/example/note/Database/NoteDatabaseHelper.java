package com.example.note.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabaseHelper extends SQLiteOpenHelper {
//  SQLiteOpenHelper ->  A helper class to manage database creation and version management.

    public static final String table_name = "note";//数据库表名

    public NoteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public NoteDatabaseHelper(Context context) {
        super(context, "db_1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //生成一张数据表
        //execSQL -> 执行单个SQL语句，该SQL语句不是SELECT语句或任何其他返回数据的SQL语句。
        db.execSQL("create table " + table_name + "(id integer primary key ,title text,content text,time text,note_group integer,note_length,integer)");
        db.execSQL("create table groups (id integer primary key ,name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //用来更新版本，用不着
    }
}
