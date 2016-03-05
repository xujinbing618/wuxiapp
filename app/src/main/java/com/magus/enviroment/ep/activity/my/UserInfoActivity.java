package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.User;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.service.MessagePushService;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.CustomItemLayout;
import com.magus.magusutils.SharedPreferenceUtil;

/**
 * 登陆界面
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/24-下午4:22.
 */
public class UserInfoActivity extends SwipeBaseActivity {
    private static String TAG = "UserInfoActivity";

    private CustomActionBar mActionBar;

    private Button login;//登陆
    private Button register;//注册

    private EditText username;
    private EditText password;

    private String usernameString = "";
    private String passwordString = "";

    private ProgressDialog mLoginDialog;

    private ImageView mHeadImage;

    private boolean isCancelLogin = false;//登入过程按取消按钮isCancelLogin为true，重新登陆后又变为false

    private CustomItemLayout mUserNameLayout;
    private CustomItemLayout mRealNameLayout;
    private CustomItemLayout mDescriptionLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);
        initActionBar();
        initView();
    }

    private void initView() {
        mUserNameLayout = (CustomItemLayout) findViewById(R.id.my_username);
        mRealNameLayout = (CustomItemLayout) findViewById(R.id.my_real_name);
        mDescriptionLayout = (CustomItemLayout) findViewById(R.id.my_description);
        mUserNameLayout.getRightText().setText(MyApplication.mUser.getRoleName());
        mRealNameLayout.getRightText().setText(MyApplication.mUser.getUserFullName());
        mDescriptionLayout.getRightText().setText(MyApplication.mUser.getDescription());

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(UserInfoActivity.this);
    }


    public void logout(View v) {
        switch (v.getId()) {

            case R.id.logout:
                Log.e(TAG, "volleyString");
                logout();
                break;
        }
    }

    /**
     * 打开dialog
     */
    private void openLoginDialog() {
        isCancelLogin = false;
        if (mLoginDialog == null) {
            mLoginDialog = new ProgressDialog(this);
            mLoginDialog.setMessage("正在退出，请稍后...");
            mLoginDialog.setCancelable(true);
            mLoginDialog.setCanceledOnTouchOutside(false);
        }
        mLoginDialog.show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mLoginDialog.dismiss();
    }

    /**
     * 关闭dialog
     */
    private void closeLoginDialog() {
        if (mLoginDialog != null && mLoginDialog.isShowing()) {
            mLoginDialog.dismiss();
        }
    }

    private View initDialogView() {
        View view = mLayoutInflater.inflate(R.layout.dialog_login_tips, null);
        return view;
    }

    private void logout() {
        SharedPreferenceUtil.remove(SpKeyConstant.USER_OBJECT);
        MyApplication.mUser = null;
        MyApplication.mUid = null;
        MyApplication.mIsLogin = false;
        MyApplication.mUserName = null;
        MyApplication.mRoleId = null;
        SharedPreferenceUtil.remove("gn_0001");
        SharedPreferenceUtil.remove("gn_0002");
        SharedPreferenceUtil.remove("gn_0003");
        SharedPreferenceUtil.remove("gn_0004");
        SharedPreferenceUtil.remove("gn_0005");
        SharedPreferenceUtil.remove("gn_0006");
        SharedPreferenceUtil.remove("gn_0007");
        SharedPreferenceUtil.remove("gn_0008");
        SharedPreferenceUtil.remove("gn_0009");

//        MyApplication.setGn_0001(false);
//        MyApplication.setGn_0002(false);
//        MyApplication.setGn_0003(false);
//        MyApplication.setGn_0004(false);
//        MyApplication.setGn_0005(false);
//        MyApplication.setGn_0006(false);
//        MyApplication.setGn_0007(false);
//        MyApplication.setGn_0008(false);
//        MyApplication.setGn_0009(false);
        Intent intent = new Intent(UserInfoActivity.this, MessagePushService.class);
        UserInfoActivity.this.stopService(intent);//关闭服务


        this.finish();
    }

    protected void parseResponse(String response) {
        closeLoginDialog();
        if (!isCancelLogin) {
            User.parseUserInfo(response, new RequestCallBack() {
                @Override
                public void onSuccess(Object object) {
                    User user = (User) object;
                    SharedPreferenceUtil.saveObject(SpKeyConstant.USER_OBJECT, getApplicationContext(), user);
                    UserInfoActivity.this.finish();
                }

                @Override
                public void onFailed(String errorMessage) {
                    super.onFailed(errorMessage);
                    Toast.makeText(UserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mLoginDialog != null && mLoginDialog.isShowing()) {
            mLoginDialog.dismiss();
            isCancelLogin = true;
        }
    }
}
