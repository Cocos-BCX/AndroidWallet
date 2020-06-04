package com.cocos.bcx_sdk.bcx_wallet.common;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class unsigned_number_serializer {
    public static class UnsigendIntegerSerializer implements JsonSerializer<UnsignedInteger> {

        @Override
        public JsonElement serialize(UnsignedInteger src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.longValue());
        }
    }

    public static class UnsigendShortSerializer implements JsonSerializer<unsigned_short> {

        @Override
        public JsonElement serialize(unsigned_short src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.intValue());
        }
    }

    public static class UnsignedLongSerializer implements JsonSerializer<UnsignedLong> {

        @Override
        public JsonElement serialize(UnsignedLong src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.bigIntegerValue());
        }
    }
}
