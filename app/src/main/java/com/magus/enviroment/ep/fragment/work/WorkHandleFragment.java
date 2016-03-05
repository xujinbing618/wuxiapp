package com.magus.enviroment.ep.fragment.work;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.adapter.WorkUncheckedAdapter;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;

import java.util.Arrays;
import java.util.List;

/**
 * 已处理
 * Created by pau
 * Packagename com.magus.enviroment.fragment.work
 * 2015-15/4/16-下午5:02.
 */
public class WorkHandleFragment extends BaseFragment {
    private View mRootView;

    private PullToRefreshListView mRefreshListView;

    private WorkUncheckedAdapter mAdapter;

    private String[] strs={"已处理","已处理","已处理","已处理","已处理","已处理","已处理","已处理","已处理"};

    private List<String> mList ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView==null){
            mRootView = inflater.inflate(R.layout.fragment_work_handle,null);

        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent!=null){
            parent.removeView(mRootView);
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        mRefreshListView= (PullToRefreshListView) mRootView.findViewById(R.id.pulltorefresh);
        mList = Arrays.asList(strs);
        mAdapter = new WorkUncheckedAdapter(mActivity,mList);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Toast.makeText(mActivity, "底部刷新", Toast.LENGTH_SHORT).show();
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Toast.makeText(mActivity, "下拉刷新", Toast.LENGTH_SHORT).show();

                new GetDataTask().execute();
            }
        });
    }


    //模拟网络加载数据的   异步请求类
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override

        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {

            }
            return strs;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mAdapter.notifyDataSetChanged();

            mRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}
