package com.cocos.bcx_sdk.bcx_sql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_sql.contract.AccountEntry;
import com.cocos.bcx_sdk.bcx_sql.helper.DataHelper;
import com.cocos.bcx_sdk.bcx_utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ningkang.guo
 * @Date 2019/3/15
 */
public class AccountDao {

    private final DataHelper helper;
    private final SQLiteDatabase mDatabase;


    public AccountDao(Context context) {
        helper = new DataHelper(context);
        mDatabase = helper.getWritableDatabase();
    }


    /**
     * insert account
     *
     * @param accountName
     * @param userId
     * @param keyStore
     * @param accountType
     * @param chainId
     */
    public void insertAccount(final String accountName, final String userId, final String keyStore, final String accountType, final String chainId) {
        ThreadManager.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mDatabase.query(AccountEntry.TABLE_NAME, new String[]{AccountEntry.COLUMN_ACCOUNT_NAME, AccountEntry.COLUMN_CHAINID}, AccountEntry.COLUMN_ACCOUNT_NAME + " = ? and " + AccountEntry.COLUMN_CHAINID + " = ?", new String[]{accountName, chainId}, null, null, null);
                if (cursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(AccountEntry.COLUMN_ACCOUNT_NAME, accountName);
                    values.put(AccountEntry.COLUMN_ACCOUNT_ID, userId);
                    values.put(AccountEntry.COLUMN_KEY_STORE, keyStore);
                    values.put(AccountEntry.COLUMN_ACCOUNT_TYPE, accountType);
                    values.put(AccountEntry.COLUMN_CHAINID, chainId);
                    mDatabase.insert(AccountEntry.TABLE_NAME, null, values);
                } else {      // already saved , to update.
                    updateAccount(accountName, keyStore, accountType, chainId);
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        });
    }

    /**
     * update account info
     *
     * @param accountName
     * @param keyStore
     * @param accountType
     * @param chainId
     */
    private void updateAccount(String accountName, String keyStore, String accountType, String chainId) {
        ContentValues values = new ContentValues();
        values.put(AccountEntry.COLUMN_KEY_STORE, keyStore);
        values.put(AccountEntry.COLUMN_ACCOUNT_TYPE, accountType);
        mDatabase.update(AccountEntry.TABLE_NAME, values, AccountEntry.COLUMN_ACCOUNT_NAME + " = ? and " + AccountEntry.COLUMN_CHAINID + " = ?", new String[]{accountName, chainId});
    }


    /**
     * query_account_names
     *
     * @return
     */
    public List<String> queryAccountNamesByChainId() {
        Cursor cursor = mDatabase.query(AccountEntry.TABLE_NAME, null, AccountEntry.COLUMN_CHAINID + " = ?", new String[]{CocosBcxApiWrapper.chainId}, null, null, null, null);
//        String existSql = "select * from "+AccountEntry.TABLE_NAME+" where "+AccountEntry.COLUMN_CHAINID+"=? ";
//        Cursor cursor = mDatabase.rawQuery(existSql, new String[]{CocosBcxApiWrapper.chainId});
        List<String> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String accountName = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_NAME));
                list.add(accountName);
                cursor.moveToNext();
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * query all account in same chain
     */
    public List<AccountEntity.AccountBean> queryAllAccountByChainId() {
        Cursor cursor = mDatabase.query(AccountEntry.TABLE_NAME, null, AccountEntry.COLUMN_CHAINID + " = ?", new String[]{CocosBcxApiWrapper.chainId}, null, null, null, null);
        List<AccountEntity.AccountBean> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String accountName = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_NAME));
                String accountId = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_ID));
                String keyStore = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_KEY_STORE));
                String accountType = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_TYPE));
                AccountEntity.AccountBean model = new AccountEntity.AccountBean();
                model.setName(accountName);
                model.setId(accountId);
                model.setKeystore(keyStore);
                model.setAccount_type(accountType);
                list.add(model);
                cursor.moveToNext();
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    /**
     * query account by name
     */
    public AccountEntity.AccountBean queryChainAccountByName(String accountName) {
        Cursor cursor = mDatabase.query(AccountEntry.TABLE_NAME, null, AccountEntry.COLUMN_ACCOUNT_NAME + " = ? and " + AccountEntry.COLUMN_CHAINID + " = ?", new String[]{accountName, CocosBcxApiWrapper.chainId}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String accountId = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_ID));
                String keyStore = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_KEY_STORE));
                String accountType = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_ACCOUNT_TYPE));
                String chainId = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_CHAINID));
                AccountEntity.AccountBean model = new AccountEntity.AccountBean();
                model.setName(accountName);
                model.setId(accountId);
                model.setKeystore(keyStore);
                model.setAccount_type(accountType);
                model.setChainId(chainId);
                cursor.moveToNext();
                return model;
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return null;
    }


    /**
     * delete by name
     *
     * @param userName
     */
    public int deleteAccountByName(String userName) {
        return mDatabase.delete(AccountEntry.TABLE_NAME, AccountEntry.COLUMN_ACCOUNT_NAME + " = ?", new String[]{userName});
    }

    /**
     * delete by id
     *
     * @param userId
     */
    public int deleteAccountById(String userId) {
        return mDatabase.delete(AccountEntry.TABLE_NAME, AccountEntry.COLUMN_ACCOUNT_ID + " = ?", new String[]{userId});
    }


}
