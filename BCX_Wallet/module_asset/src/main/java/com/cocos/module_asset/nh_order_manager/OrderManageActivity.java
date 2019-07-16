package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.TabEntity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityOrderManageBinding;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
@Route(path = RouterActivityPath.ACTIVITY_ORDER_MANAGE)
public class OrderManageActivity extends BaseActivity<ActivityOrderManageBinding, OrderManageViewModel> {

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mTitles = {R.string.module_asset_nh_order_mine_title, R.string.module_asset_nh_order_all_title,};

    private ArrayList<Fragment> mFragments;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_manage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        MineNhOrderFragment mineNhOrderFragment = new MineNhOrderFragment();
        AllNhOrderFragment allNhOrderFragment = new AllNhOrderFragment();
        mFragments.add(mineNhOrderFragment);
        mFragments.add(allNhOrderFragment);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(Utils.getString(mTitles[i])));
        }
        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        initTabListener();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Utils.getString(mTitles[position]);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private void initTabListener() {
        binding.tabLayout.setTabData(mTabEntities);
        binding.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.viewPager.setCurrentItem(0);
    }
}
