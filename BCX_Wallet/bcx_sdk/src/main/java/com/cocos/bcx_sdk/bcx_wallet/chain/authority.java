package com.cocos.bcx_sdk.bcx_wallet.chain;

import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class authority {
    public Integer weight_threshold;
    public HashMap<object_id<account_object>, Integer> account_auths = new HashMap<>();
    public HashMap<types.public_key_type, Integer> key_auths = new HashMap<>();
    private ArrayList<ArrayList> address_auths = new ArrayList();

    public authority(int nWeightThreshold, types.public_key_type publicKeyType, int nWeightType) {
        weight_threshold = nWeightThreshold;
        key_auths.put(publicKeyType, nWeightType);
    }

    public boolean is_public_key_type_exist(types.public_key_type publicKeyType) {
        return key_auths.containsKey(publicKeyType);
    }

    public List<types.public_key_type> get_keys() {
        List<types.public_key_type> listKeyType = new ArrayList<>();
        listKeyType.addAll(key_auths.keySet());
        return listKeyType;
    }

    public void write_to_encode(base_encoder baseEncoder) {
        raw_type rawObject = new raw_type();
        baseEncoder.write(rawObject.get_byte_array(weight_threshold));
        rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(account_auths.size()));

        for (Map.Entry<object_id<account_object>, Integer> entry : account_auths.entrySet()) {
            baseEncoder.write(rawObject.get_byte_array(entry.getKey().get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(entry.getKey().get_instance()));
            Integer weight = (Integer) entry.getValue();
            baseEncoder.write(rawObject.get_byte_array(weight.shortValue()));
        }

        rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(key_auths.size()));

        for (Map.Entry<types.public_key_type, Integer> key : key_auths.entrySet()) {
            types.public_key_type pub = (types.public_key_type) key.getKey();
            baseEncoder.write(pub.key_data);
            Integer weight = (Integer) key.getValue();
            baseEncoder.write(rawObject.get_byte_array(weight.shortValue()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(address_auths.size()));
        }
    }
}
