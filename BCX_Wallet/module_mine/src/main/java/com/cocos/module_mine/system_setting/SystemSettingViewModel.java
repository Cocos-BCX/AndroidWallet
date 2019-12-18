package com.cocos.module_mine.system_setting;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.module_mine.R;

/**
 * @author ningkang.guo
 * @Date 2019/3/1
 */
public class SystemSettingViewModel extends BaseViewModel {

    public SystemSettingViewModel(@NonNull Application application) {
        super(application);
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        languageType.set(selectLanguage == 0 ? Utils.getString(R.string.language_cn) : Utils.getString(R.string.language_en));
        NodeInfoModel.DataBean netNode = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
        if (null != netNode && !TextUtils.isEmpty(netNode.type)) {
            netType.set(TextUtils.equals("0", netNode.type) ? Utils.getString(R.string.module_mine_net_test_text) : Utils.getString(R.string.module_mine_net_main_text));
        } else {
            netType.set("");
        }
        // 0 :人民币  1：美元
        int currencyType = SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0);
        coinType.set(currencyType == 0 ? Utils.getString(R.string.module_mine_coin_cny) : Utils.getString(R.string.module_mine_coin_usd));
    }

    public ObservableField<String> languageType = new ObservableField<>(Utils.getString(R.string.language_cn));

    public ObservableField<String> coinType = new ObservableField<>(Utils.getString(R.string.module_mine_coin_cny));

    // 节点的默认值
    public ObservableField<String> netType = new ObservableField<>(Utils.getString(R.string.module_mine_net_test_text));

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean multiLanguageObservable = new ObservableBoolean(false);
        public ObservableBoolean netSettingObservable = new ObservableBoolean(false);
        public ObservableBoolean coinSettingObservable = new ObservableBoolean(false);
    }

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //多语言按钮的点击事件
    public BindingCommand multiLanguageOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.multiLanguageObservable.set(!uc.multiLanguageObservable.get());
        }
    });


    //网络设置按钮的点击事件
    public BindingCommand netSettingOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.netSettingObservable.set(!uc.netSettingObservable.get());
        }
    });

    //网络设置按钮的点击事件
    public BindingCommand coinTypeSettingOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.coinSettingObservable.set(!uc.coinSettingObservable.get());
        }
    });
}
