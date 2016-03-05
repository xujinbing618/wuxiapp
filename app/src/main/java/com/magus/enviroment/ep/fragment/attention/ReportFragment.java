//package com.magus.enviroment.ep.fragment.attention;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.magus.enviroment.R;
//import com.magus.enviroment.ep.MyApplication;
//import com.magus.enviroment.ep.activity.attention.AttentionAbnormalReportActivity;
//import com.magus.enviroment.ep.activity.attention.AttentionAddReportActivity;
//import com.magus.enviroment.ep.activity.attention.AttentionOverReportActivity;
//import com.magus.enviroment.ep.base.BaseFragment;
//import com.magus.enviroment.global.log.Log;
//import com.magus.enviroment.ui.UIUtil;
//
///**
// * 报告
// * Created by pau
// * Packagename com.magus.enviroment.ep.fragment.attention
// * 2015-15/5/10-上午9:44.
// */
//public class ReportFragment extends BaseFragment {
//
//    private View mRootView;
//
//    private ImageView[] mReportTypeButtons;
//    private int[] mReportTypeIDs = {R.id.attention_abnormal_report, R.id.attention_over_report, R.id.attention_add_report};
//
//    private GridView ReportTypeGrid;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(R.layout.fragment_attention_report, null);
//            initView();
//        }
//        ViewGroup parent = (ViewGroup) mRootView.getParent();
//        if (parent != null) {
//            parent.removeView(mRootView);
//        }
//
//        return mRootView;
//    }
//
//    private void initView() {
//        mReportTypeButtons = new ImageView[mReportTypeIDs.length];
//        for (int i = 0; i < mReportTypeIDs.length; i++) {
//            mReportTypeButtons[i] = (ImageView) mRootView.findViewById(mReportTypeIDs[i]);
//            mReportTypeButtons[i].setOnClickListener(onClickListener);
//
//        }
////        ReportTypeGrid = (GridView) mRootView.findViewById(R.id.attention_report_type_grid);
//    }
//
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()) {
//
//                case R.id.attention_abnormal_report:
//                  //  if (MyApplication.mRolePagerInfos.contains("120")) {//权限判断
//                        startNewActivity(AttentionAbnormalReportActivity.class);
////                    } else {
////                        UIUtil.toast(mActivity, "您暂未获得该权限");
////                    }
//                    break;
//                case R.id.attention_over_report:
//                   // if (MyApplication.mRolePagerInfos.contains("121")) { //权限判断
//                        startNewActivity(AttentionOverReportActivity.class);
////                    } else {
////                        UIUtil.toast(mActivity, "您暂未获得该权限");
////                    }
//                    break;
////                case R.id.attention_add_report:
////                    startNewActivity(AttentionAddReportActivity.class);
////                    break;
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
//
//}
