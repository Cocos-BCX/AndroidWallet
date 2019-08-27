package com.cocos.library_base.entity;

import java.io.Serializable;

/**
 * @author ningkang.guo
 * @Date 2019/5/16
 */
public class FeeModel extends BaseResult {

    public DataBean data;

    public static class DataBean implements Serializable {
        public String amount;
        public String asset_id;
    }
}
