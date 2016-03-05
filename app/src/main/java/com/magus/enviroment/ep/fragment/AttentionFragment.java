package com.magus.enviroment.ep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.activity.attention.AlarmOverActivity;
import com.magus.enviroment.ep.activity.attention.AlarmPicActivity;
import com.magus.enviroment.ep.activity.attention.AlarmSixActivity;
import com.magus.enviroment.ep.activity.attention.AlarmTotalActivity;
import com.magus.enviroment.ep.activity.attention.AttentionAbnormalReportActivity;
import com.magus.enviroment.ep.activity.attention.AttentionOverReportActivity;
import com.magus.enviroment.ep.activity.attention.OverGridCheckActivity;
import com.magus.enviroment.ep.activity.attention.OverMonitoringActivity;
import com.magus.enviroment.ep.activity.attention.OverStatisticsActivity;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.magusutils.SharedPreferenceUtil;

/**
 * 关注页面
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment
 * 2015-15/5/10-上午9:29.
 */
public class AttentionFragment extends BaseFragment {
    private CustomActionBar mActionBar;





    private View mRootView;
    private ImageView[] mButtons;
    private int[] mButtonIDs = {R.id.attention_abnormal_report,
            R.id.attention_over_report, R.id.alarm_over, R.id.alarm_six, R.id.alarm_pic,
            R.id.overview_statistics, R.id.overview_grid_check, R.id.overview_total_monitoring, R.id.alarm_total};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_attention, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initView() {
        initActionBar();
        mButtons = new ImageView[mButtonIDs.length];
        for (int i = 0; i < mButtonIDs.length; i++) {
            mButtons[i] = (ImageView) mRootView.findViewById(mButtonIDs[i]);
            mButtons[i].setOnClickListener(onClickListener);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.overview_statistics:
                    //异常统计
                    if ("true".equals(SharedPreferenceUtil.get("gn_0001", "false"))) {
                        //startNewActivity(OverStatisticsActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.overview_grid_check:
                    //网格考核
                    if ("true".equals(SharedPreferenceUtil.get("gn_0002", "false"))) {
                        startNewActivity(OverGridCheckActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.overview_total_monitoring:
                    //总量监控
                    if ("true".equals(SharedPreferenceUtil.get("gn_0003", "false"))) {
                        startNewActivity(OverMonitoringActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.attention_abnormal_report:
                    //工况异常报告
                    if ("true".equals(SharedPreferenceUtil.get("gn_0008", "false"))) {
                        startNewActivity(AttentionAbnormalReportActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.attention_over_report:
                    //超标报告
                    if ("true".equals(SharedPreferenceUtil.get("gn_0009", "false"))) {
                        startNewActivity(AttentionOverReportActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.alarm_over:
                    //超标预警
                    if ("true".equals(SharedPreferenceUtil.get("gn_0004", "false"))) {
                        startNewActivity(AlarmOverActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.alarm_six:
                    //六类报警
                    if ("true".equals(SharedPreferenceUtil.get("gn_0005", "false"))) {
                        startNewActivity(AlarmSixActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.alarm_total:
                    //总量预警
                    if ("true".equals(SharedPreferenceUtil.get("gn_0007", "false"))) {
                        startNewActivity(AlarmTotalActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.alarm_pic:
                    //自定义报警
                    if ("true".equals(SharedPreferenceUtil.get("gn_0006", "false"))) {
                        startNewActivity(AlarmPicActivity.class);
                    } else {

                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case R.id.overview_statistics:
//                    //异常统计
//                    if (MyApplication.isGn_0001()) {
//                        startNewActivity(OverStatisticsActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.overview_grid_check:
//                    //网格考核
//                    if (MyApplication.isGn_0002()) {
//                        startNewActivity(OverGridCheckActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.overview_total_monitoring:
//                    //总量监控
//                    if (MyApplication.isGn_0003()) {
//                        startNewActivity(OverMonitoringActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.attention_abnormal_report:
//                    //工况异常报告
//                    if (MyApplication.isGn_0008()) {
//                        startNewActivity(AttentionAbnormalReportActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.attention_over_report:
//                    //超标报告
//                    if (MyApplication.isGn_0009()) {
//                        startNewActivity(AttentionOverReportActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.alarm_over:
//                    //超标预警
//                    if (MyApplication.isGn_0004()) {
//                        startNewActivity(AlarmOverActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.alarm_six:
//                    //六类报警
//                    if (MyApplication.isGn_0005()) {
//                        startNewActivity(AlarmSixActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.alarm_total:
//                    //总量预警
//                    if (MyApplication.isGn_0007()) {
//                        startNewActivity(AlarmTotalActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//
//                    break;
//                case R.id.alarm_pic:
//                    //自定义报警
//                    if (MyApplication.isGn_0006()) {
//                        startNewActivity(AlarmPicActivity.class);
//                    } else {
//                        Toast.makeText(getActivity(), "对不起，您没有权限", Toast.LENGTH_SHORT).show();
//                    }
//                    break;

            }
        }
    };
    private void initActionBar() {
        mActionBar = (CustomActionBar) mRootView.findViewById(R.id.custom_action_bar);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
    private void startNewActivity(Class<?> cls) {

        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
