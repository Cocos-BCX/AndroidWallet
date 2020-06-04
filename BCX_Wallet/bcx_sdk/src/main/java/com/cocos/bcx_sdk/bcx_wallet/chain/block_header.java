package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;


/**
 * get_block_header 返回数据实体
 */
public class block_header {

    private String previous;
    private String timestamp;
    private String witness;
    private String transaction_merkle_root;
    private List<Object> extensions;

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getWitness() {
        return witness;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }

    public String getTransaction_merkle_root() {
        return transaction_merkle_root;
    }

    public void setTransaction_merkle_root(String transaction_merkle_root) {
        this.transaction_merkle_root = transaction_merkle_root;
    }

    public List<?> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Object> extensions) {
        this.extensions = extensions;
    }
}
