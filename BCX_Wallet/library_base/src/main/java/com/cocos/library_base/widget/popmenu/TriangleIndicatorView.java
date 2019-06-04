package com.cocos.library_base.widget.popmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.cocos.library_base.R;


/**
 * 三角形指示器
 */
public class TriangleIndicatorView extends View {

    private int mWidth = 16;
    private int mHeight = 8;
    private int mColor = getResources().getColor(R.color.drop_pop_menu_bg_color);
    private boolean mIsUp = true;

    public TriangleIndicatorView(Context context) {
        super(context);

        init();
    }

    public TriangleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mWidth = dip2px(getContext(), mWidth);
        mHeight = dip2px(getContext(), mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(mColor);

        Path path = new Path();
        if (mIsUp) {
            path.moveTo(mWidth / 2, 0);// 此点为多边形的起点
            path.lineTo(0, mHeight);
            path.lineTo(mWidth, mHeight);
        } else {
            path.moveTo(0, 0);// 此点为多边形的起点
            path.lineTo(mWidth, 0);
            path.lineTo(mWidth / 2, mHeight);
        }

        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getLayoutParams().height = mHeight;
        getLayoutParams().width = mWidth;
    }

    public int getRealWidth() {
        return mWidth;
    }

    public int getRealHeight(){
        return mHeight;
    }

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    /**
     * 设置方向
     *
     * @param isUp 是否向上
     */
    public void setOrientation(boolean isUp) {
        mIsUp = isUp;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
