package com.cocos.module_mine.asset_overview;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.TabEntity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityAssetOverviewBinding;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
@Route(path = RouterActivityPath.ACTIVITY_ASSET_OVERVIEW)
public class AssetOverviewActivity extends BaseActivity<ActivityAssetOverviewBinding, AssetOverviewViewModel> {

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private ArrayList<Fragment> mFragments;

    private int[] mTitles = {R.string.module_mine_asset_overview_digital_assets, R.string.module_mine_asset_overview_prop_assets,};
    private int tabPosition = 0;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_asset_overview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            tabPosition = bundle.getInt(IntentKeyGlobal.TAB_POSITION);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        NumberAssetFragment numberAssetFragment = new NumberAssetFragment();
        NHAssetFragment NHAssetFragment = new NHAssetFragment();
        mFragments.add(numberAssetFragment);
        mFragments.add(NHAssetFragment);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(Utils.getString(mTitles[i])));
        }
        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        initTabListener();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.pSwitchObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (!viewModel.uc.pSwitchObservable.get()) {
                    binding.ivAssetVisible.setImageResource(R.drawable.asset_overview_visible_icon);
                    viewModel.totalAsset.set(SPUtils.getString(Utils.getContext(), SPKeyGlobal.TOTAL_ASSET_VALUE, "0.00"));
                } else {
                    binding.ivAssetVisible.setImageResource(R.drawable.asset_overview_invisible_icon);
                    viewModel.totalAsset.set("****");
                }
            }
        });
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
        binding.viewPager.setCurrentItem(tabPosition);
    }

}
