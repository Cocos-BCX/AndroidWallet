package com.cocos.bcx_sdk.bcx_api;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_callback.ResponseData;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.bcx_sdk.bcx_entity.AccountType;
import com.cocos.bcx_sdk.bcx_entity.CreateAccountParamEntity;
import com.cocos.bcx_sdk.bcx_entity.CreateAccountRequestParamsEntity;
import com.cocos.bcx_sdk.bcx_error.AccountExistException;
import com.cocos.bcx_sdk.bcx_error.AccountNotFoundException;
import com.cocos.bcx_sdk.bcx_error.AssetNotFoundException;
import com.cocos.bcx_sdk.bcx_error.AuthorityException;
import com.cocos.bcx_sdk.bcx_error.ContractNotFoundException;
import com.cocos.bcx_sdk.bcx_error.KeyInvalideException;
import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.bcx_sdk.bcx_error.NhAssetNotFoundException;
import com.cocos.bcx_sdk.bcx_error.NoRewardAvailableException;
import com.cocos.bcx_sdk.bcx_error.NotAssetCreatorException;
import com.cocos.bcx_sdk.bcx_error.NotMemberException;
import com.cocos.bcx_sdk.bcx_error.OrderNotFoundException;
import com.cocos.bcx_sdk.bcx_error.PasswordInvalidException;
import com.cocos.bcx_sdk.bcx_error.PasswordVerifyException;
import com.cocos.bcx_sdk.bcx_error.UnLegalInputException;
import com.cocos.bcx_sdk.bcx_error.WordViewExistException;
import com.cocos.bcx_sdk.bcx_error.WordViewNotExistException;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_server.ConnectServer;
import com.cocos.bcx_sdk.bcx_sql.dao.AccountDao;
import com.cocos.bcx_sdk.bcx_utils.utils.DateUtil;
import com.cocos.bcx_sdk.bcx_utils.utils.IDHelper;
import com.cocos.bcx_sdk.bcx_utils.utils.NumberUtil;
import com.cocos.bcx_sdk.bcx_utils.utils.PassWordCheckUtil;
import com.cocos.bcx_sdk.bcx_wallet.authority1;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_related_word_view_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_fee_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.block_header;
import com.cocos.bcx_sdk.bcx_wallet.chain.block_info;
import com.cocos.bcx_sdk.bcx_wallet.chain.committee_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.committee_object_result;
import com.cocos.bcx_sdk.bcx_wallet.chain.contract_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.create_account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.dynamic_global_property_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.feed_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.fill_order_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.global_config_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.global_property_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.limit_orders_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.market_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.memo_data;
import com.cocos.bcx_sdk.bcx_wallet.chain.nh_asset_order_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.nhasset_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.object_id;
import com.cocos.bcx_sdk.bcx_wallet.chain.operation_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.operations;
import com.cocos.bcx_sdk.bcx_wallet.chain.private_key;
import com.cocos.bcx_sdk.bcx_wallet.chain.public_key;
import com.cocos.bcx_sdk.bcx_wallet.chain.settle_price_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.signed_operate;
import com.cocos.bcx_sdk.bcx_wallet.chain.transaction_in_block_info;
import com.cocos.bcx_sdk.bcx_wallet.chain.types;
import com.cocos.bcx_sdk.bcx_wallet.chain.vesting_balances_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.vesting_balances_result;
import com.cocos.bcx_sdk.bcx_wallet.chain.vote_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.witnesses_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.witnesses_object_result;
import com.cocos.bcx_sdk.bcx_wallet.chain.world_view_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.aes;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.sha256_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.sha512_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.data_stream_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.data_stream_size_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.bitcoinj.core.AddressFormatException;
import org.spongycastle.openssl.PasswordException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cocos.bcx_sdk.bcx_error.ErrorCode.CHAIN_ID_NOT_MATCH;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_ACCOUNT_OBJECT_EXIST;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_INVALID_PRIVATE_KEY;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_NETWORK_FAIL;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_OBJECT_NOT_FOUND;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_PARAMETER;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_PARAMETER_DATA_TYPE;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_PASSWORD_NOT_SATISFIED;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_UNLOCK_ACCOUNT;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.ERROR_WRONG_PASSWORD;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.OPERATE_FAILED;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.OPERATE_SUCCESS;


/**
 * CocosBcxApi
 * <p>
 * this class is between rpc api and sdk api
 * most business logic write here,
 */
public class CocosBcxApi {


    private ConnectServer mWebSocketApi;
    private String rspText;
    private HashMap<types.public_key_type, types.private_key_type> mHashMapPub2Private = new HashMap<>();
    private wallet_object mWalletObject = new wallet_object();
    private sha512_object mCheckSum;
    private String active_key_auths;
    private String owner_key_auths;
    private Date currentDateObject;


    private CocosBcxApi() {
        mWebSocketApi = ConnectServer.getBcxWebServerInstance();
    }


    private static class CocosBcxApiInstanceHolder {
        static final CocosBcxApi INSTANCE = new CocosBcxApi();
    }


    public static CocosBcxApi getBcxInstance() {
        return CocosBcxApiInstanceHolder.INSTANCE;
    }


    class wallet_object {
        sha256_object chain_id;
        List<account_object> my_accounts = new ArrayList<>();
        ByteBuffer cipher_keys;
        List<Object> extra_keys = new ArrayList<>();

        void update_account(account_object accountObject, object_id<account_object> id, List<types.public_key_type> listPublicKeyType) {
            my_accounts.clear();
            extra_keys.clear();
            my_accounts.add(accountObject);
            List<Object> extra_keys2 = new ArrayList<>();
            extra_keys2.add(id);
            extra_keys2.add(listPublicKeyType);
            extra_keys.add(extra_keys2);
        }
    }


    static class plain_keys {
        Map<types.public_key_type, String> keys;
        sha512_object checksum;

        void write_to_encoder(base_encoder encoder) {
            raw_type rawType = new raw_type();
            rawType.pack(encoder, UnsignedInteger.fromIntBits(keys.size()));
            for (Map.Entry<types.public_key_type, String> entry : keys.entrySet()) {
                encoder.write(entry.getKey().key_data);
                byte[] byteValue = entry.getValue().getBytes();
                rawType.pack(encoder, UnsignedInteger.fromIntBits(byteValue.length));
                encoder.write(byteValue);
            }
            encoder.write(checksum.hash);
        }

        static plain_keys from_input_stream(InputStream inputStream) {
            plain_keys keysResult = new plain_keys();
            keysResult.keys = new HashMap<>();
            keysResult.checksum = new sha512_object();
            raw_type rawType = new raw_type();
            UnsignedInteger size = rawType.unpack(inputStream);
            try {
                for (int i = 0; i < size.longValue(); ++i) {
                    types.public_key_type publicKeyType = new types.public_key_type();
                    inputStream.read(publicKeyType.key_data);
                    UnsignedInteger strSize = rawType.unpack(inputStream);
                    byte[] byteBuffer = new byte[strSize.intValue()];
                    inputStream.read(byteBuffer);
                    String strPrivateKey = new String(byteBuffer);
                    keysResult.keys.put(publicKeyType, strPrivateKey);
                }
                inputStream.read(keysResult.checksum.hash);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keysResult;
        }
    }


    /**
     * initialize server build
     *
     * @return code
     */
    int initialize() {
        int nRet = mWebSocketApi.connect();
        if (nRet == OPERATE_SUCCESS) {
            sha256_object sha256Object = null;
            try {
                sha256Object = mWebSocketApi.get_chain_id();
                mWalletObject.chain_id = sha256Object;
                if (CocosBcxApiWrapper.chainId != null && !CocosBcxApiWrapper.chainId.equals(sha256Object.toString())) {
                    nRet = CHAIN_ID_NOT_MATCH;
                    return nRet;
                }
            } catch (NetworkStatusException e) {
                nRet = ERROR_NETWORK_FAIL;
            }
        }
        return nRet;
    }


    /**
     * create account
     *
     * @param faucetUrl
     * @param paramEntity paramEntity
     * @param isAutoLogin true :   log in， false:just register
     * @param accountDao
     * @param callBack
     * @throws NetworkStatusException
     */
    public void createAccount(String faucetUrl, CreateAccountParamEntity paramEntity, boolean isAutoLogin, AccountDao accountDao, IBcxCallBack callBack) throws NetworkStatusException, UnLegalInputException {

        if (!PassWordCheckUtil.passwordVerify(paramEntity.getPassword())) {
            rspText = new ResponseData(ERROR_PASSWORD_NOT_SATISFIED, "Password does not meet the rules：^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.-]).{12,}$", null).toString();
            callBack.onReceiveValue(rspText);
            return;
        }
        private_key privateActiveKey = private_key.from_seed(paramEntity.getActiveSeed());
        private_key privateOwnerKey = private_key.from_seed(paramEntity.getOwnerSeed());
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());
        account_object accountObject = lookup_account_names(paramEntity.getAccountName());
        if (accountObject != null) {
            rspText = new ResponseData(ERROR_ACCOUNT_OBJECT_EXIST, "Account already exist", null).toString();
            callBack.onReceiveValue(rspText);
            return;
        }
        CreateAccountRequestParamsEntity.CreateAccountParams createAccountRequestParamsEntitys = new CreateAccountRequestParamsEntity.CreateAccountParams();
        createAccountRequestParamsEntitys.name = paramEntity.getAccountName();
        createAccountRequestParamsEntitys.active_key = publicActiveKeyType;
        createAccountRequestParamsEntitys.owner_key = publicOwnerKeyType;
        createAccountRequestParamsEntitys.memo_key = publicActiveKeyType;
        createAccountRequestParamsEntitys.refcode = null;
        createAccountRequestParamsEntitys.referrer = "";

        CreateAccountRequestParamsEntity createAccountRequestParamsEntity = new CreateAccountRequestParamsEntity();
        createAccountRequestParamsEntity.account = createAccountRequestParamsEntitys;
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(createAccountRequestParamsEntity));
        Request request = new Request.Builder().url(faucetUrl + "/api/v1/accounts").header("Accept", "application/json").addHeader("Authorization", "YnVmZW5nQDIwMThidWZlbmc=").post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String strResponse = response.body().string();
            if (response.isSuccessful()) {
                // parse create account data model
                create_account_object createAccountObject = global_config_object.getInstance().getGsonBuilder().create().fromJson(strResponse, create_account_object.class);
                if (createAccountObject.code != 200) {
                    rspText = new ResponseData(createAccountObject.code, createAccountObject.getMsg(), null).toString();
                    callBack.onReceiveValue(rspText);
                    return;
                }
                long startTime = System.currentTimeMillis();
                long endTime;
                do {
                    accountObject = lookup_account_names(createAccountObject.getData().getAccount().getName());
                    endTime = System.currentTimeMillis();
                    if (endTime - startTime > 6000) {
                        rspText = new ResponseData(OPERATE_FAILED, "operate failed", null).toString();
                        callBack.onReceiveValue(rspText);
                        return;
                    }
                } while (accountObject == null);
                if (isAutoLogin) {
                    // get account object
                    //prepare data to store keystore
                    List<types.public_key_type> listPublicKeyType = new ArrayList<>();
                    listPublicKeyType.add(publicActiveKeyType);
                    listPublicKeyType.add(publicOwnerKeyType);
                    mWalletObject.update_account(accountObject, accountObject.id, listPublicKeyType);
                    mHashMapPub2Private.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
                    mHashMapPub2Private.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));
                    save_account(paramEntity.getAccountName(), accountObject.id.toString(), paramEntity.getPassword(), paramEntity.getAccountType().name(), accountDao);
                    rspText = new ResponseData(OPERATE_SUCCESS, createAccountObject.getMsg(), createAccountObject.getData()).toString();
                    callBack.onReceiveValue(rspText);
                } else {
                    rspText = new ResponseData(OPERATE_SUCCESS, createAccountObject.getMsg(), createAccountObject.getData()).toString();
                    callBack.onReceiveValue(rspText);
                }
            } else {
                if (response.body().contentLength() != 0) {
                    rspText = new ResponseData(OPERATE_FAILED, "operate failed", strResponse).toString();
                    callBack.onReceiveValue(rspText);
                }
            }
        } catch (Exception e) {
            rspText = new ResponseData(OPERATE_FAILED, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        }
    }


    /**
     * account model login
     *
     * @param strAccountName
     * @param strPassword
     * @param accountDao
     * @param callBack
     * @throws NetworkStatusException
     */
    public void password_login(String strAccountName, String strPassword, AccountDao accountDao, IBcxCallBack callBack) throws NetworkStatusException, UnLegalInputException {
        // get public key
        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        //prepare data to store keystore
        account_object accountObject = lookup_account_names(strAccountName);
        if (accountObject == null) {
            rspText = new ResponseData(ERROR_OBJECT_NOT_FOUND, "Account does not exist", null).toString();
            callBack.onReceiveValue(rspText);
            return;
        }
        // verify password
        if (accountObject != null && !accountObject.active.is_public_key_type_exist(publicActiveKeyType) &&
                !accountObject.owner.is_public_key_type_exist(publicActiveKeyType) &&
                !accountObject.active.is_public_key_type_exist(publicOwnerKeyType) &&
                !accountObject.owner.is_public_key_type_exist(publicOwnerKeyType)) {
            rspText = new ResponseData(ERROR_WRONG_PASSWORD, "Wrong password", null).toString();
            callBack.onReceiveValue(rspText);
            return;
        }

        //prepare data to store keystore
        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicActiveKeyType);
        listPublicKeyType.add(publicOwnerKeyType);
        mWalletObject.update_account(accountObject, accountObject.id, listPublicKeyType);
        mHashMapPub2Private.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
        mHashMapPub2Private.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));

        //return account object
        rspText = new ResponseData(OPERATE_SUCCESS, "success", accountObject).toString();
        callBack.onReceiveValue(rspText);

        // save_account account
        save_account(accountObject.name, accountObject.id.toString(), strPassword, AccountType.ACCOUNT.name(), accountDao);
    }


    /**
     * update KeyStore
     *
     * @param strAccountName
     * @param strPassword
     * @param accountDao
     * @throws NetworkStatusException
     * @throws UnLegalInputException
     */
    void updateKeyStore(String strAccountName, String strPassword, AccountDao accountDao) throws NetworkStatusException, UnLegalInputException {
        // get public key
        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());
        //prepare data to store keystore
        account_object accountObject = lookup_account_names(strAccountName);
        //prepare data to store keystore
        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicActiveKeyType);
        listPublicKeyType.add(publicOwnerKeyType);
        mWalletObject.update_account(accountObject, accountObject.id, listPublicKeyType);
        mHashMapPub2Private.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
        mHashMapPub2Private.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));
        // save_account account
        save_account(accountObject.name, accountObject.id.toString(), strPassword, AccountType.ACCOUNT.name(), accountDao);
    }


    /**
     * import keystore
     *
     * @param keystore is json string the data type must as some as you exported;
     * @param password
     */
    public void import_keystore(String keystore, String password, String accountType, AccountDao accountDao, IBcxCallBack callBack) {
        try {
            // parse keystore
            Gson gson = global_config_object.getInstance().getGsonBuilder().create();
            mWalletObject = gson.fromJson(keystore, wallet_object.class);
            // decrypt keystore
            Map<String, String> private_keys = decrypt_keystore_callback_private_key(password);
            // return  private key
            rspText = new ResponseData(OPERATE_SUCCESS, "success", private_keys).toString();
            callBack.onReceiveValue(rspText);
            account_object account_object = lookup_account_names(mWalletObject.my_accounts.get(0).name);
            if (null == account_object) {
                throw new AccountNotFoundException("Account does not exist");
            }
            save_account(account_object.name, account_object.id.toString(), password, accountType, accountDao);
        } catch (JsonSyntaxException e) {
            rspText = new ResponseData(ERROR_PARAMETER_DATA_TYPE, "Please check parameter type", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (NetworkStatusException e) {
            rspText = new ResponseData(ERROR_NETWORK_FAIL, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (UnLegalInputException e) {
            rspText = new ResponseData(ERROR_PARAMETER, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (KeyInvalideException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AddressFormatException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AccountNotFoundException e) {
            rspText = new ResponseData(ERROR_OBJECT_NOT_FOUND, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        }

    }


    /**
     * export keystore
     *
     * @param accountName
     * @param password
     * @param callBack    you can get the keystore of the account you input,you can read the keystore in file to save .
     */
    public void export_keystore(String accountName, String password, AccountDao accountDao, IBcxCallBack callBack) {
        try {

            AccountEntity.AccountBean accountBean = get_dao_account_by_name(accountName, accountDao);
            if (null == accountBean) {
                throw new AccountNotFoundException("Account does not exist");
            }
            if (unlock(accountName, password, accountDao) != OPERATE_SUCCESS && verify_password(accountName, password).size() <= 0) {
                throw new PasswordVerifyException("Wrong password");
            }
            String resultStr = accountBean.getKeystore();
            mWalletObject = global_config_object.getInstance().getGsonBuilder().create().fromJson(resultStr, wallet_object.class);
            HashMap<types.public_key_type, Integer> activeAuths = mWalletObject.my_accounts.get(0).active.key_auths;
            for (Map.Entry<types.public_key_type, Integer> entry : activeAuths.entrySet()) {
                active_key_auths = resultStr.replace("{\"" + entry.getKey() + "\":" + entry.getValue() + "}", "[[" + "\"" + entry.getKey() + "\"," + entry.getValue() + "]]");
            }
            HashMap<types.public_key_type, Integer> auths = mWalletObject.my_accounts.get(0).owner.key_auths;
            for (Map.Entry<types.public_key_type, Integer> entry : auths.entrySet()) {
                owner_key_auths = active_key_auths.replace("{\"" + entry.getKey() + "\":" + entry.getValue() + "}", "[[" + "\"" + entry.getKey() + "\"," + entry.getValue() + "]]");
            }
            callBack.onReceiveValue(owner_key_auths);
            lock();
        } catch (NetworkStatusException e) {
            rspText = new ResponseData(ERROR_NETWORK_FAIL, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AccountNotFoundException e) {
            rspText = new ResponseData(ERROR_OBJECT_NOT_FOUND, "Account does not exist", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (PasswordVerifyException e) {
            rspText = new ResponseData(ERROR_WRONG_PASSWORD, "Wrong password", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (KeyInvalideException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AddressFormatException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        }
    }


    /**
     * get_account_name_by_private_key
     *
     * @param wifKey private key
     */
    public List<account_object> get_account_by_private_key(String wifKey) throws NetworkStatusException, AccountNotFoundException, KeyInvalideException, AddressFormatException {
        // get public key
        types.private_key_type privateKeyType = new types.private_key_type(wifKey);
        public_key publicKey = privateKeyType.getPrivateKey().get_public_key();
        types.public_key_type publicKeyType = new types.public_key_type(publicKey);
        List<String> publicKeyTypes = new ArrayList<>();
        publicKeyTypes.add(publicKeyType.toString());
        // request to get accoount id
        List<List<String>> objects = mWebSocketApi.get_key_references(publicKeyTypes);
        // if id[]  null ,you need know the response about this rpc:get_key_references
        if (objects == null || objects.size() <= 0 || objects.get(0).size() <= 0) {
            throw new AccountNotFoundException("The private key has no account information");
        }
        List<account_object> account_names = new ArrayList<>();
        String addedid = null;
        for (List<String> account_ids : objects) {
            for (String id : account_ids) {
                if (TextUtils.equals(id, addedid)) {
                    continue;
                }
                // get account object
                account_object account_object = get_account_object(id);
                account_names.add(account_object);
                addedid = id;
            }
        }
        return account_names;
    }


    /**
     * import private key
     *
     * @param wifKey   private key
     * @param password to encrypt your private key,
     */
    public List<String> import_wif_key(String wifKey, String password, String accountType, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, KeyInvalideException, AddressFormatException, PasswordInvalidException {

        if (!PassWordCheckUtil.passwordVerify(password)) {
            throw new PasswordInvalidException("Password does not meet the rules：^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.-]).{12,}$");
        }

        // get public key
        types.private_key_type privateKeyType = new types.private_key_type(wifKey);
        public_key publicKey = privateKeyType.getPrivateKey().get_public_key();
        types.public_key_type publicKeyType = new types.public_key_type(publicKey);
        List<String> publicKeyTypes = new ArrayList<>();
        publicKeyTypes.add(publicKeyType.toString());

        // request to get accoount id
        List<List<String>> objects = mWebSocketApi.get_key_references(publicKeyTypes);

        // if id[]  null ,you need know the callback  about this rpc:get_key_references
        if (objects == null || objects.size() <= 0 || objects.get(0).size() <= 0) {
            throw new AccountNotFoundException("The private key has no account information");
        }
        List<String> account_names = new ArrayList<>();
        for (List<String> account_ids : objects) {
            for (String id : account_ids) {
                // get account object
                account_object account_object = get_account_object(id);
                //prepare data to store keystore
                List<types.public_key_type> listPublicKeyType = new ArrayList<>();
                listPublicKeyType.add(publicKeyType);
                mWalletObject.update_account(account_object, account_object.id, listPublicKeyType);
                mHashMapPub2Private.put(publicKeyType, privateKeyType);
                account_names.add(account_object.name);
                // save_account account
                if (TextUtils.equals(AccountType.ACCOUNT.name(), accountType) && account_names.size() == 1) {
                    save_account(account_object.name, account_object.id.toString(), password, accountType, accountDao);
                    return account_names;
                }
                save_account(account_object.name, account_object.id.toString(), password, accountType, accountDao);
            }
        }
        if (TextUtils.equals(AccountType.WALLET.name(), accountType)) {
            return account_names;
        }
        return null;
    }


    /**
     * transfer
     *
     * @param password
     * @param strFrom
     * @param strTo
     * @param strAmount
     * @param strAssetSymbolOrId
     * @param strMemo
     * @param is_encrypt_memo
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws AuthorityException
     * @throws AssetNotFoundException
     * @throws KeyInvalideException
     * @throws AddressFormatException
     */
    public String transfer(String password, String strFrom, String strTo, String strAmount, String strAssetSymbolOrId, String strMemo, boolean is_encrypt_memo, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, AuthorityException, AssetNotFoundException, KeyInvalideException, AddressFormatException {
        // get asset object
        asset_object assetObject = lookup_asset_symbols(strAssetSymbolOrId);

        if (null == assetObject) {
            throw new AssetNotFoundException("Transfer asset does not exist");
        }

        // get account object to get account id
        account_object accountObjectFrom = get_account_object(strFrom);
        account_object accountObjectTo = get_account_object(strTo);
        if (accountObjectTo == null) {
            throw new AccountNotFoundException("Account to does not exist");
        }

        if (accountObjectFrom == null) {
            throw new AccountNotFoundException("Account from does not exist");
        }

        // verify tempory password and account model password
        if (unlock(accountObjectFrom.name, password, accountDao) != OPERATE_SUCCESS && verify_password(accountObjectFrom.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        operations.transfer_operation transferOperation = new operations.transfer_operation();
        transferOperation.from = accountObjectFrom.id;
        transferOperation.to = accountObjectTo.id;
        transferOperation.amount = assetObject.amount_from_string(strAmount);
        transferOperation.extensions = new HashSet<>();

        //sign  memo
        if (!TextUtils.isEmpty(strMemo)) {
            List<Object> memo = new ArrayList<>();
            if (is_encrypt_memo) {
                memo_data memo_data = new memo_data();
                memo_data.from = accountObjectFrom.options.memo_key;
                memo_data.to = accountObjectTo.options.memo_key;
                types.private_key_type privateKeyType = mHashMapPub2Private.get(accountObjectFrom.options.memo_key);
                if (privateKeyType == null) {
                    // Must have active permission to transfer  please confirm that you imported the activePrivateKey
                    throw new AuthorityException("Transfer requires the private key of activity mode, make sure that the private key of the activity mode is imported");
                }
                memo_data.set_message(
                        privateKeyType.getPrivateKey(),
                        accountObjectTo.options.memo_key.getPublicKey(),
                        strMemo,
                        0
                );
                memo_data.get_message(
                        privateKeyType.getPrivateKey(),
                        accountObjectTo.options.memo_key.getPublicKey()
                );
                memo.add(1);
                memo.add(memo_data);
            } else {
                memo.add(0);
                memo.add(strMemo);
            }
            transferOperation.memo = memo;
        }

        // prepare to sign transfer
        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = transferOperation;
        operationType.nOperationType = operations.ID_TRANSFER_OPERATION;

        signed_operate tx = new signed_operate();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);
        tx.extensions = new HashSet<>();
        return sign_transaction(tx, accountObjectFrom);
    }


    /**
     * sign transaction with active private_key
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private String sign_transaction(signed_operate tx, account_object account_object) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> key_auths = account_object.active.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have active permission");
            }
        }
        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction(tx);
    }

    /**
     * sign transaction with owner private_key
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private String sign_transaction_owner(signed_operate tx, account_object account_object) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> key_auths = account_object.owner.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have owner permission");
            }
        }
        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction(tx);
    }


    /**
     * sign transaction with owner & active private_key
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private String sign_transaction_both(signed_operate tx, account_object account_object) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> activity_key_auths = account_object.active.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : activity_key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have active permission");
            }
        }

        HashMap<types.public_key_type, Integer> key_auths = account_object.owner.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have owner permission");
            }
        }
        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction(tx);
    }

    /**
     * sign transaction with active & other_private_key
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private String sign_transaction_active_double(signed_operate tx, account_object account_object, String other_private_key) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> activity_key_auths = account_object.active.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : activity_key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have active permission");
            }
        }

        types.private_key_type privateKey = mHashMapPub2Private.get(other_private_key);
        if (privateKey != null) {
            tx.sign(privateKey, mWebSocketApi.get_chain_id());
        } else {
            throw new AuthorityException("Author failed! make sure other_private_key right");
        }

        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction(tx);
    }


    /**
     * sign transaction with owner & other_private_key
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private String sign_transaction_owner_double(signed_operate tx, account_object account_object, String other_private_key) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> activity_key_auths = account_object.owner.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : activity_key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have owner permission");
            }
        }

        types.private_key_type privateKey = mHashMapPub2Private.get(other_private_key);
        if (privateKey != null) {
            tx.sign(privateKey, mWebSocketApi.get_chain_id());
        } else {
            throw new AuthorityException("Author failed! make sure other_private_key right");
        }

        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction(tx);
    }


    /**
     * sign transaction
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    private Object sign_transaction_with_callback(signed_operate tx, account_object account_object) throws NetworkStatusException, AuthorityException {
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);
        dateObject = calender.getTime();
        tx.set_expiration(dateObject);
        HashMap<types.public_key_type, Integer> key_auths = account_object.active.key_auths;
        for (Map.Entry<types.public_key_type, Integer> entry : key_auths.entrySet()) {
            types.private_key_type privateKey = mHashMapPub2Private.get(entry.getKey());
            if (privateKey != null) {
                tx.sign(privateKey, mWebSocketApi.get_chain_id());
            } else {
                throw new AuthorityException("Author failed! make sure you logined and have active permission");
            }
        }
        LogUtils.i("sign_transaction", global_config_object.getInstance().getGsonBuilder().create().toJson(tx));
        return mWebSocketApi.broadcast_transaction_with_callback(tx);
    }


    /**
     * verify account password
     *
     * @return
     */
    private Map<String, String> verify_password(String accountName, String strPassword) throws NetworkStatusException, PasswordVerifyException, AccountNotFoundException {

        private_key privateActiveKey = private_key.from_seed(accountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(accountName + "owner" + strPassword);
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        List<String> publicKeyTypes1 = new ArrayList<>();
        publicKeyTypes1.add(publicOwnerKeyType.toString());
        publicKeyTypes1.add(publicActiveKeyType.toString());
        //get account id
        List<List<String>> publicKeyTypes1List = mWebSocketApi.get_key_references(publicKeyTypes1);
        // if id[]  null ,you need know the callback  about this rpc:get_key_references
        if (publicKeyTypes1List == null || publicKeyTypes1List.size() <= 0 || publicKeyTypes1List.get(0).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        // load private key in memory
        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicActiveKeyType);
        listPublicKeyType.add(publicOwnerKeyType);
        account_object accountObject = get_account_object(publicKeyTypes1List.get(0).get(0));
        if (accountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        Map<String, String> strings = new HashMap<>();
        types.private_key_type private_active_key_type = new types.private_key_type(privateActiveKey);
        types.private_key_type private_owner_key_type = new types.private_key_type(privateOwnerKey);
        mWalletObject.update_account(accountObject, accountObject.id, listPublicKeyType);
        mHashMapPub2Private.put(publicActiveKeyType, private_active_key_type);
        mHashMapPub2Private.put(publicOwnerKeyType, private_owner_key_type);
        strings.put(publicActiveKeyType.toString(), private_active_key_type.toString());
        strings.put(publicOwnerKeyType.toString(), private_owner_key_type.toString());
        return strings;
    }


    /**
     * invoking contract method
     *
     * @param strAccount
     * @param password
     * @param contractNameOrId
     * @param functionName
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws AuthorityException
     * @throws PasswordVerifyException
     * @throws ContractNotFoundException
     */
    public String invoking_contract(String strAccount, String password, String contractNameOrId, String functionName, List value_list, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, AuthorityException, ContractNotFoundException, PasswordVerifyException, KeyInvalideException, AddressFormatException {

        //search contract
        contract_object contractObject = mWebSocketApi.get_contract(contractNameOrId);
        if (contractObject == null) {
            throw new ContractNotFoundException("Contract does not exist");
        }

        // search account object
        account_object accountObject = get_account_object(strAccount);
        if (accountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        // verify tempory password and account model password
        if (unlock(accountObject.name, password, accountDao) != OPERATE_SUCCESS && verify_password(accountObject.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.invoking_contract_operation invokingContractOperation = new operations.invoking_contract_operation();
        invokingContractOperation.caller = accountObject.id;
        invokingContractOperation.contract_id = contractObject.id;
        invokingContractOperation.function_name = functionName;
        invokingContractOperation.extensions = new HashSet<>();
        invokingContractOperation.value_list = new ArrayList<>();
        if (null != value_list && value_list.size() > 0) {
            int typeId;
            for (Object param : value_list) {
                String type = param.getClass().toString();
                switch (type) {
                    case "class java.lang.String":
                        typeId = 2;
                        break;
                    case "class java.lang.Long":
                    case "class java.lang.Integer":
                        typeId = 0;
                        break;
                    case "class java.lang.Boolean":
                        typeId = 3;
                        break;
                    case "class java.lang.Double":
                    case "class java.lang.Float":
                        typeId = 1;
                        break;
                    default:
                        typeId = 4;
                        break;
                }
                List<Object> base_encoder = new ArrayList<>();
                operations.invoking_contract_operation.v baseValues = new operations.invoking_contract_operation.v();
                baseValues.v = param;
                base_encoder.add(typeId);
                base_encoder.add(baseValues);
                invokingContractOperation.value_list.add(base_encoder);
            }
        }
        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = invokingContractOperation;
        operationType.nOperationType = operations.ID_CALCULATE_INVOKING_CONTRACT_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();

        return sign_transaction(transactionWithCallback, accountObject);
    }


    /**
     * get contract info
     *
     * @param contractNameOrId
     * @return
     */
    public contract_object get_contract(String contractNameOrId) throws NetworkStatusException, ContractNotFoundException {
        contract_object contract_object = mWebSocketApi.get_contract(contractNameOrId);
        if (null == contract_object) {
            throw new ContractNotFoundException("contract does not exist!");
        }
        return contract_object;
    }

    /**
     * get nh asset order object
     *
     * @throws NetworkStatusException
     */
    public nh_asset_order_object get_nhasset_order_object(String nh_order_id) throws NetworkStatusException {
        return mWebSocketApi.get_nhasset_order_object(nh_order_id);
    }

    /**
     * get nh asset order object
     *
     * @throws NetworkStatusException
     */
    public limit_orders_object get_limit_order_object(String limit_order_id) throws NetworkStatusException {
        return mWebSocketApi.get_limit_order_object(limit_order_id);
    }


    /**
     * create_nh_asset
     *
     * @param register_creator
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws AuthorityException
     * @throws PasswordVerifyException
     */
    public String register_creator(String register_creator, String password, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException {

        account_object register_creator_object = get_account_object(register_creator);
        if (register_creator_object == null) {
            throw new AccountNotFoundException("Register account does not exist");
        }

        // verify tempory password and account model password
        if (unlock(register_creator_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(register_creator_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.register_creator_operation register_creator_operation = new operations.register_creator_operation();
        register_creator_operation.fee_paying_account = register_creator_object.id;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = register_creator_operation;
        operationType.nOperationType = operations.ID_REGISTOR_CREATOR_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, register_creator_object);
    }


    /**
     * create_worldview
     *
     * @param world_view
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws AuthorityException
     * @throws PasswordVerifyException
     */
    public String create_worldview(String world_view, final String create_worldview_account, final String password, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException, WordViewExistException {

        account_object create_worldview_account_object = get_account_object(create_worldview_account);
        if (create_worldview_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        List<String> worldViews = new ArrayList<>();
        worldViews.add(world_view);
        List<world_view_object> world_view_objects = lookup_world_view(worldViews);
        if (world_view_objects != null && world_view_objects.size() > 0 && world_view_objects.get(0) != null) {
            throw new WordViewExistException("WorldView already exist");
        }
        // verify tempory password and account model password
        if (unlock(create_worldview_account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(create_worldview_account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.create_worldview_operation create_worldview_operation = new operations.create_worldview_operation();
        create_worldview_operation.fee_paying_account = create_worldview_account_object.id;
        create_worldview_operation.world_view = world_view;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_worldview_operation;
        operationType.nOperationType = operations.ID_CREATE_WORLDVIEW_OPERATION;

        signed_operate signed_operate = new signed_operate();
        signed_operate.operations = new ArrayList<>();
        signed_operate.operations.add(operationType);
        signed_operate.extensions = new HashSet<>();
        return sign_transaction(signed_operate, create_worldview_account_object);
    }


    /**
     * create_nh_asset
     *
     * @param fee_paying_account
     * @param owner
     * @param asset_id
     * @param world_view
     * @param base_describe
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws AuthorityException
     * @throws PasswordVerifyException
     */
    public String create_nh_asset(String fee_paying_account, String password, String owner, String asset_id, String world_view, String base_describe, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException, AssetNotFoundException, WordViewNotExistException {

        account_object fee_paying_account_object = get_account_object(fee_paying_account);
        if (fee_paying_account_object == null) {
            throw new AccountNotFoundException("Creator account does not exist");
        }
        account_object owner_account_object = get_account_object(owner);
        if (owner_account_object == null) {
            throw new AccountNotFoundException("Owner account does not exist");
        }
        asset_object settle_asset_object = lookup_asset_symbols(asset_id);
        if (null == settle_asset_object) {
            throw new AssetNotFoundException(asset_id + "does not exist");
        }
        List<String> worldViews = new ArrayList<>();
        worldViews.add(world_view);
        List<world_view_object> world_view_names = lookup_world_view(worldViews);
        if (world_view_names == null || world_view_names.get(0) == null) {
            throw new WordViewNotExistException("worldViews do not exist");
        }
        // verify tempory password and account model password
        if (unlock(fee_paying_account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(fee_paying_account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.create_nhasset_operation create_nhasset_operation = new operations.create_nhasset_operation();
        create_nhasset_operation.fee_paying_account = fee_paying_account_object.id;
        create_nhasset_operation.owner = owner_account_object.id;
        create_nhasset_operation.asset_id = asset_id;
        create_nhasset_operation.world_view = world_view;
        create_nhasset_operation.base_describe = base_describe;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_nhasset_operation;
        operationType.nOperationType = operations.ID_CREATE_NH_ASSET_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, fee_paying_account_object);
    }


    /**
     * transfer nh asset
     *
     * @param password
     * @param account_from
     * @param account_to
     * @param nh_asset_ids
     * @param accountDao
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws NhAssetNotFoundException
     * @throws AuthorityException
     * @throws PasswordVerifyException
     */
    public String transfer_nh_asset(String password, String account_from, String account_to, List<String> nh_asset_ids, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, NhAssetNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException, NotAssetCreatorException {

        account_object accountObjectFrom = get_account_object(account_from);
        if (accountObjectFrom == null) {
            throw new AccountNotFoundException("Transfer account does not exist");
        }
        account_object accountObjectTo = get_account_object(account_to);
        if (accountObjectTo == null) {
            throw new AccountNotFoundException("Receiving account does not exist");
        }

        // verify tempory password and account model password
        if (unlock(accountObjectFrom.name, password, accountDao) != OPERATE_SUCCESS && verify_password(accountObjectFrom.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();

        for (String nh_asset_id : nh_asset_ids) {
            nhasset_object nhasset_object = lookup_nh_asset_object(nh_asset_id);
            if (nhasset_object == null) {
                throw new NhAssetNotFoundException(nh_asset_id + " does not exist");
            }
            if (!TextUtils.equals(nhasset_object.nh_asset_owner.toString(), accountObjectFrom.id.toString())) {
                throw new NotAssetCreatorException(nh_asset_id + " is not your asset");
            }

            operations.transfer_nhasset_operation transfer_nhasset_operation = new operations.transfer_nhasset_operation();
            transfer_nhasset_operation.from = accountObjectFrom.id;
            transfer_nhasset_operation.to = accountObjectTo.id;
            transfer_nhasset_operation.nh_asset = nhasset_object.id;

            operations.operation_type operationType = new operations.operation_type();
            operationType.operationContent = transfer_nhasset_operation;
            operationType.nOperationType = operations.ID_TRANSFER_NH_ASSET_OPERATION;

            transactionWithCallback.operations.add(operationType);
        }
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, accountObjectFrom);
    }


    /**
     * delete nhasset
     *
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws NhAssetNotFoundException
     */
    public String delete_nh_asset(String fee_paying_account, String password, List<String> nhasset_ids, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, NhAssetNotFoundException, KeyInvalideException, AddressFormatException, PasswordVerifyException, AuthorityException, NotAssetCreatorException {

        account_object feePayaccountObject = get_account_object(fee_paying_account);
        if (feePayaccountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (unlock(feePayaccountObject.name, password, accountDao) != OPERATE_SUCCESS && verify_password(feePayaccountObject.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();

        for (String nhasset_id : nhasset_ids) {
            nhasset_object nhasset_object = lookup_nh_asset_object(nhasset_id);
            if (nhasset_object == null) {
                throw new NhAssetNotFoundException(nhasset_id + " does not exist");
            }

            if (!TextUtils.equals(nhasset_object.nh_asset_owner.toString(), feePayaccountObject.id.toString())) {
                throw new NotAssetCreatorException(nhasset_id + " is not your asset");
            }

            operations.delete_nhasset_operation delete_nhasset_operation = new operations.delete_nhasset_operation();
            delete_nhasset_operation.fee_paying_account = feePayaccountObject.id;
            delete_nhasset_operation.nh_asset = nhasset_object.id;

            operations.operation_type operationType = new operations.operation_type();
            operationType.operationContent = delete_nhasset_operation;
            operationType.nOperationType = operations.ID_DELETE_NH_ASSET_OPERATION;

            transactionWithCallback.operations.add(operationType);
        }
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, feePayaccountObject);
    }


    /**
     * cancel nhasset order
     *
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     */
    public String cancel_nh_asset_order(String fee_paying_account, String password, String order_id, AccountDao accountDao)
            throws NetworkStatusException, AccountNotFoundException, KeyInvalideException, AddressFormatException, PasswordVerifyException, AuthorityException, OrderNotFoundException, NotAssetCreatorException {

        account_object feePayaccountObject = get_account_object(fee_paying_account);
        if (feePayaccountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        nh_asset_order_object nh_asset_order_object = get_nhasset_order_object(order_id);
        if (null == nh_asset_order_object) {
            throw new OrderNotFoundException("Order does not exist");
        }

        if (!TextUtils.equals(nh_asset_order_object.seller.toString(), feePayaccountObject.id.toString())) {
            throw new NotAssetCreatorException("You are not the order creator");
        }

        if (unlock(feePayaccountObject.name, password, accountDao) != OPERATE_SUCCESS && verify_password(feePayaccountObject.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.cancel_nhasset_order_operation cancel_nhasset_order_operation = new operations.cancel_nhasset_order_operation();
        cancel_nhasset_order_operation.order = nh_asset_order_object.id;
        cancel_nhasset_order_operation.fee_paying_account = feePayaccountObject.id;
        cancel_nhasset_order_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = cancel_nhasset_order_operation;
        operationType.nOperationType = operations.ID_CANCEL_NH_ASSET_ORDER_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, feePayaccountObject);
    }


    /**
     * buy nhasset
     *
     * @param password
     * @param fee_paying_account
     * @param order_Id
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws NhAssetNotFoundException
     */
    public String buy_nh_asset(String fee_paying_account, String password, String order_Id, AccountDao accountDao)
            throws NetworkStatusException, AccountNotFoundException, OrderNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException {

        account_object feePayAccountObject = get_account_object(fee_paying_account);
        if (feePayAccountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        nh_asset_order_object nh_asset_order_object = get_nhasset_order_object(order_Id);
        if (null == nh_asset_order_object) {
            throw new OrderNotFoundException("Order does not exist");
        }
        asset_object price_asset_object = lookup_asset_symbols(nh_asset_order_object.price.asset_id.toString());
        // verify tempory password and account model password
        if (unlock(feePayAccountObject.name, password, accountDao) != OPERATE_SUCCESS && verify_password(feePayAccountObject.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        operations.buy_nhasset_operation buy_nhasset_operation = new operations.buy_nhasset_operation();
        buy_nhasset_operation.order = nh_asset_order_object.id;
        buy_nhasset_operation.fee_paying_account = feePayAccountObject.id;
        buy_nhasset_operation.seller = nh_asset_order_object.seller;
        buy_nhasset_operation.nh_asset = nh_asset_order_object.nh_asset_id;
        buy_nhasset_operation.price_amount = String.valueOf(nh_asset_order_object.price.amount / (Math.pow(10, price_asset_object.precision)));
        buy_nhasset_operation.price_asset_id = nh_asset_order_object.price.asset_id;
        buy_nhasset_operation.price_asset_symbol = price_asset_object.symbol;
        buy_nhasset_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = buy_nhasset_operation;
        operationType.nOperationType = operations.ID_BUY_NH_ASSET_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();

        return sign_transaction(transactionWithCallback, feePayAccountObject);
    }

    /**
     * create nh asset order
     *
     * @param pending_order_nh_asset
     * @param pending_order_fee
     * @param pending_order_memo
     * @param pending_order_price
     * @param pending_order_price_symbol
     * @return
     * @throws NetworkStatusException
     * @throws NhAssetNotFoundException
     */
    public String create_nh_asset_order(String otcaccount, String seller, String password, AccountDao accountDao, String pending_order_nh_asset, String pending_order_fee, String pending_order_fee_symbol, String pending_order_memo, String pending_order_price, String pending_order_price_symbol, long pending_order_valid_time_millis) throws
            NetworkStatusException, NhAssetNotFoundException, AssetNotFoundException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException, ParseException, UnLegalInputException {

        account_object feePayAccountObject = get_account_object(seller);
        account_object otcAccount = get_account_object(otcaccount);
        nhasset_object nhasset_object = lookup_nh_asset_object(pending_order_nh_asset);
        asset_object pending_order_price_asset_object = lookup_asset_symbols(pending_order_price_symbol);
        asset_object pending_order_fee_asset = lookup_asset_symbols(pending_order_fee_symbol);

        if (TextUtils.equals(seller, otcaccount)) {
            throw new UnLegalInputException("otcaccount can not be self");
        }

        if (feePayAccountObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (otcAccount == null) {
            throw new AccountNotFoundException("otcaccount does not exist");
        }

        if (unlock(feePayAccountObject.name, password, accountDao) != OPERATE_SUCCESS && verify_password(feePayAccountObject.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        if (nhasset_object == null) {
            throw new NhAssetNotFoundException("NhAsset does not exist");
        }
        if (null == pending_order_price_asset_object) {
            throw new AssetNotFoundException("Price asset does not exist");
        }

        operations.create_nhasset_order_operation create_nhasset_order_operation = new operations.create_nhasset_order_operation();
        create_nhasset_order_operation.seller = feePayAccountObject.id;
        create_nhasset_order_operation.otcaccount = otcAccount.id;
        create_nhasset_order_operation.pending_orders_fee = pending_order_fee_asset.amount_from_string(pending_order_fee);
        create_nhasset_order_operation.nh_asset = nhasset_object.id;
        create_nhasset_order_operation.memo = pending_order_memo;
        create_nhasset_order_operation.price = pending_order_price_asset_object.amount_from_string(pending_order_price);

        long valid_time = System.currentTimeMillis() + pending_order_valid_time_millis * 1000;
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        String expiration = sDateFormat.format(valid_time);
        LogUtils.i("SimpleDateFormat", expiration);
        Date dateObject = sDateFormat.parse(expiration);
        create_nhasset_order_operation.expiration = dateObject;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_nhasset_order_operation;
        operationType.nOperationType = operations.ID_CREATE_NH_ASSET_ORDER_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();

        return sign_transaction(transactionWithCallback, feePayAccountObject);
    }


    /**
     * calculate upgrade to lifetime member
     *
     * @param upgrade_account_id_or_symbol
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     */
    public String upgrade_to_lifetime_member(String upgrade_account_id_or_symbol, String upgrade_account_password, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException {

        account_object upgrade_account_object = get_account_object(upgrade_account_id_or_symbol);
        if (upgrade_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (unlock(upgrade_account_object.name, upgrade_account_password, accountDao) != OPERATE_SUCCESS && verify_password(upgrade_account_object.name, upgrade_account_password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.upgrade_to_lifetime_member_operation upgrade_to_lifetime_member_operation = new operations.upgrade_to_lifetime_member_operation();
        upgrade_to_lifetime_member_operation.account_to_upgrade = upgrade_account_object.id;
        upgrade_to_lifetime_member_operation.upgrade_to_lifetime_member = true;
        upgrade_to_lifetime_member_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = upgrade_to_lifetime_member_operation;
        operationType.nOperationType = operations.ID_UPGRADE_TO_LIFETIME_MEMBER_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();

        return sign_transaction(transactionWithCallback, upgrade_account_object);
    }


    /**
     * calculate create child account
     *
     * @param paramEntity
     * @param registrar_account_id_or_symbol
     * @return
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     */
    public String create_child_account(CreateAccountParamEntity paramEntity, String registrar_account_id_or_symbol, String registrar_account_password, AccountDao accountDao)
            throws NetworkStatusException, AccountNotFoundException, AccountExistException, UnLegalInputException, AuthorityException, PasswordVerifyException, KeyInvalideException, AddressFormatException {

        private_key privateActiveKey = private_key.from_seed(paramEntity.getActiveSeed());
        private_key privateOwnerKey = private_key.from_seed(paramEntity.getOwnerSeed());
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());
        account_object child_account_object = lookup_account_names(paramEntity.getAccountName());
        account_object registrar_account_object = get_account_object(registrar_account_id_or_symbol);

        if (registrar_account_object == null) {
            throw new AccountNotFoundException("Registrar account does not exist");
        }

        if (unlock(registrar_account_object.name, registrar_account_password, accountDao) != OPERATE_SUCCESS && verify_password(registrar_account_object.name, registrar_account_password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        if (child_account_object != null) {
            throw new AccountExistException("Account already exist");
        }
        authority1 owner = new authority1(1, publicOwnerKeyType, 1);
        authority1 active = new authority1(1, publicActiveKeyType, 1);
        types.account_options options = new types.account_options();
        options.extensions = new HashSet<>();
        options.memo_key = publicActiveKeyType;
        options.votes = new ArrayList<>();
        operations.create_child_account_operation create_child_account_operation = new operations.create_child_account_operation();
        create_child_account_operation.registrar = registrar_account_object.id;
        create_child_account_operation.name = paramEntity.getAccountName();
        create_child_account_operation.owner = owner;
        create_child_account_operation.active = active;
        create_child_account_operation.options = options;
        create_child_account_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_child_account_operation;
        operationType.nOperationType = operations.ID_CREATE_CHILD_ACCOUNT_OPERATION;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();

        return sign_transaction(transactionWithCallback, registrar_account_object);
    }


    /**
     * get asset object by asset symbol
     *
     * @param assetsSymbolOrId ：asset symbol or asset id
     * @return asset object
     * @throws NetworkStatusException
     */
    public asset_object lookup_asset_symbols(String assetsSymbolOrId) throws NetworkStatusException {
        return mWebSocketApi.lookup_asset_symbols(assetsSymbolOrId);
    }

    /**
     * get asset by id
     *
     * @param listAssetObjectId ：asset ID
     * @return asset object
     * @throws NetworkStatusException
     */
    private List<asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        return mWebSocketApi.get_assets(listAssetObjectId);
    }


    /**
     * get account by account id
     *
     * @param account_id ：account id
     * @return account object
     * @throws NetworkStatusException
     */
    public account_object get_accounts(String account_id) throws NetworkStatusException, UnLegalInputException {
        List<object_id<account_object>> account_ids = new ArrayList<>();
        object_id<account_object> accountId = object_id.create_from_string(account_id);
        if (null == accountId) {
            throw new UnLegalInputException("Please input account id");
        }
        account_ids.add(accountId);
        return mWebSocketApi.get_accounts(account_ids).get(0);
    }


    /**
     * get account info
     *
     * @param strAccountNameOrId ：strAccountNameOrId
     * @return account info
     */
    public account_object get_account_object(String strAccountNameOrId) throws NetworkStatusException, AccountNotFoundException {

        object_id<account_object> objectId = object_id.create_from_string(strAccountNameOrId);

        List<account_object> listAccountObject = null;

        if (objectId == null) {
            listAccountObject = mWebSocketApi.lookup_account_names(strAccountNameOrId);
        } else {
            List<object_id<account_object>> listObjectId = new ArrayList<>();
            listObjectId.add(objectId);
            listAccountObject = mWebSocketApi.get_accounts(listObjectId);
        }
        if (listAccountObject.isEmpty() || null == listAccountObject.get(0)) {
            throw new AccountNotFoundException("Account does not exist");
        }
        return listAccountObject.get(0);
    }


    /**
     * get account info by account name
     *
     * @param account_name ：account_name
     * @return account info
     */
    public account_object lookup_account_names(String account_name) throws NetworkStatusException, UnLegalInputException {
        object_id<account_object> accountId = object_id.create_from_string(account_name);
        if (null != accountId) {
            throw new UnLegalInputException("Please input account name");
        }
        return mWebSocketApi.lookup_account_names(account_name).get(0);
    }


    /**
     * get_full_accounts and subscribe
     *
     * @throws NetworkStatusException
     */
    public Object get_full_accounts(String names_or_id, boolean subscribe) throws NetworkStatusException {
        return mWebSocketApi.get_full_accounts(names_or_id, subscribe);
    }


    /**
     * list all assets in chain
     *
     * @throws NetworkStatusException
     */
    public List<asset_object> list_assets(String strLowerBound, int nLimit) throws NetworkStatusException {
        return mWebSocketApi.list_assets(strLowerBound, nLimit);
    }


    /**
     * lookup_nh_asset get NH asset detail by NH asset id or hash
     *
     * @param nh_asset_ids_or_hash
     * @return
     * @throws NetworkStatusException
     * @throws NhAssetNotFoundException
     */
    public List<Object> lookup_nh_asset(List<String> nh_asset_ids_or_hash) throws NetworkStatusException, NhAssetNotFoundException {
        List<Object> nhasset_object = mWebSocketApi.lookup_nh_asset(nh_asset_ids_or_hash);
        if (nhasset_object == null || nhasset_object.size() <= 0) {
            throw new NhAssetNotFoundException("Nhasset does not exist");
        }
        return nhasset_object;
    }

    /**
     * lookup_nh_asset get NH asset detail by NH asset id or hash
     *
     * @param nh_asset_ids_or_hash
     * @return
     * @throws NetworkStatusException
     * @throws NhAssetNotFoundException
     */
    public nhasset_object lookup_nh_asset_object(String nh_asset_ids_or_hash) throws NetworkStatusException, NhAssetNotFoundException {
        List<nhasset_object> nhasset_object = mWebSocketApi.lookup_nh_asset_object(nh_asset_ids_or_hash);
        if (nhasset_object == null || nhasset_object.size() <= 0) {
            throw new NhAssetNotFoundException("Nhasset does not exist");
        }
        return nhasset_object.get(0);
    }


    /**
     * lookup_nh_asset get account NH asset by account id or name
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_account_nh_asset(String account_id_or_name, List<String> world_view_name_or_ids, int page, int pageSize) throws NetworkStatusException, AccountNotFoundException {
        account_object account_object = get_account_object(account_id_or_name);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        return mWebSocketApi.list_account_nh_asset(account_object.id.toString(), world_view_name_or_ids, page, pageSize);
    }


    /**
     * list account nh asset order
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_account_nh_asset_order(String account_id_or_name, int pageSize, int page) throws NetworkStatusException, AccountNotFoundException {
        account_object account_object = get_account_object(account_id_or_name);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        return mWebSocketApi.list_account_nh_asset_order(account_object.id.toString(), pageSize, page);
    }

    /**
     * list nh asset order
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_nh_asset_order(String asset_id_or_symbol, String world_view_name_or_ids, String baseDescribe, int pageSize, int page) throws NetworkStatusException {
        return mWebSocketApi.list_nh_asset_order(asset_id_or_symbol, world_view_name_or_ids, baseDescribe, pageSize, page);
    }

    /**
     * list nh asset order no filter options
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_nh_asset_order(int page, int pageSize) throws NetworkStatusException {
        return mWebSocketApi.list_nh_asset_order("", "", "", pageSize, page);
    }


    /**
     * Seek World View Details
     *
     * @throws NetworkStatusException
     */
    public List<world_view_object> lookup_world_view(List<String> world_view_names) throws NetworkStatusException {
        return mWebSocketApi.lookup_world_view(world_view_names);
    }

    /**
     * get Developer-Related World View
     *
     * @throws NetworkStatusException
     */
    public account_related_word_view_object get_nh_creator(String account_id_or_name) throws NetworkStatusException, AccountNotFoundException {
        account_object account_object = get_account_object(account_id_or_name);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        return mWebSocketApi.get_nh_creator(account_object.id.toString());
    }


    /**
     * Query NH assets created by developers
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_nh_asset_by_creator(String account_id, String worldview, int page, int pageSize) throws NetworkStatusException, AccountNotFoundException {
        account_object account_object = get_account_object(account_id);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        return mWebSocketApi.list_nh_asset_by_creator(account_object.id.toString(), worldview, page, pageSize);
    }


    /**
     * get account by account_names_or_ids
     *
     * @param ids ：ids
     * @return account object
     * @throws NetworkStatusException
     */
    public List<Object> get_objects(List<String> ids) throws NetworkStatusException {
        return mWebSocketApi.get_objects(ids);
    }

    /**
     * get account by account_names_or_ids
     *
     * @param id ：ids
     * @return account object
     * @throws NetworkStatusException
     */
    public Object get_objects(String id) throws NetworkStatusException {
        return mWebSocketApi.get_objects(id);
    }


    /**
     * get account operate history
     *
     * @param accountNameOrId
     * @param nLimit
     * @return
     * @throws NetworkStatusException
     */
    public List<operation_history_object> get_account_history(String accountNameOrId, int nLimit) throws NetworkStatusException, AccountNotFoundException {
        account_object objectId = get_account_object(accountNameOrId);
        object_id<operation_history_object> startId = new object_id<>(0, operation_history_object.class);
        return mWebSocketApi.get_account_history(objectId.id, startId, nLimit);
    }


    /**
     * get account operate history
     *
     * @param accountNameOrId
     * @param nLimit
     * @return
     * @throws NetworkStatusException
     */
    public List<operation_history_object> get_account_history(String accountNameOrId, String startId, String endId, int nLimit) throws NetworkStatusException, AccountNotFoundException {
        account_object objectId = get_account_object(accountNameOrId);
        return mWebSocketApi.get_account_history(objectId.id, startId, endId, nLimit);
    }


    /**
     * get all assets balances of account
     *
     * @param account_id
     * @return account balance
     * @throws NetworkStatusException
     */
    public List<asset_fee_object> get_all_account_balances(String account_id) throws NetworkStatusException, UnLegalInputException, AccountNotFoundException {

        object_id<account_object> accountId = object_id.create_from_string(account_id);
        if (null == accountId) {
            account_object account_object = lookup_account_names(account_id);
            if (null == account_object) {
                throw new AccountNotFoundException("Account does not exist");
            }
            account_id = account_object.id.toString();
        }

        List<asset_fee_object> assets = mWebSocketApi.get_all_account_balances(account_id);
        List<asset_fee_object> assetsObjects = new ArrayList<>();
        for (asset_fee_object asset : assets) {
            asset_object asset_object = lookup_asset_symbols(asset.asset_id.toString());
            asset.amount = String.valueOf(Double.valueOf(asset.amount) / (Math.pow(10, asset_object.precision)));
            assetsObjects.add(asset);
        }
        return assetsObjects;
    }

    /**
     * get_account_balances。
     *
     * @param account_id
     * @return account balance
     * @throws NetworkStatusException
     */
    public asset_fee_object get_account_balances(String account_id, String assetsId) throws NetworkStatusException, UnLegalInputException, AssetNotFoundException, AccountNotFoundException {

        object_id<account_object> accountId = object_id.create_from_string(account_id);
        if (null == accountId) {
            account_object account_object = lookup_account_names(account_id);
            if (null == account_object) {
                throw new AccountNotFoundException("Account does not exist");
            }
            account_id = account_object.id.toString();
        }

        asset_object asset_object = mWebSocketApi.lookup_asset_symbols(assetsId);
        if (null == asset_object) {
            throw new AssetNotFoundException("Asset does not exist");
        }

        List<Object> objects = new ArrayList<>();
        objects.add(asset_object.id.toString());
        List<asset_fee_object> assets = mWebSocketApi.get_account_balances(account_id, objects);
        asset_fee_object asset = assets.get(0);
        asset.amount = String.valueOf(Double.valueOf(asset.amount) / (Math.pow(10, asset_object.precision)));
        return asset;
    }


    /**
     * search  current dynamic_global_property_object
     *
     * @return
     * @throws NetworkStatusException
     */
    public dynamic_global_property_object get_dynamic_global_properties() throws NetworkStatusException {
        return mWebSocketApi.get_dynamic_global_properties();
    }


    /**
     * search  current  global_property_object
     *
     * @return
     * @throws NetworkStatusException
     */
    public global_property_object get_global_properties() throws NetworkStatusException {
        return mWebSocketApi.get_global_properties();
    }


    /**
     * get block header。
     *
     * @throws NetworkStatusException
     */
    public block_header get_block_header(String nBlockNumber) throws NetworkStatusException {
        return mWebSocketApi.get_block_header(nBlockNumber);
    }

    /**
     * get block header。
     *
     * @throws NetworkStatusException
     */
    public block_info get_block(String nBlockNumber) throws NetworkStatusException {
        return mWebSocketApi.get_block(nBlockNumber);
    }
    public Object getBlockNumber(String nBlockNumber) throws NetworkStatusException {
        return mWebSocketApi.getBlockNumber(nBlockNumber);
    }

    /**
     * get transaction in block info
     *
     * @throws NetworkStatusException
     */
    public transaction_in_block_info get_transaction_in_block_info(String tr_id) throws NetworkStatusException {
        return mWebSocketApi.get_transaction_in_block_info(tr_id);
    }


    /**
     * get transaction by tx_id
     *
     * @throws NetworkStatusException
     */
    public Object get_transaction_by_id(String tr_id) throws NetworkStatusException {
        return mWebSocketApi.get_transaction_by_id(tr_id);
    }


    /**
     * decrypt memo
     *
     * @param mMemoJson
     * @return
     */
    public void decrypt_memo_message(String accountName, String password, String mMemoJson, AccountDao accountDao, IBcxCallBack callBack) {
        try {
            int result = unlock(accountName, password, accountDao);
            if (result == ERROR_OBJECT_NOT_FOUND) {
                rspText = new ResponseData(ERROR_OBJECT_NOT_FOUND, "Account does not exist", null).toString();
                callBack.onReceiveValue(rspText);
                return;
            } else if (result == ERROR_WRONG_PASSWORD) {
                rspText = new ResponseData(ERROR_WRONG_PASSWORD, "Wrong password", null).toString();
                callBack.onReceiveValue(rspText);
                return;
            }
            memo_data memoData = global_config_object.getInstance().getGsonBuilder().create().fromJson(mMemoJson, memo_data.class);
            String strMessage = null;
            types.private_key_type privateKeyType = mHashMapPub2Private.get(memoData.to);
            if (privateKeyType != null) {
                strMessage = memoData.get_message(privateKeyType.getPrivateKey(), memoData.from.getPublicKey());
            } else {
                privateKeyType = mHashMapPub2Private.get(memoData.from);
                if (privateKeyType != null) {
                    strMessage = memoData.get_message(privateKeyType.getPrivateKey(), memoData.to.getPublicKey());
                }
            }
            rspText = new ResponseData(OPERATE_SUCCESS, "success", strMessage).toString();
            callBack.onReceiveValue(rspText);
        } catch (JsonSyntaxException e) {
            rspText = new ResponseData(ERROR_PARAMETER_DATA_TYPE, "Please check parameter type", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (KeyInvalideException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AddressFormatException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        }
    }


    /**
     * encrypt private key ;
     *
     * @return keyStore
     */
    private void encrypt_keys(String name, String accountId, String password, String accountType, AccountDao accountDao) {
        mCheckSum = sha512_object.create_from_string(password);
        plain_keys data = new plain_keys();
        data.keys = new HashMap<>();

        for (Map.Entry<types.public_key_type, types.private_key_type> entry : mHashMapPub2Private.entrySet()) {
            data.keys.put(entry.getKey(), entry.getValue().toString());
        }

        data.checksum = mCheckSum;
        data_stream_size_encoder sizeEncoder = new data_stream_size_encoder();
        data.write_to_encoder(sizeEncoder);
        data_stream_encoder encoder = new data_stream_encoder(sizeEncoder.getSize());
        data.write_to_encoder(encoder);
        byte[] byteKey = new byte[32];
        System.arraycopy(mCheckSum.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(mCheckSum.hash, 32, ivBytes, 0, ivBytes.length);
        ByteBuffer byteResult = aes.encrypt(byteKey, ivBytes, encoder.getData());
        mWalletObject.cipher_keys = byteResult;
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        accountDao.insertAccount(name, accountId, gson.toJson(mWalletObject), accountType, CocosBcxApiWrapper.chainId);
    }


    /**
     * save_account account : encrypt and save account
     *
     * @return
     */
    public void save_account(String name, String accountId, String password, String accountType, AccountDao accountDao) {
        // encrypt and save account
        encrypt_keys(name, accountId, password, accountType, accountDao);
        // clear data in memory
        lock();
    }


    /**
     * clear private data out of memory;
     *
     * @return
     */
    public void lock() {
        mCheckSum = new sha512_object();
        mHashMapPub2Private.clear();
    }


    /**
     * unlock account :means load private key in memory
     *
     * @param accountName
     * @param strPassword
     * @param accountDao
     * @return
     */
    private int unlock(String accountName, String strPassword, AccountDao accountDao) throws KeyInvalideException, AddressFormatException {

        assert (strPassword.length() > 0);
        sha512_object passwordHash = sha512_object.create_from_string(strPassword);
        byte[] byteKey = new byte[32];
        System.arraycopy(passwordHash.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(passwordHash.hash, 32, ivBytes, 0, ivBytes.length);
        // get keystore from db
        AccountEntity.AccountBean accountBean = get_dao_account_by_name(accountName, accountDao);
        if (accountBean == null || TextUtils.isEmpty(accountBean.getKeystore())) {
            return ERROR_OBJECT_NOT_FOUND;
        }
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        mWalletObject = gson.fromJson(accountBean.getKeystore(), wallet_object.class);
        ByteBuffer byteDecrypt = aes.decrypt(byteKey, ivBytes, mWalletObject.cipher_keys.array());
        if (byteDecrypt == null || byteDecrypt.array().length == 0) {
            return ERROR_WRONG_PASSWORD;
        }

        plain_keys dataResult = plain_keys.from_input_stream(new ByteArrayInputStream(byteDecrypt.array()));
        for (Map.Entry<types.public_key_type, String> entry : dataResult.keys.entrySet()) {
            types.private_key_type privateKeyType = new types.private_key_type(entry.getValue());
            mHashMapPub2Private.put(entry.getKey(), privateKeyType);
        }
        mCheckSum = passwordHash;
        if (passwordHash.equals(dataResult.checksum)) {
            return OPERATE_SUCCESS;
        } else {
            return ERROR_WRONG_PASSWORD;
        }
    }


    /**
     * export private key
     *
     * @param accountName account name
     * @param password    the key you set to encrypt your private key;
     * @return
     */
    public void export_private_key(String accountName, String password, AccountDao accountDao, IBcxCallBack callBack) {
        try {
            // get keystore
            AccountEntity.AccountBean accountBean = get_dao_account_by_name(accountName, accountDao);
            if (null == accountBean) {
                rspText = new ResponseData(ERROR_UNLOCK_ACCOUNT, "Please login in or import the private key first", null).toString();
                callBack.onReceiveValue(rspText);
                return;
            }
            Gson gson = global_config_object.getInstance().getGsonBuilder().create();
            mWalletObject = gson.fromJson(accountBean.getKeystore(), wallet_object.class);
            //decrypt keystore return private keys
            Map<String, String> private_keys = decrypt_keystore_callback_private_key(password);
            if (private_keys.size() <= 0) {
                private_keys = verify_password(accountName, password);
            }
            rspText = new ResponseData(OPERATE_SUCCESS, "success", private_keys).toString();
            callBack.onReceiveValue(rspText);
            // clear data out of memory;
            lock();
        } catch (JsonSyntaxException e) {
            rspText = new ResponseData(ERROR_PARAMETER_DATA_TYPE, "Please check parameter types", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (NetworkStatusException e) {
            rspText = new ResponseData(ERROR_NETWORK_FAIL, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AccountNotFoundException e) {
            rspText = new ResponseData(ERROR_OBJECT_NOT_FOUND, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (PasswordVerifyException e) {
            rspText = new ResponseData(ERROR_WRONG_PASSWORD, e.getMessage(), null).toString();
            callBack.onReceiveValue(rspText);
        } catch (KeyInvalideException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        } catch (AddressFormatException e) {
            rspText = new ResponseData(ERROR_INVALID_PRIVATE_KEY, "Please enter the correct private key", null).toString();
            callBack.onReceiveValue(rspText);
        }
    }


    /**
     * decrypt keyStore
     *
     * @param strPassword
     * @return privateKeys
     */
    private Map<String, String> decrypt_keystore_callback_private_key(String strPassword) throws KeyInvalideException, AddressFormatException {

        assert (strPassword.length() > 0);
        sha512_object passwordHash = sha512_object.create_from_string(strPassword);
        byte[] byteKey = new byte[32];
        System.arraycopy(passwordHash.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(passwordHash.hash, 32, ivBytes, 0, ivBytes.length);

        ByteBuffer byteDecrypt = aes.decrypt(byteKey, ivBytes, this.mWalletObject.cipher_keys.array());
        if (byteDecrypt == null || byteDecrypt.array().length == 0) {
            return new HashMap<>();
        }
        mCheckSum = passwordHash;
        plain_keys dataResult = plain_keys.from_input_stream(new ByteArrayInputStream(byteDecrypt.array()));
        Map<String, String> private_key = new HashMap();

        for (Map.Entry<types.public_key_type, String> entry : dataResult.keys.entrySet()) {
            types.private_key_type privateKeyType = new types.private_key_type(entry.getValue());
            mHashMapPub2Private.put(entry.getKey(), privateKeyType);
            private_key.put(entry.getKey().toString(), entry.getValue());
        }
        if (passwordHash.equals(dataResult.checksum)) {
            return private_key;
        } else {
            return new HashMap<>();
        }
    }

    /**
     * createLimitOrder
     *
     * @return
     */
    public String create_limit_order(String seller, String password, String transactionPair, int type, int validTime, BigDecimal price, BigDecimal amount, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, UnLegalInputException {

        if (TextUtils.isEmpty(transactionPair)) {
            throw new UnLegalInputException("transactionPair can not be empty");
        }

        if (!transactionPair.contains("_")) {
            throw new UnLegalInputException("transactionPair is not right");
        }

        String[] paris = transactionPair.split("_");

        asset_object firstAssetObject = lookup_asset_symbols(paris[0].trim());

        asset_object secondAssetObject = lookup_asset_symbols(paris[1].trim());


        if (unlock(seller, password, accountDao) != OPERATE_SUCCESS && verify_password(seller, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        if (null == firstAssetObject) {
            throw new AssetNotFoundException(paris[0].trim() + "does not exist");
        }

        if (null == secondAssetObject) {
            throw new AssetNotFoundException(paris[1].trim() + "does not exist");
        }

        if (validTime > 200000) {
            throw new InputMismatchException("error: validTime bigger than 200000");
        }

        account_object sellerObject = get_account_object(seller);
        if (sellerObject == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        operations.create_limit_order_operation create_limit_order_operation = new operations.create_limit_order_operation();
        create_limit_order_operation.seller = sellerObject.id;
        if (type == 0) {
            create_limit_order_operation.amount_to_sell = firstAssetObject.amount_from_string(amount.toString());
            create_limit_order_operation.min_to_receive = secondAssetObject.amount_from_string(price.multiply(amount).toString());
        } else if (type == 1) {
            create_limit_order_operation.amount_to_sell = secondAssetObject.amount_from_string(price.multiply(amount).toString());
            create_limit_order_operation.min_to_receive = firstAssetObject.amount_from_string(amount.toString());
        } else {
            throw new InputMismatchException("type not right");
        }

        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObject);
        cal.add(Calendar.DATE, validTime);
        create_limit_order_operation.expiration = cal.getTime();
        create_limit_order_operation.fill_or_kill = false;
        create_limit_order_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_limit_order_operation;
        operationType.nOperationType = operations.ID_CREATE_LIMIT_ORDER;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, sellerObject);
    }


    /**
     * cancel limit order
     *
     * @return
     */
    public String cancel_limit_order(String fee_paying_account, String password, String limit_order_id, String fee_pay_asset_symbol_or_id, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException, OrderNotFoundException, NotAssetCreatorException {

        // get asset object
        asset_object feeAssetObject = lookup_asset_symbols(fee_pay_asset_symbol_or_id);

        account_object fee_paying_account_object = get_account_object(fee_paying_account);

        limit_orders_object limit_order_object = get_limit_order_object(limit_order_id);

        if (limit_order_object == null) {
            throw new OrderNotFoundException("order does not exist");
        }

        if (fee_paying_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (feeAssetObject == null) {
            throw new AssetNotFoundException("Fee pay asset does not exist");
        }

        if (!TextUtils.equals(limit_order_object.seller.toString(), fee_paying_account_object.id.toString())) {
            throw new NotAssetCreatorException("You are not the order creator");
        }

        if (unlock(fee_paying_account, password, accountDao) != OPERATE_SUCCESS && verify_password(fee_paying_account, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.cancel_limit_order_operation cancel_limit_order_operation = new operations.cancel_limit_order_operation();
        cancel_limit_order_operation.fee_paying_account = fee_paying_account_object.id;
        cancel_limit_order_operation.order = limit_order_object.id;
        cancel_limit_order_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = cancel_limit_order_operation;
        operationType.nOperationType = operations.ID_CANCEL_LIMIT_ORDER;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, fee_paying_account_object);
    }


    /**
     * get_limit_orders
     *
     * @return get_dao_account_by_name
     */
    public List<limit_orders_object> get_limit_orders(String transactionPair, int nLimit) throws NetworkStatusException, AssetNotFoundException, UnLegalInputException {

        if (TextUtils.isEmpty(transactionPair)) {
            throw new UnLegalInputException("transactionPair can not be empty");
        }

        if (!transactionPair.contains("_")) {
            throw new UnLegalInputException("transactionPair is not right");
        }

        String[] paris = transactionPair.split("_");

        asset_object firstAssetObject = lookup_asset_symbols(paris[0].trim());

        asset_object secondAssetObject = lookup_asset_symbols(paris[1].trim());

        if (null == firstAssetObject) {
            throw new AssetNotFoundException(paris[0].trim() + "does not exist");
        }

        if (null == secondAssetObject) {
            throw new AssetNotFoundException(paris[1].trim() + "does not exist");
        }
        return mWebSocketApi.get_limit_orders(firstAssetObject.id.toString(), secondAssetObject.id.toString(), nLimit);
    }


    /**
     * get_fill_order_history
     */
    public List<fill_order_history_object> get_fill_order_history(String transactionPair, int nLimit) throws NetworkStatusException, AssetNotFoundException, UnLegalInputException {

        if (TextUtils.isEmpty(transactionPair)) {
            throw new UnLegalInputException("transactionPair can not be empty");
        }

        if (!transactionPair.contains("_")) {
            throw new UnLegalInputException("transactionPair is not right");
        }

        String[] paris = transactionPair.split("_");

        asset_object firstAssetObject = lookup_asset_symbols(paris[0].trim());

        asset_object secondAssetObject = lookup_asset_symbols(paris[1].trim());

        if (null == firstAssetObject) {
            throw new AssetNotFoundException(paris[0].trim() + "does not exist");
        }

        if (null == secondAssetObject) {
            throw new AssetNotFoundException(paris[1].trim() + "does not exist");
        }
        return mWebSocketApi.get_fill_order_history(firstAssetObject.id.toString(), secondAssetObject.id.toString(), nLimit);
    }


    /**
     * get market history
     */
    public List<market_history_object> get_market_history(String quote_asset_symbol_or_id, String base_asset_symbol_or_id, long seconds, String start_time, String end_time) throws NetworkStatusException, AssetNotFoundException {

        asset_object quoteAssetObject = lookup_asset_symbols(quote_asset_symbol_or_id);

        asset_object baseAssetObject = lookup_asset_symbols(base_asset_symbol_or_id);

        if (null == quoteAssetObject) {
            throw new AssetNotFoundException("quote asset does not exist");
        }

        if (null == baseAssetObject) {
            throw new AssetNotFoundException("base asset does not exist");
        }

        return mWebSocketApi.get_market_history(quoteAssetObject.id.toString(), baseAssetObject.id.toString(), seconds, start_time, end_time);
    }


    /**
     * update feed_object product
     */
    public String update_feed_product(String issuer, String password, String asset_symbol_or_id, List<String> products, String fee_asset_symbol, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, UnLegalInputException, AuthorityException {

        account_object issuerObject = get_account_object(issuer);
        if (issuerObject == null) {
            throw new AccountNotFoundException("issuer does not exist");
        }

        asset_object fee_asset_symbol_object = lookup_asset_symbols(fee_asset_symbol);
        if (null == fee_asset_symbol_object) {
            throw new AssetNotFoundException("fee asset does not exist");
        }

        asset_object asset_to_update_object = lookup_asset_symbols(asset_symbol_or_id);
        if (null == asset_to_update_object) {
            throw new AssetNotFoundException("issue asset does not exist");
        }

        if (null == products || products.size() <= 0) {
            throw new UnLegalInputException("products can not be null");
        }

        List<object_id<account_object>> productIds = new ArrayList<>();

        for (String product : products) {
            account_object productObject = get_account_object(product);
            if (productObject == null) {
                productIds.clear();
                throw new AccountNotFoundException(product + "does not exist");
            }
            productIds.add(productObject.id);
        }

        if (unlock(issuer, password, accountDao) != OPERATE_SUCCESS && verify_password(issuer, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.update_feed_product_operation update_feed_product_operation = new operations.update_feed_product_operation();
        update_feed_product_operation.issuer = issuerObject.id;
        update_feed_product_operation.asset_to_update = asset_to_update_object.id;
        update_feed_product_operation.new_feed_producers = productIds;
        update_feed_product_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = update_feed_product_operation;
        operationType.nOperationType = operations.ID_UPDATE_FEED_PRODUCT;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, issuerObject);
    }


    /**
     * publish feed
     */
    public String publish_feed(String publisher, String password, String base_asset_symbol, String price,
                               BigDecimal maintenance_collateral_ratio, BigDecimal maximum_short_squeeze_ratio,
                               String core_exchange_rate_base, String core_exchange_rate_quote, String core_exchange_quote_symbol, String fee_asset_symbol, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {

        account_object publisherObject = get_account_object(publisher);
        if (publisherObject == null) {
            throw new AccountNotFoundException("publisher does not exist");
        }

        asset_object quote_asset_symbol_object = lookup_asset_symbols(core_exchange_quote_symbol);
        if (null == quote_asset_symbol_object) {
            throw new AssetNotFoundException("quote asset does not exist");
        }

        asset_object base_asset_symbol_object = lookup_asset_symbols(base_asset_symbol);
        if (null == base_asset_symbol_object) {
            throw new AssetNotFoundException("base asset does not exist");
        }

        asset_object fee_asset_symbol_object = lookup_asset_symbols(fee_asset_symbol);
        if (null == fee_asset_symbol_object) {
            throw new AssetNotFoundException("fee asset does not exist");
        }

        if (unlock(publisher, password, accountDao) != OPERATE_SUCCESS && verify_password(publisher, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.publish_feed_operation publish_feed_operation = new operations.publish_feed_operation();
        feed_object feed_object = new feed_object();
        feed_object.CoreExchangeRateBean core_exchange_rate = new feed_object.CoreExchangeRateBean();
        feed_object.SettlementPriceBean settlement_price = new feed_object.SettlementPriceBean();
        core_exchange_rate.base = base_asset_symbol_object.amount_from_string(core_exchange_rate_base);
        core_exchange_rate.quote = quote_asset_symbol_object.amount_from_string(core_exchange_rate_quote);
        settlement_price.base = base_asset_symbol_object.amount_from_string(price);
        settlement_price.quote = quote_asset_symbol_object.amount_from_string(price);
        feed_object.core_exchange_rate = core_exchange_rate;
        feed_object.settlement_price = settlement_price;
        feed_object.maintenance_collateral_ratio = maintenance_collateral_ratio.multiply(BigDecimal.valueOf(1000));
        feed_object.maximum_short_squeeze_ratio = maximum_short_squeeze_ratio.multiply(BigDecimal.valueOf(1000));
        publish_feed_operation.publisher = publisherObject.id;
        publish_feed_operation.asset_id = base_asset_symbol_object.id;
        publish_feed_operation.feed = feed_object;
        publish_feed_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = publish_feed_operation;
        operationType.nOperationType = operations.ID_PUBLISH_FEED;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, publisherObject);
    }

    /**
     * asset settle
     */
    public String asset_settle(String account, String password, String settle_asset_symbol, String settle_asset_amount, String fee_asset_symbol, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {

        account_object account_object = get_account_object(account);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        asset_object settle_asset_object = lookup_asset_symbols(settle_asset_symbol);
        if (null == settle_asset_object) {
            throw new AssetNotFoundException("settle asset does not exist");
        }

        asset_object fee_asset_object = lookup_asset_symbols(fee_asset_symbol);
        if (null == fee_asset_object) {
            throw new AssetNotFoundException("fee asset does not exist");
        }

        if (unlock(account, password, accountDao) != OPERATE_SUCCESS && verify_password(account, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.asset_settle_operation asset_settle_operation = new operations.asset_settle_operation();
        asset_settle_operation.account = account_object.id;
        asset_settle_operation.amount = settle_asset_object.amount_from_string(settle_asset_amount);
        asset_settle_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = asset_settle_operation;
        operationType.nOperationType = operations.ID_ASSET_SETTLE;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, account_object);
    }

    /**
     * global asset settle
     */
    public String global_asset_settle(String issuer, String password, String settle_asset_symbol, String settle_asset_price, String fee_asset_symbol, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {

        account_object issuer_object = get_account_object(issuer);
        if (issuer_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        asset_object settle_asset_object = lookup_asset_symbols(settle_asset_symbol);
        if (null == settle_asset_object) {
            throw new AssetNotFoundException("settle asset does not exist");
        }

        asset_object fee_asset_object = lookup_asset_symbols(fee_asset_symbol);
        if (null == fee_asset_object) {
            throw new AssetNotFoundException("fee asset does not exist");
        }

        if (unlock(issuer, password, accountDao) != OPERATE_SUCCESS && verify_password(issuer, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.global_asset_settle_operation global_asset_settle_operation = new operations.global_asset_settle_operation();
        global_asset_settle_operation.issuer = issuer_object.id;
        global_asset_settle_operation.asset_to_settle = settle_asset_object.id;
        settle_price_object settle_price_object = new settle_price_object();
        settle_price_object.base = settle_asset_object.amount_from_string("100000000");
        settle_price_object.quote = fee_asset_object.amount_from_string(settle_asset_price);
        global_asset_settle_operation.settle_price = settle_price_object;
        global_asset_settle_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = global_asset_settle_operation;
        operationType.nOperationType = operations.ID_GLOBAL_ASSET_SETTLE;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, issuer_object);
    }


    /**
     * estimation_gas
     *
     * @param amount
     * @return estimation_gas
     */
    public asset_fee_object estimation_gas(String amount) throws NetworkStatusException, AssetNotFoundException {
        asset_object asset_object = lookup_asset_symbols(CocosBcxApiWrapper.coreAsset);
        if (null == asset_object) {
            throw new AssetNotFoundException("COCOS asset does not exist");
        }
        asset asset = asset_object.amount_from_string(amount);
        asset_fee_object gas_asset = mWebSocketApi.estimation_gas(asset);
        gas_asset.amount = String.valueOf(Double.valueOf(gas_asset.amount) / (Math.pow(10, asset_object.precision)));
        return gas_asset;
    }

    /**
     * update_collateral_for_gas
     *
     * @param mortgagor
     * @return update_collateral_for_gas
     */
    public String update_collateral_for_gas(String mortgagor, String password, String beneficiary, String amount, AccountDao accountDao) throws NetworkStatusException, AssetNotFoundException, AccountNotFoundException, AuthorityException, PasswordVerifyException, KeyInvalideException {

        account_object mortgagor_object = get_account_object(mortgagor);
        if (mortgagor_object == null) {
            throw new AccountNotFoundException("Mortgagor account does not exist");
        }
        account_object beneficiary_object = get_account_object(beneficiary);
        if (beneficiary_object == null) {
            throw new AccountNotFoundException("Beneficiary does not exist");
        }
        asset_object asset_object = lookup_asset_symbols(CocosBcxApiWrapper.coreAsset);
        if (null == asset_object) {
            throw new AssetNotFoundException("COCOS asset does not exist");
        }
        if (unlock(mortgagor_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(mortgagor_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.update_collateral_for_gas_operation update_collateral_for_gas_operation = new operations.update_collateral_for_gas_operation();
        update_collateral_for_gas_operation.beneficiary = beneficiary_object.id;
        update_collateral_for_gas_operation.mortgager = mortgagor_object.id;
        update_collateral_for_gas_operation.collateral = asset_object.amount_from_string(amount).amount;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = update_collateral_for_gas_operation;
        operationType.nOperationType = operations.ID_UPDATE_COLLATERAL_FOR_GAS;

        signed_operate transactionWithCallback = new signed_operate();
        transactionWithCallback.operations = new ArrayList<>();
        transactionWithCallback.operations.add(operationType);
        transactionWithCallback.extensions = new HashSet<>();
        return sign_transaction(transactionWithCallback, mortgagor_object);
    }


    /**
     * receive_vesting_balances
     *
     * @param accountNameOrId "
     * @param password
     * @param accountDao
     * @return get_vesting_balances
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    public String receive_vesting_balances(String accountNameOrId, String password, String awardId, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException, AssetNotFoundException, NoRewardAvailableException {

        account_object account_object = get_account_object(accountNameOrId);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (unlock(account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        Object vesting_balances_result = get_objects(awardId);
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        vesting_balances_object vesting_balances_object = gson.fromJson(gson.toJson(vesting_balances_result), vesting_balances_object.class);
        if (null == vesting_balances_object) {
            throw new NoRewardAvailableException("No reward available");
        }
        asset_object asset_object = lookup_asset_symbols(vesting_balances_object.balance.asset_id.toString());
        if (null == asset_object) {
            throw new AssetNotFoundException("Asset does not exist");
        }
        JsonElement jsonElement = vesting_balances_object.policy.get(1);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement coin_seconds_earnedStr = jsonObject.get("coin_seconds_earned");
        double coin_seconds_earned = coin_seconds_earnedStr.getAsDouble();
        JsonElement vesting_seconds_str = jsonObject.get("vesting_seconds");
        double vesting_seconds = vesting_seconds_str.getAsDouble();

        JsonElement coin_seconds_earned_last_update = jsonObject.get("coin_seconds_earned_last_update");
        String coin_seconds_earned_last_update_str = coin_seconds_earned_last_update.getAsString();
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        Date beforeDate = null;
        try {
            Date beforeDateObject = sDateFormat.parse(coin_seconds_earned_last_update_str);
            beforeDate = DateUtil.formDate(beforeDateObject);
            String dateString = sDateFormat.format(new Date());
            currentDateObject = sDateFormat.parse(dateString);
            long past_seconds = (currentDateObject.getTime() - beforeDate.getTime()) / 1000;
            double total_earned = NumberUtil.mul1(vesting_seconds, vesting_balances_object.balance.amount);
            double new_earned = NumberUtil.mul1(NumberUtil.div(past_seconds, vesting_seconds, 5), total_earned);
            double old_earned = coin_seconds_earned;
            double earned = NumberUtil.add(old_earned, new_earned);
            double availablePercent = NumberUtil.div(earned, NumberUtil.mul1(vesting_seconds, vesting_balances_object.balance.amount), 5);
            if (availablePercent >= 1) {
                availablePercent = 1;
            }
            double available_balance_amounts;
            double available_balance_amount = NumberUtil.div(NumberUtil.mul1(availablePercent, vesting_balances_object.balance.amount), Math.pow(10, asset_object.precision), 5);
            if (availablePercent >= 1) {
                available_balance_amounts = available_balance_amount;
            } else {
                available_balance_amounts = available_balance_amount - (available_balance_amount * 0.02);
            }
            operations.receive_vesting_balances_operation receive_vesting_balances = new operations.receive_vesting_balances_operation();
            receive_vesting_balances.vesting_balance = vesting_balances_object.id;
            receive_vesting_balances.owner = account_object.id;
            receive_vesting_balances.amount = asset_object.amount_from_string(String.valueOf(available_balance_amounts));

            operations.operation_type operationType = new operations.operation_type();
            operationType.operationContent = receive_vesting_balances;
            operationType.nOperationType = operations.ID_RECEIVE_VESTING_BALANCES;

            signed_operate signed_operate = new signed_operate();
            signed_operate.operations = new ArrayList<>();
            signed_operate.operations.add(operationType);
            signed_operate.extensions = new HashSet<>();
            return sign_transaction(signed_operate, account_object);
        } catch (ParseException e) {
            throw new UnknownError("Date parse error");
        } catch (ArithmeticException e) {
            throw new NoRewardAvailableException("No reward available");
        }
    }


    /**
     * get_vesting_balances
     *
     * @param accountNameOrId
     * @return get_vesting_balances
     */
    public vesting_balances_result get_vesting_balances(String accountNameOrId) throws NetworkStatusException, AccountNotFoundException, NoRewardAvailableException, AssetNotFoundException {
        account_object account_object = get_account_object(accountNameOrId);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        List<vesting_balances_object> vesting_balances_objects = mWebSocketApi.get_vesting_balances(account_object.id.toString());
        if (null == vesting_balances_objects || vesting_balances_objects.size() <= 0) {
            throw new NoRewardAvailableException("No reward available");
        }

        vesting_balances_object vestingBalancesObject = vesting_balances_objects.get(0);
        JsonElement jsonElement = vestingBalancesObject.policy.get(1);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement coin_seconds_earned = jsonObject.get("coin_seconds_earned");
        JsonElement vesting_seconds = jsonObject.get("vesting_seconds");
        double gas_amount = coin_seconds_earned.getAsDouble() / vesting_seconds.getAsDouble();
        double available_percent = gas_amount / vestingBalancesObject.balance.amount;

        asset_object asset_object = lookup_asset_symbols(vestingBalancesObject.balance.asset_id.toString());
        if (null == asset_object) {
            throw new AssetNotFoundException("Asset does not exist");
        }
        long require_coindays = new BigDecimal(vestingBalancesObject.balance.amount / Math.pow(10, asset_object.precision)).setScale(0, BigDecimal.ROUND_HALF_EVEN).longValue();
        double return_cash = vestingBalancesObject.balance.amount / Math.pow(10, asset_object.precision);

        vesting_balances_result vesting_balances_results = new vesting_balances_result();
        vesting_balances_results.id = vestingBalancesObject.id;
        vesting_balances_results.return_cash = return_cash;
        vesting_balances_results.earned_coindays = String.valueOf(BigDecimal.valueOf(require_coindays * available_percent).setScale(0, BigDecimal.ROUND_HALF_EVEN));
        vesting_balances_results.require_coindays = String.valueOf(require_coindays);
        vesting_balances_results.remaining_days = String.valueOf(BigDecimal.valueOf(1 - available_percent).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        vesting_balances_results.available_percent = String.valueOf(BigDecimal.valueOf(available_percent * 100).setScale(2, BigDecimal.ROUND_UP));
        vesting_balances_result.AvailableBalanceBean availableBalanceBean = new vesting_balances_result.AvailableBalanceBean();
        availableBalanceBean.amount = String.valueOf(vestingBalancesObject.balance.amount * available_percent / Math.pow(10, asset_object.precision));
        availableBalanceBean.asset_id = asset_object.id.toString();
        availableBalanceBean.symbol = asset_object.symbol;
        availableBalanceBean.precision = asset_object.precision;
        vesting_balances_results.available_balance = availableBalanceBean;
        return vesting_balances_results;
    }


    /**
     * get_committee_members
     *
     * @return get_committee_members
     */
    List<committee_object_result> get_committee_members(String support_account) throws NetworkStatusException, UnLegalInputException, AccountNotFoundException {
        Object committee_base_object = get_objects("2.0.0");
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        vote_object vote_object = gson.fromJson(gson.toJson(committee_base_object), vote_object.class);
        LogUtils.i("vote_object", vote_object.active_committee_members.get(0).toString());
        List<String> stringList = IDHelper.getIds(1, 5, 0, 50);
        LogUtils.i("IDhelpergetIds", stringList.get(2));
        List<Object> objectList = get_objects(stringList);
        List<committee_object> committee_objects = gson.fromJson(gson.toJson(objectList), new TypeToken<List<committee_object>>() {
        }.getType());
        LogUtils.i("committee_objects", committee_objects.get(0).committee_member_account.toString());
        account_object support_object = get_account_object(support_account);
        List<committee_object_result> committee_object_results = new ArrayList<>();
        for (committee_object committee_object : committee_objects) {
            if (null == committee_object) {
                return committee_object_results;
            }
            committee_object_result committee_object_result = new committee_object_result();
            account_object account_object = get_accounts(committee_object.committee_member_account.toString());
            committee_object_result.account_id = account_object.id.toString();
            committee_object_result.account_name = account_object.name;
            committee_object_result.committee_id = committee_object.id.toString();
            committee_object_result.type = "committee";
            committee_object_result.url = committee_object.url;
            committee_object_result.vote_id = committee_object.vote_id;
            committee_object_result.votes = String.valueOf(committee_object.total_votes / Math.pow(10, 5));
            committee_object_result.active = false;
            committee_object_result.supported = false;
            for (object_id<committee_object> committeeObject : vote_object.active_committee_members) {
                if (TextUtils.equals(committeeObject.toString(), committee_object.id.toString())) {
                    committee_object_result.active = true;
                }
            }
            List<committee_object_result.SupportersBean> supportersBeans = new ArrayList<>();
            for (Map.Entry<object_id<account_object>, asset_fee_object> objectHashMap : committee_object.supporters.entrySet()) {
                committee_object_result.SupportersBean supportersBean = new committee_object_result.SupportersBean();
                supportersBean.account_id = objectHashMap.getKey();
                asset_fee_object asset_fee_object = objectHashMap.getValue();
                supportersBean.amount_raw = asset_fee_object;
                asset_object asset_object = lookup_asset_symbols(asset_fee_object.asset_id.toString());
                supportersBean.amount_text = new BigDecimal(asset_fee_object.amount).divide(new BigDecimal(Math.pow(10, asset_object.precision))) + asset_object.symbol;
                object_id<account_object> accountObjectobjectId = objectHashMap.getKey();
                if (TextUtils.equals(support_object.id.toString(), accountObjectobjectId.toString())) {
                    committee_object_result.supported = true;
                }
                supportersBeans.add(supportersBean);
            }
            committee_object_result.supporters = supportersBeans;
            committee_object_results.add(committee_object_result);
        }
        return committee_object_results;
    }


    /**
     * get_witnesses_members
     *
     * @return get_witnesses_members
     */
    List<witnesses_object_result> get_witnesses_members(String support_account) throws NetworkStatusException, UnLegalInputException, AccountNotFoundException {
        Object witnesses_base_object = get_objects("2.0.0");
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        vote_object vote_object = gson.fromJson(gson.toJson(witnesses_base_object), vote_object.class);
        List<String> stringList = IDHelper.getIds(1, 6, 1, 50);
        List<Object> objectList = get_objects(stringList);
        List<witnesses_object> witnesses_objects = gson.fromJson(gson.toJson(objectList), new TypeToken<List<witnesses_object>>() {
        }.getType());
        account_object support_object = get_account_object(support_account);
        List<witnesses_object_result> witnesses_object_results = new ArrayList<>();
        for (witnesses_object witnesses_object : witnesses_objects) {
            if (null == witnesses_object) {
                return witnesses_object_results;
            }
            witnesses_object_result witnesses_object_result = new witnesses_object_result();
            account_object account_object = get_accounts(witnesses_object.witness_account.toString());
            witnesses_object_result.account_id = account_object.id.toString();
            witnesses_object_result.account_name = account_object.name;
            witnesses_object_result.witness_id = witnesses_object.id.toString();
            witnesses_object_result.type = "witnesses";
            witnesses_object_result.url = witnesses_object.url;
            witnesses_object_result.vote_id = witnesses_object.vote_id;
            witnesses_object_result.votes = String.valueOf(witnesses_object.total_votes / Math.pow(10, 5));
            witnesses_object_result.total_missed = witnesses_object.total_missed;
            witnesses_object_result.last_confirmed_block_num = witnesses_object.last_confirmed_block_num;
            witnesses_object_result.last_aslot = witnesses_object.last_aslot;
            witnesses_object_result.active = false;
            witnesses_object_result.supported = false;
            for (object_id<witnesses_object> witnessesObject : vote_object.active_witnesses) {
                if (TextUtils.equals(witnessesObject.toString(), witnesses_object.id.toString())) {
                    witnesses_object_result.active = true;
                }
            }
            List<witnesses_object_result.SupportersBean> supportersBeans = new ArrayList<>();
            for (Map.Entry<object_id<account_object>, asset_fee_object> objectHashMap : witnesses_object.supporters.entrySet()) {
                witnesses_object_result.SupportersBean supportersBean = new witnesses_object_result.SupportersBean();
                supportersBean.account_id = objectHashMap.getKey();
                asset_fee_object asset_fee_object = objectHashMap.getValue();
                supportersBean.amount_raw = asset_fee_object;
                asset_object asset_object = lookup_asset_symbols(asset_fee_object.asset_id.toString());
                supportersBean.amount_text = new BigDecimal(asset_fee_object.amount).divide(new BigDecimal(Math.pow(10, asset_object.precision))) + asset_object.symbol;
                object_id<account_object> accountObjectId = objectHashMap.getKey();
                if (TextUtils.equals(support_object.id.toString(), accountObjectId.toString())) {
                    witnesses_object_result.supported = true;
                }
                supportersBeans.add(supportersBean);
            }
            witnesses_object_result.supporters = supportersBeans;
            witnesses_object_results.add(witnesses_object_result);
        }
        return witnesses_object_results;
    }


    /**
     * vote_members
     *
     * @param vote_account "
     * @param password
     * @param accountDao
     * @param vote_count
     * @return hash
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    public String vote_members(String vote_account, String password, String type, List<String> vote_ids, String vote_count, AccountDao accountDao)
            throws NetworkStatusException, AccountNotFoundException,
            PasswordVerifyException, KeyInvalideException, AuthorityException, UnLegalInputException, NotMemberException {

        int types = -1;
        List<String> vote_ids_list = new ArrayList<>();
        if (TextUtils.equals(type, "witnesses")) {
            types = 1;
            HashSet<String> witnessesIds = new HashSet<>(vote_ids);
            for (String witnessesId : witnessesIds) {
                account_object account_object = get_accounts(witnessesId);
                if (account_object == null) {
                    throw new AccountNotFoundException(witnessesId + " does not exist");
                }
                if (null != account_object.witness_status) {
                    String witnessId = (String) account_object.witness_status.get(0);
                    Object witnesses_base_object = get_objects(witnessId);
                    Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                    witnesses_object witnesses_object = gson.fromJson(gson.toJson(witnesses_base_object), witnesses_object.class);
                    vote_ids_list.add(witnesses_object.vote_id);
                } else {
                    throw new NotMemberException(witnessesId + " is not a witness");
                }
            }
        } else if (TextUtils.equals(type, "committee")) {
            types = 0;
            HashSet<String> committeeIds = new HashSet<>(vote_ids);
            for (String committee_id : committeeIds) {
                account_object account_object = get_accounts(committee_id);
                if (account_object == null) {
                    throw new AccountNotFoundException(committee_id + " does not exist");
                }
                if (null != account_object.committee_status) {
                    String comm_id = (String) account_object.committee_status.get(0);
                    Object committee_base_object = get_objects(comm_id);
                    Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                    committee_object committee_object = gson.fromJson(gson.toJson(committee_base_object), committee_object.class);
                    vote_ids_list.add(committee_object.vote_id);
                } else {
                    throw new NotMemberException(committee_id + " is not a committee");
                }
            }
        }

        String[] strings = new String[vote_ids_list.size()];
        for (int i = 0; i < vote_ids_list.size(); i++) {
            String s = vote_ids_list.get(i);
            strings[i] = s;
        }

        int length = strings.length;
        boolean flag;
        for (int i = 0; i < length - 1; i++) {
            flag = false;
            for (int j = 0; j < length - i - 1; j++) {
                int nIndex = strings[j].indexOf(':');
                int nIndexs = strings[j + 1].indexOf(':');
                if (Integer.valueOf(strings[j].substring(nIndex + 1)) > Integer.valueOf(strings[j + 1].substring(nIndexs + 1))) {
                    String temp = strings[j];
                    strings[j] = strings[j + 1];
                    strings[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
        return vote_members_local(vote_account, password, types, Arrays.asList(strings), vote_count, accountDao);
    }


    /**
     * vote_members
     *
     * @param vote_account "
     * @param password
     * @param accountDao
     * @param vote_count
     * @return hash
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    private String vote_members_local(String vote_account, String password, int type, List<String> vote_ids, String vote_count, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {
        account_object vote_account_object = get_account_object(vote_account);
        if (vote_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (unlock(vote_account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(vote_account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        operations.operation_id_map.add_operate(1);
        asset_object assetObject = lookup_asset_symbols(CocosBcxApiWrapper.coreAsset);
        operations.vote_members_operation vote_members_operation = new operations.vote_members_operation();
        List<Object> lock_with_vote = new ArrayList<>();
        lock_with_vote.add(type);
        lock_with_vote.add(assetObject.amount_from_string(vote_count));
        vote_members_operation.lock_with_vote = lock_with_vote;
        vote_members_operation.account = vote_account_object.id;
        types.account_options new_options = new types.account_options();
        new_options.memo_key = vote_account_object.options.memo_key;
        new_options.votes = vote_ids;
        new_options.extensions = new HashSet<>();
        vote_members_operation.new_options = new_options;
        vote_members_operation.extensions = new HashMap();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = vote_members_operation;
        operationType.nOperationType = operations.ID_VOTE_MEMBER;

        signed_operate signed_operate = new signed_operate();
        signed_operate.operations = new ArrayList<>();
        signed_operate.operations.add(operationType);
        signed_operate.extensions = new HashSet<>();
        return sign_transaction_owner(signed_operate, vote_account_object);
    }


    /**
     * create_committee_member
     *
     * @param owner_account "
     * @param password
     * @param url
     * @param accountDao
     * @return hash
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    public String create_committee_member(String owner_account, String password, String url, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {

        account_object owner_account_object = get_account_object(owner_account);
        if (owner_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        if (unlock(owner_account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(owner_account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        operations.create_committee_member_operation create_committee_member_operation = new operations.create_committee_member_operation();
        create_committee_member_operation.committee_member_account = owner_account_object.id;
        create_committee_member_operation.url = url;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_committee_member_operation;
        operationType.nOperationType = operations.ID_CREATE_COMMITTEE_MEMBER;

        signed_operate signed_operate = new signed_operate();
        signed_operate.operations = new ArrayList<>();
        signed_operate.operations.add(operationType);
        signed_operate.extensions = new HashSet<>();
        return sign_transaction(signed_operate, owner_account_object);
    }


    /**
     * create_witness
     *
     * @param owner_account "
     * @param password
     * @param url
     * @param accountDao
     * @return hash
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    public String create_witness(String owner_account, String password, String url, AccountDao accountDao) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException {

        account_object owner_account_object = get_account_object(owner_account);
        if (owner_account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }
        if (unlock(owner_account_object.name, password, accountDao) != OPERATE_SUCCESS && verify_password(owner_account_object.name, password).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }

        operations.create_witness_operation create_witness_operation = new operations.create_witness_operation();
        create_witness_operation.witness_account = owner_account_object.id;
        create_witness_operation.url = url;
        create_witness_operation.block_signing_key = owner_account_object.options.memo_key;

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = create_witness_operation;
        operationType.nOperationType = operations.ID_CREATE_WITNESS;

        signed_operate signed_operate = new signed_operate();
        signed_operate.operations = new ArrayList<>();
        signed_operate.operations.add(operationType);
        signed_operate.extensions = new HashSet<>();
        return sign_transaction(signed_operate, owner_account_object);
    }


    /**
     * create_witness
     *
     * @return hash
     * @throws NetworkStatusException
     * @throws AccountNotFoundException
     * @throws PasswordVerifyException
     * @throws KeyInvalideException
     */
    public String modify_password(String accountName, String originPwd, String newPwd) throws NetworkStatusException, AccountNotFoundException, PasswordVerifyException, KeyInvalideException, AuthorityException, UnLegalInputException, PasswordInvalidException {

        if (!PassWordCheckUtil.passwordVerify(newPwd)) {
          throw new PasswordInvalidException("Password does not meet the rules：^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.-]).{12,}$");
        }

        private_key privateActiveKey = private_key.from_seed(accountName + "active" + newPwd);
        private_key privateOwnerKey = private_key.from_seed(accountName + "owner" + newPwd);
        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        account_object account_object = get_account_object(accountName);
        if (account_object == null) {
            throw new AccountNotFoundException("Account does not exist");
        }

        if (verify_password(account_object.name, originPwd).size() <= 0) {
            throw new PasswordVerifyException("Wrong password");
        }
        operations.operation_id_map.add_operate(0);
        authority1 owner = new authority1(1, publicOwnerKeyType, 1);
        authority1 active = new authority1(1, publicActiveKeyType, 1);
        types.account_options options = new types.account_options();
        options.memo_key = publicActiveKeyType;
        options.votes = account_object.options.votes;
        options.extensions = new HashSet<>();
        operations.modify_password_operation modify_password_operation = new operations.modify_password_operation();
        modify_password_operation.account = account_object.id;
        modify_password_operation.owner = owner;
        modify_password_operation.active = active;
        modify_password_operation.new_options = options;
        modify_password_operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.operationContent = modify_password_operation;
        operationType.nOperationType = operations.ID_VOTE_MEMBER;

        signed_operate signed_operate = new signed_operate();
        signed_operate.operations = new ArrayList<>();
        signed_operate.operations.add(operationType);
        signed_operate.extensions = new HashSet<>();
        return sign_transaction_owner(signed_operate, account_object);
    }


    /**
     * get_dao_account_by_name
     *
     * @param accountName
     * @param accountDao
     * @return get_dao_account_by_name
     */
    private AccountEntity.AccountBean get_dao_account_by_name(String accountName, AccountDao accountDao) {
        return accountDao.queryChainAccountByName(accountName);
    }


    /**
     * log out
     *
     * @return
     */
    public int log_out() {
        lock();
        return OPERATE_SUCCESS;
    }

}


