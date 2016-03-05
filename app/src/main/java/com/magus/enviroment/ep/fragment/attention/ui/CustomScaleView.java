package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.magus.magusutils.UnitUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 刻度条
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui;
 * 2015-15/5/14-下午2:45.
 */
public class CustomScaleView extends BaseHistogramView {

    private static String TAG = "BarScaleView";
    private String[] mScale = {"0%", "20%", "40%", "60%", "80%","100%"};
    private Paint mPaint ;
    private float SCALE_TEXT_SIZE =UnitUtil.dip2px(9);
    private int[] arrays =  {2, 24, 3, 18, 2, 22, 9, 32};
    private int mMax = 30;
    private List<Float> mList = new ArrayList<Float>();
    private float mPerScale;
    public CustomScaleView(Context context) {
        super(context);
        init(context);
    }

    public CustomScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setList(List<Float> list){
        this.mList = list;
        invalidate();
    }

    private void init(Context context) {
        mPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        if (mList.size()!=0) {
            Collections.sort(mList);
            mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / mMax; //每一份的高度
            mPerScale = mList.get(mList.size() - 1) / 5.0f; //每个模块占多少份
            paintScale(canvas);
        }
    }
    //刻度
    private void paintScale(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(SCALE_TEXT_SIZE);
        canvas.drawLine(mWidth - 1, mHeight - BOTTOM_MARGIN, mWidth - 1, TOP_MARGIN, mPaint);
        mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / (mScale.length-1);
        for (int i = 0; i < mScale.length; i++) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dd = fnum.format(mPerScale*i);

            canvas.drawText(dd, mWidth / 6, mHeight - BOTTOM_MARGIN - mPerHeight * i, mPaint);
        }
    }
}
