package com.cocos.module_mine.join_community;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityJoinCommunityBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */

@Route(path = RouterActivityPath.ACTIVITY_JOIN_COMMUNITY)
public class JoinCommunityActivity extends BaseActivity<ActivityJoinCommunityBinding, JoinCommunityViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_join_community;
    }

    @Override
    public void initData() {
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        binding.llBoard.setBackground(Utils.getDrawable(selectLanguage == 0 ? R.drawable.join_community_banner_zh : R.drawable.join_community_banner_en));
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
