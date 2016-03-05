package com.magus.magusutils;

import android.view.View;

/**
 * View操作工具
 * @author 徐健君 2014年11月10日
 *
 */
public class ViewUtil {
	
	/**
	 * 防止控件被连续误点击的实用方法，传入要保护的时间，在此时间内将不可被再次点击
	 * @param v
	 * @param protectionMilliseconds
	 */
	public static void preventViewMultipleClick(final View v, int protectionMilliseconds) {
		v.setClickable(false);
		v.setEnabled(false);
		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				v.setClickable(true);
				v.setEnabled(true);
			}
		},protectionMilliseconds);
	}
}
