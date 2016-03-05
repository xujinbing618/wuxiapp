package com.magus.enviroment.ep.activity.attention;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.fragment.attention.PreviewBarChartFragement;
import com.magus.enviroment.ep.fragment.attention.PreviewDiagramFragment;
import com.magus.enviroment.ep.fragment.attention.PreviewLineChartFragment;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.magusutils.DateUtil;
import java.util.ArrayList;
/**
 * 异常统计
 * Created by pau on 15/7/21.
 */
public class OverStatisticsActivity extends FragmentActivity {
    private static final String TAG = "OverStatisticsActivity";
    private ArrayList<Fragment> mViewPagerChildList = new ArrayList<Fragment>();
    private int mLastItemIndex = -1;//最后一次选中的fragment index
    private int defaultPageSize = 1;//默认显示页数

    private FragmentManager mFragmentManager;
    private PreviewBarChartFragement mChartFragment;//柱状图
    private PreviewDiagramFragment mDiagramFragment;//表格
    private PreviewLineChartFragment mLineFrgment;//折线图

    private static final int ID_CHART_FRAGMENT = 0;
    private static final int ID_DIAGRAM_FRAGMENT = 1;

    private Button btnChart;
    private Button btnDiagram;


//    private List<AttentionZone> mZoneNameList = new ArrayList<AttentionZone>();
//    private List<EnterpriseRateInfo> mRateList = new ArrayList<EnterpriseRateInfo>();

    private TextView txtChartTitle;//柱状图标题
    private TextView txtCurveTitle;//折线图标题

    private LinearLayout mFailPage2;
    private LinearLayout mLoadingPage2;

    private CustomActionBar mActionBar;
    private boolean isDestroy=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_statistics);
        initActionBar();
        init();
        initTopFragment();
        initLineChar();
    }

    private void init() {
        initView();
    }

    private void initView() {

        mFailPage2 = (LinearLayout) findViewById(R.id.ll_fail_page2);
        mLoadingPage2 = (LinearLayout) findViewById(R.id.ll_loading_now2);
        mFailPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFailPage2.setVisibility(View.GONE);
            }
        });
        mFragmentManager = getSupportFragmentManager();
        btnChart = (Button) findViewById(R.id.chart);
        btnDiagram = (Button) findViewById(R.id.diagram);
        btnChart.setOnClickListener(onClickListener);
        btnDiagram.setOnClickListener(onClickListener);
        txtChartTitle = (TextView) findViewById(R.id.chart_title);
        txtChartTitle.setText(DateUtil.getCurrentDay() + "异常处理率");
        txtCurveTitle = (TextView) findViewById(R.id.curve_title);
    }


    //初始化柱状图
    private void initTopFragment() {
        setSelected(btnChart);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mChartFragment = new PreviewBarChartFragement();
        transaction.replace(R.id.chart_content, mChartFragment);
        transaction.commitAllowingStateLoss();
    }


    //第二部分折线图就在这儿初始化
    private void initLineChar() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction1 = fragmentManager.beginTransaction();
        mLineFrgment = new PreviewLineChartFragment();
        transaction1.replace(R.id.line_content, mLineFrgment);
        transaction1.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        getReceiver();
        getLineTitleReceiver();
        getTableReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiver != null) {
            OverStatisticsActivity.this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    //图标切换监听事件
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chart:
                    changeFragment(ID_CHART_FRAGMENT);
                    break;
                case R.id.diagram:
                    changeFragment(ID_DIAGRAM_FRAGMENT);
                    break;
            }
        }
    };

    private void changeFragment(int id) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (id == ID_CHART_FRAGMENT) {
            if (mChartFragment == null) {
                mChartFragment = new PreviewBarChartFragement();
            }
            transaction.replace(R.id.chart_content, mChartFragment);
            setSelected(btnChart);
        } else if (id == ID_DIAGRAM_FRAGMENT) {
            if (mDiagramFragment == null) {
                mDiagramFragment = new PreviewDiagramFragment();
            }
            transaction.replace(R.id.chart_content, mDiagramFragment);
            setSelected(btnDiagram);
        }
        transaction.commit();
    }

    private void setSelected(Button button) {
        if (button == btnChart) {
            btnChart.setSelected(true);
            btnDiagram.setSelected(false);
        } else {
            btnChart.setSelected(false);
            btnDiagram.setSelected(true);
        }
    }

    /**
     * 广播接收更新刷新柱状图标题数据
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String date = intent.getStringExtra(PreviewBarChartFragement.KEY_DATE);
                    boolean tableshow=intent.getBooleanExtra("tableshow",false);
                    if (tableshow){
                       btnDiagram.setVisibility(View.VISIBLE);
                        btnChart.setVisibility(View.VISIBLE);
                    }else{
                        btnChart.setVisibility(View.GONE);
                        btnDiagram.setVisibility(View.GONE);
                    }
                    txtChartTitle.setText(date + "异常处理率");
                }
            };

    private void getReceiver() {
        IntentFilter filter = new IntentFilter(
                PreviewBarChartFragement.ACTION_CHANGE_DATE);
        this.registerReceiver(receiver, filter);
    }

    /**
     * 广播接收是否显示表格
     */
    private BroadcastReceiver tablereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean tableshow=intent.getBooleanExtra("tableshow",false);
            if (tableshow){
                System.out.println("*******///******" + tableshow);
                btnDiagram.setVisibility(View.VISIBLE);
                btnChart.setVisibility(View.VISIBLE);
            }else{
                System.out.println("************"+tableshow);
                btnDiagram.setVisibility(View.GONE);
                btnChart.setVisibility(View.GONE);
            }
        }
    };

    private void getTableReceiver() {
        IntentFilter filter = new IntentFilter(
                PreviewBarChartFragement.ACTION_CHANGE_TABLE);
        this.registerReceiver(tablereceiver, filter);
    }
    /**
     * 广播接收更新刷新折线图标题数据
     */
    private BroadcastReceiver lineReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String city = intent.getStringExtra(PreviewLineChartFragment.KEY_CITY);
            txtCurveTitle.setText(city + "异常处理率");
        }
    };

    private void getLineTitleReceiver() {
        IntentFilter filter = new IntentFilter(
                PreviewLineChartFragment.ACTION_CHANGE_DATE);
        this.registerReceiver(lineReceiver, filter);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(OverStatisticsActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
    @Override
    protected void onStop()
    {
        unregisterReceiver(lineReceiver);
        unregisterReceiver(tablereceiver);
        super.onStop();
    }
}
