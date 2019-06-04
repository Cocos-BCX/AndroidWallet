package com.cocos.library_base.widget;

import android.view.View;
import android.widget.EditText;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseDialogFragment;

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
        mainView.findViewById(R.id.btn_password_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPasswordListener.cancel();
                dismiss();
            }
        });
        mainView.findViewById(R.id.btn_password_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPasswordListener.onFinish(editText.getText().toString());
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
