package com.cocos.library_base.entity.js_response;

import com.cocos.library_base.entity.BaseResult;

/**
 * @author ningkang.guo
 * @Date 2019/5/16
 */
public class MemoModel extends BaseResult {

    public DataBean data;

    public static class DataBean {

        //  public boolean isMine;
        public String text;

    }
}
