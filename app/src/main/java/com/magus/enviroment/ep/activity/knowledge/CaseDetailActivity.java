package com.magus.enviroment.ep.activity.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;

/**
 * 执法案例详情
 */
public class CaseDetailActivity extends SwipeBackActivity {
    private CustomActionBar customActionBar;

    public static final String KEY_CASE_REMARK="KEY_CASE_REMARK";
    private TextView mTv_Detail;
    private String mRemarkDetail;
    private int width;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        initActionBar();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        width = dm.widthPixels;


        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent!=null){
               Bundle bundle=intent.getExtras();
                if(bundle!=null){
                    mRemarkDetail = bundle.getString(KEY_CASE_REMARK);

                }

        }
    }

    private void initView() {
        mTv_Detail = (TextView) findViewById(R.id.tv_detail);
        //根据屏幕调整文字大小
        mTv_Detail.setLineSpacing(0f, 1.5f);
        mTv_Detail.setTextSize(8 * (float) width / 320f);
        mTv_Detail.setText("\t" + mRemarkDetail);
//        TextJustification.justify(mTv_Detail,mTv_Detail.getWidth());
    }


    private void initActionBar() {
        customActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        customActionBar.setLeftImageClickListener(CaseDetailActivity.this);
        customActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

}
