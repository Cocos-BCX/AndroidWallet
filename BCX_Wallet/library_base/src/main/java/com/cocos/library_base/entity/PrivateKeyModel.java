package com.cocos.library_base.entity;

import com.cocos.library_base.entity.BaseResult;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/1/30
 */
@Getter
@Setter
public class PrivateKeyModel extends BaseResult {
    private Map<String,String> data;
}
