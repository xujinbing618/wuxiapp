package com.magus.enviroment.ep.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.CustomItemLayout;

/**
 * 服务中心
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/23-上午11:14.
 */
public class MyServiceCentreActivity extends SwipeBaseActivity {
    private CustomActionBar mActionBar;

    private CustomItemLayout leaveMessage;
    private CustomItemLayout normalQuesstion;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_centre);
        initActionBar();
        initView();
    }

    private void initView() {

        leaveMessage = (CustomItemLayout) findViewById(R.id.service_leave_message);
        leaveMessage.setOnClickListener(clickListener);
        normalQuesstion = (CustomItemLayout) findViewById(R.id.service_normal_question);
        normalQuesstion.setOnClickListener(clickListener);
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.service_leave_message:
                    intent.setClass(MyServiceCentreActivity.this,ServiceLeaveMessageActivity.class);
                    break;

                case R.id.service_normal_question:
                    intent.setClass(MyServiceCentreActivity.this,ServiceNormalQuestionActivity.class);
                    break;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MyServiceCentreActivity.this);
    }
}
