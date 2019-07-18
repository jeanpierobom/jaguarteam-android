package com.example.capstone.layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AspectKeptLinearLayout extends LinearLayout {
    private final int mBgWidth;
    private final int mBgHeight;
    private ViewGroup.LayoutParams mLayoutParams = null;

    public AspectKeptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable bg = getBackground();
        mBgWidth = bg.getIntrinsicWidth();
        mBgHeight = bg.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLayoutParams == null) {
           mLayoutParams = getLayoutParams();
        }
        int width = 0;
        int height = 0;
        if ((mLayoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT || mLayoutParams.width == 0) && mLayoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = width * mBgHeight / mBgWidth;
        } else {
            // You can extend the MATCH_PARENT of the height case
            throw new UnsupportedOperationException(
            "width="+mLayoutParams.width+" height="+mLayoutParams.height
            );
        }
        int mode = MeasureSpec.EXACTLY;
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, mode),
        MeasureSpec.makeMeasureSpec(height, mode));
    }
}