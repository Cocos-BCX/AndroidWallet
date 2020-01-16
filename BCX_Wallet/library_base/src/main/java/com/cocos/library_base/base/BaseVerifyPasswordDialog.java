package com.cocos.library_base.base;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cocos.library_base.R;

/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */
public class BaseVerifyPasswordDialog extends BaseDialogFragment {


    IPasswordListener iPasswordListener;

    public void setPasswordListener(IPasswordListener iPasswordListener) {
        this.iPasswordListener = iPasswordListener;
    }

    @Override
    protected int getPopUpLayoutId() {
        return R.layout.password_verify_dialog_layout;
    }

    @Override
    protected void initView(View mainView) {
        EditText editText = mainView.findViewById(R.id.edt_password);
        ImageView ivPwdVisible = mainView.findViewById(R.id.iv_pwd_visible);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pwdVisibleControl(editText, ivPwdVisible);
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
                    iPasswordListener.onFinish(editText.getText().toString());
                }
                dismiss();
            }
        });
        ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl(editText, ivPwdVisible);
            }
        });
    }

    @Override
    protected boolean needForbidBackPress() {
        return false;
    }

    @Override
    protected boolean isCancelableTouchOutSide() {
        return true;
    }


    public interface IPasswordListener {

        void onFinish(String password);

        void cancel();
    }

}
