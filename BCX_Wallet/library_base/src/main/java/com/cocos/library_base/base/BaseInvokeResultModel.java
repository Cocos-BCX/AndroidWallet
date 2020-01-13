package com.cocos.library_base.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
@Setter
@Getter
public class BaseInvokeResultModel implements Serializable {
    private int code;
    private String data;
    private String message;
    private String actionId;
}
