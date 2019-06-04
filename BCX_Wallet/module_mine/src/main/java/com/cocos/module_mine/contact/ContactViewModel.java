package com.cocos.module_mine.contact;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.utils.singleton.ContactDaoInstance;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class ContactViewModel extends BaseViewModel {

    private int type;

    public ContactViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt RecyclerViewVisible = new ObservableInt(View.VISIBLE);

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean addContactObservable = new ObservableBoolean(false);
    }


    //返回按钮事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //添加联系人按钮事件
    public BindingCommand addContactOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.addContactObservable.set(!uc.addContactObservable.get());
        }
    });

    public ObservableList<ContactItemViewModel> contactObservableList = new ObservableArrayList<>();

    public ItemBinding<ContactItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_item_contacts);

    public final BindingRecyclerViewAdapter<ContactItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public void requestContactsListData() {
        List<ContactModel> contactModels = ContactDaoInstance.getContactDaoInstance().queryAllContact();
        if (null == contactModels || contactModels.size() <= 0) {
            emptyViewVisible.set(View.VISIBLE);
            RecyclerViewVisible.set(View.GONE);
            return;
        }
        emptyViewVisible.set(View.GONE);
        RecyclerViewVisible.set(View.VISIBLE);
        contactObservableList.clear();
        for (ContactModel contactModel : contactModels) {
            ContactItemViewModel itemViewModel = new ContactItemViewModel(ContactViewModel.this, contactModel, type);
            contactObservableList.add(itemViewModel);
        }
    }

    public void setIntentType(int type) {
        this.type = type;
    }
}
