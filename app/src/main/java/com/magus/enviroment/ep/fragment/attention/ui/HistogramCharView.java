package com.magus.enviroment.ep.fragment.attention.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.bean.EnterpriseRateInfo;
import com.magus.magusutils.UnitUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 柱状图
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention.ui
 * 2015-15/5/11-上午11:57.
 */
public class HistogramCharView extends BaseHistogramView {
    private static final String TAG = "HistogramCharView";
    private int mEnterpriseNumbers;//企业数量
    private int mAlarmTypeNumbers;//关注的异常类型数量
    //    private int mArrays[][];
    private int mMaxHeight = 100;//最大高度 100%

    private int mRealCanvasWidth;//画布实际宽度
    private int mHeightMeasureSpec;

    private static int PER_RECT_WIDTH = UnitUtil.dip2px(18); //每个长方形宽度
    private static int PER_CITY_SPACE = UnitUtil.dip2px(30); //每个城市间距
    private static int CITY_TEXT_SIZE = UnitUtil.dip2px(8); //城市字体大小

    private Canvas mCanvas;

    private Paint mPaint;

    private List<EnterpriseRateInfo> mRateList = new ArrayList<EnterpriseRateInfo>();
    //紫色 超标报警    红色 机组停运    蓝色  数据缺失     绿色 治污设施停运
    int[] histogramColors = {getResources().getColor(R.color.attention_bar_blue), getResources().getColor(R.color.attention_bar_red),
            getResources().getColor(R.color.attention_bar_green), getResources().getColor(R.color.attention_bar_purse)};

    public HistogramCharView(Context context) {
        super(context);
        init(context);
    }

    public HistogramCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 去锯齿

    }

    /**
     * 设置柱状图数据
     * @param list
     */
    public void setNum(List<EnterpriseRateInfo> list) {
        mRateList = list;
        mEnterpriseNumbers = list.size();
        if (mEnterpriseNumbers > 0) {
            mAlarmTypeNumbers = list.get(0).getDealRates().size();
        }
//        mArrays = arrays;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置大小
//        Log.e(TAG, "重设宽度");
        mHeightMeasureSpec = heightMeasureSpec;
        mRealCanvasWidth = mEnterpriseNumbers * (PER_RECT_WIDTH * mAlarmTypeNumbers + PER_CITY_SPACE);
//        Log.e(TAG, "mRealCanvasWidth=" + mRealCanvasWidth);
        setMeasuredDimension(mRealCanvasWidth > (MyApplication.mWidth - UnitUtil.dip2px(40)) ?
                mRealCanvasWidth : (MyApplication.mWidth - UnitUtil.dip2px(40)), mHeightMeasureSpec);
    }

    private void resetCanvasSize() {
        mRealCanvasWidth = mEnterpriseNumbers * (PER_RECT_WIDTH * mAlarmTypeNumbers + PER_CITY_SPACE);
//        Log.e(TAG, "mRealCanvasWidth=" + mRealCanvasWidth);
        setMeasuredDimension(mRealCanvasWidth > (MyApplication.mWidth - UnitUtil.dip2px(40)) ?
                mRealCanvasWidth : (MyApplication.mWidth - UnitUtil.dip2px(40)), mHeightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //resetCanvasSize();
        mCanvas = canvas;
        mHeight = canvas.getHeight();
        mWidth = canvas.getWidth();
        mPerHeight = (mHeight - BOTTOM_MARGIN - TOP_MARGIN) / mMaxHeight;
        paintHistogram(canvas);
    }

    private void paintHistogram(Canvas canvas) {
        float leftPosition = 0;
        //绘制柱状图
        float textX = 0;
        float textY = mHeight - BOTTOM_MARGIN / 3;
        for (int i = 0; i < mEnterpriseNumbers; i++) {
            mPaint.reset();
            for (int j = 0; j < mAlarmTypeNumbers; j++) {
//                mPaint.setColor(histogramColors[j]);
                mPaint.setAntiAlias(true);
                float clx = PER_RECT_WIDTH * (i * mAlarmTypeNumbers + j) + leftPosition;
                float crx = PER_RECT_WIDTH * (i * mAlarmTypeNumbers + j + 1) + leftPosition;
                int rate = 0;
                String rateString = mRateList.get(i).getDealRates().get(j).getRate();
                try {
                    rate = (int) Double.parseDouble(rateString);
                } catch (Exception e) {

                }
                String code = mRateList.get(i).getDealRates().get(j).getAlarmCode().trim();
//                android.util.Log.e(TAG,"code="+code);
                if ("5".equals(code)) { //机组停运
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_red));
                } else if ("14".equals(code)) {//超标报警
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_purse));//此处应为purple 一开始打错了
                } else if ("7".equals(code)) {//数据缺失
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_blue));
                } else if ("9".equals(code)) {//治污设施停运
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_green));
                }else if ("1".equals(code)) {//超限报警
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_orange));
                }else if ("12".equals(code)) {//数据恒定
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_gray));
                }else if ("1401".equals(code)) {//三倍以上
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_green2));
                }else if ("1402".equals(code)) {//三倍以下
                    mPaint.setColor(getResources().getColor(R.color.attention_bar_yellow));
                }

                canvas.drawRect(clx, mHeight - mPerHeight * rate - BOTTOM_MARGIN, crx, mHeight - BOTTOM_MARGIN, mPaint);
            }
            String cityName = "N/A";
            if (mRateList.get(i).getShortName() != null) {
                cityName = mRateList.get(i).getShortName();
            }
            //写城市
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(CITY_TEXT_SIZE);
            textX = ((PER_RECT_WIDTH * mAlarmTypeNumbers + PER_CITY_SPACE) * i);
            canvas.drawText(cityName, textX, textY, mPaint);
            leftPosition = leftPosition + PER_CITY_SPACE;
        }
        //画底部线
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
//        Log.e(TAG,"mRealWidth="+mRealCanvasWidth+"screenWidth"+MyApplication.mWidth);
        canvas.drawLine(0, mHeight - BOTTOM_MARGIN, mRealCanvasWidth > (MyApplication.mWidth - UnitUtil.dip2px(40)) ? mRealCanvasWidth : (MyApplication.mWidth - UnitUtil.dip2px(40)), mHeight - BOTTOM_MARGIN, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                mCanvas.drawLine(mHeight, event.getY(), 0, event.getY(), mPaint);
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.onTouchEvent(event);

    }
}
