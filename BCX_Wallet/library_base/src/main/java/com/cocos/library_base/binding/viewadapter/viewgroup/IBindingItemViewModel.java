package com.cocos.library_base.binding.viewadapter.viewgroup;

/**
 * Created by guoningkang on 2017/6/15.
 */

import android.databinding.ViewDataBinding;

public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
