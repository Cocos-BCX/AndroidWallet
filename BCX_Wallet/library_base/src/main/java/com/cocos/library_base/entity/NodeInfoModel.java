package com.cocos.library_base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/5/31
 */
public class NodeInfoModel implements Serializable {

    public int status;
    public String msg;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        public String name;
        public String ws;
        public String faucetUrl;
        public String chainId;
        public String coreAsset;
        public String type;
        public boolean isNative;
        public boolean isForce;

        @Override
        public String toString() {
            return name + ws + faucetUrl + chainId + coreAsset + type;
        }
    }
}
