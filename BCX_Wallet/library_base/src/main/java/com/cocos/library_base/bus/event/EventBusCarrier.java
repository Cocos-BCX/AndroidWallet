package com.cocos.library_base.bus.event;


import lombok.Getter;
import lombok.Setter;

/**
 * 发送帐户名
 */

public class EventBusCarrier {
    private String eventType; //区分事件的类型
    private Object object;  //事件的实体类

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
