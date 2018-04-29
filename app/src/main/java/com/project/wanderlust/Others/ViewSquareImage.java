package com.project.wanderlust.Others;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ViewSquareImage extends AppCompatImageView {

    public ViewSquareImage(Context context) {
        super(context);
    }

    public ViewSquareImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewSquareImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

}