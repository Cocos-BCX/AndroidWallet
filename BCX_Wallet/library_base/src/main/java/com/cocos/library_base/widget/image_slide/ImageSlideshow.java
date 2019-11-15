package com.cocos.library_base.widget.image_slide;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cocos.library_base.R;
import com.cocos.library_base.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jun on 2016/8/4.
 */
public class ImageSlideshow extends FrameLayout {

    private static final String TAG = "ImageSlideshow";

    private Context context;
    private View contentView;
    private ViewPager vpImageTitle;
    private LinearLayout llDot;
    private int count;
    private List<View> viewList;
    private boolean isAutoPlay;
    private Handler handler;
    private int currentItem;
    private Animator animatorToLarge;
    private Animator animatorToSmall;
    private SparseBooleanArray isLarge;
    private List<ImageTitleBean> imageTitleBeanList;
    private int dotSize = 12;
    private int dotSpace = Utils.dip2px(8);
    private int delay = 3000;

    public ImageSlideshow(Context context) {
        this(context, null);
    }

    public ImageSlideshow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageSlideshow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = Utils.getContext();
        // 初始化View
        initView();
        // 初始化Animator
        initAnimator();
        // 初始化数据
        initData();
    }

    private void initData() {
        imageTitleBeanList = new ArrayList<>();
    }

    private void initAnimator() {
        animatorToLarge = AnimatorInflater.loadAnimator(context, R.animator.scale_to_large);
        animatorToSmall = AnimatorInflater.loadAnimator(context, R.animator.scale_to_small);
    }

    /**
     * 初始化View
     */
    private void initView() {
        contentView = LayoutInflater.from(Utils.getContext()).inflate(R.layout.is_main_layout, this, true);
        vpImageTitle = (ViewPager) findViewById(R.id.vp_image_title);
        llDot = (LinearLayout) findViewById(R.id.ll_dot);
    }

    // 设置小圆点的大小
    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    // 设置小圆点的间距
    public void setDotSpace(int dotSpace) {
        this.dotSpace = dotSpace;
    }

    // 设置图片轮播间隔时间
    public void setDelay(int delay) {
        this.delay = delay;
    }


    // 添加图片
    public void addImageResource(int imageId) {
        ImageTitleBean imageTitleBean = new ImageTitleBean();
        imageTitleBean.setImageId(imageId);
        imageTitleBeanList.add(imageTitleBean);
    }

    public void clear() {
        imageTitleBeanList.clear();
    }

    // 添加图片
    public void addImageUrl(String imageUrl) {
        ImageTitleBean imageTitleBean = new ImageTitleBean();
        imageTitleBean.setImageUrl(imageUrl);
        imageTitleBeanList.add(imageTitleBean);
    }

    // 添加图片和标题
    public void addImageTitle(String imageUrl, String title) {
        ImageTitleBean imageTitleBean = new ImageTitleBean();
        imageTitleBean.setImageUrl(imageUrl);
        imageTitleBean.setTitle(title);
        imageTitleBeanList.add(imageTitleBean);
    }

    // 添加图片和标题的JavaBean
    public void addImageTitleBean(ImageTitleBean imageTitleBean) {
        imageTitleBeanList.add(imageTitleBean);
    }

    // 设置图片和标题的JavaBean数据列表
    public void setImageTitleBeanList(List<ImageTitleBean> imageTitleBeanList) {
        this.imageTitleBeanList = imageTitleBeanList;
    }

    // 设置完后最终提交
    public void commit() {
        if (imageTitleBeanList != null) {
            count = imageTitleBeanList.size();
            // 设置ViewPager
            setViewPager(imageTitleBeanList);
            // 设置指示器
            setIndicator();
            // 开始播放
            starPlay();
        } else {
            Log.e(TAG, "数据为空");
        }
    }

    /**
     * 设置指示器
     */
    private void setIndicator() {
        try {
            isLarge = new SparseBooleanArray();
            // 记得创建前先清空数据，否则会受遗留数据的影响。
            llDot.removeAllViews();
            for (int i = 0; i < count; i++) {
                View view = new View(context);
                view.setBackgroundResource(R.drawable.found_vp_indicator_normal);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(9), Utils.dip2px(3));
                if (i != 0) {
                    layoutParams.leftMargin = dotSpace;
                }
                llDot.addView(view, layoutParams);
                isLarge.put(i, false);
            }
            llDot.getChildAt(0).setBackgroundResource(R.drawable.found_vp_indicator_focused);
            animatorToLarge.setTarget(llDot.getChildAt(0));
            animatorToLarge.start();
            isLarge.put(0, true);
        } catch (Exception e) {

        }
    }

    /**
     * 开始自动播放图片
     */
    private void starPlay() {
        // 如果少于2张就不用自动播放了
        if (count < 2) {
            isAutoPlay = false;
        } else {
            isAutoPlay = true;
            handler = new Handler();
            handler.postDelayed(task, delay);
        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                // 位置循环
                currentItem = currentItem % (count + 1) + 1;
                // 正常每隔3秒播放一张图片
                vpImageTitle.setCurrentItem(currentItem);
                handler.postDelayed(task, delay);
            } else {
                // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
                handler.postDelayed(task, 5000);
            }
        }
    };

    // 创建监听器接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 声明监听器
    private OnItemClickListener onItemClickListener;

    // 提供设置监听器的公共方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    class ImageTitlePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = viewList.get(position);
            // 设置Item的点击监听器
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 注意：位置是position-1
                    onItemClickListener.onItemClick(v, position - 1);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    /**
     * 设置ViewPager
     *
     * @param imageTitleBeanList
     */
    private void setViewPager(List<ImageTitleBean> imageTitleBeanList) {
        // 设置View列表
        setViewList(imageTitleBeanList);
        vpImageTitle.setAdapter(new ImageTitlePagerAdapter());
        // 从第1张图片开始（位置刚好也是1，注意：0位置现在是最后一张图片）
        currentItem = 1;
        vpImageTitle.setCurrentItem(1);
        vpImageTitle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 遍历一遍子View，设置相应的背景。
                for (int i = 0; i < llDot.getChildCount(); i++) {
                    if (i == position - 1) {// 被选中
                        llDot.getChildAt(i).setBackgroundResource(R.drawable.found_vp_indicator_focused);
                        if (!isLarge.get(i)) {
                            animatorToLarge.setTarget(llDot.getChildAt(i));
                            animatorToLarge.start();
                            isLarge.put(i, true);
                        }
                    } else {// 未被选中
                        llDot.getChildAt(i).setBackgroundResource(R.drawable.found_vp_indicator_normal);
                        if (isLarge.get(i)) {
                            animatorToSmall.setTarget(llDot.getChildAt(i));
                            animatorToSmall.start();
                            isLarge.put(i, false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    // 闲置中
                    case ViewPager.SCROLL_STATE_IDLE:
                        // “偷梁换柱”
                        if (vpImageTitle.getCurrentItem() == 0) {
                            vpImageTitle.setCurrentItem(count, false);
                        } else if (vpImageTitle.getCurrentItem() == count + 1) {
                            vpImageTitle.setCurrentItem(1, false);
                        }
                        currentItem = vpImageTitle.getCurrentItem();
                        isAutoPlay = true;
                        break;
                    // 拖动中
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    // 设置中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
    }

    /**
     * 根据出入的数据设置View列表
     *
     * @param imageTitleBeanList
     */
    private void setViewList(List<ImageTitleBean> imageTitleBeanList) {
        viewList = new ArrayList<>();
        for (int i = 0; i < imageTitleBeanList.size() + 2; i++) {
            View view = LayoutInflater.from(Utils.getContext()).inflate(R.layout.is_image_title_layout, null);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            try {
                if (i == 0) {
                    if (!TextUtils.isEmpty(imageTitleBeanList.get(imageTitleBeanList.size() - 1).getImageUrl())) {
                        Glide.with(context).load(imageTitleBeanList.get(imageTitleBeanList.size() - 1).getImageUrl()).into(ivImage);
                    }
                } else if (i == imageTitleBeanList.size() + 1) {
                    if (!TextUtils.isEmpty(imageTitleBeanList.get(0).getImageUrl())) {
                        Glide.with(context).load(imageTitleBeanList.get(0).getImageUrl()).into(ivImage);
                    }
                } else {
                    if (!TextUtils.isEmpty(imageTitleBeanList.get(i - 1).getImageUrl())) {
                        Glide.with(context).load(imageTitleBeanList.get(i - 1).getImageUrl()).into(ivImage);
                    }
                }
            } catch (Exception e) {
            }
            viewList.add(view);
        }
    }

    /**
     * 释放资源
     */
    public void releaseResource() {
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
        context = null;
    }
}
