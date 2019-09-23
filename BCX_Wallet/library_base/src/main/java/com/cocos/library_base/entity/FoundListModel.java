package com.cocos.library_base.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */

@Setter
@Getter
public class FoundListModel extends BaseResult {

    private String imageUrl;

    private String listDesc;

    private String listTitle;

    private String linkUrl;

    private String headerTitle = null;

}
