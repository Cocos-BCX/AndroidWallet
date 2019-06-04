package com.cocos.module_mine.contact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.widget.popmenu.DropPopMenu;
import com.cocos.library_base.widget.popmenu.MenuItem;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityContactBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */

@Route(path = RouterActivityPath.ACTIVITY_CONTACT)
public class ContactActivity extends BaseActivity<ActivityContactBinding, ContactViewModel> {

    private int type;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_contact;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null != busCarrier && TextUtils.equals(EventTypeGlobal.CONTACT_CHANGED_TYPE, busCarrier.getEventType())) {
            viewModel.requestContactsListData();
        } else if (null != busCarrier && TextUtils.equals(EventTypeGlobal.SET_CONTACT, busCarrier.getEventType())) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.CONTACT_ENTITY, (ContactModel) busCarrier.getObject());
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
    }

    @Override
    public void initParam() {
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                type = bundle.getInt(IntentKeyGlobal.TRANSFER_TO_CONTACT);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setIntentType(type);
        viewModel.requestContactsListData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentKeyGlobal.REQ_CAPTURE_CODE) {
                try {
                    Bundle bundle = data.getExtras();
                    String captureResult = bundle.getString(IntentKeyGlobal.CAPTURE_RESULT);
                    JSONObject jsonObject = new JSONObject(captureResult);
                    if (jsonObject.has("address")) {
                        ContactModel contactModel = new ContactModel();
                        contactModel.accountName = String.valueOf(jsonObject.get("address"));
                        Bundle bundles = new Bundle();
                        bundles.putSerializable(IntentKeyGlobal.CONTACT_ENTITY, contactModel);
                        ARouter.getInstance().build(RouterActivityPath.ACTIVITY_EDIT_CONTACT).with(bundles).navigation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.addContactObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                onClickPopIcon(binding.addContact);
            }
        });
    }

    public void onClickPopIcon(View view) {
        DropPopMenu dropPopMenu = new DropPopMenu(this);
        dropPopMenu.setTriangleIndicatorViewColor(Utils.getColor(R.color.color_F5F6FA));
        dropPopMenu.setBackgroundResource(R.drawable.bg_drop_pop_menu_white_shap);
        dropPopMenu.setItemTextColor(Utils.getColor(R.color.color_262A33));
        dropPopMenu.setIsShowIcon(true);
        dropPopMenu.setOnItemClickListener(new DropPopMenu.OnItemClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id, MenuItem menuItem) {
                if (menuItem.getItemId() == 1) {
                    ARouter.getInstance().build(RouterActivityPath.ACTIVITY_EDIT_CONTACT).navigation();
                } else {
                    RxPermissions rxPermissions = new RxPermissions(ContactActivity.this);
                    rxPermissions.request(Manifest.permission.CAMERA)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(IntentKeyGlobal.TO_CAPTURE, IntentKeyGlobal.GET_CAPTURE_RESULT);
                                        ARouter.getInstance().
                                                build(RouterActivityPath.ACTIVITY_CAPTURE).
                                                with(bundle).
                                                navigation(ContactActivity.this, IntentKeyGlobal.REQ_CAPTURE_CODE);
                                    }
                                }
                            });
                }
            }
        });
        dropPopMenu.setMenuList(getIconMenuList());
        dropPopMenu.show(view);
    }

    private List<MenuItem> getIconMenuList() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(R.drawable.contact_add_manully_icon, 1, Utils.getString(R.string.module_mine_contact_add_manully)));
        list.add(new MenuItem(R.drawable.contact_scan_add_icon, 2, Utils.getString(R.string.module_mine_contact_scan_add)));
        return list;
    }
}
