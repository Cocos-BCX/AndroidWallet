package com.cocos.library_base.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/12/18
 */
@Getter
@Setter
public class PriceModel {
    private String id;
    private String name;
    private String symbol;
    private long rank;
    private String logo;
    private String logo_png;
    private BigDecimal price_usd;
    private BigDecimal price_btc;
    private BigDecimal volume_24h_usd;
    private BigDecimal market_cap_usd;
    private BigDecimal available_supply;
    private BigDecimal total_supply;
    private BigDecimal max_supply;
    private BigDecimal percent_change_1h;
    private BigDecimal percent_change_24h;
    private BigDecimal percent_change_7d;
    private long last_updated;
}
