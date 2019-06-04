package com.cocos.library_base.utils.singleton;

import android.content.ClipboardManager;
import android.content.Context;

import com.cocos.library_base.utils.Utils;

/**
 * BclSDK单列对象类
 *
 * @author ningkang
 */

public class ClipboardManagerInstance {

    private static class ClipboardManagerInstanceHolder {
        static final ClipboardManager INSTANCE = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static ClipboardManager getClipboardManager() {
        return ClipboardManagerInstanceHolder.INSTANCE;
    }

}
