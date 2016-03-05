package com.magus.enviroment.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;

/**
 * 自定义我的界面的item
 * Created by pau
 * Packagename com.magus.enviroment.ui
 * 2015-15/4/14-下午3:47.
 */
public class CustomItemLayout extends LinearLayout {

    private LayoutInflater mInflater;

    private View mRootView;

    private TextView mLeftText;

    private TextView mRightText;

    private String mLeftString;

    private String mRightString;

    private ImageView mLeftImage;

    private ImageView mRightImage;

    private Drawable mLeftDrawable;

    private Drawable mRightDrawable;

    public CustomItemLayout(Context context) {
        super(context);
    }

    public CustomItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomMyItemLayout);
        mLeftString = typedArray.getString(R.styleable.CustomMyItemLayout_my_left_text);
        mRightString = typedArray.getString(R.styleable.CustomMyItemLayout_my_right_text);
        mLeftDrawable = typedArray.getDrawable(R.styleable.CustomMyItemLayout_my_left_image);
        mRightDrawable = typedArray.getDrawable(R.styleable.CustomMyItemLayout_my_right_image);
        typedArray.recycle();
        initView(context);
    }

    public CustomItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = mInflater.inflate(R.layout.ui_custom_myitem,this, true);
        mLeftText = (TextView) mRootView.findViewById(R.id.my_left_text);
        mLeftText.setText(mLeftString);
        mRightText = (TextView) mRootView.findViewById(R.id.my_right_text);
        mRightText.setText(mRightString);
        mLeftImage = (ImageView) mRootView.findViewById(R.id.my_left_image);
        if (mLeftDrawable!=null) {
            mLeftImage.setImageDrawable(mLeftDrawable);
        }else {
            mLeftImage.setVisibility(View.GONE);
        }
        mRightImage = (ImageView) mRootView.findViewById(R.id.my_right_image);
        mRightImage.setImageDrawable(mRightDrawable);
    }



    public ImageView getLeftImage() {
        return mLeftImage;
    }

    public ImageView getRightImage() {
        return mRightImage;
    }

    public TextView getLeftText() {
        return mLeftText;
    }

    public TextView getRightText() {
        return mRightText;
    }
}
