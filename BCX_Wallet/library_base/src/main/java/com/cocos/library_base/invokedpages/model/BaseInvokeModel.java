package com.cocos.library_base.invokedpages.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
@Setter
@Getter
@ToString
public class BaseInvokeModel implements Serializable {
    private String packageName;
    private String className;
    private String appName;
    private String action;
    private String param;
}
