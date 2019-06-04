package com.cocos.library_base.entity;

/**
 * @author ningkang.guo
 * @Date 2019/5/15
 */
public class AccountNhAssetOrderModel {

    private String id;
    private String seller;
    private String otcaccount;
    private String nh_asset_id;
    private String asset_qualifier;
    private String world_view;
    private String base_describe;
    private String nh_hash;
    private PriceBean price;
    private String memo;
    private String expiration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getOtcaccount() {
        return otcaccount;
    }

    public void setOtcaccount(String otcaccount) {
        this.otcaccount = otcaccount;
    }

    public String getNh_asset_id() {
        return nh_asset_id;
    }

    public void setNh_asset_id(String nh_asset_id) {
        this.nh_asset_id = nh_asset_id;
    }

    public String getAsset_qualifier() {
        return asset_qualifier;
    }

    public void setAsset_qualifier(String asset_qualifier) {
        this.asset_qualifier = asset_qualifier;
    }

    public String getWorld_view() {
        return world_view;
    }

    public void setWorld_view(String world_view) {
        this.world_view = world_view;
    }

    public String getBase_describe() {
        return base_describe;
    }

    public void setBase_describe(String base_describe) {
        this.base_describe = base_describe;
    }

    public String getNh_hash() {
        return nh_hash;
    }

    public void setNh_hash(String nh_hash) {
        this.nh_hash = nh_hash;
    }

    public PriceBean getPrice() {
        return price;
    }

    public void setPrice(PriceBean price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public static class PriceBean {

        private double amount;
        private String asset_id;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }
    }
}
