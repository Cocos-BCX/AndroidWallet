package com.cocos.library_base.invokedpages.model;


import lombok.Getter;
import lombok.Setter;

/**
 * 登录认证信息
 */

@Getter
@Setter
public class Authorize extends BaseInfo {

    public Authorize() {
        //默认设置类型
        setAction(ActionEnum.Authorize.getValue());
    }

}


