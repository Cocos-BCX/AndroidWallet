package com.cocos.library_base.widget.zloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;


public class ZLoadingDrawable extends Drawable implements Animatable
{
    private final ZLoadingBuilder mZLoadingBuilder;

    ZLoadingDrawable(ZLoadingBuilder builder)
    {
        this.mZLoadingBuilder = builder;
        this.mZLoadingBuilder.setCallback(new Callback()
        {
            @Override
            public void invalidateDrawable(Drawable d)
            {
                invalidateSelf();
            }

            @Override
            public void scheduleDrawable(Drawable d, Runnable what, long when)
            {
                scheduleSelf(what, when);
            }

            @Override
            public void unscheduleDrawable(Drawable d, Runnable what)
            {
                unscheduleSelf(what);
            }
        });
    }

    void initParams(Context context)
    {
        if (mZLoadingBuilder != null)
        {
            mZLoadingBuilder.init(context);
            mZLoadingBuilder.initParams(context);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        if (!getBounds().isEmpty())
        {
            this.mZLoadingBuilder.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha)
    {
        this.mZLoadingBuilder.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {
        this.mZLoadingBuilder.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start()
    {
        this.mZLoadingBuilder.start();
    }

    @Override
    public void stop()
    {
        this.mZLoadingBuilder.stop();
    }

    @Override
    public boolean isRunning()
    {
        return this.mZLoadingBuilder.isRunning();
    }

    @Override
    public int getIntrinsicHeight()
    {
        return (int) this.mZLoadingBuilder.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth()
    {
        return (int) this.mZLoadingBuilder.getIntrinsicWidth();
    }
}
