package com.magus.magusutils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 动画效果类
 * Author huarizhong
 * Date 2015/1/6
 * Time 9:20
 */
public class AnimationUtil {
    /**
     * 动画淡入效果
     * @param image 需要进行动画效果的控件
     * @param drawable  所用图片
     */
    public static void setModeImage(ImageView image, Drawable drawable) {
        AnimationSet animationSet = new AnimationSet(true);
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //设置动画执行的时间
        alphaAnimation.setDuration(500);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        //使用ImageView的startAnimation方法执行动画
        image.startAnimation(animationSet);
        image.setImageDrawable(drawable);
    }

    /**
     * 视图淡出
     * @param view 需要进行动画效果的view
     */
    public static void viewFadeOut(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation;
        alphaAnimation = new AlphaAnimation(1, 0);
        //设置动画执行的时间
        alphaAnimation.setDuration(500);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        //使用ImageView的startAnimation方法执行动画
        view.startAnimation(animationSet);
        view.setVisibility(View.GONE);
    }

    /**
     * 视图淡入
     * @param view 需要进行动画效果的view
     */
    public static void viewFadeIn(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation;
        alphaAnimation = new AlphaAnimation(0,1);
        //设置动画执行的时间
        alphaAnimation.setDuration(500);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        //使用ImageView的startAnimation方法执行动画
        view.startAnimation(animationSet);
    }


    public static void TopIn(View view){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation;
        translateAnimation= new TranslateAnimation(0,0,-view.getHeight(),0);
        translateAnimation.setDuration(500);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);

    }

    public static void TopOut(View view){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation;
        translateAnimation= new TranslateAnimation(0,0,0,-view.getHeight());
        translateAnimation.setDuration(500);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
    }
}
