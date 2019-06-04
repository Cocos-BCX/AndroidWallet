package com.cocos.library_base.binding.typeface;

import android.databinding.BindingConversion;
import android.graphics.Typeface;

import com.cocos.library_base.utils.Utils;

public class FontBinding {
    @BindingConversion
    public static Typeface convertStringToFace(String fontName) {
        try {
            return FontCache.getTypeface(fontName, Utils.getContext());
        } catch (Exception e) {
            throw e;
        }
    }
}
