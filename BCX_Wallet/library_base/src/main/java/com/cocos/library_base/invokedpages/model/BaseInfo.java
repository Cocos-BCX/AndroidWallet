package com.cocos.library_base.invokedpages.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseInfo implements Serializable {

    /**
     * 协议
     */
    private String protocol;

    /**
     * 版本号
     */
    private String version ;

    /**
     * dapp名称
     */
    private String dappName;

    /**
     * dapp 图标
     */
    private String dappIcon;

    /**
     * 执行的操作类型:transfer,login, callContract
     */
    private String action;

    /**
     * 执行的操作id标识
     */
    private String actionId;

    /**
     * 过期时间，单位为秒
     */
    private long expired;

    /**
     * 操作描述
     */
    private String desc;
}
