package com.cocos.library_base.widget;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;

/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */
public class JsWebVerifyPasswordDialog extends BaseVerifyPasswordDialog {


    IPasswordListener iPasswordListener;
    public static boolean isChecked;

    public void setPasswordListener(IPasswordListener iPasswordListener) {
        this.iPasswordListener = iPasswordListener;
    }

    @Override
    protected int getPopUpLayoutId() {
        return R.layout.js_web_password_verify_dialog_layout;
    }

    @Override
    protected void initView(View mainView) {
        EditText editText = mainView.findViewById(R.id.edt_password);
        CheckBox checkBox = mainView.findViewById(R.id.cb_secret_free_check);
        mainView.findViewById(R.id.btn_password_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != iPasswordListener) {
                    iPasswordListener.cancel();
                }
                dismiss();
            }
        });
        mainView.findViewById(R.id.btn_password_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != iPasswordListener) {
                    JsWebVerifyPasswordDialog.isChecked = checkBox.isChecked();
                    String passcode = editText.getText().toString();
                    iPasswordListener.onFinish(passcode);
                }
                dismiss();
            }
        });

    }

    @Override
    protected boolean needForbidBackPress() {
        return true;
    }

    @Override
    protected boolean isCancelableTouchOutSide() {
        return false;
    }


    public interface IPasswordListener {

        void onFinish(String password);

        void cancel();
    }

}
