package com.magus.enviroment.ep.activity.my;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.util.MyDownload;
import com.magus.enviroment.ep.util.MyDownloadHandler;
import com.magus.enviroment.ep.util.MyDownloadManager;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.CustomItemLayout;
import com.magus.enviroment.ui.UIUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 设置
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/23-上午11:15.
 */
public class MySystemSettingsActivity extends SwipeBaseActivity {
    private String TAG = "MySystemSettings";
    private CustomActionBar mActionBar;
    private CustomItemLayout checkForUpdate;//检查更新
    private AlertDialog mDialog;
    private MyDownloadManager mDownloadManager;
    private String downloadURL = "";//下载地址
    private String downloadURL2 = "http://35-android-eq.oss-cn-hangzhou.aliyuncs.com/MEQ.apk";
    private String downloadURL3 = "http://app.91.com/soft/Controller.ashx?Action=Download&id=10775978&m=61ccfce725b9ab3e17d729e17b013eba";
    private ProgressDialog progressDialog;

    private CustomItemLayout aboutUs;//关于我们
  //  private CustomItemLayout versionInfo;//新版本说明

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        initActionBar();
        initView();
    }

    private void initView() {
        mDownloadManager = MyDownloadManager.getInstance();
        aboutUs=(CustomItemLayout) findViewById(R.id.system_about_us);
        //versionInfo=(CustomItemLayout)findViewById(R.id.system_new_version_intro);
        checkForUpdate = (CustomItemLayout) findViewById(R.id.system_check_new_version);
        checkForUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = UIUtil.initDialog(MySystemSettingsActivity.this, "检查更新请稍后...");
                progressDialog.show();
                sendRequest();
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(MySystemSettingsActivity.this, MySystemSettingAboutUsActivity.class);
                startActivity(intent);
            }
        });
//        versionInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent();
//                intent.setClass(MySystemSettingsActivity.this, MySystemSettingVersionExplain.class);
//                startActivity(intent);
//            }
//        });

    }


    /**
     * 请求
     */
    private void sendRequest() {
        String url = URLConstant.HEAD_URL + URLConstant.URL_CHECK_FOR_UPDATE;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UIUtil.toast(MySystemSettingsActivity.this, "请求失败");

            }
        });

        MyApplication.getRequestQueue().add(request);

    }

    /**
     * 解析
     *
     * @param response
     */
    private void parseResponse(String response) {
        final MyApplication myApplication = (MyApplication) MySystemSettingsActivity.this.getApplication();
        String v = "";//服务器版本号
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("resultCode")) {
                String rc = jsonObject.getString("resultCode");
                if ("true".equals(rc)) {
                    if (jsonObject.has("resultEntity")) {
                        JSONObject object = new JSONObject(jsonObject.getString("resultEntity"));
                        if (object.has("version")) {
                            v = object.getString("version");
                            downloadURL = URLConstant.HEAD_URL+object.getString("fileUrl");
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (v.equals(myApplication.getVersionName())) {
                                UIUtil.toast(MySystemSettingsActivity.this, "您的版本已经是最新了");

                            } else {
                                mDialog = null;
                                mDialog = initDialog(v);
                                if (mDialog != null && !mDialog.isShowing()) {
                                    mDialog.show();
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MySystemSettingsActivity.this);
    }


    /**
     * 提示对话框
     */
    private AlertDialog initDialog(String version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("下载更新")
                .setMessage("最新版本为" + version + "，是否需要更新？\r\n ")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialog != null && mDialog.isShowing()) {
                            UIUtil.toast(MySystemSettingsActivity.this, "开始下载");
                            Log.e(TAG, downloadURL);
//                            mDownloadManager.download(downloadURL);
                            MyDownload myDownload = new MyDownload(downloadURL, new MyDownloadHandler() {
                                @Override
                                public void onSuccess(File file) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    openFile(file);
                                }

                                @Override
                                public void onFailed() {
                                    UIUtil.toast(MySystemSettingsActivity.this, "下载失败");
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            new Thread(myDownload) {
                            }.start();
                            mDialog.dismiss();
                        }

                        progressDialog = null;
                        progressDialog = UIUtil.initDialog(MySystemSettingsActivity.this, "正在下载，请稍后...");
                        progressDialog.show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                });
        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            // 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            String dlid = "" + intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            String[] names = downloadURL.split("/");
            String fileName = names[names.length - 1];
            mDownloadManager.queryDownloadStatus(fileName);
        }
    };


    // 打开APK程序代码
    public void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e(TAG, file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        this.startActivity(intent);
    }
}
