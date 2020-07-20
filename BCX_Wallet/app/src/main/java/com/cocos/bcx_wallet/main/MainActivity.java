package com.cocos.bcx_wallet.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_wallet.BR;
import com.cocos.bcx_wallet.R;
import com.cocos.bcx_wallet.adapter.MainViewPagerAdapter;
import com.cocos.bcx_wallet.databinding.ActivityMainBinding;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.ConfrimDialogViewModel;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.databinding.DialogAuthorAccountNotExistBinding;
import com.cocos.library_base.entity.AccountNamesEntity;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.Authorize;
import com.cocos.library_base.invokedpages.model.BaseInfo;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.Contract;
import com.cocos.library_base.invokedpages.model.SignMessage;
import com.cocos.library_base.invokedpages.model.Transfer;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ActivityContainer;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.VersionUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.ui.asset.AssetFragment;
import com.cocos.module_found.fragment.FoundFragment;
import com.cocos.module_mine.mine_fragment.MineFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @author ningkang.guo
 * @Date 2019/1/29
 */
@Route(path = RouterActivityPath.ACTIVITY_MAIN_PATH)
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {

    private long mExitTime;
    private BottomSheetDialog bottomSheetDialog;
    BaseInvokeModel baseInvokeModel;
    private boolean isFirst = true;
    private long runIntent;
    private String TAG = "拉起加载数据===";

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        requested = false;
        //初始化Fragment
        initFragment();
        binding.assetRb.setOnClickListener(this);
        binding.foundRb.setOnClickListener(this);
        binding.mineRb.setOnClickListener(this);
        initIntent(getIntent(),false);
    }

    private void initFragment() {
        ArrayList mFragments = new ArrayList<>();
        mFragments.add(new AssetFragment());
        mFragments.add(new FoundFragment());
        mFragments.add(new MineFragment());
        MainViewPagerAdapter mainVPAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragments);
        binding.vpMain.setAdapter(mainVPAdapter);
        binding.vpMain.setOffscreenPageLimit(mFragments.size());
        StatusBarUtils.with(MainActivity.this).init();
        VersionUtil.updateVersion(viewModel, MainActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asset_rb:
                StatusBarUtils.with(MainActivity.this).init();
                binding.vpMain.setCurrentItem(0, false);
                break;
            case R.id.found_rb:
                initWidow();
                binding.vpMain.setCurrentItem(1, false);
                break;
            case R.id.mine_rb:
                initWidow();
                binding.vpMain.setCurrentItem(2, false);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.system_exit), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityContainer.finishAllActivity();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (hasInstallPermission && !TextUtils.isEmpty(VersionUtil.getDestFileDir(MainActivity.this))) {
                    VersionUtil.installApkO(MainActivity.this, VersionUtil.getDestFileDir(MainActivity.this));
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        runIntent = System.currentTimeMillis();
        setIntent(intent);
        initIntent(intent,true);


    }

    private boolean requested = false;

    //数据请求成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        requested = true;
        boolean loadComplete = SPUtils.getBoolean(this, "loadComplete", false);
        if (loadComplete) {
            SPUtils.putBoolean(this, "loadComplete", false);
            parseInvokeIntent();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
    }

    private void initIntent(Intent intent,boolean isNewIntent) {
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        baseInvokeModel = new BaseInvokeModel();
        if (bundle != null) {
            baseInvokeModel.setPackageName(bundle.getString("packageName"));
            baseInvokeModel.setClassName(bundle.getString("className"));
            baseInvokeModel.setAppName(bundle.getString("appName"));
            baseInvokeModel.setAction(bundle.getString("action"));
        } else {
            bundle = new Bundle();
        }
        Uri uri = intent.getData();
        if (uri != null) {
            baseInvokeModel.setParam(uri.getQueryParameter("param"));
        }
        LogUtils.d("拉起==数据",baseInvokeModel.toString());
//        this.baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_SENDER_INFO);
        boolean loadComplete = SPUtils.getBoolean(this, "loadComplete", false);
        if (isNewIntent) {
            SPUtils.putBoolean(this, "loadComplete", false);
            parseInvokeIntent();
        }


    }

    private void parseInvokeIntent() {
        try {
            Intent intent = getIntent();

            if ((null != baseInvokeModel && baseInvokeModel.getAppName() != null) && requested) {
                String param = baseInvokeModel.getParam();
                Bundle bundle1 = new Bundle();
                switch (baseInvokeModel.getAction()) {
                    case "login":
                        BaseInvokeModel finalBaseInvokeModel = baseInvokeModel;
                        CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onReceiveValue(String s) {
                                MainHandler.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("loginresult", s);
                                        AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                                        if (accountNamesEntity.isSuccess()) {
                                            Authorize authorize = GsonSingleInstance.getGsonInstance().fromJson(param, Authorize.class);
                                            bundle1.putSerializable(IntentKeyGlobal.INVOKE_AUTHORIZE_INFO, authorize);
                                            bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, finalBaseInvokeModel);
                                            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_LOGIN).with(bundle1).navigation();
                                        } else {
                                            ToastUtils.showShort(R.string.account_empty);
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case "transfer":
                        Transfer transfer = GsonSingleInstance.getGsonInstance().fromJson(param, Transfer.class);
                        BaseInvokeModel finalBaseInvokeModel2 = baseInvokeModel;
                        CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onReceiveValue(String s) {
                                MainHandler.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("transferresult", s);
                                        AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                                        if (accountNamesEntity.isSuccess()) {
                                            if (null != transfer.getFrom() && accountNamesEntity.data.contains(transfer.getFrom())) {
                                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_TRANSFER_INFO, transfer);
                                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, finalBaseInvokeModel2);
                                                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_TRANSFER).with(bundle1).navigation();
                                            } else {
                                                showAccountNotExistDialog(baseInvokeModel, transfer);
                                            }
                                        } else {
                                            ToastUtils.showShort(R.string.account_empty);
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case "callContract":

                        Contract contract = GsonSingleInstance.getGsonInstance().fromJson(param, Contract.class);
                        BaseInvokeModel finalBaseInvokeModel1 = baseInvokeModel;
                        CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onReceiveValue(String s) {
                                MainHandler.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("callContractresult", s);
                                        AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                                        if (accountNamesEntity.isSuccess()) {
                                            if (null != contract.getAuthorizedAccount() && accountNamesEntity.data.contains(contract.getAuthorizedAccount())) {
                                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_CONTRACT_INFO, contract);
                                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, finalBaseInvokeModel1);
                                                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_CONTRACT).with(bundle1).navigation();
                                            } else {
                                                showAccountNotExistDialog(baseInvokeModel, contract);
                                            }
                                        } else {
                                            ToastUtils.showShort(R.string.account_empty);
                                        }
                                    }
                                });
                            }
                        });
                        break;

                    case "signmessage":
                        SignMessage signMessage = GsonSingleInstance.getGsonInstance().fromJson(param, SignMessage.class);
                        BaseInvokeModel baseInvokeModel1 = baseInvokeModel;
                        String accountName = AccountHelperUtils.getCurrentAccountName();
                        MainHandler.getInstance().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(accountName)) {
                                    ToastUtils.showShort(R.string.account_empty);
                                    return;
                                }
                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_SIGNMESSAGE, signMessage);
                                bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, baseInvokeModel1);
                                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_SIGNMESSAGE).with(bundle1).navigation();
                            }
                        }, 1000);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + baseInvokeModel.getAction());
                }
            }
            baseInvokeModel = null;
        } catch (Exception e) {
            Log.i("parseInvokeIntent-e", e.getMessage());
        }
    }

    private void showAccountNotExistDialog(BaseInvokeModel baseInvokeModel, BaseInfo baseInfo) {
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
         DialogAuthorAccountNotExistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_author_account_not_exist, null, false);
        bottomSheetDialog.setContentView(binding.getRoot());
        // 设置dialog 完全显示
        View parent = (View) binding.getRoot().getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        binding.getRoot().measure(0, 0);
        behavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        final ConfrimDialogViewModel confrimDialogViewModel = new ConfrimDialogViewModel(getApplication());
        confrimDialogViewModel.setBaseInfo(baseInvokeModel, baseInfo);
        binding.setViewModel(confrimDialogViewModel);
        bottomSheetDialog.show();
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null == busCarrier) {
            return;
        }
        if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.DIALOG_DISMISS_TYPE)) {
            if (null != bottomSheetDialog) {
                bottomSheetDialog.dismiss();
            }
        }
    }
}
