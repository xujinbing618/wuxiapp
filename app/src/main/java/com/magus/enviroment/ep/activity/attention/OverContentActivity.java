package com.magus.enviroment.ep.activity.attention;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ui.CustomActionBar;

/**
 * 超标预警详情
 */
public class OverContentActivity extends SwipeBaseActivity {
    private static final String TAG = "PicContentActivity";

    private TextView over_content_facilities_type_name;
    private TextView over_content_pollutant_name;
    private TextView over_content_fact_out_nd;

    private CustomActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_content);

        initActionBar();
        initView();
        initData();

    }
    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            over_content_facilities_type_name.setText(intent.getStringExtra("pollSourceName"));
            over_content_pollutant_name.setText(intent.getStringExtra("pollutantName"));
            over_content_fact_out_nd.setText(intent.getStringExtra("fact_out_nd"));
        }

    }

    private void initView() {
        over_content_facilities_type_name=(TextView)findViewById(R.id.over_content_facilities_type_name);
        over_content_pollutant_name=(TextView)findViewById(R.id.over_content_pollutant_name);
        over_content_fact_out_nd=(TextView)findViewById(R.id.over_content_fact_out_nd);

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(OverContentActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
