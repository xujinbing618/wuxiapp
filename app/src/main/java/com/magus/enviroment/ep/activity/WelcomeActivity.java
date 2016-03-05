package com.magus.enviroment.ep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.base.BaseActivity;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.magusutils.SharedPreferenceUtil;

/**
 * Created by pau
 * Packagename com.magus.enviroment
 * 2015-15/4/16-上午10:09.
 */
public class WelcomeActivity extends BaseActivity {
    private long mShowTime;
    private static final String TAG= "WelcomeActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        sendRequest();

        if (SharedPreferenceUtil.getBoolean(SpKeyConstant.IS_FIRST_IN,true)){
            mShowTime=3000;
            SharedPreferenceUtil.saveBoolean(SpKeyConstant.IS_FIRST_IN,false);
        }else {
            mShowTime=0;
        }
        handler.sendEmptyMessageDelayed(0, mShowTime);//
    }

    /**
     * 跳转控制handler
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://启动activity
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();

//                    Log.e(TAG, "跳转");
                    break;
            }
        }

        ;
    };

}
