package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.HashMap;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/11
 */
public class block_info {
    public String previous;
    public String timestamp;
    public String witness;
    public String transaction_merkle_root;
    public String witness_signature;
    public String block_id;
    public List<Object> extensions;
    public HashMap<String, transactions_object> transactions = new HashMap<>();
}
