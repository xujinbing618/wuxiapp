//package com.magus.enviroment.ep.fragment.attention;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.magus.enviroment.R;
//import com.magus.enviroment.ep.activity.attention.AttentionAbnormalReportActivity;
//import com.magus.enviroment.ep.activity.attention.AttentionOverReportActivity;
//import com.magus.enviroment.ep.activity.attention.OverGridCheckActivity;
//import com.magus.enviroment.ep.activity.attention.OverMonitoringActivity;
//import com.magus.enviroment.ep.activity.attention.OverStatisticsActivity;
//import com.magus.enviroment.ep.activity.attention.TotalMonitoringActivity;
//import com.magus.enviroment.ep.base.BaseFragment;
//import com.magus.enviroment.ui.UIUtil;
//
///**
// * 总览
// * Created by pau on 15/7/21.
// */
//public class OverViewFragment extends BaseFragment {
//    private View mRootView;
//    private ImageView[] mButtons;
//    private int[] mButtonIDs = {R.id.overview_statistics, R.id.overview_grid_check, R.id.overview_total_monitoring};
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(R.layout.fragment_attention_overview, null);
//            initView();
//        }
//        ViewGroup parent = (ViewGroup) mRootView.getParent();
//        if (parent != null) {
//            parent.removeView(mRootView);
//        }
//        return mRootView;
//    }
//
//    private void initView() {
//        mButtons = new ImageView[mButtonIDs.length];
//        for (int i = 0; i < mButtonIDs.length; i++) {
//            mButtons[i] = (ImageView) mRootView.findViewById(mButtonIDs[i]);
//            mButtons[i].setOnClickListener(onClickListener);
//
//        }
//    }
//
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.overview_statistics:
//                    startNewActivity(OverStatisticsActivity.class);
//                    break;
//                case R.id.overview_grid_check:
//                    startNewActivity(OverGridCheckActivity.class);
//                    break;
//                case R.id.overview_total_monitoring:
//                    startNewActivity(TotalMonitoringActivity.class);
//                    break;
//            }
//        }
//    };
//
//    private void startNewActivity(Class<?> cls) {
//
//        Intent intent = new Intent();
//        intent.setClass(mActivity, cls);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
//}
