package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui
 * 2015-15/5/15-上午9:26.
 */
public class BaseView extends View {
    protected float mHeight;//容器的高度==canvas.getHeight()
    protected float mWidth;//容器的宽度==canvas.getWidth()
    protected float mPerHeight; //每单位机子的高度

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
