package com.cocos.library_base.entity;

import java.util.List;

public class ContractDataEntity extends BaseResult{

    public  ContractDataBean data;

    public class ContractDataBean {
    public  long ref_block_num;
    public  long ref_block_prefix;
    public  String expiration;
    public List<List<Object>> operations;
    }

}
