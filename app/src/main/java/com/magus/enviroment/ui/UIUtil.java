package com.magus.enviroment.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 提示工具类
 * Created by pau on 15/7/8.
 */
public class UIUtil {
    public static void toast(final Context context, final String msg) {

        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 进度条dialog
     * @param context
     * @param tips
     * @return
     */
    public static ProgressDialog initDialog(Context context, String tips) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
