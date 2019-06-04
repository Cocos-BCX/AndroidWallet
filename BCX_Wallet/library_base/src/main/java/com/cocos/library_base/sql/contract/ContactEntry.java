package com.cocos.library_base.sql.contract;

import android.provider.BaseColumns;

public final class ContactEntry implements BaseColumns {
    public final static String TABLE_NAME = "contact_info";
    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_CONTACT_NAME = "name";
    public final static String COLUMN_ACCOUNT_NAME = "accountName";
    public final static String COLUMN_MEMO = "memo";
}