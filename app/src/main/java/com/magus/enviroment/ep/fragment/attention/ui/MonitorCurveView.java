package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.magus.enviroment.R;
import com.magus.magusutils.UnitUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui
 * 2015-15/5/14-下午6:57.
 */
public class MonitorCurveView extends BaseHistogramView {
    private static String TAG = "MonitorCurveView";
    private float mMax = 32.0f; //数组中最大值
    private int mMin = 0; //坐标起始值
    private float mPerHeight; //每单位机子的高度
    private float mPerWidth;
    private Paint mPaint;
    protected static float RIGHT_MARGIN = UnitUtil.dip2px(20);//柱形图离容器顶部的距离
    private List<Float> mOverList = new ArrayList<Float>();
    private List<Float> mMaList = new ArrayList<Float>();
    private List<String> mDateList=new ArrayList<String>();
    private Canvas mCanvas;
    private float mAvgValue=0;
    public MonitorCurveView(Context context) {
        super(context);
        init(context);
    }

    public MonitorCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonitorCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //overList, stopList, lostList, stop2List
    public void setList(List<Float> overList, List<Float> maList,List<String> dateList) {
        this.mOverList = overList;
        this.mMaList = maList;
        this.mDateList = dateList;
        invalidate();

    }

    public void setAvgValue(float value){
        this.mAvgValue = value;
    }

    private void init(Context context) {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Collections.sort(mMaList);
        if (mMaList.size() != 0) {
            mMax = mMaList.get(mMaList.size() - 1);
            mCanvas = canvas;

            mWidth = mCanvas.getWidth() - RIGHT_MARGIN;
            mHeight = mCanvas.getHeight();
            mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / mMax;
            Log.e(TAG, "mPerHeight=" + mPerHeight);
            mPerWidth = mWidth / (mOverList.size() - 1);
            mPaint.setAntiAlias(true);

            mPaint.setColor(getResources().getColor(R.color.attention_bar_purse));
            mPaint.setStrokeWidth(4);

            //预测线
            for (int i = 0; i < mOverList.size() - 1; i++) {
                mCanvas.drawLine(mPerWidth * i, mHeight - BOTTOM_MARGIN - mPerHeight * mOverList.get(i), mPerWidth * (i + 1), mHeight - BOTTOM_MARGIN - mPerHeight * mOverList.get(i + 1), mPaint);
            }

            mPaint.setColor(getResources().getColor(R.color.attention_bar_red));
            for (int i = 0; i < mMaList.size() - 1; i++) {//机组停运

            }
            mPaint.setColor(Color.BLACK);
            for (int i = 0; i < mMaList.size(); i++) {
                mPaint.setTextSize(20);
                mCanvas.drawText(mDateList.get(i).substring(5), mPerWidth * i, mHeight - BOTTOM_MARGIN / 3, mPaint);
            }

            //画均线
            mPaint.setColor(getResources().getColor(R.color.attention_bar_orange));
            mCanvas.drawLine(0, mHeight - BOTTOM_MARGIN - mAvgValue * mPerHeight, mWidth, mHeight - BOTTOM_MARGIN - mAvgValue * mPerHeight, mPaint);

            //坐标x轴
            mPaint.setStrokeWidth(2);
            mPaint.setColor(Color.BLACK);
            mCanvas.drawLine(0, mHeight - BOTTOM_MARGIN, mWidth, mHeight - BOTTOM_MARGIN, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setStrokeWidth(4);
                float p = mMax / (mHeight - BOTTOM_MARGIN - TOP_MARGIN);
                float height = mHeight - BOTTOM_MARGIN - event.getY();
                float value = p * height;
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String dd = fnum.format(value);

                if (event.getY() > TOP_MARGIN && event.getY() < (mHeight - BOTTOM_MARGIN)) {
                    mPaint.setColor(Color.BLACK);
                    mCanvas.drawLine(0, event.getY(), mWidth, event.getY(), mPaint);
                    mCanvas.drawText(dd, 20, event.getY() - 20, mPaint);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }
}
