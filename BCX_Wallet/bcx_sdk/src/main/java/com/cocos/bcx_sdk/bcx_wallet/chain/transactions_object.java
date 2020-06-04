package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.HashMap;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/11
 */
public class transactions_object {

    public String ref_block_num;
    public String ref_block_prefix;
    public String expiration;
    public List<Object> extensions;
    public List<String> signatures;
    public HashMap<Integer, contract_operations> operations = new HashMap<>();
    public HashMap<Integer, operation_results_object> operation_results = new HashMap<>();
}
