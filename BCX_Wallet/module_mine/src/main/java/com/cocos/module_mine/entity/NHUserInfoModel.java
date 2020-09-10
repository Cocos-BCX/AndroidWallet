package com.cocos.module_mine.entity;

/**
 * created by Jiang on 2020/9/10 12:04
 */
public class NHUserInfoModel {

    /**
     * liquidity : 17460152
     * name : cocos-1.3.10-lp
     * icon : http://download.dappx.com/swap_token.png
     * type : swap cert
     * precision : 5
     * version : 1
     * token_id : 1.3.10
     */

    private int liquidity;
    private String name;
    private String icon;
    private String type;
    private int precision;
    private int version;
    private String token_id;

    public int getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(int liquidity) {
        this.liquidity = liquidity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }
}
