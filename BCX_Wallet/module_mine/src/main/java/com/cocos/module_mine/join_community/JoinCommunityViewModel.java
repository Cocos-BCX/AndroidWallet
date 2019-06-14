package com.cocos.module_mine.join_community;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_mine.R;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */
public class JoinCommunityViewModel extends BaseViewModel {


    public JoinCommunityViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> officialWechat = new ObservableField<>("Cocos-BCX");
    public ObservableField<String> officialWechatAccount = new ObservableField<>("CocosBCX");
    public ObservableField<String> discord = new ObservableField<>("Cocos-BCX");
    public ObservableField<String> telegram = new ObservableField<>("Cocos-BCX Official (EN)");

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand copyOfficialWechatOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(officialWechat.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", officialWechat.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand copyOfficialWechatAccountOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(officialWechatAccount.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", officialWechatAccount.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand copyDiscordOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", Utils.getString(R.string.module_mine_discord_ling_url));
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand copyTelegramOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", Utils.getString(R.string.module_mine_telegram_link_url));
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

}
