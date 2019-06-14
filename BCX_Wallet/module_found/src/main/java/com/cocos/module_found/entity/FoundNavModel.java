package com.cocos.module_found.entity;

import com.cocos.library_base.entity.BaseResult;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */

@Getter
@Setter
public class FoundNavModel extends BaseResult {

    private String navTitle;

    private String navIconUrl;

    private int navTitleColor;

    private String navUrl;
}
