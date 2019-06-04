package com.cocos.library_base.utils;


import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.types;

import java.util.List;
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
            SPUtils.putString(Utils.getContext(), ACCOUNT_NAME, accountName);
            setCurrentAccountId(accountName);
        } catch (Exception e) {
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
        String accountId = CocosBcxApiWrapper.getBcxInstance().get_account_id_by_name(accountName);
        SPUtils.putString(Utils.getContext(), ACCOUNT_ID, accountId);
    }

    /**
     * getCurrentAccountId
     */
    public static String getCurrentAccountId() {
        return SPUtils.getString(Utils.getContext(), ACCOUNT_ID);
    }

    /**
     * getCurrentAccountId
     */
    public static List<String> getAccountNames() {
        return CocosBcxApiWrapper.getBcxInstance().get_dao_account_names();
    }

    /**
     * getCurrentActivePublicKey
     */
    public static String getCurrentActivePublicKey() {
        account_object account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object(getCurrentAccountName());
        if (null == account_object) {
            return "";
        }
        for (Map.Entry<types.public_key_type, Integer> entry : account_object.active.key_auths.entrySet()) {
            return entry.getKey().toString();
        }
        return "";
    }

    /**
     * getCurrentActivePublicKey
     */
    public static String getActivePublicKey(String accountName) {
        account_object account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object(accountName);
        if (null == account_object) {
            return "";
        }
        for (Map.Entry<types.public_key_type, Integer> entry : account_object.active.key_auths.entrySet()) {
            return entry.getKey().toString();
        }
        return "";
    }

    /**
     * getCurrentOwnerPublicKey
     */
    public static String getCurrentOwnerPublicKey() {
        account_object account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object(getCurrentAccountName());
        if (null == account_object) {
            return "";
        }
        for (Map.Entry<types.public_key_type, Integer> entry : account_object.owner.key_auths.entrySet()) {
            return entry.getKey().toString();
        }
        return "";
    }

    /**
     * getCurrentOwnerPublicKey
     */
    public static String getOwnerPublicKey(String accountName) {
        account_object account_object = CocosBcxApiWrapper.getBcxInstance().get_account_object(accountName);
        if (null == account_object) {
            return "";
        }
        for (Map.Entry<types.public_key_type, Integer> entry : account_object.owner.key_auths.entrySet()) {
            return entry.getKey().toString();
        }
        return "";
    }
}
