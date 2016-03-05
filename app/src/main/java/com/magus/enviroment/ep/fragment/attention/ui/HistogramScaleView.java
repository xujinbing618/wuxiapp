package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.magus.magusutils.UnitUtil;


/**
 * 刻度条
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui;
 * 2015-15/5/14-下午2:45.
 */
public class HistogramScaleView extends BaseHistogramView {

    private static String TAG = "BarScaleView";
    private String[] mScale = {"0%", "20%", "40%", "60%", "80%","100%"};
    private Paint mPaint ;
    private float SCALE_TEXT_SIZE =UnitUtil.dip2px(9);

    public HistogramScaleView(Context context) {
        super(context);
        init(context);
    }

    public HistogramScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistogramScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init(Context context) {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        paintScale(canvas);

    }
    //刻度
    private void paintScale(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(SCALE_TEXT_SIZE);
        canvas.drawLine(mWidth - 1, mHeight - BOTTOM_MARGIN, mWidth - 1, TOP_MARGIN, mPaint);
        mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / (mScale.length-1);
        for (int i = 0; i < mScale.length; i++) {
            canvas.drawText(mScale[i], mWidth / 6, mHeight - BOTTOM_MARGIN - mPerHeight * i, mPaint);
        }
    }
}
