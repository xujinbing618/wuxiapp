package com.magus.enviroment.ep.fragment.attention;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.activity.attention.AlarmOverActivity;
import com.magus.enviroment.ep.activity.attention.AlarmPicActivity;
import com.magus.enviroment.ep.activity.attention.AlarmSixActivity;
import com.magus.enviroment.ep.activity.attention.AlarmTotalActivity;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AlarmTotal;
import com.magus.enviroment.ui.UIUtil;

/**
 * 报警
 * Created by pau on 15/7/21.
 */
public class AlarmFragment extends BaseFragment {
    private View mRootView;
    private ImageView[] mButtons;
    private int[] mButtonIDs = {R.id.alarm_six, R.id.alarm_total};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_attention_alarm, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initView() {
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
//                case R.id.alarm_over:
//                    startNewActivity(AlarmOverActivity.class);
//                    break;
                case R.id.alarm_six:
                    startNewActivity(AlarmSixActivity.class);
                    break;
//                case R.id.alarm_pic:
//                   // UIUtil.toast(getActivity(),"此功能未开放!");
//                    startNewActivity(AlarmPicActivity.class);
//                    break;
                case  R.id.alarm_total:
                    startNewActivity(AlarmTotalActivity.class);
                    break;
            }
        }
    };

    private void startNewActivity(Class<?> cls) {

        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
