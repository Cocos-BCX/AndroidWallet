package com.cocos.module_mine.contact;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityEditContactBinding;

import java.util.Objects;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
@Route(path = RouterActivityPath.ACTIVITY_EDIT_CONTACT)
public class EditContactActivity extends BaseActivity<ActivityEditContactBinding, EditContactViewModel> {

    private ContactModel contactModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_edit_contact;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            contactModel = (ContactModel) Objects.requireNonNull(getIntent().getExtras()).getSerializable(IntentKeyGlobal.CONTACT_ENTITY);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setContactModel(contactModel);
    }
}
