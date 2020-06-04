package com.cocos.bcx_sdk.bcx_error;


public class ErrorCode {


    /**
     * websocket connection invalid
     */
    public static int WEBSOCKET_CONNECT_INVALID = -1;

    /**
     * websocket connection success
     */
    public static int OPERATE_SUCCESS = 1;

    /**
     * websocket connection failed
     */
    public static int OPERATE_FAILED = 0;

    /**
     * Account already exist
     */
    public static final int ERROR_ACCOUNT_OBJECT_EXIST = 159;

    /**
     * websocket connection failed. please check your network
     */
    public static int RPC_NETWORK_EXCEPTION = 301;

    /**
     * network fail
     */
    public static final int ERROR_NETWORK_FAIL = 102;

    /**
     * Account is locked or not logged in
     */
    public static final int ACCOUNT_LOCKED_OR_NOT_LOGGED = 114;

    /**
     * account not exist
     */
    public static final int ERROR_OBJECT_NOT_FOUND = 104;

    /**
     * Wrong password
     */
    public static final int ERROR_WRONG_PASSWORD = 105;

    /**
     * Please import the private key
     */
    public static final int ERROR_UNLOCK_ACCOUNT = 107;


    /**
     * Please import right private key
     */
    public static final int ERROR_INVALID_PRIVATE_KEY = 109;

    /**
     * The private key has no account information
     */
    public static final int NO_ACCOUNT_INFORMATION = 110;

    /**
     * the right of private key is not match the operation
     */
    public static final int AUTHORITY_EXCEPTION = 112;

    /**
     * not reward owner
     */
    public static final int NOT_REWARD_OWNER = 115;

    /**
     * No reward available
     */
    public static final int NO_REWARD_AVAILABLE = 127;

    /**
     * please check parameter data type
     */
    public static final int ERROR_PARAMETER_DATA_TYPE = 135;


    /**
     * contract not found
     */
    public static final int ERROR_CONTRACT_NOT_FOUND = 145;

    /**
     * NHAsset do not exist
     */
    public static final int ERROR_NHASSET_DO_NOT_EXIST = 147;

    /**
     * Orders do not exist
     */
    public static final int ERROR_ORDERS_DO_NOT_EXIST = 161;

    /**
     * not the creator of the Asset XX
     */
    public static final int ERROR_NOT_ASSET_CREATOR = 160;

    /**
     * worldViews do not exist
     */
    public static final int ERROR_WORLDVIEW_DO_NOT_EXIST = 164;

    /**
     * The Wallet Chain ID does not match the current chain configuration information.
     */
    public static final int CHAIN_ID_NOT_MATCH = 166;

    /**
     * worldViews exist
     */
    public static final int ERROR_WORLDVIEW_AREADY_EXIST = 167;

    /**
     * Parameter error
     */
    public static final int ERROR_PARAMETER = 1011;

    /**
     * not a committee or witness
     */
    public static final int ERROR_NOT_MEMEBER = 170;

    /**
     * database query error
     */
    public static final int ERROR_DB_ERROR = 177;

    /**
     * 密码不符合规则
     */
    public static final int ERROR_PASSWORD_NOT_SATISFIED = 178;


}