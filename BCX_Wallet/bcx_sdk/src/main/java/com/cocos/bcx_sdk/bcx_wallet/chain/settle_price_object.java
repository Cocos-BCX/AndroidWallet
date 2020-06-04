package com.cocos.bcx_sdk.bcx_wallet.chain;

import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;

/**
 * @author ningkang.guo
 * @Date 2019/8/30
 */
public class settle_price_object {
    public asset base;
    public asset quote;

    public void write_to_encoder(base_encoder baseEncoder) {
        base.write_to_encoder(baseEncoder);
        quote.write_to_encoder(baseEncoder);
    }
}
