package com.magus.enviroment.ep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.activity.my.LoginActivity;
import com.magus.enviroment.ep.activity.my.MyAttentionActivity;
import com.magus.enviroment.ep.activity.my.MyMessageActivity;
import com.magus.enviroment.ep.activity.my.MyPushSettingsActivity;
import com.magus.enviroment.ep.activity.my.MyServiceCentreActivity;
import com.magus.enviroment.ep.activity.my.MySystemSettingsActivity;
import com.magus.enviroment.ep.activity.my.UserInfoActivity;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.User;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ui.CustomItemLayout;
import com.magus.enviroment.ui.UIUtil;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.SharedPreferenceUtil;


/**
 * 我的界面
 * Created by pau on 3/15/15.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private View mRootView;

    private CustomItemLayout[] mButtons;

    private int[] mButtonIds = {R.id.my_message, R.id.my_attention,
            R.id.my_service_centre, R.id.my_system_settings, R.id.my_push_settings};

    private TextView mNickName;
   // private TextView my_message_num;//我的消息数量
    private ImageView mLoginImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_me, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initView();
    }


    @Override
    public void onResume() {
        super.onResume();
        User user = ((User) SharedPreferenceUtil.getObject(SpKeyConstant.USER_OBJECT, ContextUtil.getContext()));
        if (user!=null){
            mNickName.setText(user.getUserFullName());
            // initMessage(MyApplication.getMessageNum());
        }else {
            mNickName.setText("请登录");
        }
    }

    private void initView() {
        mLoginImage = (ImageView) mRootView.findViewById(R.id.me_head_icon);
        mLoginImage.setOnClickListener(loginListener);
        mNickName = (TextView) mRootView.findViewById(R.id.me_nickname);
       // my_message_num=(TextView)mRootView.findViewById(R.id.my_message_num);
        //initMessage(MyApplication.getMessageNum());
        mButtons = new CustomItemLayout[mButtonIds.length];
        for (int i = 0; i < mButtonIds.length; i++) {
            mButtons[i] = (CustomItemLayout) mRootView.findViewById(mButtonIds[i]);
            mButtons[i].setOnClickListener(this);
        }

    }

    /**
     * 登陆事件监听
     */
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            if (MyApplication.mIsLogin) {
                intent.setClass(mActivity, UserInfoActivity.class);
            } else {
                UIUtil.toast(mActivity,"测试点击");
                intent.setClass(mActivity, LoginActivity.class);//登陆
            }
            startActivity(intent);
        }
    };

    /**
     * 初始化我的消息数量
     */
//    public void initMessage(int messageNum){
//        if(messageNum>0){
//            my_message_num.setVisibility(View.VISIBLE);
//            my_message_num.setText(messageNum+"");
//        }else{
//            my_message_num.setVisibility(View.GONE);
//        }
//    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.my_message:
                intent.setClass(mActivity, MyMessageActivity.class); //我的消息
                break;
            case R.id.my_attention:
                intent.setClass(mActivity, MyAttentionActivity.class);//我的关注
                break;
            case R.id.my_service_centre:
                intent.setClass(mActivity, MyServiceCentreActivity.class);//服务中心
                break;
            case R.id.my_system_settings:
                intent.setClass(mActivity, MySystemSettingsActivity.class);//系统设置
                break;
            case R.id.my_push_settings:
                intent.setClass(mActivity, MyPushSettingsActivity.class);//推送设置


        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
