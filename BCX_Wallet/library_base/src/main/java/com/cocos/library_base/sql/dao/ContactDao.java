package com.cocos.library_base.sql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.sql.contract.ContactEntry;
import com.cocos.library_base.sql.helper.SQLiteHelper;
import com.cocos.library_base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @author ningkang.guo
 * @Date 2019/1/30
 */
public class ContactDao {

    private Context mContext;
    private final SQLiteHelper helper;
    private final SQLiteDatabase mDatabase;

    public ContactDao() {
        this.mContext = Utils.getContext();
        helper = new SQLiteHelper(mContext);
        mDatabase = helper.getWritableDatabase();
    }

    /**
     * 插入联系人
     *
     * @param name
     * @param accountName
     * @param memo
     */
    public void insertContact(@NonNull String name, @NonNull String accountName, @NonNull String memo) {
        Cursor cursor = mDatabase.query(ContactEntry.TABLE_NAME, new String[]{ContactEntry.COLUMN_ACCOUNT_NAME}, ContactEntry.COLUMN_ACCOUNT_NAME + " = ?", new String[]{accountName}, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(ContactEntry.COLUMN_ACCOUNT_NAME, accountName);
            values.put(ContactEntry.COLUMN_CONTACT_NAME, name);
            values.put(ContactEntry.COLUMN_MEMO, memo);
            mDatabase.insert(ContactEntry.TABLE_NAME, null, values);
        } else {
            updateContact(accountName, name, memo);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


    /**
     * 删除指定用户
     *
     * @param accountName
     */
    public void deleteContact(String accountName) {
        mDatabase.delete(ContactEntry.TABLE_NAME, ContactEntry.COLUMN_ACCOUNT_NAME + " = ?", new String[]{accountName});
    }

    /**
     * 批量删除指定用户
     *
     * @param accountName
     */
    public void deleteMore(String accountName) {
        mDatabase.delete(ContactEntry.TABLE_NAME, ContactEntry.COLUMN_ACCOUNT_NAME + " = ?", new String[]{accountName});
    }

    /**
     * 更改contact数据
     */
    public void updateContact(String accountName, String name, String memo) {
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_CONTACT_NAME, name);
        values.put(ContactEntry.COLUMN_MEMO, memo);
        mDatabase.update(ContactEntry.TABLE_NAME, values, ContactEntry.COLUMN_ACCOUNT_NAME + " = ?", new String[]{accountName});
    }


    /**
     * 查询全部帐户名数据
     */
    public List<ContactModel> queryAllContact() {
        //查询全部数据
        Cursor cursor = mDatabase.query(ContactEntry.TABLE_NAME, null, null, null, null, null, null, null);
        List<ContactModel> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String accountName = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_ACCOUNT_NAME));
                String name = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_NAME));
                String memo = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_MEMO));
                ContactModel model = new ContactModel();
                model.setAccountName(accountName);
                model.setName(name);
                model.setMemo(memo);
                list.add(model);
                cursor.moveToNext();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

}
