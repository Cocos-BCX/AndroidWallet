package com.cocos.library_base.http.apiserver;

import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.entity.UpdateInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api  接口
 *
 * @author ningkang
 */

public interface ApiServer {

//    /**
//     * 校验交易密码
//     */
//    @POST("api/v1/user/check")
//    Observable<BaseEntity> verifyPwd(@Body RequestBody requestBody);

    /**
     * 获取版本更新信息
     */
    @GET("/getPolicyUrl")
    Observable<UpdateInfo> getVersionInfo(@Query("channel") int channel, @Query("platform") String platform);


    /**
     * 获取节点信息
     */
    @GET("/getParams")
    Observable<NodeInfoModel> getNodeInfo();

}
