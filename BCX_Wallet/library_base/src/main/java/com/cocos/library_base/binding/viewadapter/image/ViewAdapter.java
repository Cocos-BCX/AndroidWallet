package com.cocos.library_base.binding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cocos.library_base.utils.Utils;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(Utils.getContext())
                        .load(url)
                        .apply(new RequestOptions().placeholder(placeholderRes))
                        .into(imageView);
            }
        } catch (Exception e) {
            Log.i("setImageUri--error", e.getMessage());
        }
    }
}

