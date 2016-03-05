package com.magus.enviroment.ep.activity.attention;

import android.os.Bundle;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ui.CustomActionBar;

/**
 * 添加报告
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.attention
 * 2015-15/5/10-下午1:08.
 */
public class AttentionAddReportActivity extends SwipeBaseActivity {

    private CustomActionBar mActionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_add_report);
        initActionBar();
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionAddReportActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
