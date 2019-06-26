package com.cocos.library_base.utils.singleton;


import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;

/**
 * Gson单列对象类
 *
 * @author ningkang
 */

public class BottomDialogSingleInstance {

    private static volatile BottomSheetDialog mInstance;

    public static BottomSheetDialog getInstance(Activity activity) {
        if (mInstance == null) {
            synchronized (BottomSheetDialog.class) {
                if (mInstance == null) {
                    mInstance = new BottomSheetDialog(activity);
                }
            }
        }
        return mInstance;
    }
}
