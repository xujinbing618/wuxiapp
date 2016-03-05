package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.magus.magusutils.UnitUtil;

/**
 * Created by pau
 * Packagename demo.hpg.org.pauldemo.chart.ui
 * 2015-15/5/14-下午2:45.
 */
public class BaseHistogramView extends BaseView {
    protected static float BOTTOM_MARGIN= UnitUtil.dip2px(20); //柱形图离容器底部的距离
    protected static float TOP_MARGIN=UnitUtil.dip2px(20);//柱形图离容器顶部的距离
    public BaseHistogramView(Context context) {
        super(context);
    }

    public BaseHistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseHistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
