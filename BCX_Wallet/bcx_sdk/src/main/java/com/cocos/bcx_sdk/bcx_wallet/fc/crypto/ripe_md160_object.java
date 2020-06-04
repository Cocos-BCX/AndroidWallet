package com.cocos.bcx_sdk.bcx_wallet.fc.crypto;

import com.google.common.io.BaseEncoding;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class ripe_md160_object {
    public int hash[] = new int[5];

    public static class ripemd160_object_deserializer implements JsonDeserializer<ripe_md160_object> {

        @Override
        public ripe_md160_object deserialize(JsonElement json,
                                             Type typeOfT,
                                             JsonDeserializationContext context) throws JsonParseException {
            ripe_md160_object ripemd160Object = new ripe_md160_object();
            BaseEncoding encoding = BaseEncoding.base16().lowerCase();
            byte[] byteContent = encoding.decode(json.getAsString());
            if (byteContent.length != 20) {
                throw new JsonParseException("ripe_md160_object size not correct.");
            }
            for (int i = 0; i < 5; ++i) {
                ripemd160Object.hash[i] = ((byteContent[i * 4 + 3] & 0xff) << 24) |
                        ((byteContent[i * 4 + 2] & 0xff) << 16) |
                        ((byteContent[i * 4 + 1] & 0xff) << 8) |
                        ((byteContent[i * 4 ] & 0xff));
            }

            return ripemd160Object;
        }
    }
}
