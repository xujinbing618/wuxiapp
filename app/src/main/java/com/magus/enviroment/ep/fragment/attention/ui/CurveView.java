package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.ZoneDealRate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui
 * 2015-15/5/14-下午6:57.
 */
public class CurveView extends BaseHistogramView {

    private int mMax = 100; //数组中最大值
    private int mMin = 0; //坐标起始值
    private float mPerHeight; //每单位机子的高度
    private float mPerWidth;
    private int[] arrays = {100, 2, 18, 3, 17, 2, 24, 3, 18, 2, 22, 9, 30, 3, 18, 1, 29, 4, 11, 2, 29, 2, 29,
            1, 12, 4, 23, 7, 29, 2};
    private int[] arrays2 = {100, 2, 18, 3, 17, 22, 24, 33, 18, 12, 22, 39, 30, 43, 18, 51, 29, 54, 11, 52, 29, 52, 29,
            1, 12, 4, 23, 7, 29, 2};
    private int[] arrays3 = {100, 12, 8, 13, 17, 12, 24, 23, 18, 32, 22, 39, 30, 43, 18, 21, 29, 14, 11, 32, 29, 22, 29,
            1, 12, 4, 23, 7, 29, 2};
    private int[] arrays4 = {100, 2, 18, 53, 17, 2, 24, 63, 18, 34, 22, 9, 30, 63, 18, 31, 29, 34, 11, 72, 29, 52, 29,
            1, 12, 4, 23, 7, 29, 2};
    private Paint mPaint;

    private List<ZoneDealRate> mOverList = new ArrayList<ZoneDealRate>();
    private List<ZoneDealRate> mStopList = new ArrayList<ZoneDealRate>();
    private List<ZoneDealRate> mLostList = new ArrayList<ZoneDealRate>();
    private List<ZoneDealRate> mStop2List = new ArrayList<ZoneDealRate>();




    public CurveView(Context context) {
        super(context);
        init(context);
    }

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //overList, stopList, lostList, stop2List
    public void setList(List<ZoneDealRate> overList, List<ZoneDealRate> stopList, List<ZoneDealRate> lostList, List<ZoneDealRate> stop2List) {
        this.mOverList = overList;
        this.mStopList = stopList;
        this.mLostList = lostList;
        this.mStop2List = stop2List;
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
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / mMax;
        mPerWidth = mWidth / (mOverList.size() - 1);
        mPaint.setAntiAlias(true);



        mPaint.setColor(getResources().getColor(R.color.attention_bar_purse));
        mPaint.setStrokeWidth(2);
        for (int i = 0; i < mOverList.size() - 1; i++) {//超标报警
            canvas.drawLine(mPerWidth * i, mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mOverList.get(i).getRate()) * mPerHeight,
                    mPerWidth * (i + 1), mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mOverList.get(i+1).getRate()) * mPerHeight, mPaint);
        }
        mPaint.setColor(getResources().getColor(R.color.attention_bar_red));
        for (int i = 0; i < mStopList.size() - 1; i++) {//机组停运
            canvas.drawLine(mPerWidth * i, mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mStopList.get(i).getRate()) * mPerHeight,
                    mPerWidth * (i + 1), mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mStopList.get(i+1).getRate()) * mPerHeight, mPaint);
        }
        mPaint.setColor(getResources().getColor(R.color.attention_bar_blue));
        for (int i = 0; i < mLostList.size() - 1; i++) {//数据缺失
            canvas.drawLine(mPerWidth * i, mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mLostList.get(i).getRate()) * mPerHeight,
                    mPerWidth * (i + 1), mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mLostList.get(i+1).getRate()) * mPerHeight, mPaint);
        }
        mPaint.setColor(getResources().getColor(R.color.attention_bar_green));
        for (int i = 0; i < mStop2List.size() - 1; i++) {//治污设施停运
            canvas.drawLine(mPerWidth * i, mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mStop2List.get(i).getRate()) * mPerHeight,
                    mPerWidth * (i + 1), mHeight - BOTTOM_MARGIN - (int)Double.parseDouble(mStop2List.get(i+1).getRate()) * mPerHeight, mPaint);
        }

        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, mHeight - BOTTOM_MARGIN, mWidth, mHeight - BOTTOM_MARGIN, mPaint);
    }
}
