package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AttentionAbnormalReport;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SearchOverReportDialog;
import com.magus.enviroment.ui.UIUtil;
import com.magus.magusutils.DateUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 工况异常报告
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.attention
 * 2015-15/5/10-下午1:08.
 */
public class AttentionAbnormalReportActivity extends SwipeBaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "AttentionAbnormalReportActivity";
    private CustomActionBar mActionBar;
    private PullToRefreshListView listView;
    private LayoutInflater layoutInflater;
    private List<AttentionAbnormalReport> data = new ArrayList<AttentionAbnormalReport>();
    private MyAdapter mAdapter;
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private ProgressDialog progressDialog;//进度条
    private int requestNum=20;
    private int mtotalRecord=0;//数据总数
    private String dayString;//当前月份
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_abnormal_report);
        initActionBar();
        initView();
        sendRequest(firstStartRecord, false);
    }
    private void initView() {
        dayString = DateUtil.getBeforeMon();
        progressDialog = UIUtil.initDialog(AttentionAbnormalReportActivity.this, "正在加载，请稍后...");
        layoutInflater = LayoutInflater.from(this);
        listView = (PullToRefreshListView) findViewById(R.id.attention_abnormal_listview);
        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("");
                sendRequest(firstStartRecord, false);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mtotalRecord <= Integer.parseInt(mStartPosition)) {
                    refreshView.getLoadingLayoutProxy().setPullLabel("没有更多了");
                }
                sendRequest(mStartPosition, true);
            }
        });
    }

    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();
        String url = URLConstant.HEAD_URL+URLConstant.URL_ABNORMAL_REPORT + "?userId=" + MyApplication.mUid + "&start=" + position;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionAbnormalReportActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60*60*1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);
    }
    private void parseResponse(String response, final boolean isUpRefresh) {
        AttentionAbnormalReport.parseAbnormalInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list,int startRecord,int totalRecord) {
                super.onSuccess(list);

                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord=totalRecord;
                if (isUpRefresh) {
                    data.addAll((Collection<? extends AttentionAbnormalReport>) list);
                } else {
                    data = (List<AttentionAbnormalReport>) list;

                    SharedPreferenceUtil.saveObject(SpKeyConstant.ABNORMAL_REPORT_LIST,
                            AttentionAbnormalReportActivity.this, data);
                }
                mAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailed() {
                super.onFailed();
                if (progressDialog.isShowing()&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionAbnormalReportActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();
            }
        }, AttentionAbnormalReportActivity.this);

    }


    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionAbnormalReportActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
//        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
//        mActionBar.getRightImageView().setImageResource(R.mipmap.search);
//        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final SearchOverReportDialog dialog = new SearchOverReportDialog(AttentionAbnormalReportActivity.this);
//                dialog.show();
//                dialog.search_over_report_dialog_sbumit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dayString = dialog.getMonth();
//                        sendRequest(firstStartRecord, false);
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AttentionAbnormalReport info = data.get(position - 1);
        Intent intent = new Intent();
        intent.setClass(AttentionAbnormalReportActivity.this, AttentionAbnormalDetailActivity.class);
        intent.putExtra(AttentionAbnormalDetailActivity.KEY_ALARMLOGID, info.getAlarmLogId());
        startActivity(intent);

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_attention_abnormal_statistics, null);
                viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.attention_abnormal_layout);
                viewHolder.sourceName = (TextView) convertView.findViewById(R.id.area);
                viewHolder.alarmName = (TextView) convertView.findViewById(R.id.lost);
                viewHolder.facilityName = (TextView) convertView.findViewById(R.id.stop);
                viewHolder.facilityNum = (TextView) convertView.findViewById(R.id.over);
                viewHolder.alarmtime = (TextView) convertView.findViewById(R.id.stop2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position % 2 == 1) {
                viewHolder.layout.setBackgroundColor(AttentionAbnormalReportActivity.this.getResources().
                        getColor(R.color.me_message_background_gray));
            } else {
                viewHolder.layout.setBackgroundColor(AttentionAbnormalReportActivity.this.getResources().
                        getColor(R.color.me_message_background_white));
            }
            AttentionAbnormalReport obj = data.get(position);
            viewHolder.sourceName.setText(obj.getpollSourceName());
            viewHolder.alarmName.setText(obj.getalarmTypeName());
            viewHolder.facilityName.setText(obj.getfacilityName());
            viewHolder.facilityNum.setText(obj.getfacilityBasId());
            viewHolder.alarmtime.setText(obj.getalarmTime());

            return convertView;
        }
    }

    private class ViewHolder {
        LinearLayout layout;
        TextView sourceName;
        TextView alarmName;
        TextView facilityName;
        TextView facilityNum;
        TextView alarmtime;
    }
}
