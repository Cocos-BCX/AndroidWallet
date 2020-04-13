package com.cocos.library_base.invokedpages.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignMessage extends BaseInfo {

    private String message;

    public SignMessage() {
        setAction(ActionEnum.SignMessage.getValue());
    }
}
