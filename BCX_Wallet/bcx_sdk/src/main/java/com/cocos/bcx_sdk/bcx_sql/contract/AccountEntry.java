package com.cocos.bcx_sdk.bcx_sql.contract;

import android.provider.BaseColumns;

public final class AccountEntry implements BaseColumns {
    public final static String TABLE_NAME = "cocos_bcx_android_account_info";
    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_ACCOUNT_NAME = "account_name";
    public final static String COLUMN_ACCOUNT_ID = "account_id";
    public final static String COLUMN_KEY_STORE = "key_store";
    public final static String COLUMN_ACCOUNT_TYPE = "account_type";
    public final static String COLUMN_CHAINID = "chainId";
}