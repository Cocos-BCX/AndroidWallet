package com.cocos.bcx_wallet.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_wallet.BR;
import com.cocos.bcx_wallet.R;
import com.cocos.bcx_wallet.adapter.MainViewPagerAdapter;
import com.cocos.bcx_wallet.databinding.ActivityMainBinding;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.AccountNamesEntity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.Authorize;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.Contract;
import com.cocos.library_base.invokedpages.model.Transfer;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ActivityContainer;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.VersionUtil;
import com.cocos.library_base.utils.node.NodeConnectUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.module_asset.ui.asset.AssetFragment;
import com.cocos.module_found.fragment.FoundFragment;
import com.cocos.module_mine.mine_fragment.MineFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/1/29
 */
@Route(path = RouterActivityPath.ACTIVITY_MAIN_PATH)
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {

    private long mExitTime;

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
        //初始化Fragment
        initFragment();
        binding.assetRb.setOnClickListener(this);
        binding.foundRb.setOnClickListener(this);
        binding.mineRb.setOnClickListener(this);
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
    protected void onResume() {
        super.onResume();
        NodeConnectUtil.testNetStatus();
        parseInvokeIntent();
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
        setIntent(intent);

    }

    private void parseInvokeIntent() {
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            BaseInvokeModel baseInvokeModel = null;
            if (bundle != null) {
                baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_SENDER_INFO);
            }
            if (null != baseInvokeModel) {
                String param = baseInvokeModel.getParam();
                Bundle bundle1 = new Bundle();
                switch (baseInvokeModel.getAction()) {
                    case "login":
                        BaseInvokeModel finalBaseInvokeModel = baseInvokeModel;
                        CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onReceiveValue(String s) {
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
                        break;
                    case "transfer":
                        Transfer transfer = GsonSingleInstance.getGsonInstance().fromJson(param, Transfer.class);
                        BaseInvokeModel finalBaseInvokeModel2 = baseInvokeModel;
                        CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onReceiveValue(String s) {
                                Log.i("transferresult", s);
                                AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                                if (accountNamesEntity.isSuccess()) {
                                    if (null != transfer.getFrom() && accountNamesEntity.data.contains(transfer.getFrom())) {
                                        bundle1.putSerializable(IntentKeyGlobal.INVOKE_TRANSFER_INFO, transfer);
                                        bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, finalBaseInvokeModel2);
                                        ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_TRANSFER).with(bundle1).navigation();
                                    } else {
                                        ToastUtils.showShort(R.string.author_account_not_exist);
                                    }
                                } else {
                                    ToastUtils.showShort(R.string.account_empty);
                                }
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
                                Log.i("callContractresult", s);
                                if (null == contract.getAuthorizedAccount()) {
                                    Log.i("getAuthorizedAccount", "getAuthorizedAccount is null");
                                }
                                AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                                if (accountNamesEntity.isSuccess()) {
                                    if (null != contract.getAuthorizedAccount() && accountNamesEntity.data.contains(contract.getAuthorizedAccount())) {
                                        bundle1.putSerializable(IntentKeyGlobal.INVOKE_CONTRACT_INFO, contract);
                                        bundle1.putSerializable(IntentKeyGlobal.INVOKE_BASE_INFO, finalBaseInvokeModel1);
                                        ARouter.getInstance().build(RouterActivityPath.ACTIVITY_INVOKE_CONTRACT).with(bundle1).navigation();
                                    } else {
                                        ToastUtils.showShort(R.string.author_account_not_exist);
                                    }
                                } else {
                                    ToastUtils.showShort(R.string.account_empty);
                                }
                            }
                        });
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + baseInvokeModel.getAction());
                }
                intent.removeExtra(IntentKeyGlobal.INVOKE_SENDER_INFO);
            }
        } catch (Exception e) {
            Log.i("parseInvokeIntent-e", e.getMessage());
        }
    }
}
