package com.cocos.library_base.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/10/21
 */
public class FeesModel {

    public List<FeesBean> fees;

    public static class FeesBean {
        public BigDecimal amount;
        public String asset_id;
    }
}
