package com.cocos.library_base.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseResultModel<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public boolean isSuccess() {
        if (code == 1) {
            return true;
        } else {
            return false;
        }
    }

}