package com.cocos.library_base.http.apiserver;

import com.cocos.library_base.entity.CurrencyRateModel;
import com.cocos.library_base.entity.FoundModel;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.entity.PriceModel;
import com.cocos.library_base.entity.UpdateInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api  接口
 *
 * @author ningkang
 */

public interface ApiServer {

    /**
     * 获取版本更新信息
     */
    @GET("getPolicyUrl")
    Observable<UpdateInfo> getVersionInfo(@Query("channel") int channel, @Query("platform") String platform);


    /**
     * 获取节点信息
     */
    @GET("getParams")
    Observable<NodeInfoModel> getNodeInfo();


    /**
     * 获取发现页
     */
    @GET("getBanInfo")
    Observable<FoundModel> getFoundInfo();

    /**
     * 获取cocosbcx 价格
     */
    @GET("v1/ticker")
    Observable<List<PriceModel>> getCocosPrice(@Query("code") String code);

    /**
     * get currency rate
     */
    @GET("currencyRate")
    Observable<CurrencyRateModel> getCurrencyRate();

}
