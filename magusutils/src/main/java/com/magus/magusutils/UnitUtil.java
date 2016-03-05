package com.magus.magusutils;
import android.content.Context;


/**
 * dip 与像素的相互转换
 * @author joe.xu, 2014-3-16
 */
public class UnitUtil {


	/**将dip转换为像素
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        return dip2px(ContextUtil.context, dipValue);
    }

    /**将dip转换为像素
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

	/**
	 * 将像素转换为dip
	 * @param pxvalue
	 * @return
	 */
	public static float px2dip(int pxvalue) {
        return px2dip(ContextUtil.context, pxvalue);
	}

    /**
     * 将像素转换为dip
     * @param pxvalue
     * @return
     */
    public static float px2dip(Context context, int pxvalue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxvalue/scale;
    }

    /**
     * 获取屏幕高度像素
     * @return
     */
    public static int getScreenHeight(){
        return ContextUtil.context.getResources().getDisplayMetrics().heightPixels;
    }

}
