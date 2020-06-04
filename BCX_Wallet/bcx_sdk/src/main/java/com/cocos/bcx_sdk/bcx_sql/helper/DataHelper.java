package com.cocos.bcx_sdk.bcx_sql.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cocos.bcx_sdk.bcx_sql.contract.AccountEntry;

import javax.annotation.Nullable;

/**
 * @author ningkang.guo
 * @Date 2019/3/15
 */
public class DataHelper extends SQLiteOpenHelper {

    String SQL_CREATE_USER_TABLE = "CREATE TABLE " + AccountEntry.TABLE_NAME + " ("
            + AccountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountEntry.COLUMN_ACCOUNT_NAME + " TEXT NOT NULL, "
            + AccountEntry.COLUMN_ACCOUNT_ID + " TEXT , "
            + AccountEntry.COLUMN_KEY_STORE + " TEXT NOT NULL, "
            + AccountEntry.COLUMN_ACCOUNT_TYPE + " TEXT ,"
            + AccountEntry.COLUMN_CHAINID + " TEXT NOT NULL);";


    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "cocos_bcx_android_sdk.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
