package com.cocos.library_base.invokedpages.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
@Setter
@Getter
public class BaseResultModel<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String actionId;
}
