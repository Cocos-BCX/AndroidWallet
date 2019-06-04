package com.cocos.library_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cocos.library_base.R;

public class LinearLinearLayout extends LinearLayout {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 边框颜色
     */
    private int mPaintColor;
    /**
     * 边框粗细
     */
    private float mBorderStrokeWidth;
    /**
     * 底边边线左边断开距离
     */
    private int mBorderBottomLeftBreakSize;
    /**
     * 底边边线右边断开距离
     */
    private int mBorderBottomRightBreakSize;
    /**
     * 是否需要上边框
     */
    private boolean isNeedTopBorder;
    /**
     * 是否需要左右边框
     */
    private boolean isNeedLeftAndRightBorder;
    /**
     * 是否需要下边框
     */
    private boolean isNeedBottomBorder;


    public LinearLinearLayout(Context context) {
        this(context, null);
    }

    public LinearLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderRelativeLayout);
        mPaintColor = ta.getColor(R.styleable.BorderRelativeLayout_borderColor, Color.GRAY);
        mBorderStrokeWidth = ta.getFloat(R.styleable.BorderRelativeLayout_borderStrokeWidth, 2.0f);
        mBorderBottomLeftBreakSize = ta.getDimensionPixelSize(R.styleable.BorderRelativeLayout_borderBottomLeftBreakSize, 0);
        mBorderBottomRightBreakSize = ta.getDimensionPixelSize(R.styleable.BorderRelativeLayout_borderBottomRightBreakSize, 0);
        isNeedTopBorder = ta.getBoolean(R.styleable.BorderRelativeLayout_needTopBorder, true);
        isNeedLeftAndRightBorder = ta.getBoolean(R.styleable.BorderRelativeLayout_needLeftAndRigtBorder, true);
        isNeedBottomBorder = ta.getBoolean(R.styleable.BorderRelativeLayout_needBottomBorder, true);
        ta.recycle();
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mBorderStrokeWidth);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //  画4个边
        if (isNeedTopBorder) {
            canvas.drawLine(0, 0, this.getWidth(), 0, mPaint);
        }
        if (isNeedBottomBorder) {
            canvas.drawLine(mBorderBottomLeftBreakSize, this.getHeight(), this.getWidth() - mBorderBottomRightBreakSize, this.getHeight(), mPaint);
        }
        if (isNeedLeftAndRightBorder) {
            canvas.drawLine(0, 0, 0, this.getHeight(), mPaint);
            canvas.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight(), mPaint);
        }
    }

    /**
     * 设置边框颜色
     *
     * @param color
     */
    public void setBorderColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置边框宽度
     *
     * @param size
     */
    public void setBorderStrokeWidth(float size) {
        mPaint.setStrokeWidth(size);
        invalidate();
    }


    /**
     * 设置是否需要顶部边框
     *
     * @param needtopborder
     */
    public void setNeedTopBorder(boolean needtopborder) {
        isNeedTopBorder = needtopborder;
        invalidate();
    }

    /**
     * 设置是否需要底部边框
     *
     * @param needbottomborder
     */
    public void setNeedBottomBorder(boolean needbottomborder) {
        isNeedBottomBorder = needbottomborder;
        invalidate();
    }
}
