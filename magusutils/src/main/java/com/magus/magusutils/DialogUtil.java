package com.magus.magusutils;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 *
 * 对话框工具类
 * 
 */
public class DialogUtil {

    private static volatile WeakReference<PopupWindow> refWindow;
    //是否正在显示过程中
    private static volatile boolean isShowing;

	/**
	 * 显示全屏朦皮
	 * 
	 * @param context
	 * @param drawableResourceId
	 *            要显示的朦皮的resourceId
	 */
	public static void showVeilPicture(final Activity context, int drawableResourceId) {

		try {
            //如果已经被取消，就不再显示
            if(!isShowing){
                return;
            }
			LinearLayout content = new LinearLayout(context);
			final PopupWindow window = new PopupWindow(context);
			window.setOutsideTouchable(true);
			window.setFocusable(true);
			window.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
			window.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
			window.setContentView(content);
			window.setBackgroundDrawable(context.getResources().getDrawable(drawableResourceId));
			window.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
			/*
			 * //校准顶部位置 Window parentWindow=context.getWindow(); Rect r = new
			 * Rect(); parentWindow.getDecorView().getRootView().
			 * getWindowVisibleDisplayFrame(r); int contentViewTop=r.top;
			 * content.setPadding(0, contentViewTop, 0, 0); View v=new
			 * View(context); v.setBackgroundColor(0xFF0000FF);
			 * v.setLayoutParams(new
			 * android.view.ViewGroup.LayoutParams(126,126));
			 * content.addView(v);
			 */
			// ----
			window.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					window.dismiss();
					return true;
				}
			});
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    isShowing=false;
                    refWindow.clear();
                }
            });
            //加入全局
            refWindow=new WeakReference<PopupWindow>(window);
		} catch (Exception e) {
			//ignore
		}
	}

	/**
	 * 只会显示一次的全屏朦皮，朦皮以键值keyname来区分
	 * 
	 * @param context
	 * @param drawableResourceId
	 *            要显示的朦皮的resourceId
	 * @param keyname
	 *            用来标识朦皮的唯一名称
	 */
	public static void showVeilPictureOnce(final Activity context, final int drawableResourceId, final String keyname) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (SharedPreferenceUtil.getBoolean(keyname, true)) {
                        isShowing=true;
						SystemClock.sleep(500);//延时保护
						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								try {

									DialogUtil.showVeilPicture(context, drawableResourceId);
                                    SharedPreferenceUtil.saveBoolean(keyname, false);
								} catch (Exception ex) {
									Log.e("showVeilPictureOnce " + keyname, ex.getMessage(), ex);
								}
							}
						});
					}
				} catch (Exception ex) {
					Log.e("showVeilPictureOnce " + keyname, ex.getMessage(), ex);
				}

			}

		}).start();
	}

    public static void hideVeilPicture(){
        isShowing=false;
        if(refWindow==null){
            return;
        }
        PopupWindow window=refWindow.get();
        if(window!=null && window.isShowing()){
            window.dismiss();
        }
    }
	/**
	 * 显示一个简单的列表对话框，可以点击其中一个项。只有取消按钮
	 */
	public static void showListDialog(Activity context, String title, String[] items, OnClickListener listener) {
		// -------对话框------
		Builder bd = new Builder(context);

		// 设置title
		bd.setTitle(title);
		bd.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		bd.setItems(items, listener);
		bd.show();
	}
	/**
	 * 显示一个简单对话框，提示用户一段文字，只有关闭按钮
	 */
	public static void showAlertDialog(Activity context, String title, String msg) {
		Builder bd = new Builder(context);

		// 设置title
		bd.setTitle(title);
        bd.setMessage(msg);
		bd.setNegativeButton("关闭", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		bd.show();
	}
	/**
	 * 显示一个简单对话框，提示用户确认信息，可以点击确认或者取消按钮
	 */
	public static void showConfirmDialog(Activity context, String title, String msg, final OnClickListener okButtonListener,final OnClickListener cancelButtonListener) {
		Builder bd = new Builder(context);

		// 设置title
		bd.setTitle(title);
        bd.setMessage(msg);
		bd.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                okButtonListener.onClick(dialog, which);
				dialog.dismiss();
			}
		});
		bd.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                cancelButtonListener.onClick(dialog, which);
				dialog.dismiss();
			}
		});
		bd.show();
	}

    public static void showToast(String msg){
        Toast.makeText(ContextUtil.context, msg, Toast.LENGTH_LONG).show();
    }
}
