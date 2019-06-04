package com.cocos.module_mine.multi_language;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.multi_language.SPUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */
public class MultiLanguageViewModel extends BaseViewModel {


    public MultiLanguageViewModel(@NonNull Application application) {
        super(application);
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        chineseCheck.set(selectLanguage == 0);
        englishCheck.set(selectLanguage == 1);
    }

    public ObservableBoolean chineseCheck = new ObservableBoolean(true);

    public ObservableBoolean englishCheck = new ObservableBoolean(false);


    //中文按钮的点击事件
    public BindingCommand chineseLanguageOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            englishCheck.set(false);
            chineseCheck.set(true);
            LocalManageUtil.saveSelectLanguage(Utils.getContext(), 0);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_LANGUAGE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    //英文的点击事件
    public BindingCommand englishLanguageOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            englishCheck.set(true);
            chineseCheck.set(false);
            LocalManageUtil.saveSelectLanguage(Utils.getContext(), 1);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_LANGUAGE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    //取消按钮的点击事件
    public BindingCommand cancelOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

}
