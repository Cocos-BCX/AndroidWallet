package com.cocos.library_base.entity;

import com.cocos.library_base.entity.BaseResult;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */

@Setter
@Getter
public class FoundListModel extends BaseResult {

    private int listIcon;

    private String listDesc;

    private String listTitle;

    private String listUrl;

}
