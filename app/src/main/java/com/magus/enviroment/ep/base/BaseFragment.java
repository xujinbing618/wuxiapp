package com.magus.enviroment.ep.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.magus.enviroment.ep.MyApplication;

/**
 * Fragment基类
 * Created by pau on 3/15/15.
 */
public class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected MyApplication mApp;
    protected LayoutInflater mLayoutInflater;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (MyApplication) mActivity.getApplication();
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
}
