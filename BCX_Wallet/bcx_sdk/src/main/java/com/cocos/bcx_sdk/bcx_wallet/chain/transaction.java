package com.cocos.bcx_sdk.bcx_wallet.chain;


import com.cocos.bcx_sdk.bcx_wallet.common.unsigned_short;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.bitutil;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.ripe_md160_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.crypto.sha256_object;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class transaction {

    public class required_authorities {
        public List<object_id<account_object>> active;
        public List<object_id<account_object>> owner;
        public List<authority> other;
    }

    /**
     * Least significant 16 bits from the reference block number. If @ref relative_expiration is zero, this field
     * must be zero as well.
     */
    public unsigned_short ref_block_num = unsigned_short.ZERO;
    /**
     * The first non-block-number 32-bits of the reference block ID. Recall that block IDs have 32 bits of block
     * number followed by the actual block hash, so this field should be set using the second 32 bits in the
     *
     * @ref block_id_type
     */
    public UnsignedInteger ref_block_prefix = UnsignedInteger.ZERO;

    /**
     * This field specifies the absolute expiration for this transaction.
     */
    public Date expiration;
    public List<operations.operation_type> operations;
    public Set<types.void_t> extensions;


    public ripe_md160_object id() {
        return null;
    }

    public void set_reference_block(ripe_md160_object reference_block) {
        ref_block_num = new unsigned_short((short) bitutil.endian_reverse_u32(reference_block.hash[0]));
        ref_block_prefix = UnsignedInteger.fromIntBits(reference_block.hash[1]);
    }

    public void set_expiration(Date expiration_time) {
        expiration = expiration_time;
    }


    public sha256_object sig_digest(sha256_object chain_id) {
        sha256_object.encoder enc = new sha256_object.encoder();
        enc.write(chain_id.hash, 0, chain_id.hash.length);
        raw_type rawTypeObject = new raw_type();
        enc.write(rawTypeObject.get_byte_array(ref_block_num.shortValue()));
        enc.write(rawTypeObject.get_byte_array(ref_block_prefix.intValue()));
        enc.write(rawTypeObject.get_byte_array(expiration));
        rawTypeObject.pack(enc, UnsignedInteger.fromIntBits(operations.size()));
        for (com.cocos.bcx_sdk.bcx_wallet.chain.operations.operation_type operationType : operations) {
            rawTypeObject.pack(enc, UnsignedInteger.fromIntBits(operationType.nOperationType));
            com.cocos.bcx_sdk.bcx_wallet.chain.operations.base_operation baseOperation = (com.cocos.bcx_sdk.bcx_wallet.chain.operations.base_operation) operationType.operationContent;
            baseOperation.write_to_encoder(enc);
        }
        rawTypeObject.pack(enc, UnsignedInteger.fromIntBits(extensions.size()));
        return enc.result();
    }
}
