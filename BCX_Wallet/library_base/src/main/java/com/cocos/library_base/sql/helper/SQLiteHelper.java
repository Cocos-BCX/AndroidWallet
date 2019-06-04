package com.cocos.library_base.sql.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.cocos.library_base.sql.contract.ContactEntry;

/**
 * @author ningkang.guo
 * @Date 2019/1/30
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactEntry.TABLE_NAME + " ("
            + ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContactEntry.COLUMN_ACCOUNT_NAME + " TEXT NOT NULL, "
            + ContactEntry.COLUMN_CONTACT_NAME + " TEXT,"
            + ContactEntry.COLUMN_MEMO + " TEXT);";


    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "cocos_bcx_wallet_android.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACT_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
