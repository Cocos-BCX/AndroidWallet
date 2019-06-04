package com.cocos.library_base.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自动轮播handler
 *
 * @author ningkang
 * @date 2018/11/19
 */

public class AutoLooperHandler extends Handler {

    /**
     * 首页伦播banner消息处理what
     */
    public static final int SWITCH_BANNER = 5;
    private ViewPager mViewPager;


    @SuppressLint("ClickableViewAccessibility")
    public AutoLooperHandler(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.setOnTouchListener(new HomeVpTouchListener());
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SWITCH_BANNER:
                startLooper();
                break;
            default:
                break;
        }
    }

    /**
     * viewPager触摸监听
     */
    private class HomeVpTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pauseLooper();
                    break;
                case MotionEvent.ACTION_MOVE:
                    pauseLooper();
                    break;
                case MotionEvent.ACTION_UP:
                    restartLooper();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 开始切换广告条
     */
    public void startLooper() {
        try {
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(currentItem + 1);
            sendEmptyMessageDelayed(SWITCH_BANNER, 3000);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 继续切换广告条
     */
    public void restartLooper() {
        removeMessages(SWITCH_BANNER);
        sendEmptyMessageDelayed(SWITCH_BANNER, 3000);
    }

    /**
     * 停止切换广告条
     */
    public void pauseLooper() {
        removeCallbacksAndMessages(null);
    }


}
