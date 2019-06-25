package com.cocos.library_base.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.UpdateInfo;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.download.CheckApkExistUtil;
import com.cocos.library_base.http.download.DownLoadManager;
import com.cocos.library_base.http.download.ProgressCallBack;
import com.cocos.library_base.http.http.HttpMethods;
import com.cocos.library_base.utils.multi_language.SPUtil;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * @author ningkang.guo
 * @Date 2019/5/28
 */
public class VersionUtil {

    /**
     * 请求版本更新接口
     */
    public static void updateVersion(BaseViewModel baseViewModel, Activity activity) {
        try {
            Observable<UpdateInfo> observable = BaseUrlApi.getApiBaseService().getVersionInfo(1002, "Android");
            HttpMethods.toSubscribe(observable, new BaseObserver<UpdateInfo>() {
                @Override
                protected void onBaseNext(UpdateInfo data) {
                    String currentVersion = AppApplicationMgr.getVersionName(Utils.getContext());
                    int result = compareVersion(currentVersion, data.data.version);
                    if (result == -1) {
                        update(data, baseViewModel, activity);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private static void update(UpdateInfo data, BaseViewModel baseViewModel, Activity activity) {
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setTitle(R.string.update_title);
        normalDialog.setCancelable(false);
        normalDialog.setMessage(selectLanguage == 0 ? data.data.info : data.data.en_info);
        normalDialog.setPositiveButton(R.string.update_btn_text, (dialog, which) -> {
            baseViewModel.loadUrlEvent.observe((LifecycleOwner) activity, url -> downFile(url, activity));
            baseViewModel.loadUrlEvent.setValue(data.data.download_url);
        });
        if (!data.data.is_force) {
            normalDialog.setNegativeButton(R.string.cancel_text, (dialog, which) -> dialog.dismiss());
        }
        normalDialog.show();
    }


    /**
     * 下载apk文件
     *
     * @param url
     * @param activity
     */
    private static void downFile(String url, Activity activity) {
        String diskDir = getDiskCacheDir(activity);
        if (TextUtils.isEmpty(diskDir)) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(Utils.getString(R.string.module_asset_downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        DownLoadManager.getInstance().load(url, new ProgressCallBack<ResponseBody>(diskDir, getDestFileName()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                installApkO(activity, getDestFileDir(activity));
            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) progress);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort(Utils.getString(R.string.module_asset_download_failed));
                progressDialog.dismiss();
            }
        });
    }

    public static void installApkO(Activity context, String downloadApkPath) {
        // 8.0 需要判断是否允许了安装未知来源应用的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否有安装位置来源的权限
            boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                installApk(context, downloadApkPath);
            } else {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
                normalDialog.setTitle(R.string.requests_permissions_title);
                normalDialog.setMessage(R.string.unknown_source_request_tips);
                normalDialog.setPositiveButton(R.string.to_setting,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Uri packageUri = Uri.parse("package:" + context.getPackageName());
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                                context.startActivityForResult(intent, 10086);
                            }
                        });
                normalDialog.setNegativeButton(R.string.cancel_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                normalDialog.show();
            }
        } else {
            installApk(context, downloadApkPath);
        }
    }

    private static void installApk(Context context, String downloadApk) {
        try {
            File file = new File(downloadApk);
            if (!file.exists()) {
                ToastUtils.showLong(R.string.package_not_exist);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = getMimeType(file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0 及以上
                Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, type);
            } else {
                //6.0 及以下
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, type);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            CheckApkExistUtil.checkApkExist(context, context.getPackageName());
        }
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    private static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 获取
     *
     * @param context
     * @return
     */
    private static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Objects.requireNonNull(context.getExternalCacheDir()).getPath();
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                CheckApkExistUtil.checkApkExist(context, context.getPackageName());
                return null;
            }
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    private static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * @param context
     * @return
     */
    public static String getDestFileDir(Context context) {
        final String destFileDir = getDiskCacheDir(context);
        return destFileDir + getDestFileName();
    }

    private static String getDestFileName() {
        return "/cocos_bcx.apk";
    }
}
