package com.cocos.module_asset.entity;

import com.cocos.library_base.entity.BaseResult;

/**
 * @author ningkang.guo
 * @Date 2019/4/2
 */
public class BlockHeaderModel extends BaseResult {

    public BlockHeader data;

    public static class BlockHeader {
        public String previous;
        public String timestamp;
        public String transaction_merkle_root;
        public String witness;
    }
}
