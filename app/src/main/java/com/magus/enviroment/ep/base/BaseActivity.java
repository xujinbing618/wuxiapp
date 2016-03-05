package com.magus.enviroment.ep.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.global.log.Log;

/**
 * Activity基类
 * Created by pau on 3/15/15.
 */
public class BaseActivity extends ActionBarActivity {
    protected MyApplication mApp;
    public static boolean IS_PAD = false ;//判断是否为pad
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (MyApplication)getApplication();
    }

    private void setScreenOrientation(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels>dm.heightPixels){
            Log.d("TAG", "sss");
            IS_PAD = true ;
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


}
