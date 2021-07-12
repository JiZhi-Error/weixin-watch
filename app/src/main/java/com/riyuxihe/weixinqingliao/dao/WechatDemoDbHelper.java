/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteOpenHelper
 */
package com.riyuxihe.weixinqingliao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WechatDemoDbHelper
extends SQLiteOpenHelper {
    private static final String COMMA = ",";
    public static final String DATABASE_NAME = "WechatDemo.db";
    public static final int DATABASE_VERSION = 1;
    private static final String INTEGER = " INTEGER";
    static final String SQL_CREATE_ENTRIES = "CREATE TABLE message (_id INTEGER PRIMARY KEY,client_msg_id TEXT,msg_id TEXT,msg_type INTEGER,content TEXT,from_username TEXT,to_username TEXT,from_nickname TEXT,to_nickname TEXT,from_member_username TEXT,from_member_nickname TEXT,voice_length INTEGER,create_time INTEGER ) ";
    static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS message";
    private static final String TEXT = " TEXT";

    public WechatDemoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        this.onUpgrade(sQLiteDatabase, n2, n3);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        sQLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        this.onCreate(sQLiteDatabase);
    }
}

