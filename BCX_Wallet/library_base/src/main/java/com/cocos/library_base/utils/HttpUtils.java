package com.cocos.library_base.utils;

import android.text.TextUtils;

import com.cocos.library_base.entity.CurrencyRateModel;
import com.cocos.library_base.entity.PriceModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.api.CocosPriceUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.http.HttpMethods;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author ningkang.guo
 * @Date 2019/12/18
 */
public class HttpUtils {

    /**
     * get currency rate
     */
    public static void getCurrencyRate() {
        try {
            Observable<CurrencyRateModel> observable = BaseUrlApi.getApiBaseService().getCurrencyRate();
            HttpMethods.toSubscribe(observable, new BaseObserver<CurrencyRateModel>() {
                @Override
                protected void onBaseNext(CurrencyRateModel data) {
                    if (null != data && data.getStatus() == 200) {
                        for (CurrencyRateModel.CurrencyRateBean.CurrencyRateBeanResult resultBean : data.getData().getResult()) {
                            if (TextUtils.equals(resultBean.getCurrencyF(), "USD")) {
                                SPUtils.putString(Utils.getContext(), SPKeyGlobal.CURRENCY_RATE, resultBean.getExchange());
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * get cocos price
     */
    public static void getCocosPrice() {
        try {
            Observable<List<PriceModel>> observable = CocosPriceUrlApi.getApiBaseService().getCocosPrice("cocosbcx");
            HttpMethods.toSubscribe(observable, new BaseObserver<List<PriceModel>>() {
                @Override
                protected void onBaseNext(List<PriceModel> data) {
                    if (null != data && data.size() > 0) {
                        PriceModel priceModel = data.get(0);
                        BigDecimal price = priceModel.getPrice_usd();
                        String cocosPrice = price.compareTo(BigDecimal.ZERO) == 0 ? price.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : price.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
                        SPUtils.putString(Utils.getContext(), SPKeyGlobal.COCOS_PRICE, cocosPrice);
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}
