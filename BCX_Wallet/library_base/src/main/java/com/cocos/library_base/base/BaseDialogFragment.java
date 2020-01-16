package com.cocos.library_base.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.cocos.library_base.R;

import java.util.Objects;

public abstract class BaseDialogFragment extends DialogFragment {

    private View mainView;
    private boolean isPwdCancel = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(isCancelableTouchOutSide());
        Objects.requireNonNull(getDialog().getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        backPressForbid(needForbidBackPress());

        // 去除一开始的导航栏显示
        // see https://stackoverflow.com/questions/22794049/how-do-i-maintain-the-immersive-mode-in-dialogs
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());

        getDialog().setOnShowListener(dialog -> {
            // Clear the not focusable flag from the window
            try {
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Update the WindowManager with the new attributes (no nicer way I know of to do this)..
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            try {
                assert wm != null;
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mainView = inflater.inflate(getPopUpLayoutId(), container);
        mainView.setOnSystemUiVisibilityChangeListener(i -> hideSystemUI(mainView));
        initView(mainView);
        initData();
        return mainView;
    }

    /**
     * 获取弹窗布局Id
     *
     * @return 获取弹窗布局Id
     */
    protected abstract int getPopUpLayoutId();


    protected abstract void initView(View mainView);

    /**
     * 是否需要禁掉返回键
     *
     * @return 是否需要禁用返回
     */
    protected abstract boolean needForbidBackPress();

    /**
     * 点击外部区域弹窗是否消失
     *
     * @return 点击外部区域弹窗是否消失
     */
    protected abstract boolean isCancelableTouchOutSide();


    /**
     * 初始化弹窗数据
     */
    protected void initData() {
    }

    @Override
    public void dismiss() {
        try {
            getDialog().dismiss();
        } catch (NullPointerException e) {
            // ignore
        }

    }

    /**
     * 返回键事件
     * param: needForbid 是否需要禁掉返回键
     */
    private void backPressForbid(boolean needForbid) {
        if (!needForbid) {
            return;
        }
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            return keyCode == KeyEvent.KEYCODE_BACK;
        });
    }

    private void hideSystemUI(View view) {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (RuntimeException e) {
        }

    }

    /**
     * 密码输入框右边的眼睛点击改变状态逻辑
     */
    public void pwdVisibleControl(EditText editText, ImageView imageView) {
        if (isPwdCancel) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(R.drawable.hidden_pwd);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(R.drawable.show_pwd);
        }
        isPwdCancel = !isPwdCancel;
        String pwd = editText.getText().toString().trim();
        editText.setSelection(pwd.length());
    }
}
