package com.cocos.library_base.utils;

import com.cocos.library_base.global.SPKeyGlobal;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/12/19
 */
public class CurrencyUtils {


    public static String getTotalCurrencyType() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? "总资产(￥)" : "总资产（$）";
    }


    public static String getSingleCurrencyType() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? "≈ ￥" : "≈ $";
    }

    public static String getCnyCocosPrice() {
        String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
        String exchange = SPUtils.getString(Utils.getContext(), SPKeyGlobal.CURRENCY_RATE);
        BigDecimal cnyCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(exchange));
        return cnyCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? cnyCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : cnyCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getUsdCocosPrice() {
        return SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
    }

    public static String getCocosPrice() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? getCnyCocosPrice() : getUsdCocosPrice();
    }

    public static String getTotalCnyCocosPrice(String totalValue) {
        String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
        String exchange = SPUtils.getString(Utils.getContext(), SPKeyGlobal.CURRENCY_RATE);
        BigDecimal totalCnyCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(exchange)).multiply(new BigDecimal(totalValue));
        return totalCnyCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? totalCnyCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : totalCnyCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getTotalUsdCocosPrice(String totalValue) {
        String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
        BigDecimal totalUsdCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(totalValue));
        return totalUsdCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? totalUsdCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : totalUsdCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getTotalCocosPrice(String totalValue) {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? getTotalCnyCocosPrice(totalValue) : getTotalUsdCocosPrice(totalValue);
    }
}
