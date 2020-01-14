package com.cocos.library_base.invokedpages.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 转账信息
 */

@Setter
@Getter
public class Transfer extends BaseInfo {

    private String from;

    private String to;

    private double amount;

    private String symbol;

    private int precision;

    private String memo;

    public Transfer() {
        setAction(ActionEnum.Transfer.getValue());
    }
}
