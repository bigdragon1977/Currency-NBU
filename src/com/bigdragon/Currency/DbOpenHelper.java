package com.bigdragon.currency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 @brief Сласс для работы с базой данных.
 */
public class DbOpenHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "currency_db";
    public static final String TABLE_NAME_MAIN = "global_currency";
    public static final String TABLE_NAME_UPDATE = "update_rate";
    public static final String TABLE_NAME_CURRENCY = "currency_name";
    public static final String FIELD_CURRENCY_NAME = "currency";
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_DATA_UPDATE = "data";
    private static final String CREATE_TABLE_MAIN = "create table " + TABLE_NAME_MAIN + " ( id_main integer primary key autoincrement, id_currency integer , id_update integer )";
    private static final String CREATE_TABLE_UPDATE = "create table " + TABLE_NAME_UPDATE + " ( id_update integer primary key autoincrement, " + FIELD_RATE + " text, " + FIELD_DATA_UPDATE + " text )";
    private static final String CREATE_TABLE_CURRENCY = "create table " + TABLE_NAME_CURRENCY + " ( id_currency integer primary key autoincrement, " + FIELD_CURRENCY_NAME + " text )";
    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }
/**
 @brief Вызывается для создания базы данных.
 */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CURRENCY);
        sqLiteDatabase.execSQL(CREATE_TABLE_UPDATE);
        sqLiteDatabase.execSQL(CREATE_TABLE_MAIN);
    }
/**
 @brief Вызывается для изменения базы данных.
 */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
