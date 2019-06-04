package com.cocos.library_base.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.WebViewModel;

/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */
public class JsWebViewViewModel extends BaseViewModel {


    public JsWebViewViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> webTitle = new ObservableField<>();


    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand rightIconClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });


    public void setWebData(WebViewModel webViewModel) {
        if (!TextUtils.isEmpty(webViewModel.getTitle())) {
            webTitle.set(webViewModel.getTitle());
        }
    }
}
