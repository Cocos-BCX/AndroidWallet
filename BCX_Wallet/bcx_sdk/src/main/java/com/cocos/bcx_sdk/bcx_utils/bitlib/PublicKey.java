package com.cocos.bcx_sdk.bcx_utils.bitlib;


import com.cocos.bcx_sdk.bcx_wallet.chain.address;

import org.bitcoinj.core.ECKey;
import org.spongycastle.math.ec.ECPoint;

import java.io.Serializable;

public class PublicKey implements ByteSerializable, Serializable {
    private ECKey publicKey;

    public PublicKey(ECKey key) {
        if (key.hasPrivKey()) {
            throw new IllegalStateException("Passing a private key to PublicKey constructor");
        }
        this.publicKey = key;
    }

    public ECKey getKey() {
        return publicKey;
    }

    @Override
    public byte[] toBytes() {
        if (publicKey.isCompressed()) {
            return publicKey.getPubKey();
        } else {
            publicKey = ECKey.fromPublicOnly(ECKey.compressPoint(publicKey.getPubKeyPoint()));
            return publicKey.getPubKey();
        }
    }

    public String getAddress() {
        ECKey pk = ECKey.fromPublicOnly(publicKey.getPubKey());
        if (!pk.isCompressed()) {
            ECPoint point = ECKey.compressPoint(pk.getPubKeyPoint());
            pk = ECKey.fromPublicOnly(point);
        }
        return new address(pk).toString();
    }

    @Override
    public int hashCode() {
        return publicKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        PublicKey other = (PublicKey) obj;
        return this.publicKey.equals(other.getKey());
    }
}