package com.cocos.library_base.widget.popmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.cocos.library_base.R;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


/**
 * 带箭头指示器的下拉弹窗菜单
 * Created by gnk on 2018/10/22/022.
 */
public class DropPopMenu {

    private static final int INDICATOR_TO_CONTAINER_MIN_MARGIN = 5;
    private static final int MARGIN_SCREEN = 5;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private TriangleIndicatorView mTriangleUpIndicatorView;
    private TriangleIndicatorView mTriangleDownIndicatorView;
    private LinearLayout mContainerLayout;
    private DropPopLayout mDropPopLayout;
    private ListView mListView;
    private View mBtnView;
    private DropPopMenuAdapter mDropPopMenuAdapter;
    private OnItemClickListener mOnItemClickListener;

    private List<MenuItem> mMenuItemList;
    /**
     * 指示器与菜单布局两侧的最小间距
     */
    private int mIndicatorToContainerMinMargin;
    private int mHorizontalMargin = 10;
    private int mIconTextMargin = 10;
    /**
     * 距离屏幕的间距
     */
    private int mMarginScreen;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mIconWidth = 25;
    private int mTextSize;
    private int mItemHeight;
    private int mTotalHeight;
    private int mMaxTextWidth;
    private int mWidth;

    private boolean mIsShowIcon = false;
    private boolean mIsShowAtUp = false;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mItemTextColor = 0;

    public DropPopMenu(Context context) {
        mContext = context;
        init();

        mDropPopLayout = new DropPopLayout(context);
        mTriangleUpIndicatorView = mDropPopLayout.getTriangleUpIndicatorView();
        mTriangleDownIndicatorView = mDropPopLayout.getTriangleDownIndicatorView();
        mContainerLayout = mDropPopLayout.getContainerLayout();

        mScreenWidth = getScreenWidth(mContext);
        create();
    }

    private void init() {
        mIndicatorToContainerMinMargin = dip2px(mContext, INDICATOR_TO_CONTAINER_MIN_MARGIN);
        mMarginScreen = dip2px(mContext, MARGIN_SCREEN);
        mIconTextMargin = (int) mContext.getResources().getDimension(R.dimen.dp_12);
        mHorizontalMargin = (int) mContext.getResources().getDimension(R.dimen.dp_22);
        mIconWidth = (int) mContext.getResources().getDimension(R.dimen.dp_22);
        mItemHeight = (int) mContext.getResources().getDimension(R.dimen.dp_44);
        mTextSize = (int) mContext.getResources().getDimension(R.dimen.sp_15);
        mScreenHeight = getScreenHeight(mContext);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void create() {
        mPopupWindow = new PopupWindow(mDropPopLayout, LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopOnDismissListener());

        mDropPopLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return true;
            }
        });
    }

    private void initListView() {
        mListView = new ListView(mContext);
        mListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT));
        mListView.setDivider(null);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    MenuItem menuItem = null;
                    if (mMenuItemList != null) {
                        menuItem = mMenuItemList.get(position);
                    }
                    mOnItemClickListener.onItemClick(adapterView, view, position, id, menuItem);
                }
                mPopupWindow.dismiss();
            }
        });
        mContainerLayout.addView(mListView);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setWidth(int width) {
        mWidth = width;
        if (mBtnView != null) {
            updateViewPosition(mBtnView);
        }
    }

    public void setIsShowIcon(boolean isShowIcon) {
        mIsShowIcon = isShowIcon;
    }

    public void setMenuList(List<MenuItem> menuList) {
        if (mMenuItemList != null) {
            mMenuItemList.clear();
        } else {
            mMenuItemList = new ArrayList<>();
        }
        mMenuItemList.addAll(menuList);

        checkWidth();
        checkHeight();

        initListView();
        if (mDropPopMenuAdapter == null) {
            mDropPopMenuAdapter = new DropPopMenuAdapter();
            mListView.setAdapter(mDropPopMenuAdapter);
        } else {
            mDropPopMenuAdapter.notifyDataSetChanged();
        }
    }

    public void setBackgroundResource(int backgroundResource) {
        mDropPopLayout.setBackgroundResource(backgroundResource);
    }

    public void setBackgroundColor(int color) {
        mDropPopLayout.setBackgroundColor(color);
    }

    /**
     * 设置箭头颜色
     *
     * @param color
     */
    public void setTriangleIndicatorViewColor(int color) {
        mDropPopLayout.setTriangleIndicatorViewColor(color);
    }

    public void setItemTextColor(int color) {
        mItemTextColor = color;
    }

    private void checkWidth() {
        int size = getListSize(mMenuItemList);
        String temp = "";
        for (int i = 0; i < size; i++) {
            String itemTitle = mMenuItemList.get(i).getItemTitle();
            if (itemTitle.length() > temp.length()) {
                temp = itemTitle;
            }
        }

        mMaxTextWidth = (int) getTextWidth(temp, mTextSize);
    }

    private void checkHeight() {
        int size = getListSize(mMenuItemList);
        mTotalHeight = size * mItemHeight;
    }

    public void show(final View parent) {
        mBtnView = parent;
        setBackgroundAlpha(50f);
        mDropPopLayout.requestFocus();

        mIsShowAtUp = false;
        int parentHeight = parent.getHeight();
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int y = location[1];
        int popMenuHeight = mTotalHeight + mTriangleUpIndicatorView.getRealHeight();
        if (mScreenHeight - y - parentHeight < popMenuHeight) {
            mIsShowAtUp = true;
        }

        if (mIsShowAtUp) {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, mScreenHeight - y);
        } else {
            mPopupWindow.showAsDropDown(parent, 0, 0);
        }
        updateView();
    }

    private void updateView() {
        mDropPopLayout.post(new TimerTask() {
            @Override
            public void run() {
                updateViewPosition(mBtnView);
            }
        });
    }

    private void updateViewPosition(View parent) {
        int parentWidth = parent.getMeasuredWidth();
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int x = location[0];

        int centerX = x + parentWidth / 2;
        int leftMargin = x;
        int rightMargin = mScreenWidth - leftMargin - parentWidth;
        int containerViewHalfWidth = mWidth / 2;
        int indicatorViewHalfWidth = mTriangleUpIndicatorView.getRealWidth() / 2;

        LinearLayout.LayoutParams upIndicatorParams = (LinearLayout.LayoutParams) mTriangleUpIndicatorView.getLayoutParams();
        LinearLayout.LayoutParams containerParams = (LinearLayout.LayoutParams) mContainerLayout.getLayoutParams();
        containerParams.width = mWidth;


        if (leftMargin < rightMargin) {//在左侧
            if (leftMargin >= containerViewHalfWidth) {//显示在中间
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = centerX - containerViewHalfWidth;
            } else {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = mMarginScreen;
                if (upIndicatorParams.rightMargin > containerParams.rightMargin - mIndicatorToContainerMinMargin
                        && mWidth <= mScreenWidth / 2) {//矫正箭头在列表右边
                    int newLeftMargin = upIndicatorParams.leftMargin - mIndicatorToContainerMinMargin;
                    if (newLeftMargin >= mMarginScreen) {
                        containerParams.leftMargin = newLeftMargin;
                    }
                }
            }
        } else if (leftMargin > rightMargin) {//在右侧
            if (rightMargin >= containerViewHalfWidth) {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = centerX - containerViewHalfWidth;
            } else {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = mScreenWidth - containerViewHalfWidth * 2 - mMarginScreen;
                if (upIndicatorParams.leftMargin < containerParams.leftMargin + mIndicatorToContainerMinMargin) {//矫正箭头在列表左边
                    containerParams.leftMargin = upIndicatorParams.leftMargin - mIndicatorToContainerMinMargin;
                }
            }
        } else {//在中间
            int left = centerX - indicatorViewHalfWidth;
            int right = centerX - containerViewHalfWidth;
            upIndicatorParams.leftMargin = left;
            containerParams.leftMargin = right;
        }

        if (upIndicatorParams.leftMargin <= 0) {//校正三角形指示器的边界超过范围
            upIndicatorParams.leftMargin = mMarginScreen + mIndicatorToContainerMinMargin;
        } else if (upIndicatorParams.leftMargin + indicatorViewHalfWidth * 2 >= mScreenWidth) {
            upIndicatorParams.leftMargin = mScreenWidth - indicatorViewHalfWidth * 2 - mMarginScreen - mIndicatorToContainerMinMargin;
        }

        mDropPopLayout.setOrientation(mIsShowAtUp);

        mTriangleUpIndicatorView.setLayoutParams(upIndicatorParams);
        mTriangleDownIndicatorView.setLayoutParams(upIndicatorParams);
        mContainerLayout.setLayoutParams(containerParams);
    }

    private class PopOnDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        }
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /***
     * 设置添加屏幕的背景透明度* @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public float getTextWidth(String text, int textSize) {
        int digitAndLetterCount = getDigitLetterCount(text);
        int chLength = text.length() - digitAndLetterCount;
        return chLength * textSize + digitAndLetterCount * textSize * 2 / 3;
    }

    private int getDigitLetterCount(String text) {
        int length = text.length();
        int count = 0;
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9') {
                count++;
            } else if (c >= 'a' && c <= 'z') {
                count++;
            }
        }
        return count;
    }

    private int getListSize(List list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    private class DropPopMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuItemList == null ? 0 : mMenuItemList.size();
        }

        @Override
        public MenuItem getItem(int position) {
            return mMenuItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null || view.getTag() == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_drop_pop_menu, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.iconIv.setVisibility(mIsShowIcon ? View.VISIBLE : View.GONE);
            if (mIsShowIcon) {
                holder.iconIv.setImageResource(getItem(position).iconResId);
            }
            holder.textTv.setText(getItem(position).itemTitle);
            if (mItemTextColor != 0) {
                holder.textTv.setTextColor(mItemTextColor);
            }

            //根据最长文字调整总宽度
            boolean iconIsGone = holder.iconIv.getVisibility() == View.GONE;
            if (position == 0) {
                mWidth = mMaxTextWidth + mHorizontalMargin * 2;
                if (!iconIsGone) {
                    mWidth += mIconWidth + mIconTextMargin;
                }
            }

            return view;
        }

        private class ViewHolder {
            ImageView iconIv;
            TextView textTv;

            @SuppressLint("CutPasteId")
            public ViewHolder(View view) {
                iconIv = (ImageView) view.findViewById(R.id.iv_icon);
                textTv = (TextView) view.findViewById(R.id.tv_text);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int position, long id, MenuItem menuItem);
    }
}
