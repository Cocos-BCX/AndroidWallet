package com.cocos.module_mine.entity;

import com.cocos.library_base.entity.BaseResult;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/7/17
 */
@Getter
@Setter
public class SaleNHAssetParamsModel extends BaseResult {


    /**
     * NH资产ID
     */
    private String nhAssetId;

    /**
     * 价格
     */
    private String priceAmount;

    private String priceSymbol;

    /**
     * 有效时间
     */
    private long validTime;

    /**
     * 挂单费
     */
    private String saleFee;

    /**
     * 备注
     */
    private String orderMemo;



}
