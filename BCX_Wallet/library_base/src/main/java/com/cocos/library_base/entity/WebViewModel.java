package com.cocos.library_base.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */
@Setter
@Getter
public class WebViewModel implements Serializable {
    public String title;
    public String url;
    public String iconUrl;
    public String desc;
}
