package com.cocos.library_base.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/12/18
 */
@Getter
@Setter
public class CurrencyRateModel {

    private int status;
    private String msg;
    private CurrencyRateBean data;

    @Getter
    @Setter
    public static class CurrencyRateBean {
        private String reason;
        private int error_code;
        private List<CurrencyRateBeanResult> result;

        @Getter
        @Setter
        public static class CurrencyRateBeanResult {
            private String currencyF;
            private String currencyF_Name;
            private String currencyT;
            private String currencyT_Name;
            private String currencyFD;
            private String exchange;
            private String result;
            private String updateTime;
        }
    }
}
