package com.cocos.bcx_sdk.bcx_server;

import android.text.TextUtils;

import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_server.callback.IReplyObjectProcess;
import com.cocos.bcx_sdk.bcx_server.entity.Call;
import com.cocos.bcx_sdk.bcx_server.entity.Reply;
import com.cocos.bcx_sdk.bcx_server.entity.ReplyBase;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.account_related_word_view_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_fee_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.block_header;
import com.cocos.bcx_sdk.bcx_wallet.chain.block_info;
import com.cocos.bcx_sdk.bcx_wallet.chain.contract_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.dynamic_global_property_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.fill_order_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.global_config_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.global_property_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.limit_orders_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.market_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.nh_asset_order_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.nhasset_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.object_id;
import com.cocos.bcx_sdk.bcx_wallet.chain.operation_history_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.transaction;
import com.cocos.bcx_sdk.bcx_wallet.chain.transaction_in_block_info;
import com.cocos.bcx_sdk.bcx_wallet.chain.vesting_balances_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.world_view_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.sha256_object;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.cocos.bcx_sdk.bcx_error.ErrorCode.OPERATE_FAILED;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.OPERATE_SUCCESS;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.RPC_NETWORK_EXCEPTION;
import static com.cocos.bcx_sdk.bcx_error.ErrorCode.WEBSOCKET_CONNECT_INVALID;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_BROADCAST_TRANSACTION;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_BROADCAST_TRANSACTION_WITH_CALLBACK;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_DATABASE;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_ESTIMATION_GAS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_ACCOUNTS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_ACCOUNT_BALANCES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_ACCOUNT_HISTORY;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_ASSETS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_BLOCK;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_BLOCK_HEADER;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_CHAIN_ID;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_CONTRACT;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_DYNAMIC_GLOBAL_PROPERTIES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_FILL_ORDER_HISTORY;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_FULL_ACCOUNTS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_GLOBAL_PROPERTIES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_KEY_REFERENCES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_LIMIT_ORDERS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_MARKET_HISTORY;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_NH_CREATOR;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_OBJECTS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_TRANSACTION_BY_ID;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_TRANSACTION_IN_BLOCK_INFO;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_GET_VESTING_BALANCES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_HISTORY;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LIST_ACCOUNT_NH_ASSET;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LIST_ACCOUNT_NH_ASSET_ORDER;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LIST_ASSETS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LIST_NH_ASSET_BY_CREATOR;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LIST_NH_ASSET_ORDER;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LOGIN;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LOOKUP_ACCOUNT_NAMES;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LOOKUP_ASSET_SYMBOLS;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LOOKUP_NH_ASSET;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_LOOKUP_WORLD_VIEW;
import static com.cocos.bcx_sdk.bcx_rpc.RPC.CALL_NETWORK_BROADCAST;

/**
 * socket server
 * rpc api
 *
 * @author ningkang.guo
 */
public class ConnectServer extends WebSocketListener {

    private AtomicInteger mnCallId = new AtomicInteger(1);
    private int mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
    private OkHttpClient mOkHttpClient;
    private WebSocket mWebSocket;
    private int mDatabaseId;
    private int mHistoryId;
    private int mBroadcastId;
    private Map<Integer, IReplyObjectProcess> mHashMapIdToProcess = new ConcurrentHashMap<>();


    private ConnectServer() {
    }

    private static class WebServerInstanceHolder {
        static final ConnectServer INSTANCE = new ConnectServer();
    }


    public static ConnectServer getBcxWebServerInstance() {
        return ConnectServer.WebServerInstanceHolder.INSTANCE;
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        synchronized (mWebSocket) {
            mnConnectStatus = OPERATE_SUCCESS;
            mWebSocket.notify();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        LogUtils.i("WebSocket_CallBack", text);
        try {
            Gson gson = new Gson();
            ReplyBase replyObjectBase = gson.fromJson(text, ReplyBase.class);

            IReplyObjectProcess iReplyObjectProcess = null;
            synchronized (mHashMapIdToProcess) {
                if (mHashMapIdToProcess.containsKey(replyObjectBase.id)) {
                    iReplyObjectProcess = mHashMapIdToProcess.get(replyObjectBase.id);
                }
            }
            if (iReplyObjectProcess != null) {
                iReplyObjectProcess.processTextToObject(text);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        if (null != mWebSocket) {
            if (t instanceof IOException) {
                synchronized (mWebSocket) {
                    mnConnectStatus = OPERATE_FAILED;
                    mWebSocket.notify();
                }
                synchronized (mHashMapIdToProcess) {
                    for (Map.Entry<Integer, IReplyObjectProcess> entry : mHashMapIdToProcess.entrySet()) {
                        entry.getValue().notifyFailure(t);
                    }
                    mHashMapIdToProcess.clear();
                }
            }
        }
    }


    /**
     * conncet
     *
     * @return
     */
    public synchronized int connect() {
        String strServer = FullNodeServer.getConnectServers();
        LogUtils.i("ConnectedServerUrl", strServer);
        if (TextUtils.isEmpty(strServer)) {
            return OPERATE_FAILED;
        }
        LogUtils.d("ConnectedServerUrl", strServer);
        Request request = new Request.Builder().url(strServer).build();
        mOkHttpClient = new OkHttpClient();
        mWebSocket = mOkHttpClient.newWebSocket(request, this);
        synchronized (mWebSocket) {
            if (mnConnectStatus == WEBSOCKET_CONNECT_INVALID) {
                try {
                    mWebSocket.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mnConnectStatus != OPERATE_SUCCESS) {
                    return OPERATE_FAILED;
                }
            }
        }

        int nRet = OPERATE_SUCCESS;

        try {
            boolean bLogin = login();
            if (bLogin) {
                mDatabaseId = getApiId(CALL_DATABASE);
                mHistoryId = getApiId(CALL_HISTORY);
                mBroadcastId = getApiId(CALL_NETWORK_BROADCAST);
            } else {
                nRet = OPERATE_FAILED;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            nRet = RPC_NETWORK_EXCEPTION;
        }

        if (nRet != OPERATE_SUCCESS && null != mWebSocket) {
            mWebSocket.close(1000, "close");
            mWebSocket = null;
            mnConnectStatus = OPERATE_FAILED;
        } else {
            mnConnectStatus = OPERATE_SUCCESS;
        }
        return nRet;
    }


    /**
     * query database/history/network_broadcast id
     *
     * @param strApiName
     * @return
     * @throws NetworkStatusException
     */
    private int getApiId(String strApiName) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add(strApiName);

        List<Object> listDatabaseParams = new ArrayList<>();
        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<Integer>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>() {
        }.getType());
        Reply<Integer> replyApiId = sendForReply(callObject, replyObject);
        return replyApiId.result;
    }

    /**
     * send message for reply
     *
     * @param callObject
     * @param replyObjectProcess
     * @param <T>
     * @return
     * @throws NetworkStatusException
     */
    private <T> Reply<T> sendForReply(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        if (mWebSocket == null || mnConnectStatus != OPERATE_SUCCESS) {
            int nRet = connect();
            if (nRet == WEBSOCKET_CONNECT_INVALID || nRet == OPERATE_FAILED) {
                throw new NetworkStatusException("It doesnt connect to the server.");
            }
        }
        return sendForReplyImpl(callObject, replyObjectProcess);
    }

    private <T> Reply<T> sendForReplyImpl(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        String strMessage = gson.toJson(callObject);
        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }
        synchronized (replyObjectProcess) {
            LogUtils.i("WebSocket_SendMessage", strMessage);
            // send message
            boolean bRet = mWebSocket != null && mWebSocket.send(strMessage);
            if (!bRet) {
                throw new NetworkStatusException("Failed to send message to server.");
            }
            try {
                replyObjectProcess.wait();
                Reply<T> replyObject = replyObjectProcess.getReplyObject();
                String strError = replyObjectProcess.getError();
                if (!TextUtils.isEmpty(strError)) {
                    throw new NetworkStatusException(strError);
                } else if (replyObjectProcess.getException() != null) {
                    throw new NetworkStatusException(replyObjectProcess.getException());
                } else if (replyObject == null) {
                    throw new NetworkStatusException("Reply object is null.\n" + replyObjectProcess.getResponse());
                } else if (replyObject.error != null) {
                    throw new NetworkStatusException(gson.toJson(replyObject.error));
                }
                return replyObject;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private boolean login() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add(CALL_LOGIN);

        List<Object> listLoginParams = new ArrayList<>();
        listLoginParams.add("");
        listLoginParams.add("");
        callObject.params.add(listLoginParams);

        ReplyObjectProcess<Reply<Boolean>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<Boolean>>() {
        }.getType());
        Reply<Boolean> replyLogin = sendForReply(callObject, replyObject);
        return replyLogin.result;
    }

    /**
     * close socket connect
     *
     * @return
     */
    public synchronized int close() {
        synchronized (mHashMapIdToProcess) {
            for (Map.Entry<Integer, IReplyObjectProcess> entry : mHashMapIdToProcess.entrySet()) {
                synchronized (entry.getValue()) {
                    entry.getValue().notify();
                }
            }
        }
        mWebSocket.close(1000, "close");
        mOkHttpClient = null;
        mWebSocket = null;
        mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
        mDatabaseId = -1;
        mBroadcastId = -1;
        mHistoryId = -1;
        return OPERATE_SUCCESS;
    }

    /**
     * get chain od
     *
     * @return
     * @throws NetworkStatusException
     */
    public sha256_object get_chain_id() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_CHAIN_ID);

        List<Object> listDatabaseParams = new ArrayList<>();
        callObject.params.add(listDatabaseParams);
        ReplyObjectProcess<Reply<sha256_object>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<sha256_object>>() {
        }.getType());
        Reply<sha256_object> replyDatabase = sendForReply(callObject, replyObject);
        return replyDatabase.result;
    }

    /**
     * lookup_account_names
     *
     * @param strAccountName
     * @return
     * @throws NetworkStatusException
     */
    public List<account_object> lookup_account_names(String strAccountName) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LOOKUP_ACCOUNT_NAMES);

        List<Object> listAccountNames = new ArrayList<>();
        listAccountNames.add(strAccountName);
        List<Object> listAccountNamesParams = new ArrayList<>();
        listAccountNamesParams.add(listAccountNames);
        callObject.params.add(listAccountNamesParams);

        ReplyObjectProcess<Reply<List<account_object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<account_object>>>() {
        }.getType());
        Reply<List<account_object>> replyAccountObjectList = sendForReply(callObject, replyObject);
        return replyAccountObjectList.result;
    }


    /**
     * get account by account id
     *
     * @param account_ids ：account id
     * @return account object
     * @throws NetworkStatusException
     */
    public List<account_object> get_accounts(List<object_id<account_object>> account_ids) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_ACCOUNTS);

        List<Object> listAccountIds = new ArrayList<>();
        listAccountIds.add(account_ids);
        List<Object> listAccountNamesParams = new ArrayList<>();
        listAccountNamesParams.add(listAccountIds);
        callObject.params.add(listAccountIds);
        ReplyObjectProcess<Reply<List<account_object>>> replyObject =
                new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<account_object>>>() {
                }.getType());
        Reply<List<account_object>> replyAccountObjectList = sendForReply(callObject, replyObject);

        return replyAccountObjectList.result;
    }

    /**
     * get_objects
     *
     * @throws NetworkStatusException
     */
    public List<Object> get_objects(List<String> ids) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_OBJECTS);

        List<Object> listParams = new ArrayList<>();
        listParams.add(ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> reply = sendForReply(callObject, replyObject);

        return reply.result;
    }


    /**
     * query_contract
     *
     * @throws NetworkStatusException
     */
    public Object get_objects(String ids) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_OBJECTS);

        List<Object> list_ids = new ArrayList<>();
        List<Object> listParams = new ArrayList<>();
        list_ids.add(ids);
        listParams.add(list_ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> reply = sendForReply(callObject, replyObject);
        return reply.result.size() > 0 ? reply.result.get(0) : null;
    }


    /**
     * get_key_references
     *
     * @throws NetworkStatusException
     */
    public List<List<String>> get_key_references(List<String> publicKeys) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_KEY_REFERENCES);

        List<Object> listParams = new ArrayList<>();
        listParams.add(publicKeys);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<List<String>>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<List<String>>>>() {
        }.getType());
        Reply<List<List<String>>> reply = sendForReply(callObject, replyObject);

        return reply.result;
    }

    /**
     * lookup_asset_symbols
     *
     * @throws NetworkStatusException
     */
    public asset_object lookup_asset_symbols(String assetsSymbolOrId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LOOKUP_ASSET_SYMBOLS);
        List<Object> listAssetsParam = new ArrayList<>();
        List<Object> listAssetSysmbols = new ArrayList<>();
        listAssetSysmbols.add(assetsSymbolOrId);
        listAssetsParam.add(listAssetSysmbols);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<asset_object>>>() {
                }.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result.get(0);
    }

    /**
     * get_assets
     *
     * @throws NetworkStatusException
     */
    public List<asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_ASSETS);
        List<Object> listAssetsParam = new ArrayList<>();
        List<Object> listObjectId = new ArrayList<>();
        listObjectId.addAll(listAssetObjectId);
        listAssetsParam.add(listObjectId);

        callObject.params.add(listAssetsParam);
        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<asset_object>>>() {
                }.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }

    /**
     * list_assets
     *
     * @throws NetworkStatusException
     */
    public List<asset_object> list_assets(String strLowerBound, int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LIST_ASSETS);

        List<Object> listAssetsParam = new ArrayList<>();
        listAssetsParam.add(strLowerBound);
        listAssetsParam.add(nLimit);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<asset_object>>>() {
                }.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }

    public dynamic_global_property_object get_dynamic_global_properties() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_DYNAMIC_GLOBAL_PROPERTIES);
        callObject.params.add(new ArrayList<Object>());

        ReplyObjectProcess<Reply<dynamic_global_property_object>> replyObjectProcess =
                new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<dynamic_global_property_object>>() {
                }.getType());
        Reply<dynamic_global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }

    public global_property_object get_global_properties() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_GLOBAL_PROPERTIES);
        callObject.params.add(new ArrayList<>());

        ReplyObjectProcess<Reply<global_property_object>> replyObjectProcess = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<global_property_object>>() {
        }.getType());
        Reply<global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);
        return replyObject.result;
    }

    /**
     * broadcast_transaction
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    public String broadcast_transaction(transaction tx) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mBroadcastId);
        callObject.params.add(CALL_BROADCAST_TRANSACTION);
        List<Object> listTransaction = new ArrayList<>();
        listTransaction.add(tx);
        callObject.params.add(listTransaction);

        ReplyObjectProcess<Reply<String>> replyObjectProcess = new ReplyObjectProcess<>(new TypeToken<Reply<String>>() {
        }.getType());
        Reply<String> replyObject = sendForReply(callObject, replyObjectProcess);
        return replyObject.result;
    }

    /**
     * broadcast_transactionwith_callback
     *
     * @param tx
     * @return
     * @throws NetworkStatusException
     */
    public Object broadcast_transaction_with_callback(transaction tx) throws NetworkStatusException {
        Call callObject = new Call();
        int id = mnCallId.getAndIncrement();
        callObject.id = id;
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mBroadcastId);
        callObject.params.add(CALL_BROADCAST_TRANSACTION_WITH_CALLBACK);
        List<Object> listTransaction = new ArrayList<>();
        listTransaction.add(id);
        listTransaction.add(tx);
        callObject.params.add(listTransaction);

        ReplyObjectProcess<Reply<Object>> replyObjectProcess = new ReplyObjectProcess<>(new TypeToken<Reply<Object>>() {
        }.getType());
        Reply<Object> replyObject = sendForReply(callObject, replyObjectProcess);
        return replyObject.result;
    }


    /**
     * get_account_history
     *
     * @param accountId
     * @param startId
     * @param nLimit
     * @return
     * @throws NetworkStatusException
     */
    public List<operation_history_object> get_account_history(object_id<account_object> accountId, object_id<operation_history_object> startId, int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mHistoryId);
        callObject.params.add(CALL_GET_ACCOUNT_HISTORY);

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add(startId);
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add("1.11.0");
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>() {
        }.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        return replyAccountHistory.result;
    }


    public List<operation_history_object> get_account_history(object_id<account_object> id, String startId, String endId, int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mHistoryId);
        callObject.params.add(CALL_GET_ACCOUNT_HISTORY);

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(id);
        listAccountHistoryParam.add(startId);
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add(endId);
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>() {
        }.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        return replyAccountHistory.result;
    }

    /**
     * get_full_accounts
     *
     * @throws NetworkStatusException
     */
    public Object get_full_accounts(String names_or_id, boolean subscribe) throws NetworkStatusException, IndexOutOfBoundsException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_FULL_ACCOUNTS);

        List<Object> listParams = new ArrayList<>();
        List<String> listNameOrIds = new ArrayList<>();
        listNameOrIds.add(names_or_id);
        listParams.add(listNameOrIds);
        listParams.add(subscribe);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<List<Object>>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<List<Object>>>>() {
        }.getType());
        Reply<List<List<Object>>> reply = sendForReply(callObject, replyObject);

        return reply.result.size() > 0 ? global_config_object.getInstance().getGsonBuilder().create().toJson(reply.result.get(0).get(1)) : reply.result;
    }

    /**
     * get block header。
     *
     * @throws NetworkStatusException
     */
    public block_header get_block_header(String nBlockNumber) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_BLOCK_HEADER);
        List<Object> listBlockNumber = new ArrayList<>();
        listBlockNumber.add(nBlockNumber);
        callObject.params.add(listBlockNumber);

        ReplyObjectProcess<Reply<block_header>> replyObjectProcess = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<block_header>>() {
        }.getType());
        Reply<block_header> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }


    /**
     * get block header。
     *
     * @throws NetworkStatusException
     */
    public block_info get_block(String nBlockNumber) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_BLOCK);
        List<Object> listBlockNumber = new ArrayList<>();
        listBlockNumber.add(nBlockNumber);
        callObject.params.add(listBlockNumber);

        ReplyObjectProcess<Reply<block_info>> replyObjectProcess = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<block_info>>() {
        }.getType());
        Reply<block_info> replyObject = sendForReply(callObject, replyObjectProcess);
        return replyObject.result;
    }

    /**
     * get_contract
     *
     * @param contractNameOrId
     * @return
     * @throws NetworkStatusException
     */
    public contract_object get_contract(String contractNameOrId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_CONTRACT);

        List<Object> contractNameOrIds = new ArrayList<>();
        contractNameOrIds.add(contractNameOrId);
        callObject.params.add(contractNameOrIds);

        ReplyObjectProcess<Reply<contract_object>> replyObjectProcess = new ReplyObjectProcess<>(new TypeToken<Reply<contract_object>>() {
        }.getType());
        Reply<contract_object> replyObject = sendForReply(callObject, replyObjectProcess);
        return replyObject.result;
    }


    /**
     * get_account_balances
     *
     * @throws NetworkStatusException
     */
    public List<asset_fee_object> get_account_balances(String account_id, List<Object> assetsIds) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_ACCOUNT_BALANCES);

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(account_id);
        listAccountBalancesParam.add(assetsIds);
        callObject.params.add(listAccountBalancesParam);

        ReplyObjectProcess<Reply<List<asset_fee_object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<asset_fee_object>>>() {
        }.getType());
        Reply<List<asset_fee_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }


    /**
     * get all assets balances of account
     *
     * @param account_id
     * @return account balance
     * @throws NetworkStatusException
     */
    public List<asset_fee_object> get_all_account_balances(String account_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_ACCOUNT_BALANCES);

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(account_id);
        listAccountBalancesParam.add(new ArrayList<Object>());
        callObject.params.add(listAccountBalancesParam);

        ReplyObjectProcess<Reply<List<asset_fee_object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<asset_fee_object>>>() {
        }.getType());
        Reply<List<asset_fee_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }

    /**
     * lookup_nh_asset get NH asset detail by NH asset id
     *
     * @throws NetworkStatusException
     */
    public List<Object> lookup_nh_asset(List<String> nh_asset_ids_or_hash) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LOOKUP_NH_ASSET);

        List<Object> nhAssetIds = new ArrayList<>();
        nhAssetIds.add(nh_asset_ids_or_hash);
        callObject.params.add(nhAssetIds);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }

    /**
     * lookup_nh_asset get NH asset detail by NH asset id
     *
     * @throws NetworkStatusException
     */
    public List<nhasset_object> lookup_nh_asset_object(String nh_asset_ids_or_hash) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LOOKUP_NH_ASSET);

        List<String> nh_asset_ids_or_hashs = new ArrayList<>();
        nh_asset_ids_or_hashs.add(nh_asset_ids_or_hash);
        List<Object> nhAssetIds = new ArrayList<>();
        nhAssetIds.add(nh_asset_ids_or_hashs);
        callObject.params.add(nhAssetIds);

        ReplyObjectProcess<Reply<List<nhasset_object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<nhasset_object>>>() {
        }.getType());
        Reply<List<nhasset_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }

    /**
     * lookup_nh_asset get account NH asset by account id or name
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_account_nh_asset(String account_id, List<String> world_view_name_or_ids, int page, int pageSize) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LIST_ACCOUNT_NH_ASSET);

        List<Object> nhparams = new ArrayList<>();
        nhparams.add(account_id);
        nhparams.add(world_view_name_or_ids);
        nhparams.add(pageSize);
        nhparams.add(page);
        nhparams.add(4);
        callObject.params.add(nhparams);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return (List<Object>) replyLookupAccountNames.result.get(0);
    }


    /**
     * list account nh asset order
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_account_nh_asset_order(String account_id, int pageSize, int page) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LIST_ACCOUNT_NH_ASSET_ORDER);

        List<Object> nh_order_params = new ArrayList<>();
        nh_order_params.add(account_id);
        nh_order_params.add(pageSize);
        nh_order_params.add(page);
        callObject.params.add(nh_order_params);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return (List<Object>) replyLookupAccountNames.result.get(0);
    }


    /**
     * list nh asset order
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_nh_asset_order(String asset_id_or_symbol, String world_view_name_or_ids, String baseDescribe, int pageSize, int page) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LIST_NH_ASSET_ORDER);

        List<Object> nh_order_params = new ArrayList<>();
        nh_order_params.add(asset_id_or_symbol);
        nh_order_params.add(world_view_name_or_ids);
        nh_order_params.add(baseDescribe);
        nh_order_params.add(pageSize);
        nh_order_params.add(page);
        nh_order_params.add(true);
        callObject.params.add(nh_order_params);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return (List<Object>) replyLookupAccountNames.result.get(0);
    }

    /**
     * get nh asset order object
     *
     * @throws NetworkStatusException
     */
    public nh_asset_order_object get_nhasset_order_object(String nh_order_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_OBJECTS);

        List<String> nh_order_ids = new ArrayList<>();
        nh_order_ids.add(nh_order_id);
        List<Object> listParams = new ArrayList<>();
        listParams.add(nh_order_ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<nh_asset_order_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<nh_asset_order_object>>>() {
        }.getType());
        Reply<List<nh_asset_order_object>> reply = sendForReply(callObject, replyObject);

        return reply.result.size() > 0 ? reply.result.get(0) : null;
    }


    /**
     * get limit asset order object
     *
     * @throws NetworkStatusException
     */
    public limit_orders_object get_limit_order_object(String limit_order_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_OBJECTS);

        List<String> nh_order_ids = new ArrayList<>();
        nh_order_ids.add(limit_order_id);
        List<Object> listParams = new ArrayList<>();
        listParams.add(nh_order_ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_orders_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_orders_object>>>() {
        }.getType());
        Reply<List<limit_orders_object>> reply = sendForReply(callObject, replyObject);

        return reply.result.size() > 0 ? reply.result.get(0) : null;
    }


    /**
     * Seek World View Details
     *
     * @throws NetworkStatusException
     */
    public List<world_view_object> lookup_world_view(List<String> world_view_names) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LOOKUP_WORLD_VIEW);

        List<Object> nh_order_params = new ArrayList<>();
        nh_order_params.add(world_view_names);
        callObject.params.add(nh_order_params);

        ReplyObjectProcess<Reply<List<world_view_object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<world_view_object>>>() {
        }.getType());
        Reply<List<world_view_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }

    /**
     * get Developer-Related World View
     *
     * @throws NetworkStatusException
     */
    public account_related_word_view_object get_nh_creator(String account_name) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_NH_CREATOR);

        List<Object> nh_order_params = new ArrayList<>();
        nh_order_params.add(account_name);
        callObject.params.add(nh_order_params);

        ReplyObjectProcess<Reply<account_related_word_view_object>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<account_related_word_view_object>>() {
        }.getType());
        Reply<account_related_word_view_object> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }


    /**
     * Query NH assets created by developers
     *
     * @throws NetworkStatusException
     */
    public List<Object> list_nh_asset_by_creator(String account_id, String worldview, int page, int pageSize) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_LIST_NH_ASSET_BY_CREATOR);

        List<Object> nh_order_params = new ArrayList<>();
        nh_order_params.add(account_id);
        nh_order_params.add(worldview);
        nh_order_params.add(page);
        nh_order_params.add(pageSize);
        callObject.params.add(nh_order_params);

        ReplyObjectProcess<Reply<List<Object>>> replyObject = new ReplyObjectProcess<>(new com.google.gson.reflect.TypeToken<Reply<List<Object>>>() {
        }.getType());
        Reply<List<Object>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }

    /**
     * get transaction in block info
     *
     * @throws NetworkStatusException
     */
    public transaction_in_block_info get_transaction_in_block_info(String tr_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_TRANSACTION_IN_BLOCK_INFO);

        List<Object> tr_ids = new ArrayList<>();
        tr_ids.add(tr_id);
        callObject.params.add(tr_ids);

        ReplyObjectProcess<Reply<transaction_in_block_info>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<transaction_in_block_info>>() {
        }.getType());
        Reply<transaction_in_block_info> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }

    /**
     * get_limit_orders
     *
     * @throws NetworkStatusException
     */
    public List<limit_orders_object> get_limit_orders(String first_id, String second_id, int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_LIMIT_ORDERS);

        List<Object> tr_ids = new ArrayList<>();
        tr_ids.add(first_id);
        tr_ids.add(second_id);
        tr_ids.add(nLimit);
        callObject.params.add(tr_ids);

        ReplyObjectProcess<Reply<List<limit_orders_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_orders_object>>>() {
        }.getType());
        Reply<List<limit_orders_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }


    /**
     * get_fill_order_history
     *
     * @throws NetworkStatusException
     */
    public List<fill_order_history_object> get_fill_order_history(String first_id, String second_id, int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mHistoryId);
        callObject.params.add(CALL_GET_FILL_ORDER_HISTORY);

        List<Object> tr_ids = new ArrayList<>();
        tr_ids.add(first_id);
        tr_ids.add(second_id);
        tr_ids.add(nLimit);
        callObject.params.add(tr_ids);

        ReplyObjectProcess<Reply<List<fill_order_history_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<fill_order_history_object>>>() {
        }.getType());
        Reply<List<fill_order_history_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }


    /**
     * get market history
     *
     * @throws NetworkStatusException
     */
    public List<market_history_object> get_market_history(String quote_asset_symbol_or_id, String base_asset_symbol_or_id, long seconds, String start_time, String end_time) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mHistoryId);
        callObject.params.add(CALL_GET_MARKET_HISTORY);

        List<Object> tr_ids = new ArrayList<>();
        tr_ids.add(quote_asset_symbol_or_id);
        tr_ids.add(base_asset_symbol_or_id);
        tr_ids.add(seconds);
        tr_ids.add(start_time);
        tr_ids.add(end_time);
        callObject.params.add(tr_ids);

        ReplyObjectProcess<Reply<List<market_history_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<market_history_object>>>() {
        }.getType());
        Reply<List<market_history_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }


    /**
     * estimation_gas
     *
     * @throws NetworkStatusException
     */
    public asset_fee_object estimation_gas(asset tr_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_ESTIMATION_GAS);

        List<asset> asset_fee_objects = new ArrayList<>();
        asset_fee_objects.add(tr_id);
        callObject.params.add(asset_fee_objects);

        ReplyObjectProcess<Reply<asset_fee_object>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<asset_fee_object>>() {
        }.getType());
        Reply<asset_fee_object> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }

    /**
     * get_vesting_balances
     *
     * @throws NetworkStatusException
     */
    public List<vesting_balances_object> get_vesting_balances(String account_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_VESTING_BALANCES);

        List<String> asset_fee_objects = new ArrayList<>();
        asset_fee_objects.add(account_id);
        callObject.params.add(asset_fee_objects);

        ReplyObjectProcess<Reply<List<vesting_balances_object>>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<List<vesting_balances_object>>>() {
        }.getType());
        Reply<List<vesting_balances_object>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }

    /**
     * get transaction by tr_id
     *
     * @throws NetworkStatusException
     */
    public Object get_transaction_by_id(Object tr_id) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(mDatabaseId);
        callObject.params.add(CALL_GET_TRANSACTION_BY_ID);

        List<Object> tr_ids = new ArrayList<>();
        tr_ids.add(tr_id);
        callObject.params.add(tr_ids);

        ReplyObjectProcess<Reply<Object>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<Object>>() {
        }.getType());
        Reply<Object> replyLookupAccountNames = sendForReply(callObject, replyObject);
        return replyLookupAccountNames.result;
    }


    /**
     * get_chain_data :
     * you can thought this method to call all rpc interface,
     * also you need connect server first
     */
    public Object get_chain_data(ChainApiID chainApiID, String method, String methodName, Object params) throws NetworkStatusException {
        int chainApiId = -11;
        switch (chainApiID) {
            case HISTORY:
                chainApiId = mHistoryId;
                break;
            case DATABASE:
                chainApiId = mDatabaseId;
                break;
            case BROADCAST:
                chainApiId = mBroadcastId;
                break;
        }
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = method;
        callObject.params = new ArrayList<>();
        callObject.params.add(chainApiId);
        callObject.params.add(methodName);
        callObject.params.add(params);

        ReplyObjectProcess<Reply<Object>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<Object>>() {
        }.getType());
        Reply<Object> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }


    /**
     * deal reply data from server
     *
     * @param <T>
     */
    private class ReplyObjectProcess<T> implements IReplyObjectProcess<T> {

        private String strError;
        private T mT;
        private Type mType;
        private Throwable exception;
        private String strResponse;

        public ReplyObjectProcess(Type type) {
            mType = type;
        }

        public void processTextToObject(String strText) {
            try {
                Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                mT = gson.fromJson(strText, mType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            } catch (Exception e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            }
            synchronized (this) {
                notify();
            }
        }

        @Override
        public T getReplyObject() {
            return mT;
        }

        @Override
        public String getError() {
            return strError;
        }

        @Override
        public void notifyFailure(Throwable t) {
            exception = t;
            synchronized (this) {
                notify();
            }
        }

        @Override
        public Throwable getException() {
            return exception;
        }

        @Override
        public String getResponse() {
            return strResponse;
        }
    }

}
