package com.cocos.bcx_sdk.bcx_wallet.common;


public class unsigned_short extends Number {
    public  static final unsigned_short ZERO = new unsigned_short((short)0);

    private short value;
    public unsigned_short(short value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return toInt();
    }

    @Override
    public short shortValue() {
        return value;
    }

    @Override
    public long longValue() {
        return toInt();
    }

    @Override
    public float floatValue() {
        return toInt();
    }

    @Override
    public double doubleValue() {
        return toInt();
    }


    private int toInt() {
        return value & 0xffff;
    }
}
