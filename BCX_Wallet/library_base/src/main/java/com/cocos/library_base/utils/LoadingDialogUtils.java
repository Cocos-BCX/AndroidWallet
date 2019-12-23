package com.cocos.library_base.utils;

import com.cocos.library_base.R;
import com.cocos.library_base.widget.zloading.ZLoadingDialog;
import com.cocos.library_base.widget.zloading.Z_TYPE;


/**
 * Created by guoningkang on 2017/5/10.
 */

public class LoadingDialogUtils {

    /***
     * 显示加载中loading
     *
     * @param type
     */
    public static void showIndeterminateProgressDialog(ZLoadingDialog dialog, final Z_TYPE type) {
        try {
            dialog.setLoadingBuilder(type)
                    .setLoadingColor(Utils.getColor(R.color.color_262A33))
                    .setHintText(Utils.getString(R.string.loading))
                    //     .setHintTextSize(12) // 设置字体大小
                    .setHintTextColor(Utils.getColor(R.color.color_262A33))  // 设置字体颜色
                    .setDurationTime(0.8) // 设置动画时间百分比
                    // .setDialogBackgroundColor(Utils.getColor(R.color.z_transparent)) // 设置背景色
                    .show();
        } catch (Exception e) {

        }
    }

    /***
     * 显示加载中loading
     *
     * @param type
     */
    public static void showIndeterminateProgressDialog(ZLoadingDialog dialog, String customtext, final Z_TYPE type) {
        try {
            dialog.setLoadingBuilder(type)
                    .setLoadingColor(Utils.getColor(R.color.color_262A33))
                    .setHintText(customtext)
                    //     .setHintTextSize(12) // 设置字体大小
                    .setHintTextColor(Utils.getColor(R.color.color_262A33))  // 设置字体颜色
                    .setDurationTime(0.8) // 设置动画时间百分比
                    // .setDialogBackgroundColor(Utils.getColor(R.color.z_transparent)) // 设置背景色
                    .show();
        } catch (Exception e) {

        }
    }
}
