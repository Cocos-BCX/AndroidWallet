package com.cocos.library_base.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/15
 */
@Getter
@Setter
public class UserModel extends BaseResult {

    private String userId;
    private String userName;
    private String activePrivateKey;
    private String ownerPrivateKey;

}
