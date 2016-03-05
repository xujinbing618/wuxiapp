package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.bean.AttentionAbnormalReportDetail;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.magusutils.ContextUtil;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.ImageRequest;
import com.magus.volley.toolbox.StringRequest;

/**
 * 工况异常报告详情
 */
public class AttentionAbnormalDetailActivity extends SwipeBackActivity {

    public static final String KEY_ALARMLOGID = "KEY_ALARMLOGID";

    private CustomActionBar mActionBar;
    private int mAlarmLogId;

    private TextView mTitle; // 企业头
    private TextView mSourceName; // 企业名称
    private TextView mFacilityName; // 机组名称
    private TextView mAlarmType; // 异常名称
    private TextView mFacilityId; // 机组编号
    private TextView mAlarmExplain; // 异常分析
    private TextView mAlarmCause; // 异常原因
    private TextView mAlarmDate; // 时间
    private ImageView imageView;//图片
    private ProgressDialog progressDialog;//进度条

    private AttentionAbnormalReportDetail bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_abnormal_detail);
        progressDialog = UIUtil.initDialog(AttentionAbnormalDetailActivity.this, "正在加载，请稍后...");
        initActionBar();
        initData();
        initView();
    }

    private void initView() {
        this.mTitle = (TextView) findViewById(R.id.txt_title);
        this.mSourceName = (TextView) findViewById(R.id.txt_source);
        this.mFacilityId = (TextView) findViewById(R.id.txt_facilityid);
        this.mFacilityName = (TextView) findViewById(R.id.txt_facilityname);
        this.mAlarmType = (TextView) findViewById(R.id.txt_alarmtype);
        this.mAlarmCause = (TextView) findViewById(R.id.txt_alarmcause);
        this.mAlarmExplain = (TextView) findViewById(R.id.txt_alarmexplain);
        this.mAlarmDate = (TextView) findViewById(R.id.txt_alarmtime);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        sendRequest();
    }

    private void sendRequest() {
        progressDialog.show();

        String url =URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ABNORMAL_DETAIL + "?alarmLogId=" + mAlarmLogId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionAbnormalDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    /**
     * 根据url显示图片
     *
     * @param url
     * @param imageView
     */
    private void imageRequest(String url, final ImageView imageView) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setBackgroundDrawable(new BitmapDrawable(
                                AttentionAbnormalDetailActivity.this.getResources(), bitmap)
                );
            }
        }, width, height, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.faild));
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response) {
        AttentionAbnormalReportDetail.parseAbnormalInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(Object object) {
                super.onSuccess(object);
                bean = (AttentionAbnormalReportDetail) object;
                mAlarmCause.setText(bean.getAlarmCauses());
                mAlarmExplain.setText(bean.getAlarmExplain());
                mSourceName.setText(bean.getpollSourceName());
                mTitle.setText(bean.getpollSourceName());
                mAlarmDate.setText(bean.getalarmTime());
                mFacilityName.setText(bean.getfacilityName());
                mFacilityId.setText(bean.getfacilityBasId());
                mAlarmType.setText(bean.getalarmTypeName());
                imageRequest(URLConstant.HEAD_URL + URLConstant.FILE_NAME + bean.getAlarmImage(), imageView);
                System.out.println(""+bean.getAlarmImage());
                System.out.println("图片请求地址"+URLConstant.HEAD_URL+URLConstant.FILE_NAME+bean.getAlarmImage());
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionAbnormalDetailActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }, this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mAlarmLogId = intent.getIntExtra(KEY_ALARMLOGID, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bean != null) {
            mAlarmCause.setText(bean.getAlarmCauses());
            mAlarmExplain.setText(bean.getAlarmExplain());
            mSourceName.setText(bean.getpollSourceName());
            mTitle.setText(bean.getpollSourceName());
            mAlarmDate.setText(bean.getalarmTime());
            mFacilityName.setText(bean.getfacilityName());
            mFacilityId.setText(bean.getfacilityBasId());
            mAlarmType.setText(bean.getalarmTypeName());
        }
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionAbnormalDetailActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}