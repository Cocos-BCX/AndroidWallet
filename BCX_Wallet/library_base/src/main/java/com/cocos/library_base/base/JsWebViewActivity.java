package com.cocos.library_base.base;

import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
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
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.JSTools;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.library_base.widget.BaseVerifyPasswordDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData() {
        WebSettings settings = binding.jsWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Android");
        // 设置可以访问文件
        settings.setAllowContentAccess(true);
        settings.setBlockNetworkLoads(false);
        settings.setBuiltInZoomControls(false);
        settings.setDatabaseEnabled(true);
        settings.setDisplayZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            settings.setOffscreenPreRaster(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(false);
        }
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 支持多窗口
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 支持通过JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        // 将图片调整到适合WebView的大小
        settings.setUseWideViewPort(false);
        // 缩放至屏幕的大小
        settings.setLoadWithOverviewMode(false);
        // 支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        // 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
        // 使用缓存的策略
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
        binding.jsWebView.setWebContentsDebuggingEnabled(true);
        binding.jsWebView.addJavascriptInterface(new JavaScriptUtil(), "DappJsBridge");
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = settings.getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(binding.jsWebView.getSettings(), true);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
        viewModel.setWebData(webViewModel);
        if (!TextUtils.isEmpty(webViewModel.getUrl())) {
            binding.jsWebView.loadUrl(webViewModel.getUrl());
        }
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

    @Override
    protected void onDestroy() {
        binding.jsWebView.destroy();
        super.onDestroy();
    }

    @SuppressLint("ObsoleteSdkInt")
    public void onCocosJsLocal() {
        StringBuilder builder = new StringBuilder(JSTools.getJS(this, "cocos.js"));
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
                            Log.i("transfer_calculate_fee", s);
                            FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                            setTransactionFeeCallBack(baseResult, params, passwordDialog);
                        });
                        return;
                    }
                    // transfer asset
                    CocosBcxApiWrapper.getBcxInstance().transfer(password, assetModel.fromAccount, assetModel.toAccount, assetModel.amount, assetModel.assetId, assetModel.feeAssetId, assetModel.memo, s -> {
                        Log.i("transfer", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params, passwordDialog);
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
                    Log.i("calculate_invoking_contract_fee", s);
                    FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                    setTransactionFeeCallBack(baseResult, params, null);
                });
                return;
            }
            // invoking contract method
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    CocosBcxApiWrapper.getBcxInstance().invoking_contract(AccountHelperUtils.getCurrentAccountName(), password, "COCOS", jsContractParamsModel.nameOrId, jsContractParamsModel.functionName, str5.toString(), new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.i("invoking_contract", s);
                            BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                            //   setTransactionCallBack(baseResult, params, passwordDialog);
                            onJSCallback(params.serialNumber, baseResult.getData());
                            passwordDialog.dismiss();
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
                    Log.i("queryAccountInfo", s);
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
                    LogUtils.d("lookup_nh_asset", s);
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
                LogUtils.d("list_account_nh_asset", s);
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
                        LogUtils.d("decrypt_memo_message", params.param);
                        LogUtils.d("decrypt_memo_message", memo);
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
                        passwordVerifyDialog.dismiss();
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
            Log.i("transfer_nh_asset", params.param);
            TransferNHAssetParamModel transferNHAssetParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, TransferNHAssetParamModel.class);
            BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    CocosBcxApiWrapper.getBcxInstance().transfer_nh_asset(password, AccountHelperUtils.getCurrentAccountName(), transferNHAssetParamModel.toAccount, "COCOS", transferNHAssetParamModel.NHAssetIds.get(0), s -> {
                        Log.i("transfer_nh_asset", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params, passwordDialog);
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
                        Log.i("buy_nh_asset_fee", s);
                        FeeModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                        setTransactionFeeCallBack(baseResult, params, null);
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
                        Log.i("buy_nh_asset", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params, passwordDialog);
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
            Log.d("get_account_balances", params.param);
            JsBalanceParamsModel paramsModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, JsBalanceParamsModel.class);
            CocosBcxApiWrapper.getBcxInstance().get_account_balances(paramsModel.account, paramsModel.assetId, s -> {
                Log.d("get_account_balances---", s);
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
     * @param passwordDialog
     */
    private void setTransactionFeeCallBack(FeeModel baseResult, JsParamsEventModel params, BaseVerifyPasswordDialog passwordDialog) {
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
        if (null != passwordDialog) {
            passwordDialog.dismiss();
        }
    }

    /**
     * 拼接交易成功回调给dapp端的数据
     *
     * @param baseResult
     * @param params
     * @param passwordDialog
     */
    private void setTransactionCallBack(BaseResultModel<String> baseResult, JsParamsEventModel params, BaseVerifyPasswordDialog passwordDialog) {
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
        passwordDialog.dismiss();
    }

    /**
     * 执行JS回调，用于关闭web支付对话框
     *
     * @param serialNumber 参数为服务端返回的交易hash
     */
    public void onJSCallback(String serialNumber, Object data) {
        MainHandler.getInstance().post(new Runnable() {
            /**
             *
             */
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
}
