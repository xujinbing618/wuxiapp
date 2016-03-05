package com.magus.enviroment.ep.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.CustomItemLayout;

/**
 * 我的关注
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/23-上午11:13.
 */
public class MyAttentionActivity extends SwipeBaseActivity {
    private CustomItemLayout unusualType;
    private CustomItemLayout pollutionSource;
    private CustomActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MyAttentionActivity.this);
    }

    private void initView() {
        unusualType = (CustomItemLayout) findViewById(R.id.attention_unusual_type);
        unusualType.setOnClickListener(onClickListener);
        pollutionSource = (CustomItemLayout) findViewById(R.id.attention_pollution_source);
        pollutionSource.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.attention_unusual_type:
                    startNewActivity(AttentionAlarmTypeActivity.class);
                    break;
                case R.id.attention_pollution_source:

                    startNewActivity(AttentionPollutionCityActivity.class);
//                    if (MyApplication.mRolePagerInfos.contains("3210")) {//权限判断
//                    } else {
//                        UIUtil.toast(MyAttentionActivity.this, "您暂未获得该权限");
//                    }
                    break;
            }
        }
    };


    private void startNewActivity(Class<?> cls) {

        Intent intent = new Intent();
        intent.setClass(MyAttentionActivity.this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
