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
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.R;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.databinding.ActivityJsWebviewBindingImpl;
import com.cocos.library_base.entity.AccountNhAssetModel;
import com.cocos.library_base.entity.AccountNhAssetOrderModel;
import com.cocos.library_base.entity.BaseResultModel;
import com.cocos.library_base.entity.FeeModel;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.entity.js_params.AccountNHAssetsModel;
import com.cocos.library_base.entity.js_params.AccountNHOrdersParamsModel;
import com.cocos.library_base.entity.js_params.AccountNameModel;
import com.cocos.library_base.entity.js_params.BalanceInfoModel;
import com.cocos.library_base.entity.js_params.GetAccountInfoModel;
import com.cocos.library_base.entity.js_params.JsParamsEventModel;
import com.cocos.library_base.entity.js_params.NHAssetOrdersParamsModel;
import com.cocos.library_base.entity.js_params.TransactionFeeModel;
import com.cocos.library_base.entity.js_params.TransactionModel;
import com.cocos.library_base.entity.js_response.AccountNhAssetModels;
import com.cocos.library_base.entity.js_response.AccountNhAssetOrderModels;
import com.cocos.library_base.entity.js_response.FillNHAssetOrderParamModel;
import com.cocos.library_base.entity.js_response.JsBalanceParamsModel;
import com.cocos.library_base.entity.js_response.JsContractParamsModel;
import com.cocos.library_base.entity.js_response.MemoModel;
import com.cocos.library_base.entity.js_response.TransferNHAssetParamModel;
import com.cocos.library_base.entity.js_response.TransferParamModel;
import com.cocos.library_base.global.GlobalConstants;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.JSTools;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.library_base.widget.BaseVerifyPasswordDialog;

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
        initLangeuage();
    }

    private void initLangeuage() {
        LocalManageUtil.saveSelectLanguage(this, SPUtil.getInstance(this).getSelectLanguage());
    }


    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            webViewModel = (WebViewModel) bundle.getSerializable(IntentKeyGlobal.WEB_MODEL);
        } catch (Exception e) {
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
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
        if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.TRANSFERASSET)) {
            /**
             *  transferAsset
             */
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    TransferParamModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, TransferParamModel.class);
                    // only get transfer fee
                    if (assetModel.onlyGetFee) {
                        CocosBcxApiWrapper.getBcxInstance().transfer_calculate_fee(password, assetModel.fromAccount, assetModel.toAccount, assetModel.amount, assetModel.assetId, assetModel.feeAssetId, assetModel.memo, s -> {
                            FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                            setTransactionFeeCallBack(baseResult, params);
                        });
                        return;
                    }
                    // transfer asset
                    CocosBcxApiWrapper.getBcxInstance().transfer(password, assetModel.fromAccount, assetModel.toAccount, assetModel.amount, assetModel.assetId, assetModel.feeAssetId, assetModel.memo, s -> {
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                    });
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.CALLCONTRACTFUNCTION)) {

            /**
             *  callContractFunction
             */
            JsContractParamsModel jsContractParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, JsContractParamsModel.class);
            StringBuffer str5 = new StringBuffer();
            for (String s : jsContractParamsModel.valueList) {
                str5.append(s);
                str5.append(",");
            }
            if (jsContractParamsModel.onlyGetFee) {
                CocosBcxApiWrapper.getBcxInstance().calculate_invoking_contract_fee(AccountHelperUtils.getCurrentAccountName(), "COCOS", jsContractParamsModel.nameOrId, jsContractParamsModel.functionName, str5.toString(), s -> {
                    FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                    setTransactionFeeCallBack(baseResult, params);
                });
                return;
            }

            /**
             *  invoking contract method
             */
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    CocosBcxApiWrapper.getBcxInstance().invoking_contract(AccountHelperUtils.getCurrentAccountName(), password, "COCOS", jsContractParamsModel.nameOrId, jsContractParamsModel.functionName, str5.toString(), new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(String s) {
                            BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                            if (baseResult.isSuccess()) {
                                onJSCallback(params.serialNumber, baseResult.getData());
                            } else {
                                onJSCallback(params.serialNumber, s);
                            }
                        }
                    });
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.QUERYACCOUNTINFO)) {
            /**
             * queryAccountInfo
             */
            AccountNameModel accountNameModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, AccountNameModel.class);
            CocosBcxApiWrapper.getBcxInstance().get_full_accounts(accountNameModel.account, false, new IBcxCallBack() {
                @Override
                public void onReceiveValue(String s) {
                    onJSCallback(params.serialNumber, s);
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
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.QUERYNHASSETS)) {

            /**
             * query nh asset detail
             */
            LogUtils.d("lookup_nh_asset", params.param);
            TransferNHAssetParamModel transferNHAssetParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, TransferNHAssetParamModel.class);
            CocosBcxApiWrapper.getBcxInstance().lookup_nh_asset(transferNHAssetParamModel.NHAssetIds, new IBcxCallBack() {
                @Override
                public void onReceiveValue(String s) {
                    onJSCallback(params.serialNumber, s);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.QUERYACCOUNTNHASSETORDERS)) {

            /**
             * list_account_nh_asset_order
             */
            AccountNHOrdersParamsModel accountNHOrdersParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, AccountNHOrdersParamsModel.class);
            CocosBcxApiWrapper.getBcxInstance().list_account_nh_asset_order(accountNHOrdersParamsModel.account, accountNHOrdersParamsModel.pageSize, accountNHOrdersParamsModel.page, s -> {
                BaseResultModel<List<AccountNhAssetOrderModel>> listBaseResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                AccountNhAssetOrderModels accountNhAssetOrderModels = new AccountNhAssetOrderModels();
                if (listBaseResultModel.isSuccess()) {
                    accountNhAssetOrderModels.data = listBaseResultModel.getData();
                    accountNhAssetOrderModels.total = listBaseResultModel.getData().size();
                }
                accountNhAssetOrderModels.code = listBaseResultModel.getCode();
                accountNhAssetOrderModels.message = listBaseResultModel.getMessage();
                onJSCallback(params.serialNumber, accountNhAssetOrderModels);
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.QUERYNHASSETORDERS)) {

            /**
             * list_nh_asset_order
             */
            NHAssetOrdersParamsModel ordersParamsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, NHAssetOrdersParamsModel.class);
            CocosBcxApiWrapper.getBcxInstance().list_nh_asset_order(ordersParamsModel.assetIds, ordersParamsModel.worldViews, ordersParamsModel.baseDescribe, ordersParamsModel.pageSize, ordersParamsModel.page, s -> {
                BaseResultModel<List<AccountNhAssetOrderModel>> listBaseResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                AccountNhAssetOrderModels accountNhAssetOrderModels = new AccountNhAssetOrderModels();
                if (listBaseResultModel.isSuccess()) {
                    accountNhAssetOrderModels.data = listBaseResultModel.getData();
                    accountNhAssetOrderModels.total = listBaseResultModel.getData().size();
                }
                accountNhAssetOrderModels.code = listBaseResultModel.getCode();
                accountNhAssetOrderModels.message = listBaseResultModel.getMessage();
                onJSCallback(params.serialNumber, accountNhAssetOrderModels);
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.QUERYACCOUNTNHASSETS)) {

            /**
             * list_account_nh_asset
             */
            LogUtils.d("list_account_nh_asset", params.param);

            AccountNHAssetsModel accountNHAssetsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, AccountNHAssetsModel.class);
            CocosBcxApiWrapper.getBcxInstance().list_account_nh_asset(accountNHAssetsModel.account, accountNHAssetsModel.worldViews, accountNHAssetsModel.page, accountNHAssetsModel.pageSize, s -> {
                BaseResultModel<List<AccountNhAssetModel>> listBaseResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                AccountNhAssetModels accountNhAssetOrderModels = new AccountNhAssetModels();
                if (listBaseResultModel.isSuccess()) {
                    accountNhAssetOrderModels.data = listBaseResultModel.getData();
                    accountNhAssetOrderModels.total = listBaseResultModel.getData().size();
                }
                accountNhAssetOrderModels.code = listBaseResultModel.getCode();
                accountNhAssetOrderModels.message = listBaseResultModel.getMessage();
                onJSCallback(params.serialNumber, accountNhAssetOrderModels);
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.DECODEMEMO)) {
            /**
             * decodeMemo
             */
            final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
            passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
            passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
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
                    });
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
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    CocosBcxApiWrapper.getBcxInstance().transfer_nh_asset(password, AccountHelperUtils.getCurrentAccountName(), transferNHAssetParamModel.toAccount, "COCOS", transferNHAssetParamModel.NHAssetIds.get(0), s -> {
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                    });
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.FILLNHASSETORDER)) {

            /**
             * buy nh asset fee
             */
            FillNHAssetOrderParamModel fillNHAssetOrderParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, FillNHAssetOrderParamModel.class);
            if (fillNHAssetOrderParamModel.onlyGetFee) {
                CocosBcxApiWrapper.getBcxInstance().buy_nh_asset_fee(AccountHelperUtils.getCurrentAccountName(), fillNHAssetOrderParamModel.orderId, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                        setTransactionFeeCallBack(baseResult, params);
                    }
                });
                return;
            }
            // buy nh asset
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    CocosBcxApiWrapper.getBcxInstance().buy_nh_asset(password, AccountHelperUtils.getCurrentAccountName(), fillNHAssetOrderParamModel.orderId, s -> {
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                    });
                }

                @Override
                public void cancel() {
                    onCancel(params);
                }
            });

        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.GETACCOUNTBALANCES)) {

            /**
             *    get account balance
             */
            JsBalanceParamsModel paramsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, JsBalanceParamsModel.class);
            CocosBcxApiWrapper.getBcxInstance().get_account_balances(paramsModel.account, paramsModel.assetId, s -> {
                FeeModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                BalanceInfoModel balanceInfoModel = new BalanceInfoModel();
                BalanceInfoModel.DataInfo dataInfo = new BalanceInfoModel.DataInfo();
                if (feeModel.isSuccess()) {
                    dataInfo.COCOS = feeModel.data.amount;
                }
                balanceInfoModel.code = feeModel.getCode();
                balanceInfoModel.message = feeModel.getMessage();
                balanceInfoModel.data = dataInfo;
                onJSCallback(params.serialNumber, balanceInfoModel);
            });
        }

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
            dataBean.fee_symbol = CocosBcxApiWrapper.getBcxInstance().get_asset_object(baseResult.data.asset_id).symbol;
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
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    sb.insert(0, "javascript:");
                    binding.jsWebView.loadUrl(sb.toString().replace("\\", "\\\\"));
                    Log.i("javascript", sb.toString().replace("\\", "\\\\"));
                } else {
                    binding.jsWebView.evaluateJavascript(sb.toString().replace("\\", "\\\\"), null);
                    Log.i("javascript", sb.toString().replace("\\", "\\\\"));
                }
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
        binding.jsWebView.setWebChromeClient(null);
        binding.jsWebView.setWebViewClient(null);
        binding.jsWebView.getSettings().setJavaScriptEnabled(false);
        binding.jsWebView.destroy();
        super.onDestroy();
    }

}
