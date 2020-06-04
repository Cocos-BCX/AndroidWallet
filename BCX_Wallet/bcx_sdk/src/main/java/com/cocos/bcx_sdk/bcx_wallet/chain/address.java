package com.cocos.bcx_sdk.bcx_wallet.chain;

import android.annotation.TargetApi;
import android.os.Build;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_error.MalformedAddressException;
import com.cocos.bcx_sdk.bcx_utils.bitlib.PublicKey;
import com.cocos.bcx_sdk.bcx_utils.bitlib.bitcoinj.Base58;
import com.cocos.bcx_sdk.bcx_utils.bitlib.crypto.digest.RIPEMD160Digest;
import com.google.common.primitives.Bytes;

import org.bitcoinj.core.ECKey;

import java.util.Arrays;

/**
 * Class used to encapsulate address-related operations.
 */
public class address {

    public final static String BITSHARES_PREFIX = CocosBcxApiWrapper.coreAsset;

    private PublicKey publicKey;
    private String prefix;

    public address(ECKey key) {
        this.publicKey = new PublicKey(key);
        this.prefix = BITSHARES_PREFIX;
    }

    public address(ECKey key, String prefix) {
        this.publicKey = new PublicKey(key);
        this.prefix = prefix;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public address(String address) throws MalformedAddressException {
        this.prefix = address.substring(0, 3);
        byte[] decoded = Base58.decode(address.substring(3, address.length()));
        byte[] pubKey = Arrays.copyOfRange(decoded, 0, decoded.length - 4);
        byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 4, decoded.length);
        publicKey = new PublicKey(ECKey.fromPublicOnly(pubKey));
        byte[] calculatedChecksum = calculateChecksum(pubKey);
        for (int i = 0; i < calculatedChecksum.length; i++) {
            if (checksum[i] != calculatedChecksum[i]) {
                throw new MalformedAddressException("Checksum error");
            }
        }
    }


    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public String toString() {
        byte[] pubKey = this.publicKey.toBytes();
        byte[] checksum = calculateChecksum(pubKey);
        byte[] pubKeyChecksummed = Bytes.concat(pubKey, checksum);
        return this.prefix + Base58.encode(pubKeyChecksummed);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private byte[] calculateChecksum(byte[] data) {
        byte[] checksum = new byte[160 / 8];
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(data, 0, data.length);
        ripemd160Digest.doFinal(checksum, 0);
        return Arrays.copyOfRange(checksum, 0, 4);
    }
}
