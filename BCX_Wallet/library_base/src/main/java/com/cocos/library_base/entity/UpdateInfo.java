package com.cocos.library_base.entity;

/**
 * @author ningkang.guo
 * @Date 2019/5/28
 */
public class UpdateInfo {

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        public String version;
        public boolean is_force;
        public String download_url;
        public String info;
    }
}
