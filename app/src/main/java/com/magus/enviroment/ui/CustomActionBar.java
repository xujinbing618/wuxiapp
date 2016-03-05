package com.magus.enviroment.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magus.enviroment.R;

public class CustomActionBar extends LinearLayout {

    private View mRootView;

    private LayoutInflater mInflater;

    private ImageView mLeftImage;//左图

    private ImageView mRightImage;//右图

    private TextView mLeftText;//左边字

    private TextView mRightText;//右边字

    private TextView mMiddleText;//中间的字

    private RelativeLayout mActionBarLayout;//整体布局

    private String mLeftString;

    private String mRightString;

    private String mMiddleString;

    private Drawable mLeftDrawable;

    private Drawable mRightDrawable;

    public CustomActionBar(Context context) {
        super(context);
    }

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar);
        mLeftString = typedArray.getString(R.styleable.CustomActionBar_left_text);
        mRightString = typedArray.getString(R.styleable.CustomActionBar_right_text);
        mLeftDrawable = typedArray.getDrawable(R.styleable.CustomActionBar_left_image);
        mRightDrawable = typedArray.getDrawable(R.styleable.CustomActionBar_right_image);
        mMiddleString=typedArray.getString(R.styleable.CustomActionBar_middle_text);
        typedArray.recycle();
        initView(context);


    }

    public CustomActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化
    private void initView(Context context){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = mInflater.inflate(R.layout.ui_custom_actionbar,this,true);
        mRightImage = (ImageView) mRootView.findViewById(R.id.right_image);
        mLeftImage= (ImageView) mRootView.findViewById(R.id.left_image);
        if (mLeftDrawable!=null){
            mLeftImage.setImageDrawable(mLeftDrawable);
        }else {
            mLeftImage.setVisibility(View.GONE);
        }
        mRightText = (TextView) mRootView.findViewById(R.id.right_text);
        mLeftText = (TextView) mRootView.findViewById(R.id.left_text);
        mMiddleText = (TextView) mRootView.findViewById(R.id.middle_text);
        mLeftText.setText(mLeftString);
        mRightText.setText(mRightString);
        mMiddleText.setText(mMiddleString);
        mActionBarLayout = (RelativeLayout) mRootView.findViewById(R.id.custom_action_bar_layout);
    }

    public ImageView getLeftImageView(){
        return mLeftImage;
    }
    public ImageView getRightImageView(){
        return mRightImage;
    }
    public TextView getLeftTextView(){
        return mLeftText;
    }
    public TextView getRightTextView(){
        return mRightText;
    }
    public TextView getMiddleTextView(){

        return mMiddleText;
    }
    public RelativeLayout getActionBarLayout(){
        return mActionBarLayout;
    }

    /**
     * 设置actionbar背景色
     * @param color
     */
    public void setActionBarBackground(int color){
        mActionBarLayout.setBackgroundColor(color);
    }

    private Activity mActivity;
    public void setLeftImageClickListener(final Activity activity){
        mActivity = activity;
        mLeftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
                mActivity=null;
            }
        });
    }
}
