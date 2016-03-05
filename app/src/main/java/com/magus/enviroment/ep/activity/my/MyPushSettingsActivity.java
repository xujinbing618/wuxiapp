package com.magus.enviroment.ep.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.service.MessagePushService;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.CustomSwitchButton;
import com.magus.magusutils.SharedPreferenceUtil;

/**
 * 操作日志
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/24-下午3:36.
 */
public class MyPushSettingsActivity extends SwipeBaseActivity {
    private CustomActionBar mActionBar;
    private CustomSwitchButton fixedTime;//定时推送
    private CustomSwitchButton realTime;//实时推送
    private CheckBox checkBox;//设置是否离线推送
    private Spinner spinner;//设置定时推送时间
    private String second = "10";//实时推送时间

    //private CustomSwitchButton noPush;//设置是否离线推送
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_push_settings);
        initActionBar();
        initView();
        initData();
    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.spinner_push_fixed);
        fixedTime = (CustomSwitchButton) findViewById(R.id.push_fixed_time);
        realTime = (CustomSwitchButton) findViewById(R.id.push_real_time);
        realTime.updateSwitchState(false);

        checkBox = (CheckBox) findViewById(R.id.check_push_setting);
        //  noPush=(CustomSwitchButton) findViewById(R.id.push_no);
        if (SharedPreferenceUtil.get("Time", "10").equals(second)) {
            realTime.updateSwitchState(true);
            fixedTime.updateSwitchState(false);

        } else {
            fixedTime.updateSwitchState(true);
            realTime.updateSwitchState(false);
        }
        if (SharedPreferenceUtil.get("noPush", "true").equals("true")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        if (MyApplication.mIsLogin) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (fixedTime.getSwitchState()) {
                        SharedPreferenceUtil.save("Time", Integer.parseInt(String.valueOf(spinner.getSelectedItem())) * 60 + "");
                        SharedPreferenceUtil.save("persion", spinner.getSelectedItemPosition() + "");
                        realTime.updateSwitchState(false);
                    } else {
                        SharedPreferenceUtil.save("Time", second);
                                 realTime.updateSwitchState(true);
                    }
                    Intent intent = new Intent(MyPushSettingsActivity.this, MessagePushService.class);
                    MyPushSettingsActivity.this.stopService(intent);//关闭服务
                    intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
                    MyPushSettingsActivity.this.startService(intent);//开启服务
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            fixedTime.setOnUISwitchDelegate(new CustomSwitchButton.OnUISwitchDelegate() {
                @Override
                public void onUISwitchDelegate(boolean isSwitchOn) {
                    if (fixedTime.getSwitchState()) {
                        SharedPreferenceUtil.save("Time", Integer.parseInt(String.valueOf(spinner.getSelectedItem())) * 60 + "");
                        SharedPreferenceUtil.save("persion", spinner.getSelectedItemPosition() + "");
                        realTime.updateSwitchState(false);
                    } else {
                        SharedPreferenceUtil.save("Time", second);
                        realTime.updateSwitchState(true);
                    }
                    Intent intent = new Intent(MyPushSettingsActivity.this, MessagePushService.class);
                    MyPushSettingsActivity.this.stopService(intent);//关闭服务
                    intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
                    MyPushSettingsActivity.this.startService(intent);//开启服务
                }
            });
            realTime.setOnUISwitchDelegate(new CustomSwitchButton.OnUISwitchDelegate() {
                @Override
                public void onUISwitchDelegate(boolean isSwitchOn) {
                    if (realTime.getSwitchState()) {
                        SharedPreferenceUtil.save("Time", second);
                        fixedTime.updateSwitchState(false);
                    } else {
                        SharedPreferenceUtil.save("Time", Integer.parseInt(String.valueOf(spinner.getSelectedItem())) * 60 + "");
                        fixedTime.updateSwitchState(true);
                    }
                    Intent intent = new Intent(MyPushSettingsActivity.this, MessagePushService.class);
                    MyPushSettingsActivity.this.stopService(intent);//关闭服务
                    intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
                    MyPushSettingsActivity.this.startService(intent);//开启服务
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        /**
                         * 如果服务关闭 则开启服务
                         */
//                        ActivityManager activityManager = (ActivityManager)
//                                MyPushSettingsActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
//                        List<ActivityManager.RunningServiceInfo> serviceList
//                                = activityManager.getRunningServices(30);
//                        String classNames = "";
//                        for (int i = 0; i < serviceList.size(); i++) {
//                            classNames = classNames + serviceList.get(i).service.getClassName() + ",";
//                        }
//                        if (!classNames.contains(MessagePushService.class.getName())) {
//                            Intent intent = new Intent(MyPushSettingsActivity.this, MessagePushService.class);
//                            intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
//                            MyPushSettingsActivity.this.startService(intent);//开启服务
//                        }
                        SharedPreferenceUtil.save("noPush", "true");
                    } else {
                        SharedPreferenceUtil.save("noPush", "false");
                    }
                }
            });
//        noPush.setOnUISwitchDelegate(new CustomSwitchButton.OnUISwitchDelegate() {
//            @Override
//            public void onUISwitchDelegate(boolean isSwitchOn) {
//                if (noPush.getSwitchState()) {
//                        Intent intent = new Intent(MyPushSettingsActivity.this, MessagePushService.class);
//                        intent.putExtra("time", SharedPreferenceUtil.get("Time", "1"));
//                        MyPushSettingsActivity.this.startService(intent);//开启服务
//                        SharedPreferenceUtil.save("noPush", "true");
//
//                } else {
//
//                    SharedPreferenceUtil.save("noPush", "false");
//                }
//            }
//        });
        }

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MyPushSettingsActivity.this);
    }

    private void initData() {
        //使用数组作为数据源
        final Integer arr[] = new Integer[]{1, 12, 24};
        // adpater对象
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(Integer.parseInt(SharedPreferenceUtil.get("persion", "0")), true);
    }


}
