package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.Toast;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AttentionAlarmOverAdapter;
import com.magus.enviroment.ep.bean.AlarmDetailInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SearchDialogOver;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;
/**
 * 超标预警
 * Created by pau on 15/7/21.
 */
public class AlarmOverActivity extends SwipeBackActivity{

    private static final String TAG = "AlarmOverActivity";
    private PullToRefreshListView mDetailListView;
    private CustomActionBar mActionBar;
    private ProgressDialog progressDialog;//进度条
    private List<AlarmDetailInfo> mAlarmDetailList = new ArrayList<AlarmDetailInfo>();
    private AttentionAlarmOverAdapter mAdapter;
    private int requestNum=20;//请求数量
    private int mtotalRecord=0;//数据总数
    private String mStartPosition = "0";//每次上拉加载后的请求位置
    private String firstStartRecord = "0";//第一次请求的位置
    private String companyId = "";//企业id
    private String source = "";//污染源
    private String startTime = "";//开始时间
    private String stopTime = "";//结束时间

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_over);
        progressDialog = UIUtil.initDialog(AlarmOverActivity.this, "正在加载，请稍后...");
        initActionBar();
        initView();
        initData();


    }
    private void initData() {
        sendRequest(firstStartRecord, false);
    }
    private void initView() {
        mDetailListView = (PullToRefreshListView) findViewById(R.id.attention_detail_list);
        mAdapter = new AttentionAlarmOverAdapter(this, mAlarmDetailList);
        mDetailListView.setAdapter(mAdapter);
        mDetailListView.setMode(PullToRefreshBase.Mode.BOTH);
      //  mDetailListView.setOnItemClickListener(this);
        mDetailListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                    pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("");
                    sendRequest(firstStartRecord,false);
                }
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                    if (mtotalRecord<=Integer.parseInt(mStartPosition)) {
                        pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("没有更多了");
                    }
                    sendRequest(mStartPosition,true);
                }
        });

    }
    /**
     * 请求
     *
     * @param position    请求位置
     * @param isUpRefresh 是否上啦刷新
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();

        final String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_OVERPRE_DETAIL + "?userId=" + MyApplication.mUid +"&start=" + position+"&startTime=" + startTime+"&endTime=" + stopTime+"&type=" +source +"&factoryId=" + companyId;//DateUtil.getCurrentDay()
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
            public void onResponse(String response) {

                parseResponse(response, isUpRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDetailListView.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AlarmOverActivity.this,"网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }) {

           // protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";
            // 重写parseNetworkResponse方法改变返回头参数解决乱码问题
            // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    String type = response.headers.get(HTTP.CONTENT_TYPE);
//                    if (type == null) {
//                        type = TYPE_UTF8_CHARSET;
//                        response.headers.put(HTTP.CONTENT_TYPE, type);
//                    } else if (!type.contains("UTF-8")) {
//                        type += ";" + TYPE_UTF8_CHARSET;
//                        response.headers.put(HTTP.CONTENT_TYPE, type);
//                    }
//                } catch (Exception e) {
//                }
//                return super.parseNetworkResponse(response);
//            }
        };

        final int maxMemory = (int) Runtime.getRuntime().maxMemory();

        // 设置是否缓存
        request.setShouldCache(true);
        //设置请求超时
        request.setRetryPolicy(new DefaultRetryPolicy(60*60*1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmDetailInfo.parseAlarmDetailInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord,int totalRecord) {

                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord=totalRecord;
                //如果上啦刷新则加入更多
                if (isUpRefresh) {
                    mAlarmDetailList.addAll((List<AlarmDetailInfo>) list);
                } else {
                    mAlarmDetailList = (List<AlarmDetailInfo>) list;
                    SharedPreferenceUtil.saveObject(SpKeyConstant.ALARM_OVER_LIST, ContextUtil.getContext(), mAlarmDetailList);
                }
                mAdapter.setList(mAlarmDetailList);
                mDetailListView.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailed() {
                super.onFailed();
                mDetailListView.onRefreshComplete();
                if(!isUpRefresh){
                    mAlarmDetailList.clear();
                    mAdapter.setList(mAlarmDetailList);
                    Toast.makeText(AlarmOverActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        }, this);
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e(TAG, "position=" + position);
//        AlarmDetailInfo info = mAlarmDetailList.get(position - 1);
//        Intent intent = new Intent();
//        intent.setClass(this, OverContentActivity.class);
//        intent.putExtra("pollSourceName", info.getPollSourceName());
//        intent.putExtra("pollutantName", info.getPollutantName());
//        intent.putExtra("fact_out_nd", info.getFact_out_nd());
//        startActivity(intent);
//    }

    private void ListAnim() {
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.list_view_animation);
        //得到一个LayoutAnimationController对象；
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        //设置控件显示的顺序；
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        //设置控件显示间隔时间；
        lac.setDelay(1);
        //为ListView设置LayoutAnimationController属性；
        mDetailListView.setLayoutAnimation(lac);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AlarmOverActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
        mActionBar.getRightImageView().setImageResource(R.mipmap.search);
        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchDialogOver dialog = new SearchDialogOver(AlarmOverActivity.this);
                dialog.show();
                dialog.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        companyId = dialog.getName();
                        source = dialog.getSource();
                        startTime = dialog.getStartTime();
                        stopTime = dialog.getStopTime();
                        sendRequest(firstStartRecord,false);
                    }
                });
            }
        });
    }
}
