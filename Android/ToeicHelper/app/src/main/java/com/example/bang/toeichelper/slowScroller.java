package com.example.bang.toeichelper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by BANG on 2015-01-10.
 */
public class slowScroller extends Scroller{

    private int mDuration = 800;

    public slowScroller(Context context) {
        super(context);
    }

    public slowScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public slowScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
