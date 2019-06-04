package com.cocos.library_base.base;

import android.webkit.JavascriptInterface;

import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.js_params.JsParamsEventModel;

import org.greenrobot.eventbus.EventBus;


public class JavaScriptUtil {

    @JavascriptInterface
    public void pushMessage(String serialNumber, String params, String methodName) {
        EventBusCarrier eventBusCarrier = new EventBusCarrier();
        JsParamsEventModel jsParamsEventModel = new JsParamsEventModel();
        jsParamsEventModel.serialNumber = serialNumber;
        jsParamsEventModel.param = params;
        eventBusCarrier.setEventType(methodName);
        eventBusCarrier.setObject(jsParamsEventModel);
        EventBus.getDefault().post(eventBusCarrier);
    }
}
