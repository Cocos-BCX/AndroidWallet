package com.cocos.library_base.utils;

import android.text.TextUtils;

import com.cocos.library_base.R;
import com.cocos.library_base.global.SPKeyGlobal;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/12/19
 */
public class CurrencyUtils {


    public static String getTotalCurrencyType() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? Utils.getString(R.string.module_asset_total_assets) + "(￥)" : Utils.getString(R.string.module_asset_total_assets) + "（$）";
    }


    public static String getSingleCurrencyType() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? "≈ ￥" : "≈ $";
    }

    public static String getCnyCocosPrice() {
        try {
            String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
            if (TextUtils.equals(netType, "0")) {
                return "0.00";
            }
            String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
            String exchange = SPUtils.getString(Utils.getContext(), SPKeyGlobal.CURRENCY_RATE);
            BigDecimal cnyCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(exchange));
            return cnyCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? cnyCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : cnyCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            ToastUtils.showLong(R.string.price_get_error);
            return "0.00";
        }
    }

    public static String getUsdCocosPrice() {
        try {
            String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
            if (TextUtils.equals(netType, "0")) {
                return "0.00";
            }
            return SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
        } catch (Exception e) {
            ToastUtils.showLong(R.string.price_get_error);
            return "0.00";
        }
    }

    public static String getCocosPrice() {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? getCnyCocosPrice() : getUsdCocosPrice();
    }

    public static String getTotalCnyCocosPrice(String totalValue) {
        try {
            String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
            if (TextUtils.equals(netType, "0")) {
                return "0.00";
            }
            String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
            String exchange = SPUtils.getString(Utils.getContext(), SPKeyGlobal.CURRENCY_RATE);
            BigDecimal totalCnyCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(exchange)).multiply(new BigDecimal(totalValue));
            return totalCnyCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? totalCnyCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : totalCnyCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            ToastUtils.showLong(R.string.price_get_error);
            return "0.00";
        }
    }

    public static String getTotalUsdCocosPrice(String totalValue) {
        try {
            String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
            if (TextUtils.equals(netType, "0")) {
                return "0.00";
            }
            String cocos_price = SPUtils.getString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE);
            BigDecimal totalUsdCocosPrice = new BigDecimal(cocos_price).multiply(new BigDecimal(totalValue));
            return totalUsdCocosPrice.compareTo(BigDecimal.ZERO) == 0 ? totalUsdCocosPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : totalUsdCocosPrice.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            ToastUtils.showLong(R.string.price_get_error);
            return "0.00";
        }
    }

    public static String getTotalCocosPrice(String totalValue) {
        return SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0) == 0 ? getTotalCnyCocosPrice(totalValue) : getTotalUsdCocosPrice(totalValue);
    }
}
