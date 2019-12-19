package com.cocos.library_base.utils;


import android.text.TextUtils;
import android.util.Log;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_error.AccountNotFoundException;
import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.types;
import com.cocos.library_base.R;
import com.cocos.library_base.global.SPKeyGlobal;

import java.util.Map;

import static com.cocos.library_base.global.SPKeyGlobal.ACCOUNT_ID;
import static com.cocos.library_base.global.SPKeyGlobal.ACCOUNT_NAME;

/**
 * @author ningkang.guo
 * @Date 2019/4/1
 */
public class AccountHelperUtils {


    /**
     * setCurrentAccountName
     *
     * @param accountName
     */
    public static void setCurrentAccountName(String accountName) {
        try {
            Log.i("setCurrentAccountName:", accountName);
            if (TextUtils.isEmpty(accountName)) {
                SPUtils.putString(Utils.getContext(), ACCOUNT_NAME, "");
                setCurrentAccountId("");
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.TOTAL_ASSET_VALUE, "0.00");
                return;
            }
            SPUtils.putString(Utils.getContext(), ACCOUNT_NAME, accountName);
            setCurrentAccountId(accountName);
        } catch (Exception e) {
            SPUtils.putString(Utils.getContext(), ACCOUNT_NAME, "");
            setCurrentAccountId("");
        }
    }

    /**
     * getCurrentAccountName
     */
    public static String getCurrentAccountName() {
        return SPUtils.getString(Utils.getContext(), ACCOUNT_NAME);
    }

    /**
     * setCurrentAccountId
     *
     * @param accountName
     */
    private static void setCurrentAccountId(String accountName) {
        if (TextUtils.isEmpty(accountName)) {
            SPUtils.putString(Utils.getContext(), ACCOUNT_ID, "");
            return;
        }
        String accountId = null;
        try {
            accountId = CocosBcxApiWrapper.getBcxInstance().get_account_id_by_name_sync(accountName);
            SPUtils.putString(Utils.getContext(), ACCOUNT_ID, accountId);
            Log.i("setCurrentAccountId:", accountId);
        } catch (NetworkStatusException e) {
            ToastUtils.showShort(R.string.net_work_failed);
        } catch (AccountNotFoundException e) {
            ToastUtils.showShort(R.string.account_not_found);
        }
    }

    /**
     * getCurrentAccountId
     */
    public static String getCurrentAccountId() {
        return SPUtils.getString(Utils.getContext(), ACCOUNT_ID);
    }

    /**
     * getCurrentActivePublicKey
     */
    public static String getCurrentActivePublicKey() {
        return getActivePublicKey(getCurrentAccountName());
    }

    /**
     * getCurrentActivePublicKey
     */
    public static String getActivePublicKey(String accountName) {
        account_object account_object = null;
        try {
            account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object_sync(accountName);
            for (Map.Entry<types.public_key_type, Integer> entry : account_object.active.key_auths.entrySet()) {
                return entry.getKey().toString();
            }
        } catch (NetworkStatusException e) {
            ToastUtils.showShort(R.string.net_work_failed);
        } catch (AccountNotFoundException e) {
            ToastUtils.showShort(R.string.account_not_found);
        }
        return "";
    }

    /**
     * getCurrentOwnerPublicKey
     */
    public static String getCurrentOwnerPublicKey() {
        return getOwnerPublicKey(getCurrentAccountName());
    }

    /**
     * getCurrentOwnerPublicKey
     */
    public static String getOwnerPublicKey(String accountName) {
        account_object account_object = null;
        try {
            account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object_sync(accountName);
            for (Map.Entry<types.public_key_type, Integer> entry : account_object.owner.key_auths.entrySet()) {
                return entry.getKey().toString();
            }
        } catch (NetworkStatusException e) {
            ToastUtils.showShort(R.string.net_work_failed);
        } catch (AccountNotFoundException e) {
            ToastUtils.showShort(R.string.account_not_found);
        }
        return "";
    }
}
