package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmPicAdapter;
import com.magus.enviroment.ep.bean.AlarmPic;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
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
 * 自定义报警
 * Created by pau on 15/7/21.
 */
public class AlarmPicActivity extends SwipeBackActivity{
    private PullToRefreshListView mDetailListView;
    private CustomActionBar mActionBar;
    private ProgressDialog progressDialog;//进度条
    private List<AlarmPic> mAlarmDetailList = new ArrayList<AlarmPic>();
    private AlarmPicAdapter mAdapter;
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private int requestNum=20;
    private int mtotalRecord=0;//数据总数
//    private LinearLayout mFailPage;
//    private LinearLayout mLoadingPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pic);
        progressDialog = UIUtil.initDialog(AlarmPicActivity.this, "正在加载，请稍后...");
        initActionBar();
        initData();
        initView();
    }


    private void initData() {
//        mFailPage = (LinearLayout) findViewById(R.id.ll_fail_page);
//        mLoadingPage = (LinearLayout) findViewById(R.id.ll_loading_now);
//        mFailPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLoadingPage.setVisibility(View.VISIBLE);
//                mFailPage.setVisibility(View.GONE);
//                sendRequest(firstStartRecord, false);
//            }
//        });
        sendRequest(firstStartRecord, false);
        // List<AlarmDetailInfo> list = (List<AlarmDetailInfo>) SharedPreferenceUtil.getObject(SpKeyConstant.ALARM_DETAIL_LIST, ContextUtil.getContext());
//        if (list!=null) {
//            mAlarmDetailList = list;
//        }else {
//            mLoadingPage.setVisibility(View.VISIBLE);
//        }
    }

    private void initView() {
        mDetailListView = (PullToRefreshListView) findViewById(R.id.list_alarm_pic);
        mAdapter = new AlarmPicAdapter(this, mAlarmDetailList);
        mDetailListView.setAdapter(mAdapter);
        mDetailListView.setMode(PullToRefreshBase.Mode.BOTH);
     //   mDetailListView.setOnItemClickListener(this);
        mDetailListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("");
                sendRequest(firstStartRecord, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Log.e("AlarmPicActivity",mStartPosition);
                if (mtotalRecord<=Integer.parseInt(mStartPosition)) {

                    pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("没有更多了");
                }
                sendRequest(mStartPosition, true);
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
        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_PIC_LIST + "?userId=" + MyApplication.mUid+ "&start=" + position;

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
                Toast.makeText(AlarmPicActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60*60*1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }

    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmPic.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list,int startRecord,int totalRecord) {
                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord=totalRecord;
                //如果上啦刷新则加入更多
                if (isUpRefresh) {
                    mAlarmDetailList.addAll((List<AlarmPic>) list);
                } else {
                    mAlarmDetailList = (List<AlarmPic>) list;
                    // SharedPreferenceUtil.saveObject(SpKeyConstant.ALARM_DETAIL_LIST, ContextUtil.getContext(), mAlarmDetailList);
                }
                // mLoadingPage.setVisibility(View.GONE);
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
                    Toast.makeText(AlarmPicActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e(TAG, "position=" + position);
//        AlarmPic info = mAlarmDetailList.get(position - 1);
//        Intent intent = new Intent();
//        intent.setClass(this, PicContentActivity.class);
//        intent.putExtra("alarm_path", info.getAlarm_path());
//        intent.putExtra("alarm_desc", info.getAlarm_desc());
//        startActivity(intent);
//    }

//    private void ListAnim() {
//        //通过加载XML动画设置文件来创建一个Animation对象；
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.list_view_animation);
//        //得到一个LayoutAnimationController对象；
//        LayoutAnimationController lac = new LayoutAnimationController(animation);
//        //设置控件显示的顺序；
//        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        //设置控件显示间隔时间；
//        lac.setDelay(1);
//        //为ListView设置LayoutAnimationController属性；
//        mDetailListView.setLayoutAnimation(lac);
//    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AlarmPicActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
