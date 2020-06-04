package com.cocos.bcx_sdk.bcx_wallet.chain;


import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;

import java.util.Arrays;

public class asset {

    public long amount;

    public object_id<asset_object> asset_id;

    public asset(long lAmount, object_id<asset_object> assetObjectobjectId) {
        amount = lAmount;
        asset_id = assetObjectobjectId;
    }

    public void write_to_encoder(base_encoder baseEncoder) {

        raw_type rawObject = new raw_type();
        baseEncoder.write(rawObject.get_byte_array(this.amount));

        baseEncoder.write(rawObject.get_byte_array(this.asset_id.get_instance()));

//        rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(this.asset_id.get_instance()));
    }
}

