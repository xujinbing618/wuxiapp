package com.magus.enviroment.ep.activity.my;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.bean.User;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;
import com.magus.enviroment.ep.service.MessagePushService;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆界面
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/24-下午4:22.
 */
public class LoginActivity extends SwipeBaseActivity {
    private static String TAG = "LoginActivity";

    private CustomActionBar mActionBar;

    private Button login;//登陆

    private EditText etUsername;
    private EditText etPassword;

    private String usernameString = "";
    private String passwordString = "";
    private LinearLayout layout_configuration;//服务器地址配置界面
    private EditText edit_configuration;//服务器地址
    private Button btn_configuration;//提交服务器地址
    private ProgressDialog mLoginDialog;
    private Button configuration;

    private ImageView mHeadImage;

    private boolean isCancelLogin = false;//登入过程按取消按钮isCancelLogin为true，重新登陆后又变为false


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActionBar();
        initView();
    }

    private void initView() {
        login = (Button) findViewById(R.id.login);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        layout_configuration = (LinearLayout) findViewById(R.id.layout_configuration);
        btn_configuration=(Button)findViewById(R.id.btn_configuration);
        edit_configuration=(EditText)findViewById(R.id.edit_configuration);
        configuration=(Button)findViewById(R.id.configuration);
        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_configuration.setVisibility(View.VISIBLE);
            }
        });
        btn_configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(edit_configuration.getText().toString().trim())){
                    URLConstant.HEAD_URL="http://"+edit_configuration.getText().toString();//+"/ep4.1_phoneapp";
                    SharedPreferenceUtil.save("head_url", URLConstant.HEAD_URL);
                }
                layout_configuration.setVisibility(View.GONE);
            }
        });

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(LoginActivity.this);
    }


    public void loginRegister(View v) {
        switch (v.getId()) {
            case R.id.login:
                openLoginDialog();
                loginTest();
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
            mLoginDialog.setMessage("正在登入，请稍后...");
            mLoginDialog.setCancelable(true);
            mLoginDialog.setCanceledOnTouchOutside(false);
        }
        mLoginDialog.show();
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

//    private void login() {
//        String url = "http://192.168.199.180:8080/SingleServer/User/login";
//        HttpPostRequest postRequest = new HttpPostRequest();
//
//        mApp.getThreadPool().execute(postRequest);
//
//    }

    private void loginTest() {
        //192.168.199.180
        //192.168.5.252
        // String url = "http://192.168.199.180:8080/SingleServer/User/login";
        String loginUrl = URLConstant.HEAD_URL+URLConstant.URL_LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeLoginDialog();
                Toast.makeText(LoginActivity.this, "登入失败，请重试！", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", etUsername.getText().toString().trim());
                map.put("password", etPassword.getText().toString().trim());
                return map;
            }

        };

        MyApplication.getRequestQueue().add(stringRequest);
    }

    //解析返回数据
    protected void parseResponse(String response) {
        //关闭正在登陆进度条对话框
        closeLoginDialog();
        //判断是否取消登陆
        if (!isCancelLogin) {
            User.parseUserInfo(response, new RequestCallBack() {
                @Override
                public void onSuccess(Object object) {
                    User user = (User) object;
                    SharedPreferenceUtil.saveObject(SpKeyConstant.USER_OBJECT, getApplicationContext(), user);
                    findType();
                    /**
                     * 判断服务未启动 则启动服务
                     */
                    ActivityManager activityManager = (ActivityManager)
                            LoginActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningServiceInfo> serviceList
                            = activityManager.getRunningServices(30);
                    String classNames = "";
                    for (int i = 0; i < serviceList.size(); i++) {
                        classNames = classNames + serviceList.get(i).service.getClassName() + ",";
                    }
                    if (!classNames.contains(MessagePushService.class.getName()) && MyApplication.mIsLogin) {
                        Intent intent = new Intent(LoginActivity.this, MessagePushService.class);
                        if (SharedPreferenceUtil.get("Time", "10") != null) {
                            intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
                        } else {
                            intent.putExtra("time", "10");
                        }
                        LoginActivity.this.startService(intent);//开启服务
                    }
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailed(String errorMessage) {
                    super.onFailed(errorMessage);
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

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

    /**
     * 请求我的关注报警类型数据
     */
    private void findType() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // db.execSQL("DROP TABLE TABLE_ALARM_TYPE_INFO");
        db.execSQL("DELETE FROM TABLE_ATTENTION_ENTERPRISE");
        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_TYPE + "?userId=" + MyApplication.mUid;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseType(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getRequestQueue().add(request);

    }

    private void parseType(String response) {
        AlarmTypeInfo.parseAlarmTypeInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);
    }

}
