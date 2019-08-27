package com.cocos.library_base.entity;

import java.io.Serializable;
import java.util.List;

public class NhAssetOrderEntity extends BaseResult {

    /**
     * code : 1
     * data : [{"id":"4.3.108","seller":"1.2.30","otcaccount":"1.2.11233","nh_asset_id":"4.2.4287","asset_qualifier":"COCOS","world_view":"CCShooter","base_describe":"{\"type\":\"weapon\",\"desc\":\"weapon\",\"name\":\"武器架\",\"icon\":\"weapon.png\",\"attached\":1,\"isLottery\":true}","nh_hash":"bf100000000000003f433f450fbf2c9915cd4633815608472c1641e74b0de207","price":{"amount":1.0E7,"asset_id":"1.3.0"},"memo":"vdfdfvb","expiration":"2019-07-18T03:54:37"},{"id":"4.3.109","seller":"1.2.30","otcaccount":"1.2.11233","nh_asset_id":"4.2.4294","asset_qualifier":"COCOS","world_view":"CCShooter","base_describe":"{\"type\":\"weapon\",\"desc\":\"weapon\",\"name\":\"武器架\",\"icon\":\"weapon.png\",\"attached\":1,\"isLottery\":true}","nh_hash":"c6100000000000003f433f450fbf2c9915cd4633815608472c1641e74b0de207","price":{"amount":1200000,"asset_id":"1.3.0"},"memo":"test","expiration":"2019-07-18T04:41:40"}]
     * message : success
     */

    private List<NhOrderBean> data;


    public List<NhOrderBean> getData() {
        return data;
    }

    public void setData(List<NhOrderBean> data) {
        this.data = data;
    }

    public static class NhOrderBean implements Serializable {

        public String id;
        public String minerFee;
        public String feeSymbol;
        public String seller;
        public String sellerName;
        public String otcaccount;
        public String nh_asset_id;
        public String asset_qualifier;
        public String world_view;
        public String base_describe;
        public String nh_hash;
        public PriceBean price;
        public String memo;
        public String expiration;
        public String expirationTime;
        public String priceWithSymbol;
        public boolean isMineOrder;

        public static class PriceBean implements Serializable {

            public String amount;
            public String asset_id;

        }
    }
}
