package com.magus.enviroment.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.magus.enviroment.R;

/***
 * 仿IOS开关按钮
 * 在背景值为#f8f8f8下使用，或修改common_slider_mask.png色值
 * 退出activity调用dalloc释放内存
 * 调用：uiSwitch = (UISwitch)findViewById(R.id.uiswitch);
   uiSwitch.setSwitchState(true);
 */
public class CustomSwitchButton extends View implements OnTouchListener {

	//开关开启时的背景，关闭时的背景，滑动按钮
	private Bitmap switch_frame;
	private Bitmap switch_mask;
	private Bitmap switch_bg;
	private Bitmap slip_btn;
	
	//是否正在滑动
	private boolean isSlipping = false;
	//当前开关状态，true为开启，false为关闭
	private boolean isSwitchOn = false;
	
	//手指按下时的水平坐标X，当前的水平坐标X
	private float previousX, currentX;
	
	//开关监听器
	private OnUISwitchDelegate onUISwitchDelegate;
	//是否设置了开关监听器
	private boolean isUISwitchOn = false;
	
	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();

	//滑动按钮的左边坐标
	private float left_SlipBtn = 0;
	
//	private int color;
	
    private static final Xfermode[] sModes = {
        new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
        new PorterDuffXfermode(PorterDuff.Mode.SRC),
        new PorterDuffXfermode(PorterDuff.Mode.DST),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
        new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
        new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
        new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
        new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
        new PorterDuffXfermode(PorterDuff.Mode.XOR),
        new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
        new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
        new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
        new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };
	
	public CustomSwitchButton(Context context) {
		super(context);
		init(context);
	}
	
	
	public CustomSwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	
	private void init(Context ctx) {
//		color = ctx.getResources().getColor(R.color.bg);
		switch_frame = BitmapFactory.decodeResource(getResources(), R.mipmap.common_slider_frame);
		switch_mask = BitmapFactory.decodeResource(getResources(), R.mipmap.common_slider_mask);
		switch_bg = BitmapFactory.decodeResource(getResources(), R.mipmap.common_slider_background);
		slip_btn = BitmapFactory.decodeResource(getResources(), R.mipmap.common_slider_controler);
		setOnTouchListener(this);
	}

	public void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
	}
	
	
	public boolean getSwitchState() {
		return isSwitchOn;
	}
	
	
	public void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//判断当前是否正在滑动
		if(isSlipping) {
			if(currentX > switch_frame.getWidth()) {
				left_SlipBtn = 0;
			} else {
				left_SlipBtn = currentX - slip_btn.getWidth() / 2;
			}
		} else {
			//根据当前的开关状态设置滑动按钮的位置
			if(isSwitchOn) {
				left_SlipBtn = 0;
			} else {
				left_SlipBtn = switch_frame.getWidth() - slip_btn.getWidth();
			}
		}
		
		//对滑动按钮的位置进行异常判断
		if(left_SlipBtn > 0) {
			left_SlipBtn = 0;
		} else if(left_SlipBtn < (switch_frame.getWidth() - slip_btn.getWidth())){
			left_SlipBtn = switch_frame.getWidth() - slip_btn.getWidth();
		}

        int sc = canvas.saveLayer(0, 0, switch_frame.getWidth(), switch_frame.getHeight(), null,
                                  Canvas.MATRIX_SAVE_FLAG |
                                  Canvas.CLIP_SAVE_FLAG |
                                  Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                                  Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                                  Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        
		canvas.drawBitmap(switch_bg, left_SlipBtn, 0, paint);

//        paint.setStyle(Paint.Style.FILL);
//        paint.setShader(null);
//		paint.setColor(color);
		//paint.setXfermode(sModes[8]);
        
		paint.setXfermode(sModes[6]);
		canvas.drawBitmap(switch_mask, matrix, paint);
        paint.setXfermode(null);
		canvas.drawBitmap(switch_frame, matrix, paint);
		canvas.drawBitmap(slip_btn, left_SlipBtn, 0, paint);
        canvas.restoreToCount(sc);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(switch_frame.getWidth(), switch_frame.getHeight());
	}

	
	boolean lastSwitchState = false;//移动过程最终停留的开关处
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
  
		switch(event.getAction()) {
		//滑动
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX(); 
			if(event.getX() >= (switch_frame.getWidth() / 2)) {
				lastSwitchState = true; 
			} else {
				lastSwitchState = false; 
			} 
			break;
			
		//按下
		case MotionEvent.ACTION_DOWN:
			if(event.getX() > switch_frame.getWidth() || event.getY() > switch_frame.getHeight()) {
				return false;
			}
			
			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;
		
		//松开
		case MotionEvent.ACTION_CANCEL: 
			isSlipping = false; 
			isSwitchOn = lastSwitchState ; 
			//如果设置了监听器，则调用此方法
			if(isUISwitchOn) { 
				onUISwitchDelegate.onUISwitchDelegate(isSwitchOn);
			} 
			break;
		case MotionEvent.ACTION_UP: 
			isSlipping = false;
			//松开前开关的状态 
			if(Math.abs(previousX-event.getX())<5){//移动在很小范围可以视为是点击操作。
				isSwitchOn=!isSwitchOn;
				//如果设置了监听器，则调用此方法
				if(isUISwitchOn) { 
					onUISwitchDelegate.onUISwitchDelegate(isSwitchOn);
				}
				break;
			} 
			
			isSwitchOn = lastSwitchState;
			//如果设置了监听器，则调用此方法
			if(isUISwitchOn ) {  
				onUISwitchDelegate.onUISwitchDelegate(isSwitchOn);
			} 
			break;
		
		default:
			break;
		}
		
		//重新绘制控件
		invalidate();
		return true;
	}

	public void setOnUISwitchDelegate(OnUISwitchDelegate _delegate) {
		onUISwitchDelegate = _delegate;
		isUISwitchOn = true;
	}
	
	public interface OnUISwitchDelegate {
		abstract void onUISwitchDelegate(boolean isSwitchOn);
	}
	
	/**
	 * 释放内存
	 */
	public void dalloc() {
		// 回收Bitmap资源  
        if (switch_frame != null && !switch_frame.isRecycled()) {  
        	switch_frame.recycle();  
        	switch_frame = null;  
        }
        

        if (switch_bg != null && !switch_bg.isRecycled()) {  
        	switch_bg.recycle();  
        	switch_bg = null;  
        }
        

        if (slip_btn != null && !slip_btn.isRecycled()) {  
        	slip_btn.recycle();  
        	slip_btn = null;  
        }
        
        paint = null;
        matrix = null;
	}
}
