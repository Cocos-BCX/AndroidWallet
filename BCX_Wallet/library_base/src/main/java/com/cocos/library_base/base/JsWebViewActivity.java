package com.cocos.library_base.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.component.transfer.OrderConfirmViewModel;
import com.cocos.library_base.databinding.ActivityJsWebviewBindingImpl;
import com.cocos.library_base.databinding.DialogJswebviewMoreBinding;
import com.cocos.library_base.databinding.DialogTransferPayConfirmBinding;
import com.cocos.library_base.entity.BaseResultModel;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.entity.js_params.GetAccountInfoModel;
import com.cocos.library_base.entity.js_params.JsParamsEventModel;
import com.cocos.library_base.entity.js_params.TransactionModel;
import com.cocos.library_base.entity.js_response.CancelNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.ClaimVestingBalanceParamModle;
import com.cocos.library_base.entity.js_response.CreateNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.DeleteNHAssetParamsModel;
import com.cocos.library_base.entity.js_response.FillNHAssetOrderParamsModel;
import com.cocos.library_base.entity.js_response.JsContractParamsModel;
import com.cocos.library_base.entity.js_response.MemoModel;
import com.cocos.library_base.entity.js_response.PublishVotesParamsModel;
import com.cocos.library_base.entity.js_response.SelectLanguageModel;
import com.cocos.library_base.entity.js_response.TransferNHAssetParamModel;
import com.cocos.library_base.entity.js_response.TransferParamModel;
import com.cocos.library_base.entity.js_response.UpdateGasParamsModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.GlobalConstants;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.JSTools;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.library_base.utils.singleton.BottomDialogSingleInstance;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.library_base.viewmodel.JsWebMoreViewModel;
import com.cocos.library_base.widget.JsWebVerifyPasswordDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;


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

    private String solidPassworld;

    private WebViewModel webViewModel;
    private BottomSheetDialog bottomSheetDialog;

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(JsWebViewActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(JsWebViewActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(JsWebViewActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };


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

    @SuppressLint({"LongLogTag", "CheckResult"})
    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null == busCarrier) {
            return;
        }

        JsParamsEventModel params = (JsParamsEventModel) busCarrier.getObject();
        if (null == params) {
            if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.JSWEB_REFRESH_TYPE)) {
                if (null != binding.jsWebView) {
                    binding.jsWebView.reload();
                }
                if (null != bottomSheetDialog) {
                    bottomSheetDialog.dismiss();
                }
            } else if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.JSWEB_COPYURL_TYPE)) {
                ClipData mClipData = ClipData.newPlainText("Label", webViewModel.getUrl());
                ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
                ToastUtils.showShort(R.string.copy_success);
                if (null != bottomSheetDialog) {
                    bottomSheetDialog.dismiss();
                }
            } else if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.JSWEB_SHARE_TYPE)) {
                RxPermissions rxPermissions = new RxPermissions(JsWebViewActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            UMImage image = new UMImage(JsWebViewActivity.this, webViewModel.iconUrl);//网络图片
                            UMWeb web = new UMWeb(webViewModel.url);
                            web.setTitle(webViewModel.title);//标题
                            web.setThumb(image);//缩略图
                            web.setDescription(webViewModel.desc);//描述

                            new ShareAction(JsWebViewActivity.this)
                                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                    .withMedia(web)//分享内容
                                    .setCallback(shareListener)//回调监听器
                                    .share();
                            if (null != bottomSheetDialog) {
                                bottomSheetDialog.dismiss();
                            }
                        }
                    }
                });
            } else if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.JSWEB_BROWSER_TYPE)) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(webViewModel.getUrl());
                    intent.setData(content_url);
                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    startActivity(intent);
                    if (null != bottomSheetDialog) {
                        bottomSheetDialog.dismiss();
                    }
                } catch (Exception e) {

                }
            } else if (TextUtils.equals(busCarrier.getEventType(), EventTypeGlobal.DIALOG_DISMISS_TYPE)) {
                if (null != bottomSheetDialog) {
                    bottomSheetDialog.dismiss();
                }
            }

            return;
        }

        if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.TRANSFERASSET)) {
            /**
             *  transferAsset
             */
            if (!TextUtils.isEmpty(solidPassworld)) {
                transferAssets(solidPassworld, params);
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

            /**
             *  callContractFunction
             */
            JsContractParamsModel jsContractParamsModel = GsonSingleInstance.buildGson().fromJson(params.param, JsContractParamsModel.class);

            Log.i("callContractFunction--param", params.param);
            /**
             *  invoking contract method
             */
            if (!TextUtils.isEmpty(solidPassworld)) {
                invoking_contract(solidPassworld, jsContractParamsModel, jsContractParamsModel.valueList, params);
                return;
            }

            JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
            passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(String password) {
                    invoking_contract(password, jsContractParamsModel, jsContractParamsModel.valueList, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                decryptMemo(solidPassworld, params);
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

            if (!TextUtils.isEmpty(solidPassworld)) {
                transferNhAssets(solidPassworld, transferNHAssetParamModel, params);
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
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.WALLETLANGUAGE)) {
            int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
            SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
            selectLanguageModel.code = 1;
            selectLanguageModel.data = selectLanguage == 0 ? "cn" : "en";
            onJSCallback(params.serialNumber, selectLanguageModel);
        } else if (TextUtils.equals(busCarrier.getEventType(), GlobalConstants.DELETENHASSET)) {
            Log.i(GlobalConstants.DELETENHASSET, params.param);
            /**
             * delete nh asset
             */
            DeleteNHAssetParamsModel transferNHAssetParamModel = GsonSingleInstance.getGsonInstance().fromJson(params.param, DeleteNHAssetParamsModel.class);
            if (!TextUtils.isEmpty(solidPassworld)) {
                deleteNhAssets(solidPassworld, transferNHAssetParamModel, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                createNhAssetsOrder(solidPassworld, createNHAssetOrderParamsModel, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                fillNhAssetsOrder(solidPassworld, fillNHAssetOrderParamsModel, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                cancelNhAssetsOrder(solidPassworld, cancelNHAssetOrderParamsModel, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                updateGas(solidPassworld, updateGasParamsModel, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                claimVestingBalance(claimVestingBalancePrams.id, solidPassworld, params);
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
            if (!TextUtils.isEmpty(solidPassworld)) {
                publishVotes(solidPassworld, publishVotesParamsModel, params);
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
        if (TextUtils.equals(publishVotes.votes, "0")) {
            publishVotes.vote_ids = new ArrayList<>();
        }
        CocosBcxApiWrapper.getBcxInstance().vote_members(AccountHelperUtils.getCurrentAccountName(), password, publishVotes.type, publishVotes.vote_ids, String.valueOf(publishVotes.votes),
                new IBcxCallBack() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("vote_members", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        remeberPwd(password, baseResult.getCode() != 105);
                    }
                });
    }

    /**
     * 领取GAS或节点出块奖励
     *
     * @param password
     * @param params
     */
    private void claimVestingBalance(List<String> awardIds, String password, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().receive_vesting_balances(AccountHelperUtils.getCurrentAccountName(), password, awardIds,
                new IBcxCallBack() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("claimVestingBalance", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        remeberPwd(password, baseResult.getCode() != 105);
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
                        remeberPwd(password, baseResult.getCode() != 105);
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
                        remeberPwd(password, baseResult.getCode() != 105);
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
                        remeberPwd(password, baseResult.getCode() != 105);
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
        CocosBcxApiWrapper.getBcxInstance().create_nh_asset_order(createNHAssetOrderParamsModel.otcAccount, AccountHelperUtils.getCurrentAccountName(),
                password, createNHAssetOrderParamsModel.NHAssetId, createNHAssetOrderParamsModel.orderFee, "COCOS", createNHAssetOrderParamsModel.memo,
                String.valueOf(createNHAssetOrderParamsModel.price), createNHAssetOrderParamsModel.priceAssetId, createNHAssetOrderParamsModel.expiration,
                new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("create_nh_asset_order", s);
                        BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                        setTransactionCallBack(baseResult, params);
                        remeberPwd(password, baseResult.getCode() != 105);
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
                remeberPwd(password, baseResult.getCode() != 105);
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
            remeberPwd(password, baseResult.getCode() != 105);
        });
    }

    private void remeberPwd(String password, boolean b) {
        if (b && JsWebVerifyPasswordDialog.isChecked) {
            solidPassworld = password;
        }
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
            remeberPwd(password, resultModel.getCode() != 105);
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
    private void invoking_contract(String password, JsContractParamsModel jsContractParamsModel, List str5, JsParamsEventModel params) {
        CocosBcxApiWrapper.getBcxInstance().invoking_contract(AccountHelperUtils.getCurrentAccountName(), password, jsContractParamsModel.nameOrId, jsContractParamsModel.functionName, str5, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String s) {
                BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                if (baseResult.isSuccess()) {
                    onJSCallback(params.serialNumber, baseResult.getData());
                } else {
                    onJSCallback(params.serialNumber, s);
                }
                remeberPwd(password, baseResult.getCode() != 105);
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
        CocosBcxApiWrapper.getBcxInstance().transfer(password, assetModel.fromAccount, assetModel.toAccount, assetModel.amount, assetModel.assetId, assetModel.memo, false, s -> {
            BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
            setTransactionCallBack(baseResult, params);
            remeberPwd(password, baseResult.getCode() != 105);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

        try {
            viewModel.uc.moreObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    bottomSheetDialog = new BottomSheetDialog(JsWebViewActivity.this);
                    DialogJswebviewMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_jswebview_more, null, false);
                    bottomSheetDialog.setContentView(binding.getRoot());
                    // 设置dialog 完全显示
                    View parent = (View) binding.getRoot().getParent();
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                    binding.getRoot().measure(0, 0);
                    behavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    parent.setLayoutParams(params);
                    bottomSheetDialog.setCanceledOnTouchOutside(true);
                    final JsWebMoreViewModel jsWebMoreViewModel = new JsWebMoreViewModel(getApplication());
                    binding.setViewModel(jsWebMoreViewModel);
                    bottomSheetDialog.show();
                }
            });
        } catch (Exception e) {

        }
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
        binding.jsWebView.destroy();
        super.onDestroy();
    }

}
