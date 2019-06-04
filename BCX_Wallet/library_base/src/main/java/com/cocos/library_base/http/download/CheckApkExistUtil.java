package com.cocos.library_base.http.download;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckApkExistUtil {

    /**
     * 应用市场包名
     */
    private static final String[] PACKAGENAME = {
            "com.tencent.android.qqdownloader", "com.qihoo.appstore", "com.baidu.appsearch", "com.xiaomi.market", "com.huawei.appmarket", "com.bbk.appstore",
            "com.meizu.mstore", "com.wandoujia.phoenix2", "com.oppo.market", "cn.goapk.market", "com.android.vending", "com.lenovo.leos.appstore", "com.mappn.gfan"
    };

    public static boolean checkApkExist(Context context, String appPkg) {
        try {
            for (String packageName : PACKAGENAME) {
                if (isAvilible(context, packageName)) {
                    launchAppDetail(context, appPkg, packageName);
                    return true;
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否安装了某应用
     *
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();

        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 启动到应用商店app详情界面
     */
    private static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                return;
            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

