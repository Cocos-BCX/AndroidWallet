package com.cocos.bcx_sdk.bcx_entity;

import com.cocos.bcx_sdk.bcx_wallet.chain.types;


/**
 * 创建账号请求参数实体
 */
public class CreateAccountRequestParamsEntity {

    public CreateAccountParams account;

    public static class CreateAccountParams {
        public String name;
        public types.public_key_type owner_key;
        public types.public_key_type active_key;
        public types.public_key_type memo_key;
        public String refcode;
        public String referrer;
    }
}
