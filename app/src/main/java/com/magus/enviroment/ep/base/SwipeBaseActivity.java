package com.magus.enviroment.ep.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.base
 * 2015-15/5/7-下午4:58.
 */
public class SwipeBaseActivity extends SwipeBackActivity {
    protected MyApplication mApp;
    protected LayoutInflater mLayoutInflater;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (MyApplication)getApplication();
        mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
