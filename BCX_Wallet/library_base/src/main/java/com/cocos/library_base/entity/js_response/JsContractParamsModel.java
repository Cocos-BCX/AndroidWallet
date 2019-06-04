package com.cocos.library_base.entity.js_response;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/4/30
 */
public class JsContractParamsModel {

    /**
     * nameOrId : contract.dicegame
     * functionName : bet
     * valueList : [50,1]
     * runTime : 10
     * onlyGetFee : false
     */
    public String nameOrId;
    public String functionName;
    public int runtime;
    public boolean onlyGetFee;
    public List<String> valueList;


}
