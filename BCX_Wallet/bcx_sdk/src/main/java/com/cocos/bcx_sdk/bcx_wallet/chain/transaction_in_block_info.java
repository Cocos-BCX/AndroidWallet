package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/6/6
 */
public class transaction_in_block_info {

    private String id;
    private long block_num;
    private String trx_in_block;
    private String trx_hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBlock_num() {
        return block_num;
    }

    public void setBlock_num(long block_num) {
        this.block_num = block_num;
    }

    public String getTrx_in_block() {
        return trx_in_block;
    }

    public void setTrx_in_block(String trx_in_block) {
        this.trx_in_block = trx_in_block;
    }

    public String getTrx_hash() {
        return trx_hash;
    }

    public void setTrx_hash(String trx_hash) {
        this.trx_hash = trx_hash;
    }
}
