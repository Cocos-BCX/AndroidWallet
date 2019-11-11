package com.cocos.library_base.base;

import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.library_base.R;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.databinding.ActivityJsWebviewBindingImpl;
import com.cocos.library_base.entity.BaseResultModel;
import com.cocos.library_base.entity.FeeModel;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.entity.js_params.GetAccountInfoModel;
import com.cocos.library_base.entity.js_params.JsParamsEventModel;
import com.cocos.library_base.entity.js_params.TransactionFeeModel;
import com.cocos.library_base.entity.js_params.TransactionModel;
import com.cocos.library_base.entity.js_response.CancelNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.ClaimVestingBalanceParamModle;
import com.cocos.library_base.entity.js_response.CreateNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.DeleteNHAssetParamsModel;
import com.cocos.library_base.entity.js_response.FillNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.JsContractParamsModel;
import com.cocos.library_base.entity.js_response.MemoModel;
import com.cocos.library_base.entity.js_response.PublishVotesParamsModel;
import com.cocos.library_base.entity.js_response.TransferNHAssetParamModel;
import com.cocos.library_base.entity.js_response.TransferParamModel;
import com.cocos.library_base.entity.js_response.UpdateGasParamsModel;
import com.cocos.library_base.global.GlobalConstants;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.JSTools;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.library_base.widget.JsWebVerifyPasswordDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.tatarka.bindingcollectionadapter2.BR;


/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */

@Route(path = RouterActivityPath.ACTIVITY_JS_WEB)
public class JsWebViewActivity extends BaseActivity<ActivityJsWebviewBindingImpl, JsWebViewViewModel> {

    /**
     * JS注入成功标识，该JS文件只需要注入一次，true表示注入成功，false表示未注入
     */
    private boolean mInjection = false;


    private WebViewModel webViewModel;

    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_js_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage();
    }

    private void initLanguage() {
        LocalManageUtil.setApplicationLanguage(this);
    }


    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            webViewModel = (WebViewModel) bundle.getSerializable(IntentKeyGlobal.WEB_MODEL);
        } catch (Exception e) {
        }
    }


    @SuppressLint({"SetJavaScriptEnabled", "CheckResult"})
    @Override
    public void initData() {
        viewModel.setWebData(webViewModel);
        if (!TextUtils.isEmpty(webViewModel.getUrl())) {
            binding.jsWebView.loadUrl(webViewModel.getUrl());
        }

        binding.jsWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mInjection) {
                    mInjection = false;
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (mInjection) {
                    return;
                }
                if (url.endsWith(".js") || url.endsWith(".css")) {
                    onCocosJsLocal();
                    mInjection = true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(viewModel.webTitle.get())) {
                    viewModel.webTitle.set(title);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (binding.jsWebView.canGoBack()) {
                binding.jsWebView.goBack();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("ObsoleteSdkInt")
    public void onCocosJsLocal() {
        StringBuilder builder = new StringBuilder(Objects.requireNonNull(JSTools.getJS(this, "cocos.js")));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            binding.jsWebView.loadUrl("javascript:" + builder.toString());
        } else {
            binding.jsWebView.evaluateJavascript(builder.toString(), null);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null == busCarrier) {
            return;
        }
        JsParamsEventModel params = (JsParamsEventModel) busCarrier.getObject();
        String password = SPUtils.getString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT);

        if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.TRANSFERASSET)) {
            /**
             *  transferAsset
             */
            if (!TextUtils.isEmpty(password)) {
                transferAssets(password, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    transferAssets(password, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.CALLCONTRACTFUNCTION)) {

            Log.i("callContractFunction--param", params.param);
            /**
             *  callContractFunction
             */
            JsContractParamsModel jsContractParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, JsContractParamsModel.class);
            StringBuffer str5 = new StringBuffer();
            for (String s : jsContractParamsModel.valueList) {
                str5.append(s);
                str5.append(",");
            }

            /**
             *  invoking contract method
             */
            if (!TextUtils.isEmpty(password)) {
                Log.i("callContractFunction--valueList", String.valueOf(str5.toString()));
                invoking_contract(password, jsContractParamsModel, str5, params);
                return;
            }

            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    Log.i("callContractFunction--valueList", String.valueOf(str5.toString()));
                    invoking_contract(password, jsContractParamsModel, str5, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.GETACCOUNTINFO)) {
            /**
             *  get current account info
             */
            GetAccountInfoModel jsAccountInfoModel = new GetAccountInfoModel();
            jsAccountInfoModel.account_name = AccountHelperUtils.getCurrentAccountName();
            jsAccountInfoModel.account_id = AccountHelperUtils.getCurrentAccountId();
            onJSCallback(params.serialNumber, jsAccountInfoModel);
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.DECODEMEMO)) {
            /**
             * decodeMemo
             */
            if (!TextUtils.isEmpty(password)) {
                decryptMemo(password, params);
                return;
            }

            final JsWebVerifyPasswordDialog passwordVerifyDialog = new JsWebVerifyPasswordDialog();
            passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
            passwordVerifyDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    decryptMemo(password, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.TRANSFERNHASSET)) {
            /**
             * transfer nh asset
             */
            TransferNHAssetParamModel transferNHAssetParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, TransferNHAssetParamModel.class);

            if (!TextUtils.isEmpty(password)) {
                transferNhAssets(password, transferNHAssetParamModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    transferNhAssets(password, transferNHAssetParamModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.INITCONNECT)) {
            NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
            onJSCallback(params.serialNumber, selectedNodeModel);
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.DELETENHASSET)) {
            Log.i(GlobalConstants.DELETENHASSET, params.param);
            /**
             * delete nh asset
             */
            DeleteNHAssetParamsModel transferNHAssetParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, DeleteNHAssetParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                deleteNhAssets(password, transferNHAssetParamModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    deleteNhAssets(password, transferNHAssetParamModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.CREATNHASSETORDER)) {
            Log.i(GlobalConstants.CREATNHASSETORDER, params.param);

            /**
             * create nh asset order
             */
            CreateNHAssetOrderParamsModel createNHAssetOrderParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, CreateNHAssetOrderParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                createNhAssetsOrder(password, createNHAssetOrderParamsModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    createNhAssetsOrder(password, createNHAssetOrderParamsModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.FILLNHASSETORDER)) {
            Log.i(GlobalConstants.FILLNHASSETORDER, params.param);

            /**
             * fill nh asset order
             */
            FillNHAssetOrderParamsModel fillNHAssetOrderParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, FillNHAssetOrderParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                fillNhAssetsOrder(password, fillNHAssetOrderParamsModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    fillNhAssetsOrder(password, fillNHAssetOrderParamsModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.CANCELNHASSETORDER)) {
            Log.i(GlobalConstants.CANCELNHASSETORDER, params.param);
            /**
             * cancel nh asset order
             */
            CancelNHAssetOrderParamsModel cancelNHAssetOrderParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, CancelNHAssetOrderParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                cancelNhAssetsOrder(password, cancelNHAssetOrderParamsModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    cancelNhAssetsOrder(password, cancelNHAssetOrderParamsModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.UPDATE_COLLATERAL_FOR_GAS)) {
            Log.i(GlobalConstants.UPDATE_COLLATERAL_FOR_GAS, params.param);

            /**
             * updateCollateralForGas
             */
            UpdateGasParamsModel updateGasParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, UpdateGasParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                updateGas(password, updateGasParamsModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    updateGas(password, updateGasParamsModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.CLAIM_VESTING_BALANCE)) {
            Log.i(GlobalConstants.CLAIM_VESTING_BALANCE, params.param);
            /**
             * claimVestingBalance
             */
            ClaimVestingBalanceParamModle claimVestingBalancePrams = GsonSingleInstance.getGsonInstance().fromJson(params.param, ClaimVestingBalanceParamModle.class);

            if (!TextUtils.isEmpty(password)) {
                claimVestingBalance(claimVestingBalancePrams.id, password, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    claimVestingBalance(claimVestingBalancePrams.id, password, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.PUBLISH_VOTES)) {
            Log.i(GlobalConstants.PUBLISH_VOTES, params.param);

            /**
             * publishVotes
             */
            PublishVotesParamsModel publishVotesParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, PublishVotesParamsModel.class);
            if (!TextUtils.isEmpty(password)) {
                publishVotes(password, publishVotesParamsModel, params);
                return;
            }
            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    publishVotes(password, publishVotesParamsModel, params);
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        }
    }

    private void publishVotes(String password, PublishVotesParamsModel publishVotes, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().vote_members(AccountHelperUtils.getCurrentAccountName(), password, publishVotes.type, publishVotes.vote_ids, String.valueOf(publishVotes.votes),
                new IBcxCallBack() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("vote_members", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }

    /**
     * 领取GAS或节点出块奖励
     *
     * @param password
     * @param params
     */
    private void claimVestingBalance(String awardId, String password, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().receive_vesting_balances(AccountHelperUtils.getCurrentAccountName(), awardId, password,
                new IBcxCallBack() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("claimVestingBalance", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }


    /**
     * 抵押GAS
     *
     * @param password
     * @param updateGasParamsModel
     * @param params
     */
    private void updateGas(String password, UpdateGasParamsModel updateGasParamsModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().update_collateral_for_gas(updateGasParamsModel.mortgager, password, updateGasParamsModel.beneficiary, String.valueOf(updateGasParamsModel.amount),
                new IBcxCallBack() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("update_collateral_for_gas", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }

    /**
     * 取消NH 资产订单
     *
     * @param password
     * @param cancelNHAssetOrderParamsModel
     * @param params
     */
    private void cancelNhAssetsOrder(String password, CancelNHAssetOrderParamsModel cancelNHAssetOrderParamsModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().cancel_nh_asset_order(AccountHelperUtils.getCurrentAccountName(), password, cancelNHAssetOrderParamsModel.orderId,
                new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("cancel_nh_asset_order", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }


    /**
     * 购买NH 资产
     *
     * @param password
     * @param fillNHAssetOrderParamsModel
     * @param params
     */
    private void fillNhAssetsOrder(String password, FillNHAssetOrderParamsModel fillNHAssetOrderParamsModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().buy_nh_asset(AccountHelperUtils.getCurrentAccountName(), password, fillNHAssetOrderParamsModel.orderId,
                new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("buy_nh_asset", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }


    /**
     * 创建NH 资产订单
     *
     * @param password
     * @param createNHAssetOrderParamsModel
     * @param params
     */
    private void createNhAssetsOrder(String password, CreateNHAssetOrderParamsModel createNHAssetOrderParamsModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().create_nh_asset_order("syling", AccountHelperUtils.getCurrentAccountName(),
                password, createNHAssetOrderParamsModel.NHAssetId, "0", "COCOS", createNHAssetOrderParamsModel.memo,
                String.valueOf(createNHAssetOrderParamsModel.price), createNHAssetOrderParamsModel.priceAssetId, createNHAssetOrderParamsModel.expiration,
                new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("create_nh_asset_order", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                        }
                    }
                });
    }

    /**
     * 删除NH 资产
     *
     * @param password
     * @param deleteNHAssetParamModel
     * @param params
     */
    private void deleteNhAssets(String password, DeleteNHAssetParamsModel deleteNHAssetParamModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().delete_nh_asset(AccountHelperUtils.getCurrentAccountName(), password, deleteNHAssetParamModel.NHAssetIds, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String s) {
                BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                setTransactionCallBack(baseResult, params);
                if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                    SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                }
            }
        });
    }

    /**
     * 转移NH 资产
     *
     * @param password
     * @param transferNHAssetParamModel
     * @param params
     */
    private void transferNhAssets(String password, TransferNHAssetParamModel transferNHAssetParamModel, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().transfer_nh_asset(password, AccountHelperUtils.getCurrentAccountName(), transferNHAssetParamModel.toAccount, transferNHAssetParamModel.NHAssetIds, s -> {
            BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
            setTransactionCallBack(baseResult, params);
            if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
            }
        });
    }

    /**
     * memo解密
     *
     * @param password
     * @param params
     */
    private void decryptMemo(String password, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().decrypt_memo_message(AccountHelperUtils.getCurrentAccountName(), password, params.param, memo -> {
            BaseResultModel<String> resultModel = GsonSingleInstance.getGsonInstance().fromJson(memo, BaseResultModel.class);
            MemoModel memoModel = new MemoModel();
            if (resultModel.isSuccess()) {
                MemoModel.DataBean dataBean = new MemoModel.DataBean();
                dataBean.text = resultModel.getData();
                memoModel.data = dataBean;
            }
            memoModel.code = resultModel.getCode();
            memoModel.message = resultModel.getMessage();
            onJSCallback(params.serialNumber, memoModel);
            if (resultModel.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
            }
        });
    }

    /**
     * 调合约
     *
     * @param password
     * @param jsContractParamsModel
     * @param str5
     * @param params
     */
    private void invoking_contract(String password, JsContractParamsModel jsContractParamsModel, StringBuffer str5, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().invoking_contract(AccountHelperUtils.getCurrentAccountName(), password, jsContractParamsModel.nameOrId, jsContractParamsModel.functionName, str5.toString(), new IBcxCallBack() {
            @Override
            public void onReceiveValue(String s) {
                BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                if (baseResult.isSuccess()) {
                    onJSCallback(params.serialNumber, baseResult.getData());
                } else {
                    onJSCallback(params.serialNumber, s);
                }
                if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                    SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
                }
            }
        });
    }

    /**
     * 转账
     *
     * @param password
     * @param params
     */
    private void transferAssets(String password, JsParamsEventModel params) {
        TransferParamModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, TransferParamModel.class);
        // transfer asset
        CocosBcxApiWrapper.getBcxInstance().transfer(password, assetModel.fromAccount, assetModel.toAccount, assetModel.amount, assetModel.assetId, assetModel.memo, s -> {
            BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
            setTransactionCallBack(baseResult, params);
            if (baseResult.isSuccess() && JsWebVerifyPasswordDialog.isChecked) {
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, password);
            }
        });
    }

    /**
     * 密码输入取消键返回值
     *
     * @param params
     */
    private void onCancel(JsParamsEventModel params) {
        onJSCallback(params.serialNumber, "{\"type\":\"signature_rejected\",\"message\":\"User rejected the signature request\",\"code\":402,\"isError\":true}");
    }


    /**
     * 拼接交易费用成功回调给dapp端的数据
     *
     * @param baseResult
     * @param params
     */
    private void setTransactionFeeCallBack(FeeModel baseResult, JsParamsEventModel params) {
        TransactionFeeModel transactionFeeModel = new TransactionFeeModel();
        if (baseResult.isSuccess()) {
            TransactionFeeModel.DataBean dataBean = new TransactionFeeModel.DataBean();
            dataBean.fee_amount = Double.valueOf(baseResult.data.amount);
            asset_object asset_object = CocosBcxApiWrapper.getBcxInstance().get_asset_object(baseResult.data.asset_id);
            if (null != asset_object) {
                dataBean.fee_symbol = asset_object.symbol;
            }
            transactionFeeModel.data = dataBean;
        }
        transactionFeeModel.code = baseResult.getCode();
        transactionFeeModel.message = baseResult.getMessage();
        onJSCallback(params.serialNumber, transactionFeeModel);
    }

    /**
     * 拼接交易成功回调给dapp端的数据
     *
     * @param baseResult
     * @param params
     */
    private void setTransactionCallBack(BaseResultModel<String> baseResult, JsParamsEventModel params) {
        TransactionModel transactionModel = new TransactionModel();
        if (baseResult.isSuccess()) {
            TransactionModel.TrxDataBean trx_data = new TransactionModel.TrxDataBean();
            List<TransactionModel.DataBean> dataBeans = new ArrayList<>();
            TransactionModel.DataBean dataBean = new TransactionModel.DataBean();
            dataBeans.add(dataBean);
            trx_data.trx_id = baseResult.getData();
            transactionModel.trx_data = trx_data;
            transactionModel.data = dataBeans;
        }
        transactionModel.code = baseResult.getCode();
        transactionModel.message = baseResult.getMessage();
        onJSCallback(params.serialNumber, transactionModel);
    }

    /**
     * 执行JS回调，用于关闭web支付对话框
     *
     * @param serialNumber 参数为服务端返回的交易hash
     */
    public void onJSCallback(String serialNumber, Object data) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder("callbackResult(\'" + serialNumber + "\',\'");
                sb.append(data instanceof String ? data : GsonSingleInstance.getGsonInstance().toJson(data));
                sb.append("\')");
                binding.jsWebView.evaluateJavascript(sb.toString().replace("\\", "\\\\"), null);
                Log.i("javascript", sb.toString().replace("\\", "\\\\"));
            }
        });
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.backObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.jsWebView.canGoBack()) {
                    binding.jsWebView.goBack();
                    return;
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 缓存只清一次
        boolean currentVersion = SPUtils.getBoolean(Utils.getContext(), SPKeyGlobal.WEB_CACHE_CLEAR, false);
        if (!currentVersion) {
            binding.jsWebView.clearCache(true);
            binding.jsWebView.clearFormData();
            SPUtils.putBoolean(Utils.getContext(), SPKeyGlobal.WEB_CACHE_CLEAR, true);
        }
        SPUtils.putString(Utils.getContext(), SPKeyGlobal.KEY_FOR_VERIFY_ACCOUNT, "");
        SPUtils.putBoolean(Utils.getContext(), SPKeyGlobal.SECRET_FREE_CHECK_STATUS, false);
        binding.jsWebView.destroy();
        super.onDestroy();
    }

}
