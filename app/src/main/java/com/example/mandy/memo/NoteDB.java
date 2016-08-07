package com.example.mandy.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NoteDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";//表名
    public static final String CONTENT = "content"; //内容
    public static final String ID = "_id"; //数据库的id
    public static final String TIME = "time"; //时间
//    public static  final String  CALCULATIME= "calculaTime"; //倒计时

    public NoteDB(Context context) {
        super(context, "notes", null, 1);
    }

    //表格列 id列会自增
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //id主动递增且为integer类
                + CONTENT + " TEXT NOT NULL, "
                + TIME + " TEXT NOT NULL ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
