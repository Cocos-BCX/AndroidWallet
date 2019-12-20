package com.cocos.library_base.global;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.library_base.utils.AccountHelperUtils;

/**
 * @author ningkang.guo
 * @Date 2019/1/29
 */
public final class SPKeyGlobal {
    /**
     * ACCOUNT_NAME
     */
    public static final String ACCOUNT_NAME = "account_name";

    /**
     * account_id
     */
    public static final String ACCOUNT_ID = "account_id";

    public static final String CUSTOM_NODE_MODEL_LIST = "custom_node_model_list";

    public static final String NODE_WORK_MODEL_SELECTED = "node_work_model_selected";
    public static final String WEB_CACHE_CLEAR = "web_cache_clear";
    public static final String SYSTEM_LANGUAGE = "system_language";
    public static final String FOUND_DIALOG_SHOWED_MARK = "found_dialog_showed_mark";
    public static final String KEY_FOR_VERIFY_ACCOUNT = "key_for_verify_account";
    public static final String SECRET_FREE_CHECK_STATUS = "secret_free_check_status";
    public static final String SYMBOL_SELECTED = "SYMBOL_SELECTED";

    public static final String NET_TYPE = "net_type";
    public static final String IS_FIRST_CONNECT = "is_first_connect";
    public static final String CURRENCY_TYPE = "currency_type";
    public static final String CURRENCY_RATE = "currency_rate";
    public static final String COCOS_PRICE = "cocos_price";
    public static final String TOTAL_ASSET_VALUE = "total_asset_value";
    public static String TOTAL_LOCK_ASSET = AccountHelperUtils.getCurrentAccountName() + AccountHelperUtils.getCurrentAccountId() + CocosBcxApiWrapper.chainId;
}
