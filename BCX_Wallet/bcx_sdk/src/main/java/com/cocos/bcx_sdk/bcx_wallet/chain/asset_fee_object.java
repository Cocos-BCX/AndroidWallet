package com.cocos.bcx_sdk.bcx_wallet.chain;


import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;

public class asset_fee_object {

    public String amount;

    public object_id<asset_object> asset_id;

    public asset_fee_object(String lAmount, object_id<asset_object> assetObjectobjectId) {
        amount = lAmount;
        asset_id = assetObjectobjectId;
    }

    /**
     * @param baseEncoder
     */
    public void write_to_encoder(base_encoder baseEncoder) {

        raw_type rawObject = new raw_type();

        baseEncoder.write(rawObject.get_byte_array(Long.valueOf(this.amount)));

//        rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(this.asset_id.get_instance()));
        baseEncoder.write(rawObject.get_byte_array(asset_id.get_instance()));
    }
}
