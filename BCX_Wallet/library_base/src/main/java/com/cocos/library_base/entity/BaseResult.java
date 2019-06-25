package com.cocos.library_base.entity;

import com.cocos.library_base.R;
import com.cocos.library_base.utils.ToastUtils;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseResult implements Serializable {

    public int code;
    public String message;

    public boolean isSuccess() {
        if (code == 1) {
            return true;
        } else if (code == 102) {
            ToastUtils.showShort(R.string.net_work_failed);
            return false;
        }
        return false;
    }

}